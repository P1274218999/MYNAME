package com.dhht.sld.main.helporder.bean;

import com.dhht.sld.base.BaseHttpResBean;

import java.util.List;

/**
 * 登录返回的用户信息
 */
public class HelpOrderBean extends BaseHttpResBean {

    public List<ResData> data;

    public static class ResData {

        public int id;

        public int find_order_id;

        public int is_status;

        public int status;

        public String article_name;

        public String start_address;

        public String end_address;

        public String yes_time;
    }

}
