package ceg4110.wright.edu.seefoodclient;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

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
import com.android.volley.toolbox.Volley;

public class StartScreen extends AppCompatActivity {

    String url = "http://34.237.62.217/";

    StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
            new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    goToMain();
                }
            },
            new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    // Handle error
                }
            });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start_screen);

        Cache cache = new DiskBasedCache(getCacheDir(), 1024 * 1024); // 1MB cap

        Network network = new BasicNetwork(new HurlStack());

        RequestQueue queue = new RequestQueue(cache, network);




        // Add the request to the RequestQueue.
        queue.add(stringRequest);

    }

    public void goToMain(View v){
        Intent i = new Intent(getApplicationContext(),MainActivity.class);
        startActivity(i);
    }

    public void goToMain(){
        Intent i = new Intent(getApplicationContext(),MainActivity.class);
        startActivity(i);
    }
}
