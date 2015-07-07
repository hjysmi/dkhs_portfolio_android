package com.dkhs.portfolio.ui;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.dkhs.portfolio.R;
import com.dkhs.portfolio.app.PortfolioApplication;
import com.dkhs.portfolio.bean.OptionNewsBean;
import com.dkhs.portfolio.bean.UserEntity;
import com.dkhs.portfolio.engine.LoadNewsDataEngine;
import com.dkhs.portfolio.engine.NewsforModel;
import com.dkhs.portfolio.engine.OpitionNewsEngineImple;
import com.dkhs.portfolio.engine.UserEngineImpl;
import com.dkhs.portfolio.ui.adapter.OptionMarketAdapter;
import com.dkhs.portfolio.ui.fragment.ReportListForAllFragment;
import com.dkhs.portfolio.ui.widget.PullToRefreshListView;
import com.umeng.analytics.MobclickAgent;

import java.util.List;

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


}
