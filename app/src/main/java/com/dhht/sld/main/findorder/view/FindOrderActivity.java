package com.dhht.sld.main.findorder.view;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.dhht.sld.R;
import com.dhht.sld.base.BaseActivity;
import com.dhht.sld.base.inject.ViewInject;


@ViewInject(mainLayoutId = R.layout.activity_find_order)
public class FindOrderActivity extends BaseActivity {


    @Override
    public void afterBindView() {

        FindOrderFragment findOrderFragment = new FindOrderFragment();
        addFragment(findOrderFragment,R.id.find_order_activity);
    }
}
