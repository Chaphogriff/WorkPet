package com.workthrutheweak.workpet;

import static java.time.LocalDate.of;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.icu.util.Calendar;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.workthrutheweak.workpet.databinding.ActivityModifytaskBinding;
import com.workthrutheweak.workpet.notification.ReminderBroadcast;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Locale;

public class ModifyTaskActivity extends AppCompatActivity {
    private ActivityModifytaskBinding binding;//For ViewBinding feature
    private DatePickerDialog datePickerDialog;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    private DocumentReference docref = db.collection("Users").document(user.getUid());

    Button button_back;
    Button button_time;
    Button button_date;
    Button button_save;
    EditText titleTask;
    EditText descTask;
    LocalDate localdate;
    LocalTime localtime;
    int hour, minute;
    String difficulty;
    String mode;
    int gold, xp;
    int posdifspin, posmodespin;

    int yearIntent, monthIntent, dayIntent, hourIntent, minuteIntent;
    String taskId;
    String origin;
    boolean isDateSet = false;
    boolean isTimeSet = false;


    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addtask);

        //Setup View Binding variable
        binding = ActivityModifytaskBinding.inflate(getLayoutInflater());
        View v = binding.getRoot();
        setContentView(v);

        getSupportActionBar().hide();
        // Récupérer les éléments du xml
        button_back = binding.back2;
        button_save = binding.ModifyButton;
        button_date = binding.Datebutton;
        button_time = binding.Timebutton;


        titleTask = binding.TitleInputField;
        descTask = binding.DescInputField;
        ReminderBroadcast.createNotificationChannel(this);

        String titleIntent = getIntent().getStringExtra("Title");
        String descIntent = getIntent().getStringExtra("Desc");
        yearIntent = getIntent().getIntExtra("Year", 2020);
        monthIntent = getIntent().getIntExtra("Month", 1);
        dayIntent = getIntent().getIntExtra("Day", 1);
        hourIntent = getIntent().getIntExtra("Hour", 0);
        minuteIntent = getIntent().getIntExtra("Minute", 0);
        int goldIntent = getIntent().getIntExtra("Gold", 0);
        taskId = getIntent().getStringExtra("TaskId");
        String modeIntent = getIntent().getStringExtra("Mode");
        origin = getIntent().getStringExtra("origin");

        Log.i("taskmodify", String.valueOf(dayIntent));
        titleTask.setText(titleIntent);
        descTask.setText(descIntent);

        initDatePicker();

        switch (modeIntent) {
            case "Once":
                posmodespin = 0;
                break;
            case "Daily":
                posmodespin = 1;
                break;
            case "Weekly":
                posmodespin = 2;
                break;
            case "Monthly":
                posmodespin = 3;
                break;
            case "Yearly":
                posmodespin = 4;
                break;
            default:
                posmodespin = 0;
                break;
        }

        switch (goldIntent) {
            case 10:
                posdifspin = 0;
                break;
            case 30:
                posdifspin = 1;
                break;
            case 50:
                posdifspin = 2;
                break;
            case 70:
                posdifspin = 3;
                break;
            default:
                posdifspin = 0;
                break;
        }

        Spinner difspinner = (Spinner) findViewById(R.id.difficulty_spinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.difficulty_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        difspinner.setAdapter(adapter);
        difspinner.setSelection(posdifspin);
        difspinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int pos, long l) {
                difficulty = adapterView.getItemAtPosition(pos).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                difficulty = adapterView.getItemAtPosition(0).toString();
            }
        });

        Spinner modespinner = (Spinner) findViewById(R.id.mode_spinner);
        ArrayAdapter<CharSequence> adapter2 = ArrayAdapter.createFromResource(this,
                R.array.mode_array, android.R.layout.simple_spinner_item);
        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        modespinner.setAdapter(adapter2);
        modespinner.setSelection(posmodespin);
        modespinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int pos, long l) {
                mode = adapterView.getItemAtPosition(pos).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                mode = adapterView.getItemAtPosition(0).toString();
            }
        });

        localtime = LocalTime.of(0, 0);
        // Mettre en place les listeners
        // Appuyer le bouton nous envoie vers un autre activité
        
        button_back.setOnClickListener(view ->
                startActivity(new Intent(this, TaskActivity.class))
        );


        button_time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                popTimePicker(view);
                isTimeSet = true;
            }
        });

        button_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openDatePicker(view);
                isDateSet = true;

            }
        });

        button_save.setOnClickListener(new View.OnClickListener() {

                                           @RequiresApi(api = Build.VERSION_CODES.O)
                                           @Override
                                           public void onClick(View view) {
                                               String Title = titleTask.getText().toString();
                                               String Desc = descTask.getText().toString();
                                               switch (difficulty) {
                                                   case "Easy":
                                                       gold = 10;
                                                       xp = 10;
                                                       break;
                                                   case "Medium":
                                                       gold = 30;
                                                       xp = 30;
                                                       break;
                                                   case "Hard":
                                                       gold = 50;
                                                       xp = 50;
                                                       break;
                                                   case "Very Hard":
                                                       gold = 70;
                                                       xp = 70;
                                                       break;
                                                   default:
                                                       gold = 5;
                                                       xp = 5;
                                               }

                                               // Check if date and time are set
                                               if (!isDateSet || !isTimeSet) {
                                                   Toast.makeText(ModifyTaskActivity.this, "Please set a due date and time before saving the change.", Toast.LENGTH_SHORT).show();
                                                   return;
                                               }
                                               if (Title.isEmpty()) {
                                                   Title = "Unnamed Task";
                                               }
                                               if (Desc.isEmpty()) {
                                                   Desc = "";
                                               }
                                               int year = localdate.getYear();
                                               int month = localdate.getMonthValue();
                                               int day = localdate.getDayOfMonth();
                                               int hour = localtime.getHour();
                                               int minute = localtime.getMinute();
                                               Log.i("taskid : ", taskId);
                                               docref.collection("Tasks").document(taskId).update("title", Title,
                                                       "description", Desc,
                                                       "year", year,
                                                       "month", month,
                                                       "day", day,
                                                       "hour", hour,
                                                       "minute", minute,
                                                       "goldreward", gold,
                                                       "xpreward", xp,
                                                       "mode", mode);
                                               //finish();
                                               if (origin.equals("taskactivity")) {
                                                   startActivity(new Intent(getApplicationContext(), TaskActivity.class));
                                               } else {
                                                   startActivity(new Intent(getApplicationContext(), MainActivity.class));
                                               }

                                           }
                                       }
        );
    }

    public void popTimePicker(View view) {
        TimePickerDialog.OnTimeSetListener onTimeSetListener = new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                hour = selectedHour;
                minute = selectedMinute;
                button_time.setText(String.format(Locale.getDefault(), "%02d:%02d", hour, minute));
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    localtime = LocalTime.of(hour, minute);
                }
            }
        };

        TimePickerDialog timePickerDialog = new TimePickerDialog(this, onTimeSetListener, hour, minute, true);
        timePickerDialog.updateTime(hourIntent, minuteIntent);
        timePickerDialog.setTitle("Select Time");
        timePickerDialog.show();
    }

    private void initDatePicker() {
        DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                // month = month + 1;
                String date = makeDateString(day, month, year);
                button_date.setText(date);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    localdate = of(year, month + 1, day);
                }
            }
        };
        Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        int day = cal.get(Calendar.DAY_OF_MONTH);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            localdate = LocalDate.now();
            year = localdate.getYear();
            month = localdate.getMonthValue() - 1;
            day = localdate.getDayOfMonth();
        }
        int style = AlertDialog.THEME_HOLO_LIGHT;

        datePickerDialog = new DatePickerDialog(this, style, dateSetListener, year, month, day);
        //datePickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis());

    }

    private String makeDateString(int day, int month, int year) {
        return getMonthFormat(month + 1) + " " + day + " " + year;
    }

    private String getMonthFormat(int month) {
        String[] monthNames = {"JAN", "FEB", "MAR", "APR", "MAY", "JUN", "JUL", "AUG", "SEP", "OCT", "NOV", "DEC"};
        return monthNames[month - 1];
    }

    public void openDatePicker(View view) {
        datePickerDialog.show();
    }
}
