package com.dkhs.portfolio.ui;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.GridView;

import com.dkhs.portfolio.R;
import com.dkhs.portfolio.bean.MarketCenterGridItem;
import com.dkhs.portfolio.ui.adapter.MarketCenterGridAdapter;
import com.umeng.analytics.MobclickAgent;

/**
 * 行情中心
 * 
 * @author weiting
 * 
 */
public class MarketCenterActivity extends ModelAcitivity implements OnClickListener {
    // private LinearLayout layoutMarkerParent;
    // private Context context;

    private GridView mGridView;
    /**
     * 没有HeaderId的List
     */
    private List<MarketCenterGridItem> nonHeaderIdList = new ArrayList<MarketCenterGridItem>();

    @Override
    protected void onCreate(Bundle arg0) {
        // TODO Auto-generated method stub
        super.onCreate(arg0);
        setContentView(R.layout.activity_marketcenter);
        setTitle(R.string.marketcenter_title);

        mGridView = (GridView) findViewById(R.id.asset_grid);

        // 给GridView的item的数据生成HeaderId
        List<MarketCenterGridItem> hasHeaderIdList = generateHeaderId(nonHeaderIdList);
        // 排序
        mGridView.setAdapter(new MarketCenterGridAdapter(MarketCenterActivity.this, hasHeaderIdList, mGridView));
        // layoutMarkerParent = (LinearLayout) findViewById(R.id.layout_marker_parenst);
        // context = this;
        // Button btn = getRightButton();
        // btn.setCompoundDrawablesWithIntrinsicBounds(getResources().getDrawable(R.drawable.btn_search_select), null,
        // null, null);
        // // btn.setBackgroundResource(R.drawable.btn_search_select);
        // btn.setOnClickListener(this);
        // String[] name = new String[]{"沪深行情","国内指数"};
        // List<Fragment> frag = new ArrayList<Fragment>();
        // frag.add(FragmentMarkerCenter.initFrag(FragmentMarkerCenter.SHEN_HU));
        // frag.add(FragmentMarkerCenter.initFrag(FragmentMarkerCenter.INSIDE_COUNT));
        // new FragmentSelectAdapter(context, name, frag, layoutMarkerParent, getSupportFragmentManager());
    }

    @Override
    public void onClick(View v) {
        // TODO Auto-generated method stub
        switch (v.getId()) {
            case R.id.btn_right:
                Intent intent = new Intent(this, SelectAddOptionalActivity.class);
                startActivity(intent);
                break;
            default:
                break;
        }
    }

    private List<MarketCenterGridItem> generateHeaderId(List<MarketCenterGridItem> nonHeaderIdList) {
        // Map<String, Integer> mHeaderIdMap = new HashMap<String, Integer>();
        // int mHeaderId = 1;
        // List<MarketCenterGridItem> hasHeaderIdList;

        for (int i = 1; i < 30; i++) {
            MarketCenterGridItem mGridItem = new MarketCenterGridItem("dddd", i + "");
            // String ymd = mGridItem.getTime();
            // if (!mHeaderIdMap.containsKey(ymd)) {
            if (i % 3 == 0 || i % 4 == 0) {
                mGridItem.setHeaderId(2);

            } else {

                mGridItem.setHeaderId(1);
            }
            nonHeaderIdList.add(mGridItem);
            // mHeaderIdMap.put(ymd, mHeaderId);
            // mHeaderId++;
            // } else {
            // mGridItem.setHeaderId(mHeaderIdMap.get(ymd));
            // }
        }
        // hasHeaderIdList = nonHeaderIdList;

        return nonHeaderIdList;
    }

    @Override
    public void onPause() {
        // TODO Auto-generated method stub
        super.onPause();
        // SDK已经禁用了基于Activity 的页面统计，所以需要再次重新统计页面
        MobclickAgent.onPause(this);
    }

    @Override
    public void onResume() {
        // TODO Auto-generated method stub
        super.onResume();
        // SDK已经禁用了基于Activity 的页面统计，所以需要再次重新统计页面
        MobclickAgent.onResume(this);
    }
}
