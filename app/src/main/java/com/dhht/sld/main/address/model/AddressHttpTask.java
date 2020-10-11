package com.dhht.sld.main.address.model;

import com.dhht.http.LfHttpServer;
import com.dhht.http.annotation.RequestMethod;
import com.dhht.http.result.IResult;
import com.dhht.sld.base.BaseHttpResBean;
import com.dhht.sld.base.http.DoRequest;
import com.dhht.sld.main.address.bean.AddressResBean;

import java.util.HashMap;
import java.util.Map;

/**
 *
 */
public class AddressHttpTask<T> extends LfHttpServer {

    public IResult<T> doSubmitRequest(int type,String name,String phone,double latitude,double longitude,String province,String area,String house_number)
    {
        Map<String, Object> params = new HashMap<>();
        params.put("name", name);
        params.put("type", type);
        params.put("phone", phone);
        params.put("latitude", latitude);
        params.put("longitude", longitude);
        params.put("province", province);
        params.put("area", area);
        params.put("house_number", house_number);

        return super.execute(
                DoRequest.sendHttp("/address/add", RequestMethod.Post, AddressResBean.class)
                ,params);
    }
}
