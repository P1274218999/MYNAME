package com.dhht.sld.main.personal.view;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.provider.MediaStore;
import android.provider.Settings;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.dhht.sld.R;
import com.dhht.sld.base.BaseFragment;
import com.dhht.sld.base.BaseHttpResBean;
import com.dhht.sld.base.BaseHttpResImgBean;
import com.dhht.sld.base.inject.ViewInject;
import com.dhht.sld.main.login.bean.LoginSuccessBean;
import com.dhht.sld.main.personal.contract.PersonalContract;
import com.dhht.sld.main.personal.presenter.PersonalPresenter;
import com.dhht.sld.utlis.ToastUtli;
import com.dhht.sld.utlis.UserSPUtli;
import com.dhht.sld.utlis.view.BottomDialog;
import com.qmuiteam.qmui.widget.QMUIRadiusImageView2;
import com.qmuiteam.qmui.widget.dialog.QMUIDialog;
import com.qmuiteam.qmui.widget.dialog.QMUIDialogAction;
import com.tamsiree.rxkit.RxPhotoTool;
import com.tamsiree.rxkit.RxPictureTool;
import com.tamsiree.rxkit.view.RxToast;
import com.yalantis.ucrop.UCrop;
import com.yalantis.ucrop.UCropActivity;


import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 作者：pst
 * 邮箱：1274218999@qq.com
 * 创建时间：2020/7/27  9:29
 * 文件描述：
 */

@ViewInject(mainLayoutId = R.layout.fragment_personal)
public class PersonalFragment extends BaseFragment implements PersonalContract.Iview {
    @BindView(R.id.toolbar_title)
    TextView commonTitle;
    @BindView(R.id.toolbar_title_right)
    TextView titleRight;
    @BindView(R.id.personal_user_img)
    QMUIRadiusImageView2 userImg;
    @BindView(R.id.personal_user_name)
    TextView userName;
    @BindView(R.id.personal_user_phone)
    TextView userPhone;
    @BindView(R.id.personal_user_approve)
    TextView userApprove;
    PersonalPresenter personalPresenter;
    LoginSuccessBean.ResData userData;

    @Override
    public void afterBindView() {
        personalPresenter = new PersonalPresenter(this);
        iniview();
    }

    private void iniview() {
        commonTitle.setText("个人信息设置");
        titleRight.setVisibility(View.INVISIBLE);
        userData = UserSPUtli.getInstance().getUserData();
        Glide.with(mActivity)
                .load(userData.avatar)
                .apply(new RequestOptions().placeholder(R.mipmap.default_user_img).skipMemoryCache(true))
                .into(userImg);
        if (TextUtils.isEmpty(userData.name)) {
            userName.setText("未认证");
        } else {
            userName.setText(userData.name);
        }
        userApprove.setText(userData.is_identity ? "是" : "否");
        userPhone.setText(userData.phone);
    }

    @OnClick({R.id.personal_user_img})
    public void onClick(View view) {
        switch (view.getId()) {

            case R.id.personal_user_img:
                showPhotoDialog();
                break;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RxPhotoTool.GET_IMAGE_FROM_PHONE && resultCode == -1) {//相册
            if (data == null) return;
            initUCrop(data.getData());
        } else if (requestCode == RxPhotoTool.GET_IMAGE_BY_CAMERA && resultCode == -1) {//相机
            initUCrop(RxPhotoTool.imageUriFromCamera);
        } else if (requestCode == RxPhotoTool.CROP_IMAGE && resultCode == -1) {//图片剪裁
            Uri resultUri = UCrop.getOutput(data);
            String imageAbsolutePath = RxPhotoTool.getImageAbsolutePath(mContext, resultUri);//图片绝对路劲
            File file=new File(imageAbsolutePath);
            personalPresenter.upload(file.getPath());
            ToastUtli.getInstance(mContext).showLoading("图片上传中...");
        }
    }


    /**
     * @param res 上传头像
     */
    @Override
    public void resImgUrl(BaseHttpResImgBean res) {
        Glide.with(mActivity)
                .load(res.data.url)
                .apply(new RequestOptions().placeholder(R.mipmap.default_user_img).skipMemoryCache(true))
                .into(userImg);
        if (res.code > 0 && !TextUtils.isEmpty(res.data.url)) {
            personalPresenter.updateAvatar(res.data.url);
            userData.avatar = res.data.url;
        } else {
            RxToast.error("图片上传失败");
            ToastUtli.getInstance(mContext).hideTipDialog();
        }

    }

    /**
     * @param res 更新头像成功
     */
    @Override
    public void upDateAvResult(BaseHttpResBean res) {
        if (res.code > 0) {
            ToastUtli.getInstance(mContext).hideTipDialog();
            ToastUtli.getInstance(mContext).showSuccessTip("头像更新成功");
            UserSPUtli.getInstance().saveUser(userData);
        } else {
            ToastUtli.getInstance(mContext).hideTipDialog();
            ToastUtli.getInstance(mContext).showSuccessTip("头像更新失败");
        }
    }


