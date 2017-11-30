package ceg4110.wright.edu.seefoodclient;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.widget.ImageView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by DJ on 11/27/2017.
 * This class does the work of adding the server's data to the image file.
 * A JSONProcessor object must be instantiated for each image processed.
 * This will allow the image to be passed to it separately from the JSON data.
 */

class JSONProcessor {

    private Drawable imageFile;

    private Drawable foodYes;
    private Drawable foodNo;
    private Context context;

    JSONProcessor(Drawable file, Context newContext){
        super();
        imageFile = file;
        context = newContext;
        foodYes = context.getResources().getDrawable( R.drawable.yes_food );
        foodNo = context.getResources().getDrawable( R.drawable.no_food );
    }

    ImageView processJSONData(JSONArray input) throws JSONException {
        JSONArray secondArray = input.getJSONArray(0);
        JSONObject obj = secondArray.getJSONObject(0);
        Drawable[] layers = new Drawable[2];
        String fileName = obj.optString("file_name");
        Double imageScore = obj.optDouble("file_score");
        Boolean foodBoolean = obj.optBoolean("food_boolean");

        ImageView imageView = new ImageView(context);
        layers[0] = imageFile;

        if (foodBoolean){
            layers[1] = foodYes;
        } else {
            layers[1] = foodNo;
        }

        Drawable layersDrawable = new LayerDrawable(layers);
        imageView.setImageDrawable(layersDrawable);
        return imageView;
    }
}
