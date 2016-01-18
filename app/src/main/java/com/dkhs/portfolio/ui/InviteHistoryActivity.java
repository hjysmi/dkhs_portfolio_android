package com.dkhs.portfolio.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.AdapterView;

import com.dkhs.adpter.adapter.DKBaseAdapter;
import com.dkhs.portfolio.R;
import com.dkhs.portfolio.bean.InvitationBean;
import com.dkhs.portfolio.engine.AdEngineImpl;
import com.dkhs.portfolio.net.SimpleParseHttpListener;
import com.dkhs.portfolio.ui.ItemView.InvitationsItemHandler;
import com.dkhs.widget.CircularProgress;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;

import java.util.ArrayList;
import java.util.List;

/**
 * @author zwm
 * @version 2.0
 * @ClassName WebActivity
 * @Description TODO(这里用一句话描述这个类的作用)
 * @date 2015-5-18 上午10:26:35
 */
public class InviteHistoryActivity extends ModelAcitivity {


    @ViewInject(R.id.tv_percentage)
    android.widget.TextView mTvpercentage;
    @ViewInject(R.id.view_stock_title)
    android.widget.LinearLayout mViewstocktitle;
    @ViewInject(R.id.loadView)
    CircularProgress mLoadView;
    @ViewInject(R.id.listView)
    android.widget.ListView mListView;
    @ViewInject(R.id.errIM)
    android.widget.ImageView mErrIM;
    @ViewInject(R.id.errTV)
    android.widget.TextView mErrTV;
    @ViewInject(R.id.rl_empty)
    android.widget.RelativeLayout mRlempty;
    private android.widget.BaseAdapter adapter;
    private java.util.List<InvitationBean> dataL = new ArrayList<>();

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_invite_history);
        ViewUtils.inject(this);
        setTitle(getString(R.string.invite_history));

        adapter = new DKBaseAdapter(this, dataL).buildSingleItemView(new InvitationsItemHandler());
        mListView.setAdapter(adapter);
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                InvitationBean invitationBean = dataL.get(position);
                InviteHistoryActivity.this.startActivity(UserHomePageActivity.getIntent(InviteHistoryActivity.this, invitationBean.getUsername(), invitationBean.getId() + ""));
            }
        });
        initData();

    }

    /**
     * iniView initData
     */
    public void initData() {

        AdEngineImpl.getInvitations(new SimpleParseHttpListener() {
            @Override
            public Class getClassType() {
                return InvitationBean.class;
            }

            @Override
            protected void afterParseData(Object object) {
                mLoadView.setVisibility(View.GONE);

                if (object != null) {
                    dataL.clear();

                    List<InvitationBean> sub = (List<InvitationBean>) object;
                    dataL.addAll(sub);
                    adapter.notifyDataSetChanged();

                    if (dataL.size() == 0) {
                        mRlempty.setVisibility(View.VISIBLE);
                    } else {
                        mRlempty.setVisibility(View.GONE);
                    }

                }

            }

            @Override
            public void onFailure(int errCode, String errMsg) {
                super.onFailure(errCode, errMsg);
                mLoadView.setVisibility(View.GONE);
                mRlempty.setVisibility(View.VISIBLE);
            }
        });


    }

    /**
     * getData from net
     */
    public void getDataForNet() {
    }
    @Override
    public int getPageStatisticsStringId() {
        return R.string.statistics_invitehistory;
    }

}
