package com.dhht.sld.main.login.presenter;

import com.dhht.http.result.IResult;
import com.dhht.sld.base.BasePresenter;
import com.dhht.sld.base.DoHttpTask;
import com.dhht.sld.main.login.bean.LoginSuccessBean;
import com.dhht.sld.main.login.contract.LoginHttpContract;
import com.dhht.sld.main.login.model.LoginHttpTask;

public class LoginGetCodePresenter extends BasePresenter<LoginHttpContract.GetCodeView> implements LoginHttpContract.GetCodePresenter  {

    public LoginGetCodePresenter(LoginHttpContract.GetCodeView view) {
        super(view);
    }

    @Override
    public void checkCodeLogin(String phone,String code) {
        submitTask(new DoHttpTask<LoginSuccessBean>(){
            @Override
            public IResult<LoginSuccessBean> onBackground() {
                return new LoginHttpTask<LoginSuccessBean>().checkCodeLogin(phone,code);
            }

            @Override
            public void onSuccess(IResult<LoginSuccessBean> t) {
                LoginSuccessBean data = t.data();
                getView().resCheckCodeLogin(data);
            }
        });
    }
}
