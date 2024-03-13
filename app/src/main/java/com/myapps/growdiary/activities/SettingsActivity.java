package com.myapps.growdiary.activities;

import static com.myapps.growdiary.model.PlantHub.getAllPlants;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.SwitchCompat;
import androidx.core.app.ActivityOptionsCompat;
import androidx.core.content.ContextCompat;
import androidx.core.view.WindowCompat;

import com.google.android.material.button.MaterialButton;
import com.myapps.growdiary.R;
import com.myapps.growdiary.model.Settings;

public class SettingsActivity extends AppCompatActivity {

    private MaterialButton bottom_NAV_gallery,bottom_NAV_plants,bottom_NAV_settings;
    private Class<?> mainClass;
    private SwitchCompat settings_SWCH_mode;
    Settings mySettings = new Settings();

    @Override
    protected void onCreate(Bundle saveInstanceState) {
        super.onCreate(saveInstanceState);
        setContentView(R.layout.activity_settings);

        findViews();
        bottomNavListener();
        bottom_NAV_settings.setIconTint(getColorStateList(R.color.primary));
        bottom_NAV_settings.setTextColor(getColorStateList(R.color.primary));

        setNightModeButton(mySettings);

        settings_SWCH_mode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                 if(mySettings.isNightMode()){
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                    mySettings.setIsNightMode(false);

                }
                else{
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                    mySettings.setIsNightMode(true);
                }
            }
        });
    }

    private void findViews() {
        settings_SWCH_mode = findViewById(R.id.settings_SWCH_mode);
        bottom_NAV_gallery=findViewById(R.id.bottom_NAV_gallery);
        bottom_NAV_plants=findViewById(R.id.bottom_NAV_plants);
        bottom_NAV_settings=findViewById(R.id.bottom_NAV_settings);
    }

    private void bottomNavListener() {
        bottom_NAV_gallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                redirectActivity(SettingsActivity.this, GalleryActivity.class);
            }
        });

        bottom_NAV_plants.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getCurrentMain();
                redirectActivity(SettingsActivity.this, mainClass);
            }
        });
    }
    private void setNightModeButton(Settings primerSettings){
        if(primerSettings.isNightMode()) {
            //settings_SWCH_mode.setChecked(true);
            settings_SWCH_mode.setThumbDrawable(ContextCompat.getDrawable(this, R.drawable.thumb_day_mode));
            settings_SWCH_mode.setTrackDrawable(ContextCompat.getDrawable(this, R.drawable.track_day_mode));

            //settings_SWCH_mode.set
        }
        else {
            //settings_SWCH_mode.setChecked(true);
            settings_SWCH_mode.setThumbDrawable(ContextCompat.getDrawable(this, R.drawable.thumb_night_mode));
            settings_SWCH_mode.setTrackDrawable(ContextCompat.getDrawable(this, R.drawable.track_night_mode));
        }
    }
    private void getCurrentMain() {
        if(getAllPlants().isEmpty())
            mainClass = NoPlantsActivity.class;
        else
            mainClass = MainActivity.class;
    }

    public void redirectActivity(Activity activity, Class<?> secondActivity){
        Intent intent = new Intent(activity,secondActivity);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        activity.startActivity(intent);
        overridePendingTransition(0, 0);
        activity.finish();
    }

}
