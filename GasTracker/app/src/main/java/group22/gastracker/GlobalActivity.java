package group22.gastracker;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;

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

    //cancels all request tasks of given tag
    protected void CancelAllOfTag(String tag){
        requestHandler.cancelAllOfTag(tag);
    }

}
