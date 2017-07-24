package com.example.nero.barcodescanner;


import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.Array;
import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 */
public class Favourite extends Fragment implements ItemInteractor{

    RecyclerView recyclerView;
    ArrayList<BarcodeItem> tempArrayList;
    ArrayList<BarcodeItem> savedItems;
    SwipeRefreshLayout swipeRefreshLayout;

    public Favourite() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v =  inflater.inflate(R.layout.fragment_favourite, container, false);
        recyclerView = (RecyclerView)v.findViewById(R.id.favourite_recyclerView);
        swipeRefreshLayout = (SwipeRefreshLayout)v.findViewById(R.id.favourite_swiperefresh);

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getItems();
            }
        });

        getItems();

        return v;
    }

    public void getItems(){
        try{
            FileInputStream fis = getContext().openFileInput("myfavouriteitem");
            ObjectInputStream ois = new ObjectInputStream(fis);

            tempArrayList = (ArrayList) ois.readObject();
            if(tempArrayList.isEmpty()){
                //DISPLAY NECESSARY MESSAGE
            }
            else{
                savedItems = new ArrayList<>();
                for(BarcodeItem barcodeItem : tempArrayList){
                    BarcodeItem barcodeItem1 = new BarcodeItem(barcodeItem.getTitle(), barcodeItem.getBrand(),
                            barcodeItem.getASN(), barcodeItem.getDescription(), barcodeItem.getImageURL(),
                            barcodeItem.getHighestPrice(), barcodeItem.getLowestPrice());

                    savedItems.add(barcodeItem1);
                }

                ItemRecyclerAdapter adapter = new ItemRecyclerAdapter(getContext(), savedItems);
                adapter.setInteractor(this);
                recyclerView.setAdapter(adapter);
                ois.close();
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        onCompletion();

    }

    @Override
    public void OpenItemDescription(BarcodeItem barcodeItem) {
        Intent intent = new Intent(getActivity(), FavouriteItemDescription.class);
        intent.putExtra("position", barcodeItem.getPosition());
        intent.putExtra("title", barcodeItem.getTitle());
        intent.putExtra("brand", barcodeItem.getBrand());
        intent.putExtra("description", barcodeItem.getDescription());
        intent.putExtra("asn", barcodeItem.getASN());
        intent.putExtra("image", barcodeItem.getImageURL());
        intent.putExtra("price", barcodeItem.getLowestPrice());
        startActivity(intent);
    }

    @Override
    public void onLongClick(final BarcodeItem barcodeItem) {
        AlertDialog.Builder alert = new AlertDialog.Builder(getActivity());
        alert.setTitle("DELETE");
        alert.setMessage("Are you sure you wish to delete the item from the list?");
        alert.setPositiveButton("YES", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                deleteSelectedItem(barcodeItem.getPosition());
                getItems();
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

    private void deleteSelectedItem(int position) {
        try{
            FileInputStream fis = getContext().openFileInput("myfavouriteitem");
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
            FileOutputStream fos = getContext().openFileOutput("myfavouriteitem", Context.MODE_PRIVATE);
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(savedItems);
            oos.close();
            fos.close();
            Toast.makeText(getContext(), "Item deleted successfully!", Toast.LENGTH_LONG).show();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void onCompletion() {

        if (swipeRefreshLayout.isRefreshing()) {
            swipeRefreshLayout.setRefreshing(false);
        }
    }
}
