package group22.gastracker;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class LoginActivity extends GlobalActivity {

    EditText usernameView, passwordView;
    Button loginButton, signUpButton;
    CheckBox rememberCheck;

    SharedPreferences sharedPreferences;
    String username, password;
    Boolean isRemember;

    boolean darkMode = false;
    int currentTheme = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSavedTheme();
        updateTheme();
        setContentView(R.layout.activity_login);
        getSupportActionBar().hide();
        usernameView = findViewById(R.id.editText_Username);
        passwordView = findViewById(R.id.editText_Password);
        rememberCheck = findViewById(R.id.checkBox_Remember);
        loginButton = findViewById(R.id.button_logIn);
        signUpButton = findViewById(R.id.button_signUp);
        getSavedValue();

        if(rememberCheck.isChecked()){
            loginButton.performClick();
            loginButton.setPressed(true);
            loginButton.invalidate();
            loginButton.setPressed(false);
            loginButton.invalidate();
        }

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                rememberValues();
            }
        });

        rememberCheck.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                if(!isChecked){
                    forgetValues();
                }
            }
        });

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                username = usernameView.getText().toString();
                password = passwordView.getText().toString();
                verifyUser(username, password);
            }
        });

        signUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                username = usernameView.getText().toString();
                password = passwordView.getText().toString();

                // TODO: Do some password verification

                //make hashmap of what to pass to server
                HashMap<String, String> params = new HashMap<String, String>();
                params.put("type", "user");
                params.put("username", username.trim());
                params.put("password", password);

                //make request (be careful of GET, POST or DELETE methods)
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

                            // TODO: save user somewhere. Can't save in variable because external variables aren't allowed
                            //       within this scope unless they're final. So call some function to save it or something

                        }
                    });


            }
        });

    }

    private void verifyUser(String username, String password){
        GlobalGasTracker globalData = (GlobalGasTracker) getApplication();
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("type", "verify_password");
        params.put("username", username.trim());
        params.put("password", password);
        MakeRequest(Request.Method.GET, params,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String r) {
                        Log.d("Volley Log", r);

                        ArrayList<Bundle> users = Utility.HandleReceivedData(getApplicationContext(), r);
                        if (users == null) return;

                        for (Bundle u:users){
                            Log.d("Bundle Array", u.toString());
                        }

                        for(Bundle currentUser : users){
                            Log.d("verifyUsers", currentUser.toString());
                            Log.d("verifyUsers", "user = " + currentUser.getString("username", null));
                            Log.d("verifyUsers", "username = " + currentUser.getString("username", null));
                            if(username.equals(currentUser.getString("username", null))){
                                globalData.setUsername(username);
                                globalData.setPassword(currentUser.getString("password", null));
                                rememberValues();
                                Intent main_intent = new Intent(LoginActivity.this, MainActivity.class);
                                startActivity(main_intent);
                            }
                        }
                    }
                });
    }

    private void forgetValues(){
        SharedPreferences.Editor sharedPrefEditor = sharedPreferences.edit();
        sharedPrefEditor.putString("username", null);
        sharedPrefEditor.putString("password", null);
        sharedPrefEditor.putBoolean("isRemember", false);
        sharedPrefEditor.commit();
    }

    private void rememberValues() {
        sharedPreferences = getSharedPreferences("saveLoginInfo", Context.MODE_PRIVATE);
        username = usernameView.getText().toString();
        password = passwordView.getText().toString();
        isRemember = rememberCheck.isChecked();

        SharedPreferences.Editor sharedPrefEditor = sharedPreferences.edit();

        if(isRemember){
            sharedPrefEditor.putString("username", username);
            sharedPrefEditor.putString("password", password);
            sharedPrefEditor.putBoolean("isRemember", isRemember);
            sharedPrefEditor.commit();
        }else{
            sharedPrefEditor.putString("username", null);
            sharedPrefEditor.putString("password", null);
            sharedPrefEditor.putBoolean("isRemember", false);
            sharedPrefEditor.commit();
        }

    }

    protected void getSavedValue(){
        sharedPreferences = getSharedPreferences("saveLoginInfo", Context.MODE_PRIVATE);

        username = sharedPreferences.getString("username", null);
        password = sharedPreferences.getString("password", null);
        isRemember = sharedPreferences.getBoolean("isRemember", false);

        usernameView.setText(username);
        passwordView.setText(password);
        rememberCheck.setChecked(isRemember);
        if(isRemember){
            verifyUser(username, password);
        }
    }

    protected void getSavedTheme(){
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