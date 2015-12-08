package com.dkhs.portfolio.ui.fragment;


import android.app.Fragment;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.dkhs.portfolio.R;
import com.dkhs.portfolio.bean.CityBean;
import com.dkhs.portfolio.engine.LocalDataEngine.CityEngine;
import com.dkhs.portfolio.ui.adapter.SelectCityAdapter;
import com.dkhs.portfolio.ui.city.SelectCityActivity;
import com.dkhs.portfolio.ui.eventbus.BackCityEvent;
import com.dkhs.portfolio.ui.eventbus.BusProvider;
import com.dkhs.portfolio.ui.eventbus.QueryCityEvent;
import com.dkhs.portfolio.ui.widget.sortlist.SideBar;
import com.dkhs.portfolio.utils.UIUtils;
import com.lidroid.xutils.util.LogUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.squareup.otto.Subscribe;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class SelectCityFragment extends BaseFragment {

    public static final int CITY_TYPE = 0;
    public static final int PROVICE_TYPE = 1;
    private static final String EXTRA_TYPE = "extra_type";
    private static final String EXTRA_PARENT_NAME = "extra_parent_name";
    private static final String EXTRA_PARENT_CODE = "extra_parent_code";
    @ViewInject(R.id.lv_select_city)
    private ListView mCityLv;


    @ViewInject(R.id.sidrbar)
    private SideBar mSidebar;
    @ViewInject(R.id.tv_center_index)
    private TextView mIndexTv;
    private SelectCityAdapter mAdapter;
    private List<CityBean> mCityList;
    private int mCityType;

    /**
     * 如果是城市保存其所有省份名称
     */
    private String mParentName;
    private String mParentCode;

    @Override
    public int setContentLayoutId() {
        return R.layout.fragment_select_city;
    }

    public static SelectCityFragment newInstance(int type, String parentName, String parentCode) {
        SelectCityFragment fg = new SelectCityFragment();
        Bundle args = new Bundle();
        args.putInt(EXTRA_TYPE, type);
        args.putString(EXTRA_PARENT_NAME, parentName);
        args.putString(EXTRA_PARENT_CODE, parentCode);
        fg.setArguments(args);
        return fg;
    }

    private void handleIntent(Bundle bundle) {
        mCityType = bundle.getInt(EXTRA_TYPE);
        mParentName = bundle.getString(EXTRA_PARENT_NAME);
        mParentCode = bundle.getString(EXTRA_PARENT_CODE);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getArguments();
        handleIntent(bundle);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mCityList = new ArrayList<>();
        initViews();
        BusProvider.getInstance().register(this);
    }

    private void initViews() {
        mSidebar.setTextView(mIndexTv);


        //设置右侧触摸监听
        mSidebar.setOnTouchingLetterChangedListener(new SideBar.OnTouchingLetterChangedListener() {

            @Override
            public void onTouchingLetterChanged(String s) {
                //该字母首次出现的位置
                int position = mAdapter.getPositionForSection(s.charAt(0));
                if (position != -1) {
                    mCityLv.setSelection(position);
                }

            }
        });

        mCityLv.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                CityBean item = (CityBean) mAdapter.getItem(position);
                setSelectBack(item);
            }
        });

        mAdapter = new SelectCityAdapter(getActivity(), mCityList);
        mCityLv.setAdapter(mAdapter);
        getData();
    }

    private void getData() {
        if (mCityType == CITY_TYPE) {
            CityEngine.getCity(mParentCode);
        } else {
            CityEngine.getProvice();
        }
    }

    private void setSelectBack(CityBean city) {
        //直辖市或者城市点击时返回到选择页
        if (city.isMunicipality() || mCityType == CITY_TYPE) {
            String comName;
            if (!TextUtils.isEmpty(mParentName)) {
                comName = mParentName + " " + city.name;
            } else {
                comName = city.name;
            }
            LogUtils.d("wys", "cityName" + comName);
            BusProvider.getInstance().post(new BackCityEvent(comName));
            getActivity().finish();
        } else {
            UIUtils.startAnimationActivity(getActivity(), SelectCityActivity.getItent(getActivity(), city.name, city.id));
            getActivity().finish();
        }
    }

    @Subscribe
    public void getQueryData(QueryCityEvent event) {
        if (event != null && event.beans != null) {
            mCityList.addAll(event.beans);
            mAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        BusProvider.getInstance().unregister(this);
    }
}
