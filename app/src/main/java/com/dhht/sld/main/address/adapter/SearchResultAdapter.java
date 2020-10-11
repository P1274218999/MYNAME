package com.dhht.sld.main.address.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.amap.api.services.core.PoiItem;
import com.dhht.sld.R;
import com.dhht.sld.utlis.IconFontView;

import java.util.ArrayList;
import java.util.List;

public class SearchResultAdapter extends RecyclerView.Adapter<SearchResultAdapter.ViewHolder> {
    private List<PoiItem> data;

    public SearchResultAdapter() {
        data = new ArrayList<>();
    }

    public void setData(List<PoiItem> poiItems) {
        if (poiItems == null)
        {
            this.data.clear();
        }else{
            this.data = poiItems;
        }

    }

    /**
     */
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.view_list_addres_choose_search_result,parent,false);
        ViewHolder holder = new ViewHolder(v);
        return holder;
    }

    /**
     *
     */
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        if (position >= data.size()) return;
        PoiItem poiItem = data.get(position);

        holder.textTitle.setText(poiItem.getTitle());
        holder.textSubTitle.setText(poiItem.getCityName() + poiItem.getAdName() + poiItem.getSnippet());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null) {
                    listener.onClick(
                            poiItem.getProvinceName()+poiItem.getCityName(),
                            poiItem.getAdName()+ poiItem.getSnippet() + " " +poiItem.getTitle(),
                            poiItem.getLatLonPoint().getLatitude(),
                            poiItem.getLatLonPoint().getLongitude());
                }
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
        TextView textTitle;
        TextView textSubTitle;
        IconFontView iconCheck;

        public ViewHolder(View view) {
            super(view);
            textTitle = (TextView) view.findViewById(R.id.text_title);
            textSubTitle = (TextView) view.findViewById(R.id.text_title_sub);
            iconCheck = (IconFontView) view.findViewById(R.id.icon_check);
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
