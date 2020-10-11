package com.dhht.sld.main.home.util.routeplan;

import android.content.Context;
import android.util.Log;

import com.amap.api.maps.AMap;
import com.amap.api.maps.model.LatLng;
import com.amap.api.services.core.AMapException;
import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.route.BusRouteResult;
import com.amap.api.services.route.DrivePath;
import com.amap.api.services.route.DriveRouteResult;
import com.amap.api.services.route.DriveStep;
import com.amap.api.services.route.RideRouteResult;
import com.amap.api.services.route.RouteSearch;
import com.amap.api.services.route.WalkPath;
import com.amap.api.services.route.WalkRouteResult;
import com.amap.api.services.route.WalkStep;
import com.dhht.sld.main.home.model.HelpRoutResModel;
import com.tamsiree.rxkit.view.RxToast;

import java.util.ArrayList;
import java.util.List;

/**
 * 路线规划
 */
public class RoutePlanHelp implements RouteSearch.OnRouteSearchListener {
    private Context mContext;
    private AMap aMap;
    private LatLonPoint mStartPoint;
    private LatLonPoint mCenterPoint;
    private LatLonPoint mEndPoint;
    private RouteSearch mRouteSearch;
    private WalkRouteOverlay walkRouteOverlay;
    private List<HelpRoutResModel> routePath = new ArrayList();
    private int type;

    public RoutePlanHelp(Context context,AMap aMap,LatLng startPoint,LatLng centerPoint,LatLng endPoint)
    {
        this.mContext = context;
        this.aMap = aMap;
        this.mStartPoint = new LatLonPoint(startPoint.latitude,startPoint.longitude);
        this.mCenterPoint = new LatLonPoint(centerPoint.latitude,centerPoint.longitude);
        this.mEndPoint = new LatLonPoint(endPoint.latitude,endPoint.longitude);

        mRouteSearch = new RouteSearch(context);
        mRouteSearch.setRouteSearchListener(this);
    }

    public RoutePlanHelp(Context context,AMap aMap,LatLng startPoint,LatLng endPoint)
    {
        this.mContext = context;
        this.aMap = aMap;
        this.mStartPoint = new LatLonPoint(startPoint.latitude,startPoint.longitude);
        this.mEndPoint = new LatLonPoint(endPoint.latitude,endPoint.longitude);

        mRouteSearch = new RouteSearch(context);
        mRouteSearch.setRouteSearchListener(this);
    }

    /**
     *
     * @param routeType
     * @param type 1 当前位置到取货地 2 取货地到送货地
     */
    public void run(int routeType, int type)
    {
        this.type = type;
        if (mStartPoint == null) {
            RxToast.error("起点未设置");
            return;
        }
        if (mEndPoint == null) {
            RxToast.error("终点未设置");
        }

        final RouteSearch.FromAndTo fromAndTo = new RouteSearch.FromAndTo(mStartPoint, mEndPoint);
        switch (routeType)
        {
            case 1: // 驾车
                // 第一个参数表示路径规划的起点和终点，第二个参数表示驾车模式，第三个参数表示途经点，第四个参数表示避让区域，第五个参数表示避让道路
                RouteSearch.DriveRouteQuery queryDrive = new RouteSearch.DriveRouteQuery(fromAndTo, RouteSearch.DRIVING_SINGLE_DEFAULT,null,null,"");
                mRouteSearch.calculateDriveRouteAsyn(queryDrive);// 异步路径规划驾车模式查询
            case 2:

                break;
            case 3:
                RouteSearch.WalkRouteQuery queryWalk = new RouteSearch.WalkRouteQuery(fromAndTo);
                mRouteSearch.calculateWalkRouteAsyn(queryWalk);// 异步路径规划步行模式查询
                break;
            case 4:

                break;
            default:
                return;
        }
    }

    @Override
    public void onBusRouteSearched(BusRouteResult busRouteResult, int i) {}

