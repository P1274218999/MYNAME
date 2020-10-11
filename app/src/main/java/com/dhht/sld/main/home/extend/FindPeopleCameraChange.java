package com.dhht.sld.main.home.extend;

import com.amap.api.maps.AMap;
import com.amap.api.maps.model.CameraPosition;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.Marker;
import com.dhht.sld.main.home.presenter.FindPeoplePresenter;

import java.util.List;

public class FindPeopleCameraChange implements AMap.OnCameraChangeListener {
    private static List<Marker> mList;
    private static AMap mAMap;
    private static FindPeoplePresenter mPresenter;

    private FindPeopleCameraChange (){}

    public static FindPeopleCameraChange getInstance(FindPeoplePresenter presenter, AMap aMap, List<Marker> list)
    {
        mPresenter = presenter;
        mAMap = aMap;
        mList = list;
        return FindPeopleCameraChangeHolder.INSTANCE;
    }

    private static class FindPeopleCameraChangeHolder {
        private static final FindPeopleCameraChange INSTANCE = new FindPeopleCameraChange();
    }

    @Override
    public void onCameraChange(CameraPosition cameraPosition) {
        listener.CameraChange(cameraPosition.target);
        //保持中心Marker不动
        mPresenter.setPositionByPixels(cameraPosition.target);
    }

    @Override
    public void onCameraChangeFinish(CameraPosition cameraPosition) {
        listener.CameraChangeFinish(cameraPosition.target);

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

    // 设置监听回调
    private OnCameraChangeFinish listener;
    public void setCameraChangeFinishListener(OnCameraChangeFinish resListener){
        this.listener = resListener;
    }
    public interface OnCameraChangeFinish{
        void CameraChangeFinish(LatLng latLng);
        void CameraChange(LatLng target);
    }
}
