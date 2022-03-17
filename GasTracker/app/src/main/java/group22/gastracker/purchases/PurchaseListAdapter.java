package group22.gastracker.purchases;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;

import group22.gastracker.R;

public class PurchaseListAdapter extends ArrayAdapter<Purchase> {

    private static final String TAG = "PurchaseListAdapter";
    private Context mContext;
    private int mResource;

    public PurchaseListAdapter(@NonNull Context context, int resource, @NonNull ArrayList<Purchase> objects) {
        super(context, resource, objects);
        mContext = context;
        mResource = resource;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        String date = getItem(position).getDate();
        String description = getItem(position).getDescription();
        double amount = getItem(position).getAmount();

        Purchase purchase = new Purchase(date, description, amount);

        LayoutInflater inflater = LayoutInflater.from(mContext);
        convertView = inflater.inflate(mResource, parent, false);

        TextView dateDisplay = convertView.findViewById(R.id.textView_Date);
        TextView descriptionDisplay = convertView.findViewById(R.id.textView_Description);
        TextView amountDisplay = convertView.findViewById(R.id.textView_Price);

        dateDisplay.setText(date);
        descriptionDisplay.setText(description);
        amountDisplay.setText(Double.toString(amount));

        return convertView;
    }
}
