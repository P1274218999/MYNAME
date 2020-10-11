package com.dhht.http.request.call;

import com.dhht.http.request.IRequest;
import com.dhht.http.response.IResponse;

/**
 *
 */
public interface ICall {

    IResponse execute();

    IRequest getRequest();
}
