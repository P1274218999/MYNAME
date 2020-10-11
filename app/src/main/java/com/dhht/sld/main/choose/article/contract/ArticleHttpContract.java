package com.dhht.sld.main.choose.article.contract;

import com.dhht.annotation.MvpEmptyViewFactory;
import com.dhht.mvp.ILifeCircle;
import com.dhht.mvp.IMvpView;
import com.dhht.sld.base.BaseHttpResImgBean;
import com.dhht.sld.main.choose.article.Bean.ArticleDataBean;

public interface ArticleHttpContract {

    @MvpEmptyViewFactory
    interface Iview extends IMvpView{
        void  resImgUrl(BaseHttpResImgBean res);
        void resList(ArticleDataBean res);
    }

    interface IPresenter extends ILifeCircle{
        void upload(String filePath);
        void getList(int status);
    }
}
