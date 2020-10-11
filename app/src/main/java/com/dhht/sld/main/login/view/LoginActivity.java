package com.dhht.sld.main.login.view;

import android.view.View;

import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.dhht.sld.R;
import com.dhht.sld.base.BaseActivity;
import com.dhht.sld.base.inject.ViewInject;

import butterknife.OnClick;

@ViewInject(mainLayoutId = R.layout.activity_login)
public class LoginActivity extends BaseActivity {

    private FragmentManager fragmentManager;
    private FragmentTransaction transaction;

    @Override
    public void afterBindView() {
        fragmentManager = getSupportFragmentManager();
        replaceFragment(1);
    }

    public void replaceFragment(int type)
    {
        transaction = fragmentManager.beginTransaction();
        switch (type)
        {
            case 1:
                transaction.replace(R.id.login_activtiy_fragment, new LoginPhoneFragment(), "LoginGetCodeFragment");
                break;
            case 2:
                transaction.replace(R.id.login_activtiy_fragment, new LoginGetCodeFragment(), "LoginGetCodeFragment");
                break;
        }
        transaction.commit();
    }

    @OnClick({R.id.go_back})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.go_back:
                finish();
                break;
        }
    }

}
