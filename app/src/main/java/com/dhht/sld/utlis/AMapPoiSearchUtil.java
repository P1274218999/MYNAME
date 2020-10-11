package com.dhht.sld.utlis;

import android.content.Context;
import android.util.Log;

import com.amap.api.services.core.AMapException;
import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.core.PoiItem;
import com.amap.api.services.poisearch.PoiResult;
import com.amap.api.services.poisearch.PoiSearch;
import com.tamsiree.rxkit.view.RxToast;

import java.util.List;

public class AMapPoiSearchUtil implements PoiSearch.OnPoiSearchListener {

    private static Context mContext;
    private PoiSearch poiSearch;
    private PoiSearch.Query query;// Poi查询条件类
    private List<PoiItem> poiItems;// poi数据
    private int currentPage = 0;

    private AMapPoiSearchUtil(){}

    public static AMapPoiSearchUtil getInstance(Context context)
    {
        mContext = context;
        return AMapPoiSearchUtilHolder.INSTANCE;
    }

    private static class AMapPoiSearchUtilHolder{
        private static final AMapPoiSearchUtil INSTANCE = new AMapPoiSearchUtil();
    }

    public AMapPoiSearchUtil doSearch(String searchKey, LatLonPoint searchLatlonPoint)
    {
        currentPage = 0;
        query = new PoiSearch.Query(searchKey, null, "");// 第一个参数表示搜索字符串，第二个参数表示poi搜索类型，第三个参数表示poi搜索区域（空字符串代表全国）
        query.setCityLimit(true);
        query.setPageSize(30);
        query.setPageNum(currentPage); // 设置从第几页开始查询 0开始
        query.setDistanceSort(true); // 设置是否按距离
        poiSearch = new PoiSearch(mContext, query);
        poiSearch.setOnPoiSearchListener(this);
        if (searchLatlonPoint != null) { ;
            poiSearch.setBound(new PoiSearch.SearchBound(searchLatlonPoint, 200, true));
        }
        poiSearch.searchPOIAsyn();

        return this;
    }


    @Override
    public void onPoiSearched(PoiResult poiResult, int resultCode) {
        if (resultCode == AMapException.CODE_AMAP_SUCCESS) {
            if (poiResult != null && poiResult.getQuery() != null) {
                if (poiResult.getQuery().equals(query)) {
                    poiItems = poiResult.getPois();
                    if (poiItems != null && poiItems.size() > 0) {
                        Log.e("debug", "搜索成功"+poiItems);

                    } else {
                        poiItems.clear();
                        RxToast.error("无搜索结果1");
                    }
                } else {
                    poiItems.clear();
                    RxToast.error("无搜索结果2");
                }
            } else {
                poiItems.clear();
                RxToast.error("无搜索结果:"+resultCode);
            }
        } else {
            poiItems.clear();
            RxToast.error("无搜索结果4");
        }
        listener.onSuccess(poiItems);

    }

    @Override
    public void onPoiItemSearched(PoiItem poiItem, int i) {

    }

    /**
     * 定义回调
     */
    private OnSearchedListener listener;
    public void setOnSearchedListener(OnSearchedListener onInputListener){
        this.listener = onInputListener;
    }
    public interface OnSearchedListener{
        void onSuccess(List<PoiItem> poiItems);
    }
}
