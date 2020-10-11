//package com.dhht.sld.main.findorder.adapter;
//
//import android.content.Context;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.TextView;
//
//import androidx.annotation.NonNull;
//import androidx.recyclerview.widget.RecyclerView;
//
//import com.dhht.sld.R;
//import com.dhht.sld.base.Common;
//import com.dhht.sld.base.helper.ContextHelper;
//import com.dhht.sld.main.findorder.bean.FindOrderBean;
//
//import java.util.ArrayList;
//import java.util.List;
//
//import butterknife.BindView;
//
//public class FindOrderAdapter extends RecyclerView.Adapter<FindOrderAdapter.ViewHolder> {
//    private List<FindOrderBean.ResData> data;
//    private Context context = ContextHelper.getInstance().getApplicationContext();
//
//    public FindOrderAdapter() {
//        data = new ArrayList<>();
//    }
//
//    public void setData(List<FindOrderBean.ResData> data) {
//        this.data = data;
//    }
//
//    /**
//     */
//    @NonNull
//    @Override
//    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
//        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.view_list_find_order,parent,false);
//        ViewHolder holder = new ViewHolder(v);
//        return holder;
//    }
//
//    /**
//     *
//     */
//    @Override
//    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
//        if (position >= data.size()) return;
//        FindOrderBean value = data.get(position);
//
//        holder.articleName.setText(value.article_name);
//        holder.orderTime.setText(value.add_time);
//        holder.orderStartAddress.setText(value.start_address);
//        holder.orderEndAddress.setText(value.end_address);
//        if(value.is_status == 1){
//            //状态 -1取消 0待付款 1待取货 2带送货 3完成
//            switch (value.status)
//            {
//                case -1:
//                    holder.orderStatus.setText("已取消");
//                    holder.orderStatus.setTextColor(context.getResources().getColor(R.color.gray));
//                    break;
//                case 0:
//                    holder.orderStatus.setText("待付款");
//                    holder.orderStatus.setTextColor(context.getResources().getColor(R.color.appTheme));
//                    break;
//                case 1:
//                    holder.orderStatus.setText("待取货");
//                    holder.orderStatus.setTextColor(context.getResources().getColor(R.color.appTheme));
//                    break;
//                case 2:
//                    holder.orderStatus.setText("待送货");
//                    holder.orderStatus.setTextColor(context.getResources().getColor(R.color.red));
//                    break;
//                case 3:
//                    holder.orderStatus.setText("已完成");
//                    holder.orderStatus.setTextColor(context.getResources().getColor(R.color.green));
//                    break;
//                default:
//                    holder.orderStatus.setText("其他");
//                    break;
//            }
//        }else{
//            holder.orderStatus.setText("已取消");
//        }
//
//        holder.itemView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                listener.onClick(value.find_order_id,value.user_help_order_id,value.status);
//            }
//        });
//    }
//
//    @Override
//    public int getItemCount() {
//        return data.size();
//    }
//
//    /**
//     * 内部Holder
//     */
//    static class ViewHolder extends RecyclerView.ViewHolder {
//        @BindView(R.id.article_name) TextView articleName;
//        @BindView(R.id.order_status) TextView orderStatus;
//        @BindView(R.id.order_time) TextView orderTime;
//        @BindView(R.id.order_start_address) TextView orderStartAddress;
//        @BindView(R.id.order_end_address) TextView orderEndAddress;
//
//        public ViewHolder(View view) {
//            super(view);
//            Common.bindButterKnifeView(this,view);
//        }
//    }
//
//    // item点击回调
//    private OnItemClickListener listener;
//    public interface OnItemClickListener {
//        void onClick(int find_order_id,int user_help_order_id, int status);
//    }
//    public void setOnItemClickListener(OnItemClickListener listener) {
//        this.listener = listener;
//    }
//}
