package com.dhht.sld.main.receiving.presenter;

import com.dhht.http.result.IResult;
import com.dhht.sld.base.BasePresenter;
import com.dhht.sld.base.DoHttpTask;
import com.dhht.sld.main.home.model.MainHttpTask;
import com.dhht.sld.main.home.bean.MarkerBean;
import com.dhht.sld.main.receiving.contract.ReceivingMapContract;

public class ReceivingMapPresenter extends BasePresenter<ReceivingMapContract.IView> implements ReceivingMapContract.IPresenter  {

    public ReceivingMapPresenter(ReceivingMapContract.IView view) {
        super(view);
    }

    public void getMarker(int id)
    {
        submitTask(new DoHttpTask<MarkerBean>() {
            @Override
            public IResult<MarkerBean> onBackground() {
                return new MainHttpTask<MarkerBean>().getHelpMarkerOne(id);
            }

            @Override
            public void onSuccess(IResult<MarkerBean> t) {
                MarkerBean data = t.data();
                getView().resMarkerInfo(data);
            }
        });
    }
}
