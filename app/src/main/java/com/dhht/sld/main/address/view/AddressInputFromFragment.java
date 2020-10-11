package com.dhht.sld.main.address.view;

import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.dhht.sld.R;
import com.dhht.sld.base.BaseFragment;
import com.dhht.sld.base.inject.BindEventBusInject;
import com.dhht.sld.base.inject.ViewInject;
import com.dhht.sld.main.address.bean.AddressResBean;
import com.dhht.sld.main.address.contract.IDoSubmitContract;
import com.dhht.sld.main.address.model.AddressLocalData;
import com.dhht.sld.main.address.presenter.DoSubmitPresenter;
import com.dhht.sld.main.choose.endaddress.view.ChooseEndActivity;
import com.dhht.sld.main.login.view.design.RongDivisionEditText;
import com.dhht.sld.utlis.IconFontView;
import com.dhht.sld.utlis.IntentUtil;
import com.qmuiteam.qmui.widget.roundwidget.QMUIRoundButton;
import com.tamsiree.rxkit.RxRegTool;
import com.tamsiree.rxkit.view.RxToast;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.BindView;
import butterknife.OnClick;

@BindEventBusInject
@ViewInject(mainLayoutId = R.layout.fragment_addressinput_from)
public class AddressInputFromFragment extends BaseFragment implements IDoSubmitContract.Iview {

    @BindView(R.id.province_city)
    TextView provinceCity;
    @BindView(R.id.area)
    TextView areaText;
    @BindView(R.id.house_number)
    EditText houseNumber;
    @BindView(R.id.name)
    EditText name;
    @BindView(R.id.phone)
    RongDivisionEditText phone;
    @BindView(R.id.address_submit_do)
    QMUIRoundButton addressSubmitDo;
    @BindView(R.id.address_title_tip)
    TextView addressTitleTip;

    private String province;
    private String area;
    private String mHouseNumber;
    private String mName;
    private String mPhone;
    private int type;
    private double latitude;
    private double longitude;
    private double latitudeEnd;
    private double longitudeEnd;

    @Override
    public void afterBindView() {

    }

    @Subscribe(sticky = true, threadMode = ThreadMode.MAIN)
    public void getParam(AddressLocalData data) {
        type = data.getType();
        if (type == 1) {
            addressTitleTip.setText("请填写取货地址");
            latitude = data.getLatitude();
            longitude = data.getLongitude();
            if (!TextUtils.isEmpty(data.getProvince())) {
                provinceCity.setText(data.getProvince());
            }
            if (!TextUtils.isEmpty(data.getArea())) {
                areaText.setText(data.getArea());
            }

        } else {
            addressTitleTip.setText("请填写要带到地址");
            latitudeEnd = data.getEndLatitude();
            longitudeEnd = data.getEndLongitude();
            if (!TextUtils.isEmpty(data.getEndProvince())) {
                provinceCity.setText(data.getEndProvince());
            }
            if (!TextUtils.isEmpty(data.getEndArea())) {
                areaText.setText(data.getEndArea());
            }
        }
    }

    /**
     * 表单验证
     *
     * @return
     */
    private void checkParam() {
        province = provinceCity.getText().toString();
        area = areaText.getText().toString();
        String mAddress = province + area;
        if (mAddress == null) {
            RxToast.error("请选择地址");
            return;
        }

        mHouseNumber = houseNumber.getText().toString();
        if (TextUtils.isEmpty(mHouseNumber)) {
            RxToast.error("请输入门牌号");
            return;
        }

        mName = name.getText().toString();
        if (TextUtils.isEmpty(mName)) {
            RxToast.error("请输入联系人");
            return;
        }

        mPhone = phone.getContent();
        if (!RxRegTool.isMobile(mPhone)) {
            RxToast.error("请输入正确的手机号");
            return;
        }

        submitDo();
    }

    private void submitDo() {
        addressSubmitDo.setEnabled(false); // 设置button禁止点击
        ((AddressActivity) mActivity).showLoading();
        IDoSubmitContract.IPresenter mPresenter = new DoSubmitPresenter(this);
        if (type == 1) {
            mPresenter.doSubmit(type, mName, mPhone, latitude, longitude, province, area, mHouseNumber);
        } else {
            mPresenter.doSubmit(type, mName, mPhone, latitudeEnd, longitudeEnd, province, area, mHouseNumber);
        }

    }

    @OnClick({R.id.go_back, R.id.address_submit_do})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.go_back:

                break;
            case R.id.address_submit_do:
                checkParam();
                break;
        }
    }

    @OnClick(R.id.select_address_search)
    public void onClick() {
        ((AddressActivity) mContext).chanceFragment(3);
    }

    @Override
    public void resDoSubmit(AddressResBean data) {
        addressSubmitDo.setEnabled(true);
        ((AddressActivity) mActivity).hideTipDialog();

        if (data.code > 0) {

            if (type == 1) {
                EventBus.getDefault().postSticky(AddressLocalData.getInstance().setStartId(data.data.id));
                EventBus.getDefault().postSticky(AddressLocalData.getInstance().setStartUser(mName, mPhone));
            } else {
                EventBus.getDefault().postSticky(AddressLocalData.getInstance().setEndId(data.data.id));
                EventBus.getDefault().postSticky(AddressLocalData.getInstance().setEndUser(mName, mPhone));
            }
            IntentUtil.get().activityKill(mContext, ChooseEndActivity.class);
        } else {
            RxToast.error(data.msg);
        }
    }

    @OnClick(R.id.go_back)
    public void goBack() {
        getActivity().finish();
    }
}
