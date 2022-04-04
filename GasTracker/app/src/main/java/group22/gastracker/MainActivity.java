package group22.gastracker;

import static group22.gastracker.Utility.HandleReceivedData;

import androidx.annotation.NonNull;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.app.Dialog;

import com.android.volley.Request;
import com.android.volley.Response;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationBarView;
import com.google.android.material.textfield.TextInputLayout;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import group22.gastracker.purchases.PurchaseListActivity;

public class MainActivity extends GlobalActivity {

    FloatingActionButton addNewEntryButton;
    Button seeAllPurchasesButton, newPurchaseButton, cancelPurchaseButton;
    Spinner purchaseTypeSelect, currentVehicleSpinner;
    EditText purchaseCost, gasPrice;
    TextView totalSpentDisplay, gasSpentDisplay, gasLitresDisplay, repairsDisplay, insurDisplay, miscDisplay, numPurchDisplay;

    LinearLayout gasCostLayout;
    TextInputLayout vehicleDropDown;
    AutoCompleteTextView vehicleDropDownOptions;
    ArrayList<String> arrayList_vehicleList = new ArrayList<String>();
    ArrayAdapter<String> arrayAdapter_vehicles;

    BottomNavigationView bottomNav;


    double totalSpent = 0;
    double spentOnGas = 0;
    double spentOnRepairs = 0;
    double spentOnInsurance = 0;
    double spentOnMisc = 0;

    int currentListPosition;
    int vehicleID = -1;
    int currentTheme = 0;
    boolean darkMode = false;
    SharedPreferences sharedPreferences;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        GlobalGasTracker globalData = (GlobalGasTracker) getApplication();
        getSavedValue();
        updateTheme();
        setContentView(R.layout.activity_main);
        getSupportActionBar().hide();

        this.bottomNavBarHandler();

        vehicleDropDown = findViewById(R.id.textInputLayout);
        vehicleDropDownOptions = findViewById(R.id.vehicle_dropdown);
        seeAllPurchasesButton = findViewById(R.id.button_seeAllPurchasesVehicle);

