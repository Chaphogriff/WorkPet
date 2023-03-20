package com.workthrutheweak.workpet;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.workthrutheweak.workpet.adapter.TaskAdapter;
import com.workthrutheweak.workpet.data.Datasource;
import com.workthrutheweak.workpet.databinding.ActivityTaskBinding;
import com.workthrutheweak.workpet.model.Task;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class TaskActivity extends AppCompatActivity {

    // Variables
    private ActivityTaskBinding binding;//For ViewBinding feature
    Button button_back;

    FloatingActionButton button_addT;
    TextView textView;
    static List<Task> TaskList;
    Gson gson;
    File jsontask;

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

        jsontask = new File("tasklist.json");

        if (jsontask.exists()) {
            try {
                FileReader fileReader = new FileReader(jsontask);
                Type type = new TypeToken<ArrayList<Task>>() {
                }.getType();
                gson = new Gson();
                TaskList = gson.fromJson(fileReader, type);
                fileReader.close();
            } catch (FileNotFoundException e) {
                throw new RuntimeException(e);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        } else {
            TaskList = new ArrayList<>();
            TaskList.add(new Task("Bien débuter", "N'hésiter pas à remplir votre tableau", "Aujourd'hui", "10 gold et 10XP"));
        }

        gson = new Gson();
        Task task = getIntent().getParcelableExtra("Task");
        TaskList = new Datasource().loadTasks();
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
        @SuppressLint({"MissingInflatedId", "LocalSuppress"}) BottomNavigationView bottomNavigationView = findViewById(R.id.nav);
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

    @Override
    public void onPause() {
        super.onPause();
        if (isFinishing()) {

            try {
                FileWriter filewriter = new FileWriter(jsontask);
                gson.toJson(TaskList, filewriter);
                filewriter.close();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
