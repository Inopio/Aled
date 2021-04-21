package com.inop.aled;

import android.app.KeyguardManager;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        lsm = new LockedScreenManager();
        if (savedInstanceState == null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .commit();
        }

        getSupportActionBar().hide();

        getWindow().setType(
                WindowManager.LayoutParams.TYPE_KEYGUARD_DIALOG);
        getWindow().addFlags( WindowManager.LayoutParams.FLAG_FULLSCREEN
                        | WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED
                        | WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON
                        | WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD
        );
        getWindow().addFlags( WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED |
                WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD |
                WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON |
                WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON |
                WindowManager.LayoutParams.FLAG_ALLOW_LOCK_WHILE_SCREEN_ON );
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);


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

        /*
        final GestureDetector gd = new GestureDetector(getApplicationContext(), new GestureDetector.SimpleOnGestureListener() {

            @Override
            public boolean onDoubleTap(MotionEvent e) {
                System.out.println("double tap");
                final Handler handler = new Handler();
                // NOT WORKING HAHAHHAHA :@
                TextView textView = new TextView(getApplicationContext());
                textView.setText("TEST TEST TEST :)");

                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.MATCH_PARENT
                );
                textView.setLayoutParams(params);
                LinearLayout layout = findViewById(R.id.test);
                layout.addView(textView);
                Toast.makeText(getApplicationContext(), "Double tap", Toast.LENGTH_LONG);
                setContentView(R.layout.test);
                // UNTIL HERE :^(

                handler.postDelayed(new Runnable() {
                    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
                    @Override
                    public void run() {
                        Intent i = new Intent(Intent.ACTION_MAIN);
                        i.addCategory(Intent.CATEGORY_HOME);
                        startActivity(i);
                        finish();
                        finishAndRemoveTask();
                    }
                }, 500);
                return true;
            }
        });*/


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