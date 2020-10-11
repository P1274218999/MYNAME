package com.dhht.sld.main.home.presenter;

import com.dhht.http.result.IResult;
import com.dhht.sld.base.BasePresenter;
import com.dhht.sld.base.DoHttpTask;
import com.dhht.sld.main.home.model.MainHttpTask;
import com.dhht.sld.main.home.bean.CurrentOrderBean;
import com.dhht.sld.main.home.contract.MainBottomContract;

public class MainBottomPresenter extends BasePresenter<MainBottomContract.IView> implements MainBottomContract.IPresenter {

    public MainBottomPresenter(MainBottomContract.IView view) {
        super(view);
    }

    @Override
    public void getOrder() {
        submitTask(new DoHttpTask<CurrentOrderBean>() {
            @Override
            public IResult<CurrentOrderBean> onBackground() {
                return new MainHttpTask<CurrentOrderBean>().getHelpOrder();
            }

            @Override
            public void onSuccess(IResult<CurrentOrderBean> t) {
                CurrentOrderBean data = t.data();
                getView().resOrder(data);
            }
        });
    }
}
