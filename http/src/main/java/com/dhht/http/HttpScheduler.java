package com.dhht.http;

import com.dhht.http.parser.IParser;
import com.dhht.http.request.IRequest;
import com.dhht.http.request.call.ICall;
import com.dhht.http.response.IResponse;
import com.dhht.http.result.IResult;

/**
 *
 */
public abstract class HttpScheduler {

    public abstract ICall newCall(IRequest request);

    public IResult execute(ICall call) {
        //IResponse 和 IResult 进行一个转换
        IResponse iResponse = call.execute();
        IRequest request = call.getRequest();
        IParser parser = request.getParser();
        return parser.parseResponse(request,iResponse);
    }

}
