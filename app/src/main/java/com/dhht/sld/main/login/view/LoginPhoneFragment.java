package com.dhht.sld.main.login.view;

import android.util.Log;
import android.view.View;

import com.dhht.sld.R;
import com.dhht.sld.base.BaseFragment;
import com.dhht.sld.base.BaseHttpResBean;
import com.dhht.sld.base.inject.ViewInject;
import com.dhht.sld.main.login.contract.LoginHttpContract;
import com.dhht.sld.main.login.presenter.LoginPresenter;
import com.dhht.sld.main.login.view.design.RongDivisionEditText;
import com.tamsiree.rxkit.RxKeyboardTool;
import com.tamsiree.rxkit.RxRegTool;
import com.tamsiree.rxkit.view.RxToast;

import org.greenrobot.eventbus.EventBus;

import butterknife.BindView;
import butterknife.OnClick;

@ViewInject(mainLayoutId = R.layout.fragment_login_phone)
public class LoginPhoneFragment extends BaseFragment implements LoginHttpContract.Iview {

    @BindView(R.id.edit_phone)
    RongDivisionEditText phoneEdit;

    private String mPhone;

    @Override
    public void afterBindView() {
        RxKeyboardTool.showSoftInput(mContext, phoneEdit);
        Log.e("debug", "afterBindView");
    }

    private void checkParam() {
        mPhone = phoneEdit.getContent();
        if (!RxRegTool.isMobile(mPhone)) {
            RxToast.error("请输入正确的手机号");
            return;
        }

        ((LoginActivity)mActivity).showLoading();
        LoginHttpContract.IPresenter mPresenter = new LoginPresenter(this);
        mPresenter.getCode(mPhone);
    }

    @OnClick({R.id.submit_get_code, R.id.user_reg_agreement, R.id.user_privacy_agreement})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.submit_get_code:
                checkParam();
                break;
            case R.id.user_reg_agreement: // 注册协议
                break;
            case R.id.user_privacy_agreement: //隐私协议
                break;
        }
    }

    @Override
    public void resGetCode(BaseHttpResBean res) {
        ((LoginActivity)mActivity).hideTipDialog();

        if (res.code > 0)
        {
            EventBus.getDefault().postSticky(mPhone);
            LoginActivity loginActivity = (LoginActivity) mContext;
            loginActivity.replaceFragment(2);

        }else{
            RxToast.error(res.msg);
        }
    }

}
