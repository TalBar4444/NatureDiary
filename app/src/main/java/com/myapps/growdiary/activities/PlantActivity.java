package com.myapps.growdiary.activities;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.appcompat.widget.LinearLayoutCompat;

import com.google.android.material.textview.MaterialTextView;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.myapps.growdiary.R;
import com.myapps.growdiary.model.Plant;

import de.hdodenhof.circleimageview.CircleImageView;

public class PlantActivity extends AppCompatActivity {

    private Bundle bundle;
    public static final String KEY_BUNDLE = "KEY_BUNDLE";
    public static final String KEY_IS_CHANGED = "KEY_IS_CHANGED";
    private MaterialTextView plant_LBL_back,plant_LBL_edit, plant_EDT_status,plant_EDT_notes;
    private CircleImageView plant_IMG_plantImage;
    private AppCompatTextView plant_LBL_title, plant_LBL_lastUpdate, plant_LBL_plantingDate,plant_LBL_watering, plant_LBL_observationDate,plant_LBL_Location;
    private LinearLayoutCompat plant_LL_homeGardener,plant_LL_wildDiscovery,plant_LL_notes;
    private Plant receivedPlant;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_plant);
        bundle = getIntent().getBundleExtra(EditActivity.KEY_BUNDLE);
        receivedPlant = (Plant) getIntent().getSerializableExtra("KEY_PLANT");
        findViews();
        getData();

        plant_LBL_back.setOnClickListener(view -> backToMain());

        plant_LBL_edit.setOnClickListener(view -> {
            openEditActivity();
        });
    }

    private void findViews() {
        plant_LBL_back = findViewById(R.id.plant_LBL_back);
        plant_LBL_edit = findViewById(R.id.plant_LBL_edit);
        plant_IMG_plantImage = findViewById(R.id.plant_IMG_plantImage);
        plant_LBL_title = findViewById(R.id.plant_LBL_title);
        plant_LBL_lastUpdate = findViewById(R.id.plant_LBL_lastUpdate);
        plant_EDT_status = findViewById(R.id.plant_EDT_status);

        plant_LL_homeGardener = findViewById(R.id.plant_LL_homeGardener);
        plant_LBL_plantingDate = findViewById(R.id.plant_LBL_plantingDate);
        plant_LBL_watering = findViewById(R.id.plant_LBL_watering);

        plant_LL_wildDiscovery = findViewById(R.id.plant_LL_wildDiscovery);
        plant_LBL_observationDate = findViewById(R.id.plant_LBL_observationDate);
        plant_LBL_Location = findViewById(R.id.plant_LBL_Location);

        plant_LL_notes = findViewById(R.id.plant_LL_notes);
        plant_EDT_notes = findViewById(R.id.plant_EDT_notes);
    }

    private void getData() {
        if (receivedPlant.getImage() == null)
            plant_IMG_plantImage.setImageResource(R.drawable.noplantimagebackground);
        else
            plant_IMG_plantImage.setImageURI(Uri.parse(receivedPlant.getImage()));
        plant_LBL_title.setText(receivedPlant.getTitle());
        plant_LBL_lastUpdate.setText(String.format("Last Update: %s", receivedPlant.getLastUpdate()));
        plant_EDT_status.setText(receivedPlant.getStatus());
        if (receivedPlant.getType() == Plant.DocumentationType.HOME_GARDENER) {
            plant_LL_homeGardener.setVisibility(View.VISIBLE);
            if (receivedPlant.getPlantingDate() == null)
                plant_LBL_plantingDate.setText("-");
            else
                plant_LBL_plantingDate.setText(receivedPlant.getPlantingDate());
            if (receivedPlant.getWateringFrequency() == null)
                plant_LBL_watering.setText("-");
            else
                plant_LBL_watering.setText(receivedPlant.getWateringFrequency());
        } else {
            plant_LL_homeGardener.setVisibility(View.GONE);
            plant_LL_wildDiscovery.setVisibility(View.VISIBLE);
            if (receivedPlant.getObservationDate() == null)
                plant_LBL_observationDate.setText("-");
            else
                plant_LBL_observationDate.setText(receivedPlant.getObservationDate());

            if (receivedPlant.getObservationLocation() == null)
                plant_LBL_Location.setText("-");
            else
                plant_LBL_Location.setText(receivedPlant.getObservationLocation());
        }
        if (receivedPlant.getNotes() == null)
            plant_LL_notes.setVisibility(View.GONE);
        else
            plant_EDT_notes.setText(receivedPlant.getNotes());
    }

    private void backToMain(){
        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra(MainActivity.KEY_BUNDLE,bundle);
        startActivity(intent);
        overridePendingTransition(0, 0);
        finish();
    }

    private void openEditActivity(){
        Intent intent = new Intent(this, EditActivity.class);
        intent.putExtra("KEY_PLANT", receivedPlant);
        startActivity(intent);
        overridePendingTransition(0, 0);
    }

}
