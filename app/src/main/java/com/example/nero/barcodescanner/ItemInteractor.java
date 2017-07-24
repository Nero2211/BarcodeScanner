package com.example.nero.barcodescanner;

/**
 * Created by Nero on 08/07/2017.
 */

public interface ItemInteractor {
    void OpenItemDescription(BarcodeItem barcodeItem);

    void onLongClick(BarcodeItem barcodeItem);
}
