package com.dhht.sld.main.home.model;

import com.dhht.http.LfHttpServer;
import com.dhht.http.annotation.RequestMethod;
import com.dhht.http.result.IResult;
import com.dhht.sld.base.BaseHttpResBean;
import com.dhht.sld.base.http.DoRequest;
import com.dhht.sld.main.home.bean.CurrentOrderBean;
import com.dhht.sld.main.home.bean.MarkerBean;
import com.dhht.sld.main.home.bean.MarkersBean;
import com.dhht.sld.main.login.bean.LoginSuccessBean;

import java.util.HashMap;
import java.util.Map;

/**
 *
 */
public class MainHttpTask<T> extends LfHttpServer {

    public IResult<T> getFindPeopleMarker(int pageSize, double latitude, double longitude) {
        Map<String, Object> params = new HashMap<>();

        params.put("size", pageSize);
        params.put("latitude", latitude);
        params.put("longitude", longitude);

        return super.execute(DoRequest.sendHttp(
                "/helpOrder/getMarker",
                RequestMethod.Get,
                MarkersBean.class), params);
    }

    public IResult<T> getHelpPeopleMarker(int pageSize, double latitude, double longitude) {
        Map<String, Object> params = new HashMap<>();

        params.put("size", pageSize);
        params.put("latitude", latitude);
        params.put("longitude", longitude);

        return super.execute(DoRequest.sendHttp(
                "/findOrder/getMarker",
                RequestMethod.Get,
                MarkersBean.class), params);
    }

    // 获得单个Marker
    public IResult<T> getHelpMarkerOne(int id) {
        Map<String, Object> params = new HashMap<>();
        params.put("id", id);
        return super.execute(DoRequest.sendHttp(
                "/findOrder/getMarkerOne",
                RequestMethod.Get,
                MarkerBean.class), params);
    }

    // 刷新token登录状态
    public IResult<T> rfToken() {
        Map<String, Object> params = new HashMap<>();

        return super.execute(DoRequest.sendHttp(
                "/user/rf_token",
                RequestMethod.Post,
                LoginSuccessBean.class), params);
    }

    // 刷新token登录状态
    public IResult<T> doReceiving(int id) {
        Map<String, Object> params = new HashMap<>();
        params.put("id", id);
        return super.execute(DoRequest.sendHttp(
                "/findOrder/doReceiving",
                RequestMethod.Post,
                BaseHttpResBean.class), params);
    }

    // 取消帮人带订单
    public IResult<T> cancelHelpOrder(int id) {
        Map<String, Object> params = new HashMap<>();
        params.put("id", id);
        return super.execute(DoRequest.sendHttp(
                "/findOrder/cancelHelpOrder",
                RequestMethod.Post,
                BaseHttpResBean.class), params);
    }

    // 确定取货
    public IResult<T> yesQuhuoHelpOrder(int id) {
        Map<String, Object> params = new HashMap<>();
        params.put("id", id);
        return super.execute(DoRequest.sendHttp(
                "/findOrder/yesQuhuoHelpOrder",
                RequestMethod.Post,
                BaseHttpResBean.class), params);
    }

    // 确定收货
    public IResult<T> yesSongHuoHelpOrder(int id) {
        Map<String, Object> params = new HashMap<>();
        params.put("id", id);
        return super.execute(DoRequest.sendHttp(
                "/findOrder/yesSongHuoHelpOrder",
                RequestMethod.Post,
                BaseHttpResBean.class), params);
    }

    // 获取当前已接的订单信息
    public IResult<T> getHelpOrder() {
        Map<String, Object> params = new HashMap<>();

        return super.execute(DoRequest.sendHttp(
                "/findOrder/getHelpOrder",
                RequestMethod.Post,
                CurrentOrderBean.class), params);
    }
}
