package com.dhht.sld.libs.faceverify;

import com.dhht.annotation.MvpEmptyViewFactory;
import com.dhht.mvp.ILifeCircle;
import com.dhht.mvp.IMvpView;

public interface FaceContract {

    @MvpEmptyViewFactory
    interface Iview extends IMvpView{
        void  resGetFaceSign(FaceSignBean data);
    }

    interface IPresenter extends ILifeCircle{
        void getSign(String identityName,String identityNum);
    }
}
