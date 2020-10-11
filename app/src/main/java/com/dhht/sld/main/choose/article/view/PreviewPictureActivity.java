package com.dhht.sld.main.choose.article.view;

import android.view.KeyEvent;

import com.dhht.sld.R;
import com.dhht.sld.base.BaseActivity;
import com.dhht.sld.base.inject.ViewInject;
import com.dhht.sld.utlis.FragmentUtils;

/**
 * 作者：pst
 * 邮箱：1274218999@qq.com
 * 创建时间：2020/7/30  11:10
 * 文件描述：图片预览
 */
@ViewInject(mainLayoutId = R.layout.activity_base)
public class PreviewPictureActivity extends BaseActivity {
    private PreviewPictureFrgment previewPictureFrgment;

    @Override
    public void afterBindView() {
        previewPictureFrgment = new PreviewPictureFrgment();
        previewPictureFrgment.setArguments(getIntent().getExtras());
        FragmentUtils.add(getSupportFragmentManager(), previewPictureFrgment, R.id.activtiy_base_fragment);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        try {
            // 双击退出
            if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN) {
                previewPictureFrgment.back();
                return false;
            }
        } catch (Exception ignored) {
        }
        return super.onKeyDown(keyCode, event);
    }


}
