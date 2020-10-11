package com.dhht.sld.main.system.view;

import android.view.View;
import android.widget.TextView;

import com.dhht.sld.MainActivity;
import com.dhht.sld.R;
import com.dhht.sld.base.BaseFragment;
import com.dhht.sld.base.inject.ViewInject;
import com.dhht.sld.main.updateapp.CheckUpdateApp;
import com.dhht.sld.utlis.ClearCacheUtil;
import com.dhht.sld.utlis.IntentUtil;
import com.dhht.sld.utlis.ToastUtli;
import com.dhht.sld.utlis.UserSPUtli;
import com.tamsiree.rxkit.RxDeviceTool;
import com.tamsiree.rxkit.view.RxToast;

import butterknife.BindView;
import butterknife.OnClick;

@ViewInject(mainLayoutId = R.layout.fragmemt_system_setting_main)
public class SystemSettingMainFragment extends BaseFragment {
    @BindView(R.id.system_setting_cache)
    public TextView fileCache;
    @BindView(R.id.system_setting_version)
    public TextView appVersion;
    private boolean isChecked=false;
    @Override
    public void afterBindView() {
        appVersion.setText("v"+ RxDeviceTool.getAppVersionName(mActivity));
        try {
            fileCache.setText(ClearCacheUtil.getTotalCacheSize(mContext));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    @OnClick({R.id.system_login_out,R.id.system_setting_clear,R.id.system_setting_update})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.system_setting_clear:
                try {
                    ClearCacheUtil.clearAllCache(mContext);
                    ToastUtli.getInstance(mContext).showSuccessTip("清理完成");
                    fileCache.setText(ClearCacheUtil.getTotalCacheSize(mContext));
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            case R.id.system_setting_update:
                if (!isChecked){
                ToastUtli.getInstance(mContext).showLoading("版本检查...");
                    new CheckUpdateApp(mContext, new CheckUpdateApp.CheckUpdateAppListener() {
                        @Override
                        public void onComplete() {
                            isChecked=!isChecked;
                            ToastUtli.getInstance(mContext).showSuccessTip("已是最新版本");
                        }
                    });
                }else {
                    ToastUtli.getInstance(mContext).showSuccessTip("已是最新版本");
                }
                break;
            case R.id.system_login_out:
                if (UserSPUtli.getInstance().outLogin())
                {
                    RxToast.success("退出成功");
                    IntentUtil.get().activityKillAllRef(mContext, MainActivity.class);
                }else{
                    ((SystemSettingActivity)mContext).showTip("退出失败");
                }
                break;
        }

    }
}
