package com.myapps.growdiary.model;

import androidx.appcompat.app.AppCompatDelegate;

public class Settings {
    private static boolean nightMode;
    public enum UserType {
        REGULAR,
        PRO
    }
    private static UserType type;

    public enum AdsMode {
        ADS_ON,
        ADS_OFF
    }
    private static AdsMode adsMode;

    public Settings(){}

    public boolean isNightMode() {
        return nightMode;
    }

    public static UserType getType() {
        return type;
    }

    public Settings setType(UserType type) {
        this.type = type;
        MSPV.getMe().saveUserType(type);
        return this;
    }


    public static AdsMode getAdsMode() {
        return adsMode;
    }

    public Settings setAdsMode(AdsMode adsMode) {
        this.adsMode = adsMode;
        MSPV.getMe().saveAdsMode(adsMode);
        return this;
    }

    public void setIsNightMode(boolean nightMode) {
        this.nightMode = nightMode;
        MSPV.getMe().saveDisplayMode(nightMode);

    }

    public void launchDisplayMode(Boolean isNightMode, UserType userType, AdsMode adsMode){ // isNightMode = false -> dayMode, isNightMode = true -> nightMode
        this.nightMode = isNightMode;
        this.type = userType;
        this.adsMode = adsMode;
        if(isNightMode)
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        else
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
    }
}
