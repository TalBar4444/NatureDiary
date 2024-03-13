package com.myapps.growdiary.activities;

import android.app.Activity;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageButton;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.LinearLayoutCompat;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.myapps.growdiary.R;

public class NoPlantsActivity  extends AppCompatActivity {
    private ExtendedFloatingActionButton noPlants_FAB_add;
    private MaterialButton bottom_NAV_gallery,bottom_NAV_plants,bottom_NAV_settings;

    @Override
    protected void onCreate(Bundle saveInstanceState){
        super.onCreate(saveInstanceState);
        setContentView(R.layout.activity_no_plants);

        findViews();
        bottomNavListener();
        bottom_NAV_plants.setIconTint(getColorStateList(R.color.primary));
        bottom_NAV_plants.setTextColor(getColorStateList(R.color.primary));

        noPlants_FAB_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                redirectActivity(NoPlantsActivity.this, AddActivity.class);
            }
        });
    }
    private void findViews() {
        bottom_NAV_gallery= findViewById(R.id.bottom_NAV_gallery);
        bottom_NAV_plants= findViewById(R.id.bottom_NAV_plants);
        bottom_NAV_settings= findViewById(R.id.bottom_NAV_settings);
        noPlants_FAB_add = findViewById(R.id.noPlants_FAB_add);
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



