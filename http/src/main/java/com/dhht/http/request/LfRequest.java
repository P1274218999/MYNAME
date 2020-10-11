package com.dhht.http.request;

import com.dhht.http.annotation.RequestMethod;
import com.dhht.http.parser.IParser;
import com.dhht.http.request.host.IHost;

import java.lang.reflect.Type;
import java.util.Map;

/**
 *
 */
public class LfRequest implements IRequest{

    protected String path;

    protected IHost host;

    protected Map<String, Object> params;

    protected Type type;

    protected String filePath;

    protected IParser resultParser;

    protected String token;

    @RequestMethod
    protected int requestMethod;


    @Override
    public void setParams(Map<String, Object> params) {
        this.params = params;
    }

    @Override
    public Map<String, Object> getParams() {
        return params;
    }

    @Override
    public int getRequestMethod() {
        return requestMethod;
    }

    @Override
    public IHost getHost() {
        return host;
    }

    @Override
    public String getPath() {
        return path;
    }

    @Override
    public String getFilePath() {
        return filePath;
    }

    @Override
    public IParser getParser() {
        return resultParser;
    }

    @Override
    public Type getType() {
        return type;
    }

    @Override
    public String getToken() {
        return token;
    }
}
