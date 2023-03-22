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

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
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
import java.util.List;
import java.util.Locale;
import java.util.TimerTask;

public class AddTaskActivity extends AppCompatActivity {
    // Variables
    private ActivityAddtaskBinding binding;//For ViewBinding feature
    private DatePickerDialog datePickerDialog;
    Button button_back;
    Button button_time;
    Button button_date;
    FloatingActionButton button_add;
    EditText dateTask;
    EditText timeTask;
    EditText titleTask;
    EditText descTask;
    EditText goldTask;
    EditText xpTask;
    CalendarView calendarView;
    LocalDate localdate;
    LocalTime localtime;
    int hour, minute;

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

        dateTask = binding.editTextDate;
        timeTask = binding.editTextTime;
        titleTask = binding.TitleInputField;
        descTask = binding.DescInputField;
        goldTask = binding.GoldTextInput;
        xpTask = binding.XPTextInput;
        calendarView = binding.DatePicker;

        // Mettre en place les listeners

        // Appuyer le bouton nous envoie vers un autre activité
        button_back.setOnClickListener(view ->
                startActivity(new Intent(this, TaskActivity.class))
        );

        /*
        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            //show the selected date as a toast
            @Override
            public void onSelectedDayChange(CalendarView view, int year, int month, int day) {
                Calendar c = Calendar.getInstance();
                c.set(year, month, day);
                datelong = c.getTimeInMillis();
            }
        });*/

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
                    //SaveData();
                    String Title = titleTask.getText().toString();
                    String Desc = descTask.getText().toString();
                    String Date = dateTask.getText().toString();
                    String Time = timeTask.getText().toString();
                    String Gold = goldTask.getText().toString();
                    String XP = xpTask.getText().toString();
                    LocalDate ld = localdate;
                    LocalTime lt = localtime;
                    int gold = Integer.parseInt(goldTask.getText().toString());
                    int xp = Integer.parseInt(xpTask.getText().toString());
                    if (localdate==null){
                        Log.i("addTaskActivity",  "localdate is null 3");
                    }
                    //Task newTask = new Task(Title,Desc,Date,Time,Gold,XP,false );
                    Task newtask = new Task(Title,Desc,ld,lt,gold,xp,false);
                    /*Intent calendarIntent = new Intent(Intent.ACTION_INSERT);
                    if (!Title.isEmpty() && !Desc.isEmpty()) {
                        calendarIntent.setData(CalendarContract.Events.CONTENT_URI);
                        calendarIntent.putExtra(CalendarContract.Events.TITLE, Title);
                        calendarIntent.putExtra(CalendarContract.Events.DESCRIPTION, Desc);
                        calendarIntent.putExtra(CalendarContract.Events.DTSTART, datelong);
                        calendarIntent.putExtra(CalendarContract.Events.ALL_DAY, true);
                        Log.i("AddTaskActivity", "not empty");
                    }

                    if (calendarIntent.resolveActivity(getPackageManager()) != null){
                        Log.i("AddTaskActivity", "in if statement");
                        startActivity(calendarIntent);
                    } else {
                        Log.i("AddTaskActivity", "not in if statement");
                    }*/
                    if (newtask.getGoldreward()==3){
                        Log.i("addTaskActivity",  "Bonne reward");
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

    public void popTimePicker(View view)
    {
        TimePickerDialog.OnTimeSetListener onTimeSetListener = new TimePickerDialog.OnTimeSetListener()
        {
            @Override
            public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute)
            {
                hour = selectedHour;
                minute = selectedMinute;
                button_time.setText(String.format(Locale.getDefault(), "%02d:%02d",hour, minute));
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    localtime = LocalTime.of(hour,minute);
                }
            }
        };

        TimePickerDialog timePickerDialog = new TimePickerDialog(this, onTimeSetListener, hour, minute, true);

        timePickerDialog.setTitle("Select Time");
        timePickerDialog.show();
    }

    private String getTodaysDate()
    {
        Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        month = month + 1;
        int day = cal.get(Calendar.DAY_OF_MONTH);
        return makeDateString(day, month, year);
    }

    private void initDatePicker()
    {
        Log.i("addTaskActivity", " got near the date");
        DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener()
        {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day)
            {
                month = month + 1;
                String date = makeDateString(day, month, year);
                button_date.setText(date);
            }
        };
        Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        int day = cal.get(Calendar.DAY_OF_MONTH);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            localdate = of(year, month, day);
            Log.i("addTaskActivity", " got the date");
        }
        if (localdate==null){
            Log.i("addTaskActivity",  "localdate is null");
        }
        int style = AlertDialog.THEME_HOLO_LIGHT;

        datePickerDialog = new DatePickerDialog(this, style, dateSetListener, year, month, day);
        //datePickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis());

    }

    private String makeDateString(int day, int month, int year)
    {
        return getMonthFormat(month) + " " + day + " " + year;
    }

    private String getMonthFormat(int month)
    {
        if(month == 1)
            return "JAN";
        if(month == 2)
            return "FEB";
        if(month == 3)
            return "MAR";
        if(month == 4)
            return "APR";
        if(month == 5)
            return "MAY";
        if(month == 6)
            return "JUN";
        if(month == 7)
            return "JUL";
        if(month == 8)
            return "AUG";
        if(month == 9)
            return "SEP";
        if(month == 10)
            return "OCT";
        if(month == 11)
            return "NOV";
        if(month == 12)
            return "DEC";

        //default should never happen
        return "JAN";
    }

    public void openDatePicker(View view)
    {
        datePickerDialog.show();
    }
}
