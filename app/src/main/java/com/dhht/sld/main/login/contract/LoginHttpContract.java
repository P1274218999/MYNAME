package com.dhht.sld.main.login.contract;

import com.dhht.annotation.MvpEmptyViewFactory;
import com.dhht.mvp.ILifeCircle;
import com.dhht.mvp.IMvpView;
import com.dhht.sld.base.BaseHttpResBean;
import com.dhht.sld.main.login.bean.LoginSuccessBean;

public interface LoginHttpContract {

    @MvpEmptyViewFactory
    interface Iview extends IMvpView{
        void resGetCode(BaseHttpResBean res);
    }

    interface IPresenter extends ILifeCircle{
        void getCode(String phone);
    }

    /**
     * -------------------------------------------
     * 验证码页面
     */
    @MvpEmptyViewFactory
    interface GetCodeView extends IMvpView{
        void resCheckCodeLogin(LoginSuccessBean data);
    }

    interface GetCodePresenter extends ILifeCircle{
        void checkCodeLogin(String phone, String code);
    }
}
