package com.example.nero.barcodescanner;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by Nero on 08/07/2017.
 */

public class ItemRecyclerAdapter extends RecyclerView.Adapter<ItemRecyclerAdapter.ViewHolder> {

    ArrayList<BarcodeItem> items;
    Context context;
    ItemInteractor interactor;

    public ItemRecyclerAdapter(Context context, ArrayList<BarcodeItem> items){
        this.context = context;
        this.items = items;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View inflatedView = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_item, parent, false);
        return new ViewHolder(inflatedView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final BarcodeItem item = items.get(position);
        item.setPosition(position);

        holder.title.setText(item.getTitle());
        holder.brand.setText(item.getBrand());
        holder.desc.setText(item.getDescription());

        Picasso.with(context).load(item.getImageURL()).into(holder.image);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                interactor.OpenItemDescription(item);
            }
        });

        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                interactor.onLongClick(item);
                return true;
            }
        });
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public void setInteractor(ItemInteractor interactor){
        this.interactor = interactor;
    }

    class ViewHolder extends RecyclerView.ViewHolder{
        TextView title, brand, desc;
        ImageView image;

        public ViewHolder(View itemView){
            super(itemView);
            title = (TextView)itemView.findViewById(R.id.card_title);
            brand = (TextView)itemView.findViewById(R.id.card_brand);
            desc = (TextView)itemView.findViewById(R.id.card_desc);
            image = (ImageView)itemView.findViewById(R.id.card_image);

        }
    }
}
