package ceg4110.wright.edu.seefoodclient;

import android.graphics.Bitmap;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

/**
 * Created by DJ on 11/18/2017.
 */

public class ImageUploader {

    // The first method (int(Bitmap)) deals with single-image functionality.
     public static JSONObject uploadImage(Bitmap imageFile) throws IOException, JSONException{

        URL url = new URL("http://34.237.62.217/evaluation");
        byte[] imageBits;
        JSONObject result;
        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();

        try {

            urlConnection.setDoOutput(true);
            urlConnection.setChunkedStreamingMode(0);

            OutputStream out = new BufferedOutputStream(urlConnection.getOutputStream());
            InputStream in = new BufferedInputStream(urlConnection.getInputStream());

            BufferedReader streamReader = new BufferedReader(new InputStreamReader(in, "UTF-8"));
            StringBuilder responseStrBuilder = new StringBuilder();
            imageBits = getByteArray(imageFile);

            out.write(imageBits);

            String inputStr;
            while ((inputStr = streamReader.readLine()) != null)
                responseStrBuilder.append(inputStr);
            result = new JSONObject(responseStrBuilder.toString());

        } finally {
            urlConnection.disconnect();
        }

        return result;
    }

    // The second method (JSONObject[](ArrayList)) deals with multiple image functionality.
    // Commented out until functionality is achieved with the first method
//    public static JSONObject[] uploadImage (ArrayList imageList)throws IOException{
//        URL url = new URL("http://34.237.62.217/evaluation");
//    }

    private static byte[] getByteArray (Bitmap image){
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        image.compress(Bitmap.CompressFormat.JPEG, 100, stream);
        return stream.toByteArray();
    }

}
