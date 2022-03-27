package group22.gastracker;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

public class VehicleActivity extends GlobalActivity {

    BottomNavigationView bottomNav;
    private AlertDialog.Builder dialogBuilder;
    private AlertDialog dialog;
    private EditText newvehiclepopup_name, newvehiclepopup_type, newvehiclepopup_year;
    private Button newvehcilepopup_save, newvehiclepopup_cancel;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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
    /*******************************************************************************************************
     * Popup Window*/
    public void createNewVehicleDialog(){
        dialogBuilder= new AlertDialog.Builder( this);
        final View vehiclePopupView = getLayoutInflater().inflate(R.layout.activity_vehicle, null);
        newvehiclepopup_name=(EditText) vehiclePopupView.findViewById(R.id.newvehiclepopup_name);
        newvehiclepopup_type=(EditText) vehiclePopupView.findViewById(R.id.newvehiclepopup_type);
        newvehiclepopup_year=(EditText) vehiclePopupView.findViewById(R.id.newvehiclepopup_year);

        newvehcilepopup_save=(Button) vehiclePopupView.findViewById(R.id.save_Button);
        newvehiclepopup_cancel=(Button) vehiclePopupView.findViewById(R.id.cancel_Button);

        dialogBuilder.setView(vehiclePopupView);
        dialog = dialogBuilder.create();
        dialog.show();

        newvehcilepopup_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //how tf do you save the data??
            }
        });
        newvehiclepopup_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

    }

}
