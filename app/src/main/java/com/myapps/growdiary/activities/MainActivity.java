package com.myapps.growdiary.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.ads.AdView;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;

import com.myapps.growdiary.MySignal;
import com.myapps.growdiary.MyUtils;
import com.myapps.growdiary.adapters.PlantAdapter;
import com.myapps.growdiary.R;
import com.myapps.growdiary.interfaces.CallBack_List;
import com.myapps.growdiary.interfaces.RecyclerViewInterface;
import com.myapps.growdiary.model.MSPV;
import com.myapps.growdiary.model.Plant;
import com.myapps.growdiary.model.Settings;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements RecyclerViewInterface {

    public static final String KEY_BUNDLE = "KEY_BUNDLE";
    private ExtendedFloatingActionButton main_FAB_add;
    private RecyclerView main_LST_plants;
    private MaterialButton bottom_NAV_gallery,bottom_NAV_plants,bottom_NAV_settings;
    private AdView adView_AD_banner;
    private ArrayList<Plant> plants;

    @Override
    protected void onCreate(Bundle saveInstanceState){
        super.onCreate(saveInstanceState);
        setContentView(R.layout.activity_main);
        findViews();
        initList();
        checkUserProgram();
        bottomNavListener();
        bottom_NAV_plants.setIconTint(getColorStateList(R.color.primary));
        bottom_NAV_plants.setTextColor(getColorStateList(R.color.primary));

        MyUtils.rateAppPopup(MainActivity.this); //rating dialog will pop up a few days after the user launched the app
        if (!MyUtils.isInternetConnected(this)) {
            MyUtils.noInternetPopup(MainActivity.this);
        }
        main_FAB_add.setOnClickListener(view -> redirectActivity(MainActivity.this, AddActivity.class));
    }

    private void findViews() {
        bottom_NAV_gallery=findViewById(R.id.bottom_NAV_gallery);
        bottom_NAV_plants=findViewById(R.id.bottom_NAV_plants);
        bottom_NAV_settings=findViewById(R.id.bottom_NAV_settings);
        main_LST_plants = findViewById(R.id.main_LST_plants);
        main_FAB_add = findViewById(R.id.main_FAB_add);
        adView_AD_banner = findViewById(R.id.adView_AD_banner);
    }

    private void initList() {
        plants = MSPV.getMe().readThirtyPlants().getPlants();
        PlantAdapter plantAdapter = new PlantAdapter(plants,this);

        main_LST_plants.setLayoutManager(new LinearLayoutManager(this, RecyclerView.VERTICAL,false));
        main_LST_plants.setHasFixedSize(true);
        main_LST_plants.setAdapter(plantAdapter);
    }

    private void checkUserProgram() {
        if (Settings.getType() == Settings.UserType.REGULAR && Settings.getAdsMode() == Settings.AdsMode.ADS_ON) { // REGULAR + ads = Yes Banner
            adView_AD_banner.setVisibility(View.VISIBLE);
            MySignal.getInstance().loadBanner(getWindowManager().getDefaultDisplay(), adView_AD_banner);
        } else
            adView_AD_banner.setVisibility(View.GONE);
    }

    private CallBack_List callBackList = new CallBack_List() {
        @Override
        public void plantSave(Plant plant) {
            openPlantActivity(plant);
        }
    };
    @Override
    public void onItemClick(int pos) {
        callBackList.plantSave(plants.get(pos));
    }

    private void bottomNavListener() {
        bottom_NAV_gallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                redirectActivity(MainActivity.this, GalleryActivity.class);
            }
        });
        bottom_NAV_settings.setOnClickListener(view -> redirectActivity(MainActivity.this, SettingsActivity.class));
    }
    public void redirectActivity(Activity activity, Class secondActivity){
        Intent intent = new Intent(activity,secondActivity);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        activity.startActivity(intent);
        overridePendingTransition(0, 0);
        activity.finish();
    }
    private void openPlantActivity(Plant plant){
        Intent intent = new Intent(this, PlantActivity.class);
        intent.putExtra("KEY_PLANT", plant);
        startActivity(intent);
        overridePendingTransition(0, 0);
    }
}
