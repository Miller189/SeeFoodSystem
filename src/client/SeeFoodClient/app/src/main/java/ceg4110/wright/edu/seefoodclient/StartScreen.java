package ceg4110.wright.edu.seefoodclient;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.android.volley.Cache;
import com.android.volley.Network;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.BasicNetwork;
import com.android.volley.toolbox.DiskBasedCache;
import com.android.volley.toolbox.HurlStack;
import com.android.volley.toolbox.StringRequest;

import java.io.IOException;

/**
 *  Created by Don Miller
 *  This class appears on startup, and exists simply to mask an initial empty
 *  GET request which signals the server to anticipate an incoming  POST request.
 */

public class StartScreen extends AppCompatActivity{

    String url = "http://34.237.62.217/";
    Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start_screen);
        try {
            pingServer();
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    // Constructs a Volley request and hucks it at the server.
    // Upon response, waits 1 second and begins the MainActivity screen.
    public void pingServer()throws IOException, InterruptedException{
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        android.os.SystemClock.sleep(1000);
                        Log.e("Response:", response);  // Response string just in case
                        goToMain();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        AlertDialog.Builder messageBox = new AlertDialog.Builder(context);
                        messageBox.setTitle("Error");
                        messageBox.setMessage("SeeFood has errored and will now exit.");
                        messageBox.setCancelable(false);
                        messageBox.setNeutralButton("OK", null);
                        messageBox.show();
                        System.exit(0);
                    }
                });
        Cache cache = new DiskBasedCache(getCacheDir(), 1024 * 1024); // 1MB cap
        Network network = new BasicNetwork(new HurlStack());
        RequestQueue queue = new RequestQueue(cache, network);
        queue.start();

        queue.add(stringRequest);
    }

    // Automatically called when pingServer() is successful.
    public void goToMain(){
        Intent i = new Intent(getApplicationContext(),MainActivity.class);
        startActivity(i);
    }
}
