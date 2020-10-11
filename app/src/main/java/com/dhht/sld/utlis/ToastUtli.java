package com.dhht.sld.utlis;

import android.content.Context;

import com.dhht.sld.base.helper.ContextHelper;
import com.qmuiteam.qmui.widget.dialog.QMUITipDialog;

/**
 * ---------------------------------------------------------------
 * 作者：HanSheng
 * 邮箱：164897033@qq.com
 * 创建时间：2020/7/22  9:39
 * 文件描述：tip弹窗提示
 * ---------------------------------------------------------------
 */
public class ToastUtli {

    private static ToastUtli toastUtli;
    private static Context mContext;
    private QMUITipDialog dialog;

    private ToastUtli() {}

    public static ToastUtli getInstance(Context context){
        mContext   = context; //ContextHelper.getInstance().getApplicationContext();
        toastUtli = ToastUtliHolder.INSTANCE;
        return toastUtli;
    }

    private static class ToastUtliHolder
    {
        private static final ToastUtli INSTANCE = new ToastUtli();
    }

    public void showTip(String msg) {
        hideTipDialog();
        dialog = new QMUITipDialog.Builder(mContext)
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
        dialog = new QMUITipDialog.Builder(mContext)
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
        dialog = new QMUITipDialog.Builder(mContext)
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
        dialog = new QMUITipDialog.Builder(mContext)
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
}
