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
import com.mtechviral.cnsrtm.listeners.AssignListClickListener;
import com.mtechviral.cnsrtm.model.AssignMaterial;

import java.util.ArrayList;

/**
 * Created by pawankumar on 02/04/17.
 */

public class AssignListAdapter extends RecyclerView.Adapter<AssignListAdapter.ItemViewHolder> implements Filterable {
    private static ArrayList<AssignMaterial> dataList;
    private static ArrayList<AssignMaterial> filtered_datalist;
    private LayoutInflater mInflater;
    private Context context;
    private AssignListClickListener clicklistener = null;
    private AssignListAdapter.ItemFilter mFilter = new AssignListAdapter.ItemFilter();

    public AssignListAdapter(Context ctx, ArrayList<AssignMaterial> data) {
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
        private TextView title, user, status , type, assign;

        public ItemViewHolder(View itemView) {
            super(itemView);

            itemView.setOnClickListener(this);

            image = (ImageView) itemView.findViewById(R.id.image);
            title = (TextView) itemView.findViewById(R.id.title);
            user = (TextView) itemView.findViewById(R.id.user);
            status = (TextView) itemView.findViewById(R.id.status);
            type = (TextView) itemView.findViewById(R.id.type);
            assign = (TextView) itemView.findViewById(R.id.assigned);
        }

        @Override
        public void onClick(View v) {

            if (clicklistener != null) {
                clicklistener.itemRClicked(v, getAdapterPosition());
            }
        }
    }


    public void resetListData() {
        dataList = new ArrayList<>();
        notifyDataSetChanged();
    }

    public void setClickListener(AssignListClickListener listener) {
        this.clicklistener = listener;
    }

    @Override
    public AssignListAdapter.ItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_assign_list, parent, false);
        AssignListAdapter.ItemViewHolder itemViewHolder = new AssignListAdapter.ItemViewHolder(view);
        return itemViewHolder;
    }

    @Override
    public void onBindViewHolder(AssignListAdapter.ItemViewHolder holder, int position) {
        holder.title.setText(dataList.get(position).getMaterialname());
        String td= dataList.get(position).getMaterialname().substring(0,2);
        holder.user.setText(dataList.get(position).getUser());
        holder.status.setText(dataList.get(position).getStatus());
        holder.type.setText("Type: "+dataList.get(position).getType());
        ColorGenerator generator = ColorGenerator.MATERIAL; // or use DEFAULT
// generate random color
        int color1 = generator.getRandomColor();

                TextDrawable drawable = TextDrawable.builder()
                        .buildRect(td, color1);

                holder.image.setImageDrawable(drawable);

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
            final ArrayList<AssignMaterial> list = filtered_datalist;
            final ArrayList<AssignMaterial> result_list = new ArrayList<>(list.size());

            for (int i = 0; i < list.size(); i++) {
                String str_title = list.get(i).getMaterialname();
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
            dataList = (ArrayList<AssignMaterial>) results.values;
            notifyDataSetChanged();
        }
    }
}

