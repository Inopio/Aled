package com.inop.aled;

import android.app.KeyguardManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Configuration;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.PowerManager;
import android.renderscript.ScriptIntrinsicYuvToRGB;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import top.defaults.colorpicker.ColorObserver;
import top.defaults.colorpicker.ColorPickerPopup;
import top.defaults.colorpicker.ColorPickerView;

public class SettingsActivity extends AppCompatActivity implements ColorObserver {
    public ColorPickerView colorPickerView;
    public View squareView;
    public LockedScreenManager lsm;


    BroadcastReceiver mBroadcast = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Log.e("debug", "on receive");
            if (intent.getAction().equals(Intent.ACTION_SCREEN_ON)) {
                Log.e("debug", "Screen ON");
            } else if (intent.getAction().equals(Intent.ACTION_SCREEN_OFF)) {
                Log.e("debug", "Screen OFF");
                Intent intentActivity = new Intent(context, Wakeup.class);
                PendingIntent pendingIntent = PendingIntent.getActivity(context,
                        12345, intent, PendingIntent.FLAG_CANCEL_CURRENT);
                context.startActivity(intentActivity);
            }

        }
    };

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        lsm = new LockedScreenManager();

        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN |
                        WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED |
                        WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON,
                WindowManager.LayoutParams.FLAG_FULLSCREEN |
                        WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED |
                        WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON);

        if (savedInstanceState == null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .commit();
        }

        getSupportActionBar().hide();

        switch (getResources().getConfiguration().uiMode & Configuration.UI_MODE_NIGHT_MASK) {
            case Configuration.UI_MODE_NIGHT_YES:
                //Toast.makeText(this.getApplicationContext(),"Dark mode on", Toast.LENGTH_LONG).show();
                setContentView(R.layout.main_page_dark);
                squareView = (View)findViewById(R.id.colorDisplay);
                squareView.setBackgroundColor(Color.parseColor("#FF0000"));
                break;

            case Configuration.UI_MODE_NIGHT_NO:
                //Toast.makeText(this.getApplicationContext(),"Light mode on", Toast.LENGTH_LONG).show();
                colorPickerView.setInitialColor(0x11111111);
                setContentView(R.layout.main_page_light);
                break;
            default:
                break;
        }

        registerReceiver(mBroadcast, new IntentFilter(Intent.ACTION_SCREEN_OFF));

        Switch s = (Switch) findViewById(R.id.switch2);
        Boolean switchState = s.isChecked();
        if (switchState) {
            setLed();
        }

        final Button button = findViewById(R.id.button4);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                new ColorPickerPopup.Builder(getApplicationContext())
                        .initialColor(Color.RED) // Set initial color
                        .enableBrightness(true) // Enable brightness slider or not
                        .enableAlpha(true) // Enable alpha slider or not
                        .okTitle("Valider")
                        .cancelTitle("Retour")
                        .showIndicator(true)
                        .showValue(true)
                        .build()
                        .show(v, new ColorPickerPopup.ColorPickerObserver() {
                            @Override
                            public void onColorPicked(int color) {
                                v.setBackgroundColor(color);
                                squareView.setBackgroundColor(color);
                            }
                        });
            }
        });
    }

    final GestureDetector gestureDetector = new GestureDetector(new GestureDetector.SimpleOnGestureListener() {
        public void onLongPress(MotionEvent e) {
            if(lsm.isScreenLocked(getApplicationContext())) {
                Toast.makeText(getApplicationContext(), "Long press", Toast.LENGTH_LONG).show();
                setContentView(R.layout.test);
                Log.e("", "Longpress detected");
            }
        }

        public boolean onDoubleTap(MotionEvent e) {
            if(lsm.isScreenLocked(getApplicationContext())) {
                Toast.makeText(getApplicationContext(), "Double tap", Toast.LENGTH_LONG).show();
                setContentView(R.layout.test);
                Log.e("", "Longpress detected");
            }
            return true;
        }
    });

    @RequiresApi(api = Build.VERSION_CODES.M)
    public boolean onTouchEvent(MotionEvent event) {
        if(lsm.isScreenLocked(getApplicationContext())) {
            KeyguardManager.KeyguardLock keyguardLock =((KeyguardManager) getApplicationContext().getSystemService(getApplicationContext().KEYGUARD_SERVICE)).newKeyguardLock("TAG");
            keyguardLock.disableKeyguard();
            Toast.makeText(getApplicationContext(), "Single tap in locked screen", Toast.LENGTH_LONG).show();
            setContentView(R.layout.test);
        }
        return gestureDetector.onTouchEvent(event);
    };

    public void setLed() {

    }

    @Override
    public void onColor(int color, boolean fromUser, boolean shouldPropagate) {

    }
}