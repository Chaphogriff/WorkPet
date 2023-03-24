package com.workthrutheweak.workpet;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.res.AssetManager;
import android.os.Build;
import android.os.Bundle;
import android.util.JsonReader;
import android.util.JsonWriter;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.workthrutheweak.workpet.JsonManagement.JsonManager;
import com.workthrutheweak.workpet.adapter.TaskAdapter;
import com.workthrutheweak.workpet.data.Datasource;
import com.workthrutheweak.workpet.databinding.ActivityTaskBinding;
import com.workthrutheweak.workpet.model.Task;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.lang.reflect.Type;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

public class TaskActivity extends AppCompatActivity {

    // Variables
    private ActivityTaskBinding binding;//For ViewBinding feature
    JsonManager JsonM = new JsonManager();
    Button button_back;
    FloatingActionButton button_addT,edit;
    TextView textView;
    List<Task> TaskList;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task);

        //Setup View Binding variable
        binding = ActivityTaskBinding.inflate(getLayoutInflater());
        View v = binding.getRoot();
        setContentView(v);

        getSupportActionBar().hide();
        // Récupérer les éléments du xml
        button_back = binding.back;
        button_addT = binding.fab;
        textView = binding.tasksText;
        edit=binding.edit;

        File path = getApplicationContext().getFilesDir();
        FileInputStream fis = null;
        try {
            fis = new FileInputStream(new File(path, "tasklist.json"));
            TaskList = JsonManager.readJsonStream(fis);
        } catch (IOException e) {
            System.out.println(e);
            TaskList = new ArrayList<>();
            LocalDate localDate = LocalDate.ofYearDay(2023,1);
            LocalTime localTime = LocalTime.of(0,0);
            TaskList.add(new Task("Bien débuter", "N'hésiter pas à remplir votre tableau", localDate, localTime, 10, 10, false));
        }

        String titleIntent = getIntent().getStringExtra("Title");
        String descIntent = getIntent().getStringExtra("Desc");
        int yearIntent = getIntent().getIntExtra("Year", 0);
        int monthIntent = getIntent().getIntExtra("Month",0);
        int dayIntent = getIntent().getIntExtra("Day",0);
        int hourIntent = getIntent().getIntExtra("Hour",0);
        int minuteIntent = getIntent().getIntExtra("Minute",0);
        int goldIntent = getIntent().getIntExtra("Gold",0);
        int xpIntent = getIntent().getIntExtra("XP",0);
        if (yearIntent != 0) {
            LocalTime localTimeI = LocalTime.of(hourIntent,minuteIntent);
            LocalDate localDateI = LocalDate.of(yearIntent,monthIntent,dayIntent);
            Task task = new Task(titleIntent,descIntent,localDateI,localTimeI,goldIntent,xpIntent,false);
            TaskList.add(task);

        }

        RecyclerView recyclerView = binding.tasksRecyclerView;
        recyclerView.setAdapter(new TaskAdapter(this, TaskList));
        // Mettre en place les listeners



        // Appuyer le bouton nous envoie vers un autre activité
        button_back.setOnClickListener(view ->
                startActivity(new Intent(this, MainActivity.class))
        );

        button_addT.setOnClickListener(view ->
                startActivity(new Intent(this, AddTaskActivity.class))
        );

        try {
            FileOutputStream fos = new FileOutputStream(new File(path, "tasklist.json"));
            JsonManager.writeJsonStream(fos, TaskList);
            Log.i("app","saving");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        @SuppressLint({"MissingInflatedId", "LocalSuppress"}) BottomNavigationView bottomNavigationView = binding.nav;
        bottomNavigationView.setSelectedItemId(R.id.task);
        //ajout du navbar
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                switch (item.getItemId()) {
                    case R.id.calendar:
                        startActivity(new Intent(getApplicationContext(), CalendarActivity.class));
                        overridePendingTransition(0, 0);
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
                        return true;
                }
                return false;
            }
        });
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onPause() {
        if (TaskList!=null){
            if (!TaskList.isEmpty()){
                for (Task task : TaskList) {
                    if (task.isTaskDone) {
                        TaskList.remove(task);
                    }
                }
            }
        }
        File path = getApplicationContext().getFilesDir();
        try {
            FileOutputStream fos = new FileOutputStream(new File(path, "tasklist.json"));
            JsonManager.writeJsonStream(fos, TaskList);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        super.onPause();
    }
}
