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
import android.widget.TextView;

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
    String mode;
    TextView modeText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);
        Bundle bundle = getIntent().getExtras();
        mode = bundle.getString("collection");

        //Setup View Binding variable
        binding = ActivityListBinding.inflate(getLayoutInflater());
        View v = binding.getRoot();
        setContentView(v);

        // Récupérer les éléments du xml
        dataList = binding.dataList;
        button_back = binding.back;
        modeText = binding.mode;
        modeText.setText(mode.substring(0,1).toUpperCase() + mode.substring(1).toLowerCase());

        // Initialisation variables
        titles = new ArrayList<>();
        images = new ArrayList<>();
        prices = new ArrayList<>();

        // Générer la liste
        if(mode.equals("shop")){
            generateShop();
        }else if(mode.equals("inventory")){
            generateInventory();
        }else if(mode.equals("customize")){
            generateCustomized();
        }

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
                startActivity(new Intent(this, AvatarActivity.class))
        );

    }

    void generateShop(){

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

    public void generateInventory(){
        adapter = new ListAdapter(this,titles,images,prices);

        GridLayoutManager gridLayoutManager = new GridLayoutManager(this,2,GridLayoutManager.VERTICAL,false);
        dataList.setLayoutManager(gridLayoutManager);
        dataList.setAdapter(adapter);
    }

    public void generateCustomized(){

        titles.add("Brown cat");
        titles.add("Panda");
        titles.add("Bunny");
        titles.add("Gray Cat");
        titles.add("Dog");

        images.add(R.drawable.cat);
        images.add(R.drawable.panda);
        images.add(R.drawable.rabbit);
        images.add(R.drawable.cat2);
        images.add(R.drawable.dog);

        prices.add("Meow meow !");
        prices.add("Honk honk !");
        prices.add("Squeak squeak !");
        prices.add("Meeeeeow !");
        prices.add("Bark bark!");

        adapter = new ListAdapter(this,titles,images,prices);

        GridLayoutManager gridLayoutManager = new GridLayoutManager(this,2,GridLayoutManager.VERTICAL,false);
        dataList.setLayoutManager(gridLayoutManager);
        dataList.setAdapter(adapter);
    }

}