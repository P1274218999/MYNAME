package com.dhht.sld.main.choose.article.view;

import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.dhht.sld.R;
import com.dhht.sld.base.BaseFragment;
import com.dhht.sld.base.inject.ViewInject;
import com.dhht.sld.main.wallet.view.WalleActivity;
import com.dhht.sld.main.wallet.view.WalletDetailsListFragment;
import com.dhht.sld.utlis.view.PinchImageView;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 作者：pst
 * 邮箱：1274218999@qq.com
 * 创建时间：2020/7/17  10:24
 * 文件描述：图片预览详细界面
 */

@ViewInject(mainLayoutId = R.layout.fragment_preview_picture_details)
public class PreviewPictureDetailsFrament extends BaseFragment {

    private final String imgPath;
    @BindView(R.id.PreviewImageView)
    PinchImageView pinchImageView;

    public PreviewPictureDetailsFrament(String imgPath) {
        this.imgPath = imgPath;
    }

    @Override
    public void afterBindView() {
        Glide.with(mActivity)
                .load(imgPath)
                .apply(new RequestOptions().skipMemoryCache(true))
                .into(pinchImageView);
    }

}
