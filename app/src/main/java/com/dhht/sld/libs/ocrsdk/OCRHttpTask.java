package com.dhht.sld.libs.ocrsdk;

import com.dhht.http.LfHttpServer;
import com.dhht.http.annotation.RequestMethod;
import com.dhht.http.result.IResult;
import com.dhht.sld.base.http.DoRequest;

import java.util.HashMap;
import java.util.Map;

/**
 *
 */
public class OCRHttpTask<T> extends LfHttpServer {

    public IResult<T> getSign()
    {
        Map<String, Object> params = new HashMap<>();
        return super.execute(
                DoRequest.sendHttp("/verify/getORCSign", RequestMethod.Post, OCRBean.class)
                ,params);
    }

    public IResult<OCRResultBean> getResult(String orderNo, String nonce) {
        Map<String, Object> params = new HashMap<>();
        params.put("orderNo", orderNo);
        params.put("nonce", nonce);
        return super.execute(
                DoRequest.sendHttp("/verify/getORCResult", RequestMethod.Post, OCRResultBean.class)
                ,params);
    }
}