    /**
     * @param uri 跳转图片裁剪
     */
    private void initUCrop(@NonNull Uri uri) {
        SimpleDateFormat timeFormatter = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.CHINA);
        long time = System.currentTimeMillis();
        String imageName = timeFormatter.format(new Date(time));
        Uri destinationUri = Uri.fromFile(new File(mContext.getCacheDir(), imageName + ".jpeg"));
        UCrop.Options options = new UCrop.Options();
        options.setAllowedGestures(UCropActivity.SCALE, UCropActivity.ROTATE, UCropActivity.ALL);
        //设置隐藏底部容器，默认显示
        //options.setHideBottomControls(true);
        //设置toolbar颜色
        options.setToolbarColor(ActivityCompat.getColor(mContext, R.color.colorPrimary));
        //设置状态栏颜色
        options.setStatusBarColor(ActivityCompat.getColor(mContext, R.color.colorPrimaryDark));

        //开始设置
        //设置最大缩放比例
        options.setMaxScaleMultiplier(5f);
        //设置图片在切换比例时的动画
        options.setImageToCropBoundsAnimDuration(666);
        //设置裁剪窗口是否为椭圆
        //options.setCircleDimmedLayer(true);
        //设置是否展示矩形裁剪框
        // options.setShowCropFrame(false);
        //设置裁剪框横竖线的宽度
        //options.setCropGridStrokeWidth(20);
        //设置裁剪框横竖线的颜色
        //options.setCropGridColor(Color.GREEN);
        //设置竖线的数量
        //options.setCropGridColumnCount(2);
        //设置横线的数量
        //options.setCropGridRowCount(1);
        UCrop.of(uri, destinationUri)
                .withAspectRatio(1f, 1f)
                .withMaxResultSize(1000, 1000)
                .withOptions(options)
                .start(mContext, this, RxPhotoTool.CROP_IMAGE);

    }

    /**
     * 底部弹框
     */
    private void showPhotoDialog() {
        BottomDialog mBottomPhotoDialog = new BottomDialog(mContext, 0, true);
        View view = LayoutInflater.from(mContext).inflate(R.layout.bottom_photo, null, false);

        //取消
        TextView dialog_image_clicked_btn_cancel = view.findViewById(R.id.dialog_image_clicked_btn_cancel);
        dialog_image_clicked_btn_cancel.setOnClickListener(v -> mBottomPhotoDialog.dismiss());
        //从相册选择
        TextView dialog_image_clicked_btn_undetermined = view.findViewById(R.id.dialog_image_clicked_btn_undetermined);
        dialog_image_clicked_btn_undetermined.setText("相册");
        dialog_image_clicked_btn_undetermined.setOnClickListener(v -> {
            mBottomPhotoDialog.dismiss();
            Intent imagePickerIntent = new  Intent(Intent.ACTION_PICK, null);
            imagePickerIntent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
            startActivityForResult(imagePickerIntent, RxPhotoTool.GET_IMAGE_FROM_PHONE);
        });
        //拍照
        TextView dialog_image_clicked_btn_delete = view.findViewById(R.id.dialog_image_clicked_btn_delete);
        dialog_image_clicked_btn_delete.setText("拍照");
        dialog_image_clicked_btn_delete.setOnClickListener(v -> {
            mBottomPhotoDialog.dismiss();
            requestPermission();
        });
        mBottomPhotoDialog.setContentView(view);
        // 设置背景为透明色 那么白色的就能呈现出来了
        mBottomPhotoDialog.getDelegate().findViewById(R.id.design_bottom_sheet)
                .setBackgroundColor(mContext.getResources().getColor(android.R.color.transparent));
        mBottomPhotoDialog.show();
    }


    /**
     * 打开相机权限
     */
    private void requestPermission() {
        if (getActivity() == null) {
            return;
        }
        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(), Manifest.permission.CAMERA)) {
                // 用户拒绝过这个权限了，应该提示用户，为什么需要这个权限。
                RxToast.normal("请授权打开相机权限");
            }
            // 申请授权
            requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 10001);
        } else {
            RxPhotoTool.openCameraImage(this);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 10001) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                RxPhotoTool.openCameraImage(this);
            }
        } else {
            RxToast.normal("请授权打开相机权限");
            new QMUIDialog.MessageDialogBuilder(mActivity)
                    .setTitle("请求授权打开相机权限")
                    .setMessage("")
                    .addAction("去开启", new QMUIDialogAction.ActionListener() {
                        @Override
                        public void onClick(QMUIDialog dialog, int index) {
                            dialog.dismiss();
                            Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                            intent.setData(Uri.parse("package:" + mContext.getPackageName()));
                            startActivityForResult(intent,111);
                        }
                    }).show();
        }
    }
}
