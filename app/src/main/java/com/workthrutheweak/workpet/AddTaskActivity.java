package com.workthrutheweak.workpet;

import android.content.Intent;
import android.icu.util.Calendar;
import android.os.Bundle;
import android.provider.CalendarContract;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.EditText;

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
import java.util.List;
import java.util.TimerTask;

public class AddTaskActivity extends AppCompatActivity {
    // Variables
    private ActivityAddtaskBinding binding;//For ViewBinding feature
    Button button_back;
    FloatingActionButton button_add;

    EditText dateTask;
    EditText timeTask;
    EditText titleTask;
    EditText descTask;
    EditText goldTask;
    EditText xpTask;
    CalendarView calendarView;
    long datelong;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addtask);

        //Setup View Binding variable
        binding = ActivityAddtaskBinding.inflate(getLayoutInflater());
        View v = binding.getRoot();
        setContentView(v);

        getSupportActionBar().hide();
        // Récupérer les éléments du xml
        button_back = binding.back2;

        button_add = binding.AddButton;

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


        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            //show the selected date as a toast
            @Override
            public void onSelectedDayChange(CalendarView view, int year, int month, int day) {
                Calendar c = Calendar.getInstance();
                c.set(year, month, day);
                datelong = c.getTimeInMillis();
            }
        });
        button_add.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //SaveData();
                    String Title = titleTask.getText().toString();
                    String Desc = descTask.getText().toString();
                    String Date = dateTask.getText().toString();
                    String Time = timeTask.getText().toString();
                    String Gold = goldTask.getText().toString();
                    String XP = xpTask.getText().toString();
                    Task newTask = new Task(Title,Desc,Date,Time,Gold,XP,false );
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

                    Intent intent = new Intent(AddTaskActivity.this, TaskActivity.class);
                    intent.putExtra("Task", newTask);
                    startActivity(intent);
                }
            }
        );
    }
}
