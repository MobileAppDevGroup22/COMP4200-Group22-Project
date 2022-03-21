package group22.gastracker.databasing;

import android.content.Context;

import com.android.volley.*;
import com.android.volley.toolbox.Volley;

/**
 * Request Handler class - manages all http database requests.
 */
public class RequestHandler {
    private static RequestHandler instance;
    private static Context context;

    public RequestQueue requestQueue;

    //web link to php script
    public static final String url = "https://darji1.myweb.cs.uwindsor.ca/GasTracker/index.php";

    public RequestHandler(Context cxt) {
        context = cxt;
        requestQueue = getRequestQueue();
    }

    //Singleton instance getter
    public static synchronized RequestHandler Instance(Context context) {
        if (instance == null) { instance = new RequestHandler(context.getApplicationContext()); }
        return instance;
    }

    //gets current request queue
    public RequestQueue getRequestQueue() {
        if (requestQueue == null) { requestQueue = Volley.newRequestQueue(context.getApplicationContext()); }
        return requestQueue;
    }

    //overloaded function to get request queue given context
    public static synchronized RequestQueue getRequestQueue(Context cxt){
        return RequestHandler.Instance(cxt).getRequestQueue();
    }

    //adds request to queue
    public <T> void addRequest(Request<T> req) {
        getRequestQueue().add(req);
    }

    //overloaded function to add request to queue while also given context
    public static synchronized <T> void addRequest(Request<T> req, Context cxt) {
        RequestHandler.Instance(cxt).getRequestQueue().add(req);
    }

    //cancels all request tasks of given tag
    public void cancelAllOfTag(String tag){
        if (requestQueue != null){
            requestQueue.cancelAll(tag);
        }
    }

}
