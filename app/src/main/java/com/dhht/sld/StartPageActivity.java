package com.dhht.sld;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.WindowManager;

import androidx.annotation.Nullable;

import com.dhht.sld.base.BasePermission;
import com.dhht.sld.main.guide.view.GuideActivity;
import com.tamsiree.rxkit.RxSPTool;

public class StartPageActivity extends BasePermission {

    private boolean isFirstInstall=false;
    private final String IS_FIRST_INSTALL="IS_FIRST_INSTALL";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTranslucent(this);
        isFirstInstall = RxSPTool.getBoolean(this, IS_FIRST_INSTALL);

    }

    // 设置状态栏透明
    public static void setTranslucent(Activity activity) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            activity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }
    }

    @Override
    public void checkPermissionsCompele() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (!isFirstInstall){
                    Intent it = new Intent(getApplicationContext(), GuideActivity.class);
                    startActivity(it);
                    finish();
                }else {
                    Intent it = new Intent(getApplicationContext(), MainActivity.class);
                    startActivity(it);
                    finish();
                }
            }
        },800);

    }
}
