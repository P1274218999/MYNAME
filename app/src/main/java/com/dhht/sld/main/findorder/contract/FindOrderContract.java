package com.dhht.sld.main.findorder.contract;

import com.dhht.annotation.MvpEmptyViewFactory;
import com.dhht.mvp.ILifeCircle;
import com.dhht.mvp.IMvpView;
import com.dhht.sld.main.findorder.bean.FindOrderBean;

public interface FindOrderContract {

    @MvpEmptyViewFactory
    interface Iview extends IMvpView {
        void resList(FindOrderBean res);
    }

    interface IPresenter extends ILifeCircle {
        void getList(int type);
    }
}
