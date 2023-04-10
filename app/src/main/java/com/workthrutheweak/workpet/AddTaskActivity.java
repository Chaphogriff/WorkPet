package com.workthrutheweak.workpet;

import static java.time.LocalDate.of;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.icu.util.Calendar;
import android.os.Build;
import android.os.Bundle;
import android.provider.CalendarContract;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.workthrutheweak.workpet.adapter.TaskAdapter;
import com.workthrutheweak.workpet.data.Datasource;
import com.workthrutheweak.workpet.databinding.ActivityAddtaskBinding;
import com.workthrutheweak.workpet.databinding.ActivityTaskBinding;
import com.workthrutheweak.workpet.model.Task;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.TimerTask;

public class AddTaskActivity extends AppCompatActivity {
    // Variables
    private ActivityAddtaskBinding binding;//For ViewBinding feature
    private DatePickerDialog datePickerDialog;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    private static final String KEY_TITLE = "title";
    private static final String KEY_DESCRITPION = "description";
    private static final String KEY_DATE = "date";
    private static final String KEY_TIME = "time";
    private static final String KEY_GOLD = "gold";
    private static final String KEY_XP = "experience";
    private static final String KEY_DONE = "IsTaskDone";
    private static final String KEY_REPEAT = "repeat";

    Button button_back;
    Button button_time;
    Button button_date;
    FloatingActionButton button_add;
    EditText titleTask;
    EditText descTask;
    EditText goldTask;
    EditText xpTask;
    LocalDate localdate;
    LocalTime localtime;
    int hour, minute;

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
        goldTask = binding.GoldTextInput;
        xpTask = binding.XPTextInput;

        localtime = LocalTime.of(0,0);
        // Mettre en place les listeners
        // Appuyer le bouton nous envoie vers un autre activité
        button_back.setOnClickListener(view ->
                startActivity(new Intent(this, TaskActivity.class))
        );

        button_time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                popTimePicker(view);
            }
        });

        button_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openDatePicker(view);
            }
        });
        button_add.setOnClickListener(new View.OnClickListener() {
                                          @RequiresApi(api = Build.VERSION_CODES.O)
                                          @Override
                                          public void onClick(View view) {
                                              String Title = titleTask.getText().toString();
                                              String Desc = descTask.getText().toString();
                                              String goldText = goldTask.getText().toString();
                                              String xpText = xpTask.getText().toString();
                                              LocalDate ld = localdate;
                                              LocalTime lt = localtime;
                                              int gold = 0;
                                              if (!goldText.isEmpty()) {
                                                  gold = Integer.parseInt(goldTask.getText().toString());
                                              }
                                              int xp = 0;
                                              if (!xpText.isEmpty()) {
                                                  xp = Integer.parseInt(xpTask.getText().toString());                                              }

                                              if ( Title.isEmpty()) {
                                                  Title = "Unnamed Task";
                                              }
                                              if ( Desc.isEmpty()) {
                                                  Desc = "";
                                              }

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
                                              startActivity(intent);
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
                month = month + 1;
                String date = makeDateString(day, month, year);
                button_date.setText(date);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    localdate = of(year, month, day);
                }
            }
        };
        Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        int day = cal.get(Calendar.DAY_OF_MONTH);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            localdate = of(year, month + 1, day);
        }
        int style = AlertDialog.THEME_HOLO_LIGHT;

        datePickerDialog = new DatePickerDialog(this, style, dateSetListener, year, month, day);
        //datePickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis());

    }

    private String makeDateString(int day, int month, int year) {
        return getMonthFormat(month) + " " + day + " " + year;
    }

    private String getMonthFormat(int month) {
        if (month == 1)
            return "JAN";
        if (month == 2)
            return "FEB";
        if (month == 3)
            return "MAR";
        if (month == 4)
            return "APR";
        if (month == 5)
            return "MAY";
        if (month == 6)
            return "JUN";
        if (month == 7)
            return "JUL";
        if (month == 8)
            return "AUG";
        if (month == 9)
            return "SEP";
        if (month == 10)
            return "OCT";
        if (month == 11)
            return "NOV";
        if (month == 12)
            return "DEC";

        //default should never happen
        return "JAN";
    }

    public void openDatePicker(View view) {
        datePickerDialog.show();
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void saveTask(){
        String Title = titleTask.getText().toString();
        String Desc = descTask.getText().toString();
        String goldText = goldTask.getText().toString();
        String xpText = xpTask.getText().toString();
        String repeat = "to do";
        boolean istaskdone = false;
        LocalDate ld = localdate;
        LocalTime lt = localtime;
        int gold = 0;
        if (!goldText.isEmpty()) {
            gold = Integer.parseInt(goldTask.getText().toString());
        }
        int xp = 0;
        if (!xpText.isEmpty()) {
            xp = Integer.parseInt(xpTask.getText().toString());                                              }

        if ( Title.isEmpty()) {
            Title = "Unnamed Task";
        }
        if ( Desc.isEmpty()) {
            Desc = "";
        }
        int year = localdate.getYear();
        int month = localdate.getMonthValue();
        int day = localdate.getDayOfMonth();
        int hour = localtime.getHour();
        int minute = localtime.getMinute();

        Map<String, Object> task = new HashMap<>();
        task.put(KEY_TITLE, Title);
        task.put(KEY_DESCRITPION, Desc);
        task.put(KEY_DATE, localdate);
        task.put(KEY_TIME, localtime);
        task.put(KEY_GOLD, goldText);
        task.put(KEY_XP, xpText);
        task.put(KEY_DONE, istaskdone);
        task.put(KEY_REPEAT, repeat);

        db.collection("Users").document(user.getUid()).collection("Tasks").document().set(task)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Toast.makeText(AddTaskActivity.this,"Task added",Toast.LENGTH_SHORT).show();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(AddTaskActivity.this,"Failed to add task",Toast.LENGTH_SHORT).show();
                    }
                });
    }
}
