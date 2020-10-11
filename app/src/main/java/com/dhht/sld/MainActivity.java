package com.dhht.sld;

import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.view.GravityCompat;
import androidx.core.widget.NestedScrollView;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.dhht.sld.base.BaseActivity;
import com.dhht.sld.base.http.retorfit.Converter;
import com.dhht.sld.base.http.retorfit.Result;
import com.dhht.sld.base.http.retorfit.Retorfit;
import com.dhht.sld.base.inject.ViewInject;
import com.dhht.sld.main.choose.pay.view.PayActivity;
import com.dhht.sld.main.updateapp.CheckUpdateApp;
import com.dhht.sld.main.home.contract.MainContract;
import com.dhht.sld.main.home.presenter.MainPresenter;
import com.dhht.sld.main.home.view.MainBottomFragment;
import com.dhht.sld.main.home.view.MainMapFragment;
import com.dhht.sld.main.home.view.UserLeftFragment;
import com.dhht.sld.main.login.bean.LoginSuccessBean;
import com.dhht.sld.main.login.view.LoginActivity;
import com.dhht.sld.main.notification.view.NotificationActivity;
import com.dhht.sld.utlis.IconFontView;
import com.dhht.sld.utlis.IntentUtil;
import com.dhht.sld.utlis.UserSPUtli;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.tabs.TabLayout;

import butterknife.BindView;
import butterknife.OnClick;
import retrofit2.Response;

@ViewInject(mainLayoutId = R.layout.activity_main)
public class MainActivity extends BaseActivity implements MainContract.IView {

    public String[] title = {"找人带", "帮人带"};
    @BindView(R.id.bottom_sheet)
    NestedScrollView bottomSheet;
    @BindView(R.id.main_user_icon)
    IconFontView mainUserIcon;
    @BindView(R.id.main_message_icon)
    IconFontView mainMessageIcon;
    @BindView(R.id.main_message_read_logo)
    View readLogo;
    @BindView(R.id.user_left_draw)
    DrawerLayout userLeftDraw;
    @BindView(R.id.main_tab_layout)
    TabLayout mainTabLayout;

    private FragmentManager fragmentManager;
    private FragmentTransaction transaction;
    private BottomSheetBehavior behavior;
    private boolean isLogin = false;
    private int tabIndex = 0;

    @Override
    public void onStart() {
        super.onStart();
        isLogin = UserSPUtli.getInstance().isLogin();
        if (!isLogin) {
            closeDrawer();
        }
        changeFragment(tabIndex);
    }

    @Override
    public void afterBindView()
    {
        fragmentManager = getSupportFragmentManager();
        transaction = fragmentManager.beginTransaction();
        transaction.add(R.id.gaode_map_index, new MainMapFragment(), "MainMapFragment");
        transaction.add(R.id.bottom_scroll, new MainBottomFragment(), "MainBottomFragment");
        transaction.add(R.id.fragment_user_left, new UserLeftFragment(), "UserLeftFragment");
        transaction.commit();

        initLayTab();
        initUserLeft(); //初始化DrawerLayout
        initBehavior();
        initTabSelect();
        initUser();
        new CheckUpdateApp(this,null);
    }

    @Override
    protected void onResume() {
        super.onResume();
        Retorfit.getService().checkStatus(2).enqueue(new Converter<Result<Integer>>() {
            @Override
            public void onSuccess(Result<Integer> result) {
                int count= result.data;
                if (count>0){
                    readLogo.setVisibility(View.VISIBLE);
                    return;
                }
                readLogo.setVisibility(View.GONE);
            }
            @Override
            protected void onError(String err, Response<Result<Integer>> resp) {
                super.onError(err, resp);
                readLogo.setVisibility(View.GONE);
            }
        });
    }

    // 获取用户信息
    private void initUser()
    {
        MainPresenter mainPresenter = new MainPresenter(this);
        mainPresenter.getUser();
    }

