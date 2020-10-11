package com.dhht.sld.libs.ocrsdk;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.webank.mbank.ocr.WbCloudOcrSDK;
import com.webank.mbank.ocr.net.EXIDCardResult;
import com.webank.mbank.ocr.tools.ErrorCode;

public class OCRVerify {
    private Context mContext;
    private String orderNo;  //订单号
    private String appId;  //APP_ID
    private String version;  //Version
    private String nonce; //32位随机字符串
    private String userId; //user id
    private String sign; //签名信息


    public OCRVerify(Context context,
                     String orderNo,
                     String appId,
                     String version,
                     String nonce,
                     String userId,
                     String sign)
    {
        mContext = context;
        this.orderNo = orderNo;
        this.appId = appId;
        this.version = version;
        this.nonce = nonce;
        this.userId = userId;
        this.sign = sign;
    }

    public void init() {
        Log.e("debug", "开始身份证识别:"
                + orderNo
                + " " + appId
                + " " + version
                + " " + nonce
                + " " + userId
                + " " + sign);

        Bundle data = new Bundle();
        WbCloudOcrSDK.InputData inputData = new WbCloudOcrSDK.InputData(
                orderNo,
                appId,
                version,
                nonce,
                userId,
                sign);
        data.putSerializable(WbCloudOcrSDK.INPUT_DATA, inputData);
        //个性化参数设置,可以不设置，不设置则为默认选项。
        //此处均设置为和默认设置不同
        data.putString(WbCloudOcrSDK.TITLE_BAR_COLOR, "#FF9D1F");
        //设置 SDK 标题栏文字内容，默认展示身份证识别,此处设置为身份证识别
        data.putString(WbCloudOcrSDK.TITLE_BAR_CONTENT, "身份证识别");
        //设置 SDK 水印文字内容，默认仅供内部业务使用，此处设置为仅供本次业务使用
        data.putString(WbCloudOcrSDK.WATER_MASK_TEXT, "仅供本次业务使用");
        //设置扫描识别的时间上限,默认 20 秒，建议默认
        data.putLong(WbCloudOcrSDK.SCAN_TIME, 20000);

        //初始化 SDK，得到是否登录 SDK 成功的结果
        WbCloudOcrSDK.getInstance().init(mContext, data, new WbCloudOcrSDK.OcrLoginListener() {
            @Override
            public void onLoginSuccess() {
                // 跳转至身份证识别
                WbCloudOcrSDK.getInstance().startActivityForOcr(mContext, new WbCloudOcrSDK.IDCardScanResultListener() {
                    @Override
                    public void onFinish(String resultCode, String resultMsg) {
                        listener.complete();

                        // resultCode为0，则识别成功；否则识别失败
                        if ("0".equals(resultCode)) {
                            listener.success(WbCloudOcrSDK.getInstance().getResultReturn());
                        }else{
                            listener.fail(resultMsg);
                        }

                    }
                }, WbCloudOcrSDK.WBOCRTYPEMODE.WBOCRSDKTypeNormal); // WBOCRSDKTypeNormal
            }

            @Override
            public void onLoginFailed(String s, String s1) {
                listener.fail("身份证识别登录失败");
            }
        });
    }

    // 刷脸回调
    private OnOCRVerifListener listener;
    public interface OnOCRVerifListener {
        void success(EXIDCardResult result);
        void fail(String msg);
        void complete();
    }
    public void setOnItemClickListener(OnOCRVerifListener listener) {
        this.listener = listener;
    }
}
