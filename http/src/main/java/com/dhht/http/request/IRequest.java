package com.dhht.http.request;

import com.dhht.http.parser.IParser;
import com.dhht.http.request.host.IHost;

import java.lang.reflect.Type;
import java.util.Map;

/**
 *
 */
public interface IRequest {

    void setParams(Map<String, Object> params);

    Map<String, Object> getParams();

    int getRequestMethod();

    IHost getHost();

    String getPath();

    String getFilePath();

    IParser getParser();

    Type getType();

    String getToken();

}
