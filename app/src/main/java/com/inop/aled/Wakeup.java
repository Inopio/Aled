package com.inop.aled;

import android.util.Log;
import android.view.WindowManager;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;

public class Wakeup extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN |
                        WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD |
                        WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED |
                        WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON |
                        WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON,
                WindowManager.LayoutParams.FLAG_FULLSCREEN |
                        WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED |
                        WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON);

        LockedScreenManager lsm = new LockedScreenManager();
        if(lsm.isScreenLocked(getApplicationContext())) {
            setContentView(R.layout.test);
            Log.e("", "Phone locked");
        }
    }
}
