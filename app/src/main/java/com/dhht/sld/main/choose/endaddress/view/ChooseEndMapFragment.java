package com.dhht.sld.main.choose.endaddress.view;

import android.widget.Toast;

import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.MapView;
import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.Marker;
import com.amap.api.maps.model.MarkerOptions;
import com.dhht.sld.R;
import com.dhht.sld.base.inject.BindEventBusInject;
import com.dhht.sld.base.inject.ViewInject;
import com.dhht.sld.main.address.model.AddressLocalData;
import com.dhht.sld.main.home.util.routeplan.RoutePlan;
import com.dhht.sld.base.BaseMapFragment;
import com.dhht.sld.utlis.AMapServicesUtil;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.BindView;

@BindEventBusInject
@ViewInject(mainLayoutId = R.layout.fragment_base_map)
public class ChooseEndMapFragment extends BaseMapFragment {

    @BindView(R.id.base_map)
    MapView baseMap;

    private Marker startMarker;
    private Marker endMarker;

    @Override
    public void beforeBindViewMap() {
        baseMapView = baseMap;
    }

    @Override
    public void afterBindViewMap() {
        setUpMap();
    }

    @Subscribe(sticky = true, threadMode = ThreadMode.MAIN)
    public void getParam(AddressLocalData data)
    {
        LatLng startLatLng = null;
        LatLng endLatLng = null;
        if (data.getLatitude() > 0 && data.getLongitude() > 0)
        {
            startLatLng = new LatLng(data.getLatitude(),data.getLongitude());
            aMap.moveCamera(CameraUpdateFactory.newLatLngZoom(startLatLng,16f));
            addStartMarker(startLatLng);
        }else{
            Toast.makeText(mContext,"请填写起点信息", Toast.LENGTH_SHORT).show();
            return;
        }

        if (data.getEndLatitude() > 0 && data.getEndLongitude() > 0)
        {
            endLatLng = new LatLng(data.getEndLatitude(),data.getEndLongitude());
            aMap.moveCamera(CameraUpdateFactory.newLatLngZoom(endLatLng,16f));
            addEndMarker(endLatLng);
        }else{
            Toast.makeText(mContext,"请填写终点信息", Toast.LENGTH_SHORT).show();
            return;
        }

        play(startLatLng, endLatLng);

    }

    private void play(LatLng startLatLng, LatLng endLatLng)
    {
        aMap.clear();
        // 规划路线
        RoutePlan walkPlan = new RoutePlan(mContext,aMap,startLatLng,endLatLng,2);
        walkPlan.run(1);
    }

    /**
     * 设置一些地图的属性
     */
    private void setUpMap() {
        aMap.getUiSettings().setZoomControlsEnabled(false);
    }

    private void addStartMarker(LatLng startLatLng) {
        if (startMarker == null) {
            startMarker = aMap.addMarker(new MarkerOptions()
                    .position(startLatLng)
                    .anchor(0.5f, 0.5f)
                    .icon(BitmapDescriptorFactory.fromResource(R.mipmap.local_quhuo)));
            startMarker.setZIndex(1);
        }
    }

    private void addEndMarker(LatLng endLatLng) {
        if (endMarker == null) {
            endMarker = aMap.addMarker(new MarkerOptions()
                    .position(endLatLng)
                    .anchor(0.5f, 0.5f)
                    .icon(BitmapDescriptorFactory.fromResource(R.mipmap.local_song)));
            endMarker.setZIndex(1);
        }
    }

    // 设置地图中心点
    public void setMapCenter(int bottomHeight) {
        AMapServicesUtil.setMapCenter(mActivity,baseMapView,bottomHeight,aMap);
    }


}
