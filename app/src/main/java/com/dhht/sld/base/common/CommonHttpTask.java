package com.dhht.sld.base.common;

import com.dhht.http.LfHttpServer;
import com.dhht.http.result.IResult;
import com.dhht.sld.base.http.DoRequest;

import java.util.HashMap;
import java.util.Map;

/**
 *
 */
public class CommonHttpTask<T> extends LfHttpServer {

    public IResult<T> upload(String filePath)
    {
        Map<String, Object> params = new HashMap<>();
        return super.execute(DoRequest.upload(filePath),params);
    }
}
