package group22.gastracker.purchases;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

import group22.gastracker.R;

public class PurchaseListActivity extends AppCompatActivity {

    ArrayList<Purchase> arrayList_Purchases = new ArrayList<>();
    PurchaseListAdapter purchaseListAdapter;

    TextView purchasesTitle;
    TextView noPurchasesText;
    ListView purchaseListView;

    int currentTheme = 0;
    boolean darkMode = false;
    SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSavedValue();
        updateTheme();
        setContentView(R.layout.activity_purchase_list);
        getSupportActionBar().hide();
        purchasesTitle = findViewById(R.id.textView_PuchasesTitle);
        noPurchasesText = findViewById(R.id.textView_NoPurchases);
        purchaseListView = findViewById(R.id.listView_PurchaseList);

        Bundle passedValues = getIntent().getExtras();
        String currentVehicle = passedValues.getString("CurrentVehicle");
        purchasesTitle.setText("Purchases - " + currentVehicle);

        /********
         * Add code to populate list of purchases from database
         */
        arrayList_Purchases.add(new Purchase("2022-03-01", "Gas", 43.17));
        arrayList_Purchases.add(new Purchase("2022-03-05", "Insurance", 102.60));
        arrayList_Purchases.add(new Purchase("2022-03-06", "Gas", 24.53));
        arrayList_Purchases.add(new Purchase("2022-03-08", "Gas", 62.89));
        arrayList_Purchases.add(new Purchase("2022-03-13", "Repair", 506.36));
        arrayList_Purchases.add(new Purchase("2022-03-17", "Gas", 12.36));
        arrayList_Purchases.add(new Purchase("2022-03-01", "Gas", 43.17));
        arrayList_Purchases.add(new Purchase("2022-03-05", "Insurance", 102.60));
        arrayList_Purchases.add(new Purchase("2022-03-06", "Gas", 24.53));
        arrayList_Purchases.add(new Purchase("2022-03-08", "Gas", 62.89));
        arrayList_Purchases.add(new Purchase("2022-03-13", "Repair", 506.36));
        arrayList_Purchases.add(new Purchase("2022-03-17", "Gas", 12.36));
        arrayList_Purchases.add(new Purchase("2022-03-01", "Gas", 43.17));
        arrayList_Purchases.add(new Purchase("2022-03-05", "Insurance", 102.60));
        arrayList_Purchases.add(new Purchase("2022-03-06", "Gas", 24.53));
        arrayList_Purchases.add(new Purchase("2022-03-08", "Gas", 62.89));
        arrayList_Purchases.add(new Purchase("2022-03-13", "Repair", 506.36));
        arrayList_Purchases.add(new Purchase("2022-03-17", "Gas", 12.36));
        arrayList_Purchases.add(new Purchase("2022-03-01", "Gas", 43.17));
        arrayList_Purchases.add(new Purchase("2022-03-05", "Insurance", 102.60));
        arrayList_Purchases.add(new Purchase("2022-03-06", "Gas", 24.53));
        arrayList_Purchases.add(new Purchase("2022-03-08", "Gas", 62.89));
        arrayList_Purchases.add(new Purchase("2022-03-13", "Repair", 506.36));
        arrayList_Purchases.add(new Purchase("2022-03-17", "Gas", 12.36));

        if(arrayList_Purchases.isEmpty()){
            noPurchasesText.setVisibility(View.VISIBLE);
        }else{
            purchaseListAdapter = new PurchaseListAdapter(getApplicationContext(), R.layout.list_adapter_layout, arrayList_Purchases);
            purchaseListView.setAdapter(purchaseListAdapter);
        }
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