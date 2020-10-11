package com.dhht.sld.libs.faceverify;

import com.dhht.http.LfHttpServer;
import com.dhht.http.annotation.RequestMethod;
import com.dhht.http.result.IResult;
import com.dhht.sld.base.http.DoRequest;
import com.dhht.sld.libs.ocrsdk.OCRBean;

import java.util.HashMap;
import java.util.Map;

/**
 *
 */
public class FaceHttpTask<T> extends LfHttpServer {

    public IResult<T> getSign(String identityName, String identityNum)
    {
        Map<String, Object> params = new HashMap<>();
        params.put("name",identityName);
        params.put("id_no",identityNum);
        return super.execute(
                DoRequest.sendHttp("/verify/getFaceSign", RequestMethod.Post, FaceSignBean.class)
                ,params);
    }
}
