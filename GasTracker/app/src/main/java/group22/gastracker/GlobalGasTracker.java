package group22.gastracker;

import android.app.Application;

import java.util.ArrayList;

public class GlobalGasTracker extends Application {

    private String username;
    String password;

    public String getUsername() {
        return username;
    }
    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
