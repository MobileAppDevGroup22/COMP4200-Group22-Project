package group22.gastracker;

import static group22.gastracker.Utility.HandleReceivedData;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

import java.util.ArrayList;
import java.util.HashMap;

public class VehicleActivity extends GlobalActivity {

    BottomNavigationView bottomNav;

    int currentTheme = 0;
    SharedPreferences sharedPreferences;

    Button addVehicleButton;

    ListView vehicleListView;
    ArrayList<String> arrayList_vehicleList = new ArrayList<>();
    ArrayAdapter<String> adapter_vehicleList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSavedValue();
        updateTheme();
        setContentView(R.layout.activity_vehicle);
        this.bottomNavBarHandler();
        GlobalGasTracker globalData = (GlobalGasTracker) getApplication();

        addVehicleButton = findViewById(R.id.button_addVehicle);

        vehicleListView = findViewById(R.id.listView_vehicles);
        /*
        arrayList_vehicleList.add("2014 Ford Edge");
        arrayList_vehicleList.add("2006 Toyota Corolla");
        arrayList_vehicleList.add("2006 Honda Civic");
         */
        getVehicleList();
        adapter_vehicleList = new ArrayAdapter<>(getApplicationContext(), android.R.layout.simple_list_item_1, arrayList_vehicleList);
        vehicleListView.setAdapter(adapter_vehicleList);

        addVehicleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String username = globalData.getUsername();
                Toast toast = Toast.makeText(getApplicationContext(), username, Toast.LENGTH_SHORT);
                toast.show();
            }
        });

    }

    protected void getVehicleList(){
        GlobalGasTracker globalData = (GlobalGasTracker) getApplication();
        String username = globalData.getUsername();
/*
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("type", "vehicle");
        params.put("username", username);

        MakeRequest(Request.Method.POST, params,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String r) {
                        //Log.d("Volley Log", r);

                        Bundle extractedData = HandleReceivedData(getApplicationContext(), r);
                        if (extractedData == null)return;

                        arrayList_vehicleList =
                    }
                });*/
    }

    /*******************************************************************************************************
     * Navigation bar*/
    protected void bottomNavBarHandler(){
        bottomNav = findViewById(R.id.bottomNavVehicle);
        bottomNav.setSelectedItemId(R.id.nav_vehicle_list);
        bottomNav.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case(R.id.nav_home):
                        Intent mainActivity_intent = new Intent(VehicleActivity.this, MainActivity.class);
                        startActivity(mainActivity_intent);
                        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                        break;
                    case(R.id.nav_vehicle_list):
                        break;
                    case(R.id.nav_settings):
                        Intent settingsActivity_intent = new Intent(VehicleActivity.this, SettingsActivity.class);
                        startActivity(settingsActivity_intent);
                        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                        break;
                }
                return true;
            }
        });
    }

    @Override
    public void onBackPressed() {}

    protected void getSavedValue(){
        sharedPreferences = getSharedPreferences("themeInfo", Context.MODE_PRIVATE);
        currentTheme = sharedPreferences.getInt("currentTheme", 0);
    }

    public void updateTheme(){
        if(currentTheme == 0){
            setTheme(R.style.purple);
        }else if(currentTheme == 1){
            setTheme(R.style.blue);
        }else if(currentTheme == 2){
            setTheme(R.style.yellow);
        }else if(currentTheme == 3){
            setTheme(R.style.red);
        }else if(currentTheme == 4){
            setTheme(R.style.green);
        }else if(currentTheme == 5){
            setTheme(R.style.pink);
        }else{
            setTheme(R.style.purple);
        }
    }

}