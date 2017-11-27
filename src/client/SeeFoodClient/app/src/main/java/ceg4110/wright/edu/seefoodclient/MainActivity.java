package ceg4110.wright.edu.seefoodclient;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.content.FileProvider;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;

import android.widget.ImageView;
import android.widget.Spinner;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    ImageAdapter adapter;
    ImageUploader uploader;
    static final int REQUEST_TAKE_PHOTO = 1;
    String mCurrentPhotoPath;
    Context context;
    int pagerSize;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = getApplicationContext();
        setContentView(R.layout.activity_main);
        adapter = new ImageAdapter();


        ViewPager pager = (ViewPager)findViewById(R.id.viewPager);
        pager.setAdapter(adapter);

        // The process for inserting an image into the ViewPager:
        // 1. Instantiate the ImageView
        ImageView startImage = new ImageView(this);
        // 2. Convert image to Drawable object
        Drawable myIcon = getResources().getDrawable( R.drawable.ic_launcher );
        // 3. call setImageDrawable on the ImageView
        startImage.setImageDrawable(myIcon);
        // 4. Add the view using the adapter's method
        pagerSize = adapter.addView(startImage);

        // It might be worth looking into accessing the ImageUploader statically
        // I just did it this way to make it easier on myself
        uploader = new ImageUploader();

        Spinner dropdown = (Spinner)findViewById(R.id.spinner);

        dropdown.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                switch(position){
                    case 0: break;
                    // Case 1 should implement "browse server gallery"
                    case 1: break;
                    case 2: System.exit(0);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }

    public void dispatchTakePictureIntent(View v) {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        // Ensure that there's a camera activity to handle the intent
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {

            // Create the File where the photo should go
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                errorMessage("IOException", ex.getMessage());
            }
            // Continue only if the File was successfully created
            if (photoFile != null) {
                Uri photoURI = FileProvider.getUriForFile(context,
                        "ceg4110.wright.edu.seefoodclient.android.fileprovider",
                        photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(takePictureIntent, REQUEST_TAKE_PHOTO);
            }
        }

        // Upload file, get JSONObject
        File imageFile = new File(mCurrentPhotoPath);
        Drawable imageDrawable = Drawable.createFromPath(mCurrentPhotoPath);
        JSONProcessor processor = new JSONProcessor(imageDrawable, context);
        JSONObject score = new JSONObject();
        try {
            score = uploader.uploadImage(imageFile, context);
        } catch (IOException | JSONException e) {
            errorMessage("Exception", e.getMessage());
            e.printStackTrace();
        }
        try {
            ImageView image = processor.processJSONData(score);
        } catch (JSONException e) {
            errorMessage("JSONException", e.getMessage());
            e.printStackTrace();
        }

        // Score is now the JSONObject with our response data
        // This code will be used for parsing:
        // String fileName = score.getString("filename");
        // Double imageScore = score.getDouble("file_score");
        // Boolean foodBoolean = score.getBoolean("food_boolean");

        // Need to interpret these into an array of layers and call a LayerDrawable constructor
        // https://stackoverflow.com/questions/4312062/create-a-layerdrawable-object-programatically
        // https://android.jlelse.eu/simplifying-layouts-with-layer-list-drawables-2f750ea1504e

        // Send result values and image to ImageAdapter for parsing
        // ImageAdapter puts image and data into ViewPager
        // This may help: https://stackoverflow.com/questions/8642823/using-setimagedrawable-dynamically-to-set-image-in-an-imageview

    }

    private File createImageFile() throws IOException {
        // Create image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.US).format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file in a global String variable: path for use with ACTION_VIEW intents
        mCurrentPhotoPath = image.getAbsolutePath();
        return image;
    }

    // Message dialog for exception handling
    protected void errorMessage(String method, String message)
    {
        Log.d("EXCEPTION: " + method,  message);

        AlertDialog.Builder messageBox = new AlertDialog.Builder(this);
        messageBox.setTitle(method);
        messageBox.setMessage(message);
        messageBox.setCancelable(false);
        messageBox.setNeutralButton("OK", null);
        messageBox.show();
    }
}
