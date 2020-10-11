package com.dhht.sld.main.wallet.view;

import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.dhht.sld.R;
import com.dhht.sld.base.BaseFragment;
import com.dhht.sld.base.inject.ViewInject;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 作者：pst
 * 邮箱：1274218999@qq.com
 * 创建时间：2020/7/17  10:24
 * 文件描述：交易明细
 */

@ViewInject(mainLayoutId = R.layout.fragment_wallet_details)
public class WalletDetailsFrament extends BaseFragment {

    public String[] title = {"全部", "收入", "支出"};

    @BindView(R.id.detail_tab_layout)
    public TabLayout detailTabLayout;
    @BindView(R.id.toolbar_title)
    TextView commonTitle;
    @BindView(R.id.view_pager)
    ViewPager viewPager;
    private List<BaseFragment> fragmentList = new ArrayList<>(3);

    @Override
    public void afterBindView() {
        commonTitle.setText("交易明细");
        initLayTab();
        initViewPage();
        initTabSelect();
        initViewPageSelect();
    }

    private void initViewPage() {
        fragmentList.add(new WalletDetailsListFragment(0));//全部
        fragmentList.add(new WalletDetailsListFragment(1));//收入
        fragmentList.add(new WalletDetailsListFragment(2));//支出
        viewPager.setOffscreenPageLimit(3);//保持3个fragment 切换时不被销毁
        viewPager.setAdapter(new FragmentPagerAdapter(getChildFragmentManager()) {
            @NonNull
            @Override
            public Fragment getItem(int position) {
                return fragmentList.get(position);
            }

            @Override
            public int getCount() {
                return fragmentList == null ? 0 : fragmentList.size();
            }
        });
        viewPager.setCurrentItem(0);
    }

    private void initViewPageSelect() {
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                viewPager.setCurrentItem(position, false);
                detailTabLayout.getTabAt(position).select();
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    private void initLayTab() {
        for (String name : title) {
            detailTabLayout.addTab(detailTabLayout.newTab().setText(name));
        }
    }

    private void initTabSelect() {
        detailTabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
//                tabIndex = tab.getPosition();
//                changeFragment(tabIndex);
                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
            }
        });
    }


    @OnClick({R.id.toolbar_back})
    public void onClick() {
       back();
    }

}
