package com.dhht.sld.main.home.contract;

import com.dhht.annotation.MvpEmptyViewFactory;
import com.dhht.mvp.ILifeCircle;
import com.dhht.mvp.IMvpView;
import com.dhht.sld.main.home.bean.MarkerBean;
import com.dhht.sld.main.home.model.HelpRoutResModel;

import java.util.List;

public interface MainMapContract {

    @MvpEmptyViewFactory
    interface IView extends IMvpView {
        void resChooseMarker(List<HelpRoutResModel> rout, MarkerBean data);
        void resFail(String msg);
        void resCameraChange(String msg);
    }

    interface IPresenter extends ILifeCircle{

    }

    public interface IFindPresenter {
    }
}
