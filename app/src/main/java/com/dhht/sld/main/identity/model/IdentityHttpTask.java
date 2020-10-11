package com.dhht.sld.main.identity.model;

import com.dhht.http.LfHttpServer;
import com.dhht.http.annotation.RequestMethod;
import com.dhht.http.result.IResult;
import com.dhht.sld.base.BaseHttpResBean;
import com.dhht.sld.base.http.DoRequest;

import java.util.HashMap;
import java.util.Map;

/**
 *
 */
public class IdentityHttpTask<T> extends LfHttpServer {

    public IResult<T> doSubmit(String name,String id_no,String img_front,String img_reverse)
    {
        Map<String, Object> params = new HashMap<>();
        params.put("name", name);
        params.put("id_no", id_no);
        params.put("img_front", img_front);
        params.put("img_reverse", img_reverse);

        return super.execute(
                DoRequest.sendHttp("/user/idcard", RequestMethod.Post, BaseHttpResBean.class)
                ,params);
    }
}
