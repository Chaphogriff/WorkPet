package com.workthrutheweak.workpet;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Adapter;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.workthrutheweak.workpet.JsonManagement.JsonManager;
import com.workthrutheweak.workpet.adapter.ListAdapter;
import com.workthrutheweak.workpet.databinding.ActivityListBinding;
import com.workthrutheweak.workpet.databinding.ActivityMainBinding;
import com.workthrutheweak.workpet.model.Item;
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
    List<Item> ItemList;

    @RequiresApi(api = Build.VERSION_CODES.O)
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
            recoverDataFromJson();
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

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onPause() {
        updateDataToJson();
        super.onPause();
    }

    void generateShop(){

        titles.add("Cookie");
        titles.add("Bread");
        titles.add("Can");
        titles.add("Meat");
        titles.add("Starfruit");

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

        adapter = new ListAdapter(this,titles,images,prices,"shop");

        GridLayoutManager gridLayoutManager = new GridLayoutManager(this,2,GridLayoutManager.VERTICAL,false);
        dataList.setLayoutManager(gridLayoutManager);
        dataList.setAdapter(adapter);

    }

    public void generateInventory(){

        for(Item i : ItemList){
            titles.add(i.getTitle());
            int id = this.getResources().getIdentifier("drawable/"+i.getTitle().toLowerCase(), null, getPackageName());
            images.add(id);
            prices.add(i.getPrice()+" Gold");
        }

        adapter = new ListAdapter(this,titles,images,prices,"inventory");

        GridLayoutManager gridLayoutManager = new GridLayoutManager(this,2,GridLayoutManager.VERTICAL,false);
        dataList.setLayoutManager(gridLayoutManager);
        dataList.setAdapter(adapter);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void recoverDataFromJson(){

        File path = getApplicationContext().getFilesDir();
        FileInputStream fis = null;
        // Profile
        // Liste des tâches
        try {
            fis = new FileInputStream(new File(path, "itemlist.json"));
            ItemList = JsonManager.readItemStream(fis);
        } catch (IOException e) {
            System.out.println(e);
            ItemList = new ArrayList<>();
            ItemList.add(new Item("Bread", "Good bread", 50,10));
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void updateDataToJson(){
        File path = getApplicationContext().getFilesDir();

        try {
            FileOutputStream fos = new FileOutputStream(new File(path, "itemlist.json"));
            JsonManager.writeItemStream(fos, ItemList);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void generateCustomized(){

        titles.add("Cat");
        titles.add("Panda");
        titles.add("Rabbit");
        titles.add("Cat2");
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

        adapter = new ListAdapter(this,titles,images,prices,"customize");

        GridLayoutManager gridLayoutManager = new GridLayoutManager(this,2,GridLayoutManager.VERTICAL,false);
        dataList.setLayoutManager(gridLayoutManager);
        dataList.setAdapter(adapter);
    }

}