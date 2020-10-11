package com.dhht.sld.main.guide.view;

import android.content.Intent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import com.dhht.sld.MainActivity;
import com.dhht.sld.R;
import com.dhht.sld.base.BaseFragment;
import com.dhht.sld.base.inject.ViewInject;
import com.tamsiree.rxkit.RxSPTool;


import butterknife.BindView;

/**
 * 作者：pst
 * 邮箱：1274218999@qq.com
 * 创建时间：2020/7/23  14:46
 * 文件描述：引导界面布局
 */
@ViewInject(mainLayoutId = R.layout.fragment_guide_page)
public class GuideFragmentViewPager extends BaseFragment {
    private final String IS_FIRST_INSTALL="IS_FIRST_INSTALL";
    private  int page=1;
    @BindView(R.id.guide_page_img)
    ImageView guidePageImg;
    @BindView(R.id.guide_page_tv)
    TextView getGuidePageTv;
    @BindView(R.id.guide_next_now)
    TextView guideNextNow;
    public GuideFragmentViewPager(int page){
        this.page=page;
    }
    @Override
    public void afterBindView() {
        if (page!=3){
            guideNextNow.setVisibility(View.GONE);
        }
        guideNextNow.setOnClickListener(view -> {

        });

        if (page==1) {
            guidePageImg.setBackgroundResource(R.mipmap.img_guide_page_one);
            getGuidePageTv.setText("精准定位 订单实时跟踪");
        }
        if (page==2){
            guidePageImg.setBackgroundResource(R.mipmap.img_guide_page_two);
            getGuidePageTv.setText("实名认证 不抢单 安全可靠");
        }
        if (page==3){
            guidePageImg.setBackgroundResource(R.mipmap.img_guide_page_three);
            getGuidePageTv.setText("货品当面收 更放心");
        }

        guideNextNow.setOnClickListener(view -> {
            Intent it = new Intent(mContext, MainActivity.class);
            startActivity(it);
            RxSPTool.putBoolean(mContext,IS_FIRST_INSTALL,true);
            getActivity().finish();
        });
    }

}
