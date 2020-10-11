package com.dhht.sld.main.wallet.view;

import android.graphics.Color;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.dhht.sld.R;
import com.dhht.sld.base.BaseFragment;
import com.dhht.sld.base.inject.ViewInject;
import com.dhht.sld.main.wallet.bean.WalletDetailBean;
import com.dhht.sld.main.wallet.contract.WalletDetailHttpContract;
import com.dhht.sld.main.wallet.presenter.WalletDetailPresenter;
/**
 * 作者：pst
 * 邮箱：1274218999@qq.com
 * 创建时间：2020/7/17  14:45
 * 文件描述：交易明细列表
 */
@ViewInject(mainLayoutId = R.layout.fragment_wallet_details_list)
public class WalletDetailsListFragment extends BaseFragment implements WalletDetailHttpContract.Iview {
    private  BaseQuickAdapter<WalletDetailBean.Info, BaseViewHolder> quickAdapter;
    private WalletDetailPresenter walletDetailPresenter;

    private int type;
    private int page=1;
    private int total=-1;
    public  WalletDetailsListFragment(int type){
        this.type=type;
    }
    @Override
    public void afterBindView() {
        walletDetailPresenter=new WalletDetailPresenter(this);
        initAdapter();
        LinearLayoutManager manager = new LinearLayoutManager(mContext);
        mRecyclerView.setLayoutManager(manager);
        mRecyclerView.setAdapter(quickAdapter);
        walletDetailPresenter.getList(page,type);
    }


    /**
     * 上拉刷新
     */
    @Override
    public void onRefreshData() {
        super.onRefreshData();
        quickAdapter.removeAllFooterView();
        quickAdapter.notifyDataSetChanged();
        page=1;
        walletDetailPresenter.getList(page,type);
    }

    @Override
    public void onLoadMore() {
        super.onLoadMore();
        page++;
        walletDetailPresenter.getList(page,type);
    }

    @Override
    public void resList(WalletDetailBean res) {
        total=res.data.total;
       if(page==1){
           quickAdapter.replaceData(res.data.list);
       }else {
           quickAdapter.addData(res.data.list);
       }
       if (mPullAction!=null){
           mPullLayout.finishActionRun(mPullAction);
       }
        if (res.data.total <= quickAdapter.getItemCount() || res.data.list.size() == 0) {
            if (quickAdapter.getFooterViewsCount() == 0) {
                View loadMoreView = getLayoutInflater().inflate( R.layout.view_adapter_load_more, null, false );
                quickAdapter.addFooterView( loadMoreView ); //添加页脚视图
            }
        }
    }

    private void initAdapter(){
        quickAdapter=new BaseQuickAdapter<WalletDetailBean.Info, BaseViewHolder>(R.layout.view_list_wallet_detail) {
            @Override
            protected void convert(BaseViewHolder helper, WalletDetailBean.Info item) {
                TextView moneyText1 = helper.getView(R.id.wallet_list_detail_money1);
                TextView moneyText2 = helper.getView(R.id.wallet_list_detail_money2);
                String[] money=new String[2];
                if (item.money!=null&&!TextUtils.isEmpty(item.money)){
                    money = item.money.split("\\.");
                }else {
                    money[0]="00";
                    money[1]="00";
                }
                if(item.type==1){
                    moneyText1.setTextColor(Color.parseColor("#FF9D1F"));
                    moneyText2.setTextColor(Color.parseColor("#FF9D1F"));
                    moneyText1.setText("+"+money[0]);
                    moneyText2.setText("."+money[1]);
                }else {
                    moneyText1.setTextColor(Color.parseColor("#333333"));
                    moneyText2.setTextColor(Color.parseColor("#333333"));
                    moneyText1.setText("-"+money[0]);
                    moneyText2.setText("."+money[1]);
                }
                helper.setText(R.id.wallet_list_detail_type,item.comment);
                helper.setText(R.id.wallet_list_detail_date,item.time);
                }
        };
    }


    @Override
    public void onStop() {
        super.onStop();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }
}
