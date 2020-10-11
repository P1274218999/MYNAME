package com.dhht.sld.main.login.model;

import com.dhht.http.LfHttpServer;
import com.dhht.http.annotation.RequestMethod;
import com.dhht.http.result.IResult;
import com.dhht.sld.base.BaseHttpResBean;
import com.dhht.sld.base.http.DoRequest;
import com.dhht.sld.main.login.bean.LoginSuccessBean;

import java.util.HashMap;
import java.util.Map;

/**
 *
 */
public class LoginHttpTask<T> extends LfHttpServer {

    // 获取登录验证码
    public IResult<T> getCode(String phone) {
        Map<String, Object> params = new HashMap<>();
        params.put("phone", phone);
        params.put("key", 4);

        return super.execute(DoRequest.sendHttp(
                "/register/send_code",
                RequestMethod.Get,
                BaseHttpResBean.class), params);
    }

    // 执行验证码登录
    public IResult<T> checkCodeLogin(String phone, String code) {
        Map<String, Object> params = new HashMap<>();
        params.put("phone", phone);
        params.put("code", code);
        params.put("type", 2); // 2短信登录

        return super.execute(DoRequest.sendHttp(
                "/login/dologin",
                RequestMethod.Post,
                LoginSuccessBean.class), params);
    }
}
