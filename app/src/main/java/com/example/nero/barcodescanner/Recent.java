package com.example.nero.barcodescanner;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.zxing.integration.android.IntentIntegrator;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 */
public class Recent extends Fragment implements ItemInteractor {

    RecyclerView recyclerView;
    ArrayList<BarcodeItem> tempArrayList;
    ArrayList<BarcodeItem> recentItems;
    SwipeRefreshLayout swipeRefreshLayout;

    public Recent() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_recent, container, false);

        recyclerView = (RecyclerView)v.findViewById(R.id.recent_recyclerView);
        swipeRefreshLayout = (SwipeRefreshLayout)v.findViewById(R.id.recent_swipeRefresh);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getRecentItems();
            }
        });

        getRecentItems();

        return v;
    }

    public void getRecentItems(){
        try{
            FileInputStream fis = getContext().openFileInput("myrecentitem");
            ObjectInputStream ois = new ObjectInputStream(fis);

            tempArrayList = (ArrayList) ois.readObject();
            if(tempArrayList.isEmpty()){
                //DO SOMETHING
            }
            else{
                recentItems = new ArrayList<>();
                for(BarcodeItem barcodeItem : tempArrayList){
                    BarcodeItem barcodeItem1 = new BarcodeItem(barcodeItem.getTitle(),
                            barcodeItem.getBrand(), barcodeItem.getASN(), barcodeItem.getDescription(),
                            barcodeItem.getImageURL(), barcodeItem.getHighestPrice(), barcodeItem.getLowestPrice());
                    if(recentItems.size()> 9){
                        recentItems.remove(0);
                        recentItems.add(barcodeItem1);
                    }
                    else {
                        recentItems.add(barcodeItem1);
                    }
                }

                ItemRecyclerAdapter adapter = new ItemRecyclerAdapter(getContext(), recentItems);
                adapter.setInteractor(this);
                recyclerView.setAdapter(adapter);
                ois.close();

                if (swipeRefreshLayout.isRefreshing()) {
                    swipeRefreshLayout.setRefreshing(false);
                }
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void OpenItemDescription(BarcodeItem barcodeItem) {
        Intent intent = new Intent(getActivity(), BarcodeResult.class);
        intent.putExtra("barcode", barcodeItem.getASN());
        startActivity(intent);
    }

    @Override
    public void onLongClick(BarcodeItem barcodeItem) {

    }
}
