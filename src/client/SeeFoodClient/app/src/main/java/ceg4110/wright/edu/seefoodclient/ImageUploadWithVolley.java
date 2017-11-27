package ceg4110.wright.edu.seefoodclient;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.HttpHeaderParser;

import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import ch.boye.httpclientandroidlib.entity.ContentType;
import ch.boye.httpclientandroidlib.entity.mime.HttpMultipartMode;
import ch.boye.httpclientandroidlib.entity.mime.MultipartEntityBuilder;


/**
 * Created by DJ on 11/26/2017.
 * Shamelessly copied from https://stackoverflow.com/questions/27112694/how-to-do-upload-image-with-volley-library
 */

public class ImageUploadWithVolley<JSONObject> extends Request<JSONObject> {

    private MultipartEntityBuilder mBuilder = MultipartEntityBuilder.create();
    private final Response.Listener<JSONObject> mListener;
    private final File yourImageFile;
    protected Map<String, String> headers;

    ImageUploadWithVolley(String url, Response.ErrorListener errorListener, Response.Listener<JSONObject> listener, File imageFile)   {
        super(Method.POST, url, errorListener);
        mListener = listener;
        yourImageFile = imageFile;
        addImageEntity();
    }

    @Override
    public Map<String, String> getHeaders() throws AuthFailureError {
        Map<String, String> headers = super.getHeaders();
        if (headers == null
                || headers.equals(Collections.emptyMap())) {
            headers = new HashMap<String, String>();
        }
        headers.put("Accept", "application/json");
        return headers;
    }

    private void addImageEntity() {
        mBuilder.addBinaryBody("give your image name", yourImageFile, ContentType.create("image/jpeg"), yourImageFile.getName());
        mBuilder.setMode(HttpMultipartMode.BROWSER_COMPATIBLE);
        mBuilder.setLaxMode().setBoundary("xx").setCharset(Charset.forName("UTF-8"));

    }

    @Override
    public String getBodyContentType()   {
        return mBuilder.build().getContentType().getValue();
    }

    @Override
    public byte[] getBody() throws AuthFailureError    {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        try {
            mBuilder.build().writeTo(bos);
        } catch (IOException e){
            VolleyLog.e("IOException writing to ByteArrayOutputStream bos, building the multipart request.");
        }
        return bos.toByteArray();
    }

    @Override
    protected Response<JSONObject> parseNetworkResponse(NetworkResponse response){
        JSONObject result = null;
        return Response.success(result, HttpHeaderParser.parseCacheHeaders(response));
    }


    @Override
    protected void deliverResponse(JSONObject response) {
        mListener.onResponse(response);
    }
}