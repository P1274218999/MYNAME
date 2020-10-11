package com.dhht.sld.main.address.view;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.amap.api.services.core.PoiItem;
import com.dhht.sld.R;
import com.dhht.sld.base.BaseFragment;
import com.dhht.sld.base.inject.BindEventBusInject;
import com.dhht.sld.base.inject.ViewInject;
import com.dhht.sld.main.address.adapter.SearchResultAdapter;
import com.dhht.sld.main.address.model.AddressLocalData;
import com.dhht.sld.utlis.AMapPoiSearchUtil;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

@BindEventBusInject
@ViewInject(mainLayoutId = R.layout.fragment_address_search)
public class AddressSearchFragment extends BaseFragment {

    @BindView(R.id.search_edit_text)
    EditText searchEditText;
    @BindView(R.id.address_search_recycler)
    RecyclerView addressSearchRecycler;
    @BindView(R.id.address_search_nav)
    LinearLayout addressSearchNav;

    private SearchResultAdapter searchResultAdapter;
    private AMapPoiSearchUtil poiSearchUtil;
    private int type;

    @Override
    public void afterBindView() {
        initRecyclerView();
        initSearchEditText();
        initAMapPoiSearchUtil();
    }

    @Subscribe(sticky = true, threadMode = ThreadMode.MAIN)
    public void getParam(AddressLocalData data)
    {
        type = data.getType();
    }

    private void initAMapPoiSearchUtil() {
        poiSearchUtil = AMapPoiSearchUtil.getInstance(mContext);
        poiSearchUtil.setOnSearchedListener(new AMapPoiSearchUtil.OnSearchedListener() {
            @Override
            public void onSuccess(List<PoiItem> poiItems) {
                updateData(poiItems);
            }
        });
    }

    private void initRecyclerView() {
        searchResultAdapter = new SearchResultAdapter();
        // 绑定点击item事件
        searchResultAdapter.setOnItemClickListener(new SearchResultAdapter.OnItemClickListener() {
            @Override
            public void onClick(String province, String area, double latitude, double longitude) {
                if (type == 1)
                {
                    EventBus.getDefault().postSticky(AddressLocalData.getInstance().setStartAll(province, area, latitude, longitude));
                }else{
                    EventBus.getDefault().postSticky(AddressLocalData.getInstance().setEndAll(province, area, latitude, longitude));
                }

                ((AddressActivity) mContext).chanceFragment(1);
            }
        });
        LinearLayoutManager manager = new LinearLayoutManager(mActivity);
        addressSearchRecycler.setLayoutManager(manager);
        addressSearchRecycler.setAdapter(searchResultAdapter);
    }

    private void initSearchEditText() {
        searchEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String newText = s.toString().trim();
                if (newText.length() > 0) {
                    poiSearchUtil.doSearch(newText, null);
                }else{
                    updateData(null);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    private void updateData(List<PoiItem> poiItems)
    {
        if (poiItems == null || poiItems.size() <= 0)
        {
            addressSearchNav.setVisibility(View.VISIBLE);
        }else{
            addressSearchNav.setVisibility(View.GONE);
        }
        searchResultAdapter.setData(poiItems);
        searchResultAdapter.notifyDataSetChanged();
    }

    @OnClick({R.id.go_back, R.id.often_address_click, R.id.map_choose_click})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.go_back:
                break;
            case R.id.often_address_click:
                break;
            case R.id.map_choose_click:
                ((AddressActivity) mContext).chanceFragment(2);
                break;
        }
    }

}
