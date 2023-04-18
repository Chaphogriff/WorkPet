package com.workthrutheweak.workpet;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.workthrutheweak.workpet.JsonManagement.JsonManager;
import com.workthrutheweak.workpet.databinding.ActivityMainBinding;
import com.workthrutheweak.workpet.databinding.ActivitySettingBinding;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;

public class SettingActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    // Variables
    private ActivitySettingBinding binding; //For ViewBinding feature
    Button button_help, button_aboutus, button_language, button_myaccount;

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
        button_aboutus = binding.about;
        button_language = binding.language;
        button_myaccount = binding.account;

        // Mettre en place les listeners

        // Appuyer le bouton nous envoie vers un autre activité

        button_language.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(View view) {
                createNewDialog("language");
            }
        });

        button_aboutus.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(View view) {
                createNewDialog("aboutus");
            }
        });

        button_myaccount.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(View view) {
                createNewDialog("myaccount");
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

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void createNewDialog(String setting){
        dialogBuilder = new AlertDialog.Builder(this);
        View popUpView=null;

        if(setting.equals("aboutus")){
            popUpView = getLayoutInflater().inflate(R.layout.popup_settings_about,null);
        }else if(setting.equals("language")) {
            popUpView = getLayoutInflater().inflate(R.layout.popup_settings_language, null);

            String[] languages = {"English"};
            Spinner spino = (Spinner) popUpView.findViewById(R.id.spinnerLanguage);
            spino.setOnItemSelectedListener((AdapterView.OnItemSelectedListener) this);
            ArrayAdapter aa = new ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, languages);
            aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spino.setAdapter(aa);

        }else if(setting.equals("myaccount")){
            popUpView = getLayoutInflater().inflate(R.layout.popup_settings_myaccount, null);

            TextView text = (TextView) popUpView.findViewById(R.id.text);
            Button logoutbutton = (Button) popUpView.findViewById(R.id.logoutbutton);

            List<String> info = recoverProfileFromJson();

            String level = info.get(0);
            String exp = info.get(1);
            String gold = info.get(2);
            String avatarName = info.get(3);

            String accountTxt = "Pet name : "+avatarName+"\nLevel : "+level+"\nExp : "+exp+"\nGold : "+gold+"\n";
            logoutbutton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    FirebaseAuth.getInstance().signOut();
                    startActivity(new Intent(getApplicationContext(), EmailPasswordActivity.class));
                }
            });

            text.setText(accountTxt);

        }else{
            popUpView = getLayoutInflater().inflate(R.layout.popup_settings_about,null);
        }

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

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public List<String> recoverProfileFromJson() {
        File path = getApplicationContext().getFilesDir();
        FileInputStream fis = null;

        // Profile
        try {
            fis = new FileInputStream(new File(path, "profile.json"));
            List<String> listStringFromProfile = JsonManager.readProfileStream(fis);
            return listStringFromProfile;
        } catch (IOException e) {

        }
        return null;
    }
}