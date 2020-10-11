package com.dhht.sld.main.notification.view;

import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.dhht.sld.R;
import com.dhht.sld.base.BaseActivity;
import com.dhht.sld.base.inject.ViewInject;
import com.dhht.sld.utlis.FragmentUtil;
import com.dhht.sld.utlis.FragmentUtils;

/**
 * 作者：pst
 * 邮箱：1274218999@qq.com
 * 创建时间：2020/7/21  14:35
 * 文件描述：消息通知界面
 */
@ViewInject(mainLayoutId = R.layout.activity_notification)
public class NotificationActivity extends BaseActivity {

    private NotificationFragment notificationFragment=new NotificationFragment();
    @Override
    public void afterBindView() {
        addFragment(notificationFragment, R.id.notification_fragment_layout);
    }
}
