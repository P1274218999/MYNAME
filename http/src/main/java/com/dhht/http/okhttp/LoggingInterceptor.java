package com.dhht.http.okhttp;

import android.util.Log;

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
    public Response intercept(Interceptor.Chain chain) throws IOException {
        //这个chain里面包含了request和response，所以你要什么都可以从这里拿
        Request request = chain.request();

        long t1 = System.nanoTime();//请求发起的时间
//        Log.i("dt",String.format("%n 发送请求 %s on %s%n%s",
//                request.url(), chain.connection(), request.headers()));
        Response response = chain.proceed(request);
        long t2 = System.nanoTime();//收到响应的时间
        //这里不能直接使用response.body().string()的方式输出日志
        //因为response.body().string()之后，response中的流会被关闭，程序会报错，我们需要创建出一
        //个新的response给应用层处理
        ResponseBody responseBody = response.peekBody(1024 * 1024);

        Log.e("httpRes",String.format("接收响应: [%s] %n %s %n",
                response.request().url(),
                responseBody.string()));
        return response;
    }

}
