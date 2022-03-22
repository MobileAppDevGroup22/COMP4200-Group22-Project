package group22.gastracker;

import static group22.gastracker.Utility.HandleReceivedData;

import androidx.annotation.NonNull;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationBarView;
import com.google.android.material.textfield.TextInputLayout;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import group22.gastracker.purchases.PurchaseListActivity;

public class MainActivity extends GlobalActivity {

    FloatingActionButton addNewEntryButton;
    Button seeAllPurchasesButton;

    TextInputLayout vehicleDropDown;
    AutoCompleteTextView vehicleDropDownOptions;
    List<String> arrayList_vehicleList = new ArrayList<String>();
    List<Integer> arrayList_vehicleIDList = new ArrayList<Integer>();
    ArrayAdapter<String> arrayAdapter_vehicles;

    BottomNavigationView bottomNav;

    int currentVehiclePosition;
    int currentTheme = 0;
    boolean darkMode = false;
    SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSavedValue();
        updateTheme();
        setContentView(R.layout.activity_main);
        setTitle("Overview");

        this.bottomNavBarHandler();

        //getVehicleList();
        getStats();

        setAddNewEntryButtonHandler();
        seeAllPurchasesButtonHandler();

        /******************************************
         * Add method to get the users vehicles from database
         *  // vehiclesList.getUsersVehicles();
         */
        //vehicles for testing
        arrayList_vehicleList.add("2014 Ford Edge");
        arrayList_vehicleList.add("2006 Toyota Corolla");
        arrayList_vehicleList.add("2006 Honda Civic");

        /*******************************************************************************************************
         * Setup vehicle dropdown list bar*/
        vehicleDropDown = findViewById(R.id.textInputLayout);
        vehicleDropDownOptions = findViewById(R.id.vehicle_dropdown);
        arrayAdapter_vehicles = new ArrayAdapter<>(getApplicationContext(), R.layout.dropdown_item, arrayList_vehicleList);
        vehicleDropDownOptions.setAdapter(arrayAdapter_vehicles);
        vehicleDropDownOptions.setText(arrayList_vehicleList.get(currentVehiclePosition), false);

        vehicleDropDownOptions.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                currentVehiclePosition = position;
                rememberVehicle();
            }
        });

    }

    protected void getStats(){
        GlobalGasTracker globalData = (GlobalGasTracker) getApplication();
        String username = globalData.getUsername();

        HashMap<String, String> params = new HashMap<String, String>();
        params.put("type", "stat");
        params.put("username", username);
        if(arrayList_vehicleIDList.isEmpty())return;
        params.put("vehicleid", Integer.toString(arrayList_vehicleIDList.get(currentVehiclePosition)));
        MakeRequest(Request.Method.GET, params,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String r) {

                        ArrayList<Bundle> extractedData = HandleReceivedData(getApplicationContext(), r);
                        if (extractedData == null)return;

                    }
                });
    }

    protected void getVehicleList(){
        GlobalGasTracker globalData = (GlobalGasTracker) getApplication();
        String username = globalData.getUsername();

        HashMap<String, String> params = new HashMap<String, String>();
        params.put("type", "vehicle");
        params.put("username", username);

        MakeRequest(Request.Method.GET, params,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String r) {

                        ArrayList<Bundle> extractedData = HandleReceivedData(getApplicationContext(), r);
                        if (extractedData == null)return;
                        int i = 0;
                        for(Bundle currentDataBundle : extractedData){
                            arrayList_vehicleList.add(currentDataBundle.getString("vehiclename", null));
                            arrayList_vehicleIDList.add(currentDataBundle.getInt("vehicleid", 0));
                        }
                    }
                });
    }

    /*******************************************************************************************************
     * Navigation bar*/
    protected void bottomNavBarHandler(){
        bottomNav = findViewById(R.id.bottomNavHome);
        bottomNav.setSelectedItemId(R.id.nav_home);
        bottomNav.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case(R.id.nav_home):
                        break;
                    case(R.id.nav_vehicle_list):
                        Intent vehicleActivity_intent = new Intent(MainActivity.this, VehicleActivity.class);
                        startActivity(vehicleActivity_intent);
                        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                        break;
                    case(R.id.nav_settings):
                        item.setChecked(true);
                        Intent settingsActivity_intent = new Intent(MainActivity.this, SettingsActivity.class);
                        startActivity(settingsActivity_intent);
                        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                        break;
                }
                return true;
            }
        });
    }
    /*******************************************************************************************************
     * see all purchases button*/
    protected void seeAllPurchasesButtonHandler(){
        seeAllPurchasesButton = findViewById(R.id.button_seeAllPurchases);
        seeAllPurchasesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent purchaseListActivity_intent = new Intent(MainActivity.this, PurchaseListActivity.class);
                purchaseListActivity_intent.putExtra("CurrentVehicle", vehicleDropDown.getEditText().getText().toString());
                startActivity(purchaseListActivity_intent);
            }
        });
    }
    /*******************************************************************************************************
     * add new entry button*/
    protected void setAddNewEntryButtonHandler(){
        addNewEntryButton = findViewById(R.id.actionButton_addEntry);
        addNewEntryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getApplicationContext(), vehicleDropDownOptions.getText().toString(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onBackPressed() {}//disables back android button

    /*******************************************************************************************************
     * Shared prefs*/
    private void rememberVehicle(){
        sharedPreferences = getSharedPreferences("saveCurrentVehicle", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt("currentVehicle", currentVehiclePosition);
        editor.commit();
    }
    protected void getSavedValue(){
        sharedPreferences = getSharedPreferences("saveCurrentVehicle", Context.MODE_PRIVATE);
        currentVehiclePosition = sharedPreferences.getInt("currentVehicle", 0);
        sharedPreferences = getSharedPreferences("themeInfo", Context.MODE_PRIVATE);
        currentTheme = sharedPreferences.getInt("currentTheme", 0);
        darkMode = sharedPreferences.getBoolean("darkMode", false);
    }

    public void updateTheme(){
        if(darkMode){
            setTheme(R.style.Theme_GasTrackerDark);
            if(currentTheme == 0)
                setTheme(R.style.purpleDark);
            else if(currentTheme == 1)
                setTheme(R.style.blueDark);
            else if(currentTheme == 2)
                setTheme(R.style.yellowDark);
            else if(currentTheme == 3)
                setTheme(R.style.redDark);
            else if(currentTheme == 4)
                setTheme(R.style.greenDark);
            else if(currentTheme == 5)
                setTheme(R.style.pinkDark);
            else
                setTheme(R.style.purpleDark);
        }else{
            setTheme(R.style.Theme_GasTracker);
            if(currentTheme == 0)
                setTheme(R.style.purple);
            else if(currentTheme == 1)
                setTheme(R.style.blue);
            else if(currentTheme == 2)
                setTheme(R.style.yellow);
            else if(currentTheme == 3)
                setTheme(R.style.red);
            else if(currentTheme == 4)
                setTheme(R.style.green);
            else if(currentTheme == 5)
                setTheme(R.style.pink);
            else
                setTheme(R.style.purple);
        }
    }

}