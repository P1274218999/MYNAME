package com.dhht.sld.main.wallet.view;

import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.core.content.ContextCompat;

import com.dhht.sld.R;
import com.dhht.sld.base.BaseFragment;
import com.dhht.sld.base.http.retorfit.Converter;
import com.dhht.sld.base.http.retorfit.Result;
import com.dhht.sld.base.http.retorfit.Retorfit;
import com.dhht.sld.base.inject.ViewInject;
import com.dhht.sld.main.choose.pay.bean.PayOrderBean;
import com.dhht.sld.main.findorder.view.FindOrderActivity;
import com.dhht.sld.main.helporder.view.HelpOrderActivity;
import com.dhht.sld.main.system.view.SystemSettingActivity;
import com.dhht.sld.main.wallet.bean.UserMoneyBean;
import com.dhht.sld.utlis.FragmentUtil;
import com.dhht.sld.utlis.IntentUtil;
import com.qmuiteam.qmui.util.QMUIDisplayHelper;
import com.qmuiteam.qmui.util.QMUIResHelper;
import com.qmuiteam.qmui.widget.grouplist.QMUICommonListItemView;
import com.qmuiteam.qmui.widget.grouplist.QMUIGroupListView;

import java.text.DecimalFormat;

import butterknife.BindView;
import butterknife.OnClick;

@ViewInject(mainLayoutId = R.layout.fragment_wallet)
public class WalletFragment extends BaseFragment {

    @BindView(R.id.groupListView)
    QMUIGroupListView mGroupListView;
    @BindView(R.id.toolbar_title)
    TextView commonTitle;
    @BindView(R.id.wallet_balance)
    TextView walltBalance;
    private WalletDetailsFrament walletDetailsFrament=new WalletDetailsFrament();
    private WalleBankCardFragment walleBankCard=new WalleBankCardFragment();
    @Override
    public void afterBindView() {
        commonTitle.setText("我的钱包");
        initItems();
        Retorfit.getService().getAssets().enqueue(new Converter<Result<UserMoneyBean>>() {
            @Override
            public void onSuccess(Result<UserMoneyBean> userMoneyBeanResult) {
                if (userMoneyBeanResult.data!=null){
                    DecimalFormat decimalFormat=new DecimalFormat(".00");
                    walltBalance.setText(decimalFormat.format(userMoneyBeanResult.data.money));
                }
            }
        });
    }

    private void initItems() {
        int height = QMUIResHelper.getAttrDimen(mActivity, com.qmuiteam.qmui.R.attr.qmui_list_item_height);

        QMUICommonListItemView findOrder = mGroupListView.createItemView(
                ContextCompat.getDrawable(mActivity, R.drawable.order_icon),
                "交易明细",
                "",
                QMUICommonListItemView.HORIZONTAL,
                QMUICommonListItemView.ACCESSORY_TYPE_CHEVRON,
                height);
        findOrder.setTipPosition(QMUICommonListItemView.TIP_POSITION_LEFT);
//        findOrder.showRedDot(true);

        QMUICommonListItemView helpOrder = mGroupListView.createItemView(
                ContextCompat.getDrawable(mActivity, R.drawable.order_icon),
                "银行卡管理",
                "",
                QMUICommonListItemView.HORIZONTAL,
                QMUICommonListItemView.ACCESSORY_TYPE_CHEVRON,
                height);
        helpOrder.setTipPosition(QMUICommonListItemView.TIP_POSITION_LEFT);

        QMUICommonListItemView wallet = mGroupListView.createItemView(
                ContextCompat.getDrawable(mActivity, R.drawable.qianbao_icon),
                "提现",
                "",
                QMUICommonListItemView.HORIZONTAL,
                QMUICommonListItemView.ACCESSORY_TYPE_CHEVRON,
                height);
        wallet.setTipPosition(QMUICommonListItemView.TIP_POSITION_LEFT);

        QMUICommonListItemView item3 = mGroupListView.createItemView(
                ContextCompat.getDrawable(mActivity, R.drawable.seting_icon),
                "设置",
                "",
                QMUICommonListItemView.HORIZONTAL,
                QMUICommonListItemView.ACCESSORY_TYPE_CHEVRON,
                height);
        item3.setTipPosition(QMUICommonListItemView.TIP_POSITION_LEFT);

        View.OnClickListener onClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (v instanceof QMUICommonListItemView) {
                    if (((QMUICommonListItemView) v).getAccessoryType() == QMUICommonListItemView.ACCESSORY_TYPE_SWITCH) {
                        ((QMUICommonListItemView) v).getSwitch().toggle();
                    }

                    CharSequence text = ((QMUICommonListItemView) v).getText();
//                    Toast.makeText(getActivity(), text + " is Clicked：", Toast.LENGTH_SHORT).show();

                    if ("交易明细".equals(text)) {
                        addFragment(walletDetailsFrament, R.id.wallet_activtiy_fragment);
                    } else if ("银行卡管理".equals(text)) {
                        addFragment(walleBankCard, R.id.wallet_activtiy_fragment);
                    } else if ("提现".equals(text)) {
                        IntentUtil.get().activity(mActivity, WalleActivity.class);
                    } else if ("设置".equals(text)) {
                        IntentUtil.get().activity(mActivity, SystemSettingActivity.class);
                    } else {
                        ((WalleActivity)mContext).showTip("敬请期待");
                    }
                }
            }
        };

        int size = QMUIDisplayHelper.dp2px(mActivity, 20);
        QMUIGroupListView.newSection(mActivity)
                .setUseTitleViewForSectionSpace(false) // 不显示组标题
                .setLeftIconSize(size, ViewGroup.LayoutParams.WRAP_CONTENT)
                .addItemView(findOrder, onClickListener)
                .addItemView(helpOrder, onClickListener)
                .addItemView(wallet, onClickListener)
//                .addItemView(item3, onClickListener)
                .setShowSeparator(false) //不显示下横线
                .addTo(mGroupListView);
    }
    @OnClick(R.id.toolbar_back)
    public void onClick(){
        getActivity().finish();
    }

}
