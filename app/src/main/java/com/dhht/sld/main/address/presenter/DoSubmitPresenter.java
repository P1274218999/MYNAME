package com.dhht.sld.main.address.presenter;

import com.dhht.http.result.IResult;
import com.dhht.sld.base.BaseHttpResBean;
import com.dhht.sld.base.BasePresenter;
import com.dhht.sld.base.DoHttpTask;
import com.dhht.sld.main.address.bean.AddressResBean;
import com.dhht.sld.main.address.contract.IDoSubmitContract;
import com.dhht.sld.main.address.model.AddressHttpTask;

public class DoSubmitPresenter extends BasePresenter<IDoSubmitContract.Iview> implements IDoSubmitContract.IPresenter {
    public DoSubmitPresenter(IDoSubmitContract.Iview view) {
        super(view);
    }

    @Override
    public void doSubmit(int type,String name,String phone,double latitude,double longitude,String province,String area,String house_number) {
        submitTask(new DoHttpTask<AddressResBean>() {
            @Override
            public IResult<AddressResBean> onBackground() {
                return new AddressHttpTask<AddressResBean>().doSubmitRequest(
                        type,
                        name,
                        phone,
                        latitude,
                        longitude,
                        province,
                        area,
                        house_number
                        );
            }

            @Override
            public void onSuccess(IResult t) {
                AddressResBean data = (AddressResBean) t.data();
                getView().resDoSubmit(data);
            }
        });
    }
}
