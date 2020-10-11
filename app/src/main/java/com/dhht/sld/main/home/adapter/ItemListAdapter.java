package com.dhht.sld.main.home.adapter;

import android.app.Activity;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.dhht.sld.R;
import com.dhht.sld.main.itemdetail.ZhaorenDetailActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class ItemListAdapter extends RecyclerView.Adapter {

    private final ArrayList<JSONObject> mData;
    private final Activity mContent;

    public ItemListAdapter(Activity activity, ArrayList<JSONObject> data)
    {
        mContent = activity;
        mData = data;
    }

    /**
     * 创建View 进行缓存
     * @param parent
     * @param viewType
     * @return
     */
    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View infalte = LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_jiedan2_item, null);
        ItemListViewHolder itemListViewHolder = new ItemListViewHolder(infalte);

        return itemListViewHolder;
    }

    /**
     * 绑定数据
     * @param holder
     * @param position
     */
    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

        JSONObject val = mData.get(position);
        try {
            String from = val.getString("from");
            String name = val.getString("name");
            String img = val.getString("img");
            String time = val.getString("time");
            String desc = val.getString("desc");
            String start = val.getString("start");
            String end = val.getString("end");

            ((ItemListViewHolder)holder).item_list_content_from.setText("来自"+from);
            ((ItemListViewHolder)holder).item_list_content_name.setText(name);
            ((ItemListViewHolder)holder).item_list_content_time.setText(time);
            ((ItemListViewHolder)holder).item_list_content_desc.setText(desc);
            ((ItemListViewHolder)holder).item_list_content_start.setText(start);
            ((ItemListViewHolder)holder).item_list_content_end.setText(end);

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    /**
     * 条目的数量
     * @return
     */
    @Override
    public int getItemCount() {
        return mData.size();
    }


    public class ItemListViewHolder extends RecyclerView.ViewHolder{
        public TextView item_list_content_from;
        public TextView item_list_content_name;
        public ImageView item_list_content_img;
        public TextView item_list_content_time;
        public TextView item_list_content_desc;
        public TextView item_list_content_start;
        public TextView item_list_content_end;

        public ItemListViewHolder(@NonNull View itemView) {
            super(itemView);

            item_list_content_from = itemView.findViewById(R.id.item_list_content_from);
            item_list_content_name = itemView.findViewById(R.id.item_list_content_name);
            item_list_content_img = itemView.findViewById(R.id.item_list_content_img);
            item_list_content_time = itemView.findViewById(R.id.item_list_content_time);
            item_list_content_desc = itemView.findViewById(R.id.item_list_content_desc);
            item_list_content_start = itemView.findViewById(R.id.item_list_content_start);
            item_list_content_end = itemView.findViewById(R.id.item_list_content_end);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // 点击后跳转页面
//                    ZhaorenDetailActivity.start(mContent, item_list_content_from);
                    Intent intent = new Intent(mContent, ZhaorenDetailActivity.class);
                    mContent.startActivity(intent);
                }
            });

        }
    }
}
