package com.dhht.sld.main.findorder.presenter;

import android.util.Log;

import com.dhht.http.result.IResult;
import com.dhht.sld.base.BasePresenter;
import com.dhht.sld.base.DoHttpTask;
import com.dhht.sld.main.findorder.bean.FindOrderBean;
import com.dhht.sld.main.findorder.contract.FindOrderContract;
import com.dhht.sld.main.findorder.model.FindOrderHttpTask;

public class FindOrderPresenter extends BasePresenter<FindOrderContract.Iview> implements FindOrderContract.IPresenter  {

    public FindOrderPresenter(FindOrderContract.Iview view) {
        super(view);
    }

    /**
     * 获取登录的验证码
     * @param type
     */
    @Override
    public void getList(int type)
    {
        submitTask(new DoHttpTask<FindOrderBean>() {
            @Override
            public IResult<FindOrderBean> onBackground() {
                return new FindOrderHttpTask<FindOrderBean>().getList(type);
            }

            @Override
            public void onSuccess(IResult<FindOrderBean> t) {
                FindOrderBean data = t.data();
                getView().resList(data);
            }
        });
    }
}
