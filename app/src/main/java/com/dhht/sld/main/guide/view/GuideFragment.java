package com.dhht.sld.main.guide.view;

import android.view.View;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.dhht.sld.R;
import com.dhht.sld.base.BaseFragment;
import com.dhht.sld.base.inject.ViewInject;
import com.dhht.sld.main.wallet.view.WalletDetailsListFragment;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * 作者：pst
 * 邮箱：1274218999@qq.com
 * 创建时间：2020/7/23  14:46
 * 文件描述：引导界面布局
 */
@ViewInject(mainLayoutId = R.layout.fragment_guide)
public class GuideFragment extends BaseFragment {

    @BindView(R.id.view_pager)
    ViewPager viewPager;

    @BindView(R.id.guide_select_page_one)
    View pageOne;
    @BindView(R.id.guide_select_page_two)
    View pageTwo;
    @BindView(R.id.guide_select_page_three)
    View pageThree;

    private List<BaseFragment> fragmentList = new ArrayList<>(3);

    @Override
    public void afterBindView() {
        initViewPage();
        initViewPageSelect();
    }


    private void initViewPage() {
        fragmentList.add(new GuideFragmentViewPager(1));
        fragmentList.add(new GuideFragmentViewPager(2));
        fragmentList.add(new GuideFragmentViewPager(3));
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
                if (position + 1 == 1) {
                    pageOne.setBackground(mContext.getResources().getDrawable(R.drawable.oval_apptheme));
                    pageTwo.setBackground(mContext.getResources().getDrawable(R.drawable.oval_gray));
                    pageThree.setBackground(mContext.getResources().getDrawable(R.drawable.oval_gray));
                } else if (position + 1 == 2) {
                    pageOne.setBackground(mContext.getResources().getDrawable(R.drawable.oval_gray));
                    pageTwo.setBackground(mContext.getResources().getDrawable(R.drawable.oval_apptheme));
                    pageThree.setBackground(mContext.getResources().getDrawable(R.drawable.oval_gray));
                } else {
                    pageOne.setBackground(mContext.getResources().getDrawable(R.drawable.oval_gray));
                    pageTwo.setBackground(mContext.getResources().getDrawable(R.drawable.oval_gray));
                    pageThree.setBackground(mContext.getResources().getDrawable(R.drawable.oval_apptheme));
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

}
