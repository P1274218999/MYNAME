package com.dhht.sld.main.home.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.dhht.sld.R;
import com.dhht.sld.base.Common;
import com.dhht.sld.main.home.model.HelpRoutResModel;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

public class HelpClickMarkerAdapter extends RecyclerView.Adapter<HelpClickMarkerAdapter.ViewHolder> {
    private List<HelpRoutResModel> data;

    public HelpClickMarkerAdapter() {
        data = new ArrayList<>();
    }

    public void setData(List<HelpRoutResModel> data) {
        this.data = data;
    }

    /**
     */
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.view_list_help_route,parent,false);
        ViewHolder holder = new ViewHolder(v);
        return holder;
    }

    /**
     *
     */
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        if (position >= data.size()) return;
        HelpRoutResModel value = data.get(position);

        holder.routeTitle.setText(value.title);
        holder.routeDesc.setText(value.getDuration()+"("+value.getDistance()+")");

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    /**
     * 内部Holder
     */
    class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.route_title) TextView routeTitle;
        @BindView(R.id.route_desc) TextView routeDesc;

        public ViewHolder(View view) {
            super(view);
            Common.bindButterKnifeView(this,view);
        }
    }

    // item点击回调
    private OnItemClickListener listener;
    public interface OnItemClickListener {
        void onClick(String province, String area, double latitude, double longitude);
    }
    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }
}
