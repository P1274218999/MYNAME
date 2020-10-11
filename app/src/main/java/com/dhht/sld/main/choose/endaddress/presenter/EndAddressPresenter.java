package com.dhht.sld.main.choose.endaddress.presenter;

import com.dhht.http.result.IResult;
import com.dhht.sld.base.BaseHttpResBean;
import com.dhht.sld.base.BasePresenter;
import com.dhht.sld.base.DoHttpTask;
import com.dhht.sld.main.choose.endaddress.bean.CheckingPriceBean;
import com.dhht.sld.main.choose.endaddress.contract.EndAddressHttpContract;
import com.dhht.sld.main.choose.endaddress.model.CheckingPriceHttpTask;
import com.dhht.sld.main.choose.endaddress.model.EndAddressHttpTask;

public class EndAddressPresenter extends BasePresenter<EndAddressHttpContract.Iview> implements EndAddressHttpContract.IPresenter {

    public EndAddressPresenter(EndAddressHttpContract.Iview view) {
        super(view);
    }

    @Override
    public void doCreate(int articleId, int startId, int endId) {
        submitTask(new DoHttpTask<BaseHttpResBean>(){

            @Override
            public IResult<BaseHttpResBean> onBackground() {
                return new EndAddressHttpTask<BaseHttpResBean>().doCreate(articleId,startId,endId);
            }

            @Override
            public void onSuccess(IResult<BaseHttpResBean> t) {
                BaseHttpResBean data = t.data();
                getView().resCreate(data);
            }
        });
    }

    @Override
    public void doCheck(int articleId, int startId, int endId) {
        submitTask(new DoHttpTask<CheckingPriceBean>(){
            @Override
            public IResult<CheckingPriceBean> onBackground() {
                return new CheckingPriceHttpTask<CheckingPriceBean>().doCreate(articleId,startId,endId);
            }
            @Override
            public void onSuccess(IResult<CheckingPriceBean> t) {
                CheckingPriceBean data = t.data();
                getView().resCheck(data);
            }
        });
    }
}
