package com.dhht.http;

import com.dhht.http.okhttp.OkHttpScheduler;
import com.dhht.http.request.IRequest;
import com.dhht.http.request.call.ICall;
import com.dhht.http.result.IResult;

import java.util.Map;

/**
 *
 */
public class HttpHelper {

    private volatile static HttpScheduler httpScheduler;

    public static HttpScheduler getHttpScheduler() {
        if (httpScheduler == null) {
            synchronized (HttpHelper.class) {
                if (httpScheduler == null) {
                    httpScheduler = new OkHttpScheduler();
                }
            }
        }
        return httpScheduler;
    }

    //
    protected static <T> IResult<T> execute(IRequest request, Map<String,Object> params) {
        request.setParams(params);
        ICall call = getHttpScheduler().newCall(request);
        return getHttpScheduler().execute(call);
    }
}
