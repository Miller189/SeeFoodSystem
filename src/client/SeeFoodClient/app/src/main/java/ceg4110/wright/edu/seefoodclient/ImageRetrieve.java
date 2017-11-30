package ceg4110.wright.edu.seefoodclient;

import java.util.ArrayList;
import java.util.HashMap;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.util.Log;
import android.widget.ListView;

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
import android.widget.ListAdapter;
import android.widget.ImageView;
import android.widget.GridView;

import java.net.URLConnection;
import java.net.URL;
import java.io.InputStream;
import java.io.IOException;

import org.json.JSONException;
import org.json.JSONObject;


public class ImageRetrieve extends AppCompatActivity {


    Context context;
    ListAdapter adapter;
    JSONArray output = null;
    private Bitmap[] mThumbIds = null;//image
    private String[] score = null;//file_score
    private String[] isFood = null;//foodBoolean
    private Integer[]  imId = null;//foodBoolean
    
    static final int NUMBER_OF_ITEMS = 8;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = getApplicationContext();

        setContentView(R.layout.gallery);
        //ViewPager p = (ViewPager) findViewById(R.id.grid);
        GridView grid = (GridView) findViewById(R.id.grid);
        grid.setAdapter(new ImageAdapterGallery(this));


        //grid.setAdapter(new ImageAdapter());


        Log.e("Gallery:", "Load");
        try {
            pingServer();
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }

    }

    public void pingServer() throws IOException, InterruptedException {

        String uri = String.format("http://34.237.62.217/gallery?limit=%1$s",
                NUMBER_OF_ITEMS);
        Log.e("url For Get:", uri);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, uri,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        JSONObject obj = null;
                        android.os.SystemClock.sleep(1000);
                        Log.e("Response For Get:", response.toString());  // Response string
                        onPostExecute(response.toString());
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
        ;
    }


    private void processJSONDataGet(JSONObject input) {

    }

    // Automatically called when pingServer() is successful.
    public void goToMain() {
        Intent i = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(i);
    }

    public void onPostExecute(String s) {
        try {
            Log.e("String", s);
            output = new JSONArray(s);
            process(output);
        } catch (JSONException e) {
            e.printStackTrace();
        }


    }


    public void process(JSONArray output) {
        //Drawable imageDrawable = Drawable.createFromPath(imageFile.getAbsolutePath());
        //processor = new JSONProcessor(imageDrawable, context);
        try {
            processJSONData(output);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void processJSONData(JSONArray input) throws JSONException {
        int index = input.length();
        Bitmap image = null;
        Log.e("Response For Get:", "" + index);
        for (int i = 0; i < input.length() - 1; i++) {


            JSONArray secondArray = input.getJSONArray(i);
            JSONObject obj = secondArray.getJSONObject(0);
            // Drawable layers = new Drawable();

            try {
                String data = obj.getString("data");
                Integer file_ID = obj.getInt("file_ID");
                String file_name = obj.getString("file_name");
                Double file_score = obj.getDouble("file_score");
                Integer foodBoolean = obj.getInt("food_boolean");
                image = decodeToBase64(data);
                imId[i] = file_ID;//file_ID
                score[i] = file_score.toString();//file_score
                mThumbIds[i] =image ;//image
                isFood[i] = foodBoolean.toString();//food_boolean

                //Image(image,fileId,name,score,isfood)
                Log.e("Gallery:", file_ID.toString());
            } catch (JSONException e) {
                e.printStackTrace();
            }
            //ImageView imageView = new ImageView(context);
            //imageFile

            //Drawable layersDrawable = new LayerDrawable(layers);
            //imageView.setImageDrawable(layersDrawable);
        }

    }

    //from https://stackoverflow.com/questions/37158059/selecting-an-image-from-gallery-and-to-save-it-in-android-app
    public  Bitmap decodeToBase64(String input) {
        byte[] decode = Base64.decode(input, 0);
        System.out.println("MADE IT INTO decodeToBase64");
        Bitmap map = BitmapFactory.decodeByteArray(decode, 0, decode.length);
        return map;
    }
    public Integer[] getIDArray(){
        return imId;
    }
    public Bitmap[] getMThumbIds(){
        return mThumbIds;
    }
    public String[] getScore(){
        return score;
    }
    public String[] getisFood(){
        return isFood;
    }


}


