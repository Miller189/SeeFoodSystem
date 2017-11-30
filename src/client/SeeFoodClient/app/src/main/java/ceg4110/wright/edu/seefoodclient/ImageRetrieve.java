package ceg4110.wright.edu.seefoodclient;

import java.util.ArrayList;
import java.util.HashMap;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ListView;
import java.net.URLConnection;
import java.net.URL;
import java.io.InputStream;
import java.io.IOException;

/**
 * Created by Ryan Zink
 */

public class ImageRetrieve extends AppCompatActivity{

    static JSONArray jarray = null;
    static String json = "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }


    private void establish_connection() {

        URLConnection connection = null;
        URL url = null;
        InputStream is = null;


        String mUrl = "IP ADDRESS AND PORT NUMBER";

        try {
            url = new URL(mUrl);

            connection = url.openConnection();
            connection.setRequestProperty("Accept", "SPECIFY ACCEPT TYPE");
            is = connection.getInputStream();


        } catch (Exception e) {
            // Log.i("Exception:" + mUrl, e.getMessage());
            e.printStackTrace();
        }

    }

    public void parse(InputStream is){

    }

}