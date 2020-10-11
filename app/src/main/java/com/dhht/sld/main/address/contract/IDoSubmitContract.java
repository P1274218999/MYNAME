package com.dhht.sld.main.address.contract;

import com.dhht.annotation.MvpEmptyViewFactory;
import com.dhht.mvp.ILifeCircle;
import com.dhht.mvp.IMvpView;
import com.dhht.sld.main.address.bean.AddressResBean;

public interface IDoSubmitContract {

    @MvpEmptyViewFactory
    interface Iview extends IMvpView{
        void  resDoSubmit(AddressResBean data);

    }

    interface IPresenter extends ILifeCircle{
        void doSubmit(int type,String name,String phone,double latitude,double longitude,String province,String area,String house_number);
    }
}
