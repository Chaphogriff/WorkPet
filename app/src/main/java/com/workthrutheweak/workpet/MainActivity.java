package com.workthrutheweak.workpet;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

public class MainActivity extends AppCompatActivity {

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

        // Récupérer les éléments du xml
        logo_main = (ImageButton) findViewById(R.id.logo_workpet);
        button_avatar = (Button) findViewById(R.id.avatar);
        button_calendar = (Button) findViewById(R.id.calendar);
        button_task = (Button) findViewById(R.id.task);
        button_setting = (Button) findViewById(R.id.setting);

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