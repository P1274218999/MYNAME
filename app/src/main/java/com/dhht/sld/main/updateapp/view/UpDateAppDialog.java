package com.dhht.sld.main.updateapp.view;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.text.Html;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;

import com.dhht.sld.R;
import com.dhht.sld.base.inject.ViewInject;
import com.dhht.sld.main.updateapp.download.entity.AppUpdate;
import com.dhht.sld.main.updateapp.download.listener.UpdateDialogListener;
import com.dhht.sld.utlis.view.ProgressView;

import java.util.Objects;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 作者：pst
 * 邮箱：1274218999@qq.com
 * 创建时间：2020/7/22  15:48
 * 文件描述：更新提示框
 */
@ViewInject(mainLayoutId = R.layout.dialog_app_update)
public class UpDateAppDialog extends BaseDialog {
    /**
     * 8.0未知应用授权请求码
     */
    private static final int INSTALL_PACKAGES_REQUESTCODE = 1112;
    /**
     * 用户跳转未知应用安装的界面请求码
     */
    private static final int GET_UNKNOWN_APP_SOURCES = 1113;

    private UpdateDialogListener updateDialogListener;
    /**
     * 更新数据
     */
    private AppUpdate appUpdate;
    /**
     * 初始化弹框
     *
     * @param params 参数
     * @return DownloadDialog
     */
    @BindView(R.id.app_update_info)
    TextView appUpdateInfo;
    @BindView(R.id.app_update_btn)
    Button appUpdateBtn;
    @BindView(R.id.progress_view)
    ProgressView progressView;
    public static UpDateAppDialog newInstance(Bundle params) {
        UpDateAppDialog downloadDialog = new UpDateAppDialog();
        if (params != null) {
            downloadDialog.setArguments(params);
        }
        return downloadDialog;
    }
    /**
     * 回调监听
     *
     * @param updateListener 监听接口
     * @return DownloadDialog
     */
    public UpDateAppDialog addUpdateListener(UpdateDialogListener updateListener) {
        this.updateDialogListener = updateListener;
        return this;
    }
    @Override
    public void afterBindView() {
        if (getArguments() != null) {
            appUpdate = getArguments().getParcelable("appUpdate");
        }
        initDialog();
    }

    private void initDialog() {
        if (appUpdate==null) {
            return;
        }
        if (appUpdate.getForceUpdate()==1){
            getDialog().setCancelable(false);
            getDialog().setCanceledOnTouchOutside(false);
            getDialog().setOnKeyListener(new DialogInterface.OnKeyListener() {
                @Override
                public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
                    if (keyCode == KeyEvent.KEYCODE_BACK) {
                        getActivity().finish();
                        return true;
                    }
                    return false;
                }
            });
        }
        appUpdateInfo.setText(appUpdate.getUpdateInfo());

    }


    @OnClick(R.id.app_update_btn)
    public void onClick(){
        appUpdateBtn.setVisibility(View.GONE);
        progressView.setVisibility(View.VISIBLE);
        if (updateDialogListener != null) {
            requestPermission();
        }
    }

    /**
     * 判断存储卡权限
     */
    private void requestPermission() {
        if (getActivity() == null) {
            return;
        }
        //权限判断是否有访问外部存储空间权限
        int flag = ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if (flag != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                // 用户拒绝过这个权限了，应该提示用户，为什么需要这个权限。
                Toast.makeText(getActivity(), getResources().getString(R.string.update_permission), Toast.LENGTH_LONG).show();
            }
            // 申请授权
            requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
        } else {
            if (updateDialogListener != null) {
                updateDialogListener.updateDownLoad();
            }
        }
    }

    /**
     * 申请android O 安装权限
     */
    @RequiresApi(api = Build.VERSION_CODES.M)
    public void requestInstallPermission() {
        UpDateAppDialog.this.requestPermissions(new String[]{Manifest.permission.REQUEST_INSTALL_PACKAGES}, INSTALL_PACKAGES_REQUESTCODE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //8.0应用设置界面未知安装开源返回时候
        if (requestCode == GET_UNKNOWN_APP_SOURCES && Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            boolean allowInstall = Objects.requireNonNull(getContext()).getPackageManager().canRequestPackageInstalls();
            if (allowInstall) {
                dismiss();
                if (updateDialogListener != null) {
                    updateDialogListener.installApkAgain();
                }
            } else {
                Toast.makeText(getContext(), "您拒绝了安装未知来源应用，应用暂时无法更新！", Toast.LENGTH_SHORT).show();
                exit();
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 1) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                //6.0 存储权限授权结果回调
                if (updateDialogListener != null) {
                    updateDialogListener.updateDownLoad();
                }
            } else {
                //提示，并且关闭
                Toast.makeText(getActivity(), getResources().getString(R.string.update_permission), Toast.LENGTH_LONG).show();
                exit();
            }
        } else if (requestCode == INSTALL_PACKAGES_REQUESTCODE) {
            // 8.0的权限请求结果回调,授权成功
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                if (updateDialogListener != null) {
                    updateDialogListener.installApkAgain();
                }
            } else {
                // 授权失败，引导用户去未知应用安装的界面
                if (getContext() != null && android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                    //注意这个是8.0新API
                    Uri packageUri = Uri.parse("package:" + getContext().getPackageName());
                    Intent intent = new Intent(Settings.ACTION_MANAGE_UNKNOWN_APP_SOURCES, packageUri);
                    startActivityForResult(intent, GET_UNKNOWN_APP_SOURCES);
                }
            }
        }
    }

    /**
     * 强制退出
     */
    private void exit() {
        if (0 != appUpdate.getForceUpdate()) {
            if (updateDialogListener != null) {
                updateDialogListener.forceExit();
            }
        } else {
            dismiss();
        }
    }

    public void setProgress(int progress) {
        if (progressView!=null&&progress>0){
            progressView.setValue(progress);
        }
    }

    public void downLoadFail() {
    }

    public void showProgressBar() {
    }
}
