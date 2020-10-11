package com.dhht.sld.main.identity.presenter;

import com.dhht.http.result.IResult;
import com.dhht.sld.base.BaseHttpResBean;
import com.dhht.sld.base.BaseHttpResImgBean;
import com.dhht.sld.base.BasePresenter;
import com.dhht.sld.base.DoHttpTask;
import com.dhht.sld.base.common.CommonHttpTask;
import com.dhht.sld.main.address.bean.AddressResBean;
import com.dhht.sld.main.address.contract.IDoSubmitContract;
import com.dhht.sld.main.address.model.AddressHttpTask;
import com.dhht.sld.main.identity.contract.IdentityContract;
import com.dhht.sld.main.identity.model.IdentityHttpTask;

public class IdentityPresenter extends BasePresenter<IdentityContract.Iview> implements IdentityContract.IPresenter {
    public IdentityPresenter(IdentityContract.Iview view) {
        super(view);
    }

    public void submit(String name,String id_no,String img_front,String img_reverse){
        submitTask(new DoHttpTask<BaseHttpResBean>() {
            @Override
            public IResult<BaseHttpResBean> onBackground() {
                return new IdentityHttpTask<BaseHttpResBean>().doSubmit(name,id_no,img_front,img_reverse);
            }

            @Override
            public void onSuccess(IResult t) {
                BaseHttpResBean res = (BaseHttpResBean) t.data();
                getView().resSubmit(res);
            }
        });
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
}
