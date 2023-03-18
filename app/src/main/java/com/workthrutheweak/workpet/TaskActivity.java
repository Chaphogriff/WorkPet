package com.workthrutheweak.workpet;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.workthrutheweak.workpet.adapter.TaskAdapter;
import com.workthrutheweak.workpet.data.Datasource;
import com.workthrutheweak.workpet.databinding.ActivityMainBinding;
import com.workthrutheweak.workpet.databinding.ActivityTaskBinding;
import com.workthrutheweak.workpet.model.Task;

import java.util.List;

public class TaskActivity extends AppCompatActivity {

    // Variables
    private ActivityTaskBinding binding;//For ViewBinding feature
    Button button_back;
    TextView textView;

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
        textView = binding.tasksText;

        List<Task> myDataset = new Datasource().loadTasks();
        RecyclerView recyclerView = binding.tasksRecyclerView;
        recyclerView.setAdapter(new TaskAdapter(this, myDataset));

        // Mettre en place les listeners

        // Appuyer le bouton nous envoie vers un autre activité
        button_back.setOnClickListener(view ->
                startActivity(new Intent(this, MainActivity.class))
        );

    }
}