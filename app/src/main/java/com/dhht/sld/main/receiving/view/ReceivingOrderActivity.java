package com.dhht.sld.main.receiving.view;

import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.TextView;

import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.dhht.sld.MainActivity;
import com.dhht.sld.R;
import com.dhht.sld.base.BaseActivity;
import com.dhht.sld.base.BaseHttpResBean;
import com.dhht.sld.base.inject.ViewInject;
import com.dhht.sld.main.home.bean.MarkerBean;
import com.dhht.sld.main.receiving.contract.ReceivingContract;
import com.dhht.sld.main.receiving.presenter.ReceivingPresenter;
import com.dhht.sld.utlis.IconFontView;
import com.dhht.sld.utlis.IntentUtil;
import com.dhht.sld.utlis.ShowDialog;
import com.dhht.sld.utlis.ToastUtli;
import com.qmuiteam.qmui.util.QMUIStatusBarHelper;
import com.qmuiteam.qmui.widget.roundwidget.QMUIRoundButton;
import com.tamsiree.rxkit.RxDataTool;
import com.tamsiree.rxkit.RxDeviceTool;
import com.tamsiree.rxkit.view.RxToast;

import butterknife.BindView;
import butterknife.OnClick;

@ViewInject(mainLayoutId = R.layout.activity_receiving_order)
public class ReceivingOrderActivity extends BaseActivity implements ReceivingContract.IView {

    @BindView(R.id.gaode_map_receiving)
    FrameLayout gaodeMapReceiving;
    @BindView(R.id.receiving_title)
    TextView receivingTitle;
    @BindView(R.id.receiving_article_name)
    TextView receivingArticleName;
    @BindView(R.id.receiving_tip)
    TextView receivingTip;
    @BindView(R.id.receiving_content_icon)
    IconFontView receivingContentIcon;
    @BindView(R.id.receiving_cancel)
    QMUIRoundButton receivingCancel;
    @BindView(R.id.receiving_quhuo)
    QMUIRoundButton receivingQuhuo;

    private ReceivingPresenter presenter;
    private MarkerBean.Marker marker;
    private int status;
    private double dhLatitude;
    private double dhLongitude;
    private String phone;
    private int distance = -1;

    @Override
    public void afterBindView() {

        presenter = new ReceivingPresenter(this);
        receivingQuhuo.setBackgroundColor(getResources().getColor(R.color.gray));
        init();
    }

    private void init() {
        ReceivingMapFragment receivingMapFragment = new ReceivingMapFragment();
        addFragment(receivingMapFragment,R.id.gaode_map_receiving);
        if (Build.VERSION.SDK_INT >= 21) {
            Window window = getWindow();
            window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
            window.setStatusBarColor(Color.TRANSPARENT);
            receivingMapFragment.setGoBackHeight(QMUIStatusBarHelper.getStatusbarHeight(mActivity));
        }
    }

    @Override
    public void resMarkerInfo(MarkerBean res) {

    }

