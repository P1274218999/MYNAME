package com.dhht.sld.main.home.presenter;

import com.dhht.http.result.IResult;
import com.dhht.sld.base.BasePresenter;
import com.dhht.sld.base.DoHttpTask;
import com.dhht.sld.main.home.model.MainHttpTask;
import com.dhht.sld.main.home.contract.MainContract;
import com.dhht.sld.main.login.bean.LoginSuccessBean;
import com.dhht.sld.utlis.UserSPUtli;

public class MainPresenter extends BasePresenter<MainContract.IView> implements MainContract.IPresenter {

    public MainPresenter(MainContract.IView view) {
        super(view);
    }

    @Override
    public void getUser() {
        submitTask(new DoHttpTask<LoginSuccessBean>() {
            @Override
            public IResult<LoginSuccessBean> onBackground() {
                return new MainHttpTask<LoginSuccessBean>().rfToken();
            }

            @Override
            public void onSuccess(IResult<LoginSuccessBean> t) {
                LoginSuccessBean data = t.data();
                UserSPUtli.getInstance().saveUser(data.data);
                getView().resUser(data);
            }
        });
    }
}
