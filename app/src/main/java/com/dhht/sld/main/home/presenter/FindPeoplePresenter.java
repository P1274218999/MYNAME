package com.dhht.sld.main.home.presenter;

import android.content.Context;
import android.graphics.Point;
import android.util.Log;

import com.amap.api.maps.AMap;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.Marker;
import com.amap.api.maps.model.MarkerOptions;
import com.amap.api.maps.model.animation.Animation;
import com.amap.api.maps.model.animation.ScaleAnimation;
import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.geocoder.GeocodeResult;
import com.amap.api.services.geocoder.GeocodeSearch;
import com.amap.api.services.geocoder.RegeocodeQuery;
import com.amap.api.services.geocoder.RegeocodeResult;
import com.dhht.http.result.IResult;
import com.dhht.sld.MainActivity;
import com.dhht.sld.R;
import com.dhht.sld.base.BasePresenter;
import com.dhht.sld.base.DoHttpTask;
import com.dhht.sld.main.address.model.AddressLocalData;
import com.dhht.sld.main.home.bean.MarkersBean;
import com.dhht.sld.main.home.contract.MainMapContract;
import com.dhht.sld.main.home.model.MainHttpTask;
import com.dhht.sld.main.home.extend.FindPeopleCameraChange;
import com.tamsiree.rxkit.view.RxToast;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

/**
 * 帮人带地图相关数据
 */
public class FindPeoplePresenter extends BasePresenter<MainMapContract.IView> implements MainMapContract.IFindPresenter {

    private Context mContext;
    private AMap mAMap;
    private LatLng centerLatLng;
    private Marker centerMarker;
    private List<LatLng> pointList = new ArrayList<LatLng>();
    private List<Marker> markerList = new ArrayList<Marker>();

    public FindPeoplePresenter(MainMapContract.IView view, Context context,AMap aMap, LatLng latLng)
    {
        super(view);
        mContext     = context;
        mAMap        = aMap;
        centerLatLng = latLng;

        init();
    }

    /**
     * 保持Marker在中心
     * @param latLng
     */
    public void setPositionByPixels(LatLng latLng) {
        Point point = mAMap.getProjection().toScreenLocation(latLng);
        centerMarker.setPositionByPixels(point.x, point.y);
        centerLatLng = latLng;
    }

    private void init()
    {
        // 设置中心Marker
        setCenterMarker();
        // 设置缩放级别
        mAMap.moveCamera(CameraUpdateFactory.zoomTo(17));
        // 创建Marker
        getMarker(15, centerLatLng.latitude, centerLatLng.longitude);
        //绑定地图移动后停止
        setCameraChangeFinish();
    }

    // 监听地图移动后停止
    private void setCameraChangeFinish() {
        FindPeopleCameraChange findPeopleCameraChange = FindPeopleCameraChange.getInstance(this,mAMap,markerList);
        mAMap.setOnCameraChangeListener(findPeopleCameraChange);
        findPeopleCameraChange.setCameraChangeFinishListener(new FindPeopleCameraChange.OnCameraChangeFinish() {

            @Override
            public void CameraChange(LatLng target) {
                getView().resCameraChange("正在获取取货位置...");
            }

            @Override
            public void CameraChangeFinish(LatLng latLng) {
                GeocodeSearch geocodeSearch = new GeocodeSearch(mContext);
                RegeocodeQuery query = new RegeocodeQuery(new LatLonPoint(latLng.latitude, latLng.longitude), 200,GeocodeSearch.AMAP);
                geocodeSearch.getFromLocationAsyn(query);
                geocodeSearch.setOnGeocodeSearchListener(new GeocodeSearch.OnGeocodeSearchListener() {
                    @Override
                    public void onRegeocodeSearched(RegeocodeResult regeocodeResult, int i) {
                        String province = regeocodeResult.getRegeocodeAddress().getProvince() + regeocodeResult.getRegeocodeAddress().getCity();
                        String area = regeocodeResult.getRegeocodeAddress().getFormatAddress().replace(province,"");
                        double latitude = regeocodeResult.getRegeocodeQuery().getPoint().getLatitude();
                        double longitude = regeocodeResult.getRegeocodeQuery().getPoint().getLongitude();
                        EventBus.getDefault().postSticky(AddressLocalData.getInstance().setStartAll(province,area,latitude,longitude));
                        // 返回View修改去获得地址
                        getView().resCameraChange(area.replace(regeocodeResult.getRegeocodeAddress().getDistrict()+regeocodeResult.getRegeocodeAddress().getNeighborhood()+regeocodeResult.getRegeocodeAddress().getTownship(),""));

                    }

                    @Override
                    public void onGeocodeSearched(GeocodeResult geocodeResult, int i) {

                    }
                });
            }
        });
    }

    private void setCenterMarker()
    {
        centerMarker = mAMap.addMarker(new MarkerOptions()
                .anchor(0.5f, 0.92f)
                .icon(BitmapDescriptorFactory.fromResource(R.mipmap.local_quhuo))
                .position(centerLatLng));

        // 设置动画
//        animationCenterMarker(centerMarker);
    }

    // 中心点的Marker动画
    private void animationCenterMarker(Marker marker)
    {
        Animation markerAnimation = new ScaleAnimation(0, 1, 0, 1); //初始化生长效果动画
        markerAnimation.setDuration(300);  //设置动画时间 单位毫秒
        marker.setAnimation(markerAnimation);
        marker.startAnimation();
    }

    // 获取Marker
    private void getMarker(int pageSize,double latitude, double longitude)
    {
        submitTask(new DoHttpTask<MarkersBean>() {
            @Override
            public IResult<MarkersBean> onBackground() {
                return new MainHttpTask<MarkersBean>().getFindPeopleMarker(pageSize,latitude,longitude);
            }

            @Override
            public void onSuccess(IResult<MarkersBean> t) {
                MarkersBean res = t.data();
                if (res.code > 0)
                {
                    createMarker(res.data);
                }else {
                    RxToast.error(res.msg);
                }
            }

        });
    }

    // 创建Marker
    private void createMarker(ArrayList<MarkersBean.MarkerList> data)
    {
        if (data == null) return;
        for (int i = 0; i < data.size(); i++) {

            double latitude = data.get(i).start_latitude;
            double longitude = data.get(i).start_longitude;
            LatLng latLng = new LatLng(latitude, longitude);
            pointList.add(latLng);

            MarkerOptions markerOption = new MarkerOptions();
            markerOption.icon(BitmapDescriptorFactory.fromResource(R.drawable.default_user_img));
            markerOption.position(latLng);
            Marker marker = mAMap.addMarker(markerOption);
            markerList.add(marker);
            // 设置动画
            animationMarker(marker);
        }
    }

    // marker动画
    private void animationMarker(Marker marker)
    {
        Animation markerAnimation = new ScaleAnimation(0, 1, 0, 1); //初始化生长效果动画
        markerAnimation.setDuration(1000);  //设置动画时间 单位毫秒
        marker.setAnimation(markerAnimation);
        marker.startAnimation();
    }
}
