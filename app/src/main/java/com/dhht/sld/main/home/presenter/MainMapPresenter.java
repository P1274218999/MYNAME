package com.dhht.sld.main.home.presenter;

import com.dhht.http.result.IResult;
import com.dhht.sld.base.BasePresenter;
import com.dhht.sld.base.DoHttpTask;
import com.dhht.sld.main.home.bean.MarkersBean;
import com.dhht.sld.main.home.contract.MainMapContract;

public class MainMapPresenter extends BasePresenter<MainMapContract.IView> implements MainMapContract.IPresenter {

    public MainMapPresenter(MainMapContract.IView view) {
        super(view);
    }

    public void getMarker(int pageSize) {
        submitTask(new DoHttpTask<MarkersBean>() {
            @Override
            public IResult<MarkersBean> onBackground() {
                return null;
            }

            @Override
            public void onSuccess(IResult<MarkersBean> t) {

            }
        });
    }
}
