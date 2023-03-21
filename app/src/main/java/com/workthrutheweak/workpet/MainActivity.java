package com.workthrutheweak.workpet;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.JsonReader;
import android.util.JsonWriter;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import com.google.android.material.bottomnavigation.BottomNavigationView;
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
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private ActivityMainBinding binding; //For ViewBinding feature

    // Variables
    ImageButton logo_main;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Setup View Binding variable
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        View v = binding.getRoot();
        setContentView(v);

        // Récupérer les éléments du xml
        //logo_main = binding.logoWorkpet;

        List<Task> myDataset = new Datasource().loadTasks();
        RecyclerView recyclerView = binding.recyclerView;
        recyclerView.setAdapter(new TaskAdapter(this, myDataset));
        // Mettre en place les listeners

        // Appuyer le bouton nous envoie vers un autre activité
        /*logo_main.setOnClickListener(view ->
                startActivity(new Intent(MainActivity.this, TestActivity.class))
        );*/

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
}