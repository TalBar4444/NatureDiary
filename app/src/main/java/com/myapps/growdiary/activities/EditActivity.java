package com.myapps.growdiary.activities;

import static com.myapps.growdiary.activities.AddActivity.MAX_NOTE;
import static com.myapps.growdiary.activities.AddActivity.MAX_STATUS;
import static com.myapps.growdiary.activities.AddActivity.MAX_TITLE;

import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputFilter;
import android.text.InputType;
import android.text.TextWatcher;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.LinearLayoutCompat;

import com.github.dhaval2404.imagepicker.ImagePicker;
import com.google.android.material.datepicker.CalendarConstraints;
import com.google.android.material.datepicker.DateValidatorPointBackward;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.datepicker.MaterialPickerOnPositiveButtonClickListener;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.android.material.textview.MaterialTextView;
import com.myapps.growdiary.MySignal;
import com.myapps.growdiary.R;
import com.myapps.growdiary.model.MSPV;
import com.myapps.growdiary.model.Plant;
import com.myapps.growdiary.model.PlantHub;

import java.text.MessageFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;
import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;

public class EditActivity extends AppCompatActivity {

    public static final String KEY_BUNDLE = "KEY_BUNDLE";
    private MaterialTextView edit_LBL_cancel,edit_LBL_save;
    private CircleImageView edit_IMG_plantImage;
    private ExtendedFloatingActionButton edit_FAB_changeProfile;
    private TextInputLayout edit_LAY_title, edit_LAY_status;
    private TextInputEditText edit_EDT_title, edit_EDT_status, edit_EDT_plantingDate, edit_EDT_observationDate, edit_EDT_location;
    private RadioButton edit_RATIO_home,edit_RATIO_outside;
    private AutoCompleteTextView edit_PICK_watering;
    private TextView edit_LBL_counter;
    private EditText edit_EDT_notes;
    private LinearLayoutCompat edit_LL_homeGarden,edit_LL_outside;
    private ArrayAdapter<String> adapterDays;
    private String[] itemDays = {"1", "2", "3", "4", "5", "6", "7"};
    private boolean name = false,status = false;
    private boolean isChanged = false;
    private LocalDate localDate;
    private Uri imageUri;
    private Plant receivedPlant;
    private PlantHub plantHub;

    public EditActivity(){
        // Required empty public constructor
    }

    public interface editFragmentListener {
        public void onSaveClicked(ArrayList<Plant> plantList);
    }

