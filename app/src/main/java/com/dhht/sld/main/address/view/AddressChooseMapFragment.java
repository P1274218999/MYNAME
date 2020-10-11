package com.dhht.sld.main.address.view;

import android.graphics.Point;
import android.view.View;
import android.view.animation.Interpolator;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.amap.api.location.AMapLocation;
import com.amap.api.maps.AMap;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.MapView;
import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.CameraPosition;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.Marker;
import com.amap.api.maps.model.MarkerOptions;
import com.amap.api.maps.model.animation.Animation;
import com.amap.api.maps.model.animation.TranslateAnimation;
import com.amap.api.services.core.AMapException;
import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.core.PoiItem;
import com.amap.api.services.geocoder.GeocodeResult;
import com.amap.api.services.geocoder.GeocodeSearch;
import com.amap.api.services.geocoder.RegeocodeQuery;
import com.amap.api.services.geocoder.RegeocodeResult;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.dhht.sld.R;
import com.dhht.sld.base.inject.BindEventBusInject;
import com.dhht.sld.base.inject.ViewInject;
import com.dhht.sld.main.address.model.AddressLocalData;
import com.dhht.sld.base.BaseMapFragment;
import com.dhht.sld.utlis.AMapPoiSearchUtil;
import com.dhht.sld.utlis.AMapServicesUtil;
import com.dhht.sld.utlis.DensityUtil;
import com.dhht.sld.utlis.IconFontView;
import com.dhht.sld.utlis.LocationUtli;
import com.tamsiree.rxkit.view.RxToast;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

@BindEventBusInject
@ViewInject(mainLayoutId = R.layout.fragment_select_address)
public class AddressChooseMapFragment extends BaseMapFragment implements GeocodeSearch.OnGeocodeSearchListener {

    @BindView(R.id.base_map)
    MapView baseMap;
    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;

    private LatLng myLatLng;
    private Marker centerMarker;
    private boolean isItemClickAction;

    private GeocodeSearch geoCoderSearch;
    private LatLonPoint searchLatlonPoint;
    private List<PoiItem> resultData = new ArrayList<>();
    private AMapPoiSearchUtil searchUtil;
    private int type;
    private BaseQuickAdapter<PoiItem, BaseViewHolder> quickAdapter;
    private int selectedPosition = 0;

    @Override
    public void beforeBindViewMap() {
        baseMapView = baseMap;
    }

    @Override
    public void afterBindViewMap() {
        geoCoderSearch = new GeocodeSearch(mContext);
        geoCoderSearch.setOnGeocodeSearchListener(this);
        setUpMap();
        AMapServicesUtil.setMyLocalStyle(aMap);
        AMapPoiSearchUtil();
        cameraChangeListener(); // 初始化监听地图移动事件
        initRecyclerView(); // 初始化RecyclerView
        initLocal();

    }

    @Subscribe(sticky = true, threadMode = ThreadMode.MAIN)
    public void getParam(AddressLocalData data) {
        type = data.getType();
    }

    /**
     * 设置一些地图的属性
     */
    private void setUpMap() {
        aMap.getUiSettings().setZoomControlsEnabled(false);
//        aMap.getUiSettings().setMyLocationButtonEnabled(true);// 设置默认定位按钮是否显示
//        aMap.setMyLocationEnabled(true);// 设置为true表示显示定位层并可触发定位，false表示隐藏定位层并不可触发定位，默认是false
        aMap.setMyLocationType(aMap.LOCATION_TYPE_LOCATE);
    }

    private void AMapPoiSearchUtil() {
        searchUtil = AMapPoiSearchUtil.getInstance(mContext);
        searchUtil.setOnSearchedListener(new AMapPoiSearchUtil.OnSearchedListener() {
            @Override
            public void onSuccess(List<PoiItem> poiItems) {
                updateListView(poiItems);
            }
        });
    }

    private void initRecyclerView() {
        quickAdapter = new BaseQuickAdapter<PoiItem, BaseViewHolder>(R.layout.view_list_addres_choose_map_result) {
            @Override
            protected void convert(BaseViewHolder holder, PoiItem poiItem) {
                holder.setText(R.id.text_title, poiItem.getTitle());
                TextView textSubTitle = holder.getView(R.id.text_title_sub);
                IconFontView iconCheck = holder.getView(R.id.icon_check);
                textSubTitle.setText(poiItem.getCityName() + poiItem.getAdName() + poiItem.getSnippet());
                iconCheck.setVisibility(holder.getLayoutPosition() == selectedPosition ? View.VISIBLE : View.INVISIBLE);
                textSubTitle.setVisibility((holder.getLayoutPosition() == 0 && poiItem.getPoiId().equals("regeo")) ? View.GONE : View.VISIBLE);
                holder.getView(R.id.address_choose_layout).setOnClickListener(v -> {
                    if (type == 1) {
                        EventBus.getDefault().postSticky(AddressLocalData.getInstance().setStartAll(
                                poiItem.getProvinceName() + poiItem.getCityName(),
                                poiItem.getAdName() + poiItem.getSnippet() + " " + poiItem.getTitle(),
                                poiItem.getLatLonPoint().getLatitude(),
                                poiItem.getLatLonPoint().getLongitude()));
                    } else {
                        EventBus.getDefault().postSticky(AddressLocalData.getInstance().setEndAll(
                                poiItem.getProvinceName() + poiItem.getCityName(),
                                poiItem.getAdName() + poiItem.getSnippet() + " " + poiItem.getTitle(),
                                poiItem.getLatLonPoint().getLatitude(),
                                poiItem.getLatLonPoint().getLongitude()));
                    }
                    ((AddressActivity) mContext).chanceFragment(1);
                });

            }
        };

        LinearLayoutManager manager = new LinearLayoutManager(mActivity);
        recyclerView.setLayoutManager(manager);
        recyclerView.setAdapter(quickAdapter);

    }

