package com.dhht.http;

import com.dhht.http.request.IRequest;
import com.dhht.http.result.IResult;

import java.util.Map;

/**
 *
 */
public class LfHttpServer {

    protected <T> IResult<T> execute(IRequest request, Map<String,Object> params) {
        params.put("time", "" + System.currentTimeMillis()/1000);
        return HttpHelper.execute(request,params);
    }
}
