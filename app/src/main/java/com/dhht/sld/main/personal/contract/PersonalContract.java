package com.dhht.sld.main.personal.contract;

import com.dhht.annotation.MvpEmptyViewFactory;
import com.dhht.mvp.ILifeCircle;
import com.dhht.mvp.IMvpView;
import com.dhht.sld.base.BaseHttpResBean;
import com.dhht.sld.base.BaseHttpResImgBean;

public interface PersonalContract {

    @MvpEmptyViewFactory
    interface Iview extends IMvpView{
        void  resImgUrl(BaseHttpResImgBean res);
        void  upDateAvResult(BaseHttpResBean res);
    }

    interface IPresenter extends ILifeCircle{
        void upload(String filePath);
        void updateAvatar(String filePath);
    }
}