    @Override
    protected void onCreate(Bundle saveInstanceState){
        super.onCreate(saveInstanceState);
        setContentView(R.layout.activity_edit);
        receivedPlant = (Plant) getIntent().getSerializableExtra("KEY_PLANT");
        findViews();
        getData();
        checkEnable();

        edit_LBL_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                backToPlantActivity();
            }
        });

        edit_LBL_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                editPlant();
                backToPlantActivity();
            }
        });

        edit_FAB_changeProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ImagePicker.with(EditActivity.this)
                        .crop()	    			//Crop image(Optional), Check Customization for more option
                        .compress(1024)			//Final image size will be less than 1 MB(Optional)
                        .maxResultSize(1080, 1080)	//Final image resolution will be less than 1080 x 1080(Optional)
                        .start();
            }
        });

        edit_EDT_title.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_CAP_WORDS);

        edit_EDT_title.addTextChangedListener(new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                edit_LAY_title.setEndIconActivated(true);
            }

            @Override
            public void afterTextChanged(Editable editable) {
                edit_LAY_title.setEndIconMode(TextInputLayout.END_ICON_CLEAR_TEXT);
                if (editable.length() > 0) {
                    name = true;
                }
                else
                    name = false;
                checkEnable();
            }
        });

        edit_EDT_status.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                edit_LAY_status.setEndIconActivated(true);
            }

            @Override
            public void afterTextChanged(Editable editable) {
                edit_LAY_status.setEndIconMode(TextInputLayout.END_ICON_CLEAR_TEXT);
                if (editable.length() > 0) {
                    status = true;
                }
                else
                    status = false;
                checkEnable();
            }
        });

        edit_EDT_plantingDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MaterialDatePicker<Long> materialDatePicker = AddActivity.prepareCalendar();

                materialDatePicker.addOnPositiveButtonClickListener(new MaterialPickerOnPositiveButtonClickListener<Long>() {
                    @Override
                    public void onPositiveButtonClick(Long selection) {
                        String date = new SimpleDateFormat("dd-MM-yyy", Locale.getDefault()).format(new Date(selection));
                        edit_EDT_plantingDate.setText(MessageFormat.format("{0}", date));
                    }
                });
                materialDatePicker.show(getSupportFragmentManager(), "tag");
            }
        });

        edit_EDT_observationDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MaterialDatePicker<Long> materialDatePicker = AddActivity.prepareCalendar();

                materialDatePicker.addOnPositiveButtonClickListener(new MaterialPickerOnPositiveButtonClickListener<Long>() {
                    @Override
                    public void onPositiveButtonClick(Long selection) {
                        String date = new SimpleDateFormat("dd-MM-yyy", Locale.getDefault()).format(new Date(selection));
                        edit_EDT_observationDate.setText(MessageFormat.format("{0}", date));
                    }
                });
                materialDatePicker.show(getSupportFragmentManager(), "tag");
            }
        });

        edit_EDT_location.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_CAP_WORDS);

        edit_EDT_notes.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                int currentLength = charSequence.length();
                edit_LBL_counter.setText(String.valueOf(currentLength) + "/" + MAX_NOTE);
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });

    }
    private void findViews() {
        edit_LBL_save = findViewById(R.id.edit_LBL_save);
        edit_LBL_cancel = findViewById(R.id.edit_LBL_cancel);
        edit_IMG_plantImage = findViewById(R.id.edit_IMG_plantImage);
        edit_FAB_changeProfile = findViewById(R.id.edit_FAB_changeProfile);
        edit_LAY_title = findViewById(R.id.edit_LAY_title);
        edit_EDT_title = findViewById(R.id.edit_EDT_title);
        edit_LAY_status = findViewById(R.id.edit_LAY_status);
        edit_EDT_status = findViewById(R.id.edit_EDT_status);
        edit_RATIO_home = findViewById(R.id.edit_RATIO_home);
        edit_RATIO_outside = findViewById(R.id.edit_RATIO_outside);

        edit_LL_homeGarden = findViewById(R.id.edit_LL_homeGarden);
        edit_EDT_plantingDate = findViewById(R.id.edit_EDT_plantingDate);

        edit_PICK_watering = findViewById(R.id.edit_PICK_watering);
        adapterDays = new ArrayAdapter<String>(this, R.layout.item_days, itemDays);
        edit_PICK_watering.setAdapter(adapterDays);

        edit_LL_outside = findViewById(R.id.edit_LL_outside);
        edit_EDT_observationDate = findViewById(R.id.edit_EDT_observationDate);
        edit_EDT_location = findViewById(R.id.edit_EDT_location);
        edit_LBL_counter = findViewById(R.id.edit_LBL_counter);
        edit_EDT_notes = findViewById(R.id.edit_EDT_notes);
    }

    private void getData() {
        if(receivedPlant.getImage()==null)
            edit_IMG_plantImage.setImageResource(R.drawable.noplantimagebackground);
        else
            edit_IMG_plantImage.setImageURI(Uri.parse(receivedPlant.getImage()));

        if(receivedPlant.getTitle().length() > 0){
            edit_EDT_title.setText(receivedPlant.getTitle());
            name = true;
        }
        if(receivedPlant.getStatus().length() > 0) {
            edit_EDT_status.setText(receivedPlant.getStatus());
            status = true;
        }
        if(receivedPlant.getType() == Plant.DocumentationType.HOME_GARDENER) {
            edit_RATIO_outside.setEnabled(false);
            edit_LL_outside.setVisibility(View.GONE);
            edit_LL_homeGarden.setVisibility(View.VISIBLE);
            edit_EDT_plantingDate.setText(receivedPlant.getPlantingDate());
            edit_PICK_watering.setText(receivedPlant.getWateringFrequency(),false);
        }
        else {
            edit_RATIO_home.setEnabled(false);
            edit_RATIO_outside.setChecked(true);
            edit_LL_homeGarden.setVisibility(View.GONE);
            edit_LL_outside.setVisibility(View.VISIBLE);
            edit_EDT_observationDate.setText(receivedPlant.getObservationDate());
            edit_EDT_location.setText(receivedPlant.getObservationLocation());
        }
        edit_EDT_notes.setText(receivedPlant.getNotes());
        if(receivedPlant.getNotes() != null)
            edit_LBL_counter.setText(String.valueOf(receivedPlant.getNotes().length())+ "/" + MAX_NOTE);
    }

    private void checkEnable() {
        if (name && status) {
            edit_LBL_save.setEnabled(true);
            edit_LBL_save.setTextColor(getColor(R.color.primary));
        }
        else {
            edit_LBL_save.setEnabled(false);
            edit_LBL_save.setTextColor(getColor(R.color.gray));
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        imageUri = data.getData();
        if(imageUri != null)
            edit_IMG_plantImage.setImageURI(imageUri);
    }
    private void editPlant() {
        if(imageUri !=null && !compareStrings(edit_IMG_plantImage.toString(), receivedPlant.getImage()))
            receivedPlant.setImage(imageUri.toString());

        if(!compareStrings(edit_EDT_title.getText().toString().trim(), receivedPlant.getTitle()))
            receivedPlant.setTitle(edit_EDT_title.getText().toString().trim());
        if(!compareStrings(edit_EDT_status.getText().toString().trim(), receivedPlant.getStatus()))
            receivedPlant.setStatus(edit_EDT_status.getText().toString().trim());

        if(edit_RATIO_home.isChecked()) {
            String plantingDate = edit_EDT_plantingDate.getText().toString().trim();
            if(!compareStrings(plantingDate, receivedPlant.getPlantingDate()))
                receivedPlant.setPlantingDate(!plantingDate.isEmpty() ? plantingDate : null);


            String watering = edit_PICK_watering.getText().toString().trim();
            if(!compareStrings(watering, receivedPlant.getWateringFrequency()))
                receivedPlant.setWateringFrequency(!watering.isEmpty() ? watering : null);
        }
        else{
            String observationDate = edit_EDT_observationDate.getText().toString().trim();
            if(!compareStrings(observationDate, receivedPlant.getObservationDate())) {
                receivedPlant.setObservationDate(!observationDate.isEmpty() ? observationDate : null);
            }

            String location = edit_EDT_location.getText().toString().trim();
            if(!compareStrings(location, receivedPlant.getObservationLocation())) {
                receivedPlant.setObservationLocation(!location.isEmpty() ? location : null);
            }
        }

        String notes = edit_EDT_notes.getText().toString().trim();
        if(!compareStrings(notes, receivedPlant.getNotes())) {
            receivedPlant.setNotes(!notes.isEmpty() ? notes : null);
        }

        if(isChanged){
            receivedPlant.setLastUpdate(setFormattedDate());
            movePlantToTop(receivedPlant);
        }

        else
            backToPlantActivity();
    }

    private String setFormattedDate() {
        localDate = LocalDate.now(); //set the date of today
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        String formattedDate = localDate.format(dateFormatter);
        return formattedDate;
    }

    private boolean compareStrings(String str1, String str2){
        if((str1.isEmpty()) && (str2 == null ))
            return true;
        else if(!Objects.equals(str1, str2)) {
            isChanged = true;
            return false;
        }
        return true;
    }

    private void movePlantToTop(Plant plant) {
        PlantHub plants = MSPV.getMe().readThirtyPlants();
        ArrayList<Plant> allPlants = plants.getPlants();
        allPlants.remove(plant);
        allPlants.add(0, plant);
        MSPV.getMe().saveThirtyPlants(plants.setPlants(allPlants));
        MySignal.getInstance().toast(plant.getTitle() + " has been updated");
    }

    private void backToPlantActivity(){
        Bundle bundle = new Bundle();
        bundle.putBoolean(PlantActivity.KEY_IS_CHANGED,isChanged);
        Intent intent = new Intent(this, PlantActivity.class);
        intent.putExtra(PlantActivity.KEY_BUNDLE, bundle);
        intent.putExtra("KEY_PLANT",receivedPlant);
        startActivity(intent);
        overridePendingTransition(0, 0);
        finish();
    }

}