package com.dhht.sld.libs.faceverify;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;

import com.webank.facelight.contants.WbCloudFaceContant;
import com.webank.facelight.contants.WbFaceError;
import com.webank.facelight.contants.WbFaceVerifyResult;
import com.webank.facelight.listerners.WbCloudFaceVeirfyLoginListner;
import com.webank.facelight.listerners.WbCloudFaceVeirfyResultListener;
import com.webank.facelight.tools.WbCloudFaceVerifySdk;
import com.webank.facelight.ui.FaceVerifyStatus;

public class FaceVerify {
    private Context mContext;

    //这些都是 WbCloudFaceVerifySdk.InputData 对象里的字段，是需要传入的数据信息
    String faceId;  //此次刷脸用户标识，合作方需要向人脸识别后台拉取获得，详见获取 faceId 接口
    String orderNo;  //订单号
    String openApiNonce; //32位随机字符串
    String userId; //user id
    String openApiSign; //签名信息
    String openApiAppId;  //APP_ID
    String openApiAppVersion;  //Version
    String keyLicence; //给合作方派发的licence
    //刷脸类别：
    //光线活体 FaceVerifyStatus.Mode.REFLECTION
    //动作活体 FaceVerifyStatus.Mode.ACT
    //数字活体 FaceVerifyStatus.Mode.NUM
    FaceVerifyStatus.Mode verifyMode = FaceVerifyStatus.Mode.ACT;

    public FaceVerify(Context context,
                      String openApiAppId,
                      String orderNo,
                      String faceId,
                      String userId,
                      String openApiNonce,
                      String openApiSign,
                      String openApiAppVersion,
                      String keyLicence)
    {
        mContext = context;
        this.openApiAppId = openApiAppId;
        this.orderNo = orderNo;
        this.faceId = faceId;
        this.userId = userId;
        this.openApiNonce = openApiNonce;
        this.openApiSign = openApiSign;
        this.openApiAppVersion = openApiAppVersion;
        this.keyLicence = keyLicence;
    }

    public void init()
    {
        Log.e("debug","开始人脸识别:"
                +faceId
                +" "+orderNo
                +" "+openApiAppId
                +" "+openApiAppVersion
                +" "+openApiNonce
                +" "+userId
                +" "+openApiSign
                +" "+verifyMode
                +" "+keyLicence);

        Bundle data = new Bundle();
        WbCloudFaceVerifySdk.InputData inputData = new WbCloudFaceVerifySdk.InputData(
                faceId,
                orderNo,
                openApiAppId,
                openApiAppVersion,
                openApiNonce,
                userId,
                openApiSign,
                verifyMode,
                keyLicence);

        data.putSerializable(WbCloudFaceContant.INPUT_DATA, inputData);

        //个性化参数设置,可以不设置，不设置则为默认选项。
        //是否显示成功结果页，默认显示，此处设置为不显示
        data.putBoolean(WbCloudFaceContant.SHOW_SUCCESS_PAGE, true);
        //是否展示刷脸失败页面，默认展示,此处设置为不显示
        data.putBoolean(WbCloudFaceContant.SHOW_FAIL_PAGE, true);
        //sdk样式设置，默认为黑色
        //此处设置为白色
        data.putString(WbCloudFaceContant.COLOR_MODE, WbCloudFaceContant.BLACK);

        //设置选择的比对类型  默认为权威库网纹图片比对
        //权威数据源比对 WbCloudFaceContant.ID_CRAD
        //自带比对源比对  WbCloudFaceContant.SRC_IMG
        //仅活体检测  WbCloudFaceContant.NONE
        //此处设置权威数据源对比
        data.putString(WbCloudFaceContant.COMPARE_TYPE, WbCloudFaceContant.ID_CARD); //ID_CRAD
        //是否需要录制上传视频 默认需要，此处设置为需要
        data.putBoolean(WbCloudFaceContant.VIDEO_UPLOAD, true);
        //是否对录制视频进行检查,默认不检查，此处设置为不检查
        data.putBoolean(WbCloudFaceContant.VIDEO_CHECK, false);
        //设置是否打开闭眼检测，默认不检测,此处设置为检测
        data.putBoolean(WbCloudFaceContant.ENABLE_CLOSE_EYES, true);
        //设置是否打开语音提示，默认打开，此处设置为关闭
        data.putBoolean(WbCloudFaceContant. PLAY_VOICE, true);

        //初始化 SDK，得到是否登录 SDK 成功的结果
        WbCloudFaceVerifySdk.getInstance().initSdk(mContext, data, new WbCloudFaceVeirfyLoginListner() {
            @Override
            public void onLoginSuccess() {
                //登录成功，拉起sdk页面
                WbCloudFaceVerifySdk.getInstance().startWbFaceVeirifySdk(mContext, new WbCloudFaceVeirfyResultListener() {
                    @Override
                    public void onFinish(WbFaceVerifyResult wbFaceVerifyResult) {
                        if(wbFaceVerifyResult != null) {
                            if (wbFaceVerifyResult.isSuccess()) {
                                Log.e("debug", "刷脸成功！");
                                listener.success();
                            } else {
                                Log.e("debug", "刷脸失败！");
                                listener.fail();
                            }
                        }
                    }
                });
            }

            @Override
            public void onLoginFailed(WbFaceError wbFaceError) {
                Log.e("debug", "登录失败："+wbFaceError.getCode()+" "+wbFaceError.getDesc()+" "+wbFaceError.getDomain());
            }
        });
    }

    // 刷脸回调
    private OnFaceVerifListener listener;
    public interface OnFaceVerifListener {
        void success();
        void fail();
    }
    public void setOnItemClickListener(OnFaceVerifListener listener) {
        this.listener = listener;
    }
}
