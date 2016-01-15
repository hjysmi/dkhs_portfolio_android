package com.dkhs.portfolio.ui;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

import com.dkhs.portfolio.R;
import com.dkhs.portfolio.app.PortfolioApplication;
import com.dkhs.portfolio.bean.HistoryPositionDetail;
import com.dkhs.portfolio.bean.HistoryPositionDetail.HistoryPositionBean;
import com.dkhs.portfolio.bean.HistoryPositionDetail.HistoryPositionItem;
import com.dkhs.portfolio.engine.NetValueEngine;
import com.dkhs.portfolio.net.DataParse;
import com.dkhs.portfolio.net.ParseHttpListener;
import com.dkhs.portfolio.ui.widget.PullToRefreshExpanelListView;
import com.dkhs.portfolio.ui.widget.PullToRefreshExpanelListView.OnLoadMoreListener;
import com.dkhs.portfolio.utils.NetUtil;
import com.dkhs.portfolio.utils.PromptManager;
import com.dkhs.portfolio.utils.StringFromatUtils;
import com.dkhs.portfolio.utils.TimeUtils;
import com.lidroid.xutils.http.HttpHandler;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HistoryPositionDetailActivity extends ModelAcitivity implements OnLoadMoreListener {
    public static final String EXTRA_COMBINATION = "extra_combination";
    // private CombinationBean mCombinationBean;
    private String mCombinationId;
    private PullToRefreshExpanelListView mListView;
    private NetValueEngine netValueEngine;

    @Override
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.activity_history_positiondetail);
        setTitle(R.string.privacy_history_record);
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            handleExtras(extras);
        }
        initView();
        initData();
    }

    private int count = 10;
    private int page = 1;
    private int total_page = 0;

    private List<HistoryPositionBean> lists = new ArrayList<HistoryPositionBean>();
    private List<String> keys = new ArrayList<String>();
    private Map<String, List<HistoryPositionItem>> map = new HashMap<String, List<HistoryPositionItem>>();
    private MyAdapter adapter;
    private MyIhttpListener listener;
    private HttpHandler mHttphandler;

    private void initData() {
        adapter = new MyAdapter();
        mListView.setAdapter(adapter);
        mListView.setGroupIndicator(null);
        mListView.setChildDivider(new ColorDrawable(Color.TRANSPARENT));
        netValueEngine = new NetValueEngine(mCombinationId);
        listener = new MyIhttpListener();
        // add by zcm --- 2014.12.17
        if (NetUtil.checkNetWork()) {
            // PromptManager.showProgressDialog(this, "");
            listener.setLoadingDialog(this);
            mHttphandler = netValueEngine.requeryHistoryDetailPosition(count, page, listener);
        } else {
            PromptManager.showToast(R.string.no_net_connect);
        }
        // add by zcm --- 2014.12.17
    }

    private class MyIhttpListener extends ParseHttpListener<HistoryPositionDetail> {

        @Override
        protected HistoryPositionDetail parseDateTask(String jsonData) {
            HistoryPositionDetail historyValue = DataParse.parseObjectJson(HistoryPositionDetail.class, jsonData);
            total_page = historyValue.getTotal_page();
            return historyValue;
        }

        @Override
        protected void afterParseData(HistoryPositionDetail object) {
            if (object != null) {
                if (page <= total_page) {
                    List<HistoryPositionBean> results = object.getResults();
                    for (int i = 0; i < results.size(); i++) {
                        // TODO
                        HistoryPositionBean positionBean = results.get(i);
                        String dateString = TimeUtils.getDateString(positionBean.getCreated_at());
                        positionBean.setDateString(dateString);
                        List<HistoryPositionItem> itemLists;
                        if (keys.contains(dateString)) {
                            itemLists = map.get(dateString);

                        } else {
                            itemLists = new ArrayList<HistoryPositionItem>();
                            keys.add(dateString);
                            map.put(dateString, itemLists);
                        }
                        List<HistoryPositionItem> changeLists = positionBean.getDetailed_change();
                        HistoryPositionItem historyPositionItem = changeLists.get(0);
                        StringBuilder sb = new StringBuilder();
                        for (int j = 0; j < changeLists.size(); j++) {
                            HistoryPositionItem item = changeLists.get(j);
                            sb.append(item.getSymbol_name());
                            try {
                                byte[] bytes = item.getSymbol_name().getBytes("UTF-8");
                                switch (bytes.length) {
                                    case 6:
                                        sb.append("        ");
                                        break;
                                    case 7:
                                    case 8:
                                    case 9:
                                        sb.append("     ");
                                        break;
                                    case 10:
                                    case 11:
                                        sb.append("  ");
                                        break;

                                    default:
                                        break;
                                }
                            } catch (UnsupportedEncodingException e) {
                                // TODO Auto-generated catch block
                                e.printStackTrace();
                            }
                            if (item.getSymbol_name().contains("*")) {
                                sb.append("\t从");
                            } else {
                                sb.append("\t\t从");
                            }
                            sb.append(StringFromatUtils.get2PointPercent(item.getFrom_percent()));
                            // sb.append("%");
                            sb.append("调至");
                            sb.append(StringFromatUtils.get2PointPercent(item.getTo_percent()));
                            if (j != changeLists.size() - 1) {
                                sb.append("\n");
                            }
                        }
                        historyPositionItem.setHourTime(TimeUtils.getHourString(historyPositionItem.getCreated_at()));
                        historyPositionItem.setChangeStr(sb.toString());
                        itemLists.add(historyPositionItem);
                    }
                    lists.addAll(results);
                    adapter.notifyDataSetChanged();
                    for (int i = 0; i < adapter.getGroupCount(); i++) {
                        mListView.expandGroup(i);
                    }
                    page++;
                    if (page <= total_page) {
                        mListView.setCanLoadMore(true);
                        mListView.setAutoLoadMore(true);
                        if (page == 2)
                            mListView.setOnLoadListener(HistoryPositionDetailActivity.this);
                    } else {
                        mListView.setCanLoadMore(false);
                        mListView.setAutoLoadMore(false);
                    }
                    // PromptManager.closeProgressDialog();
                    mListView.onLoadMoreComplete();
                } else {
                    mListView.setCanLoadMore(false);
                }
            }
        }

    }

    private void initView() {
        mListView = (PullToRefreshExpanelListView) findViewById(R.id.mListView);
    }

    private void handleExtras(Bundle extras) {
        mCombinationId = extras.getString(EXTRA_COMBINATION);

    }

    public static Intent newIntent(Context context, String combinationId) {
        Intent intent = new Intent(context, HistoryPositionDetailActivity.class);

        intent.putExtra(EXTRA_COMBINATION, combinationId);

        return intent;
    }

    private class MyAdapter extends BaseExpandableListAdapter {

        @Override
        public int getGroupCount() {
            return keys.size();
        }

        @Override
        public int getChildrenCount(int groupPosition) {
            return map.get(keys.get(groupPosition)).size();
        }

        @Override
        public Object getGroup(int groupPosition) {
            return keys.get(groupPosition);
        }

        @Override
        public Object getChild(int groupPosition, int childPosition) {
            return map.get(keys.get(groupPosition)).get(childPosition);
        }

        @Override
        public long getGroupId(int groupPosition) {
            return groupPosition;
        }

        @Override
        public long getChildId(int groupPosition, int childPosition) {
            return 0;
        }

        @Override
        public boolean hasStableIds() {
            return false;
        }

        @Override
        public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
            GroupViewHolder holder;
            if (convertView == null) {
                convertView = View.inflate(getApplicationContext(), R.layout.item_group_history_position, null);
                holder = new GroupViewHolder();
                holder.tv_date = (TextView) convertView.findViewById(R.id.tv_date);
                convertView.setTag(holder);
            } else {
                holder = (GroupViewHolder) convertView.getTag();
            }
            holder.tv_date.setText(keys.get(groupPosition));
            return convertView;
        }

        public class GroupViewHolder {
            TextView tv_date;
        }

        @Override
        public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView,
                                 ViewGroup parent) {
            ChildViewHolder holder;
            if (convertView == null) {
                convertView = View.inflate(getApplicationContext(), R.layout.item_child_history_position, null);
                holder = new ChildViewHolder();
                holder.tv_adjust_time = (TextView) convertView.findViewById(R.id.tv_adjust_time);
                holder.tv_adjust_history = (TextView) convertView.findViewById(R.id.tv_adjust_history);
                holder.blank_view1 = convertView.findViewById(R.id.blank_view1);
                holder.blank_view2 = convertView.findViewById(R.id.blank_view2);
                convertView.setTag(holder);
            } else {
                holder = (ChildViewHolder) convertView.getTag();
            }
            HistoryPositionItem positionItem = map.get(keys.get(groupPosition)).get(childPosition);
            holder.tv_adjust_time.setText(positionItem.getHourTime());
            holder.tv_adjust_history.setText(positionItem.getChangeStr());
            if (childPosition == map.get(keys.get(groupPosition)).size() - 1) {
                holder.blank_view1.setVisibility(View.GONE);
                holder.blank_view2.setVisibility(View.VISIBLE);
            }
            return convertView;
        }

        public class ChildViewHolder {
            TextView tv_adjust_time;
            TextView tv_adjust_history;
            View blank_view1;
            View blank_view2;
        }

        @Override
        public boolean isChildSelectable(int groupPosition, int childPosition) {
            return false;
        }

    }

    @Override
    public void onLoadMore() {
        if (page > total_page) {
            mListView.setAutoLoadMore(false);
            // modify by zcm --- 2012.12.16
            // mListView.setLodaMoreText("当前没有更多了...");
            PromptManager.showToast("当前没有更多了...");
        } else {
            netValueEngine.requeryHistoryDetailPosition(count, page, listener);
            // modify by zcm --- 2012.12.16
        }
    }

    private final String mPageName = PortfolioApplication.getInstance().getString(
            R.string.count_history_positiondetainl);



    @Override
    protected void onDestroy() {
        // TODO Auto-generated method stub
        super.onDestroy();
        if (null != mHttphandler) {
            mHttphandler.cancel();
        }
    }
    @Override
    public int getPageStatisticsStringId() {
        return R.string.statistics_history_position_detail;
    }
}
