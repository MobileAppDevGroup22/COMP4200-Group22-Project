package group22.gastracker;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

public class SettingsActivity extends AppCompatActivity {

    BottomNavigationView bottomNav;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        /*******************************************************************************************************
         * Setup Navigation bar*/
        bottomNav = findViewById(R.id.bottomNavSettings);
        bottomNav.setSelectedItemId(R.id.nav_settings);
        bottomNav.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case(R.id.nav_home):
                        Intent mainActivity_intent = new Intent(SettingsActivity.this, MainActivity.class);
                        startActivity(mainActivity_intent);
                        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                        break;
                    case(R.id.nav_vehicle_list):
                        Intent vehicleActivity_intent = new Intent(SettingsActivity.this, VehicleActivity.class);
                        startActivity(vehicleActivity_intent);
                        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                        break;
                    case(R.id.nav_settings):
                        break;
                }
                return true;
            }
        });
        /*******************************************************************************************************/

    }

    @Override
    public void onBackPressed() {}

}
