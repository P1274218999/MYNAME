package com.dhht.sld.base.http;

import com.dhht.http.annotation.RequestMethod;
import com.dhht.http.parser.DefaultResultParser;
import com.dhht.http.request.IRequest;
import com.dhht.http.request.LfRequest;
import com.dhht.sld.base.BaseHttpResImgBean;
import com.dhht.sld.utlis.UserSPUtli;

import java.lang.reflect.Type;

/**
 *
 */
public class DoRequest extends LfRequest {

    public static IRequest sendHttp(String path, @RequestMethod int requestMethod, Type type) {
        DoRequest request = new DoRequest();

        request.host = HostManager.DHHost;
        request.path = path;
        request.requestMethod = requestMethod;
        request.type = type;
        request.resultParser = DefaultResultParser.getInstance();
        request.token = UserSPUtli.getInstance().getToken();

        return request;
    }

    /**
     *
     * @return
     */
    public static IRequest upload(String filePath) {
        DoRequest request = new DoRequest();

        request.host = HostManager.DHHost;
        request.path = "/upload/index";
        request.requestMethod = RequestMethod.Upload;
        request.type = BaseHttpResImgBean.class;
        request.filePath = filePath;
        request.resultParser = DefaultResultParser.getInstance();
        request.token = UserSPUtli.getInstance().getToken();

        return request;
    }

}
