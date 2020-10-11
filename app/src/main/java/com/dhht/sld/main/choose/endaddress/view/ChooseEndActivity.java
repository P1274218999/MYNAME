package com.dhht.sld.main.choose.endaddress.view;

import android.text.Html;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.FrameLayout;
import android.widget.TextView;

import androidx.core.widget.NestedScrollView;
import androidx.fragment.app.FragmentManager;

import com.dhht.sld.R;
import com.dhht.sld.base.BaseActivity;
import com.dhht.sld.base.BaseHttpResBean;
import com.dhht.sld.base.inject.BindEventBusInject;
import com.dhht.sld.base.inject.ViewInject;
import com.dhht.sld.main.address.model.AddressLocalData;
import com.dhht.sld.main.address.view.AddressActivity;
import com.dhht.sld.main.choose.article.view.ChooseArticleActivity;
import com.dhht.sld.main.choose.endaddress.bean.CheckingPriceBean;
import com.dhht.sld.main.choose.endaddress.contract.EndAddressHttpContract;
import com.dhht.sld.main.choose.endaddress.presenter.EndAddressPresenter;
import com.dhht.sld.main.findorder.view.FindOrderActivity;
import com.dhht.sld.utlis.IconFontView;
import com.dhht.sld.utlis.IntentUtil;
import com.tamsiree.rxkit.view.RxToast;


import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.text.DecimalFormat;

import butterknife.BindView;
import butterknife.OnClick;

@BindEventBusInject
@ViewInject(mainLayoutId = R.layout.activity_end_address)
public class ChooseEndActivity extends BaseActivity implements EndAddressHttpContract.Iview {

    @BindView(R.id.find_article_text)
    TextView findArticleText;
    @BindView(R.id.text_take_address)
    TextView textTakeAddress;
    @BindView(R.id.text_take_area)
    TextView textTakeArea;
    @BindView(R.id.text_take_phone)
    TextView textTakePhone;
    @BindView(R.id.text_take_name)
    TextView textTakeName;
    @BindView(R.id.bottom_sheet)
    NestedScrollView bottomSheet;
    @BindView(R.id.choose_end_address_fragment)
    FrameLayout chooseEndAddressFragment;
    @BindView(R.id.go_back)
    IconFontView goBack;
    @BindView(R.id.go_often_end)
    TextView goOftenEnd;
    @BindView(R.id.go_often_end_area)
    TextView goOftenEndArea;
    @BindView(R.id.text_take_name_end)
    TextView textTakeNameEnd;
    @BindView(R.id.text_take_phone_end)
    TextView textTakePhoneEnd;
    @BindView(R.id.end_address_price)
    TextView price;

    private FragmentManager fragmentManager;
    private int articleId;
    private int startId;
    private int endId;

    @Override
    public void afterBindView() {
        fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .add(R.id.choose_end_address_fragment, new ChooseEndMapFragment(), "ChooseEndMapFragment")
                .commit();
    }

    @Subscribe(sticky = true, threadMode = ThreadMode.MAIN)
    public void getParam(AddressLocalData data) {
        // 物品信息
        if (data.getArticleId() > 0) {
            articleId = data.getArticleId();
        }
        if (!TextUtils.isEmpty(data.getArticleName())) {
            findArticleText.setText(data.getArticleName());
            findArticleText.setTextColor(getResources().getColor(R.color.black));
        }
        // 起点信息
        if (data.getStartId() > 0) {
            startId = data.getStartId();
        }
        if (!TextUtils.isEmpty(data.getProvince())) {
            textTakeAddress.setText(data.getProvince());
        }
        if (!TextUtils.isEmpty(data.getArea())) {
            textTakeArea.setText(data.getArea());
        }
        if (!TextUtils.isEmpty(data.getUserName())) {
            textTakeName.setText(data.getUserName());
        }
        if (!TextUtils.isEmpty(data.getPhone())) {
            textTakePhone.setText(data.getPhone());
        }
        // 终点信息
        if (data.getEndId() > 0) {
            endId = data.getEndId();
        }
        if (!TextUtils.isEmpty(data.getEndProvince())) {
            goOftenEnd.setText(data.getEndProvince());
        }
        if (!TextUtils.isEmpty(data.getEndArea())) {
            goOftenEndArea.setText(data.getEndArea());
        }
        if (!TextUtils.isEmpty(data.getEndUserName())) {
            textTakeNameEnd.setText(data.getEndUserName());
        }
        if (!TextUtils.isEmpty(data.getEndPhone())) {
            textTakePhoneEnd.setText(data.getEndPhone());
        }

    }

    @Override
    protected void onResume() {
        super.onResume();
        doCheckPrice();
    }

    @OnClick({R.id.go_back, R.id.choose_article, R.id.go_often_start, R.id.often_click, R.id.create_order_click})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.go_back:
                finish();
                break;
            case R.id.choose_article:
                IntentUtil.get().activity(mActivity, ChooseArticleActivity.class);
                break;
            case R.id.go_often_start:
                EventBus.getDefault().postSticky(AddressLocalData.getInstance().setType(1));
                IntentUtil.get().activity(mActivity, AddressActivity.class);
                break;
            case R.id.often_click:
                EventBus.getDefault().postSticky(AddressLocalData.getInstance().setType(2));
                IntentUtil.get().activity(mActivity, AddressActivity.class);
                break;
            case R.id.create_order_click:
                doCreateOrder();
                break;
        }
    }

    private void doCreateOrder() {
        showLoading("发布中");
        EndAddressPresenter presenter = new EndAddressPresenter(this);
        presenter.doCreate(articleId, startId, endId);
    }

    private void doCheckPrice() {
        price.setText("");
        EndAddressPresenter presenter = new EndAddressPresenter(this);
        presenter.doCheck(articleId, startId, endId);
    }

    @Override
    public void onStart() {
        super.onStart();
        init();
    }

    private void init() {
        // 重置底部Fragment显示高度
        bottomSheet.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                int bottomHeadHeight = bottomSheet.getHeight();
                ChooseEndMapFragment chooseEndMapFragment = (ChooseEndMapFragment) getSupportFragmentManager().findFragmentByTag("ChooseEndMapFragment");
                chooseEndMapFragment.setMapCenter(bottomHeadHeight);
                bottomSheet.getViewTreeObserver().removeOnGlobalLayoutListener(this);
            }
        });
    }

    @Override
    public void resCreate(BaseHttpResBean res) {
        hideTipDialog();
        if (res.code > 0) {
            IntentUtil.get().activityKill(mActivity, FindOrderActivity.class);
        } else {
            RxToast.error(res.msg);
        }
    }

    @Override
    public void resCheck(CheckingPriceBean res) {
        if (res.code > 0) {
            DecimalFormat format = new DecimalFormat(".00");
            String html = "<font>价格总计:</font><font color='#FF9D1F'>" + format.format(res.data.price) + "</font><font>元</font>";
            price.setText(Html.fromHtml(html));
        } else {
            price.setText("");
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        AddressLocalData.getInstance().clear();
    }
}
