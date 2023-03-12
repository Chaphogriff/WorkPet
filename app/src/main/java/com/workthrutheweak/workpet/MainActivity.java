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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Récupérer les éléments du xml
        logo_main = (ImageButton) findViewById(R.id.logo_workpet);

        // Mettre en place les listeners

        // Appuyer le bouton nous envoie vers un autre activité
        logo_main.setOnClickListener(view ->
                startActivity(new Intent(MainActivity.this, TestActivity.class))
        );

    }
}