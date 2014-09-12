/**
 * @Title FragmentReportForm.java
 * @Package com.dkhs.portfolio.ui.fragment
 * @Description TODO(用一句话描述该文件做什么)
 * @author zjz
 * @date 2014-9-4 上午10:28:10
 * @version V1.0
 */
package com.dkhs.portfolio.ui.fragment;

import com.dkhs.portfolio.R;
import com.dkhs.portfolio.ui.adapter.BaseAdatperSelectStockFund;
import com.dkhs.portfolio.ui.adapter.ReportFromAdapter;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

/**
 * @ClassName FragmentReportForm
 * @Description TODO(这里用一句话描述这个类的作用)
 * @author zjz
 * @date 2014-9-4 上午10:28:10
 * @version 1.0
 */
public class FragmentReportForm extends Fragment {
    private ListView mListView;
    private ReportFromAdapter mAdapter;

    /**
     * @Title
     * @Description TODO: (用一句话描述这个方法的功能)
     * @param savedInstanceState
     * @return
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        mAdapter = new ReportFromAdapter(getActivity());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_report_form, null);
        initView(view);
        return view;
    }

    private void initView(View view) {
        mListView = (ListView) view.findViewById(R.id.lv_report_form);
        mListView.setAdapter(mAdapter);

    }
}
