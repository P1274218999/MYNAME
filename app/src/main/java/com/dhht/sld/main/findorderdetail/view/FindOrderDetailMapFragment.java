package com.dhht.sld.main.findorderdetail.view;

import android.view.View;

import com.amap.api.location.AMapLocation;
import com.amap.api.maps.MapView;
import com.amap.api.maps.UiSettings;
import com.amap.api.maps.model.LatLng;
import com.dhht.sld.R;
import com.dhht.sld.base.inject.BindEventBusInject;
import com.dhht.sld.base.inject.ViewInject;
import com.dhht.sld.main.findorderdetail.bean.FindOrderDetailBean;
import com.dhht.sld.main.findorderdetail.contract.FindOrderDetailMapContract;
import com.dhht.sld.main.findorderdetail.presenter.FindOrderDetailMapPresenter;
import com.dhht.sld.main.home.event.MarkerEvent;
import com.dhht.sld.main.home.util.routeplan.RoutePlanHelp;
import com.dhht.sld.base.BaseMapFragment;
import com.dhht.sld.utlis.AMapServicesUtil;
import com.dhht.sld.utlis.IconFontView;
import com.dhht.sld.utlis.LocationUtli;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.BindView;
import butterknife.OnClick;

@BindEventBusInject
@ViewInject(mainLayoutId = R.layout.fragment_find_order_detail_map)
public class FindOrderDetailMapFragment extends BaseMapFragment implements FindOrderDetailMapContract.IView {
    @BindView(R.id.find_order_detail_map)
    MapView mMapView;
    @BindView(R.id.go_back)
    IconFontView goBack;

    private LatLng myLatLng;
    private LatLng centerLatLng;
    private RoutePlanHelp routePlan;
    private int user_help_order_id = 0;
    private FindOrderDetailMapPresenter presenter;
    private FindOrderDetailBean.Data data;
    private boolean initLocation=false;
    private int statusBarHeight;

    @Override
    public void beforeBindViewMap() {
        presenter = new FindOrderDetailMapPresenter(this);
        baseMapView = mMapView;
    }

    @Override
    public void afterBindViewMap() {
        goBack.setPadding(0, statusBarHeight, 0, 0);
        // 自己位置样式
        AMapServicesUtil.setMyLocalStyle(aMap);
        setMap();
    }

    @Subscribe(threadMode = ThreadMode.MAIN, sticky = true)
    public void onMarkerEvent(MarkerEvent marker) {
        user_help_order_id = marker.getUser_help_order_id();
        if (!initLocation){
            initLocation();
            initLocation=true;
        }
    }
    public void setGoBackHeight(int height){
        this.statusBarHeight=height;
    }
    // 初始化定位
    private void initLocation() {
        ((FindOrderDetailActivity) mContext).showLoading("加载中...");
        if (centerLatLng == null || myLatLng == null) {
            LocationUtli location = LocationUtli.getInstance(mActivity).init();
            location.setLocationResListener(new LocationUtli.OnLocationResListener() {
                @Override
                public void localSuccess(AMapLocation aMapLocation) {
                    myLatLng = new LatLng(aMapLocation.getLatitude(), aMapLocation.getLongitude());
                    if (centerLatLng == null) centerLatLng = myLatLng;
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

    // 定位完成后的相关操作
    private void fullData() {
        presenter.getHelpOrder(user_help_order_id);
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
    public void resFindOrder(FindOrderDetailBean res) {
        ((FindOrderDetailActivity) mContext).hideTipDialog();
        if (res.data != null) {
            this.data = res.data;
            switch (res.data.status)
            {
                case 1: // 待取货
                    routePlay(centerLatLng.latitude,centerLatLng.longitude,res.data.start_latitude,res.data.start_longitude,1);
                    break;
                case 2: // 待送货
                    routePlay(res.data.start_latitude,res.data.start_longitude,res.data.end_latitude,res.data.end_longitude,2);
                    break;
                case 3: // 已完成
                    routePlay(res.data.start_latitude,res.data.start_longitude,res.data.end_latitude,res.data.end_longitude,2);

                    break;
            }

            ((FindOrderDetailActivity) mContext).update(res.data);
        }else{
            getActivity().finish();
        }
    }

    public void routePlay(double start_latitude, double start_longitude, double end_latitude, double end_longitude,int type)
    {
        if (data == null) return;

        LatLng startLatLng = new LatLng(start_latitude, start_longitude);
        LatLng endLatLng = new LatLng(end_latitude, end_longitude);
        routePlan = new RoutePlanHelp(mActivity, aMap, startLatLng, endLatLng);
        routePlan.run(1, type);
        AMapServicesUtil.zoomToSpanWithCenter(aMap,new LatLng(end_latitude, end_longitude),centerLatLng);
    }

    @OnClick({R.id.go_back, R.id.go_refresh})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.go_back:
                mActivity.finish();
                break;
            case R.id.go_refresh:
                routePlay(data.start_latitude,data.start_longitude,data.end_latitude,data.end_longitude,2);
                break;
        }
    }
}


