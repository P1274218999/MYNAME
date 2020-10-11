package com.dhht.sld.main.identity.view;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.dhht.sld.R;
import com.dhht.sld.base.BaseFragment;
import com.dhht.sld.base.BaseHttpResBean;
import com.dhht.sld.base.BaseHttpResImgBean;
import com.dhht.sld.base.inject.ViewInject;
import com.dhht.sld.libs.faceverify.FaceContract;
import com.dhht.sld.libs.faceverify.FacePresenter;
import com.dhht.sld.libs.faceverify.FaceSignBean;
import com.dhht.sld.libs.faceverify.FaceVerify;
import com.dhht.sld.libs.ocrsdk.OCRBean;
import com.dhht.sld.libs.ocrsdk.OCRContract;
import com.dhht.sld.libs.ocrsdk.OCRPresenter;
import com.dhht.sld.libs.ocrsdk.OCRResultBean;
import com.dhht.sld.libs.ocrsdk.OCRVerify;
import com.dhht.sld.main.identity.contract.IdentityContract;
import com.dhht.sld.main.identity.presenter.IdentityPresenter;
import com.dhht.sld.utlis.ToastUtli;
import com.dhht.sld.utlis.UserSPUtli;
import com.tamsiree.rxkit.RxDataTool;
import com.tamsiree.rxkit.RxFileTool;
import com.tamsiree.rxkit.RxTool;
import com.tamsiree.rxkit.interfaces.OnSimpleListener;
import com.webank.mbank.ocr.net.EXIDCardResult;

import java.io.File;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

import butterknife.BindView;
import butterknife.OnClick;

@ViewInject(mainLayoutId = R.layout.fragment_identity_input)
public class IdentityInputFragment extends BaseFragment implements IdentityContract.Iview, FaceContract.Iview, OCRContract.Iview {

    @BindView(R.id.toolbar_title)
    TextView toolbarTitle;
    @BindView(R.id.identity_img_face)
    ImageView identityImgFace;
    @BindView(R.id.identity_idcard)
    ImageView identityIdcard;
    @BindView(R.id.identity_name)
    EditText identityName;
    @BindView(R.id.identity_num)
    EditText identityNum;
    @BindView(R.id.identity_idcard_before)
    LinearLayout identityIdcardBefore;
    @BindView(R.id.identity_idcard_after)
    LinearLayout identityIdcardAfter;
    @BindView(R.id.identity_img_front)
    ImageView identityImgFront;
    @BindView(R.id.identity_img_reverse)
    ImageView identityImgReverse;

    private IdentityPresenter presenter;
    private FacePresenter facePresenter;
    private OCRPresenter ocrPresenter;

    private int requestCode;
    private Boolean isFace = false;
    private Bitmap photo;
    private String picPath;
    private String imgFront;
    private String imgReverse;

    @Override
    public void afterBindView() {
        toolbarTitle.setText("实名认证");
        isAuthentication();

        presenter = new IdentityPresenter(this);
        facePresenter = new FacePresenter(this);
        ocrPresenter = new OCRPresenter(this);
    }

