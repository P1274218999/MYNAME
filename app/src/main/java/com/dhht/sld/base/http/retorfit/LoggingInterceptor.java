package com.dhht.sld.base.http.retorfit;

import android.util.Log;

import com.dhht.sld.utlis.UserSPUtli;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;

/**
 * 作者：pst
 * 邮箱：1274218999@qq.com
 * 创建时间:2020/8/14  15:44
 * 文件描述:日志拦截器
 */
public class LoggingInterceptor implements Interceptor {
    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request()
                .newBuilder()
                .addHeader("token", UserSPUtli.getInstance().getToken())
                .build();
        Log.e("httpRes", "\r"+UserSPUtli.getInstance().getToken()+"\r");
        Response response = chain.proceed(request);
        ResponseBody responseBody = response.peekBody(1024 * 1024);
        Log.e("httpRes",String.format("接收响应: [%s] %n %s %n",
                response.request().url(),
                responseBody.string()));
        return response;
    }

}
