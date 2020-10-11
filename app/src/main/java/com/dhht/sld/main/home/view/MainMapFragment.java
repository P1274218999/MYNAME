package com.dhht.sld.main.home.view;

import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.amap.api.location.AMapLocation;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.MapView;
import com.amap.api.maps.UiSettings;
import com.amap.api.maps.model.LatLng;
import com.dhht.sld.MainActivity;
import com.dhht.sld.R;
import com.dhht.sld.base.BaseMapFragment;
import com.dhht.sld.base.inject.ViewInject;
import com.dhht.sld.main.home.bean.MarkerBean;
import com.dhht.sld.main.home.contract.MainMapContract;
import com.dhht.sld.main.home.model.HelpRoutResModel;
import com.dhht.sld.main.home.presenter.FindPeoplePresenter;
import com.dhht.sld.main.home.presenter.HelpPeoplePresenter;
import com.dhht.sld.utlis.AMapServicesUtil;
import com.dhht.sld.utlis.DensityUtil;
import com.dhht.sld.utlis.LocationUtli;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

@ViewInject(mainLayoutId = R.layout.fragment_main_map)
public class MainMapFragment extends BaseMapFragment implements MainMapContract.IView {

    @BindView(R.id.base_map)
    MapView mMapView;
    @BindView(R.id.fragment_relativeLayout)
    RelativeLayout fragmentRelativeLayout;
    @BindView(R.id.location_click)
    ImageView locationClickImg;

    private LatLng myLatLng;
    private LatLng centerLatLng;
    private int type = 0;

    private FindPeoplePresenter findPeoplePresenter;
    private HelpPeoplePresenter helpPeoplePresenter;

    @Override
    public void beforeBindViewMap() {
        baseMapView = mMapView;
    }

    @Override
    public void afterBindViewMap() {
        // 自己位置样式
        AMapServicesUtil.setMyLocalStyle(aMap);
        setMap();
    }

    /**
     * 切换 找人带和帮人带的地图
     *
     * @param position 0找人带 1帮人带
     */
    public void initMapInfo(int position) {
        aMap.clear(true); // 清空地图标注
        type = position;
        MainBottomFragment bottomFragment = (MainBottomFragment) getActivity().getSupportFragmentManager().findFragmentByTag("MainBottomFragment");
        bottomFragment.setMainBottomHeight();

        // 获取当前位置定位
        initLocation();
    }

    // 初始化定位
    private void initLocation() {
        if (centerLatLng == null)
        {
            LocationUtli location = LocationUtli.getInstance(mActivity).init();
            location.setLocationResListener(new LocationUtli.OnLocationResListener() {
                @Override
                public void localSuccess(AMapLocation aMapLocation) {
                    myLatLng = new LatLng(aMapLocation.getLatitude(), aMapLocation.getLongitude());
                    if (centerLatLng == null) centerLatLng = myLatLng;
                    aMap.moveCamera(CameraUpdateFactory.changeLatLng(myLatLng));

                    fullData();

                    String city = aMapLocation.getCity();
                    String address = aMapLocation.getAddress();
                    Log.e("debug", city + address);
                }

                @Override
                public void localFail(int code, String msg) {

                }
            });
        }else{

            fullData();

        }
    }

    // 定位完成后的相关操作
    private void fullData() {
        // 统一移动到当前位置中心
        aMap.moveCamera(CameraUpdateFactory.changeLatLng(myLatLng));
        switch (type) {
            case 0: // 找人带
                findPeoplePresenter = new FindPeoplePresenter(this, mContext, aMap, centerLatLng);
                break;
            case 1: // 帮人带
                helpPeoplePresenter = new HelpPeoplePresenter(this, mContext, aMap, centerLatLng);
                break;
        }
    }

    /**
     * 设置地图其他控件
     */
    private void setMap() {
        UiSettings mUiSettings = aMap.getUiSettings();
        mUiSettings.setZoomControlsEnabled(false);
    }

    // 回到当前位置
    private void setMoveCameraMy() {
        // 统一移动到当前位置中心
        aMap.moveCamera(CameraUpdateFactory.changeLatLng(myLatLng));
    }

    /**
     * 根据bottom view的高度设置地图中心点位置
     */
    public void setMapCenter(int bottomHeight) {
        AMapServicesUtil.setMapCenter(mActivity, baseMapView, bottomHeight, aMap);
    }

    /**
     * 设置点击定位图标的位置
     *
     * @param height
     */
    public void setLocationImgClickPosition(int height) {
        ViewGroup.MarginLayoutParams margin = new ViewGroup.MarginLayoutParams(locationClickImg.getLayoutParams());
        margin.setMargins(DensityUtil.dip2px(mContext, 15), DensityUtil.dip2px(mContext, 15), 0, 0);//在左边距400像素，顶边距10像素的位置显示图片
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(margin);
        locationClickImg.setLayoutParams(layoutParams);
    }

    @Override
    public void resChooseMarker(List<HelpRoutResModel> path, MarkerBean res) {
        MainBottomFragment bottomFragment = (MainBottomFragment) getActivity().getSupportFragmentManager().findFragmentByTag("MainBottomFragment");
        HelpPeopleBottomFragment helpPeopleBottomFragment = (HelpPeopleBottomFragment) bottomFragment.getChildFragmentManager().findFragmentByTag("HelpPeopleBottomFragment");

        if (helpPeopleBottomFragment != null) {
            helpPeopleBottomFragment.showRouteFragment();
            helpPeopleBottomFragment.updateAdapterData(path);
            helpPeopleBottomFragment.updateAddress(res.data.start_address, res.data.article_name,res.data.end_address,res.data.find_order_id);
        }
        bottomFragment.setMainBottomHeight();
    }

    @Override
    public void resFail(String msg) {
        Log.e("debug22", msg);
        ((MainActivity)mContext).showTip(msg);
    }

    @Override
    public void resCameraChange(String msg) {
        MainBottomFragment bottomFragment = (MainBottomFragment) getActivity().getSupportFragmentManager().findFragmentByTag("MainBottomFragment");
        FindPeopleBottomFragment findPeopleBottomFragment = (FindPeopleBottomFragment) bottomFragment.getChildFragmentManager().findFragmentByTag("FindPeopleBottomFragment");
        findPeopleBottomFragment.setQuHuoText(msg);
    }

    @OnClick({R.id.location_click})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.location_click:
                setMoveCameraMy();
                break;
        }
    }

    public void markerBack(){
        aMap.clear(true);

        MainBottomFragment bottomFragment = (MainBottomFragment) getActivity().getSupportFragmentManager().findFragmentByTag("MainBottomFragment");
        HelpPeopleBottomFragment helpPeopleBottomFragment = (HelpPeopleBottomFragment) bottomFragment.getChildFragmentManager().findFragmentByTag("HelpPeopleBottomFragment");
        bottomFragment.setMainBottomHeight();
        helpPeopleBottomFragment.hideRouteFragment();

        new HelpPeoplePresenter(this, mContext, aMap, centerLatLng);
    }


}


