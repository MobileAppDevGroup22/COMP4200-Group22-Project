package group22.gastracker;

import androidx.annotation.NonNull;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationBarView;
import com.google.android.material.textfield.TextInputLayout;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends GlobalActivity {


    FloatingActionButton addNewEntryButton;

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

        bottomNav = findViewById(R.id.bottomNavHome);
        bottomNav.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                switch (item.getItemId()){
                    case(R.id.nav_home):
                        //Intent vehicleActivity_intent = new Intent(MainActivity.this, VehicleActivity.class);
                        break;
                    case(R.id.nav_vehicle_list):
                        Intent vehicleActivity_intent = new Intent(MainActivity.this, VehicleActivity.class);
                        startActivity(vehicleActivity_intent);
                        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                        break;
                    case(R.id.nav_settings):
                        Intent settingsActivity_intent = new Intent(MainActivity.this, SettingsActivity.class);
                        startActivity(settingsActivity_intent);
                        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                        break;
                }

                return false;
            }
        });

        /******************************************
         * Add method to get the users vehicles from database
         *  // vehiclesList.getUsersVehicles();
         */

        //vehicles for testing
        vehicleList.add("   2014 Ford Edge");
        vehicleList.add("   2006 Toyota Corolla");
        vehicleList.add("   2006 Honda Civic");

        vehicleDropDown = findViewById(R.id.textInputLayout);
        vehicleDropDownOptions = findViewById(R.id.vehicle_dropdown);
        arrayAdapter_vehicles = new ArrayAdapter<>(getApplicationContext(), androidx.appcompat.R.layout.support_simple_spinner_dropdown_item, vehicleList);
        vehicleDropDownOptions.setAdapter(arrayAdapter_vehicles);

        addNewEntryButton = findViewById(R.id.actionButton_addEntry);
        addNewEntryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

    }

    @Override
    public void onBackPressed() {}

}