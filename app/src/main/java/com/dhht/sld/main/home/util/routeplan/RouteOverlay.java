package com.dhht.sld.main.home.util.routeplan;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.util.Log;

import com.amap.api.maps.AMap;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.model.BitmapDescriptor;
import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.LatLngBounds;
import com.amap.api.maps.model.Marker;
import com.amap.api.maps.model.MarkerOptions;
import com.amap.api.maps.model.Polyline;
import com.amap.api.maps.model.PolylineOptions;
import com.dhht.sld.R;

import java.util.ArrayList;
import java.util.List;

public class RouteOverlay {
    protected List<Marker> stationMarkers = new ArrayList<Marker>();
    protected List<Polyline> allPolyLines = new ArrayList<Polyline>();
    protected Marker startMarker;
    protected Marker endMarker;
    protected LatLng startPoint;
    protected LatLng endPoint;
    protected AMap mAMap;
    private Context mContext;
    private Bitmap startBit, endBit, busBit, walkBit, driveBit;
    protected boolean nodeIconVisible = true;

    public RouteOverlay(Context context) {
        mContext = context;
    }

    /**
     * 去掉walkRouteOverlay上所有的Marker。
     */
    public void removeFromMap() {
        if (startMarker != null) {
            startMarker.remove();

        }
        if (endMarker != null) {
            endMarker.remove();
        }
        for (Marker marker : stationMarkers) {
            marker.remove();
        }
        for (Polyline line : allPolyLines) {
            line.remove();
        }
        destroyBit();
    }

    private void destroyBit() {
        if (startBit != null) {
            startBit.recycle();
            startBit = null;
        }
        if (endBit != null) {
            endBit.recycle();
            endBit = null;
        }
        if (busBit != null) {
            busBit.recycle();
            busBit = null;
        }
        if (walkBit != null) {
            walkBit.recycle();
            walkBit = null;
        }
        if (driveBit != null) {
            driveBit.recycle();
            driveBit = null;
        }
    }

    /**
     * 给起点Marker设置图标，并返回更换图标的图片。如不用默认图片，需要重写此方法。
     * @return 更换的Marker图片。
     * @since V2.1.0
     */
    protected BitmapDescriptor getStartBitmapDescriptor() {
        return BitmapDescriptorFactory.fromResource(R.mipmap.local_start);
    }

    protected BitmapDescriptor getStartBitmapDescriptor(int type) {
        if (type == 1){
            return BitmapDescriptorFactory.fromResource(R.mipmap.local_start);
        }else if (type == 2){
            return BitmapDescriptorFactory.fromResource(R.mipmap.local_quhuo);
        }else{
            return null;
        }
    }

    /**
     * 给终点Marker设置图标，并返回更换图标的图片。如不用默认图片，需要重写此方法。
     * @return 更换的Marker图片。
     * @since V2.1.0
     */
    protected BitmapDescriptor getEndBitmapDescriptor() {
        return BitmapDescriptorFactory.fromResource(R.mipmap.local_quhuo);
    }
    protected BitmapDescriptor getEndBitmapDescriptor(int type) {
        if (type == 1){
            return BitmapDescriptorFactory.fromResource(R.mipmap.local_quhuo);
        }else if (type == 2){
            return BitmapDescriptorFactory.fromResource(R.mipmap.local_song);
        }else{
            return null;
        }
    }

    /**
     * 给公交Marker设置图标，并返回更换图标的图片。如不用默认图片，需要重写此方法。
     * @return 更换的Marker图片。
     * @since V2.1.0
     */
    protected BitmapDescriptor getBusBitmapDescriptor() {
        return BitmapDescriptorFactory.fromResource(R.drawable.anim_heart);
    }

    /**
     * 给步行Marker设置图标，并返回更换图标的图片。如不用默认图片，需要重写此方法。
     * @return 更换的Marker图片。
     * @since V2.1.0
     */
    protected BitmapDescriptor getWalkBitmapDescriptor() {
        return BitmapDescriptorFactory.fromResource(R.drawable.seat_green);
    }

