package com.dhht.sld.main.choose.pay.bean;

import com.dhht.sld.base.BaseHttpResBean;

import java.lang.annotation.Target;

/**
 * 作者：pst
 * 邮箱：1274218999@qq.com
 * 创建时间:2020/8/13  15:09
 * 文件描述:预支付
 */
public class PayOrderBean extends BaseHttpResBean {

        /**
         * appid : wx85ee40a2ae6066ec
         * sign : 49D87865D2979072EA4997D758AB3749
         * partnerid : 1567851441
         * prepayid : wx13101444263029f5bb966d1ac71b570000
         * noncestr : hcjsFtLkx247GolJxOSefjFtYorXIH2E
         * timestamp : 1597284884
         */
        public String appid;
        public String sign;
        public String partnerid;
        public String prepayid;
        public String noncestr;
        public String timestamp;
//        public String package;
}
