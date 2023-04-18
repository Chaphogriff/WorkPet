package com.workthrutheweak.workpet;


import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
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
import android.widget.CalendarView;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
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
import com.workthrutheweak.workpet.databinding.ActivityCalendarBinding;
import com.workthrutheweak.workpet.model.Task;
import com.workthrutheweak.workpet.notification.ReminderBroadcast;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;


public class CalendarActivity extends AppCompatActivity {

    // Variables
    private ActivityCalendarBinding binding; //For ViewBinding feature
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    private DocumentReference docref = db.collection("Users").document(user.getUid());
    private AlertDialog.Builder dialogbuilder;
    private AlertDialog dialog;
    private TextView popup_title;
    private RecyclerView recyclerView;
    private FloatingActionButton button_popup_back;
    private List<Task> taskList;
    private List<Task> taskList_popup;
    Button button_back;
    Button button_add;
    FloatingActionButton button_addT;
    CalendarView calendarView;
    String avatarName;
    long date;
    private FirestoreRecyclerAdapter adapter;
    private DialogInterface.OnClickListener dialogClickListener;
    int exp, gold,level;
    int usergold, userxp, userlvl;
    int Year,Month,DayOfMonth;


    @SuppressLint("NewApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar);

        //Setup View Binding variable
        binding = ActivityCalendarBinding.inflate(getLayoutInflater());
        View v = binding.getRoot();
        setContentView(v);
        ReminderBroadcast.createNotificationChannel(this);

        // Récupérer les éléments du xml
        button_back = binding.back;
        calendarView = binding.calendar;
        button_add = binding.addTask;

        docref.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                usergold = Math.toIntExact(documentSnapshot.getLong("gold"));
                userxp = Math.toIntExact(documentSnapshot.getLong("xp"));
                userlvl = Math.toIntExact(documentSnapshot.getLong("lvl"));
                Log.i("loaded", "done");
            }
        });

        // Mettre en place les listeners
        calendarView.setDate(date);

        File path = getApplicationContext().getFilesDir();
        FileInputStream fis = null;
        try {
            fis = new FileInputStream(new File(path, "tasklist.json"));
            taskList = JsonManager.readJsonStream(fis);
        } catch (IOException e) {
            System.out.println(e);
            taskList = new ArrayList<>();
            /*LocalDate localDate = LocalDate.ofYearDay(2023, 1);
            LocalTime localTime = LocalTime.of(0, 0);
            taskList.add(new Task("Bien débuter", "N'hésiter pas à remplir votre tableau", localDate, localTime, 10, 10, false));*/
        }

        taskList_popup = new ArrayList<Task>();

        // Appuyer le bouton nous envoie vers un autre activité
        button_back.setOnClickListener(view ->
                startActivity(new Intent(this, MainActivity.class))
        );

        button_add.setOnClickListener(view ->
                startActivity(new Intent(this, AddTaskActivity.class))
        );
        // Récupération de la date actuelle
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);

        // Affichage de la date actuelle sur le CalendarView
        calendarView.setDate(calendar.getTimeInMillis());

        // Ajout d'un listener pour détecter le changement de date sur le CalendarView
        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth) {

                // Récupération de la date sélectionnée sur le CalendarView
                Calendar selectedDate = Calendar.getInstance();
                selectedDate.set(year, month, dayOfMonth);
                taskList_popup.clear();
                Year = year;
                Month = month;
                DayOfMonth = dayOfMonth;
                docref.collection("Tasks").whereEqualTo("year", year).whereEqualTo("month", month+1).whereEqualTo("day", dayOfMonth).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                            Task task = documentSnapshot.toObject(Task.class);
                            task.setTaskId(documentSnapshot.getId());
                            taskList_popup.add(task);
                            Log.i("cal","tasklist is filled");
                            Log.i("cal", "is tasklist empty ?");
                            if (!taskList_popup.isEmpty()) {
                                Log.i("cal", "tasklist not empty");
                                createPopupDialog2();
                            }
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getApplicationContext(), "Failed to load tasks", Toast.LENGTH_LONG);
                    }
                });

                // Affichage de la date sélectionnée dans la console
                SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
                Log.i("Selected date", dayOfMonth +"/"+month+"/"+year );



                /*if (taskList != null) {
                    if (!taskList.isEmpty()) {
                        for (Task task : taskList) {
                            LocalDate localDate_task = LocalDate.of(task.getYear(),task.getMonth(),task.getDay());
                            if (localDate_calendar.getDayOfMonth() == localDate_task.getDayOfMonth()
                                    && localDate_calendar.getMonthValue() == localDate_task.getMonthValue()
                                    && localDate_calendar.getYear() == localDate_task.getYear()) {
                                taskList_popup.add(task);
                            }
                        }
                    }
                }*/

            }
        });

        recoverProfileFromJson();

        BottomNavigationView bottomNavigationView = binding.nav;
        bottomNavigationView.setSelectedItemId(R.id.calendar);
        //ajout du navbar
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                switch (item.getItemId()) {
                    case R.id.calendar:
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
                        startActivity(new Intent(getApplicationContext(), TaskActivity.class));
                        overridePendingTransition(0, 0);
                        return true;
                }
                return false;
            }


        });

    }

    public void createPopupDialog() {
        dialogbuilder = new AlertDialog.Builder(this);
        final View calendarPopupView = getLayoutInflater().inflate(R.layout.activity_calendar_popup, null);
        popup_title = (TextView) calendarPopupView.findViewById(R.id.daytask);
        recyclerView = (RecyclerView) calendarPopupView.findViewById(R.id.tasksRecyclerView);
        recyclerView.setAdapter(new TaskAdapter(this, taskList_popup));
        button_popup_back = (FloatingActionButton) calendarPopupView.findViewById(R.id.back);
        dialogbuilder.setView(calendarPopupView);
        dialog = dialogbuilder.create();
        dialog.show();
        Log.i("cal","in there");
        button_popup_back.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(View view) {
                if (taskList != null) {
                    if (!taskList.isEmpty()) {
                        for (Task task_popup : taskList_popup) {
                            for (Task task : taskList) {
                                if (task_popup.isTaskDone() && task_popup.getTitle() == task.getTitle()) {
                                    taskList.remove(task);
                                }
                            }
                        }
                    }
                }
                File path = getApplicationContext().getFilesDir();
                try {
                    FileOutputStream fos = new FileOutputStream(new File(path, "tasklist.json"));
                    JsonManager.writeJsonStream(fos, taskList);
                } catch (IOException e) {
                    System.out.println(e);
                }
                dialog.dismiss();
            }
        });


    }

    public void createPopupDialog2() {
        Query query = docref.collection("Tasks").whereEqualTo("year", Year).whereEqualTo("month", Month+1).whereEqualTo("day", DayOfMonth);

        dialogbuilder = new AlertDialog.Builder(this);
        final View calendarPopupView = getLayoutInflater().inflate(R.layout.activity_calendar_popup, null);

        FirestoreRecyclerOptions<Task> options = new FirestoreRecyclerOptions.Builder<Task>()
                .setQuery(query, Task.class)
                .build();

        recyclerView = (RecyclerView) calendarPopupView.findViewById(R.id.tasksRecyclerView);
        adapter = new FirestoreRecyclerAdapter<Task, CalendarActivity.TaskViewHolder>(options) {
            @NonNull
            @Override
            public CalendarActivity.TaskViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.taskholder_list, parent, false);
                return new CalendarActivity.TaskViewHolder(view);
            }

            @Override
            protected void onBindViewHolder(@NonNull CalendarActivity.TaskViewHolder holder, int position, @NonNull Task model) {
                //Task model = model.get(position); //changer pour FF
                DocumentSnapshot snapshot = options.getSnapshots().getSnapshot(position);
                String dbKey = snapshot.getId();
                model.setTaskId(dbKey);
                List<String> TaskString = model.inString();
                Log.i("task", String.valueOf(model.getDay()));
                //if(model.getDay() == )
                holder.taskTitleTextView.setText(TaskString.get(0));
                holder.taskDescTextView.setText(TaskString.get(1));
                holder.taskDateTextView.setText(TaskString.get(2));
                holder.taskRewardTextView.setText(TaskString.get(3));
                holder.validateButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        model.setTaskDone(true);
                        docref.collection("Tasks").document(model.getTaskId()).update("taskDone", true);
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
                                        Toast.makeText(CalendarActivity.this, "Yes clicked", Toast.LENGTH_SHORT).show();
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
                        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(CalendarActivity.this);
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
        adapter.startListening();

        button_popup_back = (FloatingActionButton) calendarPopupView.findViewById(R.id.back);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
        dialogbuilder.setView(calendarPopupView);
        dialog = dialogbuilder.create();
        dialog.show();
        button_popup_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
                adapter.stopListening();
                startActivity(new Intent(CalendarActivity.this, CalendarActivity.class));
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
        //adapter.startListening();
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
    }

    public void modifyTask(Task task){
        //Intent Creation
        Intent intent = new Intent(CalendarActivity.this, ModifyTaskActivity.class);
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
        intent.putExtra("origin", "calendaractivity");
        startActivity(intent);
    }
}
