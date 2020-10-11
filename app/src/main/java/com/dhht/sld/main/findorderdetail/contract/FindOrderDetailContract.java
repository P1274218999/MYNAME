package com.dhht.sld.main.findorderdetail.contract;

import com.dhht.annotation.MvpEmptyViewFactory;
import com.dhht.mvp.ILifeCircle;
import com.dhht.mvp.IMvpView;
import com.dhht.sld.main.findorderdetail.bean.FindOrderDetailBean;

public interface FindOrderDetailContract {

    @MvpEmptyViewFactory
    interface IView extends IMvpView {
        void resFindOrder(FindOrderDetailBean res);
    }

    interface IPresenter extends ILifeCircle{

    }

}