    /**
     * 进入页面第一次定位
     */
    private void initLocal() {
        LocationUtli initLocal = LocationUtli.getInstance(mActivity).init();
        initLocal.setLocationResListener(new LocationUtli.OnLocationResListener() {
            @Override
            public void localSuccess(AMapLocation aMapLocation) {
                // 统一移动到当前位置中心
                myLatLng = new LatLng(aMapLocation.getLatitude(), aMapLocation.getLongitude());
                // 设置当天位置的LatLng
                searchLatlonPoint = new LatLonPoint(myLatLng.latitude, myLatLng.longitude);
                aMap.moveCamera(CameraUpdateFactory.newLatLngZoom(myLatLng, 16f));
                addMarkerInScreenCenter(null);
            }

            @Override
            public void localFail(int code, String msg) {

            }
        });
    }

    /**
     * 地图移动监听
     */
    private void cameraChangeListener() {
        aMap.setOnCameraChangeListener(new AMap.OnCameraChangeListener() {
            @Override
            public void onCameraChangeFinish(CameraPosition cameraPosition) {
                if (!isItemClickAction) {
                    ((AddressActivity) mActivity).showLoading();
                    geoAddress();
                    startJumpAnimation();
                }
                searchLatlonPoint = new LatLonPoint(
                        cameraPosition.target.latitude,
                        cameraPosition.target.longitude);
                isItemClickAction = false;
            }

            @Override
            public void onCameraChange(CameraPosition cameraPosition) {

            }
        });
    }

    private void addMarkerInScreenCenter(LatLng locationLatLng) {
        if (centerMarker == null) {
            LatLng latLng = aMap.getCameraPosition().target;
            Point screenPosition = aMap.getProjection().toScreenLocation(latLng);
            centerMarker = aMap.addMarker(new MarkerOptions()
                    .anchor(0.5f, 0.5f)
                    .icon(BitmapDescriptorFactory.fromResource(R.mipmap.local_start)));
            //设置Marker在屏幕上,不跟随地图移动
            centerMarker.setPositionByPixels(screenPosition.x, screenPosition.y);
            centerMarker.setZIndex(1);
            startJumpAnimation();
        }
    }

    /**
     * 屏幕中心marker 跳动
     */
    public void startJumpAnimation() {
        if (centerMarker != null) {
            //根据屏幕距离计算需要移动的目标点
            final LatLng latLng = centerMarker.getPosition();
            Point point = aMap.getProjection().toScreenLocation(latLng);
            point.y -= DensityUtil.dip2px(mContext, 125);
            LatLng target = aMap.getProjection()
                    .fromScreenLocation(point);
            //使用TranslateAnimation,填写一个需要移动的目标点
            Animation animation = new TranslateAnimation(target);
            animation.setInterpolator(new Interpolator() {
                @Override
                public float getInterpolation(float input) {
                    // 模拟重加速度的interpolator
                    if (input <= 0.5) {
                        return (float) (0.5f - 2 * (0.5 - input) * (0.5 - input));
                    } else {
                        return (float) (0.5f - Math.sqrt((input - 0.5f) * (1.5f - input)));
                    }
                }
            });
            //整个移动所需要的时间
            animation.setDuration(600);
            //设置动画
            centerMarker.setAnimation(animation);
            //开始动画
            centerMarker.startAnimation();
        }
    }

    /**
     * 响应逆地理编码
     */
    private void geoAddress() {
        if (searchLatlonPoint != null) {
            RegeocodeQuery query = new RegeocodeQuery(searchLatlonPoint, 200, GeocodeSearch.AMAP);// 第一个参数表示一个Latlng，第二参数表示范围多少米，第三个参数表示是火系坐标系还是GPS原生坐标系
            geoCoderSearch.getFromLocationAsyn(query);
        }
    }

    /**
     * 更新列表中的item
     *
     * @param poiItems
     */
    private void updateListView(List<PoiItem> poiItems) {
        resultData.clear();
        quickAdapter.replaceData(poiItems);
    }

    @Override
    public void onRegeocodeSearched(RegeocodeResult result, int rCode) {
        ((AddressActivity) mActivity).hideTipDialog();
        if (rCode == AMapException.CODE_AMAP_SUCCESS) {
            if (result != null && result.getRegeocodeAddress() != null && result.getRegeocodeAddress().getFormatAddress() != null) {
                /*
                String address = result.getRegeocodeAddress().getProvince()
                        + result.getRegeocodeAddress().getCity()
                        + result.getRegeocodeAddress().getDistrict()
                        + result.getRegeocodeAddress().getTownship();
                */
                // 搜索地址
                AMapPoiSearchUtil.getInstance(mContext).doSearch("", searchLatlonPoint);
            }
        } else {
            RxToast.error("error code is " + rCode);
        }
    }

    @Override
    public void onGeocodeSearched(GeocodeResult geocodeResult, int i) {

    }

    @OnClick(R.id.go_back)
    public void onClick() {

    }
}
