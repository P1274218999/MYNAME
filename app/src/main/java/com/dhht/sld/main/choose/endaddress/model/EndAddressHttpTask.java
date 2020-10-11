package com.dhht.sld.main.choose.endaddress.model;

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
public class EndAddressHttpTask<T> extends LfHttpServer {

    public IResult<T> doCreate(int articleId,int startId,int endId) {
        Map<String, Object> params = new HashMap<>();
        params.put("articleId", articleId);
        params.put("startId", startId);
        params.put("endId", endId);

        return super.execute(DoRequest.sendHttp(
                "/findPeople/doCreate",
                RequestMethod.Post,
                BaseHttpResBean.class), params);
    }
}
