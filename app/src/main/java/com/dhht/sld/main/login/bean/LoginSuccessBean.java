package com.dhht.sld.main.login.bean;

import com.dhht.sld.base.BaseHttpResBean;

/**
 * 登录返回的用户信息
 */
public class LoginSuccessBean extends BaseHttpResBean {

    public ResData data;

    public static class ResData {

        public String token = "";

        public int id;

        public int status;

        public String name;

        public String nickname;

        public String phone;

        public String avatar;

        public Boolean is_identity;

        public void setIs_identity(Boolean is_identity) {
            this.is_identity = is_identity;
        }
    }

}
