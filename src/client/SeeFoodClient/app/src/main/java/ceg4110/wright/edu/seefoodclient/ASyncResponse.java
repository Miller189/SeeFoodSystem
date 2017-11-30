package ceg4110.wright.edu.seefoodclient;

import org.json.JSONArray;

import java.io.File;

/**
 * Created by DJ on 11/29/2017.
 * Implemented in MainActivity and its private class to return
 * a response object from an asynchronous thread to the main thread.
 */

public interface ASyncResponse {
    void processFinish(JSONArray output, File imageFile);
}
