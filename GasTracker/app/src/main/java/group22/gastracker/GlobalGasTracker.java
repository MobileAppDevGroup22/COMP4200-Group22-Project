package group22.gastracker;

import android.app.Application;

import java.util.ArrayList;

public class GlobalGasTracker extends Application {

    private String username;
    private ArrayList<String> vehicleList;

    public String getUsername() {
        return username;
    }
    public void setUsername(String username) {
        this.username = username;
    }

    public ArrayList<String> getVehicleList() {
        return vehicleList;
    }

    public void setVehicleList(ArrayList<String> vehicleList) {
        this.vehicleList = vehicleList;
    }
    public void addVehicle(String vehicle) {
        vehicleList.add(vehicle);
    }
}
