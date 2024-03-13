package com.myapps.growdiary.model;

import androidx.appcompat.app.AppCompatDelegate;

public class Settings {
    private static boolean nightMode;
    public Settings(){}

    public boolean isNightMode() {
        return nightMode;
    }

    public void setIsNightMode(boolean nightMode) {
        this.nightMode = nightMode;
        MSPV.getMe().saveDisplayMode(nightMode);

    }

    public void launchDisplayMode(Boolean isNightMode){ // isNightMode = false -> dayMode, isNightMode = true -> nightMode
        this.nightMode = isNightMode;
        if(isNightMode)
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        else
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
    }
}
