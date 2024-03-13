package com.myapps.growdiary.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.google.android.material.textview.MaterialTextView;
import com.myapps.growdiary.adapters.PlantAdapter;
import com.myapps.growdiary.R;
import com.myapps.growdiary.interfaces.CallBack_List;
import com.myapps.growdiary.interfaces.RecyclerViewInterface;
import com.myapps.growdiary.model.MSPV;
import com.myapps.growdiary.model.Plant;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements RecyclerViewInterface {

    private Bundle bundle;
    public static final String KEY_BUNDLE = "KEY_BUNDLE";
    private ExtendedFloatingActionButton main_FAB_add;
    private RecyclerView main_LST_plants;
    private MaterialButton bottom_NAV_gallery,bottom_NAV_plants,bottom_NAV_settings;
    private AppBarLayout main_APPBAR_main;
    private CollapsingToolbarLayout main_APPBAR_toolbar;
    private MaterialTextView main_LBL_title;
    private ArrayList<Plant> plants;


    @Override
    protected void onCreate(Bundle saveInstanceState){
        super.onCreate(saveInstanceState);
        bundle = getIntent().getBundleExtra(PlantActivity.KEY_BUNDLE);
        setContentView(R.layout.activity_main);

        findViews();
        initList();
        bottomNavListener();
        bottom_NAV_plants.setIconTint(getColorStateList(R.color.primary));
        bottom_NAV_plants.setTextColor(getColorStateList(R.color.primary));

        main_FAB_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                redirectActivity(MainActivity.this, AddActivity.class);
            }
        });
    }
    private void findViews() {
        bottom_NAV_gallery=findViewById(R.id.bottom_NAV_gallery);
        bottom_NAV_plants=findViewById(R.id.bottom_NAV_plants);
        bottom_NAV_settings=findViewById(R.id.bottom_NAV_settings);
        main_LST_plants = findViewById(R.id.main_LST_plants);
        main_FAB_add = findViewById(R.id.main_FAB_add);
    }

    private void initList() {
        plants = MSPV.getMe().readThirtyPlants().getPlants();
        PlantAdapter plantAdapter = new PlantAdapter(plants,this);

        main_LST_plants.setLayoutManager(new LinearLayoutManager(this, RecyclerView.VERTICAL,false));
        main_LST_plants.setHasFixedSize(true);
        main_LST_plants.setAdapter(plantAdapter);
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
        bottom_NAV_settings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                redirectActivity(MainActivity.this, SettingsActivity.class);
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
    private void openPlantActivity(Plant plant){
        Intent intent = new Intent(this, PlantActivity.class);
        intent.putExtra("KEY_PLANT", plant);
        startActivity(intent);
        overridePendingTransition(0, 0);
    }
}
