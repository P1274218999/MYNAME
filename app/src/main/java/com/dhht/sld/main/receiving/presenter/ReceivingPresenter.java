package com.dhht.sld.main.receiving.presenter;

import com.dhht.http.result.IResult;
import com.dhht.sld.base.BaseHttpResBean;
import com.dhht.sld.base.BasePresenter;
import com.dhht.sld.base.DoHttpTask;
import com.dhht.sld.main.home.model.MainHttpTask;
import com.dhht.sld.main.home.bean.MarkerBean;
import com.dhht.sld.main.receiving.contract.ReceivingContract;

public class ReceivingPresenter extends BasePresenter<ReceivingContract.IView> implements ReceivingContract.IPresenter  {

    public ReceivingPresenter(ReceivingContract.IView view) {
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

    public void cancelOrder(int id)
    {
        submitTask(new DoHttpTask<BaseHttpResBean>() {
            @Override
            public IResult<BaseHttpResBean> onBackground() {
                return new MainHttpTask<BaseHttpResBean>().cancelHelpOrder(id);
            }

            @Override
            public void onSuccess(IResult<BaseHttpResBean> t) {
                BaseHttpResBean data = t.data();
                getView().resCancelOrder(data);
            }
        });
    }

    public void yesQuhuo(int id) {
        submitTask(new DoHttpTask<BaseHttpResBean>() {
            @Override
            public IResult<BaseHttpResBean> onBackground() {
                return new MainHttpTask<BaseHttpResBean>().yesQuhuoHelpOrder(id);
            }

            @Override
            public void onSuccess(IResult<BaseHttpResBean> t) {
                BaseHttpResBean data = t.data();
                getView().resYesQuhuo(data);
            }
        });
    }

    public void yesSongHuo(int id) {
        submitTask(new DoHttpTask<BaseHttpResBean>() {
            @Override
            public IResult<BaseHttpResBean> onBackground() {
                return new MainHttpTask<BaseHttpResBean>().yesSongHuoHelpOrder(id);
            }

            @Override
            public void onSuccess(IResult<BaseHttpResBean> t) {
                BaseHttpResBean data = t.data();
                getView().resYesSongHuo(data);
            }
        });
    }
}
