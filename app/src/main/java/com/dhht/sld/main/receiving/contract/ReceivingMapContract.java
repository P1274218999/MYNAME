package com.dhht.sld.main.receiving.contract;

import com.dhht.annotation.MvpEmptyViewFactory;
import com.dhht.mvp.ILifeCircle;
import com.dhht.mvp.IMvpView;
import com.dhht.sld.main.home.bean.MarkerBean;

public interface ReceivingMapContract {

    @MvpEmptyViewFactory
    interface IView extends IMvpView {
        void resMarkerInfo(MarkerBean res);
    }

    interface IPresenter extends ILifeCircle{

    }

}
