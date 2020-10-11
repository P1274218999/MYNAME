package com.dhht.sld.main.choose.endaddress.contract;

import com.dhht.annotation.MvpEmptyViewFactory;
import com.dhht.mvp.ILifeCircle;
import com.dhht.mvp.IMvpView;
import com.dhht.sld.base.BaseHttpResBean;
import com.dhht.sld.main.choose.endaddress.bean.CheckingPriceBean;

public interface EndAddressHttpContract {

    @MvpEmptyViewFactory
    interface Iview extends IMvpView {
        void resCreate(BaseHttpResBean res);

        void resCheck(CheckingPriceBean res);
    }

    interface IPresenter extends ILifeCircle {
        void doCreate(int articleId, int startId, int endId);

        void doCheck(int articleId, int startId, int endId);
    }
}
