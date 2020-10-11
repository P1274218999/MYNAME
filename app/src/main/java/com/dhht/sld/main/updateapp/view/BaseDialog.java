package com.dhht.sld.main.updateapp.view;


import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentManager;
import com.dhht.sld.base.Common;
import com.dhht.sld.base.inject.ViewInject;
import com.dhht.sld.main.updateapp.download.listener.UpdateDialogListener;

import butterknife.Unbinder;

public abstract class BaseDialog extends DialogFragment {

    /**
     * 是否正在显示,防止在特殊情况下弹出多层
     */
    public boolean isShowing;

    // 模板方法 设计模式
    public abstract void afterBindView();
    protected Activity mActivity;
    protected Context mContext;
    protected View mView;
    private Unbinder unbinder;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        mContext  = context;
        mActivity = getActivity();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        ViewInject annotation = this.getClass().getAnnotation(ViewInject.class);
        if (annotation != null) {
            int mainLayoutId = annotation.mainLayoutId();
            if (mainLayoutId > 0) {
                mView = inflater.inflate(mainLayoutId, null);
                unbinder = Common.bindButterKnifeView(this, mView);
                Common.isLogin(mActivity);
                afterBindView();
            } else {
                throw new RuntimeException("mainLayoutId < 0");
            }
        } else {
            throw new RuntimeException("annotation = null");
        }
        return mView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onStart() {
        super.onStart();
        Window win = getDialog().getWindow();
        win.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
    }

    public void finish() {
        if (getActivity() != null) {
            getActivity().finish();
        }
    }
    public void back() {
        if (getFragmentManager() != null) {
            getFragmentManager().popBackStack();
        }
    }

    public void show(FragmentManager fragmentManager) {
        show(fragmentManager, getClass().getSimpleName());
    }

    @Override
    public void show(@NonNull FragmentManager manager, String tag) {
        try {
            if (isShowing) {
                return;
            }
            super.show(manager, tag);
            isShowing = true;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void dismiss() {
        try {
            // 不要使用super.dismiss()，会出现Can not perform this action after onSaveInstanceState异常
            // 当Activity被杀死或者按下Home回调用系统的onSaveInstance(),保存状态后，如果再次执行dismiss()会报错
            super.dismissAllowingStateLoss();
            isShowing = false;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }




}
