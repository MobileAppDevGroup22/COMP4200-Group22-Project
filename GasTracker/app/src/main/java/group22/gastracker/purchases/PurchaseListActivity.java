package group22.gastracker.purchases;

import static group22.gastracker.Utility.HandleReceivedData;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;

import java.util.ArrayList;
import java.util.HashMap;

import group22.gastracker.GlobalActivity;
import group22.gastracker.GlobalGasTracker;
import group22.gastracker.MainActivity;
import group22.gastracker.R;
import group22.gastracker.Utility;

public class PurchaseListActivity extends GlobalActivity {

    ArrayList<Purchase> arrayList_Purchases = new ArrayList<>();
    PurchaseListAdapter purchaseListAdapter;

    TextView noPurchasesText;
    ListView purchaseListView;

    int currentVehicleID;
    String currentVehicle;

    int currentTheme = 0;
    boolean darkMode = false;
    SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSavedValue();
        updateTheme();
        setContentView(R.layout.activity_purchase_list);
        setTitle("Purchases");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        purchaseListView = findViewById(R.id.listView_PurchaseList);
        noPurchasesText = findViewById(R.id.textView_NoPurchases);
        Bundle passedValues = getIntent().getExtras();
        if(passedValues != null){
            currentVehicleID = passedValues.getInt("currentVehicleID");
            currentVehicle = passedValues.getString("currentVehicle");
            setTitle("Purchases - " + currentVehicle);
            noPurchasesText.setVisibility(View.VISIBLE);
            getVehiclePurchaseList();
        }else{
            getPurchaseList();
        }
    }

    protected void PurchaseListFunctions(ArrayList<Bundle> extractedData){

        for(Bundle currentDataBundle : extractedData){
            Log.d("getPurchasesDebug", String.valueOf(currentDataBundle));
            Purchase purchase = new Purchase();
            if(currentDataBundle.getString("purchasetype", "misc").equals("Gas")){
                purchase.setDescription(currentDataBundle.getString("purchasetype", "misc") + " at $" +
                        currentDataBundle.getDouble("purchasedata", 0));
            }else{
                purchase.setDescription(currentDataBundle.getString("purchasetype", "misc"));
            }
            purchase.setAmount(currentDataBundle.getDouble("amountspent", 0));
            purchase.setDate(currentDataBundle.getString("dateofpurchase", "0000-00-00"));
            arrayList_Purchases.add(purchase);
        }

        if(darkMode){
            purchaseListAdapter = new PurchaseListAdapter(getApplicationContext(), R.layout.list_adapter_dark_layout, arrayList_Purchases);
        }else{
            purchaseListAdapter = new PurchaseListAdapter(getApplicationContext(), R.layout.list_adapter_layout, arrayList_Purchases);
        }

        purchaseListView.setAdapter(purchaseListAdapter);

        purchaseListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int selectedItem, long l) {
                Bundle bundle = extractedData.get(selectedItem);
                int purchaseID = bundle.getInt("purchaseid", -1);
                deletePurchaseDialog(purchaseID);
                return false;
            }
        });
    }

    protected void getPurchaseList(){
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
                        if (extractedData == null){
                            noPurchasesText.setVisibility(View.VISIBLE);
                            return;
                        }
                        noPurchasesText.setVisibility(View.INVISIBLE);
                        PurchaseListFunctions(extractedData);
                    }
                });
    }

    protected void getVehiclePurchaseList(){
        GlobalGasTracker globalData = (GlobalGasTracker) getApplication();
        String username = globalData.getUsername();

        HashMap<String, String> params = new HashMap<String, String>();
        params.put("type", "purchase");
        params.put("vehicleid", Integer.toString(currentVehicleID));


        MakeRequest(Request.Method.GET, params,
            new Response.Listener<String>() {
                @Override
                public void onResponse(String r) {

                    ArrayList<Bundle> extractedData = HandleReceivedData(getApplicationContext(), r);
                    if (extractedData == null){
                        noPurchasesText.setVisibility(View.VISIBLE);
                        return;
                    }
                    noPurchasesText.setVisibility(View.INVISIBLE);
                    PurchaseListFunctions(extractedData);
                }
            });
    }

    protected void deletePurchase(int purchaseID){
        GlobalGasTracker globalData = (GlobalGasTracker) getApplication();
        String username = globalData.getUsername();
        String password = globalData.getPassword();

        HashMap<String, String> params = new HashMap<String, String>();
        params.put("type", "purchase");
        params.put("purchaseid", Integer.toString(purchaseID));
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

    protected void deletePurchaseDialog(int purchaseID){
        AlertDialog.Builder deleteDialog = new AlertDialog.Builder(PurchaseListActivity.this);
        deleteDialog.setTitle("Delete Purchase");
        deleteDialog.setMessage("Do you want to delete this Purchase?");
        deleteDialog.setCancelable(false);
        deleteDialog.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });
        deleteDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                deletePurchase(purchaseID);
            }
        });
        AlertDialog alert = deleteDialog.create();
        alert.show();
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(intent);
    }

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