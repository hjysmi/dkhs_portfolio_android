package com.dkhs.portfolio.ui.fragment;


import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.dkhs.portfolio.R;
import com.dkhs.portfolio.bean.SortUserEntity;
import com.dkhs.portfolio.ui.adapter.SelectCityAdapter;
import com.dkhs.portfolio.ui.widget.sortlist.SideBar;
import com.lidroid.xutils.view.annotation.ViewInject;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class SelectCityFragment extends BaseFragment  {

    @ViewInject(R.id.lv_select_city)
    private ListView mCityLv;


    @ViewInject(R.id.sidrbar)
    private SideBar mSidebar;
    @ViewInject(R.id.tv_center_index)
    private TextView mIndexTv;
    private SelectCityAdapter mAdapter;
    private List<SortUserEntity> mSortDateList;

    @Override
    public int setContentLayoutId() {
        return R.layout.fragment_select_city;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mSortDateList = new ArrayList<SortUserEntity>();
        initViews();
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
                //TODO 替换为城市信息
                SortUserEntity item = (SortUserEntity) mAdapter.getItem(position);
                setSelectBack(item);
            }
        });
        getData();
    }

    private void getData() {
    }

    private void setSelectBack(SortUserEntity user) {
        //TODO 返回城市名称
        Intent intent = new Intent();
        intent.putExtra("select_friend", user.getUsername());
        getActivity().setResult(Activity.RESULT_OK, intent);

        getActivity().finish();
    }
}
