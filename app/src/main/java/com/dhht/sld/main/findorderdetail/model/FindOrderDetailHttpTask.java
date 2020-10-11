package com.dhht.sld.main.findorderdetail.model;

import com.dhht.http.LfHttpServer;
import com.dhht.http.annotation.RequestMethod;
import com.dhht.http.result.IResult;
import com.dhht.sld.base.BaseHttpResBean;
import com.dhht.sld.base.http.DoRequest;
import com.dhht.sld.main.findorderdetail.bean.FindOrderDetailBean;
import com.dhht.sld.main.home.bean.CurrentOrderBean;
import com.dhht.sld.main.home.bean.MarkerBean;
import com.dhht.sld.main.home.bean.MarkersBean;
import com.dhht.sld.main.login.bean.LoginSuccessBean;

import java.util.HashMap;
import java.util.Map;

/**
 *
 */
public class FindOrderDetailHttpTask<T> extends LfHttpServer {

    public IResult<T> getUserHelpOrderOne(int id) {
        Map<String, Object> params = new HashMap<>();

        params.put("id", id);

        return super.execute(DoRequest.sendHttp(
                "/findOrder/getUserHelpOrder",
                RequestMethod.Get,
                FindOrderDetailBean.class), params);
    }
}
