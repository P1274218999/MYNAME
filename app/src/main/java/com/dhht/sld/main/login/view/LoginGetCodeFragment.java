package com.dhht.sld.main.login.view;

import android.os.CountDownTimer;
import android.view.View;
import android.widget.TextView;

import com.dhht.sld.MainActivity;
import com.dhht.sld.R;
import com.dhht.sld.base.BaseFragment;
import com.dhht.sld.base.BaseHttpResBean;
import com.dhht.sld.base.inject.BindEventBusInject;
import com.dhht.sld.base.inject.ViewInject;
import com.dhht.sld.main.login.bean.LoginSuccessBean;
import com.dhht.sld.main.login.contract.LoginHttpContract;
import com.dhht.sld.main.login.presenter.LoginGetCodePresenter;
import com.dhht.sld.main.login.presenter.LoginPresenter;
import com.dhht.sld.main.login.view.design.VerificationCodeView;
import com.dhht.sld.utlis.IntentUtil;
import com.dhht.sld.utlis.UserSPUtli;
import com.tamsiree.rxkit.RxKeyboardTool;
import com.tamsiree.rxkit.view.RxToast;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.BindView;
import butterknife.OnClick;

@BindEventBusInject
@ViewInject(mainLayoutId = R.layout.fragment_login_getcode)
public class LoginGetCodeFragment extends BaseFragment implements LoginHttpContract.Iview,LoginHttpContract.GetCodeView{

    @BindView(R.id.code_countdown)
    TextView codeCountdown;
    @BindView(R.id.code_countdown_text)
    TextView codeCountdownText;
    @BindView(R.id.send_phone)
    TextView sendPhone;
    @BindView(R.id.code_number)
    VerificationCodeView codeNumber;

    private CountDownTimer timer;
    private String mPhone;

    @Override
    public void afterBindView() {
        if (timer == null) countDown();
        changeEdit();
        RxKeyboardTool.showSoftInput(mContext, codeNumber.getEditView());
    }

    @Subscribe(sticky = true, threadMode = ThreadMode.MAIN)
    public void getParam(String phone) {
        mPhone = phone;
        sendPhone.setText(phone);
    }

    /**
     * 再次获取验证码
     */
    @OnClick(R.id.code_countdown)
    public void onClick() {
        timer = null;
        LoginHttpContract.IPresenter mIPresenter = new LoginPresenter(this);
        mIPresenter.getCode(mPhone);
    }

    /**
     * 监听验证码输入完成
     */
    private void changeEdit()
    {
        LoginHttpContract.GetCodePresenter mGetCodePresenter = new LoginGetCodePresenter(this);
        codeNumber.setOnInputListener(new VerificationCodeView.OnInputListener() {
            @Override
            public void onSuccess(String code) {
                ((LoginActivity)mActivity).showLoading("登录中...");
                // 发起后端校验验证码登录
                mGetCodePresenter.checkCodeLogin(mPhone, code);
            }

            @Override
            public void onInput(String code) {}
        });
    }

    /**
     * 倒计时显示
     */
    private void countDown() {
        timer = new CountDownTimer(60000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                codeCountdown.setEnabled(false);
                codeCountdown.setText(millisUntilFinished / 1000 + "秒");
                codeCountdownText.setVisibility(View.VISIBLE);
            }

            @Override
            public void onFinish() {
                codeCountdownText.setVisibility(View.GONE);
                codeCountdown.setEnabled(true);
                codeCountdown.setText("重新获取验证码");
            }
        };
        timer.start();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        // 关闭倒计时
        if (timer != null) {
            timer.cancel();
            timer = null;
        }
    }

    @Override
    public void resGetCode(BaseHttpResBean res) {
        if (res.code > 0) {
            RxToast.success(res.msg);
            countDown();
        } else {
            RxToast.error(res.msg);
        }
    }

    /**
     * (登录)校验验证码返回
     * @param res
     */
    @Override
    public void resCheckCodeLogin(LoginSuccessBean res) {
        ((LoginActivity)mActivity).hideTipDialog();
        if (res.code > 0)
        {
            // 记录登录信息
            UserSPUtli.getInstance().saveUser(res.data);
            if (UserSPUtli.getInstance().isLogin())
            {
                RxToast.success(res.msg);
                IntentUtil.get().activityKillAllRef(mContext,MainActivity.class);

            }else{
                RxToast.error("系统异常");
            }
        }else{
            RxToast.error(res.msg);
        }
    }

}
