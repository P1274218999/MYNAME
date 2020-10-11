package com.dhht.sld.main.helporder.view;

import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.dhht.sld.R;
import com.dhht.sld.base.BaseActivity;
import com.dhht.sld.base.inject.ViewInject;
import com.dhht.sld.main.findorderdetail.view.FindOrderDetailActivity;
import com.dhht.sld.main.helporder.adapter.HelpOrderAdapter;
import com.dhht.sld.main.helporder.bean.HelpOrderBean;
import com.dhht.sld.main.helporder.contract.HelpOrderContract;
import com.dhht.sld.main.helporder.presenter.HelpOrderPresenter;
import com.dhht.sld.main.home.event.MarkerEvent;
import com.dhht.sld.main.receiving.view.ReceivingOrderActivity;
import com.dhht.sld.utlis.IntentUtil;

import org.greenrobot.eventbus.EventBus;

import butterknife.BindView;
import butterknife.OnClick;

@ViewInject(mainLayoutId = R.layout.activity_help_order)
public class HelpOrderActivity extends BaseActivity implements HelpOrderContract.Iview {

    @BindView(R.id.find_order_list)
    RecyclerView findOrderList;
    private HelpOrderAdapter findOrderAdapter;
    @BindView(R.id.toolbar_title)
    public TextView title;
    @Override
    public void afterBindView() {
        title.setText("我帮带的订单");
        initRecyclerView();
        getList();
    }

    private void getList() {
        HelpOrderPresenter presenter = new HelpOrderPresenter(this);
        presenter.getList(1);
    }

    private void initRecyclerView() {
        findOrderAdapter = new HelpOrderAdapter();
        // 绑定点击item事件
        findOrderAdapter.setOnItemClickListener(new HelpOrderAdapter.OnItemClickListener() {
            @Override
            public void onClick(int find_order_id,int status) {
                switch (status)
                {
                    case 0:
                        showTip("订单已取消");
                        break;
                    case 1:
                    case 2:
                        EventBus.getDefault().postSticky(MarkerEvent.getInstance().setFind_order_id(find_order_id));
                        IntentUtil.get().activity(mActivity, ReceivingOrderActivity.class);
                        break;
                    case 3:
                        showTip("订单已完成");
                        break;
                }
            }
        });
        LinearLayoutManager manager = new LinearLayoutManager(mActivity);
        findOrderList.setLayoutManager(manager);
        findOrderList.setAdapter(findOrderAdapter);
    }

    @Override
    public void resList(HelpOrderBean res) {
        if (res.data.size() > 0) {
            findOrderAdapter.setData(res.data);
            findOrderAdapter.notifyDataSetChanged();
        }
    }

    @OnClick(R.id.toolbar_back)
    public void onClick() {
        finish();
    }
}
