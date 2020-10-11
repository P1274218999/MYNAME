package com.dhht.sld.libs.faceverify;

import com.dhht.sld.base.BaseHttpResBean;

public class FaceSignBean extends BaseHttpResBean {
    public resData data;

    public static class resData{
        public String openApiAppId;

        public String openApiSign;

        public String userId;

        public String faceId;

        public String orderNo;

        public String openApiNonce;

        public String openApiAppVersion;

        public String keyLicence;
    }
}
