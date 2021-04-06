package com.khamvongsa.victor.mareunion.controller;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentActivity;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.google.android.material.textfield.TextInputLayout;
import com.khamvongsa.victor.mareunion.R;
import com.khamvongsa.victor.mareunion.di.DI;
import com.khamvongsa.victor.mareunion.service.CalculTime;
import com.khamvongsa.victor.mareunion.service.CalculTimeService;
import com.khamvongsa.victor.mareunion.service.ReunionApiService;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Iterator;
import java.util.List;

import static android.widget.Toast.LENGTH_SHORT;

public class AddReunionActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    // UI COMPONENTS
    @BindView(R.id.activity_add_reunion_inputDate)
    TextInputLayout mDateInput;
    @BindView(R.id.activity_add_reunion_editDate)
    EditText mEditDate;
    @BindView(R.id.activity_add_reunion_btnGetDate)
    Button mButtonGetDate;
    @BindView(R.id.activity_add_reunion_textViewDate)
    TextView mTextViewDate;

    // Start Hour Reunion
    @BindView(R.id.activity_add_reunion_inputStartHour)
    TextInputLayout mStartHourInput;
    @BindView(R.id.activity_add_reunion_editStartHour)
    EditText mEditStartHour;
    @BindView(R.id.activity_add_reunion_btnGetStartHour)
    Button mButtonGetStartHour;
    @BindView(R.id.activity_add_reunion_textViewStartHour)
    TextView mTextViewStartHour;
    // End Hour Reunion
    @BindView(R.id.activity_add_reunion_inputEndHour)
    TextInputLayout mEndHourInput;
    @BindView(R.id.activity_add_reunion_editEndHour)
    EditText mEditEndHour;
    @BindView(R.id.activity_add_reunion_btnGetEndHour)
    Button mButtonGetEndHour;
    @BindView(R.id.activity_add_reunion_textViewEndHour)
    TextView mTextViewEndHour;

    @BindView(R.id.activity_add_reunion_spinnerRoom)
    Spinner mSpinnerRoom;

    @BindView(R.id.activity_add_reunion_SubjectReunion)
    TextInputLayout mInputSubjectReunion;

    @BindView(R.id.activity_add_reunion_editText_participant)
    EditText mEditTextParticipant;
    @BindView(R.id.activity_add_reunion_chipGroup)
    ChipGroup mChipGroupParticipant;
    @BindView(R.id.activity_add_reunion_btnAdd_participant)
    Button mButtonAddParticipant;
    @BindView(R.id.activity_add_reunion_btn_showParticipant)
    Button mButtonShowParticipant;

    @BindView(R.id.activity_add_reunion_btn_createReunion)
    MaterialButton addButton;

    private ReunionApiService mReunionApiService;
    private CalculTimeService mCalculTimeService;
    private DatePickerDialog mDatePicker;
    private TimePickerDialog mTimePicker;
    private List<String> mlistParticipant = new ArrayList<String>();
    private Calendar mStartDate = Calendar.getInstance();
    private Calendar mStartHour = Calendar.getInstance();
    private Calendar mEndHour = Calendar.getInstance();
    private ExempleSalle mSalle;
    private List<ExempleReunion> mReunions;

    private List<ExempleSalle> mSallesDisponibles;
    private List<String> listSalles = new ArrayList<String>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_reunion);
        ButterKnife.bind(this);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mReunionApiService = DI.getReunionApiService();
        mCalculTimeService = new CalculTime();
        mSpinnerRoom.setEnabled(false);
        enableAddButton();
        launchEnableSpinner();

        // DATE
        mEditDate.setInputType(InputType.TYPE_NULL);
        mEditDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar cldr = Calendar.getInstance();
                int day = cldr.get(Calendar.DAY_OF_MONTH);
                int month = cldr.get(Calendar.MONTH);
                int year = cldr.get(Calendar.YEAR);
                // date picker dialog
                mDatePicker = new DatePickerDialog(AddReunionActivity.this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                                mStartDate.set(year,monthOfYear, dayOfMonth);
                                mEditDate.setText(dayOfMonth + "/" + (monthOfYear + 1) + "/" + year);
                                mTextViewDate.setText("Selected Date: " + mEditDate.getText());
                            }
                        }, year, month, day);
                mDatePicker.show();
            }
        });
        mButtonGetDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mTextViewDate.setText("Selected Date: " + mEditDate.getText());
            }
        });

        //HOUR START
        mEditStartHour.setEnabled(false);
        mEditStartHour.setInputType(InputType.TYPE_NULL);
        mEditStartHour.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar cldr = Calendar.getInstance();
                int hour = cldr.get(Calendar.HOUR_OF_DAY);
                int minutes = cldr.get(Calendar.MINUTE);
                // time picker dialog
                mTimePicker = new TimePickerDialog(AddReunionActivity.this,
                        new TimePickerDialog.OnTimeSetListener() {
                            @Override
                            public void onTimeSet(TimePicker tp, int sHour, int sMinute) {
                                mEditStartHour.setText(sHour + ":" + sMinute);
                                mStartHour.set(0,0,0,sHour,sMinute);
                                mTextViewStartHour.setText("Selected Time: " + mEditStartHour.getText());
                            }
                        }, hour, minutes, true);
                mTimePicker.show();
            }
        });
        mButtonGetStartHour.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mTextViewStartHour.setText("Selected Time: " + mEditStartHour.getText());
            }
        });

        // HOUR END
        mEditEndHour.setEnabled(false);
        mEditEndHour.setInputType(InputType.TYPE_NULL);
        mEditEndHour.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar cldr = Calendar.getInstance();
                int hour = cldr.get(Calendar.HOUR_OF_DAY);
                int minutes = cldr.get(Calendar.MINUTE);
                // time picker dialog
                mTimePicker = new TimePickerDialog(AddReunionActivity.this,
                        new TimePickerDialog.OnTimeSetListener() {
                            @Override
                            public void onTimeSet(TimePicker tp, int sHour, int sMinute) {
                                mEditEndHour.setText(sHour + ":" + sMinute);
                                mEndHour.set(0,0,0,sHour,sMinute);
                                mTextViewEndHour.setText("Selected Time: " + mEditEndHour.getText());
                                availableRoom();
                            }
                        }, hour, minutes, true);
                mTimePicker.show();
            }
        });
        mButtonGetEndHour.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mTextViewEndHour.setText("Selected Time: " + mEditEndHour.getText());
            }
        });

        // AVAILABLE_ROOM

        // REUNION SUBJECT

        // PARTICIPANTS
        this.mButtonAddParticipant.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addNewChip();
            }
        });
    }

    public void availableRoom() {

        int mReunionHourTime = mEndHour.get(Calendar.HOUR_OF_DAY) - mStartHour.get(Calendar.HOUR_OF_DAY);
        int mReunionMinuteTime = (mEndHour.get(Calendar.HOUR_OF_DAY)*60 + mEndHour.get(Calendar.MINUTE)) - (mStartHour.get(Calendar.HOUR_OF_DAY)*60 + mStartHour.get(Calendar.MINUTE));

        int mReunionMinuteTimeLeft = mCalculTimeService.calculMinuteLeft(mReunionMinuteTime, mReunionHourTime);
        int mReunionHourTimeLeft = mCalculTimeService.calculHourLeft(mReunionMinuteTime, mReunionHourTime);

        mReunions = mReunionApiService.getReunions();
        mSallesDisponibles = mReunionApiService.getSalles();
        listSalles = mReunionApiService.FilterAvailableRooms(mReunions, mSallesDisponibles, mStartDate, mStartHour, mEndHour);

        Toast.makeText(this, "Temps de réunion : "+ mReunionHourTimeLeft+"h "+ mReunionMinuteTimeLeft,LENGTH_SHORT).show();

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, listSalles);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mSpinnerRoom.setAdapter(adapter);
        mSpinnerRoom.setOnItemSelectedListener(this);
    }

    // TODO : Calculer la différence d'heures

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        Toast.makeText(getApplicationContext(), "Selected User: "+ listSalles.get(position) , Toast.LENGTH_SHORT).show();
        final String salleChoisie = listSalles.get(position);
        for (int i = 0; i < mSallesDisponibles.size(); i++) {
            if(salleChoisie.equalsIgnoreCase(mSallesDisponibles.get(i).getNom())) {
                mSalle = mSallesDisponibles.get(i);
            }
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
    }

    @Override
    public boolean onOptionsItemSelected (MenuItem item){
        if (android.R.id.home == item.getItemId()) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void addNewChip() {
        String participant = this.mEditTextParticipant.getText().toString();
        if (participant == null || participant.isEmpty()) {
            Toast.makeText(this, "Please enter the participants!", Toast.LENGTH_LONG).show();
            return;
        }

        try {
            LayoutInflater inflater = LayoutInflater.from(this);

            // Create a Chip from Layout.
            Chip newChip = (Chip) inflater.inflate(R.layout.layout_chip_entry, this.mChipGroupParticipant, false);
            newChip.setText(participant);
            newChip.setTag(participant);

            this.mChipGroupParticipant.addView(newChip);

            // Set Listener for the Chip:
            newChip.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    handleChipCheckChanged((Chip) buttonView, isChecked);
                }
            });

            newChip.setOnCloseIconClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    handleChipCloseIconClicked((Chip) v);
                }
            });

            this.mEditTextParticipant.setText("");
            mlistParticipant.add(participant);

        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(this, "Error: " + e.getMessage(), Toast.LENGTH_LONG).show();
        }

    }

    // User close a Chip.
    private void handleChipCloseIconClicked(Chip chip) {
        ChipGroup parent = (ChipGroup) chip.getParent();
        parent.removeView(chip);

        String deletedChip = (String) chip.getTag();
        Iterator i = mlistParticipant.iterator();
        while (i.hasNext()) {
            String participant = (String) i.next();
            if (deletedChip.equalsIgnoreCase(participant)) {
                i.remove();
                break;
            }
        }
    }

    // Chip Checked Changed
    private void handleChipCheckChanged(Chip chip, boolean isChecked) {
    }

    // TODO : L'utilisateur doit remplir tous les champs pour pouvoir créer la réunion

    private void enableAddButton() {

        mInputSubjectReunion.getEditText().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }
            @Override
            public void afterTextChanged(Editable s) {
                addButton.setEnabled(s.length() > 0);
            }
        });
    }

    private void launchEnableSpinner() {
        mDateInput.getEditText().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }
            @Override
            public void afterTextChanged(Editable s) {
                mEditStartHour.setEnabled(s.length()>0);
            }
        });

        mStartHourInput.getEditText().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }
            @Override
            public void afterTextChanged(Editable s) { mEditEndHour.setEnabled(s.length() > 0);
            }
        });

        mEndHourInput.getEditText().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }
            @Override
            public void afterTextChanged(Editable s) {
                mSpinnerRoom.setEnabled(s.length() > 0);
            }
        });
    }

    public static void navigate (FragmentActivity activity){
        Intent intent = new Intent(activity, AddReunionActivity.class);
        ActivityCompat.startActivity(activity, intent, null);
    }

    @OnClick(R.id.activity_add_reunion_btn_createReunion)
    void addReunion() {
        ExempleReunion reunion = new ExempleReunion(
                System.currentTimeMillis(), mStartDate.getTime(), mStartHour.getTime(), mEndHour.getTime(), mSalle, mInputSubjectReunion.getEditText().getText().toString(), mlistParticipant
        );
        mReunionApiService.createReunion(reunion);
        finish();
    }
}
