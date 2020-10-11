package com.dhht.sld.main.choose.article.presenter;

import com.dhht.http.result.IResult;
import com.dhht.sld.base.BaseHttpResImgBean;
import com.dhht.sld.base.BasePresenter;
import com.dhht.sld.base.DoHttpTask;
import com.dhht.sld.base.common.CommonHttpTask;
import com.dhht.sld.main.choose.article.Bean.ArticleDataBean;
import com.dhht.sld.main.choose.article.contract.ArticleHttpContract;
import com.dhht.sld.main.choose.article.model.ArticleHttpTask;

public class ArticlePresenter extends BasePresenter<ArticleHttpContract.Iview> implements ArticleHttpContract.IPresenter  {

    public ArticlePresenter(ArticleHttpContract.Iview view) {
        super(view);
    }

    @Override
    public void upload(String filePath) {
        submitTask(new DoHttpTask<BaseHttpResImgBean>() {
            @Override
            public IResult<BaseHttpResImgBean> onBackground() {
                return new CommonHttpTask<BaseHttpResImgBean>().upload(filePath);
            }
            @Override
            public void onSuccess(IResult t) {
                BaseHttpResImgBean data = (BaseHttpResImgBean) t.data();
                getView().resImgUrl(data);
            }
        });
    }


    /**
     *
     * @param status
     */
    @Override
    public void getList(int status)
    {
        submitTask(new DoHttpTask<ArticleDataBean>() {
            @Override
            public IResult<ArticleDataBean> onBackground() {
                return new ArticleHttpTask<ArticleDataBean>().getList(status);
            }

            @Override
            public void onSuccess(IResult<ArticleDataBean> t) {
                ArticleDataBean data = t.data();
                getView().resList(data);
            }
        });
    }
}
