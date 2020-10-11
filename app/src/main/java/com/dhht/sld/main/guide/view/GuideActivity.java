package com.dhht.sld.main.guide.view;

import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.dhht.sld.R;
import com.dhht.sld.base.BaseActivity;
import com.dhht.sld.base.inject.ViewInject;
import com.dhht.sld.main.wallet.view.WalleBankCardAddFragment;
import com.dhht.sld.main.wallet.view.WalleBankCardFragment;
import com.dhht.sld.main.wallet.view.WalletDetailsFrament;
import com.dhht.sld.main.wallet.view.WalletFragment;
import com.dhht.sld.utlis.FragmentUtil;

/**
 * 作者：pst
 * 邮箱：1274218999@qq.com
 * 创建时间：2020/7/17  9:51
 * 文件描述：引导界面
 */
@ViewInject(mainLayoutId = R.layout.activity_guide)
public class GuideActivity extends BaseActivity {
    private FragmentManager fragmentManager;
    private FragmentTransaction transaction;
    private GuideFragment guideFragment=new GuideFragment();
    public void afterBindView() {
        fragmentManager = getSupportFragmentManager();
        replaceFragment(1);
    }
    public void replaceFragment(int type)
    {
        transaction = fragmentManager.beginTransaction();
        switch (type)
        {
            case 1://我的钱包啊
                FragmentUtil.replace(fragmentManager,R.id.guide_activtiy_fragment,guideFragment,"GuideFragment");
                break;
        }
        transaction.commit();
    }


}
