package group22.gastracker;

import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

import group22.gastracker.databasing.RequestHandler;

/**
 * Global activity that every activity class should instantiate.
 * Will keep an instance of the request queue for http requests
 */
public class GlobalActivity extends AppCompatActivity {

    RequestHandler requestHandler;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //get request handler singleton
        requestHandler = RequestHandler.Instance(this.getApplicationContext());
    }

    //Functions for all activities to be able to access

    //Adds request to request queue
    protected <T> void AddRequest(Request<T> req){
        requestHandler.addRequest(req);
    }

    //Adds request to request queue after applying specified tag
    protected <T> void AddRequest(Request<T> req, String TAG){
        req.setTag(TAG);
        requestHandler.addRequest(req);
    }

    // function to streamline database communication
    // pass in request type (e.g. Request.Method.POST), params as JSON object, response listener and error listener
    protected void MakeRequest(int method, Map<String, String> params, Response.Listener<String> responseListener){
        String url = RequestHandler.url;
        if (method == Request.Method.GET) url = Utility.AttachParamsToUrl(url, params);
        /*
        String LogOutput = "";

        LogOutput += "Params: " + params.toString() + "\n";
        LogOutput += "Url: " + url;

        Log.d("Make Request", LogOutput);
        */
        requestHandler.addRequest( new StringRequest(method, url, responseListener,
                            new Response.ErrorListener() {
                                @Override
                                public void onErrorResponse(VolleyError error) {
                                    error.printStackTrace();
                                    Log.d("Volley error", params.toString());
                                }
                            }) {    @Override
                                    public Map<String, String> getHeaders() throws AuthFailureError {
                                        HashMap<String, String> headers = new HashMap<String, String>();
                                        if (method == Method.POST){
                                            //headers.put("Content-Type", "application/form-data; charset=utf-8");

                                        }
                                        headers.put("User-Agent", "GasTracker/1.0");
                                        //headers.put("", "");
                                        return headers;
                                    }

                                   @Nullable
                                   @Override
                                   protected Map<String, String> getParams() throws AuthFailureError {
                                        Log.d("Params", params.toString());

                                        return params;
                                   }
                            }
        );
    }

    //cancels all request tasks of given tag
    protected void CancelAllOfTag(String tag){ requestHandler.cancelAllOfTag(tag); }

}
