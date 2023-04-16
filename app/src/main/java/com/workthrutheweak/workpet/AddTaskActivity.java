package com.workthrutheweak.workpet;

import static java.time.LocalDate.of;

import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.icu.util.Calendar;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.workthrutheweak.workpet.databinding.ActivityAddtaskBinding;
import com.workthrutheweak.workpet.model.Task;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Locale;

public class AddTaskActivity extends AppCompatActivity {
    private ActivityAddtaskBinding binding;//For ViewBinding feature
    private DatePickerDialog datePickerDialog;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    private DocumentReference docref = db.collection("Users").document(user.getUid());
    Button button_back;
    Button button_time;
    Button button_date;
    FloatingActionButton button_add;
    EditText titleTask;
    EditText descTask;
    LocalDate localdate;
    LocalTime localtime;
    int hour, minute;
    String difficulty;
    String mode;
    int gold, xp;
    boolean isDateSet = false;
    boolean isTimeSet = false;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addtask);

        initDatePicker();

        //Setup View Binding variable
        binding = ActivityAddtaskBinding.inflate(getLayoutInflater());
        View v = binding.getRoot();
        setContentView(v);

        getSupportActionBar().hide();
        // Récupérer les éléments du xml
        button_back = binding.back2;
        button_add = binding.AddButton;
        button_date = binding.Datebutton;
        button_time = binding.Timebutton;
        titleTask = binding.TitleInputField;
        descTask = binding.DescInputField;

        Spinner difspinner = (Spinner) findViewById(R.id.difficulty_spinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.difficulty_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        difspinner.setAdapter(adapter);
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
        button_add.setOnClickListener(new View.OnClickListener() {
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
                                                      gold = 10;
                                                      xp = 10;
                                              }
                                              if (Title.isEmpty()) {
                                                  Title = "Unnamed Task";
                                              }
                                              if (Desc.isEmpty()) {
                                                  Desc = "";
                                              }

                                              // Initialize localdate and localtime
                                              LocalDate localdate = LocalDate.now();
                                              LocalTime localtime = LocalTime.now();

                                              // Check if date and time are set
                                              if (!isDateSet || !isTimeSet) {
                                                  Toast.makeText(AddTaskActivity.this, "Please set a due date and time for the task", Toast.LENGTH_SHORT).show();
                                                  return;
                                              }

                                              saveTask();
                                              Intent intent = new Intent(AddTaskActivity.this, TaskActivity.class);
                                              intent.putExtra("Title", Title);
                                              intent.putExtra("Desc", Desc);
                                              intent.putExtra("Year", localdate.getYear());
                                              intent.putExtra("Month", localdate.getMonthValue());
                                              intent.putExtra("Day", localdate.getDayOfMonth());
                                              intent.putExtra("Hour", localtime.getHour());
                                              intent.putExtra("Minute", localtime.getMinute());
                                              intent.putExtra("Gold", gold);
                                              intent.putExtra("XP", xp);
                                              intent.putExtra("isTaskDone", false);
                                              intent.putExtra("Mode", mode);
                                              startActivity(intent);

                                              long when = System.currentTimeMillis() + 6000L;

                                              AlarmManager am = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
                                              Intent intent2 = new Intent(AddTaskActivity.this, AlarmReceiver.class);
                                              intent.putExtra("myAction", "mDoNotify");
                                              PendingIntent pendingIntent = PendingIntent.getBroadcast(AddTaskActivity.this, 0, intent2, PendingIntent.FLAG_IMMUTABLE);
                                              am.set(AlarmManager.RTC_WAKEUP, when, pendingIntent);
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

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void saveTask() {
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
                gold = 10;
                xp = 10;
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


        Task task = new Task(Title, Desc, year, month, day, hour, minute, gold, xp, false, mode);

        docref.collection("Tasks").add(task).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
            @Override
            public void onSuccess(DocumentReference documentReference) {
                Toast.makeText(AddTaskActivity.this, "Task added", Toast.LENGTH_SHORT).show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(AddTaskActivity.this, "Failed to add task", Toast.LENGTH_SHORT).show();
            }
        });


    }


}
