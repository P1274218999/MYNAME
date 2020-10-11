package com.dhht.sld.libs.ocrsdk;

import com.dhht.sld.base.BaseHttpResBean;

public class OCRResultBean extends BaseHttpResBean {
    public resData data;

    public static class resData{

        public String front_url;

        public String back_url;
    }
}
