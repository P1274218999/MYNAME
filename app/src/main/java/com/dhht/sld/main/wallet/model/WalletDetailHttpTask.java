package com.dhht.sld.main.wallet.model;

import com.dhht.http.LfHttpServer;
import com.dhht.http.annotation.RequestMethod;
import com.dhht.http.result.IResult;
import com.dhht.sld.base.http.DoRequest;
import com.dhht.sld.main.choose.article.Bean.ArticleDataBean;
import com.dhht.sld.main.wallet.bean.WalletDetailBean;

import java.util.HashMap;
import java.util.Map;

/**
 *
 */
public class WalletDetailHttpTask<T> extends LfHttpServer {

    // 获取登录验证码
    public IResult<T> getList(int page,int type) {
        Map<String, Object> params = new HashMap<>();
        params.put("page", page);
        params.put("type", type);
        return super.execute(DoRequest.sendHttp(
                "/user/myWallet",
                RequestMethod.Get,
                WalletDetailBean.class), params);
    }
}
