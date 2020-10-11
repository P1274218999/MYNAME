package com.dhht.sld.libs.ocrsdk;

import com.dhht.http.result.IResult;
import com.dhht.sld.base.BasePresenter;
import com.dhht.sld.base.DoHttpTask;

public class OCRPresenter extends BasePresenter<OCRContract.Iview> implements OCRContract.IPresenter {

    public OCRPresenter(OCRContract.Iview view) {
        super(view);
    }

    @Override
    public void getSign() {
        submitTask(new DoHttpTask<OCRBean>() {
            @Override
            public IResult<OCRBean> onBackground() {
                return new OCRHttpTask<OCRBean>().getSign();
            }

            @Override
            public void onSuccess(IResult t) {
                OCRBean data = (OCRBean) t.data();
                getView().resGetOCRSign(data);
            }
        });
    }

    public void getResult( String orderNo, String nonce){
        submitTask(new DoHttpTask<OCRResultBean>() {
            @Override
            public IResult<OCRResultBean> onBackground() {
                return new OCRHttpTask<OCRResultBean>().getResult(orderNo,nonce);
            }

            @Override
            public void onSuccess(IResult t) {
                OCRResultBean res = (OCRResultBean) t.data();
                getView().resOCRResult(res);
            }
        });
    }
}
