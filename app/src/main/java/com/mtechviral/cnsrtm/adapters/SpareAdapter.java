package com.mtechviral.cnsrtm.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import com.amulyakhare.textdrawable.TextDrawable;
import com.amulyakhare.textdrawable.util.ColorGenerator;
import com.bumptech.glide.Glide;
import com.mtechviral.cnsrtm.R;
import com.mtechviral.cnsrtm.listeners.SpareClickListener;
import com.mtechviral.cnsrtm.model.datamodel.SpareData;

import java.util.ArrayList;

/**
 * Created by pawankumar on 30/03/17.
 */

public class SpareAdapter extends RecyclerView.Adapter<SpareAdapter.ItemViewHolder> implements Filterable {
    private static ArrayList<SpareData> dataList;
    private static ArrayList<SpareData> filtered_datalist;
    private LayoutInflater mInflater;
    private Context context;
    private SpareClickListener clicklistener = null;
    private SpareAdapter.ItemFilter mFilter = new SpareAdapter.ItemFilter();

    public SpareAdapter(Context ctx, ArrayList<SpareData> data) {
        context = ctx;
        dataList = data;
        filtered_datalist = data;
        mInflater = LayoutInflater.from(context);
    }

    @Override
    public Filter getFilter() {
        return mFilter;
    }

    class ItemViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private ImageView image;
        private TextView title, item_count;

        ItemViewHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);

            image = (ImageView) itemView.findViewById(R.id.image);
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

    public void setClickListener(SpareClickListener listener) {
        this.clicklistener = listener;
    }

    @Override
    public SpareAdapter.ItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_spares, parent, false);
        return new ItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(SpareAdapter.ItemViewHolder holder, int position) {
        holder.title.setText(dataList.get(position).getName());
        String td= dataList.get(position).getName().substring(0,2);
        holder.item_count.setText(dataList.get(position).getQuantity().toString()+" items in stock");

        ColorGenerator generator = ColorGenerator.MATERIAL; // or use DEFAULT
// generate random color
        int color1 = generator.getRandomColor();
        try {
            if (dataList.get(position).getImageurl().equals("1")) {
                TextDrawable drawable = TextDrawable.builder()
                        .buildRect(td, color1);

                holder.image.setScaleType(ImageView.ScaleType.FIT_CENTER);
                holder.image.setImageDrawable(drawable);

            } else {
                Glide.with(context)
//                .load("https://s3-us-west-2.amazonaws.com/material-ui-template/ecommerce/style-6/Ecommerce-6-img-1.jpg")
                        .load(dataList.get(position).getImageurl())
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
            final ArrayList<SpareData> list = filtered_datalist;
            final ArrayList<SpareData> result_list = new ArrayList<>(list.size());

            for (int i = 0; i < list.size(); i++) {
                String str_title = list.get(i).getName();
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
            dataList = (ArrayList<SpareData>) results.values;
            notifyDataSetChanged();
        }
    }
}
