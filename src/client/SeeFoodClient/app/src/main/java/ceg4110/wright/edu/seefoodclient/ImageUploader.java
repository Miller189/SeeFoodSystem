package ceg4110.wright.edu.seefoodclient;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.Base64;

import com.android.volley.Cache;
import com.android.volley.Network;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.BasicNetwork;
import com.android.volley.toolbox.DiskBasedCache;
import com.android.volley.toolbox.HurlStack;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

/**
 * Created by DJ on 11/18/2017.
 */

public class ImageUploader {

    Context context;
    String url = "http://34.237.62.217/evaluation";

    public ImageUploader(File file){
        super();
    }


//     public JSONObject uploadImage(Bitmap imageFile) throws IOException, JSONException{
//
//         URL urlObject = new URL(this.url);
//
//         String filenameString;
//
//         Cache cache = new DiskBasedCache(context.getCacheDir(), 1024 * 1024); // 1MB cap
//         Network network = new BasicNetwork(new HurlStack());
//         RequestQueue queue = new RequestQueue(cache, network);
//         queue.start();
//
//         String encodedImage = getStringFromBitmap(imageFile);
//         JSONObject jsonObj = new JSONObject("{\"image\":\" + encodedImage + \"}");
//
//         JsonObjectRequest jsObjRequest = new JsonObjectRequest
//                 (Request.Method.GET, url, jsonObj, new Response.Listener<JSONObject>() {
//                     @Override
//                     public void onResponse(JSONObject response) {
//
//                         try {
//                             String fileName = response.getString("filename");
//                             Double imageScore = response.getDouble("file_score");
//                             Boolean foodBoolean = response.getBoolean("food_boolean");//
//                         } catch (JSONException e) {
//                             e.printStackTrace();
//                         }
//                     }
//                 }, new Response.ErrorListener() {
//
//                     @Override
//                     public void onErrorResponse(VolleyError error) {
//                         // TODO Auto-generated method stub
//
//                     }
//                 });
//
//         return result;
//    }

    // This converts the file object to its Base64 representation
    // to facilitate JSON conversion.
    private String base64File(File file)throws FileNotFoundException{
        InputStream inputStream = new FileInputStream(file);
        byte[] bytes;
        byte[] buffer = new byte[8192];
        int bytesRead;
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        try {
            while ((bytesRead = inputStream.read(buffer)) != -1) {
                output.write(buffer, 0, bytesRead);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        bytes = output.toByteArray();
        return Base64.encodeToString(bytes, Base64.DEFAULT);
    }


}
