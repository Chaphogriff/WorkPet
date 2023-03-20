package com.workthrutheweak.workpet;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.workthrutheweak.workpet.adapter.TaskAdapter;
import com.workthrutheweak.workpet.data.Datasource;
import com.workthrutheweak.workpet.databinding.ActivityAddtaskBinding;
import com.workthrutheweak.workpet.databinding.ActivityTaskBinding;
import com.workthrutheweak.workpet.model.Task;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.List;
import java.util.TimerTask;

public class AddTaskActivity extends AppCompatActivity {
    // Variables
    private ActivityAddtaskBinding binding;//For ViewBinding feature
    Button button_back;
    FloatingActionButton button_add;

    EditText dateTask;
    EditText timeTask;
    EditText titleTask;
    EditText descTask;
    EditText goldTask;
    EditText xpTask;

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

        dateTask = binding.editTextDate;
        timeTask = binding.editTextTime;
        titleTask = binding.TitleInputField;
        descTask = binding.DescInputField;
        goldTask = binding.GoldTextInput;
        xpTask = binding.XPTextInput;

        // Mettre en place les listeners

        // Appuyer le bouton nous envoie vers un autre activité
        button_back.setOnClickListener(view ->
                startActivity(new Intent(this, TaskActivity.class))
        );

        button_add.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //SaveData();
                    String Title = titleTask.toString();
                    String Desc = descTask.toString();
                    String Date = dateTask.toString();
                    String Time = timeTask.toString();
                    String Gold = goldTask.toString();
                    String XP = xpTask.toString();
                    Task newTask = new Task(Title,Desc,Date,Time,Gold,XP);

                    Intent intent = new Intent(AddTaskActivity.this, TaskActivity.class);
                    intent.putExtra("Task", newTask);
                    startActivity(new Intent(view.getContext(), TaskActivity.class));

                }
            }
        );
    }

}
