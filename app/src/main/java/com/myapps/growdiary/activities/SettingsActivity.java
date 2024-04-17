package com.myapps.growdiary.activities;

import static com.myapps.growdiary.model.PlantHub.getAllPlants;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.SwitchCompat;
import androidx.core.content.ContextCompat;

import com.google.android.gms.ads.AdView;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textview.MaterialTextView;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.myapps.growdiary.MySignal;
import com.myapps.growdiary.MyUtils;
import com.myapps.growdiary.R;
import com.myapps.growdiary.model.Settings;

import hotchemi.android.rate.AppRate;

public class SettingsActivity extends AppCompatActivity {

    private MaterialButton bottom_NAV_gallery,bottom_NAV_plants,bottom_NAV_settings;
    private MaterialTextView settings_LBL_goPro, settings_LBL_share, settings_LBL_rate, settings_LBL_newVersion, settings_LBL_aboutUs,  settings_LBL_privacyPolicy;
    private SwitchCompat settings_SWCH_mode;
    private AdView adView_AD_banner;
    private Class<?> mainClass;
    private FirebaseAnalytics mFirebaseAnalytics;
    Settings mySettings = new Settings();

    @Override
    protected void onCreate(Bundle saveInstanceState) {
        super.onCreate(saveInstanceState);
        setContentView(R.layout.activity_settings);
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);
        findViews();

        bottomNavListener();
        bottom_NAV_settings.setIconTint(getColorStateList(R.color.primary));
        bottom_NAV_settings.setTextColor(getColorStateList(R.color.primary));
        setNightModeButton(mySettings);

        settings_LBL_goPro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openShopActivity();
            }
        });

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

        settings_LBL_share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle bundle = new Bundle();
                bundle.putString(FirebaseAnalytics.Param.ITEM_ID, "buttonClicked");
                bundle.putString(FirebaseAnalytics.Param.ITEM_ID, "Share Button Clicked");
                bundle.putString(FirebaseAnalytics.Param.CONTENT_TYPE, "share app");
                mFirebaseAnalytics.logEvent(FirebaseAnalytics.Event.SHARE, bundle);
                MyUtils.shareAppPopUp(SettingsActivity.this);
            }
        });

        settings_LBL_rate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AppRate.with(SettingsActivity.this)
                        .setTitle(R.string.rate_dialog_title)
                        .setMessage(R.string.rate_dialog_message)
                        .setTextNever(R.string.rate_dialog_cancel)
                        .setTextLater(R.string.rate_dialog_neutral)
                        .setTextRateNow(R.string.rate_dialog_ok)
                        .showRateDialog(SettingsActivity.this);
                AppRate.with(SettingsActivity.this).clearAgreeShowDialog();

            }
        });

        settings_LBL_newVersion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MyUtils.newVersionPopup(SettingsActivity.this);
            }
        });

        settings_LBL_aboutUs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MyUtils.openHtmlTextDialog(SettingsActivity.this, "terms_of_use.html");
            }
        });

        settings_LBL_privacyPolicy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MyUtils.openHtmlTextDialog(SettingsActivity.this, "privacy_policy.html");
            }
        });
    }
    private void findViews() {
        settings_LBL_goPro = findViewById(R.id.settings_LBL_goPro);
        settings_SWCH_mode = findViewById(R.id.settings_SWCH_mode);
        settings_LBL_share = findViewById(R.id.settings_LBL_share);
        settings_LBL_rate = findViewById(R.id.settings_LBL_rate);
        settings_LBL_newVersion = findViewById(R.id.settings_LBL_newVersion);
        settings_LBL_aboutUs = findViewById(R.id.settings_LBL_aboutUs);
        settings_LBL_privacyPolicy = findViewById(R.id.settings_LBL_privacyPolicy);

        bottom_NAV_gallery = findViewById(R.id.bottom_NAV_gallery);
        bottom_NAV_plants = findViewById(R.id.bottom_NAV_plants);
        bottom_NAV_settings = findViewById(R.id.bottom_NAV_settings);

        adView_AD_banner = findViewById(R.id.adView_AD_banner);

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
            settings_SWCH_mode.setThumbDrawable(ContextCompat.getDrawable(this, R.drawable.thumb_day_mode));
            settings_SWCH_mode.setTrackDrawable(ContextCompat.getDrawable(this, R.drawable.track_day_mode));
        }
        else {
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

    private void redirectActivity(Activity activity, Class<?> secondActivity){
        Intent intent = new Intent(activity,secondActivity);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        activity.startActivity(intent);
        overridePendingTransition(0, 0);
        activity.finish();
    }

    private void openShopActivity(){
        Intent intent = new Intent(this, ShopActivity.class);
        intent.putExtra("activity",this.getClass().getSimpleName());
        startActivity(intent);
        overridePendingTransition(0, 0);
    }

}
