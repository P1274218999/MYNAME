package com.dhht.http.parser;

import android.util.Log;

import com.dhht.config.ServerManager;
import com.dhht.http.request.IRequest;
import com.dhht.http.response.IResponse;
import com.dhht.http.result.IResult;
import com.dhht.http.result.Result;
import com.google.gson.Gson;

import java.lang.reflect.Type;

/**
 *
 */
public class DefaultResultParser implements IParser{

    private static DefaultResultParser mInstance;
    private final Gson mGson;

    private DefaultResultParser() {
        mGson = new Gson();
    }

    public static IParser getInstance() {
        if (mInstance == null) {
            mInstance = new DefaultResultParser();
        }
        return mInstance;
    }

    @Override
    public IResult parseResponse(IRequest request, IResponse response) {
        Type type = request.getType();
        String bodyStr = response.getBodyStr();

        if (ServerManager.SERVER == ServerManager.ServerKey.DEV){
//            Log.e("httpRes", bodyStr);
        }

        Object object = mGson.fromJson(bodyStr, type);
        return Result.success(object);

    }
}
