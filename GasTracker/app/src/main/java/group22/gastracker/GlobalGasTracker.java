package group22.gastracker;

import android.app.Application;

public class GlobalGasTracker extends Application {

    private String username;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
