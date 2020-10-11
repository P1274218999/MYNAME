package com.dhht.http.parser;


import com.dhht.http.request.IRequest;
import com.dhht.http.response.IResponse;
import com.dhht.http.result.IResult;

/**
 * Created by anson on 2019/1/26.
 */
public interface IParser {

    IResult parseResponse(IRequest request, IResponse response);
}
