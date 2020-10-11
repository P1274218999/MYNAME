package com.dhht.http.okhttp;

import com.dhht.http.request.IRequest;
import com.dhht.http.request.call.ICall;
import com.dhht.http.response.IResponse;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Response;

/**
 *
 */
public class OkHttpCall implements ICall {

    private IRequest request;
    private Call call;

    public OkHttpCall(IRequest request, Call call) {
        this.call = call;
        this.request = request;
    }

    @Override
    public IResponse execute() {
        Response response = null;
        try {
            response = call.execute();
        } catch (IOException e) {
            e.printStackTrace();
        }
        OkHttpResponse okHttpResponse = new OkHttpResponse(response);
        return okHttpResponse;
    }

    @Override
    public IRequest getRequest() {
        return request;
    }
}
