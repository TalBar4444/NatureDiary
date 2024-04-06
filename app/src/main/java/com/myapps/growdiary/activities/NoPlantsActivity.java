package com.myapps.growdiary.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.button.MaterialButton;
import com.myapps.growdiary.MyUtils;
import com.myapps.growdiary.R;

public class NoPlantsActivity  extends AppCompatActivity {
    private MaterialButton bottom_NAV_gallery,bottom_NAV_plants,bottom_NAV_settings;
    private MaterialButton noPlants_BTN_add;
    private static boolean hasShown = false;

    @Override
    protected void onCreate(Bundle saveInstanceState){
        super.onCreate(saveInstanceState);
        setContentView(R.layout.activity_no_plants);

        findViews();
        bottomNavListener();
        bottom_NAV_plants.setIconTint(getColorStateList(R.color.primary));
        bottom_NAV_plants.setTextColor(getColorStateList(R.color.primary));

        showFirstPrivacyPolicy();

        noPlants_BTN_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                redirectActivity(NoPlantsActivity.this, AddActivity.class);
            }
        });
    }

    private void showFirstPrivacyPolicy() {
        if(!hasShown){
            MyUtils.openHtmlTextDialog(this, "privacy_policy.html");
            hasShown = true;
        }

    }

    private void findViews() {
        bottom_NAV_gallery= findViewById(R.id.bottom_NAV_gallery);
        bottom_NAV_plants= findViewById(R.id.bottom_NAV_plants);
        bottom_NAV_settings= findViewById(R.id.bottom_NAV_settings);
        noPlants_BTN_add = findViewById(R.id.noPlants_BTN_add);
    }

    private void bottomNavListener() {
        bottom_NAV_gallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                redirectActivity(NoPlantsActivity.this, GalleryActivity.class);
            }
        });
        bottom_NAV_settings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                redirectActivity(NoPlantsActivity.this, SettingsActivity.class);
            }
        });

    }
    public void redirectActivity(Activity activity, Class secondActivity){
        Intent intent = new Intent(activity,secondActivity);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        activity.startActivity(intent);
        overridePendingTransition(0, 0);
        activity.finish();
    }

}



