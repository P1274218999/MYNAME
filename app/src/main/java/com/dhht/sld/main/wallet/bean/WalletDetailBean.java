package com.dhht.sld.main.wallet.bean;

import com.dhht.sld.base.BaseHttpResBean;

import java.util.List;

/**
 * 作者：pst
 * 邮箱：1274218999@qq.com
 * 创建时间：2020/7/18  9:45
 * 文件描述：交易明细详情列表
 */
public class WalletDetailBean extends BaseHttpResBean {

    public resData data;

    public static class resData{

        public List<Info> list;

        public int total;

    }

    public static class Info{
        /**
         * freeze : 0
         * money : 1000.00
         * user_id : 428
         * comment : test
         * id : 179
         * time : 2020-04-28 17:50:51
         * type : 1
         * status : 1
         */
        public int freeze;
        public String money;
        public int user_id;
        public String comment;
        public int id;
        public String time;
        public int type;
        public int status;
    }

}
