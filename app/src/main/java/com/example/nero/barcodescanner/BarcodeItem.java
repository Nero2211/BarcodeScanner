package com.example.nero.barcodescanner;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.example.nero.barcodescanner.com.nero.service.AbstractService;
import com.example.nero.barcodescanner.com.nero.service.BarcodeSearchService;
import com.example.nero.barcodescanner.com.nero.service.ServiceListener;

import org.json.JSONException;

import java.io.Serializable;

/**
 * Created by Nero on 07/07/2017.
 */

public class BarcodeItem implements Serializable {

    private transient  int position;
    private String title, brand, ASN, highestPrice, lowestPrice, imageURL, description;

    public BarcodeItem(String itemTitle, String itemBrand,
                       String itemASN, String itemDesc, String itemImageURL, String highestP, String lowestp) {
        this.title = itemTitle;
        this.brand = itemBrand;
        this.ASN = itemASN;
        this.description = itemDesc;
        this.imageURL = itemImageURL;
        this.highestPrice = highestP;
        this.lowestPrice = lowestp;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public String getTitle() {
        return title;
    }

    public String getBrand() {
        return brand;
    }

    public String getASN() {
        return ASN;
    }

    public String getHighestPrice() {
        return highestPrice;
    }

    public String getLowestPrice() {
        return lowestPrice;
    }

    public String getImageURL() {
        return imageURL;
    }

    public String getDescription() {
        return description;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }
}
