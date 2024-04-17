package com.myapps.growdiary.activities;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.LinearLayoutCompat;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textview.MaterialTextView;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.myapps.growdiary.R;
import com.myapps.growdiary.model.Plant;
import com.myapps.growdiary.model.Settings;

import java.util.ArrayList;

public class ShopActivity extends AppCompatActivity{
    private MaterialTextView shop_LBL_back;
    private MaterialButton shop_BTN_pro;
    private MaterialButton shop_BTN_ads;
    Settings mySettings = new Settings();

    private FirebaseAnalytics mFirebaseAnalytics;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shop);
        findViews();
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);
        setProMode(mySettings);
        firebaseShopEvent();

        shop_LBL_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
                overridePendingTransition(0, 0);
            }
        });

        shop_BTN_pro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(mySettings.getType() == Settings.UserType.REGULAR) {
                    mySettings.setType(Settings.UserType.PRO);
                    shop_BTN_pro.setText("On");
                }
                else {
                    mySettings.setType(Settings.UserType.REGULAR);
                    shop_BTN_pro.setText("Off");
                }
            }
        });

        shop_BTN_ads.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(mySettings.getAdsMode() == Settings.AdsMode.ADS_ON) {
                    mySettings.setAdsMode(Settings.AdsMode.ADS_OFF);
                    shop_BTN_ads.setText("On");
                }
                else {
                    mySettings.setAdsMode(Settings.AdsMode.ADS_ON);
                    shop_BTN_ads.setText("Off");
                }
            }
        });
    }

    private void findViews() {
        shop_LBL_back = findViewById(R.id.shop_LBL_back);
        shop_BTN_pro = findViewById(R.id.shop_BTN_pro);
        shop_BTN_ads= findViewById(R.id.shop_BTN_ads);
    }

    private void setProMode(Settings mySettings) {
        if(Settings.getType() == Settings.UserType.PRO)
            shop_BTN_pro.setText("On");
        else
            shop_BTN_pro.setText("Off");

        if(Settings.getAdsMode() == Settings.AdsMode.ADS_OFF)
            shop_BTN_ads.setText("On");
        else
            shop_BTN_ads.setText("Off");
    }

    private void firebaseShopEvent() { //compare to amount of edit
        Intent intent = getIntent();
        String activity = intent.getStringExtra("activity");
        Log.d("pttt",""+activity);
        Bundle bundle = new Bundle();
        bundle.putString(FirebaseAnalytics.Param.ITEM_ID, "buttonClicked");
        bundle.putString(FirebaseAnalytics.Param.ITEM_NAME, "shop button Clicked");
        bundle.putString(FirebaseAnalytics.Param.CONTENT_TYPE, "source of shop activity"); //usability check
        bundle.putString(FirebaseAnalytics.Param.CONTENT, activity ); //usability check
        mFirebaseAnalytics.logEvent(FirebaseAnalytics.Event.PURCHASE, bundle);
    }
}
