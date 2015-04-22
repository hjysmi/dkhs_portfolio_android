package com.dkhs.portfolio.ui;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v4.widget.SwipeRefreshLayout.OnRefreshListener;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.dkhs.portfolio.R;
import com.dkhs.portfolio.app.PortfolioApplication;
import com.dkhs.portfolio.bean.OptionNewsBean;
import com.dkhs.portfolio.bean.UserEntity;
import com.dkhs.portfolio.common.ConstantValue;
import com.dkhs.portfolio.engine.LoadNewsDataEngine;
import com.dkhs.portfolio.engine.LoadNewsDataEngine.ILoadDataBackListener;
import com.dkhs.portfolio.engine.NewsforModel;
import com.dkhs.portfolio.engine.OpitionNewsEngineImple;
import com.dkhs.portfolio.engine.UserEngineImpl;
import com.dkhs.portfolio.ui.adapter.OptionMarketAdapter;
import com.dkhs.portfolio.ui.fragment.FragmentSelectStockFund;
import com.dkhs.portfolio.ui.fragment.FragmentSelectStockFund.StockViewType;
import com.dkhs.portfolio.ui.fragment.ReportListForAllFragment;
import com.dkhs.portfolio.ui.widget.PullToRefreshListView;
import com.dkhs.portfolio.ui.widget.PullToRefreshListView.OnLoadMoreListener;
import com.dkhs.portfolio.utils.UserEntityDesUtil;
import com.lidroid.xutils.DbUtils;
import com.umeng.analytics.MobclickAgent;

/**
 * 公告
 * 
 * @author weiting
 * 
 */
public class NoticesActivity extends ModelAcitivity {
    private PullToRefreshListView mListView;

    private boolean isLoadingMore;
    private View mFootView;
    private Context context;
    private OptionMarketAdapter mOptionMarketAdapter;
    private List<OptionNewsBean> mDataList;
    private LoadNewsDataEngine mLoadDataEngine;
    boolean first = true;
    private TextView iv;
    private RelativeLayout pb;

    public SwipeRefreshLayout mSwipeLayout;
    private ReportListForAllFragment loadDataListFragment;

    @Override
    protected void onCreate(Bundle arg0) {
        // TODO Auto-generated method stub
        super.onCreate(arg0);
        setContentView(R.layout.fragment_report_news);
        setTitle(R.string.function_notice);
        replaceDataList();
    }

    private void replaceDataList() {
        // view_datalist
        if (null == loadDataListFragment) {
            UserEntity user = UserEngineImpl.getUserEntity();
            if (user != null) {
                if (!TextUtils.isEmpty(user.getAccess_token())) {
                    user = UserEntityDesUtil.decode(user, "ENCODE", ConstantValue.DES_PASSWORD);
                }
                String userId = user.getId() + "";
                NewsforModel vo = new NewsforModel();
                vo.setUserid(userId);
                loadDataListFragment = ReportListForAllFragment.getFragment(vo, OpitionNewsEngineImple.NEWSALL);
            } else {
                loadDataListFragment = ReportListForAllFragment.getFragment(null, OpitionNewsEngineImple.NEWSALL);
            }
            getSupportFragmentManager().beginTransaction().replace(R.id.view_datalist, loadDataListFragment).commit();

        }
    }

    private final String mPageName = PortfolioApplication.getInstance().getString(R.string.count_option_market);

    @Override
    public void onPause() {
        // TODO Auto-generated method stub
        super.onPause();
        // SDK已经禁用了基于Activity 的页面统计，所以需要再次重新统计页面
        MobclickAgent.onPageEnd(mPageName);
        MobclickAgent.onPause(this);
    }

    @Override
    public void onResume() {
        // TODO Auto-generated method stub
        super.onResume();
        // SDK已经禁用了基于Activity 的页面统计，所以需要再次重新统计页面
        MobclickAgent.onPageStart(mPageName);
        MobclickAgent.onResume(this);
    }
}
