package com.dhht.sld.main.home.contract;

import com.dhht.annotation.MvpEmptyViewFactory;
import com.dhht.mvp.ILifeCircle;
import com.dhht.mvp.IMvpView;
import com.dhht.sld.main.home.bean.MarkerBean;
import com.dhht.sld.main.home.model.HelpRoutResModel;

import java.util.List;

public interface UserContract {

    @MvpEmptyViewFactory
    interface IView extends IMvpView {
        void resUser(List<HelpRoutResModel> rout, MarkerBean data);
    }

    interface IPresenter extends ILifeCircle{
        void getUser();
    }

    interface IFindPresenter extends ILifeCircle {

    }

    interface IHelpPresenter extends ILifeCircle {
        void updateBottomInfo();
    }

}
