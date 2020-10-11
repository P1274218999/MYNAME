package com.dhht.sld.main.home.presenter;

import com.dhht.http.result.IResult;
import com.dhht.sld.base.BaseHttpResBean;
import com.dhht.sld.base.BasePresenter;
import com.dhht.sld.base.DoHttpTask;
import com.dhht.sld.main.home.model.MainHttpTask;
import com.dhht.sld.main.home.contract.HelpPeopleBottomContract;

public class HelpPeopleBottomPresenter extends BasePresenter<HelpPeopleBottomContract.IView> implements HelpPeopleBottomContract.IPresenter {

    public HelpPeopleBottomPresenter(HelpPeopleBottomContract.IView view) {
        super(view);
    }

    @Override
    public void doReceiving(int id) {
        submitTask(new DoHttpTask<BaseHttpResBean>() {
            @Override
            public IResult<BaseHttpResBean> onBackground() {
                return new MainHttpTask<BaseHttpResBean>().doReceiving(id);
            }

            @Override
            public void onSuccess(IResult<BaseHttpResBean> t) {
                getView().resReceiving(t.data());
            }
        });
    }
}
