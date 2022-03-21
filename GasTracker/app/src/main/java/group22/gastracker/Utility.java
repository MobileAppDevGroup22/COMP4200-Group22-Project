package group22.gastracker;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import org.json.JSONObject;

import java.util.Iterator;

public class Utility {

    public static final int DEFAULT_INT = -214748364;
    public static final double DEFAULT_DOUBLE= -21.2345373;

    /*
    *   TEMPLATE CODE FOR MAKING REQUESTS TO DATABASE - Make sure MakeRequest() is called within
    *                                                   a class that extends GlobalActivity
    * */
    /*
        HashMap<String, String> params = new HashMap<String, String>();
        //put values into params

        MakeRequest(Request.Method.POST, params,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String r) {
                        //Log.d("Volley Log", r);

                        Bundle extractedData = HandleReceivedData(getApplicationContext(), r);
                        //saved extracted data somewhere
                    }
                });
    */

    //parses given string and
    public static Bundle StringToBundle(String jsonObjectAsString){
        try {
            JSONObject jsonObject = new JSONObject(jsonObjectAsString);
            Bundle bundle = new Bundle();
            Iterator<String> iterator = jsonObject.keys();
            while(iterator.hasNext()){
                String key = (String)iterator.next();
                String value = jsonObject.getString(key);

                int tmp_int = StringToInt(value, DEFAULT_INT);
                if (tmp_int != DEFAULT_INT){
                    bundle.putInt(key, tmp_int);
                    continue;
                }

                double tmp_double = StringToDouble(value, DEFAULT_DOUBLE);
                if (tmp_double != DEFAULT_DOUBLE){
                    bundle.putDouble(key, tmp_double);
                    continue;
                }

                bundle.putString(key, value);
            }
            return bundle;
        } catch (Exception e) { return null; }
    }

    //used like this: (-1 can be whatever default value you'd like)
    // int value = StringToInt(someString, -1);
    public static int StringToInt(String str, int defaultValue){
        try{ return Integer.parseInt(str); }
        catch(Exception e){ return defaultValue; }
    }

    //used like this: (-1.0 can be whatever default value you'd like)
    // double value = StringToDouble(someString, -1.0);
    public static double StringToDouble(String str, double defaultValue){
        try{ return Double.parseDouble(str); }
        catch(Exception e){ return defaultValue; }
    }

    /*
    * Given a response string, returns the data associated within it
    * or returns null if error occurred. Generally displays toast with error
    * when that happens.
    * */
    public static Bundle HandleReceivedData(Context cxt, String s){
        //Log.d("Volley Log", s);
        Bundle response = Utility.StringToBundle(s);
        if (response == null) return null;
        if (response.getInt("status", 400) >= 400){ //400+ are error codes, don't change this
            Utility.Toast(cxt, response.getString("data", "Unknown error"));
            return null;
        }

        //successful query
        if (response.getString("description", "none").equals("Success")){
            return Utility.StringToBundle(response.getString("data", "none"));
        }

        return null;
    }

    //prints given error into logcat
    public static void VolleyPrintError(String s){ Log.d("Volley Error Printer", s); }

    //shows given string as toast on screen
    public static void Toast(Context cxt, String s){
        Toast.makeText(cxt, s, Toast.LENGTH_LONG).show();
        Log.d("Toast Error", s);
    }
}