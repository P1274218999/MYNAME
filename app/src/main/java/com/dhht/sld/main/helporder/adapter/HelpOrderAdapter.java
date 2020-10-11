package com.dhht.sld.main.helporder.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.dhht.sld.R;
import com.dhht.sld.base.Common;
import com.dhht.sld.base.helper.ContextHelper;
import com.dhht.sld.main.helporder.bean.HelpOrderBean;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

public class HelpOrderAdapter extends RecyclerView.Adapter<HelpOrderAdapter.ViewHolder> {
    private List<HelpOrderBean.ResData> data;
    private Context context = ContextHelper.getInstance().getApplicationContext();

    public HelpOrderAdapter() {
        data = new ArrayList<>();
    }

    public void setData(List<HelpOrderBean.ResData> data) {
        this.data = data;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.view_list_help_order,parent,false);
        ViewHolder holder = new ViewHolder(v);
        return holder;
    }

    /**
     *
     */
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        if (position >= data.size()) return;
        HelpOrderBean.ResData value = data.get(position);

        holder.articleName.setText(value.article_name);
        holder.orderTime.setText(value.yes_time);
        holder.orderStartAddress.setText(value.start_address);
        holder.orderEndAddress.setText(value.end_address);
        switch (value.status)
        {
            case 0:
                holder.orderStatus.setText("已取消");
                break;
            case 1:
                holder.orderStatus.setText("待取货");
                holder.orderStatus.setTextColor(context.getResources().getColor(R.color.appTheme));
                break;
            case 2:
                holder.orderStatus.setText("待送货");
                holder.orderStatus.setTextColor(context.getResources().getColor(R.color.red));
                break;
            case 3:
                holder.orderStatus.setText("已完成");
                holder.orderStatus.setTextColor(context.getResources().getColor(R.color.green));
                break;
            default:
                holder.orderStatus.setText("其他");
                break;
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onClick(value.find_order_id,value.status);
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
    static class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.article_name) TextView articleName;
        @BindView(R.id.order_status) TextView orderStatus;
        @BindView(R.id.order_time) TextView orderTime;
        @BindView(R.id.order_start_address) TextView orderStartAddress;
        @BindView(R.id.order_end_address) TextView orderEndAddress;

        public ViewHolder(View view) {
            super(view);
            Common.bindButterKnifeView(this,view);
        }
    }

    // item点击回调
    private OnItemClickListener listener;
    public interface OnItemClickListener {
        void onClick(int find_order_id, int status);
    }
    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }
}
