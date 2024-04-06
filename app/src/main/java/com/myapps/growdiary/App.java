package com.myapps.growdiary;

import android.app.Application;
import android.content.Intent;
import android.util.Log;

import com.google.android.gms.ads.MobileAds;
import com.myapps.growdiary.MySignal;
import com.myapps.growdiary.activities.MainActivity;
import com.myapps.growdiary.activities.NoPlantsActivity;
import com.myapps.growdiary.model.Imager;
import com.myapps.growdiary.model.MSPV;
import com.myapps.growdiary.model.Settings;


public class App extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        MySignal.init(this);
        Imager.initHelper(this);
        MSPV.init(this);
        Settings mySettings = new Settings();
        MobileAds.initialize(this);
        mySettings.launchDisplayMode(MSPV.getMe().readDisplayMode(),MSPV.getMe().readUserType(),MSPV.getMe().readAdsMode());
        Intent intent;
        if (MSPV.getMe().readThirtyPlants().getPlants().size() == 0) {
            intent = new Intent(this, NoPlantsActivity.class);
        } else {
            intent = new Intent(this, MainActivity.class);
        }
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);

    }

}

