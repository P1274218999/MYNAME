package com.dhht.sld.main.personal.view;

import android.util.Log;

import com.dhht.sld.R;
import com.dhht.sld.base.BaseActivity;
import com.dhht.sld.base.inject.ViewInject;
import com.dhht.sld.utlis.FragmentUtils;

/**
 * 作者：pst
 * 邮箱：1274218999@qq.com
 * 创建时间：2020/7/27  9:26
 * 文件描述：个人信息
 */

@ViewInject(mainLayoutId = R.layout.activity_personal)
public class PersonalActivity extends BaseActivity {

    private PersonalFragment personalFragment=new PersonalFragment();

    @Override
    public void afterBindView() {
        addFragment( personalFragment,R.id.activtiy_personal_fragment);
    }
}
