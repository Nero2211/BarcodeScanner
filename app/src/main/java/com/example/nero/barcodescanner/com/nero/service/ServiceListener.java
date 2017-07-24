package com.example.nero.barcodescanner.com.nero.service;

import org.json.JSONException;

/**
 * Created by Nero on 07/07/2017.
 */

public interface ServiceListener {

    public void serviceComplete(AbstractService abstractService)
        throws JSONException;
}
