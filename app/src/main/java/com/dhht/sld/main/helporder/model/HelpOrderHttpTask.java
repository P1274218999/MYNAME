package com.dhht.sld.main.helporder.model;

import com.dhht.http.LfHttpServer;
import com.dhht.http.annotation.RequestMethod;
import com.dhht.http.result.IResult;
import com.dhht.sld.base.http.DoRequest;
import com.dhht.sld.main.helporder.bean.HelpOrderBean;

import java.util.HashMap;
import java.util.Map;

/**
 *
 */
public class HelpOrderHttpTask<T> extends LfHttpServer {

    public IResult<T> getList(int type)
    {
        Map<String, Object> params = new HashMap<>();
        params.put("type", type);

        return super.execute(
                DoRequest.sendHttp("/helpOrder/getUserList", RequestMethod.Post, HelpOrderBean.class)
                ,params);
    }
}
