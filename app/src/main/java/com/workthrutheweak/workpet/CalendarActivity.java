package com.workthrutheweak.workpet;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.TextView;

import com.workthrutheweak.workpet.databinding.ActivityCalendarBinding;
import com.workthrutheweak.workpet.databinding.ActivityMainBinding;
import com.workthrutheweak.workpet.databinding.ActivityTaskBinding;

public class CalendarActivity extends AppCompatActivity {

    // Variables
    private ActivityCalendarBinding binding;
    Button button_back;
    CalendarView calendar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar);
        binding = ActivityCalendarBinding.inflate(getLayoutInflater());
        View v = binding.getRoot();
        setContentView(v);

        // Récupérer les éléments du xml
        button_back = binding.back;
        calendar = binding.calendar;

        // Mettre en place les listeners

        // Appuyer le bouton nous envoie vers un autre activité
        button_back.setOnClickListener(view ->
                startActivity(new Intent(this, MainActivity.class))
        );
    }
}