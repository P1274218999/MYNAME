package com.dhht.sld.main.home.contract;

import com.dhht.annotation.MvpEmptyViewFactory;
import com.dhht.mvp.ILifeCircle;
import com.dhht.mvp.IMvpView;
import com.dhht.sld.main.home.bean.CurrentOrderBean;
import com.dhht.sld.main.login.bean.LoginSuccessBean;

public interface MainBottomContract {

    @MvpEmptyViewFactory
    interface IView extends IMvpView {
        void resOrder(CurrentOrderBean res);
    }

    interface IPresenter extends ILifeCircle{
        void getOrder();
    }

}