    @Override
    public void onDriveRouteSearched(DriveRouteResult driveRouteResult, int errorCode) {
        aMap.clear(true);// 清理地图上的所有覆盖物
        if (errorCode == AMapException.CODE_AMAP_SUCCESS) {
            if (driveRouteResult != null && driveRouteResult.getPaths() != null) {
                if (driveRouteResult.getPaths().size() > 0) {
                    DriveRouteResult mDriveRouteResult = driveRouteResult;
                    final DrivePath drivePath = mDriveRouteResult.getPaths()
                            .get(0);
                    Log.e("debug", "驾驶-onDriveRouteSearched");
                    DrivingRouteOverlay drivingRouteOverlay = new DrivingRouteOverlay(
                            mContext, aMap, drivePath,
                            mDriveRouteResult.getStartPos(),
                            mDriveRouteResult.getTargetPos(), null,type);
                    drivingRouteOverlay.removeFromMap();

                    drivingRouteOverlay.setNodeIconVisibility(false);//设置节点marker是否显示
                    drivingRouteOverlay.setIsColorfulline(true);//是否用颜色展示交通拥堵情况，默认true
                    drivingRouteOverlay.addToMap();
                    drivingRouteOverlay.zoomToSpan();

                    // 绑定回调
                    List<DriveStep> drivePaths = drivePath.getSteps();
                    float duration = 0;
                    float distance = 0;
                    for (int i = 0; i < drivePaths.size(); i++) {
                        DriveStep step = drivePaths.get(i);
                        duration += step.getDuration();
                        distance += step.getDistance();
                    }
                    HelpRoutResModel resModel = new HelpRoutResModel();
                    resModel.setDuration(duration);
                    resModel.setDistance(distance);
                    routePath.add(resModel);
                    if (listener != null) listener.onResult(routePath);

                } else if (driveRouteResult != null && driveRouteResult.getPaths() == null) {
                    Log.e("debug", "没有result1");
                }
            } else {
                Log.e("debug", "没有result2");
            }
        } else {
            Log.e("debug", "没有result3");
        }
    }

    @Override
    public void onWalkRouteSearched(WalkRouteResult result, int errorCode) {
        aMap.clear(true);// 清理地图上的所有覆盖物

        if (errorCode == AMapException.CODE_AMAP_SUCCESS) {
            if (result != null && result.getPaths() != null) {
                if (result.getPaths().size() > 0) {
                    WalkRouteResult mWalkRouteResult = result;
                    final WalkPath walkPath = mWalkRouteResult.getPaths()
                            .get(0);
                    Log.e("debug", "规划返回成功-"+walkPath);
                    if (walkRouteOverlay != null){
                        walkRouteOverlay.removeFromMap();
                    }
                    walkRouteOverlay = new WalkRouteOverlay(
                            mContext, aMap, walkPath,
                            mWalkRouteResult.getStartPos(),
                            mWalkRouteResult.getTargetPos());
                    walkRouteOverlay.addToMap();

                    // 移动镜头到当前的视角
                    walkRouteOverlay.zoomToSpan();


                    // 绑定回调
                    List<WalkStep> walkPaths = walkPath.getSteps();
                    float duration = 0;
                    float distance = 0;
                    for (int i = 0; i < walkPaths.size(); i++) {
                        WalkStep walkStep = walkPaths.get(i);
                        duration += walkStep.getDuration() + duration;
                        distance += walkStep.getDistance() + distance;
                    }

                    HelpRoutResModel resModel = new HelpRoutResModel();
                    resModel.setDuration(duration);
                    resModel.setDistance(distance);
                    routePath.add(resModel);

                    listener.onResult(routePath);

                } else if (result != null && result.getPaths() == null) {
                    Log.e("debug", "没有result1");
                }
            } else {
                Log.e("debug", "没有result2");
            }
        } else {
            Log.e("debug", "没有result3");
        }
    }

    @Override
    public void onRideRouteSearched(RideRouteResult rideRouteResult, int i) {

    }


    // 回调
    private OnRouteResultListener listener;
    public interface OnRouteResultListener {
        void onResult(List<HelpRoutResModel> path);
    }
    public void setOnRouteResultListener(OnRouteResultListener listener) {
        this.listener = listener;
    }
}
