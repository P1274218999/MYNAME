package com.dhht.sld.main.identity.view;

import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.dhht.sld.R;
import com.dhht.sld.base.BaseActivity;
import com.dhht.sld.base.inject.ViewInject;

@ViewInject(mainLayoutId = R.layout.activity_identity)
public class IdentityActivity extends BaseActivity {

    private FragmentManager fragmentManager;
    private FragmentTransaction transaction;

    @Override
    public void afterBindView() {
        fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .add(R.id.identity_fragment,new IdentityInputFragment(),"IdentityInputFragment")
                .commit();
    }


}
