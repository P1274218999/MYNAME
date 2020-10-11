package com.dhht.sld.utlis;

import android.content.Context;

import com.dhht.sld.base.helper.ContextHelper;
import com.dhht.sld.main.login.bean.LoginSuccessBean;
import com.google.gson.Gson;
import com.tamsiree.rxkit.RxSPTool;

public class UserSPUtli {

    private static UserSPUtli userSPUtli;
    private static Context mContext;
    private static Gson gson;
    private String TAG = "userData";

    private UserSPUtli() {}

    public static UserSPUtli getInstance()
    {
        if (gson == null) gson = new Gson();

        mContext    = ContextHelper.getInstance().getApplicationContext();
        userSPUtli = UserSPUtliHolder.INSTANCE;

        return userSPUtli;
    }

    private static class UserSPUtliHolder
    {
        private static final UserSPUtli INSTANCE = new UserSPUtli();
    }

    /**
     * 判断是否登录
     * @return
     */
    public boolean isLogin()
    {
        if (getUserData() == null)
        {
            return false;
        }
        return true;
    }

    /**
     * 保持登录信息
     * @param data
     * @return
     */
    public void saveUser(LoginSuccessBean.ResData data)
    {
        if (data == null) return;
        String userData = gson.toJson(data);
        RxSPTool.putContent(mContext, TAG, userData);
    }

    /**
     * 获取用户登录信息
     * @return
     */
    public LoginSuccessBean.ResData getUserData()
    {
        String userDataTAG = RxSPTool.getContent(mContext, TAG);
        if (userDataTAG == "") return null;

        return gson.fromJson(userDataTAG, LoginSuccessBean.ResData.class);
    }

    /**
     * 获取Token
     * @return
     */
    public String getToken()
    {
        if (isLogin())
        {
            return getUserData().token;
        }else {
            return "";
        }
    }

    // 退出登录
    public boolean outLogin()
    {
        RxSPTool.clearPreference(mContext,TAG,null);
        if (isLogin())
        {
            return  false;
        }
        return true;
    }

    // 是否实名认证
    public Boolean isAuthentication(){
        if (isLogin())
        {
            return getUserData().is_identity;
        }else {
            return false;
        }
    }

    public void updateAuthentication(boolean flg) {
        LoginSuccessBean.ResData data = getUserData();
        data.setIs_identity(flg);
        saveUser(data);
    }
}
