package com.dhht.sld.main.home.extend;

import com.amap.api.maps.AMap;
import com.amap.api.maps.model.CameraPosition;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.Marker;
import com.dhht.sld.main.home.presenter.HelpPeoplePresenter;

import java.util.List;

public class HelpPeopleCameraChange implements AMap.OnCameraChangeListener {
    private static List<Marker> mList;
    private static AMap mAMap;
    private static HelpPeoplePresenter mPresenter;

    private HelpPeopleCameraChange(){}

    public static HelpPeopleCameraChange getInstance(HelpPeoplePresenter presenter, AMap aMap, List<Marker> list)
    {
        mPresenter = presenter;
        mAMap = aMap;
        mList = list;
        return HelpPeopleCameraChangeHolder.INSTANCE;
    }

    private static class HelpPeopleCameraChangeHolder {
        private static final HelpPeopleCameraChange INSTANCE = new HelpPeopleCameraChange();
    }

    @Override
    public void onCameraChange(CameraPosition cameraPosition) {

    }

    @Override
    public void onCameraChangeFinish(CameraPosition cameraPosition) {
        callBack(cameraPosition.target);
        /*
        float zoom = aMap.getCameraPosition().zoom;
        if (zoom < 5) {
            marker.markVisible(false, mList);//不显示
        }
        if (zoom >= 5) {
            marker.markVisible(true, mList);//显示
        }
        */
    }

    /**
     * ------------------------------------------
     * 定义回调
     * ------------------------------------------
     */
    private OnCameraChangeFinish resListener;
    // 设置监听回调
    public void setCameraChangeFinishListener(OnCameraChangeFinish resListener){
        this.resListener = resListener;
    }
    public interface OnCameraChangeFinish{
        void changeFinish(LatLng latLng);
    }
    private void callBack(LatLng latLng){
        if(resListener == null) return;
        resListener.changeFinish(latLng);
    }
}
