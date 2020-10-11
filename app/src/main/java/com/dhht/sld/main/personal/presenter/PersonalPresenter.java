package com.dhht.sld.main.personal.presenter;

import com.dhht.http.result.IResult;
import com.dhht.sld.base.BaseHttpResBean;
import com.dhht.sld.base.BaseHttpResImgBean;
import com.dhht.sld.base.BasePresenter;
import com.dhht.sld.base.DoHttpTask;
import com.dhht.sld.base.common.CommonHttpTask;
import com.dhht.sld.main.personal.contract.PersonalContract;
import com.dhht.sld.main.personal.model.UpdateAvatarHttpTask;

public class PersonalPresenter extends BasePresenter<PersonalContract.Iview> implements PersonalContract.IPresenter {
    public PersonalPresenter(PersonalContract.Iview view) {
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

    @Override
    public void updateAvatar(String imgUrl) {
        submitTask(new DoHttpTask<BaseHttpResBean>() {
            @Override
            public IResult<BaseHttpResBean> onBackground() {
                return new UpdateAvatarHttpTask<BaseHttpResBean>().doSubmit(imgUrl);
            }
            @Override
            public void onSuccess(IResult t) {
                BaseHttpResBean data = (BaseHttpResBean) t.data();

                getView().upDateAvResult(data);
            }
        });
    }
}
