package com.dhht.sld.main.home.bean;

import com.dhht.sld.base.BaseHttpResBean;

public class UserInfoBean extends BaseHttpResBean {

    public Info data;

    public static class Info {
        public int id;
        public String article_name;
        public String start_address;
        public Double start_latitude;
        public Double start_longitude;
        public Double end_latitude;
        public Double end_longitude;
        public String end_address;
    }
}
