package com.dhht.sld.main.helporder.presenter;

import com.dhht.http.result.IResult;
import com.dhht.sld.base.BasePresenter;
import com.dhht.sld.base.DoHttpTask;
import com.dhht.sld.main.helporder.bean.HelpOrderBean;
import com.dhht.sld.main.helporder.contract.HelpOrderContract;
import com.dhht.sld.main.helporder.model.HelpOrderHttpTask;

public class HelpOrderPresenter extends BasePresenter<HelpOrderContract.Iview> implements HelpOrderContract.IPresenter  {

    public HelpOrderPresenter(HelpOrderContract.Iview view) {
        super(view);
    }

    /**
     * 获取登录的验证码
     * @param type
     */
    @Override
    public void getList(int type)
    {
        submitTask(new DoHttpTask<HelpOrderBean>() {
            @Override
            public IResult<HelpOrderBean> onBackground() {
                return new HelpOrderHttpTask<HelpOrderBean>().getList(type);
            }

            @Override
            public void onSuccess(IResult<HelpOrderBean> t) {
                HelpOrderBean data = t.data();
                getView().resList(data);
            }
        });
    }
}
