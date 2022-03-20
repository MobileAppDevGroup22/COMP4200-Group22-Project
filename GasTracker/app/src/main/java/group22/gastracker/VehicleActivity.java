package group22.gastracker;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

public class VehicleActivity extends GlobalActivity {

    BottomNavigationView bottomNav;

    int currentTheme = 0;
    SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSavedValue();
        updateTheme();
        setContentView(R.layout.activity_vehicle);

        this.bottomNavBarHandler();

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