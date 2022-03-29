package group22.gastracker;

import static group22.gastracker.Utility.HandleReceivedData;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationBarView;

import java.util.ArrayList;
import java.util.HashMap;

import group22.gastracker.purchases.PurchaseListActivity;

public class VehicleActivity extends GlobalActivity {

    BottomNavigationView bottomNav;

    boolean darkMode = false;
    int currentTheme = 0;
    SharedPreferences sharedPreferences;

    FloatingActionButton addVehicleFloatButton;
    Button addVehicleButton, cancelNewVehicleButton;
    EditText vehicleName;
    ListView vehicleListView;
    ArrayList<String> arrayList_vehicleList = new ArrayList<>();
    ArrayAdapter<String> adapter_vehicleList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSavedValue();
        updateTheme();
        setContentView(R.layout.activity_vehicle);
        setTitle("Vehicles");
        this.bottomNavBarHandler();
        GlobalGasTracker globalData = (GlobalGasTracker) getApplication();

        addVehicleFloatButton = findViewById(R.id.floatingActionButton_addVehicle);
        vehicleListView = findViewById(R.id.listView_vehicles);
        getVehicleList();



    }

    protected void VehicleActivityFunctions(ArrayList<Bundle> extractedData){
        if(extractedData != null){
            for(Bundle currentDataBundle : extractedData){
                String vehicle = currentDataBundle.getString("vehiclename", null);
                arrayList_vehicleList.add(vehicle);
            }
            if(darkMode) {
                adapter_vehicleList = new VehicleListAdapter(getApplicationContext(), R.layout.vehicle_list_adapter_layout, arrayList_vehicleList);
            }else {
                adapter_vehicleList = new VehicleListAdapter(getApplicationContext(), R.layout.vehicle_list_dark_adapter_layout, arrayList_vehicleList);
            }
            vehicleListView.setAdapter(adapter_vehicleList);
        }

        addVehicleFloatButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showAddVehicleDialog();
            }
        });

        vehicleListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int selectedItem, long l) {
                Bundle bundle = extractedData.get(selectedItem);
                int vehicleID = bundle.getInt("vehicleid", -1);
                deleteVehicleDialog(vehicleID);
                return false;
            }
        });

    }

    protected void VehicleActivityEmptyFunctions() {
    }

    protected void showAddVehicleDialog(){
        final Dialog dialog = new Dialog(VehicleActivity.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(true);
        dialog.setContentView(R.layout.add_vehicle_dialog);

        addVehicleButton = dialog.findViewById(R.id.button_addVehicle);
        cancelNewVehicleButton = dialog.findViewById(R.id.button_cancelNewVehicle);
        vehicleName = dialog.findViewById(R.id.editText_vehicle);

        addVehicleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String newVehicle = vehicleName.getText().toString();
                if(!newVehicle.equals("")){
                    addVehicle(newVehicle);
                }
                dialog.dismiss();
                finish();
                startActivity(getIntent());
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
            }
        });

        cancelNewVehicleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

        dialog.show();
    }

    protected void deleteVehicleDialog(int vehicleID){
        AlertDialog.Builder deleteDialog = new AlertDialog.Builder(VehicleActivity.this);
        deleteDialog.setTitle("Delete Vehicle");
        deleteDialog.setMessage("Do you want to delete this vehicle?");
        deleteDialog.setCancelable(false);
        deleteDialog.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });
        deleteDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                deleteVehicle(vehicleID);
            }
        });
        AlertDialog alert = deleteDialog.create();
        alert.show();
    }

    protected void deleteVehicle(int vehicleID){
        GlobalGasTracker globalData = (GlobalGasTracker) getApplication();
        String password = globalData.getPassword();

        HashMap<String, String> params = new HashMap<String, String>();
        params.put("type", "vehicle");
        params.put("vehicleid", Integer.toString(vehicleID));
        params.put("password", password);

        MakeRequest(Request.Method.DELETE, params,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String r) {
                        ArrayList<Bundle> users = Utility.HandleReceivedData(getApplicationContext(), r);
                        if (users == null) return;

                        for (Bundle u:users){
                            Log.d("Bundle Array", u.toString());
                        }
                        finish();
                        startActivity(getIntent());
                        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                    }
                });
    }

    protected void addVehicle(String vehicleName){
        GlobalGasTracker globalData = (GlobalGasTracker) getApplication();
        String username = globalData.getUsername();

        HashMap<String, String> params = new HashMap<String, String>();
        params.put("type", "vehicle");
        params.put("username", username);
        params.put("vehiclename", vehicleName);

        //make request (be careful of GET, POST or DELETE methods)
        MakeRequest(Request.Method.POST, params,
                new Response.Listener<String>() {
                @Override
                public void onResponse(String r) {
                    Log.d("Volley Log", r);

                    ArrayList<Bundle> vehicles = Utility.HandleReceivedData(getApplicationContext(), r);
                    if (vehicles == null) return;

                    for (Bundle u:vehicles){
                        Log.d("Bundle Array", u.toString());
                    }
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

                    if (extractedData == null)VehicleActivityEmptyFunctions();
                    VehicleActivityFunctions(extractedData);
                }
            });
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