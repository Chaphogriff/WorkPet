package com.workthrutheweak.workpet;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.workthrutheweak.workpet.JsonManagement.JsonManager;
import com.workthrutheweak.workpet.databinding.ActivityTaskBinding;
import com.workthrutheweak.workpet.model.Task;
import com.workthrutheweak.workpet.notification.ReminderBroadcast;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


public class TaskActivity extends AppCompatActivity {

    // Variables
    private ActivityTaskBinding binding;//For ViewBinding feature
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    private DocumentReference docref = db.collection("Users").document(user.getUid());
    Button button_back;
    Button archivebutton;
    Button addtbutton;
    TextView textView;
    List<Task> TaskList;
    private FirestoreRecyclerAdapter adapter;
    private DialogInterface.OnClickListener dialogClickListener;
    RecyclerView recyclerView;
    int exp, gold, level;
    int usergold, userxp, userlvl;
    String avatarName;

    @RequiresApi(api = Build.VERSION_CODES.O)
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
        addtbutton = binding.addtbutton;
        archivebutton = binding.archivesbutton;
        textView = binding.tasksText;
        recyclerView = binding.tasksRecyclerView;
        ReminderBroadcast.createNotificationChannel(this);

        docref.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                usergold = Math.toIntExact(documentSnapshot.getLong("gold"));
                userxp = Math.toIntExact(documentSnapshot.getLong("xp"));
                userlvl = Math.toIntExact(documentSnapshot.getLong("lvl"));
                Log.i("loaded", "done");
            }
        });

        Query query = docref.collection("Tasks").whereEqualTo("taskDone", false);

        FirestoreRecyclerOptions<Task> options = new FirestoreRecyclerOptions.Builder<Task>()
                .setQuery(query, Task.class)
                .build();

        adapter = new FirestoreRecyclerAdapter<Task, TaskActivity.TaskViewHolder>(options) {
            @NonNull
            @Override
            public TaskActivity.TaskViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.taskholder_list, parent, false);
                return new TaskActivity.TaskViewHolder(view);
            }

            @Override
            protected void onBindViewHolder(@NonNull TaskActivity.TaskViewHolder holder, int position, @NonNull Task model) {
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
                holder.validateButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        model.setTaskDone(true);
                        docref.collection("Tasks").document(model.getTaskId()).update("taskDone", true);
                        docref.update("gold", model.getGoldreward() + usergold);
                        userxp += model.getXpreward();
                        if (userxp >= 100) {
                            userxp -= 100;
                            userlvl += 1;
                        }
                        docref.update("xp", userxp);
                        docref.update("lvl", userlvl);

                        int taskExp = model.getXpreward();
                        int taskGold = model.getGoldreward();
                        exp += taskExp;
                        // vérifier si on level up (si exp>100 -> level+1)
                        if (exp >= 100) {
                            exp -= 100;
                            level++;
                        }
                        gold += taskGold;
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
                                        Toast.makeText(TaskActivity.this, "Yes clicked", Toast.LENGTH_SHORT).show();
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
                        AlertDialog.Builder builder = new AlertDialog.Builder(TaskActivity.this);
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
                holder.modifyButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        modifyTask(model);
                    }
                });
            }
        };

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

        /*File path = getApplicationContext().getFilesDir();
        FileInputStream fis = null;
        try {
            fis = new FileInputStream(new File(path, "tasklist.json"));
            TaskList = JsonManager.readJsonStream(fis);
        } catch (IOException e) {
            System.out.println(e);
            TaskList = new ArrayList<>();
            LocalDate localDate = LocalDate.ofYearDay(2023, 1);
            LocalTime localTime = LocalTime.of(0, 0);
            TaskList.add(new Task("Bien débuter", "N'hésiter pas à remplir votre tableau", 2023, 1, 1, 0, 0, 10, 10, false, "Once"));
        }

        String titleIntent = getIntent().getStringExtra("Title");
        String descIntent = getIntent().getStringExtra("Desc");
        int yearIntent = getIntent().getIntExtra("Year", 0);
        int monthIntent = getIntent().getIntExtra("Month", 0);
        int dayIntent = getIntent().getIntExtra("Day", 0);
        int hourIntent = getIntent().getIntExtra("Hour", 0);
        int minuteIntent = getIntent().getIntExtra("Minute", 0);
        int goldIntent = getIntent().getIntExtra("Gold", 0);
        int xpIntent = getIntent().getIntExtra("XP", 0);
        boolean itdIntent = getIntent().getBooleanExtra("isTaskDone", false);
        String modeIntent = getIntent().getStringExtra("Mode");
        if (yearIntent != 0) {
            LocalTime localTimeI = LocalTime.of(hourIntent, minuteIntent);
            LocalDate localDateI = LocalDate.of(yearIntent, monthIntent, dayIntent);
            Task task = new Task(titleIntent, descIntent, yearIntent, monthIntent, dayIntent, hourIntent, minuteIntent, goldIntent, xpIntent, itdIntent, modeIntent);
            TaskList.add(task);
        } */

        /*recyclerView = binding.tasksRecyclerView;
        recyclerView.setAdapter(new TaskAdapter(this, TaskList));*/
        // Mettre en place les listeners


        // Appuyer le bouton nous envoie vers un autre activité
        button_back.setOnClickListener(view ->
                startActivity(new Intent(this, MainActivity.class))
        );

        addtbutton.setOnClickListener(view ->
                startActivity(new Intent(this, AddTaskActivity.class))
        );

        archivebutton.setOnClickListener(view ->
                startActivity(new Intent(this, ArchivedTaskActivity.class)));

        /*try {
            FileOutputStream fos = new FileOutputStream(new File(path, "tasklist.json"));
            JsonManager.writeJsonStream(fos, TaskList);
            Log.i("app", "saving");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }*/

        recoverProfileFromJson();
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
    public void recoverProfileFromJson() {
        File path = getApplicationContext().getFilesDir();
        FileInputStream fis = null;

        // Profile
        try {
            fis = new FileInputStream(new File(path, "profile.json"));
            List<String> listStringFromProfile = JsonManager.readProfileStream(fis);
            level = Integer.parseInt(listStringFromProfile.get(0));
            exp = Integer.parseInt(listStringFromProfile.get(1));
            gold = Integer.parseInt(listStringFromProfile.get(2));
            avatarName = listStringFromProfile.get(3);
        } catch (IOException e) {
            gold = 0;
            level = 1;
            exp = 0;
            avatarName = "cat";
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void updateProfileToJson() {
        File path = getApplicationContext().getFilesDir();

        // Save profile
        try {
            FileOutputStream fos = new FileOutputStream(new File(path, "profile.json"));
            List<String> stringList = new ArrayList<>();
            stringList.add(Integer.toString(level));
            stringList.add(Integer.toString(exp));
            stringList.add(Integer.toString(gold));
            stringList.add(avatarName);
            JsonManager.writeProfileStream(fos, stringList);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onPause() {
        updateProfileToJson();
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
        private ImageButton validateButton;
        private ImageButton modifyButton;
        private ImageButton deleteButton;

        public TaskViewHolder(@NonNull View itemView) {
            super(itemView);
            taskTitleTextView = itemView.findViewById(R.id.task_title);
            taskDescTextView = itemView.findViewById(R.id.task_desc);
            taskDateTextView = itemView.findViewById(R.id.task_date);
            taskRewardTextView = itemView.findViewById(R.id.task_reward);
            validateButton = itemView.findViewById(R.id.validate_button);
            modifyButton = itemView.findViewById(R.id.modify_button);
            deleteButton = itemView.findViewById(R.id.delete_button);
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        adapter.stopListening();
    }

    public void modifyTask(Task task) {
        //Intent Creation
        Intent intent = new Intent(TaskActivity.this, ModifyTaskActivity.class);
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
