package com.workthrutheweak.workpet;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
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
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.workthrutheweak.workpet.JsonManagement.JsonManager;
import com.workthrutheweak.workpet.adapter.TaskAdapter;
import com.workthrutheweak.workpet.databinding.ActivityMainBinding;
import com.workthrutheweak.workpet.model.Task;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class MainActivity extends AppCompatActivity {

    // Variables
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
    private FirestoreRecyclerAdapter adapter;
    int gold = 99;
    int level = 2;
    int exp = 75; // entre 0 et 100 ! ( si > 100, on level up )


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
        recyclerView = binding.recyclerView;
        goldTextView = binding.goldTextHome;
        levelTextView = binding.lvlText;
        progressBar = binding.expBar;

        // Récupérer les données depuis des fichiers .json
        //recoverDataFromJson();

        // Récupérer les données depuis Firestore
        //recyclerView.setAdapter(new TaskAdapter(MainActivity.this, TaskList2));

        Query query = docref.collection("Tasks");

        FirestoreRecyclerOptions<Task> options = new FirestoreRecyclerOptions.Builder<Task>()
                .setQuery(query, Task.class)
                .build();

        adapter = new FirestoreRecyclerAdapter<Task, TaskViewHolder>(options) {
            @NonNull
            @Override
            public TaskViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.task_list,parent,false);
                return new TaskViewHolder(view);
            }

            @Override
            protected void onBindViewHolder(@NonNull TaskViewHolder holder, int position, @NonNull Task model) {
                //Task model = model.get(position); //changer pour FF
                List<String> TaskString = model.inString();
                holder.taskTitleTextView.setText(TaskString.get(0));
                holder.taskDescTextView.setText(TaskString.get(1));
                holder.taskDateTextView.setText(TaskString.get(2));
                holder.taskRewardTextView.setText(TaskString.get(3));
                holder.taskCompletionCheckBox.setChecked(model.isTaskDone());
                holder.taskCompletionCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        model.setTaskDone(isChecked); //changer pour FF
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
                    TaskList2.add(task);
                    Log.i("main", "filling tasklist");
                    Log.i("main", task.inString().get(2));
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getApplicationContext(), "Failed to load tasks", Toast.LENGTH_LONG);
            }
        });

        Log.i("main", "recyclerView");


        // Set valeurs
        refreshProfileVar();

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


    private void recoverDataFromFF() {
        docref.collection("Tasks").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                    Task task = documentSnapshot.toObject(Task.class);
                    TaskList2.add(task);
                    Log.i("main", "filling tasklist");
                    Log.i("main", task.inString().get(2));
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getApplicationContext(), "Failed to load tasks", Toast.LENGTH_LONG);
            }
        });

        Log.i("main", "task adapt");
    }

    // Récupération des données
    @RequiresApi(api = Build.VERSION_CODES.O)
    public void recoverDataFromJson() {

        File path = getApplicationContext().getFilesDir();
        FileInputStream fis = null;

        // Profile
        try {
            fis = new FileInputStream(new File(path, "profile.json"));
            List<Integer> listIntegerFromProfile = JsonManager.readProfileStream(fis);
            level = listIntegerFromProfile.get(0);
            exp = listIntegerFromProfile.get(1);
            gold = listIntegerFromProfile.get(2);
        } catch (IOException e) {
            gold = 0;
            level = 1;
            exp = 0;
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
            List<Integer> integerList = new ArrayList<>();
            integerList.add(level);
            integerList.add(exp);
            integerList.add(gold);
            JsonManager.writeProfileStream(fos, integerList);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onPause() {
        updateDataToJson();
        super.onPause();
    }

    void refreshProfileVar() {
        levelTextView.setText("Lv. " + level);
        goldTextView.setText("Gold: " + gold);
        progressBar.setProgress(exp);
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
        private CheckBox taskCompletionCheckBox;

        private android.content.Context Context;

        public TaskViewHolder(@NonNull View itemView) {
            super(itemView);
            taskTitleTextView = itemView.findViewById(R.id.task_title);
            taskDescTextView = itemView.findViewById(R.id.task_desc);
            taskDateTextView = itemView.findViewById(R.id.task_date);
            taskRewardTextView = itemView.findViewById(R.id.task_reward);
            taskCompletionCheckBox = itemView.findViewById(R.id.task_completion);
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        adapter.stopListening();
    }
}