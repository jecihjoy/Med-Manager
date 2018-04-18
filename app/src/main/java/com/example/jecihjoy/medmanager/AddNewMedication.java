
package com.example.jecihjoy.medmanager;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.jecihjoy.medmanager.adapters.DatabaseAdapter;
import com.example.jecihjoy.medmanager.utilities.ReminderBroadCastReceiver;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

public class AddNewMedication extends AppCompatActivity {

    private EditText medicationNameEditText;
    private EditText medicationdescriptionEditText;
    private EditText medicationStarttimeTxt;

    Calendar mcurrentTime;
    Calendar c;
    private DatePicker startDatePicker;
    private  DatePicker endDatePicker;
    private Button saveButton;
    private ArrayList<String> mValuesList;
    private Spinner frequencySpinner;
    private String itemSelected;
    private int duration;
    private int selectedHour;
    private  int SelectedMinute;
    ArrayList<String> allMeds24 = new ArrayList<String>();
    ArrayList<String> allMeds12 = new ArrayList<String>();
    ArrayList<String> allMeds8 = new ArrayList<String>();
    ArrayList<String> allMeds6 = new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_medication);
        setTitle("Add New Medicine");

        medicationNameEditText = (EditText) findViewById(R.id.edt_medication_name);
        medicationdescriptionEditText = (EditText) findViewById(R.id.edt_medication_description);

        startDatePicker = (DatePicker) findViewById(R.id.datepicker_startdate);
        endDatePicker = (DatePicker) findViewById(R.id.datepicker_enddate);

        saveButton = (Button) findViewById(R.id.button_save);
        frequencySpinner = (Spinner) findViewById(R.id.spinner_frequency);


        medicationStarttimeTxt = (EditText) findViewById(R.id.edt_time);


        mValuesList = new ArrayList<>();
        String[] items = new String[]{"2*1", "2*2", "2*3", "3*1", "3*2", "3*3"};

        mValuesList.addAll(Arrays.asList(items));

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, mValuesList);
        frequencySpinner.setAdapter(adapter);

        //time picker
        medicationStarttimeTxt.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                mcurrentTime= Calendar.getInstance();
                c = Calendar.getInstance();
                int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
                int minute = mcurrentTime.get(Calendar.MINUTE);
                TimePickerDialog mTimePicker;
                mTimePicker = new TimePickerDialog(AddNewMedication.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int myHour, int myMinute) {
                        selectedHour = myHour;
                        SelectedMinute = myMinute;
                        mcurrentTime.set(Calendar.HOUR_OF_DAY, selectedHour);
                        mcurrentTime.set(Calendar.MINUTE, SelectedMinute);
                        if (mcurrentTime.getTimeInMillis() >= c.getTimeInMillis()) {
                            medicationStarttimeTxt.setText( String.format("%02d:%02d", selectedHour, SelectedMinute));

                        } else {
                            Toast.makeText(getApplicationContext(), "Invalid Time", Toast.LENGTH_LONG).show();
                        }
                    }
                }, hour, minute, true);//Yes 24 hour time
                mTimePicker.setTitle("Select Time");
                mTimePicker.show();


            }
        });

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //duration
                String testString = frequencySpinner.getSelectedItem().toString();
                int last = Integer.parseInt(testString.substring(2));
                if(last == 4){
                    duration = 6;
                    allMeds6.add(medicationNameEditText.getText().toString());
                }else if (last == 3){
                    duration = 8;
                    allMeds8.add(medicationNameEditText.getText().toString());

                }else if (last == 2){
                    duration = 12;
                    allMeds12.add(medicationNameEditText.getText().toString());
                }else if (last == 1){
                    duration = 24;
                    allMeds24.add(medicationNameEditText.getText().toString());
                }
                Log.e("mainactivity", "duartion" +duration);


                startDatePicker.setMinDate(System.currentTimeMillis() - 1000);
                int year = startDatePicker.getYear();
                int month = startDatePicker.getMonth();
                int day = startDatePicker.getDayOfMonth();
                String startDate = day +"/"+ month +"/"+ year;
                String myMonth = returnMonth(month);

                endDatePicker.setMinDate(System.currentTimeMillis() - 1000);
                int year1 = endDatePicker.getYear();
                int month1 = endDatePicker.getMonth();
                int day1 = endDatePicker.getDayOfMonth();
                String endDate = day1 +"/"+ month1 +"/"+ year1;


                DatabaseAdapter databaseAdapter = new DatabaseAdapter(getApplicationContext());
                databaseAdapter.open();
                try {
                    databaseAdapter.createMeds(
                            medicationNameEditText.getText().toString(),
                            medicationdescriptionEditText.getText().toString(),
                            frequencySpinner.getSelectedItem().toString(),
                            endDate,
                            startDate,
                            "07:00", //dummy time
                            endDate,
                            duration, myMonth );

                    Calendar calendar = Calendar.getInstance();
                    calendar.set(Calendar.HOUR_OF_DAY,selectedHour);
                    calendar.set(Calendar.MINUTE,SelectedMinute);
                    calendar.set(Calendar.SECOND,0);
                    calendar.set(Calendar.MILLISECOND,0);



                    switch (duration) {
                        case 24:
                            sendReminder(calendar, duration, allMeds24);
                            break;
                        case 12:
                        sendReminder(calendar, duration, allMeds12);
                        break;
                        case 8:
                            sendReminder(calendar, duration, allMeds8);
                            break;
                        case 6:
                            sendReminder(calendar, duration, allMeds6);
                            break;
                        default:

                    }

                }catch (NumberFormatException e){

                }

                databaseAdapter.close();

                Intent startMedActivity = new Intent(getApplicationContext(), AllMedsActivity.class);
                startActivity(startMedActivity);

            }
        });

    }
    public void sendReminder(Calendar calendar, int interval, List<String> medName){
        Intent setAlarmIntent = new Intent(getBaseContext(), ReminderBroadCastReceiver.class);
        setAlarmIntent.addFlags(Intent.FLAG_INCLUDE_STOPPED_PACKAGES);
        setAlarmIntent.putStringArrayListExtra("names", (ArrayList<String>) medName);
        setAlarmIntent.setAction("foo");

        PendingIntent intent = PendingIntent.getBroadcast(getBaseContext(),1,setAlarmIntent,PendingIntent.FLAG_UPDATE_CURRENT);
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        alarmManager.setRepeating(
                AlarmManager.RTC_WAKEUP,
                calendar.getTimeInMillis(),
                1000 * 60 * 60 * interval,
                intent

        );
    }
    public String returnMonth(int data){
        if(data == 1){
            return "january";
        }else if(data == 2){
            return  "february";
        }else if(data == 3){
            return  "march";
        }else if(data == 4){
            return  "april";
        }else if(data == 5){
            return  "may";
        }else if(data == 6){
            return  "june";
        }else if(data == 7){
            return  "july";
        }else if(data == 8){
            return  "august";
        }else if(data == 9){
            return  "september";
        }else if(data == 10){
            return  "october";
        }else if(data == 11){
            return  "november";
        }else
            return  "december";
    }
}
