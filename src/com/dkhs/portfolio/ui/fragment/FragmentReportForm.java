/**
 * @Title FragmentReportForm.java
 * @Package com.dkhs.portfolio.ui.fragment
 * @Description TODO(用一句话描述该文件做什么)
 * @author zjz
 * @date 2014-9-4 上午10:28:10
 * @version V1.0
 */
package com.dkhs.portfolio.ui.fragment;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.dkhs.portfolio.R;
import com.dkhs.portfolio.bean.CombinationBean;
import com.dkhs.portfolio.bean.MoreDataBean;
import com.dkhs.portfolio.bean.NetValueReportBean;
import com.dkhs.portfolio.engine.LoadMoreDataEngine.ILoadDataBackListener;
import com.dkhs.portfolio.engine.NetValueEngine;
import com.dkhs.portfolio.engine.NetValueReportEngine;
import com.dkhs.portfolio.ui.CombinationDetailActivity;
import com.dkhs.portfolio.ui.adapter.ReportFromAdapter;
import com.lidroid.xutils.cache.MD5FileNameGenerator;

/**
 * @ClassName FragmentReportForm
 * @Description TODO(这里用一句话描述这个类的作用)
 * @author zjz
 * @date 2014-9-4 上午10:28:10
 * @version 1.0
 */
public class FragmentReportForm extends Fragment {

    private int pageIndex = 1;

    private ListView mListView;
    private ReportFromAdapter mAdapter;

    private String trendType;
    private CombinationBean mCombinationBean;
    private NetValueReportEngine mNetValueDataEngine;

    List<NetValueReportBean> mReportList = new ArrayList<NetValueReportBean>();

    // public static final String TREND_TYPE_TODAY="trend_today";
    public static FragmentReportForm newInstance(String trendType) {
        FragmentReportForm fragment = new FragmentReportForm();

        Bundle arguments = new Bundle();
        arguments.putString(TrendChartFragment.ARGUMENT_TREND_TYPE, trendType);
        fragment.setArguments(arguments);

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        mAdapter = new ReportFromAdapter(getActivity(), mReportList);
        // handle fragment arguments
        Bundle arguments = getArguments();
        if (arguments != null) {
            handleArguments(arguments);
        }
        // handle intent extras
        Bundle extras = getActivity().getIntent().getExtras();
        if (extras != null) {
            handleExtras(extras);
        }
        loadData();
    }

    private void handleArguments(Bundle arguments) {
        trendType = arguments.getString(TrendChartFragment.ARGUMENT_TREND_TYPE);
    }

    private void handleExtras(Bundle extras) {
        mCombinationBean = (CombinationBean) extras.getSerializable(CombinationDetailActivity.EXTRA_COMBINATION);
        mNetValueDataEngine = new NetValueReportEngine(mCombinationBean.getId(), mLoadBackListener);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_report_form, null);
        initView(view);
        
        System.out.println("onCreateView");
        return view;
    }

    private void initView(View view) {
        mListView = (ListView) view.findViewById(R.id.lv_report_form);
        mListView.setAdapter(mAdapter);

    }
    
    /**  
     * @Title
     * @Description TODO: (用一句话描述这个方法的功能)
     * @param activity
     * @return
     */
    @Override
    public void onAttach(Activity activity) {
        // TODO Auto-generated method stub
        super.onAttach(activity);
        System.out.println("OnAttach");
    }

    /**  
     * @Title
     * @Description TODO: (用一句话描述这个方法的功能)
     * @return
     */
    @Override
    public void onStart() {
        // TODO Auto-generated method stub
        super.onStart();
        System.out.println("onStart");
   
    }
    private void loadData() {
        if (!TextUtils.isEmpty(trendType)) {
            if (trendType.equals(TrendChartFragment.TREND_TYPE_SEVENDAY)) {
                mNetValueDataEngine.requerySevenDayReport();
            } else if (trendType.equals(TrendChartFragment.TREND_TYPE_MONTH)) {
                mNetValueDataEngine.requeryMonthReport();
            } else if (trendType.equals(TrendChartFragment.TREND_TYPE_HISTORY)) {
                mNetValueDataEngine.requeryHistoryReport(pageIndex);
            }
        }
    }

    ILoadDataBackListener mLoadBackListener = new ILoadDataBackListener() {

        @Override
        public void loadFinish(MoreDataBean object) {

            List<NetValueReportBean> reportList = object.getResults();
            if (null != reportList) {
                mReportList.addAll(reportList);
                mAdapter.notifyDataSetChanged();

            }
        }

    };

}
