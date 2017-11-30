package ceg4110.wright.edu.seefoodclient;

import org.json.JSONArray;

import java.io.File;

/**
 * Created by DJ on 11/29/2017.
 */

public interface ASyncResponse {
    void processFinish(JSONArray output, File imageFile);
}
