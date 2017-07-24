package com.example.nero.barcodescanner;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.nero.barcodescanner.com.nero.service.AbstractService;
import com.example.nero.barcodescanner.com.nero.service.BarcodeSearchService;
import com.example.nero.barcodescanner.com.nero.service.ServiceListener;
import com.squareup.picasso.Picasso;

import org.json.JSONException;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by Nero on 07/07/2017.
 */

public class BarcodeResult extends AppCompatActivity implements ServiceListener, Serializable {

    TextView brandTV, titleTV, asnTV, descTV, priceTV, noResult;
    ImageView itemImage;
    Button favourite;
    String barcode;
    Toolbar toolbar;
    private Thread thread;
    boolean itemExist;
    Context context;
    BarcodeItem barcodeItem, myItem;
    final Activity activity = this;
    ArrayList<BarcodeItem>myFavItem = new ArrayList<>();
    LinearLayout linearLayout;


    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_barcode_result);

        context = this;
        barcode = getIntent().getStringExtra("barcode");

        titleTV = (TextView)findViewById(R.id.barcode_result_title);
        brandTV = (TextView)findViewById(R.id.barcode_result_brand);
        asnTV = (TextView)findViewById(R.id.barcode_result_asn);
        descTV = (TextView)findViewById(R.id.barcode_result_desc);
        priceTV = (TextView)findViewById(R.id.barcode_result_averagePrice);
        itemImage = (ImageView)findViewById(R.id.barcode_result_image);
        toolbar = (Toolbar)findViewById(R.id.barcode_result_toolbar);
        favourite = (Button)findViewById(R.id.barcode_result_saveBtn);
        linearLayout = (LinearLayout)findViewById(R.id.barcode_result_mainLayout);
        noResult = (TextView)findViewById(R.id.barcode_result_errorTitle);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        favourite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveItem("myfavouriteitem");
            }
        });

        searchIt(barcode);

    }

    public void searchIt(String barcode){
        String[] result = new String[]{barcode};
        BarcodeSearchService barcodeSearchService = new BarcodeSearchService(barcode);
        barcodeSearchService.addListener(this);

        thread = new Thread(barcodeSearchService);
        thread.start();

    }

    @Override
    public void serviceComplete(AbstractService abstractService) throws JSONException {
        findViewById(R.id.barcode_result_loadingPanel).setVisibility(View.GONE);
        if(!abstractService.hasError()){
            noResult.setVisibility(View.GONE);
            BarcodeSearchService barcodeSearchService = (BarcodeSearchService) abstractService;
            barcodeItem = new BarcodeItem(barcodeSearchService.getResult().getJSONObject(0).getString("title"),
                    barcodeSearchService.getResult().getJSONObject(0).getString("brand"),
                    barcodeSearchService.getResult().getJSONObject(0).getString("ean"),
                    barcodeSearchService.getResult().getJSONObject(0).getString("description"),
                    barcodeSearchService.getResult().getJSONObject(0).getString("images"),
                    barcodeSearchService.getResult().getJSONObject(0).getString("highest_recorded_price"),
                    barcodeSearchService.getResult().getJSONObject(0).getString("lowest_recorded_price")
            );

            titleTV.setText(barcodeItem.getTitle());
            brandTV.setText(barcodeItem.getBrand());
            asnTV.setText("UPC Number: " + barcodeItem.getASN());
            descTV.setText(barcodeItem.getDescription());

            String imageURL = barcodeItem.getImageURL().toString().replace("[", "").replace("\"", "").replace("]", "");
            if(imageURL.contains(",")){
                imageURL = imageURL.substring(0, imageURL.indexOf(','));
            }

            Picasso.with(context).load(imageURL).into(itemImage);
            barcodeItem.setImageURL(imageURL);
            priceTV.setText("£" + barcodeItem.getLowestPrice());

            saveRecentItem("myrecentitem");
        }
        else{
            linearLayout.setVisibility(View.GONE);
        }
    }

    public void saveItem(String fisName){
        myItem = new BarcodeItem(barcodeItem.getTitle(), barcodeItem.getBrand(), barcodeItem.getASN(),
                barcodeItem.getDescription(), barcodeItem.getImageURL(), barcodeItem.getHighestPrice(),
                barcodeItem.getLowestPrice());

        try{
            FileInputStream fis = activity.openFileInput(fisName);
            ObjectInputStream ois = new ObjectInputStream(fis);

            myFavItem = (ArrayList) ois.readObject();

        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        itemExist = false;

        for(int i = 0; i <myFavItem.size(); i++){
            if(myFavItem.get(i).getTitle().equals(myItem.getTitle()) &&
                    myFavItem.get(i).getBrand().equals(myItem.getBrand()) &&
                    myFavItem.get(i).getDescription().equals(myItem.getDescription())){
                Toast.makeText(context, "Item already added to favourites", Toast.LENGTH_LONG).show();
                itemExist = true;
            }
        }

        if(itemExist == false){
            myFavItem.add(myItem);
            Toast.makeText(context, "Item saved successfully!", Toast.LENGTH_LONG).show();
        }
        try{
            FileOutputStream fos = context.openFileOutput(fisName, Context.MODE_PRIVATE);
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(myFavItem);
            oos.close();
            fos.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void saveRecentItem(String fisName){
        myItem = new BarcodeItem(barcodeItem.getTitle(), barcodeItem.getBrand(), barcodeItem.getASN(),
                barcodeItem.getDescription(), barcodeItem.getImageURL(), barcodeItem.getHighestPrice(),
                barcodeItem.getLowestPrice());

        try{
            FileInputStream fis = activity.openFileInput(fisName);
            ObjectInputStream ois = new ObjectInputStream(fis);

            myFavItem = (ArrayList) ois.readObject();

        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        itemExist = false;

        for(int i = 0; i <myFavItem.size(); i++){
            if(myFavItem.get(i).getTitle().equals(myItem.getTitle()) &&
                    myFavItem.get(i).getBrand().equals(myItem.getBrand()) &&
                    myFavItem.get(i).getDescription().equals(myItem.getDescription())){
                itemExist = true;
            }
        }

        if(itemExist == false){
            myFavItem.add(myItem);
        }
        try{
            FileOutputStream fos = context.openFileOutput(fisName, Context.MODE_PRIVATE);
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(myFavItem);
            oos.close();
            fos.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return  true;
    }
}
