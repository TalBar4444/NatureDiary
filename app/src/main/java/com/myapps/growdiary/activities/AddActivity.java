package com.myapps.growdiary.activities;

import static android.view.View.GONE;

import android.app.Activity;
import android.content.Intent;

import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.LinearLayoutCompat;
import androidx.datastore.preferences.protobuf.Value;

import com.github.dhaval2404.imagepicker.ImagePicker;
import com.google.android.gms.ads.AdError;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.FullScreenContentCallback;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.OnUserEarnedRewardListener;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.google.android.gms.ads.rewarded.RewardItem;
import com.google.android.gms.ads.rewarded.RewardedAd;
import com.google.android.gms.ads.rewarded.RewardedAdLoadCallback;
import com.google.android.material.datepicker.CalendarConstraints;
import com.google.android.material.datepicker.DateValidatorPointBackward;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.datepicker.MaterialPickerOnPositiveButtonClickListener;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.android.material.textview.MaterialTextView;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.myapps.growdiary.MySignal;
import com.myapps.growdiary.MyUtils;
import com.myapps.growdiary.R;

import com.myapps.growdiary.model.MSPV;
import com.myapps.growdiary.model.Plant;
import com.myapps.growdiary.model.PlantHub;
import com.myapps.growdiary.model.Settings;

import java.text.MessageFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import de.hdodenhof.circleimageview.CircleImageView;

public class AddActivity extends AppCompatActivity {

    private MaterialTextView add_LBL_cancel,add_LBL_save;
    private CircleImageView add_IMG_plantImage;
    private ExtendedFloatingActionButton add_FAB_changeProfile;
    private TextInputLayout add_LAY_title, add_LAY_status;
    private TextInputEditText add_EDT_title, add_EDT_status, add_EDT_plantingDate, add_EDT_observationDate, add_EDT_location;
    private AppCompatImageView add_IMG_pro;
    private RadioButton add_RATIO_home,add_RATIO_outside;
    private AutoCompleteTextView add_PICK_watering;
    private TextView add_LBL_counter;
    private EditText add_EDT_notes;
    private LinearLayoutCompat add_LL_homeGarden,add_LL_outside;
    private ArrayAdapter<String> adapterDays;
    private String[] itemDays = {"1", "2", "3", "4", "5", "6", "7"};
    private boolean name = false,status = false;
    private LocalDate localDate;
    private Class<?> mainClass;
    private Uri imageUri;
    public static final int MAX_TITLE = 28;
    public static final int MAX_STATUS = 120;
    public static final int MAX_NOTE = 200;
    private FirebaseAnalytics mFirebaseAnalytics;
    private RewardedAd rewardedAd;

    @Override
    protected void onCreate(Bundle saveInstanceState) {
        super.onCreate(saveInstanceState);
        setContentView(R.layout.activity_add);
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);
        findViews();
        checkEnable();
        checkUserProgram();

        add_LBL_cancel.setOnClickListener(view -> {
            getCurrentMain();
            redirectActivity(AddActivity.this, mainClass);
        });

        add_LBL_save.setOnClickListener(view -> {
            checkUserProgram();
            addNewPlant();
            firebaseWildDiscoveryEvent();
            firebasePlantEvent();
            showVideoAd();
        });

        add_FAB_changeProfile.setOnClickListener(view -> ImagePicker.with(AddActivity.this)
                .crop()                    //Crop image(Optional), Check Customization for more option
                .compress(1024)            //Final image size will be less than 1 MB(Optional)
                .maxResultSize(1080, 1080)    //Final image resolution will be less than 1080 x 1080(Optional)
                .start());

