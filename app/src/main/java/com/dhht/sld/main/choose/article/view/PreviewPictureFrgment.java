package com.dhht.sld.main.choose.article.view;

import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.dhht.sld.R;
import com.dhht.sld.base.BaseFragment;
import com.dhht.sld.base.inject.ViewInject;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 作者：pst
 * 邮箱：1274218999@qq.com
 * 创建时间：2020/7/30  11:14
 * 文件描述：图片预览
 */
@ViewInject(mainLayoutId = R.layout.fragment_preview_image)
public class PreviewPictureFrgment extends BaseFragment {
    @BindView(R.id.toolbar_delete)
    TextView deleteBtn;
    @BindView(R.id.preview_position)
    TextView previewPosition;
    @BindView(R.id.view_pager)
    ViewPager viewPager;
    private List<BaseFragment> fragmentList = new ArrayList<>(3);
    private ArrayList<String> pictureUrlList;
    private int position = 0;

    @Override
    public void afterBindView() {
        Bundle arguments = getArguments();
        pictureUrlList = arguments.getStringArrayList("pictureUrlList");
        position = arguments.getInt("position", 0);
        initViewPage(position);
        initViewPageSelect();
    }

    private void initViewPage(int position) {
        fragmentList.clear();
        for (String imgpath : pictureUrlList) {
            fragmentList.add(new PreviewPictureDetailsFrament(imgpath));
        }
        viewPager.setOffscreenPageLimit(3);//保持3个fragment 切换时不被销毁
        FragmentPagerAdapter fragmentPagerAdapter = new FragmentPagerAdapter(getChildFragmentManager()) {
            @NonNull
            @Override
            public Fragment getItem(int position) {
                return fragmentList.get(position);
            }

            @Override
            public int getCount() {
                return fragmentList == null ? 0 : fragmentList.size();
            }
        };
        viewPager.setAdapter(fragmentPagerAdapter);
        viewPager.setCurrentItem(position);
        previewPosition.setText(position + 1 + "/" + pictureUrlList.size());
    }

    private void initViewPageSelect() {
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int pos) {
                position = pos;
                previewPosition.setText(position + 1 + "/" + pictureUrlList.size());
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    @OnClick({R.id.toolbar_back, R.id.toolbar_delete})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.toolbar_back:
                back();
                break;
            case R.id.toolbar_delete:
                pictureUrlList.remove(position);
                if (position>0){
                    position--;
                }
                if (pictureUrlList.size() <= 0) {
                    Intent intent2 = new Intent(mContext, ChooseArticleActivity.class);
                    Bundle mBundle2 = new Bundle();
                    mBundle2.putStringArrayList("pictureUrlList", pictureUrlList);
                    intent2.putExtras(mBundle2);
                    mActivity.setResult(10012, intent2);
                    getActivity().finish();
                }else {
                    initViewPage(position);
                }
                break;
        }
    }

    public void back() {
        Intent intent = new Intent(mContext, ChooseArticleActivity.class);
        Bundle mBundle = new Bundle();
        mBundle.putStringArrayList("pictureUrlList", pictureUrlList);
        intent.putExtras(mBundle);
        mActivity.setResult(10012, intent);
        getActivity().finish();
    }
}
