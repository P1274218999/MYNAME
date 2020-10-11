package com.dhht.sld.main.home.presenter;

import com.dhht.http.result.IResult;
import com.dhht.sld.base.BasePresenter;
import com.dhht.sld.base.DoHttpTask;
import com.dhht.sld.main.home.bean.MarkersBean;
import com.dhht.sld.main.home.contract.MainMapContract;
import com.dhht.sld.main.home.contract.UserContract;
import com.dhht.sld.main.login.bean.LoginSuccessBean;

public class UserLeftPresenter extends BasePresenter<UserContract.IView> implements UserContract.IPresenter {

    public UserLeftPresenter(UserContract.IView view) {
        super(view);
    }

    @Override
    public void getUser() {
        submitTask(new DoHttpTask<LoginSuccessBean>() {
            @Override
            public IResult<LoginSuccessBean> onBackground() {
                return null;
            }

            @Override
            public void onSuccess(IResult<LoginSuccessBean> t) {

            }
        });
    }
}
