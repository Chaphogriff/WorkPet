package com.workthrutheweak.workpet;

import androidx.appcompat.app.AppCompatActivity;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;

import com.workthrutheweak.workpet.databinding.ActivityFirstBinding;

public class FirstActivity extends AppCompatActivity {
    private ActivityFirstBinding binding; //For ViewBinding feature

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first);

        //Setup View Binding variable
        binding = ActivityFirstBinding.inflate(getLayoutInflater());
        View v = binding.getRoot();
        setContentView(v);

        getSupportActionBar().hide();

        final Intent i = new Intent(FirstActivity.this, EmailPasswordActivity.class);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                startActivity(i);
                finish();
            }
        }, 1000);
    }
}