package com.workthrutheweak.workpet;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.workthrutheweak.workpet.databinding.ActivityMainBinding;
import com.workthrutheweak.workpet.databinding.ActivitySettingBinding;

import java.io.File;

public class SettingActivity extends AppCompatActivity {

    // Variables
    private ActivitySettingBinding binding; //For ViewBinding feature

    TextView textView;
    Button button_help, button_aboutus;

    // Popup
    private AlertDialog.Builder dialogBuilder;
    private AlertDialog dialog;
    private Button popup_back;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        //Setup View Binding variable
        binding = ActivitySettingBinding.inflate(getLayoutInflater());
        View v = binding.getRoot();
        setContentView(v);

        // Récupérer les éléments du xml
        button_help = binding.help;
        button_aboutus = binding.about;

        // Mettre en place les listeners

        // Appuyer le bouton nous envoie vers un autre activité

        button_help.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                File path = getApplicationContext().getFilesDir();
                new File(path, "profile.json").delete();
            }
        });

        button_aboutus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createNewDialog();
            }
        });


        @SuppressLint({"MissingInflatedId", "LocalSuppress"}) BottomNavigationView bottomNavigationView = binding.nav;
        bottomNavigationView.setSelectedItemId(R.id.setting);
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

    public void createNewDialog(){
        dialogBuilder = new AlertDialog.Builder(this);
        final View popUpView = getLayoutInflater().inflate(R.layout.popup_settings_about,null);
        popup_back = (Button) popUpView.findViewById(R.id.back);

        dialogBuilder.setView(popUpView);
        dialog = dialogBuilder.create();
        dialog.show();
        popup_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
    }

}