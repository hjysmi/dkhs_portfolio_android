/**
 * @Title EditTabFundActivity.java
 * @Package com.dkhs.portfolio.ui
 * @Description TODO(用一句话描述该文件做什么)
 * @author zjz
 * @date 2015-2-9 下午1:27:54
 * @version V1.0
 */
package com.dkhs.portfolio.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.dkhs.portfolio.R;
import com.dkhs.portfolio.app.PortfolioApplication;
import com.dkhs.portfolio.bean.CombinationBean;
import com.dkhs.portfolio.bean.MoreDataBean;
import com.dkhs.portfolio.engine.FollowComEngineImpl;
import com.dkhs.portfolio.engine.FollowComListEngineImpl;
import com.dkhs.portfolio.engine.LoadMoreDataEngine;
import com.dkhs.portfolio.engine.LoadMoreDataEngine.ILoadDataBackListener;
import com.dkhs.portfolio.engine.VisitorDataEngine;
import com.dkhs.portfolio.net.ParseHttpListener;
import com.dkhs.portfolio.ui.draglist.DragCombinationAdapter;
import com.dkhs.portfolio.ui.draglist.DragListAdapter;
import com.dkhs.portfolio.ui.draglist.DragListView;
import com.dkhs.portfolio.utils.PromptManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * @author zjz
 * @version 1.0
 * @ClassName EditTabFundActivity
 * @Description TODO(这里用一句话描述这个类的作用)
 * @date 2015-2-9 下午1:27:54
 */
public class EditTabCombinationActivity extends ModelAcitivity implements OnClickListener,DragListAdapter.IDelCallBack {

    private DragListView optionEditList;
    LoadMoreDataEngine mLoadDataEngine;
    private DragCombinationAdapter adapter;
    // private Context context;


    private List<CombinationBean> mdateList = new ArrayList<CombinationBean>();
    private FollowComListEngineImpl dataEngine;

    public static Intent getIntent(Context context) {
        Intent intent = new Intent(context, EditTabCombinationActivity.class);
        // intent.putExtra("key_fund_list", (Serializable) list);
        return intent;
    }

    @Override
    protected void onCreate(Bundle arg0) {
        super.onCreate(arg0);
        setTitle(R.string.title_edit_optional_combina);
        setContentView(R.layout.activity_edit_tabfund);
        Bundle extras = getIntent().getExtras();

        if (extras != null) {
            handleExtras(extras);
        }

        initView();
        dataEngine = new FollowComListEngineImpl(new ILoadDataBackListener<CombinationBean>() {

            @Override
            public void loadFinish(MoreDataBean<CombinationBean> object) {
                if (null != object.getResults()) {
                    // mdateList.clear();
                    // mdateList.addAll(object.getResults());
                    // // adapter = new DragFundListAdapter(EditTabFundActivity.this, mdateList);
                    // adapter = new DragCombinationAdapter(EditTabFundActivity.this, optionEditList);
                    // adapter.setAdapterData(mdateList);
                    // optionEditList.setAdapter(adapter);

                    updateRemindValue(object.getResults());
                    // adapter.setAdapterData(mdateList);
                    adapter.notifyDataSetChanged();
                }
            }

            @Override
            public void loadFail() {

            }
        }, "");

    }

    private void updateRemindValue(List<CombinationBean> newComList) {

        if (null != mdateList && !mdateList.isEmpty()) {
            for (CombinationBean conBean : newComList) {
                if (mdateList.contains(conBean)) {
                    int position = mdateList.indexOf(conBean);
                    mdateList.get(position).setAlertBean(conBean.getAlertBean());
                } else {
                    mdateList.add(conBean);
                }
            }
        } else {
            mdateList.addAll(newComList);
            adapter.setAdapterData(mdateList);
        }
    }

    /**
     * @return
     * @Title
     * @Description TODO: (用一句话描述这个方法的功能)
     */
    @Override
    protected void onResume() {
        // TODO Auto-generated method stub
        super.onResume();
        dataEngine.loadAllData();
    }
    @Override
    public int getPageStatisticsStringId() {
        return R.string.statistics_edit_tab_combination;
    }
    private void handleExtras(Bundle extras) {
        mdateList = (ArrayList<CombinationBean>) extras.getParcelable("key_fund_list");
    }

    private void initView() {
        optionEditList = (DragListView) findViewById(R.id.option_edit_list);
        LinearLayout layout = (LinearLayout) findViewById(R.id.layout);
        TextView btnRight = getRightButton();
        btnRight.setOnClickListener(this);
        btnRight.setText(R.string.finish);
        layout.setOnClickListener(this);

        // mdateList.clear();
        // mdateList.addAll(object.getResults());
        adapter = new DragCombinationAdapter(EditTabCombinationActivity.this, optionEditList);
        adapter.setDelCallBack(this);
        // adapter.setAdapterData(mdateList);
        optionEditList.setAdapter(adapter);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_right:
                if (null == optionEditList || adapter.getConList().isEmpty()) {
                    finish();
                    return;
                }
                sortIndexToserver();
                break;
            case R.id.layout:
                optionEditList.review(-1);
                break;
            default:
                break;
        }
    }

    private void sortIndexToserver() {
        try {
            List<CombinationBean> list = adapter.getConList();
            JSONArray json = new JSONArray();
            for (int i = 0; i < list.size(); i++) {
                CombinationBean vo = list.get(i);
                JSONObject jo = new JSONObject();
                vo.setSortId(list.size() - i);
                vo.setSortId(i + 1);
                jo.put("portfolio_id", vo.getId());
                jo.put("sort_index", i + 1);
                json.put(jo);
            }
            Log.e("listindex", json.toString());
            if (PortfolioApplication.hasUserLogin()) {

                new FollowComEngineImpl().sortCombinations(json.toString(),
                        userInfoListener.setLoadingDialog(this, "保存中...", false));
            } else {
                new VisitorDataEngine().replaceCombination(list);
                PromptManager.showEditSuccessToast();

                finish();

                setResult(RESULT_OK);
            }
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    ParseHttpListener userInfoListener = new ParseHttpListener<Object>() {

        @Override
        protected Object parseDateTask(String josn) {
            Log.e("json", josn);

            return null;
        }

        @Override
        protected void afterParseData(Object dataList) {
            PromptManager.showEditSuccessToast();
            finish();
            setResult(RESULT_OK);
        }
    };

    @Override
    public void removeLast() {
        finish();
    }
}
