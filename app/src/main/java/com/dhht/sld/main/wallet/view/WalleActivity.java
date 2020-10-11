package com.dhht.sld.main.wallet.view;

import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.dhht.sld.R;
import com.dhht.sld.base.BaseActivity;
import com.dhht.sld.base.inject.ViewInject;
import com.dhht.sld.utlis.FragmentUtil;

/**
 * 作者：pst
 * 邮箱：1274218999@qq.com
 * 创建时间：2020/7/17  9:51
 * 文件描述：钱包管理
 */
@ViewInject(mainLayoutId = R.layout.activity_wallet)
public class WalleActivity extends BaseActivity {
    private WalletFragment walletFragment=new WalletFragment();

    @Override
    public void afterBindView() {
        addFragment(walletFragment,R.id.wallet_activtiy_fragment);
    }
}
