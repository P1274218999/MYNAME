package com.dhht.sld.main.home.view;

import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.fragment.app.FragmentManager;

import com.dhht.sld.MainActivity;
import com.dhht.sld.R;
import com.dhht.sld.base.BaseFragment;
import com.dhht.sld.base.inject.ViewInject;
import com.dhht.sld.main.home.bean.CurrentOrderBean;
import com.dhht.sld.main.home.contract.MainBottomContract;
import com.dhht.sld.main.home.event.MarkerEvent;
import com.dhht.sld.main.home.presenter.MainBottomPresenter;
import com.dhht.sld.main.receiving.view.ReceivingOrderActivity;
import com.dhht.sld.utlis.FragmentUtil;
import com.dhht.sld.utlis.IntentUtil;

import org.greenrobot.eventbus.EventBus;

import butterknife.BindView;
import butterknife.OnClick;

@ViewInject(mainLayoutId = R.layout.fragment_main)
public class MainBottomFragment extends BaseFragment implements MainBottomContract.IView {

    @BindView(R.id.main_order_tip_title)
    TextView mainOrderTipTitle;
    @BindView(R.id.main_order_tip_box)
    LinearLayout mainOrderTipBox;

    private FragmentManager fragmentManager;
    private int type = 0;
    private int beyondHeight = 50; // 定义一个统一超出的高度
    private MainMapFragment mainMapFragment;
    private HelpPeopleBottomFragment helpPeopleBottomFragment = new HelpPeopleBottomFragment();
    private FindPeopleBottomFragment findPeopleBottomFragment = new FindPeopleBottomFragment();
    private MainBottomPresenter presenter = new MainBottomPresenter(this);
    private int find_order_id;
    private int mainOrderTipHeight = 140;

    @BindView(R.id.main_bottom_head)
    FrameLayout mainBottomHead;

    @Override
    public void onStart() {
        super.onStart();
        presenter.getOrder();
    }

    @Override
    public void afterBindView() {
        fragmentManager = getChildFragmentManager();
        mainMapFragment = (MainMapFragment) getActivity().getSupportFragmentManager().findFragmentByTag("MainMapFragment");
        initChangeFragment(type);
    }

    /**
     * 切换底部Fragment
     *
     * @param type
     */
    public void initChangeFragment(int type) {
        if (type == 1) {
            FragmentUtil.replace(fragmentManager, R.id.main_bottom_head, helpPeopleBottomFragment, "HelpPeopleBottomFragment");
        } else {
            FragmentUtil.replace(fragmentManager, R.id.main_bottom_head, findPeopleBottomFragment, "FindPeopleBottomFragment");
        }
        setMainBottomHeight();
    }

    private void setChangeFragment(int height) {
        height = height + beyondHeight;

        MainMapFragment mainMapFragment = (MainMapFragment) getActivity().getSupportFragmentManager().findFragmentByTag("MainMapFragment");
        mainMapFragment.setLocationImgClickPosition(height);//设置点击定位图标的位置
        mainMapFragment.setMapCenter(height);//设置地图的中心点

        MainActivity mainActivity = (MainActivity) mContext;
        mainActivity.setBottomSheetHeight(height);//设置底部滑动的默认显示高度
    }

    // 重置底部Fragment显示高度
    public void setMainBottomHeight() {
        mainBottomHead.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                int bottomHeight = mainBottomHead.getHeight();
                if (find_order_id > 0){
                    setChangeFragment(bottomHeight+mainOrderTipHeight);
                }else{
                    setChangeFragment(bottomHeight);
                }
                mainBottomHead.getViewTreeObserver().removeOnGlobalLayoutListener(this);
            }
        });
    }

    // 重置底部Fragment显示高度 指定预留高度
    public void setMainBottomHeight(int moreHeight) {
        mainBottomHead.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                int bottomHeight = mainBottomHead.getHeight();
                setChangeFragment(bottomHeight+moreHeight);
                mainBottomHead.getViewTreeObserver().removeOnGlobalLayoutListener(this);
            }
        });
    }

    @Override
    public void resOrder(CurrentOrderBean res) {
        if (res.data == null){
            find_order_id = 0;
            mainOrderTipBox.setVisibility(View.GONE);
        }else {
            find_order_id = res.data.find_order_id;
            setOrderTip(res.data.status);
        }
    }

    private void setOrderTip(int status) {
        mainOrderTipBox.setVisibility(View.VISIBLE);
        if (status == 1){
            mainOrderTipTitle.setText("你有订单需要取货");
        }else if (status == 2){
            mainOrderTipTitle.setText("你有订单需要送货");
        }
        setMainBottomHeight(mainOrderTipHeight);
    }

    @OnClick(R.id.main_order_tip_look)
    public void onClick() {
        EventBus.getDefault().postSticky(MarkerEvent.getInstance().setFind_order_id(find_order_id));
        IntentUtil.get().activity(mContext, ReceivingOrderActivity.class);
    }
}
