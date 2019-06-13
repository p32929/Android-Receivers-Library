package p32929.easyreceiverlibrary;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import java.util.Date;

import p32929.easyreceiverlib.EasyReceivers;

public class TestReceiver extends EasyReceivers {

    String TAG = this.getClass().getSimpleName();

    @Override
    protected void onVolumeChanged(Context context, Intent intent) {
        super.onVolumeChanged(context, intent);
        Log.d(TAG, "onVolumeChanged: ");
    }

    @Override
    protected void onDeviceLocked(Context context, Intent intent) {
        super.onDeviceLocked(context, intent);
        Log.d(TAG, "onDeviceLocked: ");
    }

    @Override
    protected void onDeviceUnlocked(Context context, Intent intent) {
        super.onDeviceUnlocked(context, intent);
        Log.d(TAG, "onDeviceUnlocked: ");
    }

    @Override
    protected void onScreenOn(Context context, Intent intent) {
        super.onScreenOn(context, intent);
        Log.d(TAG, "onScreenOn: ");
    }

    @Override
    protected void onScreenOff(Context context, Intent intent) {
        super.onScreenOff(context, intent);
        Log.d(TAG, "onScreenOff: ");
    }

    @Override
    protected void onIncomingCallStarted(Context context, Intent intent, String number, Date start) {
        super.onIncomingCallStarted(context, intent, number, start);
        Log.d(TAG, "onIncomingCallStarted: "+number);
    }

    @Override
    protected void onOutgoingCallStarted(Context context, Intent intent, String number, Date start) {
        super.onOutgoingCallStarted(context, intent, number, start);
        Log.d(TAG, "onOutgoingCallStarted: "+number);
    }

    @Override
    protected void onIncomingCallEnded(Context context, Intent intent, String number, Date start, Date end) {
        super.onIncomingCallEnded(context, intent, number, start, end);
        Log.d(TAG, "onIncomingCallEnded: "+number);
    }

    @Override
    protected void onOutgoingCallEnded(Context context, Intent intent, String number, Date start, Date end) {
        super.onOutgoingCallEnded(context, intent, number, start, end);
        Log.d(TAG, "onOutgoingCallEnded: "+number);
    }

    @Override
    protected void onMissedCall(Context context, Intent intent, String number, Date start) {
        super.onMissedCall(context, intent, number, start);
        Log.d(TAG, "onMissedCall: "+number);
    }

    @Override
    protected void onChargerConnected(Context context, Intent intent) {
        super.onChargerConnected(context, intent);
        Log.d(TAG, "onChargerConnected: ");
    }

    @Override
    protected void onChargerDisconnected(Context context, Intent intent) {
        super.onChargerDisconnected(context, intent);
        Log.d(TAG, "onChargerDisconnected: ");
    }

    @Override
    protected void onHeadphoneConnected(Context context, Intent intent) {
        super.onHeadphoneConnected(context, intent);
        Log.d(TAG, "onHeadphoneConnected: ");
    }

    @Override
    protected void onHeadphoneDisconnected(Context context, Intent intent) {
        super.onHeadphoneDisconnected(context, intent);
        Log.d(TAG, "onHeadphoneDisconnected: ");
    }
}
