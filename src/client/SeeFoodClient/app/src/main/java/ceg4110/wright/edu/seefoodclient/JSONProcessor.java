package ceg4110.wright.edu.seefoodclient;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.widget.ImageView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;

/**
 * Created by DJ on 11/27/2017.
 * A JSONProcessor object must be instantiated for each image processed.
 * This will allow the image to be passed to it separately from the JSON data.
 */

class JSONProcessor {

    private Drawable imageFile;
    private Drawable[] layers;
    private Drawable foodYes;
    private Drawable foodNo;
    Context context;

    JSONProcessor(Drawable file, Context newContext){
        super();
        imageFile = file;
        context = newContext;
        foodYes = getResources().getDrawable( R.drawable.yes_food );
        foodNo = getResources().getDrawable( R.drawable.no_food );
    }

    ImageView processJSONData(JSONObject input) throws JSONException {
        String fileName = input.getString("filename");
        Double imageScore = input.getDouble("file_score");
        Boolean foodBoolean = input.getBoolean("food_boolean");

        layers[0] = (Drawable)imageFile;
        if (foodBoolean){
            layers[1] = foodYes;
        } else if (!foodBoolean){
            layers[1] = foodNo;
        }

        Drawable layersDrawable = new LayerDrawable(layers);

    }


}
