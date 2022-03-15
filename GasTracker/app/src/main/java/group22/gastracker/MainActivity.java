package group22.gastracker;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationBarView;
import com.google.android.material.textfield.TextInputLayout;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

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

        bottomNav = findViewById(R.id.bottomNavigationView);
        bottomNav.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                switch (item.getItemId()){
                    case(R.id.nav_home): Toast.makeText(getApplicationContext(), "Home", Toast.LENGTH_SHORT).show();
                        break;
                    case(R.id.nav_vehicle_list): Toast.makeText(getApplicationContext(), "Vehicles", Toast.LENGTH_SHORT).show();
                        break;
                    case(R.id.nav_settings): Toast.makeText(getApplicationContext(), "Settings", Toast.LENGTH_SHORT).show();
                        break;
                }

                return false;
            }
        });

        /******************************************
         * Add method to get the users vehicles from database
         *  // vehiclesList.getUsersVehicles();
         */

        //vehicles for testing        vehicleList.add("   2014 Ford Edge");
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

}