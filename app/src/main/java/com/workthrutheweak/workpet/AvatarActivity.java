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

import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.workthrutheweak.workpet.databinding.ActivityAvatarBinding;

import pl.droidsonroids.gif.GifImageView;

public class AvatarActivity extends AppCompatActivity {

    // Variables
    private ActivityAvatarBinding binding; //For ViewBinding feature
    GifImageView heart;
    GifImageView pet;
    MediaPlayer mp;
    Vibrator vibe;

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

        // Initialisation valeurs
        mp = MediaPlayer.create(this, R.raw.pet_sample);
        vibe = (Vibrator) this.getSystemService(Context.VIBRATOR_SERVICE);
        long[] vibrate_pattern = {0, 200, 0}; //0 to start now, 200 to vibrate 200 ms, 0 to sleep for 0 ms.

        // Mettre en place les listeners

        // Appuyer le bouton nous envoie vers un autre activité
        heart.setVisibility(View.INVISIBLE);
        // onHold
        pet.setOnTouchListener(new View.OnTouchListener() {
            @RequiresApi(api = Build.VERSION_CODES.Q)
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    heart.setVisibility(View.VISIBLE);
                    mp.start();
                    vibe.vibrate(vibrate_pattern, 0);

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
}