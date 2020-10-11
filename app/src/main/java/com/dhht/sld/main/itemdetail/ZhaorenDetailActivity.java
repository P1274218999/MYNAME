package com.dhht.sld.main.itemdetail;

import android.app.Activity;
import android.os.Build;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.widget.Toolbar;

import com.dhht.sld.base.BaseActivity;
import com.dhht.sld.R;
import com.dhht.sld.base.inject.ViewInject;
import com.qmuiteam.qmui.util.QMUIDisplayHelper;
import com.qmuiteam.qmui.widget.grouplist.QMUICommonListItemView;
import com.qmuiteam.qmui.widget.grouplist.QMUIGroupListView;

import butterknife.BindView;

@ViewInject(mainLayoutId = R.layout.activity_zhaoren_detail)
public class ZhaorenDetailActivity extends BaseActivity implements View.OnClickListener {

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.groupListView)
    QMUIGroupListView mGroupListView;

    @Override
    public void afterBindView()
    {
        toolbar.setTitle("订单详情");
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();

        if (actionBar != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setHomeButtonEnabled(true);
        }

        initItemList();
    }

    /**
     * 初始化
     */
    private void initItemList() {
        QMUICommonListItemView longTitleAndDetail = mGroupListView.createItemView(null,
                "行程描述：",
                "本人于2020.03.26从贵阳市观山湖区去北京市海淀区出差3天，于2020.03.28日返回贵阳，有人需要带东西的话可以联系我。",
                QMUICommonListItemView.VERTICAL,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        int paddingVer = QMUIDisplayHelper.dp2px(this, 12);
        longTitleAndDetail.setPadding(longTitleAndDetail.getPaddingLeft(), paddingVer,
                longTitleAndDetail.getPaddingRight(), paddingVer);

        QMUICommonListItemView itemChux = mGroupListView.createItemView("出行方式");
        itemChux.setAccessoryType(QMUICommonListItemView.ACCESSORY_TYPE_CHEVRON);
        itemChux.setDetailText("自驾");

        QMUICommonListItemView itemTime = mGroupListView.createItemView("出行时间");
        itemTime.setAccessoryType(QMUICommonListItemView.ACCESSORY_TYPE_CHEVRON);
        itemTime.setDetailText("2020-3-31 17:27:08");

        QMUICommonListItemView itemTimeEnd = mGroupListView.createItemView("预计到达时间");
        itemTimeEnd.setAccessoryType(QMUICommonListItemView.ACCESSORY_TYPE_CHEVRON);
        itemTimeEnd.setDetailText("2020-3-31 17:27:18");

        QMUIGroupListView.newSection(this)
                .addItemView(longTitleAndDetail, this::onClick)
                .addItemView(itemChux, this::onClick)
                .addItemView(itemTime, this::onClick)
                .addItemView(itemTimeEnd, this::onClick)
                .addTo(mGroupListView);
    }

    private void initAnima() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {

        }
    }

    public static void start(Activity activity, View view) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {


        }
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * Called when a view has been clicked.
     *
     * @param v The view that was clicked.
     */
    @Override
    public void onClick(View v) {
        // itemList点击
        if (v instanceof QMUICommonListItemView) {
            CharSequence text = ((QMUICommonListItemView) v).getText();
//            Toast.makeText(ZhaorenDetailActivity.this, text + " is Clicked", Toast.LENGTH_SHORT).show();
        }
    }
}
