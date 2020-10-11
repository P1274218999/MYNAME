package com.dhht.sld.base;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.dhht.mvp.view.LifeCircleMvpFragment;
import com.dhht.sld.R;
import com.dhht.sld.base.inject.AutoArg;
import com.dhht.sld.base.inject.ViewInject;
import com.dhht.sld.utlis.FragmentUtils;
import com.dhht.sld.utlis.GsonUtils;
import com.qmuiteam.qmui.nestedScroll.QMUIContinuousNestedTopRecyclerView;
import com.qmuiteam.qmui.widget.pullLayout.QMUIPullLayout;

import java.lang.reflect.Field;
import java.util.ArrayList;

import butterknife.Unbinder;

public abstract class BaseFragment extends LifeCircleMvpFragment {

    // 模板方法 设计模式
    public abstract void afterBindView();
    protected Activity mActivity;
    protected Context mContext;
    protected View mView;
    private Unbinder unbinder;
    public static long lastRefreshTime;
    private Intent mIntent;
    private Bundle mBundle = new Bundle();
    protected QMUIPullLayout mPullLayout;
    protected QMUIPullLayout.PullAction mPullAction;
    protected RecyclerView mRecyclerView;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        mContext = context;
        mActivity = getActivity();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        injectBundle(getArguments());
        ViewInject annotation = this.getClass().getAnnotation(ViewInject.class);
        if (annotation != null) {
            int mainLayoutId = annotation.mainLayoutId();
            if (mainLayoutId > 0) {
                mView = inflater.inflate(mainLayoutId, null);
                unbinder = Common.bindButterKnifeView(this, mView);
                Common.isLogin(mActivity);
                if (mView.findViewById(R.id.pull_layout) != null) {
                    mPullLayout = mView.findViewById(R.id.pull_layout);
                    mPullLayout.setActionListener(new QMUIPullLayout.ActionListener() {
                        @Override
                        public void onActionTriggered(@NonNull QMUIPullLayout.PullAction pullAction) {
                            mPullAction = pullAction;
                            mPullLayout.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    if (pullAction.getPullEdge() == QMUIPullLayout.PULL_EDGE_TOP) {
                                        onRefreshData();
                                    } else if (pullAction.getPullEdge() == QMUIPullLayout.PULL_EDGE_BOTTOM) {
                                        onLoadMore();
                                    }
                                }
                            }, 500);
                        }
                    });
                }
                if (mView.findViewById(R.id.recycler) != null) {
                    mRecyclerView = mView.findViewById(R.id.recycler);
                    LinearLayoutManager manager = new LinearLayoutManager(mContext);
                    mRecyclerView.setLayoutManager(manager);
                }
                if (mView.findViewById(R.id.toolbar_back) != null) {
                    mView.findViewById(R.id.toolbar_back).setOnClickListener(v -> back());
                }
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
    public void onStart() {
        super.onStart();
        Common.regEventBus(this);
    }

    @Override
    public void onStop() {
        super.onStop();
        Common.unRegEventBus(this);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        if (unbinder != null) {
            unbinder.unbind();
        }
    }


    /**
     * 加载更多
     */
    public void onRefreshData() {

    }

    /**
     * 加载更多
     */
    public void onLoadMore() {

    }


    /****8888888888888888888   注解传参及链式跳转     888888888888888888888888888*/
    public BaseFragment startActivity(Class activity) {
        mIntent = new Intent(getContext(), activity);
        return this;
    }

    public void go() {
        if (mIntent != null) {
            mIntent.putExtras(mBundle);
            startActivity(mIntent);
        }
    }

    public BaseFragment withObject(@Nullable String key, @Nullable Object value) {
        String json = GsonUtils.toJson(value);
        mBundle.putString(key, json);
        return this;
    }

    public BaseFragment withString(@Nullable String key, @Nullable String value) {
        mBundle.putString(key, value);
        return this;
    }

    public BaseFragment withBoolean(@Nullable String key, boolean value) {
        mBundle.putBoolean(key, value);
        return this;
    }

    public BaseFragment withInt(@Nullable String key, int value) {
        mBundle.putInt(key, value);
        return this;
    }

    public BaseFragment withFloat(@Nullable String key, float value) {
        mBundle.putFloat(key, value);
        return this;
    }

    public BaseFragment withIntegerArrayList(@Nullable String key, @Nullable ArrayList<Integer> value) {
        mBundle.putIntegerArrayList(key, value);
        return this;
    }

    public BaseFragment withStringArrayList(@Nullable String key, @Nullable ArrayList<String> value) {
        mBundle.putStringArrayList(key, value);
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
//            logw(e.getMessage());
        }
    }

    public void addFragment(@NonNull Fragment fragment, int containerId) {
        FragmentUtils.add(getParentFragmentManager(), fragment, containerId, false, true);
    }

    public void addFragment(@NonNull Fragment fragment, int containerId, boolean isAddStack) {
        FragmentUtils.add(getParentFragmentManager(), fragment, containerId, false, isAddStack);
    }

    public void replace(@NonNull Fragment fragment) {
        FragmentUtils.replace(FragmentUtils.getTopShow(getParentFragmentManager()), fragment);
    }

    public void back() {
        if (FragmentUtils.getFragments(getParentFragmentManager()).size() <= 1) {
            getActivity().finish();
        } else {
            FragmentUtils.pop(getParentFragmentManager());
        }
    }
}
