package com.dhht.sld.main.choose.article.model;

import com.dhht.http.LfHttpServer;
import com.dhht.http.annotation.RequestMethod;
import com.dhht.http.result.IResult;
import com.dhht.sld.base.http.DoRequest;
import com.dhht.sld.main.choose.article.Bean.ArticleDataBean;

import java.util.HashMap;
import java.util.Map;

/**
 *
 */
public class ArticleHttpTask<T> extends LfHttpServer {

    // 获取登录验证码
    public IResult<T> getList(int status) {
        Map<String, Object> params = new HashMap<>();
        params.put("status", status);

        return super.execute(DoRequest.sendHttp(
                "/article/get_list_cate",
                RequestMethod.Get,
                ArticleDataBean.class), params);
    }
}
