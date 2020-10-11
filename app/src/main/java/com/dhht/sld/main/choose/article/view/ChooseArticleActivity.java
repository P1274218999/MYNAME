package com.dhht.sld.main.choose.article.view;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.text.Html;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.dhht.sld.R;
import com.dhht.sld.base.BaseActivity;
import com.dhht.sld.base.BaseHttpResImgBean;
import com.dhht.sld.base.inject.ViewInject;
import com.dhht.sld.main.address.model.AddressLocalData;
import com.dhht.sld.main.choose.article.Bean.ArticleDataBean;
import com.dhht.sld.main.choose.article.contract.ArticleHttpContract;
import com.dhht.sld.main.choose.article.presenter.ArticlePresenter;
import com.dhht.sld.utlis.IconFontView;
import com.dhht.sld.utlis.ToastUtli;
import com.qmuiteam.qmui.widget.dialog.QMUIDialog;
import com.qmuiteam.qmui.widget.dialog.QMUIDialogAction;
import com.tamsiree.rxkit.RxFileTool;
import com.tamsiree.rxkit.RxPhotoTool;
import com.tamsiree.rxkit.view.RxToast;

import org.greenrobot.eventbus.EventBus;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import top.zibin.luban.Luban;
import top.zibin.luban.OnCompressListener;

@ViewInject(mainLayoutId = R.layout.activity_choose_goods)
public class ChooseArticleActivity extends BaseActivity implements ArticleHttpContract.Iview {

    @BindView(R.id.click_question)
    IconFontView clickQuestion;
    @BindView(R.id.article_list)
    RecyclerView articleList;
    @BindView(R.id.article_picture_list)
    RecyclerView pictureList;
    @BindView(R.id.text_article_info)
    EditText textArticleInfo;

    private BaseQuickAdapter<ArticleDataBean.ListArticle,BaseViewHolder> adapter;
    private BaseQuickAdapter<String, BaseViewHolder> pictureAdapter;

    private final int cameraResCode = 10010;
    private final int delPicCode = 10011;
    private int articleId = 0;
    private String articleInfo;
    private String articleName;
    private ArrayList<String> pictureUrlList=new ArrayList<>();
    private ArticlePresenter presenter;
    private List<IconFontView> fontViews = new ArrayList<>();


    @Override
    public void afterBindView() {
        initRecyclerView();
        initPictureAdapter();
        getParam(AddressLocalData.getInstance());
    }


    public void getParam(AddressLocalData data)
    {
        if (data==null)return;
        if (data.getArticleId()>0&&!TextUtils.isEmpty(data.getArticleName())){
            this.articleId=data.getArticleId();
            this.articleName=data.getArticleName();
            if (!TextUtils.isEmpty(data.getArticleInfo())) {
                textArticleInfo.setText(data.getArticleInfo());
            }
        }
        if (data.pictureUrlList!=null&&data.pictureUrlList.size()>0){
            this.pictureUrlList=data.pictureUrlList;
            if (pictureUrlList.size()<3){
                pictureUrlList.add("add");
            }
            pictureAdapter.replaceData(pictureUrlList);
        }else {
            pictureUrlList.add("add");
            pictureAdapter.replaceData(pictureUrlList);
        }
        getList();
    }
    private void getList() {
        showLoading();
        presenter = new ArticlePresenter(this);
        presenter.getList(1);
    }