        add_EDT_title.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_CAP_WORDS);

        add_EDT_title.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                add_LAY_title.setEndIconActivated(true);
            }

            @Override
            public void afterTextChanged(Editable editable) {
                add_LAY_title.setEndIconMode(TextInputLayout.END_ICON_CLEAR_TEXT);
                if (editable.length() > 0) {
                    name = true;
                } else
                    name = false;
                checkEnable();
            }
        });

        add_EDT_status.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                add_LAY_status.setEndIconActivated(true);
            }

            @Override
            public void afterTextChanged(Editable editable) {
                add_LAY_status.setEndIconMode(TextInputLayout.END_ICON_CLEAR_TEXT);
                if (editable.length() > 0) {
                    status = true;
                } else
                    status = false;
                checkEnable();
            }
        });
        add_IMG_pro.setOnClickListener(view -> MyUtils.proLimitationPopup(AddActivity.this, ShopActivity.class));

        add_RATIO_outside.setOnClickListener(view -> {
            if (!add_EDT_plantingDate.getText().toString().trim().isEmpty())
                add_EDT_plantingDate.setText("");
            if (!add_PICK_watering.getText().toString().isEmpty())
                add_PICK_watering.setText("");
            add_LL_homeGarden.setVisibility(GONE);
            add_LL_outside.setVisibility(View.VISIBLE);
        });

        add_RATIO_home.setOnClickListener(view -> {
            if (!add_EDT_observationDate.getText().toString().trim().isEmpty())
                add_EDT_observationDate.setText("");
            if (!add_EDT_location.getText().toString().trim().isEmpty())
                add_EDT_location.setText("");
            add_LL_outside.setVisibility(GONE);
            add_LL_homeGarden.setVisibility(View.VISIBLE);
        });

        add_EDT_plantingDate.setOnClickListener(view -> {
            MaterialDatePicker<Long> materialDatePicker = prepareCalendar();
            materialDatePicker.addOnPositiveButtonClickListener(new MaterialPickerOnPositiveButtonClickListener<Long>() {
                @Override
                public void onPositiveButtonClick(Long selection) {
                    String date = new SimpleDateFormat("dd-MM-yyy", Locale.getDefault()).format(new Date(selection));
                    add_EDT_plantingDate.setText(MessageFormat.format("{0}", date));
                }
            });
            materialDatePicker.show(getSupportFragmentManager(), "tag");
        });

        add_EDT_observationDate.setOnClickListener(view -> {
            MaterialDatePicker<Long> materialDatePicker = prepareCalendar();
            materialDatePicker.addOnPositiveButtonClickListener(new MaterialPickerOnPositiveButtonClickListener<Long>() {
                @Override
                public void onPositiveButtonClick(Long selection) {
                    String date = new SimpleDateFormat("dd-MM-yyy", Locale.getDefault()).format(new Date(selection));
                    add_EDT_observationDate.setText(MessageFormat.format("{0}", date));
                }
            });
            materialDatePicker.show(getSupportFragmentManager(), "tag");
        });

        add_EDT_location.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_CAP_WORDS);

        add_EDT_notes.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                int currentLength = charSequence.length();
                add_LBL_counter.setText(String.valueOf(currentLength) + "/" + MAX_NOTE);
            }

            @Override
            public void afterTextChanged(Editable editable) {}
        });
    }

    private void findViews() {
        add_LBL_save = findViewById(R.id.add_LBL_save);
        add_LBL_cancel = findViewById(R.id.add_LBL_cancel);
        add_IMG_plantImage = findViewById(R.id.add_IMG_plantImage);
        add_FAB_changeProfile = findViewById(R.id.add_FAB_changeProfile);
        add_LAY_title = findViewById(R.id.add_LAY_title);
        add_EDT_title = findViewById(R.id.add_EDT_title);
        add_LAY_status = findViewById(R.id.add_LAY_status);
        add_EDT_status = findViewById(R.id.add_EDT_status);

        add_IMG_pro = findViewById(R.id.add_IMG_pro);
        add_RATIO_home = findViewById(R.id.add_RATIO_home);
        add_RATIO_outside = findViewById(R.id.add_RATIO_outside);

        add_LL_homeGarden = findViewById(R.id.add_LL_homeGarden);
        add_EDT_plantingDate = findViewById(R.id.add_EDT_plantingDate);

        add_PICK_watering = findViewById(R.id.add_PICK_watering);
        adapterDays = new ArrayAdapter<String>(this, R.layout.item_days, itemDays);
        add_PICK_watering.setAdapter(adapterDays);

        add_LL_outside = findViewById(R.id.add_LL_outside);
        add_EDT_observationDate = findViewById(R.id.add_EDT_observationDate);
        add_EDT_location = findViewById(R.id.add_EDT_location);
        add_LBL_counter = findViewById(R.id.add_LBL_counter);
        add_EDT_notes = findViewById(R.id.add_EDT_notes);
    }

    private void checkEnable() {
        if (name && status) {
            add_LBL_save.setEnabled(true);
            add_LBL_save.setTextColor(getColor(R.color.primary));
        }
        else {
            add_LBL_save.setEnabled(false);
            add_LBL_save.setTextColor(getColor(R.color.gray));
        }
    }

    private void checkUserProgram() {
        if (Settings.getType()==Settings.UserType.PRO && Settings.getAdsMode()==Settings.AdsMode.ADS_OFF){ // PRO + No ads = No Video
            add_RATIO_outside.setEnabled(true);
            add_IMG_pro.setVisibility(GONE);
            rewardedAd = null;
        }
        if (Settings.getType()==Settings.UserType.PRO && Settings.getAdsMode()==Settings.AdsMode.ADS_ON){ // PRO + Yes ads = No Video
            add_RATIO_outside.setEnabled(true);
            add_IMG_pro.setVisibility(GONE);
            rewardedAd = null;
        }
        else if(Settings.getType()==Settings.UserType.REGULAR && Settings.getAdsMode()==Settings.AdsMode.ADS_OFF){ // REGULAR + No ads = No Video
            add_RATIO_outside.setEnabled(false);
            add_IMG_pro.setVisibility(View.VISIBLE);
            rewardedAd = null;

        } else if (Settings.getType()==Settings.UserType.REGULAR && Settings.getAdsMode()==Settings.AdsMode.ADS_ON){ // REGULAR + ads = Yes Video
            add_RATIO_outside.setEnabled(false);
            add_IMG_pro.setVisibility(View.VISIBLE);
            loadRewardedVideAd();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        imageUri = data.getData();
        if(imageUri != null)
            add_IMG_plantImage.setImageURI(imageUri);
    }

    public static MaterialDatePicker<Long> prepareCalendar() {
        CalendarConstraints.Builder constraintsBuilder  = new CalendarConstraints.Builder()
                .setValidator(DateValidatorPointBackward.now());
        constraintsBuilder.setFirstDayOfWeek(Calendar.SUNDAY);

        MaterialDatePicker<Long> materialDatePicker = MaterialDatePicker.Builder.datePicker()
                .setTitleText("Select Date")
                .setSelection(MaterialDatePicker.todayInUtcMilliseconds())
                .setCalendarConstraints(constraintsBuilder.build())
                .build();
        return materialDatePicker;
    }

    private void getCurrentMain() {
        if(getAllPlants().isEmpty())
            mainClass = NoPlantsActivity.class;
        else
            mainClass = MainActivity.class;
    }
    private void addNewPlant() {
        Plant plant = createPlantFromInput();

        ArrayList<Plant> allPlants = getAllPlants();
        allPlants.add(0, plant);
        MSPV.getMe().saveThirtyPlants(new PlantHub().setPlants(allPlants));

        MySignal.getInstance().toast("A New plant Has been added!");
    }

    private Plant createPlantFromInput() {
        Plant plant = new Plant(
                add_EDT_title.getText().toString().trim(),
                add_EDT_status.getText().toString().trim(),
                setTypeFunc()
        );

        plant.setImage(imageUri != null ? imageUri.toString() : null);

        if(add_RATIO_home.isChecked()) {
            if (!add_EDT_plantingDate.getText().toString().trim().isEmpty())
                plant.setPlantingDate(add_EDT_plantingDate.getText().toString().trim());

            if (!add_PICK_watering.getText().toString().isEmpty())
                plant.setWateringFrequency(add_PICK_watering.getText().toString().trim());
        }
        else{
            if (!add_EDT_observationDate.getText().toString().trim().isEmpty())
                plant.setObservationDate(add_EDT_observationDate.getText().toString().trim());

            if(!add_EDT_location.getText().toString().trim().isEmpty())
                plant.setObservationLocation(add_EDT_location.getText().toString().trim());
        }

        if (!add_EDT_notes.getText().toString().trim().isEmpty())
            plant.setNotes(add_EDT_notes.getText().toString().trim());

        plant.setLastUpdate(setFormattedDate());

        return plant;
    }
    private Plant.DocumentationType setTypeFunc(){
        Plant.DocumentationType type;
        if(add_RATIO_home.isChecked())
            type = Plant.DocumentationType.HOME_GARDENER;
        else
            type = Plant.DocumentationType.WILD_DISCOVERY;
        return type;
    }

    private String setFormattedDate() {
        localDate = LocalDate.now(); //set the date of today
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        String formattedDate = localDate.format(dateFormatter);

        return formattedDate;
    }

    static ArrayList<Plant> getAllPlants() {
        PlantHub plants = MSPV.getMe().readThirtyPlants();
        return plants.getPlants();
    }


    private void showVideoAd() {
        if (rewardedAd != null) {
            rewardedAd.show(this, rewardItem -> {
                redirectActivity(AddActivity.this, MainActivity.class);
            });
        } else {
            redirectActivity(AddActivity.this, MainActivity.class);
        }
    }

    private void loadRewardedVideAd() {
        AdRequest adRequest = new AdRequest.Builder().build();
        RewardedAd.load(this, "ca-app-pub-3940256099942544/5224354917",
                adRequest, new RewardedAdLoadCallback() {
                    @Override
                    public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {}

                    @Override
                    public void onAdLoaded(@NonNull RewardedAd ad) {
                        rewardedAd = ad;
                    }
                });

        RewardedAd.load(AddActivity.this, "ca-app-pub-3940256099942544/5354046379",
            new AdRequest.Builder().build(), new RewardedAdLoadCallback() {
                @Override
                public void onAdLoaded(@NonNull RewardedAd ad) {
                    super.onAdLoaded(rewardedAd);
                    rewardedAd = ad;
                }

                @Override
                public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                    super.onAdFailedToLoad(loadAdError);
                }
            });
    }


    private void firebasePlantEvent() { //compare to amount of edit
        ArrayList<Plant> allPlants = getAllPlants();
        Bundle bundle = new Bundle();
        bundle.putString(FirebaseAnalytics.Param.ITEM_ID, "buttonClicked");
        bundle.putString(FirebaseAnalytics.Param.ITEM_NAME," Save button Clicked");
        bundle.putString(FirebaseAnalytics.Param.CONTENT_TYPE, "amount of plants"); //usability check
        bundle.putString(FirebaseAnalytics.Param.CONTENT, " " + allPlants.size()); //usability check
        mFirebaseAnalytics.logEvent(FirebaseAnalytics.Event.SELECT_CONTENT, bundle);
    }

    private void firebaseWildDiscoveryEvent() {
        Plant.DocumentationType type = setTypeFunc();
        if(type == Plant.DocumentationType.WILD_DISCOVERY) {
            Bundle bundle = new Bundle();
            bundle.putString(FirebaseAnalytics.Param.ITEM_ID, "buttonChecked");
            bundle.putString(FirebaseAnalytics.Param.ITEM_NAME, "WildDiscovery Checked");
            bundle.putString(FirebaseAnalytics.Param.CONTENT_TYPE, "new plant of type WildDiscovery"); //usability check
            mFirebaseAnalytics.logEvent(FirebaseAnalytics.Event.SELECT_CONTENT, bundle);
        }
    }

    private void redirectActivity(Activity activity, Class<?> secondActivity){
        Intent intent = new Intent(activity,secondActivity);
        activity.startActivity(intent);
        overridePendingTransition(0, 0);
        activity.finish();
    }
}