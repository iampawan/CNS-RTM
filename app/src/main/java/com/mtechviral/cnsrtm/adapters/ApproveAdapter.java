package com.mtechviral.cnsrtm.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import com.amulyakhare.textdrawable.TextDrawable;
import com.amulyakhare.textdrawable.util.ColorGenerator;
import com.mtechviral.cnsrtm.R;
import com.mtechviral.cnsrtm.listeners.ApprovedItemClickListener;
import com.mtechviral.cnsrtm.model.datamodel.ApproveData;

import java.util.ArrayList;

/**
 * Created by pawankumar on 02/04/17.
 */

public class ApproveAdapter extends RecyclerView.Adapter<ApproveAdapter.ItemViewHolder> implements Filterable {
    private static ArrayList<ApproveData> dataList;
    private static ArrayList<ApproveData> filtered_datalist;
    private LayoutInflater mInflater;
    private Context context;
    private ApprovedItemClickListener clicklistener = null;
    private ApproveAdapter.ItemFilter mFilter = new ApproveAdapter.ItemFilter();

    public ApproveAdapter(Context ctx, ArrayList<ApproveData> data) {
        context = ctx;
        dataList = data;
        filtered_datalist = data;
        mInflater = LayoutInflater.from(context);
    }

    @Override
    public Filter getFilter() {
        return mFilter;
    }

    public class ItemViewHolder extends RecyclerView.ViewHolder {
        private ImageView image;
        private TextView title,stage,location;
        private Button btnApprove;

        public ItemViewHolder(View itemView) {
            super(itemView);

            image = (ImageView) itemView.findViewById(R.id.image);
            title = (TextView) itemView.findViewById(R.id.title);
            stage = (TextView) itemView.findViewById(R.id.stage);
            location = (TextView) itemView.findViewById(R.id.location);
            btnApprove = (Button) itemView.findViewById(R.id.btnApproved);
            btnApprove.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    clicklistener.itemClicked(v, getAdapterPosition());
                }
            });
        }


    }


    public void resetListData() {
        dataList = new ArrayList<>();
        notifyDataSetChanged();
    }

    public void setClickListener(ApprovedItemClickListener listener) {
        this.clicklistener = listener;
    }

    @Override
    public ApproveAdapter.ItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_approve, parent, false);
        ApproveAdapter.ItemViewHolder itemViewHolder = new ApproveAdapter.ItemViewHolder(view);
        return itemViewHolder;
    }

    @Override
    public void onBindViewHolder(ApproveAdapter.ItemViewHolder holder, int position) {
        String sparename = dataList.get(position).getSparename();
        String materialname = dataList.get(position).getMaterialname();
        Boolean approved = dataList.get(position).getApproved();
        if(sparename == null){
            sparename = "";
            holder.title.setText(materialname);
        }else{
            holder.title.setText(sparename);
        }

        String td= holder.title.getText().toString().substring(0,2);
        holder.stage.setText(dataList.get(position).getStage());
        holder.location.setText(dataList.get(position).getLocation());
        if(approved){
            holder.btnApprove.setBackgroundColor(context.getResources().getColor(R.color.button_green));
            holder.btnApprove.setText("Approved");
            holder.btnApprove.setFocusable(false);
            holder.btnApprove.setClickable(false);
        }
        else{
            holder.btnApprove.setBackgroundColor(context.getResources().getColor(R.color.button_red));
            holder.btnApprove.setText("Click to approve");
            holder.btnApprove.setFocusable(true);
            holder.btnApprove.setClickable(true);
        }
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
            final ArrayList<ApproveData> list = filtered_datalist;
            final ArrayList<ApproveData> result_list = new ArrayList<>(list.size());

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
            dataList = (ArrayList<ApproveData>) results.values;
            notifyDataSetChanged();
        }
    }
}
