package com.inop.aled;

import android.app.KeyguardManager;
import android.content.Context;
import android.hardware.display.DisplayManager;
import android.view.Display;

//https://github.com/photongh/Auto-AOD/blob/b49847815880dbf3abdaa2b1b2fa00aa3c382e2c/app/src/main/java/com/tjhost/autoaod/utils/SettingUtil.java#L35
public class LockedScreenManager {

    public static boolean isScreenOn(Context context) {
        DisplayManager powerManager = (DisplayManager) context.getSystemService(Context.DISPLAY_SERVICE);
        if (powerManager == null) return false;
        Display[] displays = powerManager.getDisplays();
        if (displays == null || displays.length == 0) return false;
        int state = displays[0].getState();
        if (state == Display.STATE_ON || state == Display.STATE_ON_SUSPEND)
            return true;
        return false;
    }

    public static boolean isScreenLocked(Context context) {
        KeyguardManager mKeyguardManager = (KeyguardManager) context.getSystemService(Context.KEYGUARD_SERVICE);
        if (mKeyguardManager == null) return false;
        return  mKeyguardManager.isKeyguardLocked();
    }

    public static boolean isScreenOnAndUnlocked(Context context) {
        return isScreenOn(context) && !isScreenLocked(context);
    }

    public static boolean isScreenOnAndLocked(Context context) {
        return isScreenOn(context) && isScreenLocked(context);
    }
}
