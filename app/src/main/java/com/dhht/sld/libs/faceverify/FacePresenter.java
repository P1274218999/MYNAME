package com.dhht.sld.libs.faceverify;

import com.dhht.http.result.IResult;
import com.dhht.sld.base.BasePresenter;
import com.dhht.sld.base.DoHttpTask;

public class FacePresenter extends BasePresenter<FaceContract.Iview> implements FaceContract.IPresenter {

    public FacePresenter(FaceContract.Iview view) {
        super(view);
    }

    @Override
    public void getSign(String identityName, String identityNum) {
        submitTask(new DoHttpTask<FaceSignBean>() {
            @Override
            public IResult<FaceSignBean> onBackground() {
                return new FaceHttpTask<FaceSignBean>().getSign(identityName,identityNum);
            }

            @Override
            public void onSuccess(IResult t) {
                FaceSignBean data = (FaceSignBean) t.data();
                getView().resGetFaceSign(data);
            }
        });
    }
}
