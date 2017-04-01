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
import com.mtechviral.cnsrtm.R;
import com.mtechviral.cnsrtm.listeners.PendingSpareClickListener;
import com.mtechviral.cnsrtm.model.datamodel.PendingSparesData;

import java.util.ArrayList;

/**
 * Created by pawankumar on 01/04/17.
 */

public class PendingSpareAdapter extends RecyclerView.Adapter<PendingSpareAdapter.ItemViewHolder> implements Filterable {
    private static ArrayList<PendingSparesData> dataList;
    private static ArrayList<PendingSparesData> filtered_datalist;
    private LayoutInflater mInflater;
    private Context context;
    private PendingSpareClickListener clicklistener = null;
    private PendingSpareAdapter.ItemFilter mFilter = new PendingSpareAdapter.ItemFilter();

    public PendingSpareAdapter(Context ctx, ArrayList<PendingSparesData> data) {
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
        private ImageView image;
        private TextView title, assigned,status;

        public ItemViewHolder(View itemView) {
            super(itemView);

            itemView.setOnClickListener(this);

            image = (ImageView) itemView.findViewById(R.id.image);
            title = (TextView) itemView.findViewById(R.id.title);
            assigned = (TextView) itemView.findViewById(R.id.assigned);
            status = (TextView) itemView.findViewById(R.id.status);
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

    public void setClickListener(PendingSpareClickListener listener) {
        this.clicklistener = listener;
    }

    @Override
    public PendingSpareAdapter.ItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_pending_spares, parent, false);
        PendingSpareAdapter.ItemViewHolder itemViewHolder = new PendingSpareAdapter.ItemViewHolder(view);
        return itemViewHolder;
    }

    @Override
    public void onBindViewHolder(PendingSpareAdapter.ItemViewHolder holder, int position) {
        holder.title.setText(dataList.get(position).getSparename());
        String td= dataList.get(position).getSparename().substring(0,2);
        try {
            holder.assigned.setText(dataList.get(position).getAssignedTo().toString());
        }catch (Exception e){}
        holder.status.setText(dataList.get(position).getStatus());
        ColorGenerator generator = ColorGenerator.MATERIAL; // or use DEFAULT
// generate random color
        int color1 = generator.getRandomColor();

        try {
                TextDrawable drawable = TextDrawable.builder()
                        .buildRect(td, color1);

                holder.image.setImageDrawable(drawable);
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
            final ArrayList<PendingSparesData> list = filtered_datalist;
            final ArrayList<PendingSparesData> result_list = new ArrayList<>(list.size());

            for (int i = 0; i < list.size(); i++) {
                String str_title = list.get(i).getSparename();
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
            dataList = (ArrayList<PendingSparesData>) results.values;
            notifyDataSetChanged();
        }
    }
}
