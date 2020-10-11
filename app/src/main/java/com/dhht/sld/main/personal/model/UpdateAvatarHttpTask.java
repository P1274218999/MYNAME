package com.dhht.sld.main.personal.model;

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
public class UpdateAvatarHttpTask<T> extends LfHttpServer {

    public IResult<T> doSubmit(String imgUrl )
    {
        Map<String, Object> params = new HashMap<>();
        params.put("img", imgUrl);
        return super.execute(
                DoRequest.sendHttp("/user/updateAvatar", RequestMethod.Post, BaseHttpResBean.class)
                ,params);
    }
}
