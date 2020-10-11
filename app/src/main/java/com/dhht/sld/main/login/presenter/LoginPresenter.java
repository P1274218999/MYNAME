package com.dhht.sld.main.login.presenter;

import com.dhht.http.result.IResult;
import com.dhht.sld.base.BaseHttpResBean;
import com.dhht.sld.base.BasePresenter;
import com.dhht.sld.base.DoHttpTask;
import com.dhht.sld.main.login.contract.LoginHttpContract;
import com.dhht.sld.main.login.model.LoginHttpTask;

public class LoginPresenter extends BasePresenter<LoginHttpContract.Iview> implements LoginHttpContract.IPresenter  {

    public LoginPresenter(LoginHttpContract.Iview view) {
        super(view);
    }

    /**
     * 获取登录的验证码
     * @param phone
     */
    @Override
    public void getCode(String phone)
    {
        submitTask(new DoHttpTask<BaseHttpResBean>() {
            @Override
            public IResult<BaseHttpResBean> onBackground() {
                return new LoginHttpTask<BaseHttpResBean>().getCode(phone);
            }

            @Override
            public void onSuccess(IResult<BaseHttpResBean> t) {
                BaseHttpResBean data = t.data();
                getView().resGetCode(data);
            }
        });
    }
}
