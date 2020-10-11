package com.dhht.sld.main.system.view;

import android.widget.TextView;

import androidx.fragment.app.FragmentManager;

import com.dhht.sld.R;
import com.dhht.sld.base.BaseActivity;
import com.dhht.sld.base.inject.ViewInject;
import com.dhht.sld.utlis.FragmentUtil;

import butterknife.BindView;
import butterknife.OnClick;

@ViewInject(mainLayoutId = R.layout.activity_system_setting)
public class SystemSettingActivity extends BaseActivity {

    private SystemSettingMainFragment mainFragment = new SystemSettingMainFragment();
    @BindView(R.id.toolbar_title)
    public TextView title;
    @Override
    public void afterBindView() {
        title.setText("设置");
        addFragment(mainFragment, R.id.system_setting_main);
    }

    @OnClick(R.id.toolbar_back)
    public void onClick() {
        finish();
    }
}
