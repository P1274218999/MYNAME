package com.dhht.sld.main.home.contract;

import com.dhht.annotation.MvpEmptyViewFactory;
import com.dhht.mvp.ILifeCircle;
import com.dhht.mvp.IMvpView;
import com.dhht.sld.main.home.bean.MarkerBean;
import com.dhht.sld.main.home.model.HelpRoutResModel;
import com.dhht.sld.main.login.bean.LoginSuccessBean;

import java.util.List;

public interface MainContract {

    @MvpEmptyViewFactory
    interface IView extends IMvpView {
        void resUser(LoginSuccessBean res);
    }

    interface IPresenter extends ILifeCircle{
        void getUser();
    }

}
