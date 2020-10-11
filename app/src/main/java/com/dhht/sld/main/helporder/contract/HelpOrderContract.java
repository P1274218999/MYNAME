package com.dhht.sld.main.helporder.contract;

import com.dhht.annotation.MvpEmptyViewFactory;
import com.dhht.mvp.ILifeCircle;
import com.dhht.mvp.IMvpView;
import com.dhht.sld.main.helporder.bean.HelpOrderBean;

public interface HelpOrderContract {

    @MvpEmptyViewFactory
    interface Iview extends IMvpView {
        void resList(HelpOrderBean res);
    }

    interface IPresenter extends ILifeCircle {
        void getList(int type);
    }
}
