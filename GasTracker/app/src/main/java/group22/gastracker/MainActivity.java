package group22.gastracker;

import androidx.annotation.NonNull;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationBarView;
import com.google.android.material.textfield.TextInputLayout;

import java.util.ArrayList;
import java.util.List;

import group22.gastracker.purchases.PurchaseListActivity;

public class MainActivity extends GlobalActivity {

    FloatingActionButton addNewEntryButton;
    Button seeAllPurchasesButton;

    TextInputLayout vehicleDropDown;
    AutoCompleteTextView vehicleDropDownOptions;
    List<String> vehicleList = new ArrayList<String>();
    ArrayAdapter<String> arrayAdapter_vehicles;

    BottomNavigationView bottomNav;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().hide();

        this.bottomNavBarHandler();
        setAddNewEntryButtonHandler();
        seeAllPurchasesButtonHandler();

        /******************************************
         * Add method to get the users vehicles from database
         *  // vehiclesList.getUsersVehicles();
         */
        //vehicles for testing
        vehicleList.add("2014 Ford Edge");
        vehicleList.add("2006 Toyota Corolla");
        vehicleList.add("2006 Honda Civic");

        /*******************************************************************************************************
         * Setup vehicle dropdown list bar*/
        vehicleDropDown = findViewById(R.id.textInputLayout);
        vehicleDropDownOptions = findViewById(R.id.vehicle_dropdown);
        arrayAdapter_vehicles = new ArrayAdapter<>(getApplicationContext(), androidx.appcompat.R.layout.support_simple_spinner_dropdown_item, vehicleList);
        vehicleDropDownOptions.setAdapter(arrayAdapter_vehicles);
        vehicleDropDownOptions.setText(vehicleList.get(0), false);

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
            }
        });
    }

    @Override
    public void onBackPressed() {}//disables back android button

}