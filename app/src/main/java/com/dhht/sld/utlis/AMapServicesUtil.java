package com.dhht.sld.utlis;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Point;
import android.view.Display;
import android.view.ViewTreeObserver;

import com.amap.api.maps.AMap;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.MapView;
import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.CustomMapStyleOptions;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.LatLngBounds;
import com.amap.api.maps.model.MyLocationStyle;
import com.amap.api.services.core.LatLonPoint;
import com.dhht.sld.R;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class AMapServicesUtil {
    public static int BUFFER_SIZE = 2048;

    public static byte[] inputStreamToByte(InputStream in) throws IOException {

        ByteArrayOutputStream outStream = new ByteArrayOutputStream();
        byte[] data = new byte[BUFFER_SIZE];
        int count = -1;
        while ((count = in.read(data, 0, BUFFER_SIZE)) != -1)
            outStream.write(data, 0, count);

        data = null;
        return outStream.toByteArray();
    }

    public static LatLonPoint convertToLatLonPoint(LatLng latLng) {
        return new LatLonPoint(latLng.latitude, latLng.longitude);
    }

    public static LatLng convertToLatLng(LatLonPoint latLonPoint) {
        return new LatLng(latLonPoint.getLatitude(), latLonPoint.getLongitude());
    }

    public static ArrayList<LatLng> convertArrList(List<LatLonPoint> shapes) {
        ArrayList<LatLng> lineShapes = new ArrayList<LatLng>();
        for (LatLonPoint point : shapes) {
            LatLng latLngTemp = AMapServicesUtil.convertToLatLng(point);
            lineShapes.add(latLngTemp);
        }
        return lineShapes;
    }

    public static Bitmap zoomBitmap(Bitmap bitmap, float res) {
        if (bitmap == null) {
            return null;
        }
        int width, height;
        width = (int) (bitmap.getWidth() * res);
        height = (int) (bitmap.getHeight() * res);
        Bitmap newBmp = Bitmap.createScaledBitmap(bitmap, width, height, true);
        return newBmp;
    }

    public static void zoomToSpanWithCenter(AMap aMap, LatLng latLng, LatLng myLatlng){
        double latA = latLng.latitude;
        double lngA = latLng.longitude;
        double latB = myLatlng.latitude;
        double lngB = myLatlng.longitude;
        LatLngBounds latLngBounds = createBounds(latA,lngA,latB,lngB);
        aMap.animateCamera(CameraUpdateFactory.newLatLngBoundsRect(latLngBounds,350,350,350,350),300L,null);
    }

    /**
     * 根据2个坐标返回一个矩形Bounds
     * 以此来智能缩放地图显示
     */
    public static LatLngBounds createBounds(Double latA,Double lngA,Double latB,Double lngB){
        LatLng northeastLatLng;
        LatLng southwestLatLng;

        Double topLat,topLng;
        Double bottomLat,bottomLng;
        if(latA>=latB){
            topLat=latA;
            bottomLat=latB;
        }else{
            topLat=latB;
            bottomLat=latA;
        }
        if(lngA>=lngB){
            topLng=lngA;
            bottomLng=lngB;
        }else{
            topLng=lngB;
            bottomLng=lngA;
        }
        northeastLatLng=new LatLng(topLat,topLng);
        southwestLatLng=new LatLng(bottomLat,bottomLng);
        return new LatLngBounds(southwestLatLng, northeastLatLng);
    }

    // 设置自定义样式地图
    public static void setMapStyle(Activity activity, AMap aMap) {
        byte[] buffer1 = null;
        byte[] buffer2 = null;
        InputStream is1 = null;
        InputStream is2 = null;
        try {
            is1 = activity.getAssets().open("amap/style/style.data");
            int lenght1 = is1.available();
            buffer1 = new byte[lenght1];
            is1.read(buffer1);
            is2 = activity.getAssets().open("amap/style/style_extra.data");
            int lenght2 = is2.available();
            buffer2 = new byte[lenght2];
            is2.read(buffer2);

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (is1 != null)
                    is1.close();
                if (is2 != null)
                    is2.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        CustomMapStyleOptions customMapStyleOptions = new CustomMapStyleOptions();
        customMapStyleOptions.setStyleData(buffer1);
        customMapStyleOptions.setStyleExtraData(buffer2);

        aMap.setCustomMapStyle(customMapStyleOptions);
    }

    // 自己位置样式
    public static void setMyLocalStyle(AMap aMap) {
        //初始化定位蓝点样式类
        MyLocationStyle myLocationStyle = new MyLocationStyle();
        //连续定位、且将视角移动到地图中心点，定位点依照设备方向旋转，并且会跟随设备移动。（1秒1次定位）如果不设置myLocationType，默认也会执行此种模式。
        myLocationStyle.myLocationType(MyLocationStyle.LOCATION_TYPE_LOCATION_ROTATE_NO_CENTER);
        //设置连续定位模式下的定位间隔，只在连续定位模式下生效，单次定位模式下不会生效。单位为毫秒。
        myLocationStyle.interval(2000);
        // 定位蓝点图标
        myLocationStyle.myLocationIcon(BitmapDescriptorFactory.fromResource(R.drawable.navi_map_gps_locked));
        // 设置圆形区域填充颜色
        myLocationStyle.radiusFillColor(Color.argb(0, 0, 0, 0));
        // 设置圆形区域边框宽度
        myLocationStyle.strokeWidth(0);
        aMap.setMyLocationStyle(myLocationStyle); //
        aMap.setMyLocationEnabled(true); //设置为true表示启动显示设置定位蓝点的Style定位蓝点
    }

    // 根据底部高度设置地图的中心点
    public static void setMapCenter(Activity activity, MapView mapView, int bottomHeight, AMap aMap) {
        Display defaultDisplay = activity.getWindowManager().getDefaultDisplay();
        Point point = new Point();
        defaultDisplay.getSize(point);
        final int screenWidth = point.x;
        final int centerX = screenWidth / 2;
        /**
         * ViewTreeObserver是一种观察者模式，可以通过给view添加布局监听，在布局完成之后进行中心点的设置。
         * 注意设置完之后要remove监听，不然每次布局变换都会被调用
         */
        mapView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                int mMapViewHeight = mapView.getHeight(); // 地图高度
                int centerY = (mMapViewHeight - bottomHeight) / 2;
                aMap.setPointToCenter(centerX, centerY);
                mapView.getViewTreeObserver().removeOnGlobalLayoutListener(this);
            }
        });
    }

    //测量两点的距离
    public static long measureDistance(LatLng fromPoint, LatLng toPoint) {
        double EARTH_RADIUS = 6378137;
        long distance = 0;
        double startLongitude = fromPoint.longitude;
        double startLatitude = fromPoint.latitude;
        double endLongitude = toPoint.longitude;
        double endLatitude = toPoint.latitude;
        double radLatitude1 = startLatitude * Math.PI / 180.0;
        double radLatitude2 = endLatitude * Math.PI / 180.0;
        double a = Math.abs(radLatitude1 - radLatitude2);
        double b = Math.abs(startLongitude * Math.PI / 180.0 - endLongitude * Math.PI / 180.0);
        double s = 2 * Math.asin(Math.sqrt(Math.pow(Math.sin(a / 2), 2)
                + Math.cos(radLatitude1) * Math.cos(radLatitude2) * Math.pow(Math.sin(b / 2), 2)));
        s = s * EARTH_RADIUS;
        distance = Math.round(s * 10000) / 10000; // 返回距离单位是米
        return distance;
    }

}
