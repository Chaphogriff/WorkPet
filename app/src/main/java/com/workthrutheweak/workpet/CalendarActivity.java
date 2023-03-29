package com.workthrutheweak.workpet;


import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.TextView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.workthrutheweak.workpet.JsonManagement.JsonManager;
import com.workthrutheweak.workpet.adapter.TaskAdapter;
import com.workthrutheweak.workpet.databinding.ActivityCalendarBinding;
import com.workthrutheweak.workpet.model.Task;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;


public class CalendarActivity extends AppCompatActivity {

    // Variables
    private ActivityCalendarBinding binding; //For ViewBinding feature
    private AlertDialog.Builder dialogbuilder;
    private AlertDialog dialog;
    private TextView popup_title;
    private RecyclerView recyclerView;
    private Button button_popup_back;
    private List<Task> taskList;
    private List<Task> taskList_popup;
    Button button_back;
    CalendarView calendarView;
    long date;


    @SuppressLint("NewApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar);

        //Setup View Binding variable
        binding = ActivityCalendarBinding.inflate(getLayoutInflater());
        View v = binding.getRoot();
        setContentView(v);

        // Récupérer les éléments du xml
        button_back = binding.back;
        calendarView = binding.calendar;

        // Mettre en place les listeners
        calendarView.setDate(date);

        File path = getApplicationContext().getFilesDir();
        FileInputStream fis = null;
        try {
            fis = new FileInputStream(new File(path, "tasklist.json"));
            taskList = JsonManager.readJsonStream(fis);
        } catch (IOException e) {
            System.out.println(e);
            taskList = new ArrayList<>();
            LocalDate localDate = LocalDate.ofYearDay(2023, 1);
            LocalTime localTime = LocalTime.of(0, 0);
            taskList.add(new Task("Bien débuter", "N'hésiter pas à remplir votre tableau", localDate, localTime, 10, 10, false));
        }

        taskList_popup = new ArrayList<Task>();

        // Appuyer le bouton nous envoie vers un autre activité
        button_back.setOnClickListener(view ->
                startActivity(new Intent(this, MainActivity.class))
        );

        // Récupération de la date actuelle
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);

        // Affichage de la date actuelle sur le CalendarView
        calendarView.setDate(calendar.getTimeInMillis());

        // Ajout d'un listener pour détecter le changement de date sur le CalendarView
        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth) {

                // Récupération de la date sélectionnée sur le CalendarView
                Calendar selectedDate = Calendar.getInstance();
                selectedDate.set(year, month, dayOfMonth);
                LocalDate localDate_calendar = LocalDate.of(year, month + 1, dayOfMonth);

                // Affichage de la date sélectionnée dans la console
                SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
                Log.d("Selected date", sdf.format(selectedDate.getTime()));

                taskList_popup.clear();

                if (taskList != null) {
                    if (!taskList.isEmpty()) {
                        for (Task task : taskList) {
                            LocalDate localDate_task = task.getLocalDate();
                            if (localDate_calendar.getDayOfMonth() == localDate_task.getDayOfMonth()
                                    && localDate_calendar.getMonthValue() == localDate_task.getMonthValue()
                                    && localDate_calendar.getYear() == localDate_task.getYear()) {
                                taskList_popup.add(task);
                            }
                        }
                    }
                }

                if (!taskList_popup.isEmpty()) {
                    createPopupDialog();
                }
            }
        });


        BottomNavigationView bottomNavigationView = binding.nav;
        bottomNavigationView.setSelectedItemId(R.id.calendar);
        //ajout du navbar
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                switch (item.getItemId()) {
                    case R.id.calendar:
                        return true;
                    case R.id.avatar:
                        startActivity(new Intent(getApplicationContext(), AvatarActivity.class));
                        overridePendingTransition(0, 0);
                        return true;
                    case R.id.home:
                        startActivity(new Intent(getApplicationContext(), MainActivity.class));
                        overridePendingTransition(0, 0);
                        return true;
                    case R.id.setting:
                        startActivity(new Intent(getApplicationContext(), SettingActivity.class));
                        overridePendingTransition(0, 0);
                        return true;
                    case R.id.task:
                        startActivity(new Intent(getApplicationContext(), TaskActivity.class));
                        overridePendingTransition(0, 0);
                        return true;
                }
                return false;
            }


        });
    }

    public void createPopupDialog() {
        dialogbuilder = new AlertDialog.Builder(this);
        final View calendarPopupView = getLayoutInflater().inflate(R.layout.activity_calendar_popup, null);
        //dialog;
        popup_title = (TextView) calendarPopupView.findViewById(R.id.daytask);
        recyclerView = (RecyclerView) calendarPopupView.findViewById(R.id.tasksRecyclerView);
        recyclerView.setAdapter(new TaskAdapter(this, taskList_popup));
        button_popup_back = (Button) calendarPopupView.findViewById(R.id.back);
        dialogbuilder.setView(calendarPopupView);
        dialog = dialogbuilder.create();
        dialog.show();

        button_popup_back.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(View view) {
                if (taskList != null) {
                    if (!taskList.isEmpty()) {
                        for (Task task_popup : taskList_popup) {
                            for (Task task : taskList) {
                                if (task_popup.isTaskDone && task_popup.get_title() == task.get_title()) {
                                    taskList.remove(task);
                                }
                            }
                        }
                    }
                }
                File path = getApplicationContext().getFilesDir();
                try {
                    FileOutputStream fos = new FileOutputStream(new File(path, "tasklist.json"));
                    JsonManager.writeJsonStream(fos, taskList);
                } catch (IOException e) {
                    System.out.println(e);
                }
                dialog.dismiss();
            }
        });
    }
}
