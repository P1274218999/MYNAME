package com.dhht.sld.main.home.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;

import androidx.annotation.NonNull;

import com.amap.api.maps.AMap;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.Marker;
import com.dhht.sld.R;
import com.dhht.sld.utlis.view.BubbleView;


public class MInfoWindowAdapter implements AMap.InfoWindowAdapter,View.OnClickListener {

    private Context context;
    private String agentName;
    private String snippet;
    private LatLng latLng;

    public MInfoWindowAdapter(Context context) {
        this.context = context;
    }

    @Override
    public View getInfoWindow(Marker marker) {
//        initData(marker);
//        View view = initView();
        View view = new BubbleView(context);

        return view;
    }

    @Override
    public View getInfoContents(Marker marker) {
        return null;
    }

    public void render(Marker marker, View view) {

    }

    private void initData(Marker marker) {
        latLng = marker.getPosition();
        snippet = marker.getSnippet();
        agentName = marker.getTitle();
    }


    @SuppressLint("StringFormatInvalid")
    @NonNull
    private View initView() {
        View view = LayoutInflater.from(context).inflate(R.layout.view_gaode_map_infowindow, null);

        return view;
    }

    /**
     * Called when a view has been clicked.
     *
     * @param v The view that was clicked.
     */
    @Override
    public void onClick(View v) {

    }
}