    private void initLayTab()
    {
        for (String name : title)
        {
            mainTabLayout.addTab(mainTabLayout.newTab().setText(name));
        }
    }

    public void initBehavior()
    {
        behavior = BottomSheetBehavior.from(bottomSheet);
        behavior.addBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View bottomSheet, int newState) {
                String state = null;
                switch (newState) {
                    case 1: //被拖拽状态
                        state = "STATE_DRAGGING";
                        break;
                    case 2: //拖拽松开之后到达终点位置
                        state = "STATE_SETTLING";
                        break;
                    case 3: //完全展开的状态
                        state = "STATE_EXPANDED";
                        break;
                    case 4: //折叠状态
                        state = "STATE_COLLAPSED";
                        break;
                    case 5: //隐藏状态。默认是false，可通过app:behavior_hideable属性设置
                        state = "STATE_HIDDEN";
                        break;
                }
            }

            @Override
            public void onSlide(@NonNull View bottomSheet, float slideOffset) {
//                Log.e("debug", "slideOffset"+slideOffset);
            }
        });
    }

    @OnClick({R.id.main_user_icon, R.id.main_message_icon})
    public void onClick(View view)
    {
        switch (view.getId()) {
            case R.id.main_user_icon:
                if (isLogin){
                    userLeftDraw.openDrawer(GravityCompat.START);
                }else{
                    IntentUtil.get().activity(mActivity, LoginActivity.class);
                }

                break;
            case R.id.main_message_icon:
                IntentUtil.get().activity(mActivity, NotificationActivity.class);
                break;
        }
    }
    private boolean drawerIsOpen=false;
    public void initUserLeft()
    {
        // 关闭滑动显示
        closeDrawer();
        userLeftDraw.addDrawerListener(new DrawerLayout.DrawerListener() {
            @Override
            public void onDrawerSlide(@NonNull View drawerView, float slideOffset) {

            }

            @Override
            public void onDrawerOpened(@NonNull View drawerView) {
                drawerIsOpen=true;
            }

            @Override
            public void onDrawerClosed(@NonNull View drawerView) {
                drawerIsOpen=false;
            }

            @Override
            public void onDrawerStateChanged(int newState) {

            }
        });
    }

    public void closeDrawer(){
        userLeftDraw.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
    }

    /**
     * 设置底部behavior的默认高度
     * @param height
     */
    public void setBottomSheetHeight(int height)
    {
        if (behavior != null)
        {
            behavior.setPeekHeight(height);
        }else{
            Log.e("debug", "behavior is null");
        }
    }

    private void initTabSelect()
    {
        mainTabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                tabIndex = tab.getPosition();
                changeFragment(tabIndex);
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }

    //
    private void changeFragment(int index){
        // 初始化地图
        MainMapFragment fragment = (MainMapFragment) getSupportFragmentManager().findFragmentByTag("MainMapFragment");
        fragment.initMapInfo(index);

        // 切换底部Fragment
        MainBottomFragment mainBottomFragment = (MainBottomFragment) getSupportFragmentManager().findFragmentByTag("MainBottomFragment");
        mainBottomFragment.initChangeFragment(index);
    }

    @Override
    public void resUser(LoginSuccessBean res) {
        if (res.code <= 0) UserSPUtli.getInstance().outLogin();
    }
    private long exitTime = 0;
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        try {
            // 双击退出
            if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN) {
                if (drawerIsOpen){
                    userLeftDraw.closeDrawers();
                    return false;
                }

                if ((System.currentTimeMillis() - exitTime) > 2000) {
                    Toast.makeText( getApplicationContext(), "再按一次退出程序", Toast.LENGTH_SHORT ).show();
                    exitTime = System.currentTimeMillis();
                } else {
                    finish();
                    return false;
                }
                return true;
            }
        } catch (Exception ignored) {
        }
        return super.onKeyDown( keyCode, event );
    }
}
