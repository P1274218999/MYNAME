package com.dhht.sld.main.notification.view;

import android.os.Bundle;
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
import com.dhht.sld.main.notification.bean.MessageBean;
import com.tamsiree.rxkit.RxTimeTool;
import com.tamsiree.rxkit.RxTool;

import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import butterknife.BindView;
import retrofit2.Response;

/**
 * 作者：pst
 * 邮箱：1274218999@qq.com
 * 创建时间：2020/7/21  14:53
 * 文件描述：消息通知
 * */
@ViewInject(mainLayoutId = R.layout.fragment_notification)
public class NotificationFragment extends BaseFragment {
    private BaseQuickAdapter<String, BaseViewHolder> quickAdapter;
    private int page=1;
    @BindView(R.id.toolbar_title)
    TextView commonTitle;
    @Override
    public void afterBindView() {
        commonTitle.setText("消息通知");
        initAdapter();
        mRecyclerView.setAdapter(quickAdapter);
    }

    private void initAdapter() {
        quickAdapter=new BaseQuickAdapter<String, BaseViewHolder>(R.layout.view_list_nootification) {
            @Override
            protected void convert(BaseViewHolder helper, String item) {
                if (item.equals("系统消息")){
                    helper.setBackgroundRes(R.id.notification_list_icon,R.mipmap.ic_notification_system_bg);
                    helper.setText(R.id.notification_list_icon,R.string.icon_system_notification);
                    helper.setText(R.id.notification_list_type,"系统消息");
                    Retorfit.getService().getMessageList(1, 10, 1).enqueue(new Converter<Result<List<MessageBean>>>() {
                        @Override
                        public void onSuccess(Result<List<MessageBean>> listResult) {
                            if (listResult.data.size()>0){
                                helper.setText(R.id.notification_list_content, listResult.data.get(0).content);
                                String timeString = RxTimeTool.milliseconds2String(listResult.data.get(0).time*1000,new SimpleDateFormat("yyyy/MM/dd HH:mm"));
                                helper.setText(R.id.notification_list_time, timeString);
                            }
                            Boolean isRead=false;
                            for (MessageBean datum : listResult.data) {
                                    if (datum.status==2)isRead=true;
                            }
                            if (isRead){
                                helper.getView(R.id.notification_list_read).setVisibility(View.VISIBLE);
                            }
                        }
                    });

                }else if (item.equals("活动消息")){
                    helper.setBackgroundRes(R.id.notification_list_icon,R.mipmap.ic_notification_activity_bg);
                    helper.setText(R.id.notification_list_icon,R.string.icon_activity_notification);
                    helper.setText(R.id.notification_list_type,"活动消息");
                }else {
                    helper.setBackgroundRes(R.id.notification_list_icon,R.mipmap.ic_notification_system_bg);
                    helper.setText(R.id.notification_list_icon,R.string.icon_kefu_notification);
                    helper.setText(R.id.notification_list_type,"客服消息");
                }
                helper.getView(R.id.notification_list_item).setOnClickListener(v->{
                    Bundle mBundle = new Bundle();
                    NotificationInfoFragment fragment = new NotificationInfoFragment();
                    if(item.equals("活动消息")){
                        mBundle.putInt("type", 2);
                    }else if (item.equals("系统消息")){
                        mBundle.putInt("type", 1);
                    }else {
                        return;
                    }
                    fragment.setArguments(mBundle);
                    addFragment(fragment, R.id.notification_fragment_layout);
                });

            }
        };

        View loadMoreView = getLayoutInflater().inflate( R.layout.view_adapter_load_more, null, false );
        TextView footText = loadMoreView.findViewById(R.id.text);
        footText.setText("~~我也是有底线的哦");
        quickAdapter.addFooterView( loadMoreView ); //添加页脚视图
    }

    @Override
    public void onResume() {
        super.onResume();
        List<String> list=new ArrayList<>();
        list.add("系统消息");
        list.add("活动消息");
        list.add("客服消息");
        quickAdapter.replaceData(list);
    }

    @Override
    public void onRefreshData() {
        super.onRefreshData();
        List<String> list=new ArrayList<>();
        list.add("系统消息");
        list.add("活动消息");
        list.add("客服消息");
        quickAdapter.replaceData(list);
        page=1;
        mPullLayout.finishActionRun(mPullAction);
    }
    @Override
    public void onLoadMore() {
        super.onLoadMore();
        List<String> list=new ArrayList<>();
        list.add("系统消息");
        list.add("活动消息");
        list.add("客服消息");
        quickAdapter.replaceData(list);
        page++;
        mPullLayout.finishActionRun(mPullAction);
    }
}
