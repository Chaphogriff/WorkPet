package com.workthrutheweak.workpet;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageButton;

public class TestActivity extends AppCompatActivity {

    // Variables
    Button button_back;
    ImageButton logo_wttw;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);


        // Récupérer les éléments du xml
        button_back = (Button) findViewById(R.id.back);
        logo_wttw = (ImageButton) findViewById(R.id.logo_wttw);

        // Mettre en place les listeners

        // Appuyer le bouton nous envoie vers un autre activité
        button_back.setOnClickListener(view ->
                startActivity(new Intent(this, MainActivity.class))
        );
        // Pivote l'image (c'est juste pour tester les intéractions
        logo_wttw.setOnClickListener(view->
            logo_wttw.setRotation(logo_wttw.getRotation() +90 )
        );


    }
}