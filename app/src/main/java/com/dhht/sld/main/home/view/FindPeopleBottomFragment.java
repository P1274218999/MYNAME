package com.dhht.sld.main.home.view;

import android.text.TextUtils;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.dhht.sld.R;
import com.dhht.sld.base.BaseFragment;
import com.dhht.sld.base.inject.BindEventBusInject;
import com.dhht.sld.base.inject.ViewInject;
import com.dhht.sld.main.address.model.AddressLocalData;
import com.dhht.sld.main.address.view.AddressActivity;
import com.dhht.sld.main.choose.article.view.ChooseArticleActivity;
import com.dhht.sld.utlis.IntentUtil;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.BindView;
import butterknife.OnClick;


@BindEventBusInject
@ViewInject(mainLayoutId = R.layout.fragment_main_zhaord)
public class FindPeopleBottomFragment extends BaseFragment {

    @BindView(R.id.text_take_address)
    TextView textTakeAddress;
    @BindView(R.id.text_take_area)
    TextView textTakeArea;
    @BindView(R.id.find_article_text)
    TextView findArticleText;

    @Override
    public void afterBindView() {

    }

    @Subscribe(sticky = true,threadMode = ThreadMode.MAIN)
    public void getParam(AddressLocalData data)
    {
        if (!TextUtils.isEmpty(data.getArticleName()))
        {
            findArticleText.setText(data.getArticleName());
            findArticleText.setTextColor(getResources().getColor(R.color.black));
        }
    }

    /**
     * LinearLayout的点击事件
     *
     * @param layout
     */
    @OnClick({R.id.go_often_start, R.id.go_often_end})
    public void go_often_click(LinearLayout layout) {
        switch (layout.getId()) {
            case R.id.go_often_start:
                EventBus.getDefault().postSticky(AddressLocalData.getInstance().setType(1));
                break;
            case R.id.go_often_end:
                EventBus.getDefault().postSticky(AddressLocalData.getInstance().setType(2));
                break;
            default:
                break;
        }
        IntentUtil.get().activity(mContext, AddressActivity.class);
    }

    /*
    @OnClick({R.id.often_start, R.id.often_end})
    public void often_click(TextView textView) {
        switch (textView.getId()) {
            case R.id.often_start:

                break;
            case R.id.often_end:

                break;
            default:
                break;
        }
    }
    */

    @OnClick(R.id.choose_article)
    public void onClick() {
        IntentUtil.get().activity(mContext, ChooseArticleActivity.class);
    }

    public void setQuHuoText(String msg) {
        textTakeAddress.setText(msg);
    }
}
