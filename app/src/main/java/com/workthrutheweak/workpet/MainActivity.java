package com.workthrutheweak.workpet;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.JsonReader;
import android.util.JsonWriter;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.workthrutheweak.workpet.JsonManagement.JsonManager;
import com.workthrutheweak.workpet.adapter.TaskAdapter;
import com.workthrutheweak.workpet.data.Datasource;
import com.workthrutheweak.workpet.databinding.ActivityMainBinding;
import com.workthrutheweak.workpet.model.Task;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class MainActivity extends AppCompatActivity {

    // Variables
    private ActivityMainBinding binding; //For ViewBinding feature
    List<Task> TaskList;
    TextView goldTextView;
    TextView levelTextView;
    ProgressBar progressBar;
    RecyclerView recyclerView;
    int gold=99;
    int level=2;
    int exp=75; // entre 0 et 100 ! ( si > 100, on level up )


    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Setup View Binding variable
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        View v = binding.getRoot();
        setContentView(v);

        // Récupérer les éléments du xml
        recyclerView = binding.recyclerView;
        goldTextView = binding.goldTextHome;
        levelTextView = binding.lvlText;
        progressBar = binding.expBar;

        // Récupérer les données depuis des fichiers .json
        recoverDataFromJson();

        // Set valeurs
        refreshProfileVar();

        BottomNavigationView bottomNavigationView = binding.nav;
        bottomNavigationView.getMenu().findItem(R.id.home).setChecked(true);

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

    // Récupération des données
    @RequiresApi(api = Build.VERSION_CODES.O)
    public void recoverDataFromJson(){

        File path = getApplicationContext().getFilesDir();
        FileInputStream fis = null;

        // Profile
        try {
            fis = new FileInputStream(new File(path, "profile.json"));
            List<Integer> listIntegerFromProfile= JsonManager.readProfileStream(fis);
            level = listIntegerFromProfile.get(0);
            exp = listIntegerFromProfile.get(1);
            gold = listIntegerFromProfile.get(2);
        } catch (IOException e) {
            gold=0;
            level=1;
            exp=0;
        }

        List<Task> top5TaskList = new ArrayList<>();
        // Liste des tâches
        try {
            fis = new FileInputStream(new File(path, "tasklist.json"));
            TaskList = JsonManager.readJsonStream(fis);
            // On garde les 5 premières tâches pour  !
            top5TaskList = TaskList.stream().limit(5).collect(Collectors.toList());
        } catch (IOException e) {
            System.out.println(e);
            TaskList = new ArrayList<>();
            LocalDate localDate = LocalDate.ofYearDay(2023, 1);
            LocalTime localTime = LocalTime.of(0, 0);
            TaskList.add(new Task("Bien débuter", "N'hésiter pas à remplir votre tableau", localDate, localTime, 10, 10, false));
        }
        recyclerView.setAdapter(new TaskAdapter(this, top5TaskList));
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void updateDataToJson(){
        File path = getApplicationContext().getFilesDir();

        // Save Task List
        List<Task> SaveList = new ArrayList<Task>();
        List<Task> DoneList = new ArrayList<Task>();
        if (TaskList != null) {
            if (!TaskList.isEmpty()) {
                for (Task task : TaskList) {
                    if (!task.isTaskDone) {
                        //TaskList.remove(task); concurrent modif except
                        SaveList.add(task);
                    }else{
                        DoneList.add(task);
                    }
                }
            }
        }
        try {
            FileOutputStream fos = new FileOutputStream(new File(path, "tasklist.json"));
            JsonManager.writeJsonStream(fos, SaveList);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        // Maj exp, level et gold selon les tâches validées
        addExpGold(DoneList);

        // Save profile
        try {
            FileOutputStream fos = new FileOutputStream(new File(path, "profile.json"));
            List<Integer> integerList = new ArrayList<>();
            integerList.add(level);
            integerList.add(exp);
            integerList.add(gold);
            JsonManager.writeProfileStream(fos,integerList);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onPause() {
        updateDataToJson();
        super.onPause();
    }

    void refreshProfileVar(){
        levelTextView.setText("Lv. "+level);
        goldTextView.setText("Gold: "+gold);
        progressBar.setProgress(exp);
    }

    // Ajouter les exp et gold quand on valide des tâches
    void addExpGold(List<Task> listTaskDone){
        if (listTaskDone != null && !listTaskDone.isEmpty()) {
            for (Task task : listTaskDone) {
                int taskExp = task.getXpreward();
                int taskGold = task.getGoldreward();
                exp+= taskExp;
                // vérifier si on level up (si exp>100 -> level+1)
                if(exp>=100){
                    exp-=100;
                    level++;
                }
                gold+= taskGold;
            }
        }
    }


}