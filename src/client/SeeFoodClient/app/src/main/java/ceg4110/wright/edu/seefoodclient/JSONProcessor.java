package ceg4110.wright.edu.seefoodclient;

import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.widget.ImageView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Don Miller on 11/27/2017.
 * This class does the work of adding the server's data to the image file.
 * A JSONProcessor object must be instantiated for each image processed.
 * This will allow the image to be passed to it separately from the JSON data.
 */

class JSONProcessor {

    private BitmapDrawable imageFile;
    private BitmapDrawable foodYes;
    private BitmapDrawable foodNo;
    private Context context;

    JSONProcessor(Drawable file, Context newContext){
        super();
        imageFile = (BitmapDrawable) file;
        context = newContext;
        foodYes = (BitmapDrawable) context.getResources().getDrawable( R.drawable.yes_food );
        foodNo = (BitmapDrawable) context.getResources().getDrawable( R.drawable.no_food );
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    ImageView processJSONData(JSONArray input) throws JSONException {

        // layer 0: original image | layer 1: boolean | layer 2: certainty/food score
        Drawable[] layers = new Drawable[3];
        layers[0] = imageFile;

        // Extract data from server response object
        JSONArray secondArray = input.getJSONArray(0);
        JSONObject obj = secondArray.getJSONObject(0);
        String fileName = obj.optString("file_name");
        Double imageScore = obj.optDouble("file_score");
        Boolean foodBoolean = obj.optBoolean("food_boolean");

        // Create drawable layer for "food" or "not food"
        if (foodBoolean){
            layers[1] = foodYes;
        } else {
            layers[1] = foodNo;
        }

        // Create and implement certainty drawable layer
        int ct = (int) Math.round(imageScore);
        String cs = Integer.toString(ct);
        String certainty = "Certainty: " + cs + "%";
        Drawable cert = new TextDrawable(certainty);
        layers[2] = cert;

        LayerDrawable ld = new LayerDrawable(layers);

        // Position the evaluated image file
        ld.setLayerWidth(0, 1000);
        ld.setLayerInsetBottom(0, 88);

        // Position the boolean
        ld.setLayerWidth(1, 1000);
        ld.setLayerInsetTop(1, 650);

        // Position the certainty score
        ld.setLayerWidth(2, 1000);
        ld.setLayerHeight(2,200);
        ld.setLayerInsetLeft(2, 100);
        ld.setLayerInsetTop(2, 400);

        ImageView imageView = new ImageView(context);
        imageView.setImageDrawable(ld);
        return imageView;
    }
}
