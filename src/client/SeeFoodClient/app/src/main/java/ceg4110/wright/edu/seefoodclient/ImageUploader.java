package ceg4110.wright.edu.seefoodclient;

import android.app.AlertDialog;
import android.content.Context;

import com.android.volley.Cache;
import com.android.volley.Network;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.BasicNetwork;
import com.android.volley.toolbox.DiskBasedCache;
import com.android.volley.toolbox.HurlStack;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;

/**
 * Created by DJ on 11/18/2017.
 */

class ImageUploader {

  ImageUploader() {
        super();
    }


     JSONObject uploadImage(final File imageFile, final Context context) throws IOException, JSONException{

         final String url;
         url = "http://34.237.62.217/evaluation";

         final JSONObject[] result;
         result = new JSONObject[1];

         Cache cache = new DiskBasedCache(context.getCacheDir(), 1024 * 1024); // 1MB cap
         Network network = new BasicNetwork(new HurlStack());
         RequestQueue queue = new RequestQueue(cache, network);
         queue.start();

         @SuppressWarnings("unchecked")
         Request jsObjRequest = new ImageUploadWithVolley(url, new Response.ErrorListener() {
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
             }, new Listener() {
             @Override
             public void onResponse(Object response) {
                 result[0] = (JSONObject)response;
             }

         }, imageFile);

         queue.add(jsObjRequest);

         return result[0];
    }




}
