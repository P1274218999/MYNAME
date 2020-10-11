package com.dhht.sld.base;

import android.app.Activity;
import android.util.Log;
import android.view.View;

import com.dhht.sld.base.inject.BindEventBusInject;
import com.dhht.sld.base.inject.IsLoginInject;
import com.dhht.sld.main.login.view.LoginActivity;
import com.dhht.sld.utlis.IntentUtil;
import com.dhht.sld.utlis.UserSPUtli;
import com.tamsiree.rxkit.RxTool;

import org.greenrobot.eventbus.EventBus;

import java.util.Random;

import butterknife.ButterKnife;
import butterknife.Unbinder;

public class Common {

    /**
     * 是否需要判断跳登录
     */
    public static void isLogin(Activity context)
    {
        if (context.getClass().isAnnotationPresent(IsLoginInject.class)) {
            Log.e("debug", "判断登录");
            if (!UserSPUtli.getInstance().isLogin())
            {
                IntentUtil.get().activityKill(context, LoginActivity.class);
            }
        }
    }

    // View 的依赖注入绑定
    public static Unbinder bindButterKnife(Activity activity)
    {
        return ButterKnife.bind(activity);
    }

    public static Unbinder bindButterKnifeView(Object target, View mView)
    {
        return ButterKnife.bind(target, mView);
    }

    // 绑定BindEventBus
    public static void regEventBus(Object _this)
    {
        if (_this.getClass().isAnnotationPresent(BindEventBusInject.class)) {
            EventBus.getDefault().register(_this);
            Log.e("debug", "regEventBus-Success");
        }
    }

    // 解绑BindEventBus
    public static void unRegEventBus(Object _this)
    {
        if (_this.getClass().isAnnotationPresent(BindEventBusInject.class)) {
            EventBus.getDefault().unregister(_this);
        }
    }

    // 初始化TxTool工具
    public static void initRxTool(Activity activity)
    {
        RxTool.init(activity);
    }

    public static String getCharAndNumr(int length) {
        Random random = new Random();
        StringBuffer valSb = new StringBuffer();
        String charStr = "0123456789abcdefghijklmnopqrstuvwxyz";
        int charLength = charStr.length();
        for (int i = 0; i < length; i++) {
            int index = random.nextInt(charLength);
            valSb.append(charStr.charAt(index));
        }
        return valSb.toString();
    }
}
