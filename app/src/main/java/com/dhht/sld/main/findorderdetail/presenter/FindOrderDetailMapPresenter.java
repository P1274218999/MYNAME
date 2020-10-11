package com.dhht.sld.main.findorderdetail.presenter;

import com.dhht.http.result.IResult;
import com.dhht.sld.base.BasePresenter;
import com.dhht.sld.base.DoHttpTask;
import com.dhht.sld.main.findorderdetail.bean.FindOrderDetailBean;
import com.dhht.sld.main.findorderdetail.contract.FindOrderDetailContract;
import com.dhht.sld.main.findorderdetail.contract.FindOrderDetailMapContract;
import com.dhht.sld.main.findorderdetail.model.FindOrderDetailHttpTask;

public class FindOrderDetailMapPresenter extends BasePresenter<FindOrderDetailMapContract.IView> implements FindOrderDetailMapContract.IPresenter  {

    public FindOrderDetailMapPresenter(FindOrderDetailMapContract.IView view) {
        super(view);
    }

    public void getHelpOrder(int user_help_order_id) {
        submitTask(new DoHttpTask<FindOrderDetailBean>() {
            @Override
            public IResult<FindOrderDetailBean> onBackground() {
                return new FindOrderDetailHttpTask<FindOrderDetailBean>().getUserHelpOrderOne(user_help_order_id);
            }

            @Override
            public void onSuccess(IResult<FindOrderDetailBean> t) {
                FindOrderDetailBean data = t.data();
                getView().resFindOrder(data);
            }
        });
    }
}
