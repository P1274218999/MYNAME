package com.dhht.sld.main.wallet.view;

import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.dhht.sld.R;
import com.dhht.sld.base.BaseFragment;
import com.dhht.sld.base.inject.ViewInject;
import com.qmuiteam.qmui.skin.QMUISkinManager;
import com.qmuiteam.qmui.util.QMUIDisplayHelper;
import com.qmuiteam.qmui.widget.popup.QMUIPopup;
import com.qmuiteam.qmui.widget.popup.QMUIPopups;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 作者：pst
 * 邮箱：1274218999@qq.com
 * 创建时间：2020/7/18  14:18
 * 文件描述：银行卡添加
 */


@ViewInject(mainLayoutId = R.layout.fragment_wallet_bankcard_add)
public class WalleBankCardAddFragment extends BaseFragment {
    @BindView(R.id.toolbar_title)
    TextView commonTitle;
    @BindView(R.id.wallet_list_bankcard_cardholder)
    EditText cardholder;
    @BindView(R.id.wallet_list_bankcard_cardnumber)
    EditText cardNumber;
    @BindView(R.id.wallet_list_bankcard_cardtype)
    TextView bandCardType;
    private QMUIPopup mNormalPopup;
    private BaseQuickAdapter<String, BaseViewHolder> quickAdapter;

    @Override
    public void afterBindView() {
        commonTitle.setText("银行卡管理");
    }

    @OnClick({R.id.toolbar_back, R.id.wallet_list_bankcard_cardtype})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.toolbar_back:
                back();
                break;
            case R.id.wallet_list_bankcard_cardtype:
                showPop(view);
                break;

        }
    }

    private void showPop(View v) {
        String[] listItems = new String[]{
                "农业银行",
                "建设银行",
                "工商银行",
        };
        List<String> data = new ArrayList<>();

        Collections.addAll(data, listItems);

        ArrayAdapter adapter = new ArrayAdapter<>(getContext(), R.layout.simple_list_item, data);
        AdapterView.OnItemClickListener onItemClickListener = new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                bandCardType.setText(listItems[i] + "");
                if (mNormalPopup != null) {
                    mNormalPopup.dismiss();
                }
            }
        };
        mNormalPopup = QMUIPopups.listPopup(getContext(),
                QMUIDisplayHelper.dp2px(getContext(), 250),
                QMUIDisplayHelper.dp2px(getContext(), 300),
                adapter,
                onItemClickListener)
                .animStyle(QMUIPopup.ANIM_GROW_FROM_CENTER)
                .preferredDirection(QMUIPopup.DIRECTION_TOP)
                .shadow(true)
                .offsetYIfTop(QMUIDisplayHelper.dp2px(getContext(), 5))
                .skinManager(QMUISkinManager.defaultInstance(getContext()))
                .onDismiss(new PopupWindow.OnDismissListener() {
                    @Override
                    public void onDismiss() {
                    }
                })
                .show(v);
    }

    /**
     * 提交
     */
    private void commit() {

    }
}
