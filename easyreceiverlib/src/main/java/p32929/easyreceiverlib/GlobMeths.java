package p32929.easyreceiverlib;

import android.app.KeyguardManager;
import android.content.Context;
import android.util.Log;

public class GlobMeths {
    private static KeyguardManager keyguardManager;
    public static boolean isDeviceLocked(Context context) {
        String TAG = "ImportantMethods";
        if (keyguardManager == null) {
            keyguardManager = ((KeyguardManager) context.getSystemService(Context.KEYGUARD_SERVICE));
        }
        if (keyguardManager.inKeyguardRestrictedInputMode()) {
            Log.d(TAG, "Locked");
            return true;
        } else {
            Log.d(TAG, "Unlocked");
            return false;
        }
    }
}
