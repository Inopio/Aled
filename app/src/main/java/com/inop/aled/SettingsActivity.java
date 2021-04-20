package com.inop.aled;

import android.content.res.Configuration;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import top.defaults.colorpicker.ColorObserver;
import top.defaults.colorpicker.ColorPickerPopup;
import top.defaults.colorpicker.ColorPickerView;

public class SettingsActivity extends AppCompatActivity implements ColorObserver {
    public ColorPickerView colorPickerView;
    public View squareView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (savedInstanceState == null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    //.replace(R.id.settings, new SettingsFragment())
                    .commit();
        }
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

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

        final Button button = findViewById(R.id.button4);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                new ColorPickerPopup.Builder(getApplicationContext())
                        .initialColor(Color.RED) // Set initial color
                        .enableBrightness(true) // Enable brightness slider or not
                        .enableAlpha(true) // Enable alpha slider or not
                        .okTitle("Choose")
                        .cancelTitle("Cancel")
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

    @Override
    public void onColor(int color, boolean fromUser, boolean shouldPropagate) {

    }
}