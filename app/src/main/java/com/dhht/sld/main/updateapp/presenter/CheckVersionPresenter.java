package com.dhht.sld.main.updateapp.presenter;

import com.dhht.http.result.IResult;
import com.dhht.mvp.base.BaseMvpPresenter;
import com.dhht.sld.base.BasePresenter;
import com.dhht.sld.base.DoHttpTask;
import com.dhht.sld.main.updateapp.bean.CheckVersionBean;
import com.dhht.sld.main.updateapp.contract.CheckVersionContract;
import com.dhht.sld.main.updateapp.model.CheckVersionHttpTask;

/**
 * 作者：pst
 * 邮箱：1274218999@qq.com
 * 创建时间：2020/7/22  17:44
 * 文件描述：
 */
public class CheckVersionPresenter extends BasePresenter<CheckVersionContract.Iview> implements CheckVersionContract.IPresenter{
    public CheckVersionPresenter(CheckVersionContract.Iview view) {
        super(view);
    }
    @Override
    public void doSubmit() {
        submitTask(new DoHttpTask<CheckVersionBean>() {
            @Override
            public IResult<CheckVersionBean> onBackground() {
                return new CheckVersionHttpTask<CheckVersionBean>().doSubmitRequest();
            }
            @Override
            public void onSuccess(IResult<CheckVersionBean> t) {
                getView().resDoSubmit(t.data());
            }
        });
    }
}
