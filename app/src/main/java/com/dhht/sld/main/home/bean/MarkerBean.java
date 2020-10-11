package com.dhht.sld.main.home.bean;

import com.dhht.sld.base.BaseHttpResBean;

import java.util.ArrayList;

public class MarkerBean extends BaseHttpResBean {

    public Marker data;

    public static class Marker {
        public int find_order_id;
        public int status;
        public String article_name;
        public String start_name;
        public String start_phone;
        public String start_address;
        public Double start_latitude;
        public Double start_longitude;
        public String end_name;
        public String end_phone;
        public String end_address;
        public Double end_latitude;
        public Double end_longitude;
    }
}
