package com.example.nero.barcodescanner;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

public class FavouriteItemDescription extends AppCompatActivity {

    TextView title, brand, desc, asn, price;
    String titleString, brandString, descString, asnString, priceString, imageURLString;
    int position;
    ImageView image;
    Button remove;
    Toolbar toolbar;
    private Context context = this;
    private Activity activity = this;
    ArrayList<BarcodeItem> savedItems;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favourite_item_description);

        title = (TextView)findViewById(R.id.favourite_title);
        brand = (TextView)findViewById(R.id.favourite_brand);
        desc = (TextView)findViewById(R.id.favourite_desc);
        asn = (TextView)findViewById(R.id.favourite_asn);
        price = (TextView)findViewById(R.id.favourite_averagePrice);
        image = (ImageView)findViewById(R.id.favourite_image);
        toolbar = (Toolbar)findViewById(R.id.favourite_toolbar);
        remove = (Button)findViewById(R.id.favourite_removeBtn);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        position = getIntent().getIntExtra("position", 0);
        titleString = getIntent().getStringExtra("title");
        brandString = getIntent().getStringExtra("brand");
        descString = getIntent().getStringExtra("description");
        asnString = getIntent().getStringExtra("asn");
        priceString = getIntent().getStringExtra("price");
        imageURLString = getIntent().getStringExtra("image");

        title.setText(titleString);
        brand.setText(brandString);
        desc.setText(descString);
        asn.setText(asnString);
        price.setText(priceString);
        Picasso.with(context).load(imageURLString).into(image);

        remove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder alert = new AlertDialog.Builder(activity);
                alert.setTitle("DELETE");
                alert.setMessage("Are you sure you wish to delete the item from the list?");
                alert.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        deleteSelectedItem(position);
                        Intent intent = new Intent(FavouriteItemDescription.this, MainControl.class);
                        startActivity(intent);
                    }
                });

                alert.setNegativeButton("NO", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });

                alert.show();

            }
        });
    }

    public void deleteSelectedItem(int position){
        try{
            FileInputStream fis = context.openFileInput("myfavouriteitem");
            ObjectInputStream ois = new ObjectInputStream(fis);

            savedItems = (ArrayList) ois.readObject();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        savedItems.remove(position);

        try{
            FileOutputStream fos = context.openFileOutput("myfavouriteitem", Context.MODE_PRIVATE);
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(savedItems);
            oos.close();
            fos.close();
            Toast.makeText(context, "Item deleted successfully!", Toast.LENGTH_LONG).show();
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
