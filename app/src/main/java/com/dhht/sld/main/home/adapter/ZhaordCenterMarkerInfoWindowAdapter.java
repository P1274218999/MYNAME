package com.dhht.sld.main.home.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;

import androidx.annotation.NonNull;

import com.amap.api.maps.AMap;
import com.amap.api.maps.model.Marker;
import com.dhht.sld.R;
import com.dhht.sld.utlis.view.BubbleView;

public class ZhaordCenterMarkerInfoWindowAdapter implements AMap.InfoWindowAdapter {

    private Context context;
    public ZhaordCenterMarkerInfoWindowAdapter(Context context) {
        this.context = context;
    }

    @Override
    public View getInfoWindow(Marker marker) {
        View view = new BubbleView(context);
        return view;
    }

    @Override
    public View getInfoContents(Marker marker) {

        return null;
    }


    @SuppressLint("StringFormatInvalid")
    @NonNull
    private View initView() {
        View view = LayoutInflater.from(context).inflate(R.layout.view_gaode_map_infowindow, null);

        return view;
    }
}
