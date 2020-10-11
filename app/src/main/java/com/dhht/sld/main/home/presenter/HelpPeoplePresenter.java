package com.dhht.sld.main.home.presenter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;

import com.amap.api.maps.AMap;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.Marker;
import com.amap.api.maps.model.MarkerOptions;
import com.amap.api.maps.model.animation.Animation;
import com.amap.api.maps.model.animation.ScaleAnimation;
import com.dhht.http.result.IResult;
import com.dhht.sld.MainActivity;
import com.dhht.sld.R;
import com.dhht.sld.base.BasePresenter;
import com.dhht.sld.base.DoHttpTask;
import com.dhht.sld.main.home.model.MainHttpTask;
import com.dhht.sld.main.home.bean.MarkerBean;
import com.dhht.sld.main.home.bean.MarkersBean;
import com.dhht.sld.main.home.contract.MainMapContract;
import com.dhht.sld.main.home.extend.HelpMarkerClick;
import com.dhht.sld.main.home.extend.HelpPeopleCameraChange;
import com.dhht.sld.main.home.model.HelpRoutResModel;
import com.dhht.sld.main.home.util.routeplan.RoutePlanHelp;
import com.dhht.sld.utlis.AMapServicesUtil;
import com.tamsiree.rxkit.view.RxToast;

import java.util.ArrayList;
import java.util.List;

/**
 * 帮人带地图相关数据
 */
public class HelpPeoplePresenter extends BasePresenter<MainMapContract.IView> {

    private Context mContext;
    private AMap mAMap;
    private LatLng centerLatLng;
    private List<LatLng> pointList = new ArrayList<LatLng>();
    private List<Marker> markerList = new ArrayList<Marker>();
    private RoutePlanHelp routePlan;

    public HelpPeoplePresenter(MainMapContract.IView view, Context context, AMap aMap, LatLng latLng)
    {
        super(view);
        mContext     = context;
        mAMap        = aMap;
        centerLatLng = latLng;
        init();
    }

    private void init()
    {
        // 设置缩放级别
        mAMap.moveCamera(CameraUpdateFactory.zoomTo(17));
        // 创建Marker
        getHelpMarker(15,centerLatLng.latitude,centerLatLng.longitude);
        //绑定移动地图事件
        setCameraChangeFinish();
        setMakerClick();
    }

    // 监听地图移动后停止
    private void setCameraChangeFinish() {
        HelpPeopleCameraChange helpPeopleCameraChange = HelpPeopleCameraChange.getInstance(this,mAMap,markerList);
        mAMap.setOnCameraChangeListener(helpPeopleCameraChange);
        helpPeopleCameraChange.setCameraChangeFinishListener(new HelpPeopleCameraChange.OnCameraChangeFinish() {
            @Override
            public void changeFinish(LatLng latLng) {
                getHelpMarker(15, latLng.latitude, latLng.longitude);
            }
        });
    }

    private void setMakerClick()
    {
        HelpMarkerClick helpMarkerClick = new HelpMarkerClick(mContext,mAMap,centerLatLng);
        mAMap.setOnMarkerClickListener(helpMarkerClick);

        helpMarkerClick.setRouteResultListener(new HelpMarkerClick.OnMarkerClickListener() {
            @Override
            public void onResult(int id) {

                submitTask(new DoHttpTask<MarkerBean>() {
                    @Override
                    public IResult<MarkerBean> onBackground() {
                        return new MainHttpTask<MarkerBean>().getHelpMarkerOne(id);
                    }

                    @Override
                    public void onSuccess(IResult<MarkerBean> t) {
                        MarkerBean markerBean = t.data();
                        setRoute(markerBean);
                    }
                });
            }
        });
    }

    // 获取Marker
    private void getHelpMarker(int pageSize, double latitude, double longitude)
    {
        submitTask(new DoHttpTask<MarkersBean>() {
            @Override
            public IResult<MarkersBean> onBackground() {
                return new MainHttpTask<MarkersBean>().getHelpPeopleMarker(pageSize,latitude,longitude);
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

        mAMap.clear(true);

        for (int i = 0; i < data.size(); i++) {
            double latitude = data.get(i).start_latitude;
            double longitude = data.get(i).start_longitude;
            LatLng latLng = new LatLng(latitude, longitude);
            pointList.add(latLng);

            MarkerOptions markerOption = new MarkerOptions();
            View view = LayoutInflater.from(mContext).inflate(R.layout.view_help_people, null);
            markerOption.icon(BitmapDescriptorFactory.fromView(view));
            markerOption.position(latLng);
            markerOption.title(data.get(i).id+"");
            markerOption.snippet(data.get(i).start_address);
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
        markerAnimation.setDuration(800);  //设置动画时间 单位毫秒
        marker.setAnimation(markerAnimation);
        marker.startAnimation();
    }

    private void setRoute(MarkerBean res)
    {
        if (res.data == null)
        {
            getView().resFail(res.msg);
            return;
        }

        MarkerBean.Marker marker = res.data;
        LatLng startLatLng = new LatLng(marker.start_latitude,marker.start_longitude);
        LatLng endLatLng = new LatLng(marker.end_latitude,marker.end_longitude);
        long distance = AMapServicesUtil.measureDistance(centerLatLng,startLatLng);

        routePlan = new RoutePlanHelp(mContext,mAMap,centerLatLng,startLatLng,startLatLng);
        if (distance >= 2000)
        {
            // 驾驶规划
            routePlan.run(1,1);
        }else{
            // 步行规划
            routePlan.run(3,1);
        }
        setOnRouteResultListener(res);

    }

    private void setOnRouteResultListener(MarkerBean marker)
    {
        routePlan.setOnRouteResultListener(new RoutePlanHelp.OnRouteResultListener() {
            @Override
            public void onResult(List<HelpRoutResModel> path) {
                ((MainActivity)mContext).hideTipDialog();
                getView().resChooseMarker(path, marker);
            }
        });
    }

}
