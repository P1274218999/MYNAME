package com.dhht.sld.main.findorder.view;

import android.view.View;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.dhht.sld.R;
import com.dhht.sld.base.BaseFragment;
import com.dhht.sld.base.http.retorfit.Converter;
import com.dhht.sld.base.http.retorfit.Result;
import com.dhht.sld.base.http.retorfit.Retorfit;
import com.dhht.sld.base.inject.ViewInject;
import com.dhht.sld.main.choose.pay.view.PayActivity;
import com.dhht.sld.main.findorder.bean.FindOrderBean;
import com.dhht.sld.main.findorderdetail.view.FindOrderDetailActivity;
import com.dhht.sld.main.home.event.MarkerEvent;
import com.dhht.sld.utlis.IntentUtil;
import com.tamsiree.rxkit.view.RxToast;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

import butterknife.BindView;

/**
 * 作者：pst
 * 邮箱：1274218999@qq.com
 * 创建时间:2020/8/27  9:38
 * 文件描述:我发布的订单
 */
@ViewInject(mainLayoutId = R.layout.fragment_find_order)
public class FindOrderFragment extends BaseFragment {
    @BindView(R.id.toolbar_title)
    public TextView title;
    BaseQuickAdapter<FindOrderBean, BaseViewHolder> mAdapter;
    private int page = 1;

    @Override
    public void afterBindView() {
        title.setText("我发布的订单");
        initAdapter();
        initData();
    }

    private void initData() {
        Retorfit.getService().getOrderList(page, 5).enqueue(new Converter<Result<List<FindOrderBean>>>() {
            @Override
            public void onSuccess(Result<List<FindOrderBean>> listResult) {
                if (page == 1) {
                    mAdapter.replaceData(listResult.data);
                } else {
                    mAdapter.addData(listResult.data);
                }
                mPullLayout.finishActionRun(mPullAction);
            }
        });
    }

    private void initAdapter() {
        mAdapter = new BaseQuickAdapter<FindOrderBean, BaseViewHolder>(R.layout.view_list_find_order) {
            @Override
            protected void convert(BaseViewHolder helper, FindOrderBean value) {
                helper.setText(R.id.article_name, value.article_name);
                helper.setText(R.id.order_time, value.add_time);
                helper.setText(R.id.order_start_address, value.start_address);
                helper.setText(R.id.order_end_address, value.end_address);
                TextView orderStatus = helper.getView(R.id.order_status);
                if (value.is_status == 1) {//订单未取消//
                    //状态 -1取消 0待付款 1待取货 2带送货 3完成
                    switch (value.status) {
                        case -1:
                            orderStatus.setText("已取消");
                            orderStatus.setTextColor(mContext.getResources().getColor(R.color.gray));
                            break;
                        case 0:
                            orderStatus.setText("待付款");
                            orderStatus.setTextColor(mContext.getResources().getColor(R.color.appTheme));
                            break;
                        case 1:
                            orderStatus.setText("待取货");
                            orderStatus.setTextColor(mContext.getResources().getColor(R.color.appTheme));
                            break;
                        case 2:
                            orderStatus.setText("待送货");
                            orderStatus.setTextColor(mContext.getResources().getColor(R.color.red));
                            break;
                        case 3:
                            orderStatus.setText("已完成");
                            orderStatus.setTextColor(mContext.getResources().getColor(R.color.green));
                            break;
                        default:
                            orderStatus.setText("其他");
                            orderStatus.setTextColor(mContext.getResources().getColor(R.color.gray));
                            break;
                    }
                } else {
                    orderStatus.setText("已取消");
                    orderStatus.setTextColor(mContext.getResources().getColor(R.color.gray));
                }

                helper.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (value.is_status!=1){
                            RxToast.normal("订单已取消");
                            return;
                        };//订单已取消
                        if (value.user_help_order_id >0 && value.status > 0){
                            EventBus.getDefault().postSticky(MarkerEvent.getInstance().setUser_help_order_id(value.user_help_order_id));
                            IntentUtil.get().activity(mActivity, FindOrderDetailActivity.class);
                        }else if (value.find_order_id>0&&value.status ==0){
                            startActivity(PayActivity.class)
                                    .withInt("status",value.status)
                                    .withInt("find_order_id", value.find_order_id)
                                    .go();
                        }
                    }


                });
            }
        };
        mRecyclerView.setAdapter(mAdapter);
    }
    @Override
    public void onRefreshData() {
        super.onRefreshData();
        page = 1;
        initData();
    }

    @Override
    public void onLoadMore() {
        super.onLoadMore();
        page++;
        initData();
    }
}
