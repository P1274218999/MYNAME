package com.dhht.sld.main.findorder.bean;

import com.dhht.sld.base.BaseHttpResBean;

import java.util.List;

/**
 * 登录返回的用户信息
 */
public class FindOrderBean extends BaseHttpResBean {

        public int find_order_id;

        public int user_help_order_id;

        public int is_status;

        public int status;

        public String article_name;

        public String start_address;

        public String end_address;

        public String add_time;

}
