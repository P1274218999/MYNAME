package com.dhht.sld.main.findorderdetail.bean;

import com.dhht.sld.base.BaseHttpResBean;

import java.util.List;

/**
 * 登录返回的用户信息
 */
public class FindOrderDetailBean extends BaseHttpResBean {

    public Data data;

    public static class Data {

        public int id;

        public int find_order_id;

        public int is_status;

        public int status;

        public String phone;

        public String avatar;

        public String article_name;

        public String start_address;

        public double start_latitude;

        public double start_longitude;

        public String end_address;

        public double end_latitude;

        public double end_longitude;

        public String yes_time;

    }

}
