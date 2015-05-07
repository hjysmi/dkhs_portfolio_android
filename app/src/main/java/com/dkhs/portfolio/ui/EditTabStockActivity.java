package com.dkhs.portfolio.ui;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.LinearLayout;

import com.dkhs.portfolio.R;
import com.dkhs.portfolio.app.PortfolioApplication;
import com.dkhs.portfolio.bean.MoreDataBean;
import com.dkhs.portfolio.bean.SelectStockBean;
import com.dkhs.portfolio.engine.LoadMoreDataEngine;
import com.dkhs.portfolio.engine.LoadMoreDataEngine.ILoadDataBackListener;
import com.dkhs.portfolio.engine.OptionalStockEngineImpl;
import com.dkhs.portfolio.engine.VisitorDataEngine;
import com.dkhs.portfolio.net.ErrorBundle;
import com.dkhs.portfolio.net.ParseHttpListener;
import com.dkhs.portfolio.ui.draglist.DragListAdapter;
import com.dkhs.portfolio.ui.draglist.DragListView;
import com.dkhs.portfolio.ui.draglist.DragStockAdapter;
import com.dkhs.portfolio.utils.PromptManager;
import com.umeng.analytics.MobclickAgent;

public class EditTabStockActivity extends ModelAcitivity implements OnClickListener {
    private DragListView optionEditList;
    LoadMoreDataEngine mLoadDataEngine;
    private DragStockAdapter adapter;
    private Context context;
    private Button btnRight;
    private LinearLayout layout;

    public static Intent newIntent(Context context, List<SelectStockBean> dataList) {
        Intent intent = new Intent(context, EditTabStockActivity.class);
        // extras
        intent.putExtra(BaseSelectActivity.ARGUMENT_SELECT_LIST, (Serializable) dataList);

        return intent;
    }

    @Override
    protected void onCreate(Bundle arg0) {
        // TODO Auto-generated method stub
        super.onCreate(arg0);
        setContentView(R.layout.activity_option_edit);
        setTitle(R.string.title_edit_optional_stock);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            handleExtras(extras);
        }

        context = this;
        initView();
        mLoadDataEngine = new OptionalStockEngineImpl(mSelectStockBackListener, true);

    }

    private List<SelectStockBean> mStockList;

    private void handleExtras(Bundle extras) {

        mStockList = (ArrayList<SelectStockBean>) extras.getSerializable(BaseSelectActivity.ARGUMENT_SELECT_LIST);

    }

    private void initView() {
        optionEditList = (DragListView) findViewById(R.id.option_edit_list);
        layout = (LinearLayout) findViewById(R.id.layout);
        btnRight = getRightButton();
        btnRight.setOnClickListener(this);
        btnRight.setText(R.string.finish);
        layout.setOnClickListener(this);

        adapter = new DragStockAdapter(context, optionEditList);
        adapter.setAdapterData(mStockList);
        optionEditList.setAdapter(adapter);
        optionEditList.setOnItemClickListener(new OnListener());

    }

    ILoadDataBackListener mSelectStockBackListener = new ILoadDataBackListener() {

        @Override
        public void loadFinish(MoreDataBean object) {
            // adapter = new DragListAdapter(context, object.getResults(), optionEditList);
            // optionEditList.setAdapter(adapter);
            // optionEditList.setOnItemClickListener(new OnListener());
            // adapter.setAdapterData(object.getResults());
            updateRemindValue(object.getResults());
            adapter.notifyDataSetChanged();
        }

        @Override
        public void loadFail() {
            // TODO Auto-generated method stub

        }

    };

    private void updateRemindValue(List<SelectStockBean> newStockList) {
        for (SelectStockBean stockBean : newStockList) {
            if (mStockList.contains(stockBean)) {
                int position = mStockList.indexOf(stockBean);
                mStockList.get(position).alertSetBean = stockBean.alertSetBean;
            }
        }
    }

    class OnListener implements OnItemClickListener {

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            // TODO Auto-generated method stub
            optionEditList.reSet(position);
        }

    }

    @Override
    public void onClick(View v) {
        // TODO Auto-generated method stub
        switch (v.getId()) {
            case R.id.btn_right:
                if (null == adapter || adapter.getStockList().isEmpty()) {
                    finish();
                    return;
                }
                try {
                    // List<DragListItem> list = optionEditList.getList();
                    List<SelectStockBean> list = adapter.getStockList();
                    // JSONArray jsonArray = JSONArray.fromObject(list);
                    JSONArray json = new JSONArray();
                    for (int i = 0; i < list.size(); i++) {
                        SelectStockBean vo = list.get(i);
                        // SelectStockBean stock = ((SelectStockBean)vo);
                        JSONObject jo = new JSONObject();
                        vo.setSortId(i + 1);
                        jo.put("symbol_id", vo.getId());
                        jo.put("sort_index", i + 1);
                        json.put(jo);

                    }
                    Log.e("listindex", json.toString());
                    if (PortfolioApplication.hasUserLogin()) {

                        OptionalStockEngineImpl.setIndex(userInfoListener.setLoadingDialog(this, "保存中...", false),
                                json.toString());
                    } else {

                        // 未完成
                        new VisitorDataEngine().replaceOptionStock(list);
                        PromptManager.showEditSuccessToast();
                        finish();
                    }
                } catch (JSONException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                setResult(RESULT_OK);
                break;
            case R.id.layout:
                optionEditList.review(-1);
                break;
            default:
                break;
        }
    }

    ParseHttpListener userInfoListener = new ParseHttpListener<List<SelectStockBean>>() {

        @Override
        protected List<SelectStockBean> parseDateTask(String josn) {
            Log.e("json", josn);

            return null;
        }

        @Override
        protected void afterParseData(List<SelectStockBean> dataList) {

            PromptManager.showEditSuccessToast();
            finish();
            setResult(RESULT_OK);
        }
    };
    private final String mPageName = PortfolioApplication.getInstance().getString(R.string.count_option_edit);

    @Override
    public void onPause() {
        // TODO Auto-generated method stub
        super.onPause();
        // SDK已经禁用了基于Activity 的页面统计，所以需要再次重新统计页面
        MobclickAgent.onPageEnd(mPageName);
        MobclickAgent.onPause(this);
    }

    private boolean isFirstLoad = true;

    @Override
    public void onResume() {
        // TODO Auto-generated method stub
        super.onResume();
        // SDK已经禁用了基于Activity 的页面统计，所以需要再次重新统计页面
        MobclickAgent.onPageStart(mPageName);
        MobclickAgent.onResume(this);
        if (isFirstLoad) {
            isFirstLoad = false;
            mLoadDataEngine = new OptionalStockEngineImpl(mSelectStockBackListener, true);
        } else {
            if (mLoadDataEngine != null) {
                mLoadDataEngine.setLoadingDialog(this);
                mLoadDataEngine.loadData();
            }
        }
    }
}