    private void initRecyclerView() {
        adapter = new BaseQuickAdapter<ArticleDataBean.ListArticle, BaseViewHolder>(R.layout.view_list_choose_article) {
            @Override
            protected void convert(BaseViewHolder helper, ArticleDataBean.ListArticle item) {
                fontViews.add(helper.getView(R.id.article_icon));
                helper.setText(R.id.article_text_title,item.name);
                if(articleId==item.id){
                    helper.setTextColor(R.id.article_icon,mContext.getResources().getColor(R.color.appTheme));
//                    helper.setTextColor(R.id.article_text_title,mContext.getResources().getColor(R.color.appTheme));
                }

                helper.setText(R.id.article_icon,Html.fromHtml(item.icon));
                helper.getView(R.id.item_view).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        for (IconFontView fontView : fontViews) {
                            fontView.setTextColor(mContext.getResources().getColor(R.color.gray));
                        }
                        helper.setTextColor(R.id.article_icon,mContext.getResources().getColor(R.color.appTheme));
//                        helper.setTextColor(R.id.article_text_title,mContext.getResources().getColor(R.color.appTheme));
                          articleId = item.id;
                          articleName=item.name;
                    }
                });
            }
        };
        articleList.setLayoutManager(new GridLayoutManager(mActivity, 4));
        articleList.setAdapter(adapter);
    }

    private void initPictureAdapter() {
        pictureList.setLayoutManager(new GridLayoutManager(mActivity, 3));
        pictureAdapter = new BaseQuickAdapter<String, BaseViewHolder>(R.layout.view_list_choose_article_picture) {
            @Override
            protected void convert(BaseViewHolder helper, String item) {
                ImageView picture = helper.getView(R.id.choose_article_picture);
                LinearLayout pictureAdd = helper.getView(R.id.choose_article_picture_add);
                if (item.equals("add")) {
                    picture.setVisibility(View.GONE);
                    pictureAdd.setVisibility(View.VISIBLE);
                } else {
                    Glide.with(mActivity)
                            .load(item)
                            .apply(new RequestOptions().skipMemoryCache(true))
                            .into(picture);
                    picture.setVisibility(View.VISIBLE);
                    pictureAdd.setVisibility(View.GONE);
                }
                pictureAdd.setOnClickListener(view -> {
                    if (item.equals("add")) {
                        requestPermission();
                    }
                });
                picture.setOnClickListener(v -> {
                    if (!item.equals("add")) {
                        if(pictureUrlList.size()<4){
                            pictureUrlList.remove("add");
                        }
                        Intent intent = new Intent(ChooseArticleActivity.this, PreviewPictureActivity.class);
                        Bundle mBundle = new Bundle();
                        mBundle.putStringArrayList("pictureUrlList", pictureUrlList);
                        int adapterPosition = helper.getAdapterPosition();
                        mBundle.putInt("position", adapterPosition);
                        intent.putExtras(mBundle);
                        startActivityForResult(intent, delPicCode);
                    }
                });

            }
        };
        pictureList.setAdapter(pictureAdapter);
    }

    @Override
    public void resImgUrl(BaseHttpResImgBean res) {
        if (res.code > 0 && !TextUtils.isEmpty(res.data.url)) {
            ToastUtli.getInstance(mActivity).hideTipDialog();
            ToastUtli.getInstance(mActivity).showSuccessTip("上传成功");
            pictureUrlList.add(pictureUrlList.size() - 1, res.data.url + "");
            if (pictureUrlList.size() >= 4) {
                pictureUrlList.remove("add");
            }
            pictureAdapter.replaceData(pictureUrlList);
            Log.e("debug",pictureUrlList.toString());
        } else {
            RxToast.error("图片上传失败");
            ToastUtli.getInstance(mActivity).hideTipDialog();
        }
    }

    @Override
    public void resList(ArticleDataBean res) {
        hideTipDialog();
        fontViews.clear();
        adapter.replaceData(res.data);
        adapter.notifyDataSetChanged();
    }

    @OnClick({R.id.click_question, R.id.article_submit_do, R.id.toolbar_back})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.click_question:
                break;
            case R.id.article_submit_do:
                doSubmit();
                break;
            case R.id.toolbar_back:
                finish();
                break;
        }
    }

    private void doSubmit() {
        if (articleId <= 0) {
            RxToast.error("请选择物品类型");
            return;
        }
        articleInfo = textArticleInfo.getText().toString();
        if (TextUtils.isEmpty(articleInfo)) {
            articleInfo="";
        }
        if (pictureUrlList.contains("add")) {
            pictureUrlList.remove("add");
        }
        EventBus.getDefault().postSticky(AddressLocalData.getInstance().setArticle(articleId, articleName, articleInfo,pictureUrlList));
        finish();
    }

    /**
     * 打开相机权限
     */
    private void requestPermission() {
        int camera = ActivityCompat.checkSelfPermission(ChooseArticleActivity.this, Manifest.permission.CAMERA);
        int readES = ActivityCompat.checkSelfPermission(ChooseArticleActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE);
        int writeES = ActivityCompat.checkSelfPermission(ChooseArticleActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if (camera!=0&&readES!=0&&writeES!=0) {
//            if (ActivityCompat.shouldShowRequestPermissionRationale(ChooseArticleActivity.this, Manifest.permission.CAMERA)) {
//                // 用户拒绝过这个权限了，应该提示用户，为什么需要这个权限。
//                RxToast.normal("请授权打开相机权限");
//            }
            // 申请授权
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermissions(new String[]{Manifest.permission.CAMERA,Manifest.permission.READ_EXTERNAL_STORAGE,Manifest.permission.WRITE_EXTERNAL_STORAGE}, 10001);
            }
        } else {
            RxPhotoTool.openCameraImage(mActivity);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 10001) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                RxPhotoTool.openCameraImage(mActivity);
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
                            intent.setData(Uri.parse("package:" + mActivity.getPackageName()));
                            startActivityForResult(intent, 111);
                        }
                    }).show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // 拍照返回
        if (requestCode == RxPhotoTool.GET_IMAGE_BY_CAMERA&&resultCode==-1){
            String imageAbsolutePath = RxPhotoTool.getImageAbsolutePath(mActivity, RxPhotoTool.imageUriFromCamera);
//            String copyPath= new UploadResUtli(mActivity).copyCameraPiccture(imageAbsolutePath, 1024);
//            presenter.upload(copyPath);
            Luban.with(mActivity)
                    .load(imageAbsolutePath)// 传人要压缩的图片列表
                    .putGear(3)
                    .ignoreBy(1024)                                            // 忽略不压缩图片的大小
                    .setTargetDir(RxFileTool.getDiskCacheDir(mActivity))                        // 设置压缩后文件存储位置
                    .setCompressListener(new OnCompressListener() { //设置回调
                        @Override
                        public void onStart() {
                            ToastUtli.getInstance(mActivity).showLoading("图片处理中...");
                        }

                        @Override
                        public void onSuccess(File file) {
                            ToastUtli.getInstance(mActivity).showLoading("图片上传中...");
                            presenter.upload(file.getPath());
                        }

                        @Override
                        public void onError(Throwable e) {
                            System.out.println("出错");
                            ToastUtli.getInstance(mActivity).showFailTip("图片处理中...");
                        }
                    }).launch();    //启动压缩
        }

        // 删除返回
        if(requestCode == delPicCode){
            if (data.getExtras()!=null){
                pictureUrlList=data.getExtras().getStringArrayList("pictureUrlList");
            }
            if (pictureUrlList.size()<3){
                pictureUrlList.add("add");
                pictureAdapter.replaceData(pictureUrlList);
            }
        }
    }
}
