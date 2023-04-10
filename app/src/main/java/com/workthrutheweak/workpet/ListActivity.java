package com.workthrutheweak.workpet;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Adapter;
import android.widget.Button;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.workthrutheweak.workpet.adapter.ListAdapter;
import com.workthrutheweak.workpet.databinding.ActivityListBinding;
import com.workthrutheweak.workpet.databinding.ActivityMainBinding;

import java.util.ArrayList;
import java.util.List;

public class ListActivity extends AppCompatActivity {

    // Variables
    private ActivityListBinding binding; //For ViewBinding feature
    private RecyclerView dataList;
    List<String> titles;
    List<Integer> images;
    List<String> prices;
    ListAdapter adapter;
    Button button_back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);

        //Setup View Binding variable
        binding = ActivityListBinding.inflate(getLayoutInflater());
        View v = binding.getRoot();
        setContentView(v);

        // Récupérer les éléments du xml
        dataList = binding.dataList;
        button_back = binding.back;

        // Initialisation variables
        titles = new ArrayList<>();
        images = new ArrayList<>();
        prices = new ArrayList<>();

        // Générer la liste
        generateList();

        BottomNavigationView bottomNavigationView = binding.nav;
        bottomNavigationView.getMenu().findItem(R.id.avatar).setChecked(true);

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
                        startActivity(new Intent(getApplicationContext(), TaskActivity.class));
                        overridePendingTransition(0, 0);
                        return true;
                }
                return false;
            }
        });

        // Appuyer le bouton nous envoie vers un autre activité
        button_back.setOnClickListener(view ->
                startActivity(new Intent(this, MainActivity.class))
        );

    }

    void generateList(){

        titles.add("Cookie");
        titles.add("Bread");
        titles.add("Can of food");
        titles.add("Meat");
        titles.add("Star fruit");

        images.add(R.drawable.cookie);
        images.add(R.drawable.bread);
        images.add(R.drawable.can);
        images.add(R.drawable.meat);
        images.add(R.drawable.starfruit);

        prices.add("50 Gold");
        prices.add("50 Gold");
        prices.add("25 Gold");
        prices.add("100 Gold");
        prices.add("500 Gold");

        adapter = new ListAdapter(this,titles,images,prices);

        GridLayoutManager gridLayoutManager = new GridLayoutManager(this,2,GridLayoutManager.VERTICAL,false);
        dataList.setLayoutManager(gridLayoutManager);
        dataList.setAdapter(adapter);

    }

}