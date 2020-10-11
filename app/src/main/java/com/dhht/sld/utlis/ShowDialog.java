package com.dhht.sld.utlis;

import android.content.Context;

public class ShowDialog {
    private FamilyDialog customDialog;

    public ShowDialog() {

    }
    public void show(final Context context, String message, final OnBottomClickListener onBottomClickListener) {
        customDialog = new FamilyDialog(context);
        customDialog.setMessage(message);
        customDialog.setYesOnClickListener("确定", new FamilyDialog.onYesOnClickListener() {
            @Override
            public void onYesClick() {
                if (onBottomClickListener != null) {
                    onBottomClickListener.positive();
                }
                customDialog.dismiss();
            }
        });

        customDialog.setNoOnClickListener("取消", new FamilyDialog.onNoClickListener() {
            @Override
            public void onNoClick() {
                if (onBottomClickListener != null) {
                    onBottomClickListener.negative();
                }
                customDialog.dismiss();
            }
        });
        customDialog.show();

    }
    public void show2(final Context context, String message,String confirm, final OnBottomClickListener onBottomClickListener) {
        customDialog = new FamilyDialog(context);
        customDialog.setMessage(message);
        customDialog.setYesOnClickListener(confirm, new FamilyDialog.onYesOnClickListener() {
            @Override
            public void onYesClick() {
                if (onBottomClickListener != null) {
                    onBottomClickListener.positive();
                }
                customDialog.dismiss();
            }
        });

        customDialog.setNoOnClickListener("取消", new FamilyDialog.onNoClickListener() {
            @Override
            public void onNoClick() {
                if (onBottomClickListener != null) {
                    onBottomClickListener.negative();
                }
                customDialog.dismiss();
            }
        });
        customDialog.show();

    }
    public interface OnBottomClickListener {
        void positive();

        void negative();

    }
}
