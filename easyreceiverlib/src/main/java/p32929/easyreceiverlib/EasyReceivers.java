package p32929.easyreceiverlib;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.telephony.TelephonyManager;
import android.util.Log;

import java.util.Date;

public class EasyReceivers extends BroadcastReceiver {

    // TAG
    private String TAG = "EasyReceivers";

    // Call detection
    private static int lastState = TelephonyManager.CALL_STATE_IDLE;
    private static Date callStartTime;
    private static boolean isIncoming;
    private static String savedNumber;

    @Override
    public void onReceive(Context context, Intent intent) {

        if (intent.getAction() != null) {
            Log.d(TAG, "onReceive: Action: " + intent.getAction());

            // Screen & locks
            if (intent.getAction().equals(Intent.ACTION_SCREEN_ON) ||
                    intent.getAction().equals(Intent.ACTION_SCREEN_OFF) ||
                    intent.getAction().equals(Intent.ACTION_USER_PRESENT)) {

                if (intent.getAction().equals(Intent.ACTION_SCREEN_ON)) {
                    onScreenOn(context, intent);
                } else {
                    onScreenOff(context, intent);
                }

                if (GlobMeths.isDeviceLocked(context)) {
                    onDeviceLocked(context, intent);
                } else {
                    onDeviceUnlocked(context, intent);
                }
            }

            // Charger
            if (intent.getAction().equals(Intent.ACTION_POWER_CONNECTED)) {
                onChargerConnected(context, intent);
            } else if (intent.getAction().equals(Intent.ACTION_POWER_DISCONNECTED)) {
                onChargerDisconnected(context, intent);
            }

            // Boot
            if (intent.getAction().equals(Intent.ACTION_BOOT_COMPLETED) ||
                    intent.getAction().equals(Intent.ACTION_LOCKED_BOOT_COMPLETED)) {
                onBootCompleted(context, intent);
            }

            if (intent.getAction().equals(Intent.ACTION_HEADSET_PLUG)) {
                int state = intent.getIntExtra("state", -1);

                if (state == 1) {
                    onHeadphoneConnected(context, intent);
                }
                else if (state == 2) {
                    onHeadphoneDisconnected(context, intent);
                } else {
                    Log.d(TAG, "onReceive: Unknown Headphone connection state");
                }
            }

            // Volume
            if (intent.getAction().equals("android.media.VOLUME_CHANGED_ACTION")) {
                Log.d(TAG, "onReceive: Extras: " + intent.getExtras());
                onVolumeChanged(context, intent);
            }

            // Calls
            if (intent.getAction().equals("android.intent.action.NEW_OUTGOING_CALL")) {
                savedNumber = intent.getExtras().getString("android.intent.extra.PHONE_NUMBER");
            }

            if (intent.getExtras() != null) {
                String stateStr = intent.getExtras().getString(TelephonyManager.EXTRA_STATE);
                String number = intent.getExtras().getString(TelephonyManager.EXTRA_INCOMING_NUMBER);
                int state = 0;

                if (stateStr != null) {
                    if (stateStr.equals(TelephonyManager.EXTRA_STATE_IDLE)) {
                        state = TelephonyManager.CALL_STATE_IDLE;
                    } else if (stateStr.equals(TelephonyManager.EXTRA_STATE_OFFHOOK)) {
                        state = TelephonyManager.CALL_STATE_OFFHOOK;
                    } else if (stateStr.equals(TelephonyManager.EXTRA_STATE_RINGING)) {
                        state = TelephonyManager.CALL_STATE_RINGING;
                    }
                    onCallStateChanged(context, state, number, intent);
                }
            }
        }
    }

    // Protected methods :D To be overridden
    protected void onIncomingCallStarted(Context context, Intent intent, String number, Date start) {
        Log.d(TAG, "onIncomingCallStarted: ");
    }

    protected void onOutgoingCallStarted(Context context, Intent intent, String number, Date start) {
        Log.d(TAG, "onOutgoingCallStarted: ");
    }

    protected void onIncomingCallEnded(Context context, Intent intent, String number, Date start, Date end) {
        Log.d(TAG, "onIncomingCallEnded: ");
    }

    protected void onOutgoingCallEnded(Context context, Intent intent, String number, Date start, Date end) {
        Log.d(TAG, "onOutgoingCallEnded: ");
    }

    protected void onMissedCall(Context context, Intent intent, String number, Date start) {
        Log.d(TAG, "onMissedCall: ");
    }

    protected void onScreenOn(Context context, Intent intent) {
        Log.d(TAG, "onScreenOn: ");
    }

    protected void onScreenOff(Context context, Intent intent) {
        Log.d(TAG, "onScreenOff: ");
    }

    protected void onChargerConnected(Context context, Intent intent) {
        Log.d(TAG, "onChargerConnected: ");
    }

    protected void onChargerDisconnected(Context context, Intent intent) {
        Log.d(TAG, "onChargerDisconnected: ");
    }

    protected void onBootCompleted(Context context, Intent intent) {
        Log.d(TAG, "onBootCompleted: ");
    }

    protected void onHeadphoneConnected(Context context, Intent intent) {
        Log.d(TAG, "onHeadphoneConnected: ");
    }

    protected void onHeadphoneDisconnected(Context context, Intent intent) {
        Log.d(TAG, "onHeadphoneDisconnected: ");
    }

    protected void onDeviceLocked(Context context, Intent intent) {
        Log.d(TAG, "onDeviceLocked: ");
    }

    protected void onDeviceUnlocked(Context context, Intent intent) {
        Log.d(TAG, "onDeviceUnlocked: ");
    }

    protected void onVolumeChanged(Context context, Intent intent) {
        Log.d(TAG, "onVolumeChanged: ");
    }

    // Private methods
    private void onCallStateChanged(Context context, int state, String number, Intent intent) {
        if (lastState == state) {
            // No change, debounce extras
            return;
        }
        switch (state) {
            case TelephonyManager.CALL_STATE_RINGING:
                isIncoming = true;
                callStartTime = new Date();
                savedNumber = number;
                onIncomingCallStarted(context, intent, number, callStartTime);
                break;
            case TelephonyManager.CALL_STATE_OFFHOOK:
                //Transition of ringing->offhook are pickups of incoming calls.  Nothing done on them
                if (lastState != TelephonyManager.CALL_STATE_RINGING) {
                    isIncoming = false;
                    callStartTime = new Date();
                    onOutgoingCallStarted(context, intent, savedNumber, callStartTime);
                }
                break;
            case TelephonyManager.CALL_STATE_IDLE:
                //Went to idle-  this is the end of a call.  What type depends on previous state(s)
                if (lastState == TelephonyManager.CALL_STATE_RINGING) {
                    //Ring but no pickup-  a miss
                    onMissedCall(context, intent, savedNumber, callStartTime);
                } else if (isIncoming) {
                    onIncomingCallEnded(context, intent, savedNumber, callStartTime, new Date());
                } else {
                    onOutgoingCallEnded(context, intent, savedNumber, callStartTime, new Date());
                }
                break;
        }
        lastState = state;
    }
}
