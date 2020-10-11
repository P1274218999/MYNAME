package com.dhht.sld.main.updateapp.model;

import com.dhht.http.LfHttpServer;
import com.dhht.http.annotation.RequestMethod;
import com.dhht.http.result.IResult;
import com.dhht.sld.base.BaseHttpResBean;
import com.dhht.sld.base.http.DoRequest;
import com.dhht.sld.main.address.bean.AddressResBean;
import com.dhht.sld.main.updateapp.bean.CheckVersionBean;

import java.util.HashMap;
import java.util.Map;

/**
 * 作者：pst
 * 邮箱：1274218999@qq.com
 * 创建时间：2020/7/22  17:30
 * 文件描述：
 */
public class CheckVersionHttpTask<T extends BaseHttpResBean> extends LfHttpServer {

    public IResult<T> doSubmitRequest()
    {
        Map<String, Object> params = new HashMap<>();
        return super.execute(
                DoRequest.sendHttp("/update/getVersion", RequestMethod.Post, CheckVersionBean.class)
                ,params);
    }


}
