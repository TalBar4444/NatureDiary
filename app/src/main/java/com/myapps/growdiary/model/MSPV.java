package com.myapps.growdiary.model;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.google.gson.Gson;

import java.util.ArrayList;

public class MSPV {
    private static final String SP_FILE_NAME = "SP_FILE_NAME";
    private static final String SP_ID_COUNTER = "SP_ID_COUNTER";
    private static final String SP_MODE = "SP_MODE";
    private SharedPreferences prefs = null;
    private static MSPV me;
    private String THIRTY = "THIRTY";
    private String COUNTER = "COUNTER";
    private String MODE = "MODE";
    private String TYPE = "TYPE";
    private String ADS = "ADS";

    private static int counter;
    //private boolean nightMode;
    private MSPV(Context context) {
        prefs = context.getApplicationContext().getSharedPreferences(SP_FILE_NAME, Context.MODE_PRIVATE);
    }
    public static void init(Context context) {
        if (me == null) {
            me = new MSPV(context);
        }
    }
    public static MSPV getMe() {
        return me;
    }
    public void saveString(String key, String value) {
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(key, value);
        editor.apply();
    }
    public String readString(String key, String def) {
        String value = prefs.getString(key, def);
        return value;
    }

    public void saveInt(String key, int value) {
        SharedPreferences.Editor editor = prefs.edit();
        editor.putInt(key, value);
        editor.apply();
    }

    public void saveCounter(int value) { //new
        SharedPreferences.Editor editor = prefs.edit();
        editor.putInt(SP_ID_COUNTER, value);
        editor.apply();
    }


    public int readInt(String key, int def) {
        int value = prefs.getInt(key, def);
        return value;
    }

    // Method to get the next available ID
    public int getNextID() {
        int counter = readInt(SP_ID_COUNTER, 0);
        int nextID = counter + 1;
        saveCounter(nextID);
        return nextID;
    }
    public int readCounter(String key, int def) { //new
        int value = prefs.getInt(key, def);
        return value;
    }
    public void saveThirtyPlants(PlantHub plantHub){
        SharedPreferences.Editor editor = prefs.edit();
        String json = new Gson().toJson(plantHub);
        this.getMe().saveString(THIRTY,json);
        editor.apply();
    }
    public PlantHub readThirtyPlants(){
        String json = this.getMe().readString(THIRTY,null);
        if(json == null) {
            saveThirtyPlants(new PlantHub().setPlants(new ArrayList<Plant>()));
            return readThirtyPlants();
        }
        return new Gson().fromJson(json, PlantHub.class);
    }

    public void saveDisplayMode(Boolean nightMode){
        SharedPreferences.Editor editor = prefs.edit();
        editor.putBoolean(SP_MODE, nightMode);
        editor.apply();
    }
    public Boolean readDisplayMode(){
        Boolean nightMode = prefs.getBoolean("SP_MODE",false);
        return nightMode;
    }

    public void saveUserType(Settings.UserType userType){
        SharedPreferences.Editor editor = prefs.edit();
        String json = new Gson().toJson(userType);
        this.getMe().saveString(TYPE,json);
        editor.apply();
    }

    public Settings.UserType readUserType(){
        String json = this.getMe().readString(TYPE, Settings.UserType.REGULAR.toString());
        return new Gson().fromJson(json, Settings.UserType.class);
    }

    public void saveAdsMode(Settings.AdsMode adsMode) {
        SharedPreferences.Editor editor = prefs.edit();
        String json = new Gson().toJson(adsMode);
        this.getMe().saveString(ADS,json);
        editor.apply();
    }

    public Settings.AdsMode readAdsMode(){
        String json = this.getMe().readString(ADS, Settings.AdsMode.ADS_ON.toString());
        return new Gson().fromJson(json, Settings.AdsMode.class);
    }
}


