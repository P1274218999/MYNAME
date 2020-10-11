package com.dhht.sld.base.http.retorfit;

import com.dhht.sld.base.http.HostManager;

import okhttp3.*;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import java.util.concurrent.TimeUnit;

public class Retorfit {
    private static final Retorfit ourInstance = new Retorfit();

    private OkHttpClient okHttpClient = new OkHttpClient.Builder()
            .retryOnConnectionFailure( true ) //链接失败时重试
            .addInterceptor(new LoggingInterceptor())  //添加拦截器
            .connectTimeout( 10, TimeUnit.SECONDS ) //链接超时
            .readTimeout( 10, TimeUnit.SECONDS )  //设置读取超时
            .build();


    private Retrofit retrofit = new Retrofit.Builder()
            .client(okHttpClient)
            .baseUrl(HostManager.DHHost.getHost())
            .addConverterFactory( GsonConverterFactory.create() ) //支持Gson解析
            .build();
    public static Retorfit getInstance() {
        return ourInstance;
    }
    private Retorfit() {
    }
    public static ApiService getService() {
        return  getInstance().retrofit.create( ApiService.class );
    }
}
