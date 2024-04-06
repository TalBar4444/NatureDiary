package com.myapps.growdiary.activities;

import static com.myapps.growdiary.model.PlantHub.getAllPlants;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.ads.AdView;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textview.MaterialTextView;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.myapps.growdiary.MySignal;
import com.myapps.growdiary.adapters.GalleryAdapter;
import com.myapps.growdiary.R;
import com.myapps.growdiary.model.Plant;

import java.util.ArrayList;

public class GalleryActivity extends AppCompatActivity {
    private MaterialTextView gallery_LBL_noPhotos;
    private MaterialButton bottom_NAV_gallery,bottom_NAV_plants,bottom_NAV_settings;
    private RecyclerView gallery_LST_photos;
    private GalleryAdapter adapter;
    private ArrayList<String> plantImageURIs;
    private Class<?> mainClass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gallery);
        findViews();
        bottomNavListener();
        bottom_NAV_gallery.setIconTint(getColorStateList(R.color.primary));
        bottom_NAV_gallery.setTextColor(getColorStateList(R.color.primary));

        plantImageURIs = new ArrayList<>(); // Initialize the list of image URIs
        ArrayList<Plant> plants = getAllPlants();

        if (plants.isEmpty()) {
            gallery_LST_photos.setVisibility(View.GONE);
            gallery_LBL_noPhotos.setVisibility(View.VISIBLE);
        } else {
            for (Plant plant : plants) { // Loop through the plants ArrayList and extract the image URIs
                String imageURI = plant.getImage();
                if (imageURI != null && !imageURI.isEmpty()) {
                    plantImageURIs.add(imageURI);
                }
            }
            if(!plantImageURIs.isEmpty()){
                gallery_LBL_noPhotos.setVisibility(View.GONE);
                gallery_LST_photos.setVisibility(View.VISIBLE);
            }
            else{
                gallery_LST_photos.setVisibility(View.GONE);
                gallery_LBL_noPhotos.setVisibility(View.VISIBLE);
            }
        }

        gallery_LST_photos.setLayoutManager(new GridLayoutManager(this, 3)); // 3 columns grid layout
        adapter = new GalleryAdapter(this, plantImageURIs);
        gallery_LST_photos.setAdapter(adapter);

        gallery_LST_photos.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                Log.d("pttt", "Gallery isFragmentOpen: " + GalleryAdapter.isFragmentOpen);
                if (GalleryAdapter.isFragmentOpen) {
                    switch (motionEvent.getAction()) {
                        case MotionEvent.ACTION_SCROLL:
                        case MotionEvent.ACTION_UP:
                        case MotionEvent.ACTION_CANCEL:
                            closeFragment(view); // Close the fragment when touched outside
                            GalleryAdapter.isFragmentOpen = false;
                            return true;
                    }
                }
                return false;
            }
        });
    }
    private void closeFragment(View view) {
        FragmentManager fragmentManager = ((FragmentActivity) GalleryActivity.this).getSupportFragmentManager();
        if (fragmentManager != null) {
            fragmentManager.popBackStack(); // Pop the fragment from the back stack to close it
        }
    }

    private void findViews() {
        bottom_NAV_gallery=findViewById(R.id.bottom_NAV_gallery);
        bottom_NAV_plants=findViewById(R.id.bottom_NAV_plants);
        bottom_NAV_settings=findViewById(R.id.bottom_NAV_settings);
        gallery_LBL_noPhotos = findViewById(R.id.gallery_LBL_noPhotos);
        gallery_LST_photos = findViewById(R.id.gallery_LST_photos);
    }

    private void bottomNavListener() {
        bottom_NAV_settings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                redirectActivity(GalleryActivity.this, SettingsActivity.class);
            }
        });
        bottom_NAV_plants.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getCurrentMain();
                redirectActivity(GalleryActivity.this, mainClass);
            }
        });
    }
    private void getCurrentMain() {
        if(getAllPlants().isEmpty())
            mainClass = NoPlantsActivity.class;
        else
            mainClass = MainActivity.class;
    }

    public void redirectActivity(Activity activity, Class<?> secondActivity){
        Intent intent = new Intent(activity,secondActivity);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        activity.startActivity(intent);
        overridePendingTransition(0, 0);
        activity.finish();
    }

}
