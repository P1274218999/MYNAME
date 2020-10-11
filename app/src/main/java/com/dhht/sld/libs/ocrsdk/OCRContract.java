package com.dhht.sld.libs.ocrsdk;

import com.dhht.annotation.MvpEmptyViewFactory;
import com.dhht.mvp.ILifeCircle;
import com.dhht.mvp.IMvpView;

public interface OCRContract {

    @MvpEmptyViewFactory
    interface Iview extends IMvpView{
        void resGetOCRSign(OCRBean res);

        void resOCRResult(OCRResultBean res);
    }

    interface IPresenter extends ILifeCircle{
        void getSign();
    }
}
