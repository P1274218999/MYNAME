package com.dhht.sld.base;

import android.app.Application;

import com.dhht.sld.base.crash.CrashProtectManager;
import com.dhht.sld.base.helper.ContextHelper;

/**
 *
 */
public class SldDhhtApplication extends Application{

    @Override
    public void onCreate() {
        super.onCreate();
        //全局Context获取类
        ContextHelper.getInstance().init(this);

        // 日志写入本地文件
//        CrashProtectManager.getInstance(this).init();

    }
}
