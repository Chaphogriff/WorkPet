package com.workthrutheweak.workpet;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import com.workthrutheweak.workpet.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {
    private ActivityMainBinding binding;

    // Variables
    ImageButton logo_main;
    Button button_avatar;
    Button button_calendar;
    Button button_setting;
    Button button_task;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        View v = binding.getRoot();
        setContentView(v);

        // Récupérer les éléments du xml
        logo_main = binding.logoWorkpet;
        button_avatar = binding.avatar;
        button_calendar = binding.calendar;
        button_task = binding.task;
        button_setting = binding.setting;

        // Mettre en place les listeners

        // Appuyer le bouton nous envoie vers un autre activité
        logo_main.setOnClickListener(view ->
                startActivity(new Intent(MainActivity.this, TestActivity.class))
        );
        button_avatar.setOnClickListener(view ->
                startActivity(new Intent(MainActivity.this,AvatarActivity.class))
        );
        button_calendar.setOnClickListener(view ->
                startActivity(new Intent(MainActivity.this, CalendarActivity.class))
        );
        button_task.setOnClickListener(view ->
                startActivity(new Intent(MainActivity.this, TaskActivity.class))
        );
        button_setting.setOnClickListener(view ->
                startActivity(new Intent(MainActivity.this, SettingActivity.class))
        );

    }
}