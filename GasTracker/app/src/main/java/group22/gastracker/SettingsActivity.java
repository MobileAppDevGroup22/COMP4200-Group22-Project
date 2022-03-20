package group22.gastracker;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

public class SettingsActivity extends GlobalActivity {

    BottomNavigationView bottomNav;

    Switch darkModeSwitch;
    Button themeSelectButton;

    private AlertDialog.Builder dialogBuilder;
    private AlertDialog dialog;
    Button purpleButton;
    Button blueButton;
    Button yellowButton;
    Button redButton;
    Button greenButton;
    Button pinkButton;

    int currentTheme;
    SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSavedValue();
        updateTheme();
        setContentView(R.layout.activity_settings);
        getSupportActionBar().hide();
        this.bottomNavBarHandler();

        themeSelectButton = findViewById(R.id.button_themeSelect);
        darkModeSwitch = findViewById(R.id.switch_DarkMode);

        themeSelectButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectThemeDialog();
            }
        });

        darkModeSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean checked) {

            }
        });

    }

    /*******************************************************************************************************
     * Navigation bar*/
    protected void bottomNavBarHandler(){
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
    }

    public void selectThemeDialog(){
        dialogBuilder = new AlertDialog.Builder(this);
        final View themeSelectionPopup = getLayoutInflater().inflate(R.layout.theme_picker_dialog, null);
        purpleButton = themeSelectionPopup.findViewById(R.id.button_purple);
        blueButton = themeSelectionPopup.findViewById(R.id.button_blue);
        yellowButton = themeSelectionPopup.findViewById(R.id.button_yellow);
        redButton = themeSelectionPopup.findViewById(R.id.button_red);
        greenButton = themeSelectionPopup.findViewById(R.id.button_green);
        pinkButton = themeSelectionPopup.findViewById(R.id.button_pink);

        dialogBuilder.setView(themeSelectionPopup);
        dialog = dialogBuilder.create();
        dialog.show();

        purpleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                currentTheme = 0;
                saveValue();
                dialog.dismiss();
            }
        });
        blueButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                currentTheme = 1;
                saveValue();
                dialog.dismiss();
            }
        });
        yellowButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                currentTheme = 2;
                saveValue();
                dialog.dismiss();
            }
        });
        redButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                currentTheme = 3;
                saveValue();
                dialog.dismiss();
            }
        });
        greenButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                currentTheme = 4;
                saveValue();
                dialog.dismiss();
            }
        });
        pinkButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                currentTheme = 5;
                saveValue();
                dialog.dismiss();
            }
        });
    }

    @Override
    public void onBackPressed() {}

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

    protected void saveValue(){
        sharedPreferences = getSharedPreferences("themeInfo", Context.MODE_PRIVATE);
        SharedPreferences.Editor sharedPrefEditor = sharedPreferences.edit();
        sharedPrefEditor.putInt("currentTheme", currentTheme);
        sharedPrefEditor.commit();
        finish();
        startActivity(getIntent());
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
    }

    protected void getSavedValue(){
        sharedPreferences = getSharedPreferences("themeInfo", Context.MODE_PRIVATE);
        currentTheme = sharedPreferences.getInt("currentTheme", 0);
    }

}
