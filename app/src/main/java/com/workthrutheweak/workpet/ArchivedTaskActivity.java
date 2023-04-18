package com.workthrutheweak.workpet;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.workthrutheweak.workpet.JsonManagement.JsonManager;
import com.workthrutheweak.workpet.databinding.ActivityArchivedtaskBinding;
import com.workthrutheweak.workpet.model.Task;
import com.workthrutheweak.workpet.notification.ReminderBroadcast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ArchivedTaskActivity extends AppCompatActivity {
    // Variables
    private ActivityArchivedtaskBinding binding;//For ViewBinding feature
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    private DocumentReference docref = db.collection("Users").document(user.getUid());
    Button button_back;
    TextView textView;
    List<Task> TaskList;
    private FirestoreRecyclerAdapter adapter;
    private DialogInterface.OnClickListener dialogClickListener;
    RecyclerView recyclerView;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task);

        //Setup View Binding variable
        binding = ActivityArchivedtaskBinding.inflate(getLayoutInflater());
        View v = binding.getRoot();
        setContentView(v);

        getSupportActionBar().hide();
        // Récupérer les éléments du xml
        button_back = binding.back;
        textView = binding.tasksText;
        recyclerView = binding.tasksRecyclerView;
        ReminderBroadcast.createNotificationChannel(this);

        Query query = docref.collection("Tasks").whereEqualTo("taskDone",true);

        FirestoreRecyclerOptions<Task> options = new FirestoreRecyclerOptions.Builder<Task>()
                .setQuery(query, Task.class)
                .build();

        adapter = new FirestoreRecyclerAdapter<Task, ArchivedTaskActivity.TaskViewHolder>(options) {
            @NonNull
            @Override
            public ArchivedTaskActivity.TaskViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.taskarchived_list,parent,false);
                return new ArchivedTaskActivity.TaskViewHolder(view);
            }

            @Override
            protected void onBindViewHolder(@NonNull ArchivedTaskActivity.TaskViewHolder holder, int position, @NonNull Task model) {
                //Task model = model.get(position); //changer pour FF
                DocumentSnapshot snapshot = options.getSnapshots().getSnapshot(position);
                String dbKey = snapshot.getId();
                model.setTaskId(dbKey);
                List<String> TaskString = model.inString();
                Log.i("task", String.valueOf(model.getDay()));
                holder.taskTitleTextView.setText(TaskString.get(0));
                holder.taskDescTextView.setText(TaskString.get(1));
                holder.taskDateTextView.setText(TaskString.get(2));
                holder.taskRewardTextView.setText(TaskString.get(3));
                holder.unarchiveButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        model.setTaskDone(true);
                        docref.collection("Tasks").document(model.getTaskId()).update("taskDone", false);
                    }
                });
                holder.deleteButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialogClickListener = new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                switch (which) {
                                    // on below line we are setting a click listener
                                    // for our positive button
                                    case DialogInterface.BUTTON_POSITIVE:
                                        // on below line we are displaying a toast message.
                                        Toast.makeText(ArchivedTaskActivity.this, "Yes clicked", Toast.LENGTH_SHORT).show();
                                        docref.collection("Tasks").document(model.getTaskId()).delete();
                                        break;
                                    // on below line we are setting click listener
                                    // for our negative button.
                                    case DialogInterface.BUTTON_NEGATIVE:
                                        // on below line we are dismissing our dialog box.
                                        dialog.dismiss();

                                }
                            }
                        };
                        // on below line we are creating a builder variable for our alert dialog
                        AlertDialog.Builder builder = new AlertDialog.Builder(ArchivedTaskActivity.this);
                        // on below line we are setting message for our dialog box.
                        builder.setMessage("Delete selected task ?")
                                // on below line we are setting positive button
                                // and setting text to it.
                                .setPositiveButton("Yes", dialogClickListener)
                                // on below line we are setting negative button
                                // and setting text to it.
                                .setNegativeButton("No", dialogClickListener)
                                // on below line we are calling
                                // show to display our dialog.
                                .show();
                    }
                });
            }
        };

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);


        // Appuyer le bouton nous envoie vers un autre activité
        button_back.setOnClickListener(view ->
                startActivity(new Intent(this, TaskActivity.class))
        );

        @SuppressLint({"MissingInflatedId", "LocalSuppress"}) BottomNavigationView bottomNavigationView = binding.nav;
        bottomNavigationView.setSelectedItemId(R.id.task);
        //ajout du navbar
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
                        startActivity(new Intent(getApplicationContext(), MainActivity.class));
                        overridePendingTransition(0, 0);
                        return true;
                    case R.id.setting:
                        startActivity(new Intent(getApplicationContext(), SettingActivity.class));
                        overridePendingTransition(0, 0);
                        return true;
                    case R.id.task:
                        return true;
                }
                return false;
            }
        });
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onPause() {
        List<Task> SaveList = new ArrayList<Task>();
        if (TaskList != null) {
            if (!TaskList.isEmpty()) {
                for (Task task : TaskList) {
                    if (!task.isTaskDone()) {
                        //TaskList.remove(task); concurrent modif except
                        SaveList.add(task);
                    }
                }
            }
        }
        File path = getApplicationContext().getFilesDir();
        try {
            FileOutputStream fos = new FileOutputStream(new File(path, "tasklist.json"));
            JsonManager.writeJsonStream(fos, SaveList);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        super.onPause();
    }

    @Override
    protected void onStart() {
        super.onStart();
        adapter.startListening();
    }

    private class TaskViewHolder extends RecyclerView.ViewHolder {
        private TextView taskTitleTextView;
        private TextView taskDescTextView;
        private TextView taskDateTextView;
        private TextView taskRewardTextView;
        private ImageButton unarchiveButton;
        private ImageButton deleteButton;
        public TaskViewHolder(@NonNull View itemView) {
            super(itemView);
            taskTitleTextView = itemView.findViewById(R.id.task_title);
            taskDescTextView = itemView.findViewById(R.id.task_desc);
            taskDateTextView = itemView.findViewById(R.id.task_date);
            taskRewardTextView = itemView.findViewById(R.id.task_reward);
            unarchiveButton = itemView.findViewById(R.id.unarchive_button);
            deleteButton = itemView.findViewById(R.id.delete_button);
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        adapter.stopListening();
    }

    public void modifyTask(Task task){
        //Intent Creation
        Intent intent = new Intent(ArchivedTaskActivity.this, ModifyTaskActivity.class);
        intent.putExtra("Title", task.getTitle());
        intent.putExtra("Desc", task.getDescription());
        intent.putExtra("Year", task.getYear());
        intent.putExtra("Month", task.getMonth());
        intent.putExtra("Day", task.getDay());
        intent.putExtra("Hour", task.getHour());
        intent.putExtra("Minute", task.getMinute());
        intent.putExtra("Gold", task.getGoldreward());
        intent.putExtra("XP", task.getXpreward());
        intent.putExtra("isTaskDone", task.isTaskDone());
        intent.putExtra("Mode", task.getMode());
        intent.putExtra("TaskId", task.getTaskId());
        intent.putExtra("origin", "taskactivity");
        startActivity(intent);
    }
}

