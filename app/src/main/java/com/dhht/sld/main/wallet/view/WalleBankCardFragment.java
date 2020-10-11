package com.dhht.sld.main.wallet.view;

import android.view.View;
import android.widget.TextView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.dhht.sld.R;
import com.dhht.sld.base.BaseFragment;
import com.dhht.sld.base.inject.ViewInject;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 作者：pst
 * 邮箱：1274218999@qq.com
 * 创建时间：2020/7/18  14:18
 * 文件描述：银行卡管理
 */


@ViewInject(mainLayoutId = R.layout.fragment_wallet_bankcard)
public class WalleBankCardFragment extends BaseFragment {
    @BindView(R.id.toolbar_title)
    TextView commonTitle;

    @BindView(R.id.recycler)
    RecyclerView recycler;
    private WalleBankCardAddFragment walleBankCardAdd=new WalleBankCardAddFragment();
    private BaseQuickAdapter<String, BaseViewHolder> quickAdapter;

    @Override
    public void afterBindView() {
        commonTitle.setText("银行卡管理");
        initAdapter();
        LinearLayoutManager manager = new LinearLayoutManager(mContext);
        recycler.setLayoutManager(manager);
        recycler.setAdapter(quickAdapter);
    }

    private void initAdapter() {
        quickAdapter = new BaseQuickAdapter<String, BaseViewHolder>(R.layout.view_list_wallet_bankcard) {
            @Override
            protected void convert(BaseViewHolder helper, String item) {
                if (item.equals("中国农业银行储蓄卡")) {
                    helper.setText(R.id.wallet_list_bankcard_typename,"中国农业银行储蓄卡");
                    helper.setBackgroundRes(R.id.wallet_list_bankcard_bgcolor, R.drawable.wallet_bankcard_green);
                    helper.setImageResource(R.id.wallet_list_bankcard_icon, R.mipmap.ic_wallet_bankcard_nongye);
                } else if (item.equals("中国建设银行储蓄卡")) {
                    helper.setText(R.id.wallet_list_bankcard_typename,"中国建设银行储蓄卡");
                    helper.setBackgroundRes(R.id.wallet_list_bankcard_bgcolor, R.drawable.wallet_bankcard_blue);
                    helper.setImageResource(R.id.wallet_list_bankcard_icon, R.mipmap.ic_wallet_bankcard_jianshe);
                } else if (item.equals("中国工商银行储蓄卡")) {
                    helper.setText(R.id.wallet_list_bankcard_typename,"中国工商银行储蓄卡");
                    helper.setBackgroundRes(R.id.wallet_list_bankcard_bgcolor, R.drawable.wallet_bankcard_red);
                    helper.setImageResource(R.id.wallet_list_bankcard_icon, R.mipmap.ic_wallet_bankcard_gongshang);
                }
            }
        };
        View foodAdd = getLayoutInflater().inflate(R.layout.view_list_wallet_bankcard_foot_add, null, false);
        foodAdd.findViewById(R.id.wallet_list_bankcard_add_layot).setOnClickListener(view -> {
            addCard();
        });
        quickAdapter.setFooterView(foodAdd);

        List<String> stringList = new ArrayList<String>();
        stringList.add("中国农业银行储蓄卡");
        stringList.add("中国建设银行储蓄卡");
        stringList.add("中国工商银行储蓄卡");
        quickAdapter.replaceData(stringList);
    }

    /**
     * 添加银行卡
     **/
    private void addCard() {
        addFragment(walleBankCardAdd, R.id.wallet_activtiy_fragment);
    }

    @Override
    public void onLoadMore() {
        super.onLoadMore();
        mPullLayout.finishActionRun(mPullAction);
    }
    @Override
    public void onRefreshData() {
        super.onRefreshData();
        mPullLayout.finishActionRun(mPullAction);
    }
    @OnClick({R.id.toolbar_back})
    public void onClick() {
      back();
    }

}
