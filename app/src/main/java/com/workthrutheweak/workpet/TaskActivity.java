package com.workthrutheweak.workpet;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.res.AssetManager;
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
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
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
import java.util.ArrayList;
import java.util.List;

public class TaskActivity extends AppCompatActivity {

    // Variables
    private ActivityTaskBinding binding;//For ViewBinding feature
    Button button_back;

    FloatingActionButton button_addT;
    TextView textView;
    List<Task> TaskList;

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
        File path = getApplicationContext().getFilesDir();
        try {
            FileInputStream fis = new FileInputStream(new File(path, "tasklist.json"));
            TaskList = readJsonStream(fis);
        } catch (IOException e) {
            TaskList = new ArrayList<>();
            TaskList.add(new Task("Bien débuter", "N'hésiter pas à remplir votre tableau", "Aujourd'hui", "10 gold et 10XP", false));
        }

        /*File jsontask = new File("tasklist.json");

        if (jsontask.exists()) {
            try {
                FileReader fileReader = new FileReader(jsontask);
                Type type = new TypeToken<ArrayList<Task>>() {
                }.getType();
                Gson gson = new Gson();
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

        try {
            FileWriter filewriter = new FileWriter("tasklist.json");
            Gson gson = new Gson();
            gson.toJson(TaskList, filewriter);
            filewriter.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }*/

        Task task = getIntent().getParcelableExtra("Task");
        if (task != null) {
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
            writeJsonStream(fos, TaskList);
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

    @Override
    protected void onPause() {
        super.onPause();

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
            writeJsonStream(fos, TaskList);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void writeJsonStream(OutputStream out, List<Task> taskList) throws IOException {
        JsonWriter writer = new JsonWriter(new OutputStreamWriter(out, "UTF-8"));
        writer.setIndent("  ");
        writeTaskList(writer, taskList);
        writer.close();
    }

    public void writeTaskList(JsonWriter writer, List<Task> taskList) throws IOException {
        writer.beginArray();
        for (Task task : taskList) {
            writeTask(writer, task);
        }
        writer.endArray();
    }

    public void writeTask(JsonWriter writer, Task task) throws IOException {
        writer.beginObject();
        writer.name("Title").value(task.get_title());
        writer.name("Description").value(task.get_description());
        writer.name("Date").value(task.get_date());
        writer.name("Reward").value(task.get_reward());
        writer.name("isTaskDone").value(task.isTaskDone());
        writer.endObject();
    }

    public List<Task> readJsonStream(InputStream in) throws IOException {
        JsonReader reader = new JsonReader(new InputStreamReader(in, "UTF-8"));
        try {
            return readTaskList(reader);
        } finally {
            reader.close();
        }
    }

    public List<Task> readTaskList(JsonReader reader) throws IOException {
        List<Task> taskList = new ArrayList<Task>();

        reader.beginArray();
        while (reader.hasNext()) {
            taskList.add(readTask(reader));
        }
        reader.endArray();
        return taskList;
    }

    public Task readTask(JsonReader reader) throws IOException {
        String title = null;
        String description = null;
        String date = null;
        String reward = null;
        Boolean isTaskDone = null;

        reader.beginObject();
        while (reader.hasNext()) {
            String name = reader.nextName();
            if (name.equals("Title")) {
                title = reader.nextString();
            } else if (name.equals("Description")) {
                description = reader.nextString();
            } else if (name.equals("Date")) {
                date = reader.nextString();
            } else if (name.equals("Reward")) {
                reward = reader.nextString();
            } else if (name.equals("isTaskDone")) {
                isTaskDone = reader.nextBoolean();
            }else {
                reader.skipValue();
            }
        }
        reader.endObject();
        return new Task(title, description, date, reward, isTaskDone);
    }
}
