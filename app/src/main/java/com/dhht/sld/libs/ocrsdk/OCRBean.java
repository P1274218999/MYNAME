package com.dhht.sld.libs.ocrsdk;

import com.dhht.sld.base.BaseHttpResBean;

public class OCRBean extends BaseHttpResBean {
    public resData data;

    public static class resData{
        public String orderNo;

        public String appId;

        public String version;

        public String nonce;

        public String userId;

        public String sign;
    }
}