        totalSpentDisplay = findViewById(R.id.textView_totalSpentDisplayVehicle);
        gasSpentDisplay = findViewById(R.id.textView_totalSpentGasVehicle);
        gasLitresDisplay = findViewById(R.id.textView_totalLitresVehicle);
        repairsDisplay = findViewById(R.id.textView_totalSpentRepairsVehicle);
        insurDisplay = findViewById(R.id.textView_totalSpentInsuranceVehicle);
        miscDisplay = findViewById(R.id.textView_totalSpentMiscVehicle);
        numPurchDisplay = findViewById(R.id.textView_totalNumberOfPurchasesVehicle);
        getVehicleList();
    }

    protected void MainFunctions(ArrayList<Bundle> extractedPurchases, ArrayList<Bundle> extractedVehicles){
        arrayList_vehicleList.add("All Vehicles");
        for(Bundle currentDataBundle : extractedVehicles){
            String vehicle = currentDataBundle.getString("vehiclename", null);
            arrayList_vehicleList.add(vehicle);
        }
        if(currentListPosition >= arrayList_vehicleList.size()){
            currentListPosition = 0;
        }

        if(extractedPurchases != null){
            if(currentListPosition == 0){
                getStats(extractedPurchases);
            }else{
                int currentVehiclePostition = currentListPosition - 1;
                vehicleID = extractedVehicles.get(currentVehiclePostition).getInt("vehicleid", -1);
                rememberVehicle();
                if(extractedPurchases != null){
                    getVehicleStats(extractedPurchases);
                }
            }
        }else{
            totalSpentDisplay.setText("$0");
            gasSpentDisplay.setText("0");
            gasLitresDisplay.setText("0 L");
            repairsDisplay.setText("$0");
            insurDisplay.setText("$0");
            miscDisplay.setText("$0");
            numPurchDisplay.setText("0");
        }

        addNewEntryButton = findViewById(R.id.actionButton_addEntry);
        vehicleDropDown = findViewById(R.id.textInputLayout);
        vehicleDropDownOptions = findViewById(R.id.vehicle_dropdown);

        arrayAdapter_vehicles = new ArrayAdapter<>(getApplicationContext(), android.R.layout.simple_list_item_1, arrayList_vehicleList);
        vehicleDropDownOptions.setAdapter(arrayAdapter_vehicles);
        vehicleDropDownOptions.setText(arrayList_vehicleList.get(currentListPosition), false);
        vehicleDropDownOptions.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                currentListPosition = position;
                if(currentListPosition == 0){
                    if(extractedPurchases != null){
                        getStats(extractedPurchases);
                    }
                }else{
                    int currentVehiclePostition = currentListPosition - 1;
                    vehicleID = extractedVehicles.get(currentVehiclePostition).getInt("vehicleid", -1);
                    if(extractedPurchases != null){
                        getVehicleStats(extractedPurchases);
                    }
                }
                rememberVehicle();
            }
        });
        vehicleDropDownOptions.requestFocus();

        seeAllPurchasesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(currentListPosition > 0){
                    vehicleID = extractedVehicles.get(currentListPosition-1).getInt("vehicleid", -1);
                    seeAllPurchasesVehicleButtonHandler(vehicleID);
                }else{
                    seeAllPurchasesButtonHandler();
                }
            }
        });

        addNewEntryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showNewPurchaseDialog(extractedVehicles);
            }
        });

    }

    protected void getStats(ArrayList<Bundle> extractedPurchases){
        int purchaseCount = 0;
        double litreCount = 0;
        for(Bundle currentDataBundle : extractedPurchases){
            ++purchaseCount;
            String purchaseType = currentDataBundle.getString("purchasetype", "misc");
            double amount = currentDataBundle.getDouble("amountspent", 0);
            switch (purchaseType){
                case "Gas":
                    spentOnGas += amount;
                    double litrePrice = currentDataBundle.getDouble("purchasedata", 0);
                    litreCount += (amount/litrePrice);
                    break;
                case "General Repair":
                    spentOnRepairs += amount;
                    break;
                case "Oil Change":
                    spentOnRepairs += amount;
                    break;
                case "Insurance":
                    spentOnInsurance += amount;
                    break;
                default:
                    spentOnMisc += amount;
                    break;
            }
        }

        totalSpent = spentOnGas + spentOnRepairs + spentOnInsurance + spentOnMisc;

        totalSpentDisplay.setText(String.format("$%.2f", totalSpent));
        gasSpentDisplay.setText(String.format("$%.2f", spentOnGas));
        gasLitresDisplay.setText(String.format("%.2fL", litreCount));
        repairsDisplay.setText(String.format("$%.2f", spentOnRepairs));
        insurDisplay.setText(String.format("$%.2f", spentOnInsurance));
        miscDisplay.setText(String.format("$%.2f", spentOnMisc));
        numPurchDisplay.setText(Integer.toString(purchaseCount));

        totalSpent = 0;
        spentOnGas = 0;
        spentOnRepairs = 0;
        spentOnInsurance = 0;
        spentOnMisc = 0;
    }

    protected void getVehicleStats(ArrayList<Bundle> extractedPurchases){
        Log.d("helpmedebug", "getting vehicle stats");
        int purchaseCount = 0;
        double litreCount = 0;
        for(Bundle currentDataBundle : extractedPurchases){
            if(currentDataBundle.getInt("vehicleid", -1) == vehicleID){
                ++purchaseCount;
                String purchaseType = currentDataBundle.getString("purchasetype", "misc");
                double amount = currentDataBundle.getDouble("amountspent", 0);
                switch (purchaseType){
                    case "Gas":
                        spentOnGas += amount;
                        double litrePrice = currentDataBundle.getDouble("purchasedata", 0);
                        litreCount += (amount/litrePrice);
                        break;
                    case "General Repair":
                        spentOnRepairs += amount;
                        break;
                    case "Oil Change":
                        spentOnRepairs += amount;
                        break;
                    case "Insurance":
                        spentOnInsurance += amount;
                        break;
                    default:
                        spentOnMisc += amount;
                        break;
                }
            }
        }

        totalSpent = spentOnGas + spentOnRepairs + spentOnInsurance + spentOnMisc;
        double percentOfPurchases = (totalSpent/totalSpent)*100;

        totalSpentDisplay.setText(String.format("$%.2f", totalSpent));
        gasSpentDisplay.setText(String.format("$%.2f", spentOnGas));
        gasLitresDisplay.setText(String.format("%.2fL", litreCount));
        repairsDisplay.setText(String.format("$%.2f", spentOnRepairs));
        insurDisplay.setText(String.format("$%.2f", spentOnInsurance));
        miscDisplay.setText(String.format("$%.2f", spentOnMisc));
        numPurchDisplay.setText(Integer.toString(purchaseCount));

        totalSpent = 0;
        spentOnGas = 0;
        spentOnRepairs = 0;
        spentOnInsurance = 0;
        spentOnMisc = 0;
    }

    void showNewPurchaseDialog(ArrayList<Bundle> extractedVehicles){
        int currentVehicleSpinnerPosition;
        final Dialog dialog = new Dialog(MainActivity.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(true);
        dialog.setContentView(R.layout.new_purchase_dialog);

        purchaseTypeSelect = dialog.findViewById(R.id.spinner_purchaseType);
        purchaseCost = dialog.findViewById(R.id.editText_purchaseCost);
        gasPrice = dialog.findViewById(R.id.editText_gasPrice);
        newPurchaseButton = dialog.findViewById(R.id.button_newPurchase);
        cancelPurchaseButton = dialog.findViewById(R.id.button_cancelNewPurchase);
        gasCostLayout = dialog.findViewById(R.id.LinearLayout_gasCost);
        currentVehicleSpinner = dialog.findViewById(R.id.spinner_vehicleNamePurchase);

        ArrayList<String> purchaseTypeOptions = new ArrayList<String>(Arrays.asList("Gas", "General Repair", "Oil Change", "Insurance", "Misc"));
        ArrayAdapter<String> purchaseAdapter = new ArrayAdapter<String>(this, R.layout.spinner_item_layout, purchaseTypeOptions);
        purchaseTypeSelect.setAdapter(purchaseAdapter);
        purchaseTypeSelect.setSelection(0);

        ArrayList<String> vehicleListOptions = new ArrayList<String>(arrayList_vehicleList);
        vehicleListOptions.remove(0);
        ArrayAdapter<String> vehicleOptionsAdapter = new ArrayAdapter<String>(this, R.layout.spinner_item_layout, vehicleListOptions);
        currentVehicleSpinner.setAdapter(vehicleOptionsAdapter);
        if(currentListPosition > 0){
            currentVehicleSpinnerPosition = currentListPosition-1;
            currentVehicleSpinner.setSelection(currentListPosition-1);
        }else{
            currentVehicleSpinnerPosition = 0;
            currentVehicleSpinner.setSelection(0);
        }

        purchaseTypeSelect.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int selectedItem, long l) {
                if(selectedItem == 0){
                    gasCostLayout.setVisibility(View.VISIBLE);
                }else{
                    gasCostLayout.setVisibility(View.GONE);
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {}
        });

        newPurchaseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String purchaseType = purchaseTypeSelect.getSelectedItem().toString();
                String amount = purchaseCost.getText().toString();
                String data;
                if(purchaseType.equals("Gas"))
                    data = gasPrice.getText().toString();
                else
                    data = null;

                String date = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
                vehicleID = extractedVehicles.get(currentVehicleSpinner.getSelectedItemPosition()).getInt("vehicleid", -1);
                addPurchase(vehicleID, purchaseType, amount, data, date);
                dialog.dismiss();
            }
        });

        cancelPurchaseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

        dialog.show();
    }

    /*******************************************************************************************************
     * database calls*/
    protected void addPurchase(int vehicleID, String purchaseType, String amount, String data, String date){
        GlobalGasTracker globalData = (GlobalGasTracker) getApplication();
        String username = globalData.getUsername();

        HashMap<String, String> params = new HashMap<String, String>();
        params.put("type", "purchase");
        params.put("username", username);
        params.put("vehicleid", Integer.toString(vehicleID));
        params.put("purchasetype", purchaseType);
        params.put("amountspent", amount);
        if(purchaseType.equals("Gas"))
            params.put("purchasedata", data);
        params.put("dateofpurchase", date);

        MakeRequest(Request.Method.POST, params,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String r) {
                        Log.d("Volley Log", r);

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

    protected void getPurchaseList(ArrayList<Bundle> extractedVehicles){
        GlobalGasTracker globalData = (GlobalGasTracker) getApplication();
        String username = globalData.getUsername();

        HashMap<String, String> params = new HashMap<String, String>();
        params.put("type", "purchase");
        params.put("username", username);

        MakeRequest(Request.Method.GET, params,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String r) {

                        ArrayList<Bundle> extractedData = HandleReceivedData(getApplicationContext(), r);
                        MainFunctions(extractedData, extractedVehicles);
                    }
                });
    }

    protected void getVehicleList(){
        Log.d("asdklasjdklajs", "IN VEHICLE LIST");
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
                        getPurchaseList(extractedData);
                        return;
                    }
                });
    }


    /*******************************************************************************************************
     * see all purchases button*/
    protected void seeAllPurchasesButtonHandler(){
        Intent intent = new Intent(getApplicationContext(), PurchaseListActivity.class);
        startActivity(intent);
    }

    protected void seeAllPurchasesVehicleButtonHandler(int vehicleID){
        Intent intent = new Intent(getApplicationContext(), PurchaseListActivity.class);
        intent.putExtra("currentVehicle", vehicleDropDown.getEditText().getText().toString());
        intent.putExtra("currentVehicleID", vehicleID);
        startActivity(intent);
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

    @Override
    public void onBackPressed() {}//disables back android button

    /*******************************************************************************************************
     * Shared prefs*/
    private void rememberVehicle(){
        sharedPreferences = getSharedPreferences("saveCurrentVehicle", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt("currentVehicle", currentListPosition);
        editor.commit();
    }
    protected void getSavedValue(){
        sharedPreferences = getSharedPreferences("saveCurrentVehicle", Context.MODE_PRIVATE);
        currentListPosition = sharedPreferences.getInt("currentVehicle", 0);
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