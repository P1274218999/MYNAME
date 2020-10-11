package com.dhht.sld.main.notification.view;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.dhht.sld.R;
import com.dhht.sld.base.BaseFragment;
import com.dhht.sld.base.http.retorfit.Converter;
import com.dhht.sld.base.http.retorfit.Result;
import com.dhht.sld.base.http.retorfit.Retorfit;
import com.dhht.sld.base.inject.AutoArg;
import com.dhht.sld.base.inject.ViewInject;
import com.dhht.sld.main.notification.bean.MessageBean;
import com.dhht.sld.main.wallet.view.WalletFragment;
import com.dhht.sld.main.webview.view.QDWebExplorerFragment;
import com.qmuiteam.qmui.widget.QMUIRadiusImageView;
import com.tamsiree.rxkit.RxTimeTool;

import java.text.SimpleDateFormat;
import java.util.List;

import butterknife.BindView;

/**
 * 作者：pst
 * 邮箱：1274218999@qq.com
 * 创建时间:2020/8/18  15:26
 * 文件描述:消息通知二级界面
 */
@ViewInject(mainLayoutId = R.layout.fragment_notification_info)
public class NotificationInfoFragment extends BaseFragment {
    @AutoArg
    private Integer type = -1;//消息类型
    @BindView(R.id.toolbar_title)
    TextView commonTitle;
    private int page = 1;
    private BaseQuickAdapter<MessageBean, BaseViewHolder> mAdapter;

    @Override
    public void afterBindView() {
        commonTitle.setText("消息通知");
        initAdapter();
        getData();
    }

    private void initAdapter() {
        if (type == 2) {
            mAdapter = new BaseQuickAdapter<MessageBean, BaseViewHolder>(R.layout.view_list_nootification_info) {
                @Override
                protected void convert(BaseViewHolder helper, MessageBean item) {
                    ImageView view = helper.getView(R.id.notification_list_image);
                    view.setVisibility(View.VISIBLE);
                    Glide.with(mActivity)
                            .load("data:image/jpeg;base64,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")
                            .apply(new RequestOptions().placeholder(R.mipmap.default_user_img).skipMemoryCache(true))
                            .into(view);
                }
            };

        } else if (type == 1) {//系统消息

            mAdapter = new BaseQuickAdapter<MessageBean, BaseViewHolder>(R.layout.view_list_nootification_system) {
                @Override
                protected void convert(BaseViewHolder helper, MessageBean item) {
                    String timeString = RxTimeTool.milliseconds2String(item.time * 1000, new SimpleDateFormat("yyyy/MM/dd HH:mm"));
                    helper.setText(R.id.notification_list_time, timeString);
                    if (!TextUtils.isEmpty(item.img)) {
                        QMUIRadiusImageView view = helper.getView(R.id.notification_list_image);
                        view.setVisibility(View.VISIBLE);
                        Glide.with(mActivity)
                                .load(item.img)
                                .apply(new RequestOptions().placeholder(R.mipmap.default_user_img).skipMemoryCache(true))
                                .into(view);
                    } else {
                        helper.getView(R.id.notification_list_image).setVisibility(View.GONE);
                    }
                    helper.setText(R.id.notification_list_title, item.title);
                    helper.setText(R.id.notification_list_content, item.content);
                    if (item.status == 2) {
                        helper.getView(R.id.notification_list_read).setVisibility(View.VISIBLE);
                    } else {
                        helper.getView(R.id.notification_list_read).setVisibility(View.GONE);
                    }
                    helper.getView(R.id.notification_list_cardView).setOnClickListener(v -> {
                        if (item.status == 2) {
                            Retorfit.getService().updateStatus(item.id).enqueue(new Converter<Result<Integer>>() {
                                @Override
                                public void onSuccess(Result<Integer> result) {
                                    if (result.data > 0) {
                                        helper.getView(R.id.notification_list_read).setVisibility(View.GONE);
                                    }
                                }
                            });
                        }
                        if (!TextUtils.isEmpty(item.url)) {
                            QDWebExplorerFragment qdWebExplorerFragment = new QDWebExplorerFragment();
                            Bundle mBundle=new Bundle();
                            mBundle.putString("mUrl", item.url);
                            mBundle.putString("mTitle", item.title);
                            qdWebExplorerFragment.setArguments(mBundle);
                            addFragment(qdWebExplorerFragment, R.id.notification_fragment_layout);
                        }

                    });
                }
            };

        }
        mRecyclerView.setAdapter(mAdapter);
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onLoadMore() {
        super.onLoadMore();
        page++;
        getData();
    }

    @Override
    public void onRefreshData() {
        super.onRefreshData();
        page = 1;
        getData();
    }

    private void getData() {
        Retorfit.getService().getMessageList(page, 10, type).enqueue(new Converter<Result<List<MessageBean>>>() {
            @Override
            public void onSuccess(Result<List<MessageBean>> listResult) {
                if (listResult.data == null) return;
                if (page == 1) {
                    mAdapter.replaceData(listResult.data);
                } else {
                    mAdapter.addData(listResult.data);
                }
                mPullLayout.finishActionRun(mPullAction);
            }
        });
    }
}
