package com.example.nero.barcodescanner.com.nero.service;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;

/**
 * Created by Nero on 07/07/2017.
 */

public class BarcodeSearchService extends AbstractService {

    private String barcode;
    private JSONArray JSONresult;

    public BarcodeSearchService(String barcode){
        try{
            this.barcode = URLEncoder.encode(barcode, "UTF-8");
        }catch (UnsupportedEncodingException ex){
            ex.printStackTrace();
        }
    }

    public JSONArray getResult(){
        return JSONresult;
    }

    @Override
    public void run() {
        URL url;
        boolean error = false;
        HttpURLConnection httpURLConnection = null;
        StringBuilder results = new StringBuilder();

        try{
            url = new URL("https://api.upcitemdb.com/prod/trial/lookup?upc="
                    + barcode);
            httpURLConnection = (HttpURLConnection)url.openConnection();
            BufferedReader bufferedReader = new BufferedReader(
                    new InputStreamReader(httpURLConnection.getInputStream()));

            String line;
            while((line = bufferedReader.readLine()) != null){
                results.append(line);
            }

            JSONObject jsonObject = new JSONObject(results.toString());
            JSONresult = jsonObject.getJSONArray("items");

        } catch(Exception ex){
            ex.printStackTrace();
            JSONresult = null;
            error = true;
        }finally {
            if(httpURLConnection != null){
                httpURLConnection.disconnect();
            }
        }
        super.serviceCallComplete(error);
    }
}
