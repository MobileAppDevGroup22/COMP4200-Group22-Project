package group22.gastracker;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.Map;

public class Utility {

    public static final int DEFAULT_INT = -214748364;
    public static final double DEFAULT_DOUBLE= -21.2345373;
    public static final ArrayList<String> INT_KEYS = new ArrayList<String>(Arrays.asList("vehicleid", "purchaseid", "statid", "status"));
    public static final ArrayList<String> DOUBLE_KEYS = new ArrayList<String>(Arrays.asList("purchasedata", "statvalue", "amountspent"));
    /*
    *   TEMPLATE CODE FOR MAKING REQUESTS TO DATABASE - Make sure MakeRequest() is called within
    *                                                   a class that extends GlobalActivity
    * */
    /*
        HashMap<String, String> params = new HashMap<String, String>();
		params.put("type", ""); //always remember what type of request you're making - see reference sheet
        //put values into params

        MakeRequest(Request.Method.POST, params,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String r) {
                        //Log.d("Volley Log", r);

                        ArrayList<Bundle> extractedData = HandleReceivedData(getApplicationContext(), r);
                        if (extractedData == null) return;

                        for (Bundle u:extractedData){
                            Log.d("Bundle Array", u.toString());
                        }
                        //saved extracted data somewhere
                    }
                });
    */

    //parses given string and returns arraylist of bundles with data
    public static ArrayList<Bundle> StringToBundleArray(String jsonObjectAsString){
        Log.d("Testing", jsonObjectAsString);
        try {
            ArrayList<Bundle> allData = new ArrayList<Bundle>();
            JSONArray jsonArray = new JSONArray(jsonObjectAsString);
            for (int i = 0; i < jsonArray.length(); i++){
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                Bundle bundle = new Bundle();
                Iterator<String> iterator = jsonObject.keys();

                while(iterator.hasNext()){
                    String key = (String)iterator.next();
                    String value = jsonObject.getString(key);

                    if (INT_KEYS.contains(key)){ bundle.putInt(key, StringToInt(value, DEFAULT_INT)); }
                    else if (DOUBLE_KEYS.contains(key)){ bundle.putDouble(key, StringToDouble(value, DEFAULT_DOUBLE)); }
                    else{ bundle.putString(key, value); }
                }
                allData.add(bundle);
            }
            return allData;
        } catch (Exception e) { return null; }
    }

    public static Bundle StringToBundle(String jsonObjectAsString){
        Log.d("Testing", jsonObjectAsString);
        try {
            JSONObject jsonObject = new JSONObject(jsonObjectAsString);
            Bundle bundle = new Bundle();
            Iterator<String> iterator = jsonObject.keys();

            while(iterator.hasNext()){
                String key = (String)iterator.next();
                String value = jsonObject.getString(key);

                if (INT_KEYS.contains(key)){ bundle.putInt(key, StringToInt(value, DEFAULT_INT)); }
                else if (DOUBLE_KEYS.contains(key)){ bundle.putDouble(key, StringToDouble(value, DEFAULT_DOUBLE)); }
                else{ bundle.putString(key, value); }
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
    public static ArrayList<Bundle> HandleReceivedData(Context cxt, String s){
        try{
            //Log.d("Volley Log", s);
            Bundle response = Utility.StringToBundle(s);
            if (response == null) return null;
            if (response.getInt("status", 400) >= 400){ //400+ are error codes, don't change this
                Utility.Toast(cxt, response.getString("data", "Unknown error"));
                return null;
            }

            //successful query
            if (response.getInt("status", 0) == 200){
                return Utility.StringToBundleArray(response.getString("data", "none"));
            }

            return null;
        } catch (Exception e){ return null; }
    }

    public static String AttachParamsToUrl(String url, Map<String, String> params){
        int paramCount = 0;
        for (Map.Entry<String, String> entry : params.entrySet()){
            if (paramCount == 0) url += "?";
            else url += "&";
            paramCount++;
            url += entry.getKey() + "=" + entry.getValue();
        }
        return url;
    }

    //prints given error into logcat
    public static void VolleyPrintError(String s){ Log.d("Volley Error Printer", s); }

    //shows given string as toast on screen
    public static void Toast(Context cxt, String s){
        Toast.makeText(cxt, s, Toast.LENGTH_LONG).show();
        Log.d("Toast Error", s);
    }
}
