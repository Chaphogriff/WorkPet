package com.workthrutheweak.workpet;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.workthrutheweak.workpet.JsonManagement.JsonManager;
import com.workthrutheweak.workpet.adapter.TaskAdapter;
import com.workthrutheweak.workpet.databinding.ActivityMainBinding;
import com.workthrutheweak.workpet.model.Task;
import com.workthrutheweak.workpet.notification.ReminderBroadcast;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import pl.droidsonroids.gif.GifImageView;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding; //For ViewBinding feature
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    private DocumentReference docref = db.collection("Users").document(user.getUid());
    List<Task> TaskList;
    List<Task> TaskList2 = new ArrayList<>();
    TextView goldTextView;
    TextView levelTextView;
    ProgressBar progressBar;
    RecyclerView recyclerView;
    GifImageView avatarView;
    String avatarName = "cat";
    Button logoutbutton;
    private FirestoreRecyclerAdapter adapter;
    private DialogInterface.OnClickListener dialogClickListener;
    int gold = 99;
    int level = 2;
    int exp = 75; // entre 0 et 100 ! ( si > 100, on level up )
    int usergold, userxp, userlvl;


    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Setup View Binding variable
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        View v = binding.getRoot();
        setContentView(v);

        // Récupérer les éléments du xml
        recyclerView = findViewById(R.id.recyclerView);
        goldTextView = binding.goldTextHome;
        levelTextView = binding.lvlText;
        progressBar = binding.expBar;
        logoutbutton = binding.logoutbutton;
        avatarView = binding.homePet;

        //Notifications
        ReminderBroadcast.createNotificationChannel(this);
/*
        NotificationHelper.createNotificationChannel(this,
                NotificationManagerCompat.IMPORTANCE_DEFAULT, false,
                getString(R.string.app_name), "App notification channel.");
        NotificationHelper.createNotificationChannel(this,
                NotificationManagerCompat.IMPORTANCE_HIGH, true,
                "Tasks", "Notification channel for tasks.");*/
        docref.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                usergold = Math.toIntExact(documentSnapshot.getLong("gold"));
                userxp = Math.toIntExact(documentSnapshot.getLong("xp"));
                userlvl = Math.toIntExact(documentSnapshot.getLong("lvl"));
            }
        });
        logoutbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(getApplicationContext(), EmailPasswordActivity.class));
            }
        });

        // Récupérer les données depuis des fichiers .json
        //recoverDataFromJson();

        // Récupérer les données depuis Firestore
        //recyclerView.setAdapter(new TaskAdapter(MainActivity.this, TaskList2));

        Query query = docref.collection("Tasks").whereEqualTo("taskDone",false)
                .orderBy("year").orderBy("month").orderBy("day").limit(5);

        FirestoreRecyclerOptions<Task> options = new FirestoreRecyclerOptions.Builder<Task>()
                .setQuery(query, Task.class)
                .build();

        adapter = new FirestoreRecyclerAdapter<Task, MainActivity.TaskViewHolder>(options) {
            @NonNull
            @Override
            public MainActivity.TaskViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.taskholder_list,parent,false);
                return new MainActivity.TaskViewHolder(view);
            }

            @Override
            protected void onBindViewHolder(@NonNull MainActivity.TaskViewHolder holder, int position, @NonNull Task model) {
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
                        docref.update("gold", model.getGoldreward()+usergold);
                        userxp += model.getXpreward();
                        if (userxp >= 100) {
                            userxp -= 100;
                            userlvl +=1;
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
                        try {
                            refreshProfileVar();
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                        model.setTaskDone(true);
                        docref.collection("Tasks").document(model.getTaskId()).update("taskDone", true);
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
                                        Toast.makeText(MainActivity.this, "Yes clicked", Toast.LENGTH_SHORT).show();
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
                        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
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


        docref.collection("Tasks").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                    Task task = documentSnapshot.toObject(Task.class);
                    task.setTaskId(documentSnapshot.getId());
                    TaskList2.add(task);
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getApplicationContext(), "Failed to load tasks", Toast.LENGTH_LONG);
            }
        });


        // Set valeurs
        recoverProfileFromJson();
        try {
            refreshProfileVar();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        BottomNavigationView bottomNavigationView = binding.nav;
        bottomNavigationView.getMenu().findItem(R.id.home).setChecked(true);

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
                        return true;
                    case R.id.setting:
                        startActivity(new Intent(getApplicationContext(), SettingActivity.class));
                        overridePendingTransition(0, 0);
                        return true;
                    case R.id.task:
                        startActivity(new Intent(getApplicationContext(), TaskActivity.class));
                        overridePendingTransition(0, 0);
                        return true;
                }
                return false;
            }
        });
    }

    public void setAvatar(String fileName) throws IOException {
        int id = this.getResources().getIdentifier("drawable/"+fileName, null, getPackageName());
        avatarView.setImageResource(id);
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

    // Récupération des données
    @RequiresApi(api = Build.VERSION_CODES.O)
    public void recoverDataFromJson() {

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

        List<Task> top5TaskList = new ArrayList<>();
        // Liste des tâches
        try {
            fis = new FileInputStream(new File(path, "tasklist.json"));
            TaskList = JsonManager.readJsonStream(fis);
            // On garde les 5 premières tâches pour  !
            top5TaskList = TaskList.stream().limit(5).collect(Collectors.toList());
        } catch (IOException e) {
            System.out.println(e);
            TaskList = new ArrayList<>();
            LocalDate localDate = LocalDate.ofYearDay(2023, 1);
            LocalTime localTime = LocalTime.of(0, 0);
            TaskList.add(new Task("Bien débuter", "N'hésiter pas à remplir votre tableau", 2023, 1, 1, 0, 0, 10, 10, false, "Once"));
        }
        recyclerView.setAdapter(new TaskAdapter(this, top5TaskList));
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
    public void updateDataToJson() {
        File path = getApplicationContext().getFilesDir();

        // Save Task List
        List<Task> SaveList = new ArrayList<Task>();
        List<Task> DoneList = new ArrayList<Task>();
        if (TaskList != null) {
            if (!TaskList.isEmpty()) {
                for (Task task : TaskList) {
                    if (!task.isTaskDone()) {
                        //TaskList.remove(task); concurrent modif except
                        SaveList.add(task);
                    } else {
                        DoneList.add(task);
                    }
                }
            }
        }
        try {
            FileOutputStream fos = new FileOutputStream(new File(path, "tasklist.json"));
            JsonManager.writeJsonStream(fos, SaveList);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        // Maj exp, level et gold selon les tâches validées
        addExpGold(DoneList);

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

    void refreshProfileVar() throws IOException {
        levelTextView.setText("Lv. " + level);
        goldTextView.setText("Gold: " + gold);
        progressBar.setProgress(exp);
        setAvatar(avatarName);
    }

    // Ajouter les exp et gold quand on valide des tâches
    void addExpGold(List<Task> listTaskDone) {
        if (listTaskDone != null && !listTaskDone.isEmpty()) {
            for (Task task : listTaskDone) {
                int taskExp = task.getXpreward();
                int taskGold = task.getGoldreward();
                exp += taskExp;
                // vérifier si on level up (si exp>100 -> level+1)
                if (exp >= 100) {
                    exp -= 100;
                    level++;
                }
                gold += taskGold;
            }
        }
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

    public void modifyTask(Task task){
        //Intent Creation
        Intent intent = new Intent(MainActivity.this, ModifyTaskActivity.class);
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
        intent.putExtra("origin", "mainactivity");
        startActivity(intent);
    }


}