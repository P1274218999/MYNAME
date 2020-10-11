package com.dhht.sld.main.choose.endaddress.model;

import com.dhht.http.LfHttpServer;
import com.dhht.http.annotation.RequestMethod;
import com.dhht.http.result.IResult;
import com.dhht.sld.base.BaseHttpResBean;
import com.dhht.sld.base.http.DoRequest;
import com.dhht.sld.main.choose.endaddress.bean.CheckingPriceBean;

import java.util.HashMap;
import java.util.Map;

/**
 * 作者：pst
 * 邮箱：1274218999@qq.com
 * 创建时间：2020/8/11  17:12
 * 查询运送物品价格
 */
public class CheckingPriceHttpTask<T> extends LfHttpServer {
    public IResult<T> doCreate(int articleId, int startId, int endId) {
        Map<String, Object> params = new HashMap<>();
        params.put("articleId", articleId);
        params.put("startId", startId);
        params.put("endId", endId);

        return super.execute(DoRequest.sendHttp(
                "/findPeople/price/",
                RequestMethod.Post,
                CheckingPriceBean.class), params);
    }
}
