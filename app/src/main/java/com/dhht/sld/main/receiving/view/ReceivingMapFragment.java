package com.dhht.sld.main.receiving.view;

import  android.util.Log;
import android.view.View;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.CoordinateConverter;
import com.amap.api.location.DPoint;
import com.amap.api.maps.AMapUtils;
import com.amap.api.maps.MapView;
import com.amap.api.maps.UiSettings;
import com.amap.api.maps.model.LatLng;
import com.dhht.sld.R;
import com.dhht.sld.base.inject.BindEventBusInject;
import com.dhht.sld.base.inject.ViewInject;
import com.dhht.sld.main.home.bean.MarkerBean;
import com.dhht.sld.main.home.event.MarkerEvent;
import com.dhht.sld.main.home.util.routeplan.RoutePlanHelp;
import com.dhht.sld.base.BaseMapFragment;
import com.dhht.sld.main.receiving.contract.ReceivingMapContract;
import com.dhht.sld.main.receiving.presenter.ReceivingMapPresenter;
import com.dhht.sld.utlis.AMapServicesUtil;
import com.dhht.sld.utlis.IconFontView;
import com.dhht.sld.utlis.LocationUtli;
import com.qmuiteam.qmui.util.QMUIStatusBarHelper;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.text.DecimalFormat;

import butterknife.BindView;
import butterknife.OnClick;

@BindEventBusInject
@ViewInject(mainLayoutId = R.layout.fragment_receiving_map)
public class ReceivingMapFragment extends BaseMapFragment implements ReceivingMapContract.IView {
    @BindView(R.id.base_receiving_map)
    MapView mMapView;
    @BindView(R.id.go_back)
    IconFontView goBack;
    private LatLng myLatLng;
    private LatLng centerLatLng;
    private RoutePlanHelp routePlan;
    private int find_order_id = 0;
    private ReceivingMapPresenter presenter;
    private MarkerBean.Marker marker;
    private int status = 1;
    private boolean initLocation=false;
    private int statusBarHeight=0;
    @Override
    public void beforeBindViewMap() {
        presenter = new ReceivingMapPresenter(this);
        baseMapView = mMapView;
    }

    @Override
    public void afterBindViewMap() {
        goBack.setPadding(0, statusBarHeight, 0, 0);
        // 自己位置样式
        AMapServicesUtil.setMyLocalStyle(aMap);
        setMap();
    }
    public void setGoBackHeight(int height){
        this.statusBarHeight=height;
    }
    @Subscribe(threadMode = ThreadMode.MAIN, sticky = true)
    public void onMarkerEvent(MarkerEvent marker) {
        find_order_id = marker.getFind_order_id();
        if (!initLocation){
            initLocation();
            initLocation=true;
        }
    }

    // 初始化定位
    private void initLocation() {
        ((ReceivingOrderActivity) mContext).showLoading("加载中...");
        if (centerLatLng == null || myLatLng == null) {
            LocationUtli location = LocationUtli.getInstance(mActivity).initcontinue();
            location.setLocationResListener(new LocationUtli.OnLocationResListener() {
                @Override
                public void localSuccess(AMapLocation aMapLocation) {
                    myLatLng = new LatLng(aMapLocation.getLatitude(), aMapLocation.getLongitude());
                    if (centerLatLng == null) centerLatLng = myLatLng;
                    if(marker!=null){
                        float distance=0.0f;
                        if(marker.status==1){
                            distance = AMapUtils.calculateLineDistance(
                                    new LatLng(marker.start_latitude, marker.start_longitude),
                                    new LatLng(myLatLng.latitude, myLatLng.longitude));
                        }
                        if (marker.status==2){
                            distance = AMapUtils.calculateLineDistance(
                                    new LatLng(marker.end_latitude, marker.end_longitude),
                                    new LatLng(myLatLng.latitude, myLatLng.longitude));
                        }
                        int random = Math.round(distance);
                        ((ReceivingOrderActivity)mActivity).setDistance(random);
                        if (distance<=100){
                            LocationUtli.getInstance(mActivity).destroyLocation();
                        }
                    }
                    fullData();
                }

                @Override
                public void localFail(int code, String msg) {

                }
            });
        } else {
            fullData();
        }
    }
    boolean isPost=true;//只做一次请求
    // 定位完成后的相关操作
    private void fullData() {
        if(isPost){
            isPost=!isPost;
            presenter.getMarker(find_order_id);
        }
    }

    /**
     * 设置地图其他控件
     */
    private void setMap() {
        UiSettings mUiSettings = aMap.getUiSettings();
        mUiSettings.setZoomControlsEnabled(false); // 是否显示缩放按钮
        mUiSettings.setRotateGesturesEnabled(false); // 是否旋转
        mUiSettings.setTiltGesturesEnabled(false); // 倾斜手势
    }

    @Override
    public void resMarkerInfo(MarkerBean res) {
        ((ReceivingOrderActivity) mContext).hideTipDialog();
        if (res.data != null) {
            marker = res.data;
            routePlay(centerLatLng.latitude,centerLatLng.longitude,marker.start_latitude,marker.start_longitude,1);
            ((ReceivingOrderActivity) mContext).update(marker, marker.status);
        }else{
            getActivity().finish();
        }
    }

    public void routePlay(double start_latitude, double start_longitude, double end_latitude, double end_longitude,int type)
    {
        if (marker == null) return;
        status = type;
        LatLng startLatLng = new LatLng(start_latitude, start_longitude);
        LatLng endLatLng = new LatLng(end_latitude, end_longitude);
        Log.e("debug", start_latitude+"-"+start_longitude+"-"+end_latitude+"-"+end_longitude);
        routePlan = new RoutePlanHelp(mActivity, aMap, startLatLng, endLatLng);
        routePlan.run(1, type);
        AMapServicesUtil.zoomToSpanWithCenter(aMap,new LatLng(marker.end_latitude, marker.end_longitude),centerLatLng);
    }

    @OnClick({R.id.go_back, R.id.go_refresh})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.go_back:
                mActivity.finish();
                break;
            case R.id.go_refresh:
                if (status == 1)
                {
                    routePlay(centerLatLng.latitude,centerLatLng.longitude,marker.start_latitude,marker.start_longitude,1);
                }else if (status == 2){
                    routePlay(marker.start_latitude,marker.start_longitude,marker.end_latitude,marker.end_longitude,2);
                }
                break;
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        LocationUtli.getInstance(mActivity).destroyLocation();
    }
}


