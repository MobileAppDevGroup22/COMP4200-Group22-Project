package group22.gastracker;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;

import group22.gastracker.purchases.Purchase;

public class VehicleListAdapter extends ArrayAdapter<String> {

    private static final String TAG = "VehicleListAdapter";
    private Context mContext;
    private int mResource;

    public VehicleListAdapter(@NonNull Context context, int resource, @NonNull ArrayList<String> objects) {
        super(context, resource, objects);
        mContext = context;
        mResource = resource;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        String vehicleName = getItem(position);

        LayoutInflater inflater = LayoutInflater.from(mContext);
        convertView = inflater.inflate(mResource, parent, false);

        TextView vehicleDisplay = convertView.findViewById(R.id.textView_VehicleName);
        vehicleDisplay.setText(vehicleName);
        //vehicleDisplay.setTextColor(com.google.android.material.R.attr.dividerColor);
        return convertView;
    }

}
