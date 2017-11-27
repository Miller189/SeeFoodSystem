package ceg4110.wright.edu.seefoodclient;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
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

import com.android.volley.Cache;
import com.android.volley.Network;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.BasicNetwork;
import com.android.volley.toolbox.DiskBasedCache;
import com.android.volley.toolbox.HurlStack;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    ImageAdapter adapter;
    static final int REQUEST_TAKE_PHOTO = 1;
    String mCurrentPhotoPath;
    Context context;
    int pagerSize;
    Boolean pictureTaken;

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
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // Check which request we're responding to
        if (requestCode == REQUEST_TAKE_PHOTO) {
            // Make sure the request was successful
            if (resultCode == RESULT_OK) {
                File imageFile = new File(mCurrentPhotoPath);

                try {
                    uploadImage(imageFile, context);
                } catch (IOException | JSONException e) {
                    errorMessage("Exception", e.getMessage());
                    e.printStackTrace();
                }

            }
        }
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

    void uploadImage(final File imageFile, final Context context) throws IOException, JSONException{
        final String url;
        url = "http://34.237.62.217/evaluation";
        Drawable imageDrawable = Drawable.createFromPath(mCurrentPhotoPath);
        final JSONProcessor processor = new JSONProcessor(imageDrawable, context);
        final ImageView[] image = {new ImageView(context)};
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
                messageBox.setNeutralButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        System.exit(0);
                    }
                });
                messageBox.show();
                System.exit(0);
            }
        }, new Response.Listener() {
            @Override
            public void onResponse(Object response) {
                result[0] = (JSONObject)response;
                try {
                    image[0] = processor.processJSONData(result[0]);
                } catch (JSONException e) {
                    errorMessage("JSONException", e.getMessage());
                    e.printStackTrace();
                }
                pagerSize = adapter.addView(image[0]);

            }

        }, imageFile);

        queue.add(jsObjRequest);

    }
}
