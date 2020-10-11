package com.dhht.sld.main.home.extend;

import android.content.Context;
import com.amap.api.maps.AMap;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.Marker;
import com.dhht.sld.MainActivity;
import com.dhht.sld.main.home.util.routeplan.RoutePlanHelp;
import com.tamsiree.rxkit.RxDataTool;

public class HelpMarkerClick implements AMap.OnMarkerClickListener {

    private Context context;
    private AMap aMap;
    private LatLng centerLatLng;
    private RoutePlanHelp routePlan;
    public HelpMarkerClick(Context context, AMap aMap, LatLng centerLatLng){
        this.centerLatLng = centerLatLng;
        this.context = context;
        this.aMap = aMap;
    }
    @Override
    public boolean onMarkerClick(Marker marker) {
        if (RxDataTool.isNullString(marker.getTitle())){
            return false;
        }
        listener.onResult(Integer.parseInt(marker.getTitle()));
        aMap.setOnCameraChangeListener(null);
        ((MainActivity)context).showLoading("加载中...");
        return true; //返回 false，除定义的操作之外，默认操作也将会被执行
    }
    // 回调
    private OnMarkerClickListener listener;
    public interface OnMarkerClickListener {
        void onResult(int id);
    }
    public void setRouteResultListener(OnMarkerClickListener listener) {
        this.listener = listener;
    }
}
