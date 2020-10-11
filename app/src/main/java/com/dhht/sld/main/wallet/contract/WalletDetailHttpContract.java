package com.dhht.sld.main.wallet.contract;

import com.dhht.annotation.MvpEmptyViewFactory;
import com.dhht.mvp.ILifeCircle;
import com.dhht.mvp.IMvpView;
import com.dhht.sld.main.choose.article.Bean.ArticleDataBean;
import com.dhht.sld.main.wallet.bean.WalletDetailBean;

public interface WalletDetailHttpContract {

    @MvpEmptyViewFactory
    interface Iview extends IMvpView{
        void resList(WalletDetailBean res);
    }

    interface IPresenter extends ILifeCircle{
        void getList(int page,int type);
    }
}
