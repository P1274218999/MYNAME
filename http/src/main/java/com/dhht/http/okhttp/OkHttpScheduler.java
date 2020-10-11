package com.dhht.http.okhttp;

import android.util.Log;

import com.dhht.config.ServerManager;
import com.dhht.http.HttpScheduler;
import com.dhht.http.annotation.RequestMethod;
import com.dhht.http.https.Https;
import com.dhht.http.request.IRequest;
import com.dhht.http.request.call.ICall;

import java.io.File;
import java.util.Iterator;
import java.util.Map;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLSession;

import okhttp3.Call;
import okhttp3.FormBody;
import okhttp3.Headers;
import okhttp3.HttpUrl;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;

/**
 *
 */
public class OkHttpScheduler extends HttpScheduler {

    private OkHttpClient client;

    @Override
    public ICall newCall(IRequest request) {
        Map<String, Object> params = request.getParams();
        int requestMethod = request.getRequestMethod();
        Request.Builder requestBuilder = new Request.Builder();
        requestBuilder.header("token", request.getToken());

        //拼接Get请求的url host + path
        StringBuilder urlStrBuilder = new StringBuilder(request.getHost().getHost());
        urlStrBuilder.append(request.getPath());

        // 开发开启http请求路径显示
        if (ServerManager.SERVER == ServerManager.ServerKey.DEV){
//            Log.e("httpRequest", urlStrBuilder.toString());
        }

        switch (requestMethod)
        {
            case RequestMethod.Get:
                HttpUrl.Builder urlBuilder = HttpUrl.parse(urlStrBuilder.toString()).newBuilder();
                if (params != null && params.size() > 0) {
                    Iterator<Map.Entry<String, Object>> iterator = params.entrySet().iterator();
                    while (iterator.hasNext()) {
                        Map.Entry<String, Object> next = iterator.next();
                        urlBuilder.addQueryParameter(next.getKey(), String.valueOf(next.getValue()));
                    }
                }
                requestBuilder.get().url(urlBuilder.build());
                break;

            case RequestMethod.Post:
                FormBody.Builder formBody = new FormBody.Builder();
                if (params != null && params.size() > 0) {
                    Iterator<Map.Entry<String, Object>> iterator = params.entrySet().iterator();
                    while (iterator.hasNext()) {
                        Map.Entry<String, Object> next = iterator.next();
                        formBody.add(next.getKey(), String.valueOf(next.getValue()));
                    }
                }
                RequestBody form = formBody.build();
                requestBuilder.post(form).url(urlStrBuilder.toString());
                break;

            case RequestMethod.Upload: // 文件上传
                String filePath = request.getFilePath();
                File file = new File(filePath);
                RequestBody fileBody = RequestBody.create(MediaType.parse("application/octet-stream"), file);
                //请求体
                RequestBody requestBody = new MultipartBody.Builder()
                        .setType(MultipartBody.FORM)
                        .addPart(Headers.of(
                                "Content-Disposition",
                                "form-data; name=\"filename\""),
                                RequestBody.create(null, "lzr"))//这里是携带上传的其他数据
                        .addPart(Headers.of(
                                "Content-Disposition",
                                "form-data; name=\"mFile\"; filename=\"" + filePath + "\""), fileBody)
                        .build();
                requestBuilder.url(urlStrBuilder.toString()).post(requestBody);
                break;
        }
        Request okHttpRequest = requestBuilder.build();
        Call call = getClient().newCall(okHttpRequest);
        OkHttpCall okHttpCall = new OkHttpCall(request,call);
        return okHttpCall;
    }

    private OkHttpClient getClient() {
        if (client == null) {
//            client = new OkHttpClient();
            OkHttpClient.Builder builder = new OkHttpClient.Builder();
            builder.sslSocketFactory(Https.getSSLSocketFactory());
//            Https2.SSLParams sslSocketFactory = Https2.getSslSocketFactory(null, null, null);
//            builder.sslSocketFactory(sslSocketFactory.sSLSocketFactory,sslSocketFactory.trustManager);
            builder.addInterceptor(new LoggingInterceptor());
            builder.hostnameVerifier(new HostnameVerifier() {
                @Override
                public boolean verify(String hostname, SSLSession session) {
                    return true;
                }
            });

            client = builder.build();
        }
        return client;
    }
}