    protected BitmapDescriptor getDriveBitmapDescriptor() {
        return BitmapDescriptorFactory.fromResource(R.drawable.caiwu2);
    }

    protected BitmapDescriptor getRideBitmapDescriptor(){
        return BitmapDescriptorFactory.fromResource(R.drawable.caiwu2);
    }

    protected void addStartAndEndMarker() {
        startMarker = mAMap.addMarker((new MarkerOptions())
                .position(startPoint).icon(getStartBitmapDescriptor())
//                .title("\u8D77\u70B9")
        );
         startMarker.showInfoWindow();

        endMarker = mAMap.addMarker((new MarkerOptions()).position(endPoint)
//                .icon(getEndBitmapDescriptor()).title("\u7EC8\u70B9")
        );

        // mAMap.moveCamera(CameraUpdateFactory.newLatLngZoom(startPoint,getShowRouteZoom()));
    }
    protected void addStartAndEndMarker(int type) {
        startMarker = mAMap.addMarker((new MarkerOptions())
//                .position(startPoint).icon(getStartBitmapDescriptor(type))
//                .title("\u8D77\u70B9"));
              .position(startPoint).icon(getStartBitmapDescriptor(type)));
        endMarker = mAMap.addMarker((new MarkerOptions()).position(endPoint)
//                .icon(getEndBitmapDescriptor(type)).title("\u7EC8\u70B9"));
                .icon(getEndBitmapDescriptor(type)));
    }

    /**
     * 移动镜头到当前的视角。
     * @since V2.1.0
     */
    public void zoomToSpan() {
        if (startPoint != null) {
            if (mAMap == null)
                return;
            try {
//                AMapServicesUtil.zoomToSpanWithCenter(mAMap, new LatLng(startPoint.latitude,startPoint.longitude), new LatLng(endPoint.latitude,endPoint.longitude));
                LatLngBounds bounds = getLatLngBounds();
                mAMap.animateCamera(CameraUpdateFactory
                        .newLatLngBoundsRect(bounds, 350,350,350,350));

            } catch (Throwable e) {
                e.printStackTrace();
            }
        }
    }

    protected LatLngBounds getLatLngBounds() {
        LatLngBounds.Builder b = LatLngBounds.builder();
        b.include(new LatLng(startPoint.latitude, startPoint.longitude));
        b.include(new LatLng(endPoint.latitude, endPoint.longitude));
        for (Polyline polyline : allPolyLines){
            for (LatLng point : polyline.getPoints()){
                b.include(point);
            }
        }
        return b.build();
    }

    /**
     * 路段节点图标控制显示接口。
     * @param visible true为显示节点图标，false为不显示。
     * @since V2.3.1
     */
    public void setNodeIconVisibility(boolean visible) {
        try {
            nodeIconVisible = visible;
            if (this.stationMarkers != null && this.stationMarkers.size() > 0) {
                for (int i = 0; i < this.stationMarkers.size(); i++) {
                    this.stationMarkers.get(i).setVisible(visible);
                }
            }
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }

    protected void addStationMarker(MarkerOptions options) {
        if(options == null) {
            return;
        }

        Log.e("debug", "addStationMarker"+mAMap);
        Marker marker = mAMap.addMarker(options);

        if(marker != null) {
            stationMarkers.add(marker);
        }

    }

    protected void addPolyLine(PolylineOptions options) {
        if(options == null) {
            return;
        }
        Polyline polyline = mAMap.addPolyline(options);
        if(polyline != null) {
            allPolyLines.add(polyline);
        }
    }

    protected float getRouteWidth() {
        return 18f;
    }

    protected int getWalkColor() {
        return Color.parseColor("#679dff");
    }

    /**
     * 自定义路线颜色。
     * return 自定义路线颜色。
     * @since V2.2.1
     */
    protected int getBusColor() {
        return Color.parseColor("#14a268");
    }

    protected int getDriveColor() {
        return Color.parseColor("#03bd74");
    }

    protected int getRideColor(){
        return Color.parseColor("#8fbc21");
    }

    protected int getShowRouteZoom() {
        return 15;
    }
}
