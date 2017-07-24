package com.example.nero.barcodescanner;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

public class CameraScanner extends AppCompatActivity {


    ImageView camera;
    Toolbar toolbar;
    EditText searchET;
    Button submitBtn;
    final Activity activity = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera_scanner);

        camera = (ImageView)findViewById(R.id.scanner_camera);
        searchET = (EditText)findViewById(R.id.scanner_searchet);
        submitBtn = (Button)findViewById(R.id.scanner_submitbtn);
        toolbar = (Toolbar)findViewById(R.id.scanner_toolbar);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        submitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                searchIt();
            }
        });

        camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                IntentIntegrator intentIntegrator = new IntentIntegrator(activity);
                intentIntegrator.setDesiredBarcodeFormats(IntentIntegrator.QR_CODE_TYPES);
                intentIntegrator.setPrompt("Scan");
                intentIntegrator.setCameraId(0);
                intentIntegrator.setBeepEnabled(false);
                intentIntegrator.setBarcodeImageEnabled(false);
                intentIntegrator.initiateScan();
            }
        });
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return  true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode,
                resultCode, data);
        if(result != null){
            if(result.getContents()==null){
                Toast.makeText(activity, "You cancelled the scanning", Toast.LENGTH_LONG).show();
            }
            else{
                String barcode = result.getContents();
                Intent intent = new Intent(activity, BarcodeResult.class);
                intent.putExtra("barcode", barcode);
                startActivity(intent);
            }
        }
        else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    public void searchIt(){
        String barcode = searchET.getText().toString();
        Intent intent = new Intent(CameraScanner.this, BarcodeResult.class);
        intent.putExtra("barcode", barcode);
        startActivity(intent);
    }
}
