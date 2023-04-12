package com.workthrutheweak.workpet;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;

import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.gson.JsonObject;
import com.workthrutheweak.workpet.JsonManagement.JsonManager;
import com.workthrutheweak.workpet.databinding.ActivityAvatarBinding;
import com.workthrutheweak.workpet.model.Task;

import org.json.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import pl.droidsonroids.gif.GifImageView;

public class AvatarActivity extends AppCompatActivity {

    // Variables
    private ActivityAvatarBinding binding; //For ViewBinding feature
    GifImageView heart;
    GifImageView pet;
    MediaPlayer mp;
    Vibrator vibe;
    TextView goldTextView;
    TextView levelTextView;
    ProgressBar progressBar;
    Button shopButton;
    Button inventoryButton;
    Button customizeButton;
    GifImageView avatarView;
    int gold=99;
    int level=2;
    int exp=75; // entre 0 et 100 ! ( si > 100, on level up )
    String avatarName="cat";

    @RequiresApi(api = Build.VERSION_CODES.O)
    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_avatar);

        //Setup View Binding variable
        binding = ActivityAvatarBinding.inflate(getLayoutInflater());
        View v = binding.getRoot();
        setContentView(v);

        // Récupérer les éléments du xml
        heart = binding.heart;
        pet = binding.pet;
        goldTextView = binding.goldText;
        levelTextView = binding.lvlText;
        progressBar = binding.expBar;
        shopButton = binding.shop;
        inventoryButton = binding.inventory;
        customizeButton = binding.customize;
        avatarView = binding.pet;

        // Initialisation valeurs
        mp = MediaPlayer.create(this, R.raw.pet_sample);
        vibe = (Vibrator) this.getSystemService(Context.VIBRATOR_SERVICE);
        heart.setVisibility(View.INVISIBLE);

        // Récupération des données
        recoverDataFromJson();
        Intent intent = getIntent();
        if(intent.hasExtra("avatar")) {
            avatarName = intent.getExtras().getString("avatar");
        }

        // Set valeurs
        levelTextView.setText("Lv. "+level);
        goldTextView.setText("Gold: "+gold);
        progressBar.setProgress(exp);
        try {
            setAvatar(avatarName);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        // ajouter tous les listeners
        setListeners();
    }

    // Méthode qui assemble tous les listeners : appelée dans onCreate
    public void setListeners(){

        // PET GIF
        long[] vibrate_pattern = {0, 200, 0}; //0 to start now, 200 to vibrate 200 ms, 0 to sleep for 0 ms.
        // on Hold
        pet.setOnTouchListener(new View.OnTouchListener() {
            @RequiresApi(api = Build.VERSION_CODES.Q)
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    heart.setVisibility(View.VISIBLE);
                    mp.start();
                    vibe.vibrate(vibrate_pattern, 0);
                    // Gold test = one click gold up : TODO : remove this
                    gold++;
                    goldTextView.setText("Gold: "+gold);

                } else if (event.getAction() == MotionEvent.ACTION_UP) {
                    // ne fonctionne pas -> voir onClick
                }
                return false;
            }
        });
        // onRelease ( MotionEvent.ACTION_UP ne fonctionne pas )
        pet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                heart.setVisibility(View.INVISIBLE);
                vibe.cancel();
            }
        });

        // Listener pour les trois boutons
        Intent i = new Intent(this, ListActivity.class);
        shopButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                i.putExtra("collection", "shop");
                i.putExtra("gold",gold);
                startActivity(i);
            }
        });
        inventoryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                i.putExtra("collection", "inventory");
                startActivity(i);
            }
        });
        customizeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                i.putExtra("collection", "customize");
                startActivity(i);
            }
        });



        // BARRE DE NAVIGATION
        @SuppressLint({"MissingInflatedId", "LocalSuppress"}) BottomNavigationView bottomNavigationView = binding.nav;
        bottomNavigationView.setSelectedItemId(R.id.avatar);
        //ajout du navbar
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                switch (item.getItemId()) {
                    case R.id.calendar:
                        mp.release();
                        startActivity(new Intent(getApplicationContext(), CalendarActivity.class));
                        overridePendingTransition(0, 0);
                        return true;
                    case R.id.avatar:
                        mp.release();
                        return true;
                    case R.id.home:
                        mp.release();
                        startActivity(new Intent(getApplicationContext(), MainActivity.class));
                        overridePendingTransition(0, 0);
                        return true;
                    case R.id.setting:
                        mp.release();
                        startActivity(new Intent(getApplicationContext(), SettingActivity.class));
                        overridePendingTransition(0, 0);
                        return true;
                    case R.id.task:
                        mp.release();
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

    // Récupère des données de JSON
    @RequiresApi(api = Build.VERSION_CODES.O)
    public void recoverDataFromJson(){

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

    public void updateDataToJson(){
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
        updateDataToJson();
        super.onPause();
    }
}