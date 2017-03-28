package com.mtechviral.cnsrtm.adapters;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
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
        private ImageView image1,image2;
        private TextView title1,itemCount1,title2,itemCount2;
        private CardView leftLayout, rightLayout;

        public ItemViewHolder(View itemView) {
            super(itemView);

            itemView.setOnClickListener(this);

            leftLayout = (CardView) itemView.findViewById(R.id.leftLayout);
            rightLayout = (CardView) itemView.findViewById(R.id.rightLayout);
            image1 = (ImageView) itemView.findViewById(R.id.image1);
            title1 = (TextView) itemView.findViewById(R.id.title1);
            itemCount1 = (TextView) itemView.findViewById(R.id.itemCount1);
            image2 = (ImageView) itemView.findViewById(R.id.image2);
            title2 = (TextView) itemView.findViewById(R.id.title2);
            itemCount2 = (TextView) itemView.findViewById(R.id.itemCount2);
        }

        @Override
        public void onClick(View v) {

            if (clicklistener != null) {
                clicklistener.itemClicked(v, getAdapterPosition());
            }
        }
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
        int num = position + 1;
        holder.leftLayout.setVisibility(View.GONE);
        holder.rightLayout.setVisibility(View.GONE);
        if(num % 2 != 0) {
            holder.leftLayout.setVisibility(View.VISIBLE);
            Glide.with(context)
                    .load(R.drawable.airoplane)
                    .thumbnail(0.01f)
                    .centerCrop()
                    .into(holder.image1);
            holder.title1.setText(dataList.get(position).getMaterialName());
            holder.itemCount1.setText(dataList.get(position).getId() + " Items");
        }else{
            holder.rightLayout.setVisibility(View.VISIBLE);
            Glide.with(context)
                    .load(R.drawable.airoplane)
                    .thumbnail(0.01f)
                    .centerCrop()
                    .into(holder.image2);
            holder.title2.setText(dataList.get(position).getMaterialName());
            holder.itemCount2.setText(dataList.get(position).getId() + " Items");
        }
    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }
}
