package com.workthrutheweak.workpet;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import com.workthrutheweak.workpet.databinding.ActivityTestBinding;
import com.workthrutheweak.workpet.notification.ReminderBroadcast;

public class TestActivity extends AppCompatActivity {

    private ActivityTestBinding binding;//For ViewBinding feature

    // Variables
    Button button_back;
    ImageButton logo_wttw;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);

        //Setup View Binding variable
        binding = ActivityTestBinding.inflate(getLayoutInflater());
        View v = binding.getRoot();
        setContentView(v);

        // Récupérer les éléments du xml
        button_back = binding.back;
        logo_wttw = binding.logoWttw;
        ReminderBroadcast.createNotificationChannel(this);

        // Mettre en place les listeners

        // Appuyer le bouton nous envoie vers un autre activité
        button_back.setOnClickListener(view ->
                startActivity(new Intent(this, MainActivity.class))
        );
        // Pivote l'image (c'est juste pour tester les intéractions
        logo_wttw.setOnClickListener(view ->
                logo_wttw.setRotation(logo_wttw.getRotation() + 90)
        );


    }
}