    private void isAuthentication(){
        if (UserSPUtli.getInstance().isAuthentication()){
            ToastUtli.getInstance(mContext).showSuccessTip("已认证，不能修改");
            RxTool.delayToDo(1500, new OnSimpleListener() {
                @Override
                public void doSomething() {
                    getActivity().finish();
                }
            });
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (data == null) return;
        Uri uri = data.getData();
        if (uri != null) {
            this.photo = BitmapFactory.decodeFile(uri.getPath());
        }

        Bundle bundle = data.getExtras();
        if (bundle != null) {
            this.photo = (Bitmap) bundle.get("data");
        } else {
            ((IdentityActivity) mContext).showTip("拍照失败");
            return;
        }

        FileOutputStream fileOutputStream = null;
        try {
            // 获取本应用图片缓存目录
            String saveDir = RxFileTool.getCacheFolder(mContext) + "";
            // 新建目录
            File dir = new File(saveDir);
            if (!dir.exists()) {
                if (!dir.mkdir()) throw new Exception("文件夹创建失败：" + saveDir);
            }
            // 生成文件名
            SimpleDateFormat t = new SimpleDateFormat("yyyyMMddssSSS");
            String filename = "MT" + (t.format(new Date())) + ".jpg";
            // 新建文件
            File file = new File(saveDir, filename);
            // 打开文件输出流
            fileOutputStream = new FileOutputStream(file);
            // 生成图片文件
            this.photo.compress(Bitmap.CompressFormat.JPEG, 100, fileOutputStream);
            // 相片的完整路径
            this.picPath = file.getPath();

            ((IdentityActivity) mContext).showLoading("上传中...");
            presenter.upload(this.picPath);

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (fileOutputStream != null) {
                try {
                    fileOutputStream.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @OnClick({R.id.toolbar_main, R.id.identity_idcard, R.id.identity_img_face,R.id.identity_img_front,R.id.identity_img_reverse})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.toolbar_main:
                getActivity().finish();
                break;
            case R.id.identity_idcard:
            case R.id.identity_img_front:
            case R.id.identity_img_reverse:
                ToastUtli.getInstance(mContext).showLoading();
                ocrPresenter.getSign();
                break;
            case R.id.identity_img_face:
                ((IdentityActivity) mContext).showLoading();
                facePresenter.getSign(identityName.getText().toString(), identityNum.getText().toString());
                break;
        }
    }

    @OnClick(R.id.identity_submit)
    public void onClickSubmit() {
        if (RxDataTool.isNullString(imgFront) || RxDataTool.isNullString(imgReverse)) {
            ((IdentityActivity) mContext).showTip("请先识别身份证");
            return;
        }
        if (!isFace) {
            ((IdentityActivity) mContext).showTip("请先进行人脸识别");
            return;
        }

        presenter.submit(
                identityName.getText().toString(),
                identityNum.getText().toString(),
                imgFront,
                imgReverse);
    }


    @Override
    public void resImgUrl(BaseHttpResImgBean res) {
        ((IdentityActivity) mContext).hideTipDialog();
        if (res.code > 0) {
            if (requestCode == 30001) { // 正面图片
                imgFront = res.data.url;

            }
            if (requestCode == 30002) { // 反面图片
                imgReverse = res.data.url;
            }
        } else {
            ((IdentityActivity) mContext).showFailTip(res.msg);
        }
    }

    /**
     * 人脸识别前获取sign信息
     *
     * @param res
     */
    @Override
    public void resGetFaceSign(FaceSignBean res) {
        ((IdentityActivity) mContext).hideTipDialog();
        if (res.code > 0) {
            FaceVerify faceVerify = new FaceVerify(mContext
                    , res.data.openApiAppId
                    , res.data.orderNo
                    , res.data.faceId
                    , res.data.userId
                    , res.data.openApiNonce
                    , res.data.openApiSign
                    , res.data.openApiAppVersion,
                    res.data.keyLicence);
            faceVerify.init();

            faceVerify.setOnItemClickListener(new FaceVerify.OnFaceVerifListener() {
                @Override
                public void success() {
                    isFace = true;
                    identityImgFace.setImageResource(R.drawable.right_2);
                    ((IdentityActivity) mContext).showTip("刷脸成功");
                }

                @Override
                public void fail() {
                    ((IdentityActivity) mContext).showTip("刷脸失败");
                }
            });
        } else {
            ((IdentityActivity) mContext).showTip(res.msg);
        }
    }

    /**
     * 身份证识别前获取sign信息
     *
     * @param res
     */
    @Override
    public void resGetOCRSign(OCRBean res) {
        if (res.code > 0) {
            OCRVerify ocrVerify = new OCRVerify(mContext,
                    res.data.orderNo,
                    res.data.appId,
                    res.data.version,
                    res.data.nonce,
                    res.data.userId,
                    res.data.sign);
            ocrVerify.init();
            ocrVerify.setOnItemClickListener(new OCRVerify.OnOCRVerifListener() {
                @Override
                public void success(EXIDCardResult result) {
                    identityName.setText(result.name);
                    identityNum.setText(result.cardNum);

                    ToastUtli.getInstance(mContext).showLoading("上传中...");
                    ocrPresenter.getResult(res.data.orderNo, res.data.nonce);
                }

                @Override
                public void fail(String msg) {
                    ToastUtli.getInstance(mContext).showTip("身份证识别失败");
                }

                @Override
                public void complete() {
                    ToastUtli.getInstance(mContext).hideTipDialog();
                }
            });
        } else {
            ToastUtli.getInstance(mContext).showTip(res.msg);
        }
    }

    @Override
    public void resOCRResult(OCRResultBean res) {
        ToastUtli.getInstance(mContext).hideTipDialog();
        if (!RxDataTool.isNullString(res.data.front_url)) {
            imgFront = res.data.front_url;
            Glide.with(this).load(imgFront).into(identityImgFront);
        }
        if (!RxDataTool.isNullString(res.data.back_url)) {
            imgReverse = res.data.back_url;
            Glide.with(this).load(imgReverse).into(identityImgReverse);
        }
        identityIdcardBefore.setVisibility(View.GONE);
        identityIdcardAfter.setVisibility(View.VISIBLE);
    }

    /**
     * 点击提交按钮后的返回数据
     *
     * @param res
     */
    @Override
    public void resSubmit(BaseHttpResBean res) {
        if (res.code > 0) {
            ((IdentityActivity) mContext).showSuccessTip(res.msg);
            UserSPUtli.getInstance().updateAuthentication(true); // 更新认证状态
            RxTool.delayToDo(1500, new OnSimpleListener() {
                @Override
                public void doSomething() {
                    getActivity().finish();
                }
            });

        } else {
            ((IdentityActivity) mContext).showTip(res.msg);
        }
    }

}
