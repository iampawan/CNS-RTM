package com.mtechviral.cnsrtm.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import com.amulyakhare.textdrawable.TextDrawable;
import com.amulyakhare.textdrawable.util.ColorGenerator;
import com.bumptech.glide.Glide;
import com.elyeproj.loaderviewlibrary.LoaderImageView;
import com.mtechviral.cnsrtm.R;
import com.mtechviral.cnsrtm.listeners.EquipmentClickListener;
import com.mtechviral.cnsrtm.model.datamodel.EquipmentData;

import java.util.ArrayList;

/**
 * Created by pawankumar on 28/03/17.
 */

public class EquipmentAdapter extends RecyclerView.Adapter<EquipmentAdapter.ItemViewHolder> implements Filterable {
    private static ArrayList<EquipmentData> dataList;
    private static ArrayList<EquipmentData> filtered_datalist;
    private LayoutInflater mInflater;
    private Context context;
    private EquipmentClickListener clicklistener = null;
    private ItemFilter mFilter = new ItemFilter();

    public EquipmentAdapter(Context ctx, ArrayList<EquipmentData> data) {
        context = ctx;
        dataList = data;
        filtered_datalist = data;
        mInflater = LayoutInflater.from(context);
    }

    @Override
    public Filter getFilter() {
        return mFilter;
    }

    public class ItemViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private LoaderImageView image;
        private TextView title, item_count;

        public ItemViewHolder(View itemView) {
            super(itemView);

            itemView.setOnClickListener(this);

            image = (LoaderImageView) itemView.findViewById(R.id.image);
            title = (TextView) itemView.findViewById(R.id.title);
            item_count = (TextView) itemView.findViewById(R.id.material_id);
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
        String td= dataList.get(position).getMaterialName().substring(0,2);
        holder.item_count.setText("Material ID : "+dataList.get(position).getId().toString());
        ColorGenerator generator = ColorGenerator.MATERIAL; // or use DEFAULT
// generate random color
        int color1 = generator.getRandomColor();

        try {
            Log.d("aaa", "onBindViewHolder: " + dataList.get(position).getImageUrl());
            if (dataList.get(position).getImageUrl().equals("1")) {
                TextDrawable drawable = TextDrawable.builder()
                        .buildRect(td, color1);

                holder.image.setImageDrawable(drawable);
            } else {

                Glide.with(context)
                        .load(dataList.get(position).getImageUrl())
                        .thumbnail(0.01f)
                        .fitCenter()
                        .placeholder(R.drawable.loading_placeholder)
                        .into(holder.image);
            }
        }catch (Exception e){}
    }



    @Override
    public int getItemCount() {
        return dataList.size();
    }

    private class ItemFilter extends Filter {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            String query = constraint.toString().toLowerCase();

            FilterResults results = new FilterResults();
            final ArrayList<EquipmentData> list = filtered_datalist;
            final ArrayList<EquipmentData> result_list = new ArrayList<>(list.size());

            for (int i = 0; i < list.size(); i++) {
                String str_title = list.get(i).getMaterialName();
                if (str_title.toLowerCase().contains(query)) {
                    result_list.add(list.get(i));
                }
            }

            results.values = result_list;
            results.count = result_list.size();

            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            dataList = (ArrayList<EquipmentData>) results.values;
            notifyDataSetChanged();
        }
    }
}
