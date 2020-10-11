package com.dhht.sld.main.wallet.presenter;

import com.dhht.http.result.IResult;
import com.dhht.sld.base.BasePresenter;
import com.dhht.sld.base.DoHttpTask;
import com.dhht.sld.main.choose.article.Bean.ArticleDataBean;
import com.dhht.sld.main.choose.article.contract.ArticleHttpContract;
import com.dhht.sld.main.choose.article.model.ArticleHttpTask;
import com.dhht.sld.main.wallet.bean.WalletDetailBean;
import com.dhht.sld.main.wallet.contract.WalletDetailHttpContract;
import com.dhht.sld.main.wallet.model.WalletDetailHttpTask;

public class WalletDetailPresenter extends BasePresenter<WalletDetailHttpContract.Iview> implements WalletDetailHttpContract.IPresenter  {

    public WalletDetailPresenter(WalletDetailHttpContract.Iview view) {
        super(view);
    }



    @Override
    public void getList(int page, int type) {
        submitTask(new DoHttpTask<WalletDetailBean>() {
            @Override
            public IResult<WalletDetailBean> onBackground() {
                return new WalletDetailHttpTask<WalletDetailBean>().getList(page,type);
            }

            @Override
            public void onSuccess(IResult<WalletDetailBean> t) {
                WalletDetailBean walletDetailBean=t.data();
                getView().resList(walletDetailBean);
            }
        });
    }


}
