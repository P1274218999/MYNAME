package com.dhht.sld.main.updateapp;

import android.content.Context;
import com.dhht.http.result.IResult;
import com.dhht.sld.base.DoHttpTask;
import com.dhht.sld.main.updateapp.bean.CheckVersionBean;
import com.dhht.sld.main.updateapp.download.downloadmanager.UpdateManager;
import com.dhht.sld.main.updateapp.download.entity.AppUpdate;
import com.dhht.sld.main.updateapp.model.CheckVersionHttpTask;
import com.dhht.sld.utlis.AppVersionUtil;
import com.dhht.task.TaskHelper;

/**
 * 作者：pst
 * 邮箱：1274218999@qq.com
 * 创建时间：2020/7/22  16:54
 * 文件描述：app更新
 */
public class CheckUpdateApp {
    private String apkFilePath = "/顺路带/";
    private Context mContext;
    private CheckUpdateAppListener checkUpdateAppListener;
    public CheckUpdateApp(Context context,CheckUpdateAppListener checkUpdateAppListener) {
        this.mContext = context;
        this.checkUpdateAppListener=checkUpdateAppListener;
        DoHttpTask doHttpTask = new DoHttpTask<CheckVersionBean>() {
            @Override
            public IResult<CheckVersionBean> onBackground() {
                return new CheckVersionHttpTask<CheckVersionBean>().doSubmitRequest();
            }

            @Override
            public void onSuccess(IResult<CheckVersionBean> t) {
                if (AppVersionUtil.compareVersion(AppVersionUtil.getVerName(mContext),t.data().data.version)!=0) {
                    String appUpDateInfo="";
                    for (int i = 0; i < t.data().data.content.size(); i++) {
                        if (i==t.data().data.content.size()-1){
                            appUpDateInfo+=t.data().data.content.get(i);break;
                        }
                        appUpDateInfo+=t.data().data.content.get(i)+"\r\n";
                    }

                    AppUpdate appUpdate =new AppUpdate.Builder()
                            .newVersionUrl(t.data().data.apk_url)
                            .savePath(apkFilePath)
                            .newVersionCode(t.data().data.version)
                            .updateInfo(appUpDateInfo)
                            .forceUpdate(t.data().data.force)
                            .isSilentMode(false)
                            .build();
                    new UpdateManager().startUpdate(mContext, appUpdate);
                }else {
                    if (checkUpdateAppListener!=null){
                        checkUpdateAppListener.onComplete();
                    }
                }
            }
        };
        TaskHelper.submitTask(doHttpTask,doHttpTask);
    }
    public static interface CheckUpdateAppListener{
        void onComplete();//检查完无需更新
    }
}
