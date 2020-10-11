package com.dhht.sld.base;

import android.os.Bundle;

import com.amap.api.maps.AMap;
import com.amap.api.maps.MapView;
import com.dhht.sld.utlis.AMapServicesUtil;
import com.tamsiree.rxkit.view.RxToast;

public abstract class BaseMapFragment extends BaseFragment implements AMap.OnMapLoadedListener
{
    protected MapView baseMapView;
    protected AMap aMap;
    // 先获取地图的View
    protected abstract void beforeBindViewMap();
    // 再初始化其他相关
    protected abstract void afterBindViewMap();

    @Override
    public void afterBindView() {
        beforeBindViewMap();
        if (baseMapView != null) {
            baseMapView.onCreate(new Bundle());
            if (aMap == null) {
                aMap = baseMapView.getMap();
            }
            // 绑定地图加载完成事件
            aMap.setOnMapLoadedListener(this);

            // 设置自定义地图
            AMapServicesUtil.setMapStyle(mActivity,aMap);

        } else {
            RxToast.error("初始化地图失败");
        }
    }

    /**
     * 地图加载完成
     */
    @Override
    public void onMapLoaded() {
        afterBindViewMap();
    }

    @Override
    public void onDestroy()
    {
        super.onDestroy();
        if (baseMapView != null) {
            baseMapView.onDestroy();
        }
    }

    @Override
    public void onResume()
    {
        super.onResume();
        if (baseMapView != null) {
            baseMapView.onResume();
        }
    }

    @Override
    public void onPause()
    {
        super.onPause();
        if (baseMapView != null) {
            baseMapView.onPause();
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState)
    {
        super.onSaveInstanceState(outState);
        if (baseMapView != null) {
            baseMapView.onSaveInstanceState(outState);
        }
    }
}


