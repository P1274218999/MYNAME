package com.dhht.sld.main.home.contract;

import com.dhht.annotation.MvpEmptyViewFactory;
import com.dhht.mvp.ILifeCircle;
import com.dhht.mvp.IMvpView;
import com.dhht.sld.base.BaseHttpResBean;

public interface HelpPeopleBottomContract {

    @MvpEmptyViewFactory
    interface IView extends IMvpView {
        void resReceiving(BaseHttpResBean res);
    }

    interface IPresenter extends ILifeCircle{
        void doReceiving(int id);
    }

}
