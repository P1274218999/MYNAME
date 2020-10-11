package com.dhht.sld.base;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.SystemClock;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.dhht.mvp.view.LifeCircleMvpActivity;
import com.dhht.sld.base.inject.AutoArg;
import com.dhht.sld.base.inject.ViewInject;
import com.dhht.sld.utlis.FragmentUtils;
import com.dhht.sld.utlis.GsonUtils;
import com.qmuiteam.qmui.widget.dialog.QMUITipDialog;

import java.lang.reflect.Field;

import butterknife.Unbinder;

public abstract class BaseActivity extends LifeCircleMvpActivity {

    protected Activity mActivity;
    private Unbinder unbinder;
    private QMUITipDialog dialog;
    private Intent mIntent;
    private Bundle mBundle = new Bundle();
    // 模板方法 设计模式
    public abstract void afterBindView();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        injectBundle(getIntent().getExtras());
        mActivity = this;
        ViewInject annotation = this.getClass().getAnnotation(ViewInject.class);
        if (annotation != null) {
            int mainLayoutId = annotation.mainLayoutId();
            if (mainLayoutId > 0) {
                setContentView(mainLayoutId);

                unbinder = Common.bindButterKnife(this);
                Common.isLogin(mActivity);
                Common.initRxTool(mActivity);

                afterBindView();
            } else {
                throw new RuntimeException("mainlayoutid < 0");
            }
        } else {
            throw new RuntimeException("annotation  = null");
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {

            case android.R.id.home:
                super.onBackPressed(); //返回
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void setContentView(View view) {
        super.setContentView(view);
        mActivity = this;
    }

    @Override
    public void setContentView(View view, ViewGroup.LayoutParams params)
    {
        super.setContentView(view, params);
        mActivity = this;
    }

    @Override
    public void onStart()
    {
        super.onStart();
        Common.regEventBus(this);
    }

    @Override
    protected void onStop() {
        super.onStop();
        Common.unRegEventBus(this);
    }

    @Override
    protected void onDestroy()
    {
        super.onDestroy();
        if (unbinder != null) unbinder.unbind();
    }

    @Override
    public void startActivityForResult(Intent intent, int requestCode, @Nullable Bundle options) {
        // 防止多次点击跳转
        if (checkDoubleClick(intent)) {
            super.startActivityForResult(intent, requestCode, options);
        }
     }

    private String mActivityJumpTag;
    private long mClickTime;
    // 检查是否重复跳转，不需要则重写方法并返回true
    private boolean checkDoubleClick(Intent intent) {
        // 默认检查通过
        boolean result = true;
        // 标记对象
        String tag;
        if (intent.getComponent() != null) { // 显式跳转
            tag = intent.getComponent().getClassName();
        }else if (intent.getAction() != null) { // 隐式跳转
            tag = intent.getAction();
        }else {
            return true;
        }
        if (tag.equals(mActivityJumpTag) && mClickTime >= SystemClock.uptimeMillis() - 500) {
            // 检查不通过
            result = false;
        }
        // 记录启动标记和时间
        mActivityJumpTag = tag;
        mClickTime = SystemClock.uptimeMillis();
        return result;
    }


    /**
     * 公共方法
     * -------------------------------------------------------------
     * 公共方法
     * -------------------------------------------------------------
     */
    public void showTip(String msg) {
        hideTipDialog();
        dialog = new QMUITipDialog.Builder(mActivity)
                .setIconType(QMUITipDialog.Builder.ICON_TYPE_NOTHING)
                .setTipWord(msg)
                .create();
        dialog.show();

        new Thread(){
            @Override
            public void run() {
                super.run();
                try {
                    Thread.sleep(1500);
                    hideTipDialog();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }.start();
    }
    public void showSuccessTip(String msg) {
        hideTipDialog();
        dialog = new QMUITipDialog.Builder(mActivity)
                .setIconType(QMUITipDialog.Builder.ICON_TYPE_SUCCESS)
                .setTipWord(msg)
                .create();
        dialog.show();

        new Thread(){
            @Override
            public void run() {
                super.run();
                try {
                    Thread.sleep(1500);
                    hideTipDialog();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }.start();
    }
    public void showFailTip(String msg) {
        hideTipDialog();
        dialog = new QMUITipDialog.Builder(mActivity)
                .setIconType(QMUITipDialog.Builder.ICON_TYPE_FAIL)
                .setTipWord(msg)
                .create();
        dialog.show();

        new Thread(){
            @Override
            public void run() {
                super.run();
                try {
                    Thread.sleep(1500);
                    hideTipDialog();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }.start();
    }
    public void showLoading(String msg) {
        hideTipDialog();
        dialog = new QMUITipDialog.Builder(mActivity)
                .setIconType(QMUITipDialog.Builder.ICON_TYPE_LOADING)
                .setTipWord(msg)
                .create();
        dialog.show();
    }
    public void showLoading(){
        showLoading("加载中...");
    }
    public void hideTipDialog(){
        if (dialog != null)
        {
            dialog.dismiss();
            dialog = null;
        }
    }


    /****8888888888888888888   注解传参及链式跳转     888888888888888888888888888*/

    public BaseActivity startActivity(Class activity) {
        mIntent = new Intent(this, activity);
        return this;
    }
    public void go() {
        if (mIntent != null) {
            mIntent.putExtras(mBundle);
            startActivity(mIntent);
        }
    }
    public void goForResult(int requestCode) {
        if (mIntent != null) {
            mIntent.putExtras(mBundle);
            startActivityForResult(mIntent, requestCode);
        }
    }
    public void goSetResult(int resultCode) {
        mIntent = new Intent();
        mIntent.putExtras(mBundle);
        setResult(resultCode, mIntent);
        finish();
    }
    public BaseActivity withObject(@Nullable String key, @Nullable Object value) {
        String json = GsonUtils.toJson(value);
        mBundle.putString(key, json);
        return this;
    }
    public BaseActivity withString(@Nullable String key, @Nullable String value) {
        mBundle.putString(key, value);
        return this;
    }
    public BaseActivity withBoolean(@Nullable String key, boolean value) {
        mBundle.putBoolean(key, value);
        return this;
    }
    public BaseActivity withInt(@Nullable String key, int value) {
        mBundle.putInt(key, value);
        return this;
    }
    public BaseActivity withDouble(@Nullable String key, @Nullable Double value) {
        mBundle.putDouble(key, value);
        return this;
    }
    public BaseActivity withFloat(@Nullable String key, float value) {
        mBundle.putFloat(key, value);
        return this;
    }
    private void injectBundle(Bundle bundle) {
        if (bundle != null) {
            injectBundle(this, bundle);
        }
    }
    private void injectBundle(Object o, Bundle bundle) {
        try {
            Field[] fields = o.getClass().getDeclaredFields();
            for (Field field : fields) {
                boolean annotationPresent = field.isAnnotationPresent(AutoArg.class);
                if (annotationPresent) {
                    field.setAccessible(true);

                    Object value = bundle.get(field.getName());
                    if (value instanceof String) {
                        String str = (String) value;
                        try {
                            Object obj = GsonUtils.fromJson(str, field.getType());

                            field.set(o, obj);
                        } catch (Exception e) {
                            field.set(o, str);
                        }

                    } else {
                        field.set(o, value);
                    }

                }
            }
        } catch (Exception e) {
            Log.e("injectBundleException", e.getMessage());
        }
    }
    public void addFragment(@NonNull Fragment fragment, int containerId) {
        fragment.setArguments(getIntent().getExtras());
        FragmentUtils.add(getSupportFragmentManager(), fragment, containerId, false, true);
    }
    public void addFragment(@NonNull Fragment fragment, int containerId, boolean isAddStack) {
        fragment.setArguments(getIntent().getExtras());
        FragmentUtils.add(getSupportFragmentManager(), fragment, containerId, false, isAddStack);
    }
    public void replace(@NonNull Fragment fragment) {
        fragment.setArguments(getIntent().getExtras());
        FragmentUtils.replace(FragmentUtils.getTopShow(getSupportFragmentManager()), fragment);
    }
    public void back() {
        FragmentUtils.pop(getSupportFragmentManager());
    }
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        try {
            // 双击退出
            if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN) {
                if (FragmentUtils.getFragments(getSupportFragmentManager()).size()<=1) {
                    finish();
                }else {
                    back();
                }
                return true;
            }
        } catch (Exception ignored) {
        }
        return super.onKeyDown( keyCode, event );
    }
}
