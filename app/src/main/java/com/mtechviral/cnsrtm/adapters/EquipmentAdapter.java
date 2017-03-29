package com.mtechviral.cnsrtm.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.elyeproj.loaderviewlibrary.LoaderImageView;
import com.mtechviral.cnsrtm.R;
import com.mtechviral.cnsrtm.listeners.EquipmentClickListener;
import com.mtechviral.cnsrtm.model.datamodel.EquipmentData;

import java.util.ArrayList;

/**
 * Created by pawankumar on 28/03/17.
 */

public class EquipmentAdapter extends RecyclerView.Adapter<EquipmentAdapter.ItemViewHolder> {
    private static ArrayList<EquipmentData> dataList;
    private LayoutInflater mInflater;
    private Context context;
    private EquipmentClickListener clicklistener = null;

    public EquipmentAdapter(Context ctx, ArrayList<EquipmentData> data) {
        context = ctx;
        dataList = data;
        mInflater = LayoutInflater.from(context);
    }

    public class ItemViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private LoaderImageView image;
        private TextView title, item_count;

        public ItemViewHolder(View itemView) {
            super(itemView);

            itemView.setOnClickListener(this);

            image = (LoaderImageView) itemView.findViewById(R.id.image);
            title = (TextView) itemView.findViewById(R.id.title);
            item_count = (TextView) itemView.findViewById(R.id.item_count);
        }

        @Override
        public void onClick(View v) {

            if (clicklistener != null) {
                clicklistener.itemClicked(v, getAdapterPosition());
            }
        }
    }


    public void resetListData() {
        dataList = new ArrayList<>();
        notifyDataSetChanged();
    }

    public void setClickListener(EquipmentClickListener listener) {
        this.clicklistener = listener;
    }

    @Override
    public ItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_equipments, parent, false);
        ItemViewHolder itemViewHolder = new ItemViewHolder(view);
        return itemViewHolder;
    }

    @Override
    public void onBindViewHolder(ItemViewHolder holder, int position) {
        holder.title.setText(dataList.get(position).getMaterialName());
        holder.item_count.setText(dataList.get(position).getId().toString()+" items in stock");

        Glide.with(context)
                .load("https://s3-us-west-2.amazonaws.com/material-ui-template/ecommerce/style-6/Ecommerce-6-img-1.jpg")
                .thumbnail(0.01f)
                .fitCenter()
                .placeholder(R.drawable.loading_placeholder)
                .into(holder.image);
    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }
}
