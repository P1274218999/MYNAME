package com.dhht.sld.main.home.view;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.core.content.ContextCompat;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.dhht.sld.R;
import com.dhht.sld.base.BaseFragment;
import com.dhht.sld.base.inject.ViewInject;
import com.dhht.sld.main.findorder.view.FindOrderActivity;
import com.dhht.sld.main.helporder.view.HelpOrderActivity;
import com.dhht.sld.main.identity.view.IdentityActivity;
import com.dhht.sld.main.login.bean.LoginSuccessBean;
import com.dhht.sld.main.personal.view.PersonalActivity;
import com.dhht.sld.main.system.view.SystemSettingActivity;
import com.dhht.sld.main.wallet.view.WalleActivity;
import com.dhht.sld.utlis.IconFontView;
import com.dhht.sld.utlis.IntentUtil;
import com.dhht.sld.utlis.UserSPUtli;
import com.qmuiteam.qmui.util.QMUIDisplayHelper;
import com.qmuiteam.qmui.util.QMUIResHelper;
import com.qmuiteam.qmui.widget.QMUIRadiusImageView2;
import com.qmuiteam.qmui.widget.grouplist.QMUICommonListItemView;
import com.qmuiteam.qmui.widget.grouplist.QMUIGroupListView;
import com.tamsiree.rxkit.RxDataTool;

import butterknife.BindView;
import butterknife.OnClick;

@ViewInject(mainLayoutId = R.layout.user_left)
public class UserLeftFragment extends BaseFragment {

    @BindView(R.id.groupListView)
    QMUIGroupListView mGroupListView;
    @BindView(R.id.user_img)
    QMUIRadiusImageView2 userImg;
    @BindView(R.id.user_name)
    TextView userName;
    @BindView(R.id.user_phone)
    TextView userPhone;

    private LoginSuccessBean.ResData userData;

    @Override
    public void afterBindView() {
        initItems();
    }

    @Override
    public void onResume() {
        super.onResume();
        userData = UserSPUtli.getInstance().getUserData();
        initUserData();
    }

    private void initItems() {
        int height = QMUIResHelper.getAttrDimen(mContext, com.qmuiteam.qmui.R.attr.qmui_list_item_height);

        QMUICommonListItemView findOrder = mGroupListView.createItemView(
                ContextCompat.getDrawable(mContext, R.mipmap.ic_user_left_order_dai),
                "找人带订单",
                "",
                QMUICommonListItemView.HORIZONTAL,
                QMUICommonListItemView.ACCESSORY_TYPE_CHEVRON,
                height);
        findOrder.setTipPosition(QMUICommonListItemView.TIP_POSITION_LEFT);
//        findOrder.showRedDot(true);

        QMUICommonListItemView helpOrder = mGroupListView.createItemView(
                ContextCompat.getDrawable(mContext, R.mipmap.ic_user_left_order_bang),
                "帮人带订单",
                "",
                QMUICommonListItemView.HORIZONTAL,
                QMUICommonListItemView.ACCESSORY_TYPE_CHEVRON,
                height);
        helpOrder.setTipPosition(QMUICommonListItemView.TIP_POSITION_LEFT);

        QMUICommonListItemView wallet = mGroupListView.createItemView(
                ContextCompat.getDrawable(mContext, R.mipmap.ic_user_left_order_wallet),
                "钱包",
                "",
                QMUICommonListItemView.HORIZONTAL,
                QMUICommonListItemView.ACCESSORY_TYPE_CHEVRON,
                height);
        wallet.setTipPosition(QMUICommonListItemView.TIP_POSITION_LEFT);
//        wallet.showRedDot(true);

        QMUICommonListItemView item3 = mGroupListView.createItemView(
                ContextCompat.getDrawable(mContext, R.mipmap.ic_user_left_order_settings),
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

                    if ("找人带订单".equals(text)) {
                        IntentUtil.get().activity(mContext, FindOrderActivity.class);
                    } else if ("帮人带订单".equals(text)) {
                        IntentUtil.get().activity(mContext, HelpOrderActivity.class);
                    } else if ("钱包".equals(text)) {
                        IntentUtil.get().activity(mContext, WalleActivity.class);
                    } else if ("设置".equals(text)) {
                        IntentUtil.get().activity(mContext, SystemSettingActivity.class);
                    }
                }
            }
        };

        int size = QMUIDisplayHelper.dp2px(mContext, 20);
        QMUIGroupListView.newSection(getContext())
                .setUseTitleViewForSectionSpace(false) // 不显示组标题
                .setLeftIconSize(size, ViewGroup.LayoutParams.WRAP_CONTENT)
                .addItemView(findOrder, onClickListener)
                .addItemView(helpOrder, onClickListener)
                .addItemView(wallet, onClickListener)
                .addItemView(item3, onClickListener)
                .setShowSeparator(false) //不显示下横线
                .addTo(mGroupListView);
    }

    public Drawable getIcon(String iconCode) {
        TextView view = new IconFontView(mContext);
        view.setText(iconCode); //&#xe633;
        view.setTextSize(30);
        view.setDrawingCacheEnabled(true);
        view.measure(View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED), View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
        view.layout(0, 0, view.getMeasuredWidth(), view.getMeasuredHeight());

        Bitmap bitmap = view.getDrawingCache();

        Drawable drawable = new BitmapDrawable(bitmap);
        return drawable;
    }

    // 初始个人中心的用户数据
    private void initUserData() {
        if (userData == null) return;
        if (!RxDataTool.isNullString(userData.phone)) {
            userPhone.setText(userData.phone);
        }
        if (!RxDataTool.isNullString(userData.name)) {
            userName.setText(userData.name);
        } else {
            userName.setText("未认证");
        }
        if (!RxDataTool.isNullString(userData.avatar)) {
            Glide.with(this)
                    .load(userData.avatar)
                    .apply(new RequestOptions().placeholder(R.mipmap.default_user_img).skipMemoryCache( true ))
                    .into(userImg);
        }
    }

    @OnClick({R.id.user_img, R.id.user_name, R.id.user_phone})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.user_img:
                IntentUtil.get().activity(mContext, PersonalActivity.class);
                break;
            case R.id.user_name:
            case R.id.user_phone:
                if (!RxDataTool.isNullString(userData.name)) {//没有认证跳转认证界面 否则跳转个人资料设置界面
                    IntentUtil.get().activity(mContext, PersonalActivity.class);
                } else {
                    IntentUtil.get().activity(mContext, IdentityActivity.class);

                }
                break;
        }
    }

}
