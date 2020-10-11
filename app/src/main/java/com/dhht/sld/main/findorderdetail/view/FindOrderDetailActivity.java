package com.dhht.sld.main.findorderdetail.view;

import android.graphics.Color;
import android.os.Build;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.bumptech.glide.Glide;
import com.dhht.sld.R;
import com.dhht.sld.base.BaseActivity;
import com.dhht.sld.base.inject.ViewInject;
import com.dhht.sld.main.findorderdetail.bean.FindOrderDetailBean;
import com.dhht.sld.main.receiving.view.ReceivingMapFragment;
import com.dhht.sld.utlis.IconFontView;
import com.dhht.sld.utlis.ToastUtli;
import com.qmuiteam.qmui.util.QMUIStatusBarHelper;
import com.tamsiree.rxkit.RxDataTool;
import com.tamsiree.rxkit.RxDeviceTool;


import butterknife.BindView;
import butterknife.OnClick;

@ViewInject(mainLayoutId = R.layout.activity_find_order_detail)
public class FindOrderDetailActivity extends BaseActivity {


    @BindView(R.id.find_order_detail_title)
    TextView findOrderDetailTitle;
    @BindView(R.id.find_order_detail_content_icon)
    IconFontView findOrderDetailContentIcon;
    @BindView(R.id.find_order_detail_article_name)
    TextView findOrderDetailArticleName;
    @BindView(R.id.find_order_user_avatar)
    ImageView findOrderUserAvatar;
    private int status;
    private FindOrderDetailBean.Data data;
    private String phone;
    private FindOrderDetailMapFragment mapFragment = new FindOrderDetailMapFragment();

    @Override
    public void afterBindView() {
        init();
    }


    private void init() {
        addFragment(mapFragment,R.id.gaode_map_find_order_detail);
        if (Build.VERSION.SDK_INT >= 21) {
            Window window = getWindow();
            window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
            window.setStatusBarColor(Color.TRANSPARENT);
            mapFragment.setGoBackHeight(QMUIStatusBarHelper.getStatusbarHeight(mActivity));
        }
    }
    public void update(FindOrderDetailBean.Data data) {
        this.status = data.status;
        this.data = data;
        phone = data.phone;
        // 替换头像
        if (!RxDataTool.isNullString(data.avatar)) {
            Glide.with(mActivity).load(data.avatar).into(findOrderUserAvatar);
        }
        // 设置商品名字
        findOrderDetailArticleName.setText(data.article_name);
        switch (this.status) {
            case 0:
                break;
            case 1: // 待取货
                findOrderDetailTitle.setText("已有人接单，等待取货中...");
                findOrderDetailContentIcon.setText(getResources().getString(R.string.icon_quhuo));
                findOrderDetailContentIcon.setTextColor(getResources().getColor(R.color.green));
                break;
            case 2: // 待送货
                findOrderDetailTitle.setText("已取到货，等待送货中...");
                findOrderDetailContentIcon.setText(getResources().getString(R.string.icon_shonghuo));
                findOrderDetailContentIcon.setTextColor(getResources().getColor(R.color.red));
                updateSongHuoRoutPlay(); // 修改路线
                break;
            case 3: // 已完成
                findOrderDetailTitle.setText("已完成，感谢您的参与");
                findOrderDetailContentIcon.setText(getResources().getString(R.string.icon_shonghuo));
                findOrderDetailContentIcon.setTextColor(getResources().getColor(R.color.blue));
                updateSongHuoRoutPlay(); // 修改路线
                break;
        }

    }


    private void updateSongHuoRoutPlay() {
        if (mapFragment == null) return;
        mapFragment.routePlay(data.start_latitude, data.start_longitude, data.end_latitude, data.end_longitude, 2);
    }

    @OnClick(R.id.find_order_detail_call_phone)
    public void onClick() {
        if (RxDataTool.isNullString(phone)) {
            ToastUtli.getInstance(mActivity).showFailTip("系统错误，请稍后再试！");
        } else {
            RxDeviceTool.dial(mActivity, phone);
        }
    }
}
