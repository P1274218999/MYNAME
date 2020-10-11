package com.dhht.sld.main.receiving.contract;

import com.dhht.annotation.MvpEmptyViewFactory;
import com.dhht.mvp.ILifeCircle;
import com.dhht.mvp.IMvpView;
import com.dhht.sld.base.BaseHttpResBean;
import com.dhht.sld.main.home.bean.MarkerBean;

public interface ReceivingContract {

    @MvpEmptyViewFactory
    interface IView extends IMvpView {
        void resMarkerInfo(MarkerBean res);

        void resCancelOrder(BaseHttpResBean res);

        void resYesQuhuo(BaseHttpResBean res);

        void resYesSongHuo(BaseHttpResBean res);
    }

    interface IPresenter extends ILifeCircle{

    }

}
