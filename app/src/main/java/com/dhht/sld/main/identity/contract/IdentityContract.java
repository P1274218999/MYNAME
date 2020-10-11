package com.dhht.sld.main.identity.contract;

import com.dhht.annotation.MvpEmptyViewFactory;
import com.dhht.mvp.ILifeCircle;
import com.dhht.mvp.IMvpView;
import com.dhht.sld.base.BaseHttpResBean;
import com.dhht.sld.base.BaseHttpResImgBean;
public interface IdentityContract {

    @MvpEmptyViewFactory
    interface Iview extends IMvpView{
        void  resImgUrl(BaseHttpResImgBean res);

        void resSubmit(BaseHttpResBean res);
    }

    interface IPresenter extends ILifeCircle{
        void upload(String filePath);
    }
}
