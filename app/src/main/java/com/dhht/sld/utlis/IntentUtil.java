package com.dhht.sld.utlis;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Parcelable;

import java.util.List;

public class IntentUtil {

    public static final String OPEN_ACTIVITY_KEY = "options";//intent跳转传递传输key
    private static Intent intent;

    private IntentUtil() {}

    public static IntentUtil get() {
        intent  = new Intent();
        return IntentUtilHolder.INSTANCE;
    }

    /**
     * 内部静态类实现单例线程安全
     */
    private static class IntentUtilHolder {
        private static final IntentUtil INSTANCE = new IntentUtil();
    }


    /**
     * 获取上一个界面传递过来的参数
     *
     * @param activity this
     * @param <T>      泛型
     * @return
     */
    public <T> T getParcelableExtra(Activity activity) {
        Parcelable parcelable = activity.getIntent().getParcelableExtra(OPEN_ACTIVITY_KEY);
        activity = null;
        return (T) parcelable;
    }

    /**
     * 启动一个Activity
     *
     * @param context
     * @param _class
     */
    public void activity(Context context, Class<? extends Activity> _class) {
        intent.setClass(context, _class);
        context.startActivity(intent);
        context = null;
    }

    /**
     * 启动activity后关闭所有页面(不刷新页面)
     */
    public void activityKillAll(Context context, Class<? extends Activity> _class) {
        intent.setClass(context, _class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP); //关掉所要到的界面中间的activity
        intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP); //设置不要刷新将要跳转的界面
        context.startActivity(intent);
        context = null;
    }

    /**
     * 启动activity后关闭所有页面(刷新页面)
     */
    public void activityKillAllRef(Context context, Class<? extends Activity> _class) {
        intent.setClass(context, _class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP); //关掉所有，跳到的界面中间的activity
        context.startActivity(intent);
        context = null;
    }

    /**
     * 启动activity后kill前一个
     *
     * @param context
     * @param _class
     */
    public void activityKill(Context context, Class<? extends Activity> _class) {
        intent.setClass(context, _class);
        context.startActivity(intent);
        ((Activity) context).finish();
        context = null;
    }

    /**
     * 回调跳转
     *
     * @param context
     * @param _class
     * @param requestCode
     */
    public void goActivityResult(Activity context, Class<? extends Activity> _class, int requestCode) {
        intent.setClass(context, _class);
        context.startActivityForResult(intent, requestCode);
        context = null;
    }

    /**
     * 回调跳转并finish当前页面
     *
     * @param context
     * @param _class
     * @param requestCode
     */
    public void goActivityResultKill(Activity context, Class<? extends Activity> _class, int requestCode) {
        intent.setClass(context, _class);
        context.startActivityForResult(intent, requestCode);
        ((Activity) context).finish();
        context = null;
    }

    /**
     * 传递一个序列化实体类
     *
     * @param context
     * @param _class
     * @param parcelable
     */
    public void goActivity(Context context, Class<? extends Activity> _class, Parcelable parcelable) {
        intent.setClass(context, _class);
        putParcelable(parcelable);
        context.startActivity(intent);
        context = null;
    }

    /**
     * 启动一个Activity
     *
     * @param context
     * @param _class
     * @param flags
     * @param parcelable 传递的实体类
     */
    public void goActivity(Context context, Class<? extends Activity> _class, int flags, Parcelable parcelable) {
        intent.setClass(context, _class);
        setFlags(flags);
        putParcelable(parcelable);
        context.startActivity(intent);
        context = null;
    }

    public void goActivityKill(Context context, Class<? extends Activity> _class, Parcelable parcelable) {
        intent.setClass(context, _class);
        putParcelable(parcelable);
        context.startActivity(intent);
        ((Activity) context).finish();
        context = null;
    }

    public void goActivityKill(Context context, Class<? extends Activity> _class, int flags, Parcelable parcelable) {
        intent.setClass(context, _class);
        setFlags(flags);
        putParcelable(parcelable);
        context.startActivity(intent);
        ((Activity) context).finish();
        context = null;
    }

    public void goActivityResult(Activity context, Class<? extends Activity> _class, Parcelable parcelable, int requestCode) {
        intent.setClass(context, _class);
        putParcelable(parcelable);
        context.startActivityForResult(intent, requestCode);
        context = null;
    }

    public void goActivityResult(Activity context, Class<? extends Activity> _class, int flags, Parcelable parcelable, int requestCode) {
        intent.setClass(context, _class);
        putParcelable(parcelable);
        setFlags(flags);
        context.startActivityForResult(intent, requestCode);
        context = null;
    }

    public void goActivityResultKill(Activity context, Class<? extends Activity> _class, Parcelable parcelable, int requestCode) {
        intent.setClass(context, _class);
        putParcelable(parcelable);
        context.startActivityForResult(intent, requestCode);
        context.finish();
        context = null;
    }

    public void goActivityResultKill(Activity context, Class<? extends Activity> _class, int flags, Parcelable parcelable, int requestCode) {
        intent.setClass(context, _class);
        putParcelable(parcelable);
        setFlags(flags);
        context.startActivityForResult(intent, requestCode);
        context.finish();
        context = null;
    }

    private void setFlags(int flags) {
        if (flags < 0) return;
        intent.setFlags(flags);
    }

    private void putParcelable(Parcelable parcelable) {
        if (parcelable == null) return;
        intent.putExtra(OPEN_ACTIVITY_KEY, parcelable);
    }

}
