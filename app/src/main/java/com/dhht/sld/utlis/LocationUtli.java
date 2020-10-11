package com.dhht.sld.utlis;

import android.app.Activity;
import android.util.Log;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.maps.LocationSource;
import com.qmuiteam.qmui.widget.dialog.QMUIDialog;
import com.qmuiteam.qmui.widget.dialog.QMUIDialogAction;
import com.tamsiree.rxkit.RxLocationTool;
import com.tamsiree.rxkit.view.RxToast;

/**
 * 高德地图定位工具
 */
public class LocationUtli implements AMapLocationListener, LocationSource {

    private static Activity mActivity;
    private OnLocationChangedListener mListener;
    private AMapLocationClient mLocationClient;
    private AMapLocationClientOption mLocationOption;

    private LocationUtli(){}

    public static LocationUtli getInstance(Activity activity){
        mActivity = activity;
        return LocationUtliHodler.INSTANCE;
    }

    private static class LocationUtliHodler {
        private static final LocationUtli INSTANCE = new LocationUtli();
    }

    /**
     * 初始化定位（仅定位一次）
     *
     * @author hongming.wang
     * @since 2.8.0
     */
    public LocationUtli init() {
        if (mLocationClient == null) mLocationClient = new AMapLocationClient(mActivity);
        if (mLocationOption == null) mLocationOption = new AMapLocationClientOption();

        // 设置定位监听
        mLocationClient.setLocationListener(this);
        // 设置为高精度定位模式
        mLocationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
        // 只定位一次
        mLocationOption.setOnceLocation(true);
        // 设置是否返回地址信息（默认返回地址信息）
        mLocationOption.setNeedAddress(true);
        // 设置定位参数
        mLocationClient.setLocationOption(mLocationOption);
        mLocationClient.startLocation();

        return this;
    }

    /**
     * 初始化定位（初始持续定位）
     *
     * @author hongming.wang
     * @since 2.8.0
     */
    public LocationUtli initcontinue() {
        if (mLocationClient == null) mLocationClient = new AMapLocationClient(mActivity);
        if (mLocationOption == null) mLocationOption = new AMapLocationClientOption();

        // 设置定位监听
        mLocationClient.setLocationListener(this);
        // 设置为高精度定位模式
        mLocationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
        //运输模式持续的定位
        mLocationOption.setLocationPurpose(AMapLocationClientOption.AMapLocationPurpose.Transport);
        mLocationOption.setInterval(1000);//一秒定位一次
        // 设置是否返回地址信息（默认返回地址信息）
        mLocationOption.setNeedAddress(false);
        // 设置定位参数
        mLocationClient.setLocationOption(mLocationOption);
        mLocationClient.startLocation();
        return this;
    }

    @Override
    public void onLocationChanged(AMapLocation aMapLocation) {
        Log.e("debug", "定位中...");
        if (aMapLocation != null) {
            if (mListener != null) {
                mListener.onLocationChanged(aMapLocation);// 显示系统小蓝点-我的位置
            }

            if (aMapLocation.getErrorCode() == 0) {
                callBack(aMapLocation);
            } else {
                //定位失败时，可通过ErrCode（错误码）信息来确定失败的原因，errInfo是错误信息，详见错误码表。
                Log.e("debug", "location Error:" + aMapLocation.getErrorCode() + ", errInfo:" + aMapLocation.getErrorInfo());
                callFail(aMapLocation.getErrorCode(),aMapLocation.getErrorInfo());
                if (aMapLocation.getErrorCode() == 12){
                    new QMUIDialog.MessageDialogBuilder(mActivity)
                        .setTitle("系统定位服务已关闭")
                        .setMessage("请打开定位服务，以便精确查找附近信息")
                        /*.addAction("取消", new QMUIDialogAction.ActionListener() {
                            @Override
                            public void onClick(QMUIDialog dialog, int index) {
                                dialog.dismiss();
                            }
                        })*/
                        .addAction("去开启", new QMUIDialogAction.ActionListener() {
                            @Override
                            public void onClick(QMUIDialog dialog, int index) {
                                dialog.dismiss();
                                RxLocationTool.openGpsSettings(mActivity);
                            }
                        }).show();
                }else{
                    RxToast.error("获取位置失败");
                }
            }
        }
    }

    @Override
    public void activate(OnLocationChangedListener onLocationChangedListener) {
        Log.e("debug", "activate");
        mListener = onLocationChangedListener;
        if (mLocationClient == null) {
            init();
        }
    }

    @Override
    public void deactivate() {
        Log.e("debug", "deactivate");
        mListener = null;
        if (mLocationClient != null) {
            mLocationClient.stopLocation();
            mLocationClient.onDestroy();
        }
        mLocationClient = null;
    }

    public void destroyLocation() {
        if (null != mLocationClient) {
            mLocationClient.onDestroy();
            mLocationClient = null;
            mLocationClient = null;
        }
    }

    /**
     * ------------------------------------------
     * 定义回调
     * ------------------------------------------
     */
    private OnLocationResListener resListener;
    // 设置监听回调
    public void setLocationResListener(OnLocationResListener resListener){
        this.resListener = resListener;
    }
    public interface OnLocationResListener{
        void localSuccess(AMapLocation aMapLocation);
        void localFail(int code, String msg);
    }
    private void callBack(AMapLocation aMapLocation){
        if(resListener == null) return;
        resListener.localSuccess(aMapLocation);
    }
    private void callFail(int code, String msg){
        if(resListener == null) return;
        resListener.localFail(code,msg);
    }
}
