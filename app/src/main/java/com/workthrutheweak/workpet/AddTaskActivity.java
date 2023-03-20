package com.workthrutheweak.workpet;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.workthrutheweak.workpet.adapter.TaskAdapter;
import com.workthrutheweak.workpet.data.Datasource;
import com.workthrutheweak.workpet.databinding.ActivityAddtaskBinding;
import com.workthrutheweak.workpet.databinding.ActivityTaskBinding;
import com.workthrutheweak.workpet.model.Task;

import java.util.List;

public class AddTaskActivity extends AppCompatActivity {
    // Variables
    private ActivityAddtaskBinding binding;//For ViewBinding feature
    Button button_back;
    FloatingActionButton button_add;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addtask);

        //Setup View Binding variable
        binding = ActivityAddtaskBinding.inflate(getLayoutInflater());
        View v = binding.getRoot();
        setContentView(v);

        getSupportActionBar().hide();
        // Récupérer les éléments du xml
        button_back = binding.back2;

        button_add = binding.AddButton;

        // Mettre en place les listeners

        // Appuyer le bouton nous envoie vers un autre activité
        button_back.setOnClickListener(view ->
                startActivity(new Intent(this, MainActivity.class))
        );

        button_add.setOnClickListener(//add saving tasks
                view -> startActivity(new Intent(this, TaskActivity.class))
        );


    }
}
