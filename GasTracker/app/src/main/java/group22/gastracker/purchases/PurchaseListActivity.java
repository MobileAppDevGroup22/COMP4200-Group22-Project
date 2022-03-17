package group22.gastracker.purchases;

import androidx.appcompat.app.AppCompatActivity;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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
}