    // 取消订单后的返回处理
    @Override
    public void resCancelOrder(BaseHttpResBean res) {
        if (res.code > 0) {
            showSuccessTip(res.msg);
            new Thread() {
                @Override
                public void run() {
                    super.run();
                    try {
                        Thread.sleep(1600);
                        hideTipDialog();
                        finish();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }.start();

        } else {
            showFailTip(res.msg);
        }
    }

    // 点击取货的后的返回处理
    @Override
    public void resYesQuhuo(BaseHttpResBean res) {
        if (res.code > 0) {
            showSuccessTip(res.msg);
            new Handler().postDelayed(new Runnable() {
                public void run() {
                    update(marker, 2);
                }
            }, 1600);

        } else {
            showFailTip(res.msg);
        }
    }

    // 点击确定送货按钮返回
    @Override
    public void resYesSongHuo(BaseHttpResBean res) {
        if (res.code > 0) {
            showSuccessTip(res.msg);
            new Handler().postDelayed(new Runnable() {
                public void run() {
                    IntentUtil.get().activityKill(mActivity, MainActivity.class);
                }
            }, 1600);
        } else {
            showFailTip(res.msg);
        }
    }

    // 加载页面时更新状态数据
    public void update(MarkerBean.Marker data, int status) {
        marker = data;
        this.status = status;

        switch (this.status) {
            case 0:
                break;
            case 1: // 取货
                dhLatitude = marker.start_latitude;
                dhLongitude = marker.start_longitude;
                phone = marker.start_phone;

                receivingTitle.setText("等待您去取货");
                receivingTip.setText(data.start_address);
                receivingContentIcon.setText(getResources().getString(R.string.icon_quhuo));
                receivingContentIcon.setTextColor(getResources().getColor(R.color.green));
                showCancelButton();
                break;
            case 2: // 送货
                dhLatitude = marker.end_latitude;
                dhLongitude = marker.end_longitude;
                phone = marker.end_phone;

                receivingTitle.setText("等待您去送货");
                receivingTip.setText(data.end_address);
                receivingContentIcon.setText(getResources().getString(R.string.icon_shonghuo));
                receivingContentIcon.setTextColor(getResources().getColor(R.color.red));
                hideCancelButton();
                updateSongHuoRoutPlay(); // 修改路线
                break;
        }

        receivingArticleName.setText(data.article_name);
        updateQuhuoButton(this.status);
    }

    public void setDistance(int distance) {
        this.distance = distance;
        if (distance <= 100) {
            receivingQuhuo.setBackgroundColor(getResources().getColor(R.color.appTheme));
        } else {
            receivingQuhuo.setBackgroundColor(getResources().getColor(R.color.gray));
        }
    }

    /**
     * 页面点击事件
     *
     * @param view
     */
    @OnClick({
            R.id.receiving_submit_daohang,
            R.id.receiving_cancel,
            R.id.receiving_quhuo,
            R.id.receiving_submit_phone
    })
    public void onClick(View view) {
        switch (view.getId()) {
            // 导航按钮
            case R.id.receiving_submit_daohang:
                Uri uri = Uri.parse("amapuri://route/plan/?dlat=" + dhLatitude + "&dlon=" + dhLongitude + "&dev=0&t=0");
                mActivity.startActivity(new Intent(Intent.ACTION_VIEW, uri));
                break;
            // 取消订单按钮
            case R.id.receiving_cancel:
                new ShowDialog().show(mActivity, "确定取消吗？", new ShowDialog.OnBottomClickListener() {
                    @Override
                    public void positive() {
                        presenter.cancelOrder(marker.find_order_id);
                    }

                    @Override
                    public void negative() {

                    }
                });

                break;
            // 取货按钮
            case R.id.receiving_quhuo:
                if (distance == -1) break;
                if (distance > 100) {
                    ToastUtli.getInstance(mActivity).showTip("距离目的地:" + distance + "米");
                    break;
                }
                ShowDialog dialog = new ShowDialog();
                if (status == 1) {
                    dialog.show(mActivity, "确定已取到货了吗？", new ShowDialog.OnBottomClickListener() {
                        @Override
                        public void positive() {
                            presenter.yesQuhuo(marker.find_order_id);
                        }

                        @Override
                        public void negative() {

                        }
                    });

                } else if (status == 2) {
                    dialog.show(mActivity, "确定货已带到了吗？", new ShowDialog.OnBottomClickListener() {
                        @Override
                        public void positive() {
                            presenter.yesSongHuo(marker.find_order_id);
                        }

                        @Override
                        public void negative() {

                        }
                    });
                }
                break;
            // 拨打电话按钮
            case R.id.receiving_submit_phone:
                if (RxDataTool.isNullString(phone)) {
                    ToastUtli.getInstance(mActivity).showFailTip("系统错误，请稍后再试！");
                } else {
                    RxDeviceTool.dial(mActivity, phone);
                }
                break;
        }
    }

    // 隐藏取消按钮
    private void hideCancelButton() {
        receivingCancel.setVisibility(View.GONE);
    }

    // 显示取消按钮
    private void showCancelButton() {
        receivingCancel.setVisibility(View.VISIBLE);
    }

    // 修改提示文案
    private void updateQuhuoButton(int status) {
        if (status == 1) {
            receivingQuhuo.setText("确定取货");
        } else if (status == 2) {
            receivingQuhuo.setText("确定已送到");
        }
    }

    // 修改路线导航
    private void updateSongHuoRoutPlay() {
        ReceivingMapFragment mapFragment = (ReceivingMapFragment) getSupportFragmentManager().findFragmentByTag("FindOrderDetailMapFragment");
        if (mapFragment == null) return;
        mapFragment.routePlay(marker.start_latitude, marker.start_longitude, marker.end_latitude, marker.end_longitude, 2);
    }
}
