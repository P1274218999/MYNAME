package com.dhht.sld.main.home.bean;

import com.dhht.sld.base.BaseHttpResBean;

public class CurrentOrderBean extends BaseHttpResBean {

    public Data data;

    public static class Data {

        public int user_help_order;

        public int find_order_id;

        public int status;

    }
}
