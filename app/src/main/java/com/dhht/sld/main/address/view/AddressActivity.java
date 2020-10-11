package com.dhht.sld.main.address.view;

import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.dhht.sld.R;
import com.dhht.sld.base.BaseActivity;
import com.dhht.sld.base.inject.IsLoginInject;
import com.dhht.sld.base.inject.ViewInject;

@IsLoginInject
@ViewInject(mainLayoutId = R.layout.activity_address)
public class AddressActivity extends BaseActivity {

    private FragmentManager fragmentManager;
    private FragmentTransaction transaction;

    @Override
    public void afterBindView() {
        fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .add(R.id.address_fragment,new AddressInputFromFragment(),"AddressInputFromFragment")
                .commit();
    }

    // 切换Fragment
    public void chanceFragment(int type)
    {
        transaction = fragmentManager.beginTransaction();
        switch (type)
        {
            case 1:
                transaction.setCustomAnimations(R.anim.slide_no,R.anim.slide_bottom_out)
                        .replace(R.id.address_fragment,
                                new AddressInputFromFragment(),
                                "AddressInputFromFragment");
                break;
            case 2:
                transaction.setCustomAnimations(R.anim.slide_bottom_in,R.anim.slide_no)
                        .replace(R.id.address_fragment,
                                new AddressChooseMapFragment(),
                                "AddressInputFromFragment");
                break;
            case 3:
                transaction.setCustomAnimations(R.anim.slide_bottom_in,R.anim.slide_no)
                        .replace(R.id.address_fragment,
                                new AddressSearchFragment(),
                                "AddressSearchFragment");
                break;
        }
        transaction.commit();
    }

}
