package com.dhht.sld.main.updateapp.bean;

import com.dhht.sld.base.BaseHttpResBean;

import java.util.List;

/**
 * 作者：pst
 * 邮箱：1274218999@qq.com
 * 创建时间：2020/7/22  17:23
 * 文件描述：版本信息
 */
public class CheckVersionBean extends BaseHttpResBean
{
    public CheckVersionBean.resData data;
    public static class resData{
        /**
         * apk_url : http://imgs.donghanhengtai.com/app/dhht-v1.1.3.0.apk
         * app_name : dhht-sld
         * force : 1
         * id : 2
         * wgt_url : null
         * version : 1.0.1
         * add_time : 1595466654
         * content : ["1.版本更新","2.修复bug"]
         */
        public String apk_url;
        public String app_name;
        public int force;
        public int id;
        public String wgt_url;
        public String version;
        public int add_time;
        public List<String> content;
    }
}
