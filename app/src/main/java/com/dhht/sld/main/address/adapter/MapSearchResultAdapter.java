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

public class MapSearchResultAdapter extends RecyclerView.Adapter<MapSearchResultAdapter.ViewHolder> {
    private List<PoiItem> data;

    private int selectedPosition = 0;

    public MapSearchResultAdapter() {
        data = new ArrayList<>();
    }

    public void setData(List<PoiItem> data) {
        this.data = data;
    }

    public void setSelectedPosition(int selectedPosition) {
        this.selectedPosition = selectedPosition;
    }

    public int getSelectedPosition() {
        return selectedPosition;
    }

    /**
     */
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.view_list_addres_choose_map_result,parent,false);
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
        holder.iconCheck.setVisibility(position == selectedPosition ? View.VISIBLE : View.INVISIBLE);
        holder.textSubTitle.setVisibility((position == 0 && poiItem.getPoiId().equals("regeo")) ? View.GONE : View.VISIBLE);

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


    // item点击回调
    public interface OnItemClickListener {
        void onClick(String province, String area, double latitude, double longitude);
    }
    private OnItemClickListener listener;
    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
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
}
