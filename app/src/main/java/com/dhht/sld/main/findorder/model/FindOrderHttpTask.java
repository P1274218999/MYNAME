package com.dhht.sld.main.findorder.model;

import com.dhht.http.LfHttpServer;
import com.dhht.http.annotation.RequestMethod;
import com.dhht.http.result.IResult;
import com.dhht.sld.base.http.DoRequest;
import com.dhht.sld.main.findorder.bean.FindOrderBean;

import java.util.HashMap;
import java.util.Map;

/**
 *
 */
public class FindOrderHttpTask<T> extends LfHttpServer {

    public IResult<T> getList(int type)
    {
        Map<String, Object> params = new HashMap<>();
        params.put("type", type);

        return super.execute(
                DoRequest.sendHttp("/findOrder/getList", RequestMethod.Post, FindOrderBean.class)
                ,params);
    }
}
