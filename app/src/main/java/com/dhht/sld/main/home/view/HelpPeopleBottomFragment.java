package com.dhht.sld.main.home.view;

import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.dhht.sld.MainActivity;
import com.dhht.sld.R;
import com.dhht.sld.base.BaseFragment;
import com.dhht.sld.base.BaseHttpResBean;
import com.dhht.sld.base.inject.ViewInject;
import com.dhht.sld.main.home.adapter.HelpClickMarkerAdapter;
import com.dhht.sld.main.home.contract.HelpPeopleBottomContract;
import com.dhht.sld.main.home.event.MarkerEvent;
import com.dhht.sld.main.home.model.HelpRoutResModel;
import com.dhht.sld.main.home.presenter.HelpPeopleBottomPresenter;
import com.dhht.sld.main.identity.view.IdentityActivity;
import com.dhht.sld.main.receiving.view.ReceivingOrderActivity;
import com.dhht.sld.utlis.IntentUtil;
import com.dhht.sld.utlis.ShowDialog;
import com.dhht.sld.utlis.UserSPUtli;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

@ViewInject(mainLayoutId = R.layout.fragment_main_bangrd)
public class HelpPeopleBottomFragment extends BaseFragment implements HelpPeopleBottomContract.IView {
    public String[] title = {"骑行", "步行", "公交", "驾车"};

    private HelpClickMarkerAdapter clickMarkerAdapter;
    private int id = 0;
    private HelpPeopleBottomPresenter presenter;

    @BindView(R.id.bottom_help_address)
    TextView bottomHelpAddress;
    @BindView(R.id.bottom_help_article)
    TextView bottomHelpArticle;
    @BindView(R.id.bottom_help_end_address)
    TextView bottomHelpEndAddress;
    //    @BindView(R.id.help_route_list)
//    RecyclerView helpRouteList;
    @BindView(R.id.help_click_top)
    LinearLayout helpClickTop;
    @BindView(R.id.help_route_bottom)
    LinearLayout helpRouteBottom;
//    @BindView(R.id.help_people_tab_layout) TabLayout tabLayout;


    @Override
    public void onStart() {
        super.onStart();
        startInit();
    }

    @Override
    public void afterBindView() {
        presenter = new HelpPeopleBottomPresenter(this);
        initView();
    }

    private void startInit() {
        hideRouteFragment();
    }

    private void initLayTab() {
        for (String name : title) {
//            tabLayout.addTab(tabLayout.newTab().setText(name));
        }
    }

    private void initView() {
        clickMarkerAdapter = new HelpClickMarkerAdapter();
        // 绑定点击item事件
        clickMarkerAdapter.setOnItemClickListener(new HelpClickMarkerAdapter.OnItemClickListener() {
            @Override
            public void onClick(String province, String area, double latitude, double longitude) {


            }
        });
        /*
        LinearLayoutManager manager = new LinearLayoutManager(mActivity);
        helpRouteList.setLayoutManager(manager);
        helpRouteList.setAdapter(clickMarkerAdapter);
        */
    }

    // 设置取送的地址显示
    public void updateAddress(String address, String article_name, String endAddress, int id) {
        this.id = id;
        bottomHelpAddress.setText(address);
        bottomHelpEndAddress.setText(endAddress);
        bottomHelpArticle.setText(article_name);
    }

    public void updateAdapterData(List<HelpRoutResModel> data) {
        clickMarkerAdapter.setData(data);
        clickMarkerAdapter.notifyDataSetChanged();
    }

    //
    public void hideRouteFragment() {
        helpRouteBottom.setVisibility(View.GONE);
        helpClickTop.setVisibility(View.VISIBLE);
    }

    public void showRouteFragment() {
        helpRouteBottom.setVisibility(View.VISIBLE);
        helpClickTop.setVisibility(View.GONE);
    }

    @Override
    public void resReceiving(BaseHttpResBean res) {
        if (res.code > 0) {
            EventBus.getDefault().postSticky(MarkerEvent.getInstance().setFind_order_id(id));
            IntentUtil.get().activity(mContext, ReceivingOrderActivity.class);
        } else {
            ((MainActivity) mContext).showTip(res.msg);
        }
    }

    @OnClick({R.id.help_cance, R.id.help_click_jiedan})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.help_cance:
                MainMapFragment mapFragment = (MainMapFragment) getActivity().getSupportFragmentManager().findFragmentByTag("MainMapFragment");
                mapFragment.markerBack();

                break;
            case R.id.help_click_jiedan:
                // 1、先判断是否实名认证
                if (UserSPUtli.getInstance().isAuthentication()) {
                    new ShowDialog().show(mContext, "确定要接吗？", new ShowDialog.OnBottomClickListener() {
                        @Override
                        public void positive() {

                            presenter.doReceiving(id);
                        }

                        @Override
                        public void negative() {

                        }
                    });
                } else {
                    new ShowDialog().show(mContext, "你还没有进行实名认证，请先认证！", new ShowDialog.OnBottomClickListener() {
                        @Override
                        public void positive() {
                            IntentUtil.get().activity(mContext, IdentityActivity.class);
                        }

                        @Override
                        public void negative() {

                        }
                    });
                }
                break;
        }
    }
}
