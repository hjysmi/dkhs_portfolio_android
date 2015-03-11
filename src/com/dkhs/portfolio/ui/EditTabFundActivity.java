/**
 * @Title EditTabFundActivity.java
 * @Package com.dkhs.portfolio.ui
 * @Description TODO(用一句话描述该文件做什么)
 * @author zjz
 * @date 2015-2-9 下午1:27:54
 * @version V1.0
 */
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
import android.widget.Button;
import android.widget.LinearLayout;

import com.dkhs.portfolio.R;
import com.dkhs.portfolio.app.PortfolioApplication;
import com.dkhs.portfolio.bean.CombinationBean;
import com.dkhs.portfolio.bean.SelectStockBean;
import com.dkhs.portfolio.engine.FollowComEngineImpl;
import com.dkhs.portfolio.engine.LoadSelectDataEngine;
import com.dkhs.portfolio.engine.OptionalStockEngineImpl;
import com.dkhs.portfolio.engine.VisitorDataEngine;
import com.dkhs.portfolio.net.ParseHttpListener;
import com.dkhs.portfolio.ui.draglist.DragFundListAdapter;
import com.dkhs.portfolio.ui.draglist.DragFundListView;
import com.dkhs.portfolio.ui.draglist.DragListView;
import com.dkhs.portfolio.utils.PromptManager;

/**
 * @ClassName EditTabFundActivity
 * @Description TODO(这里用一句话描述这个类的作用)
 * @author zjz
 * @date 2015-2-9 下午1:27:54
 * @version 1.0
 */
public class EditTabFundActivity extends ModelAcitivity implements OnClickListener {

    private DragFundListView optionEditList;
    LoadSelectDataEngine mLoadDataEngine;
    private DragFundListAdapter adapter;
    // private Context context;
    private Button btnRight;
    private LinearLayout layout;

    private List<CombinationBean> mdateList;

    public static Intent getIntent(Context context, List<CombinationBean> list) {
        Intent intent = new Intent(context, EditTabFundActivity.class);
        intent.putExtra("key_fund_list", (Serializable) list);
        return intent;
    }

    @Override
    protected void onCreate(Bundle arg0) {
        super.onCreate(arg0);
        setTitle(R.string.title_edit_optional_fund);
        setContentView(R.layout.activity_edit_tabfund);
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            handleExtras(extras);
        }

        initView();
    }

    private void handleExtras(Bundle extras) {
        mdateList = (ArrayList<CombinationBean>) extras.getSerializable("key_fund_list");
    }

    private void initView() {
        optionEditList = (DragFundListView) findViewById(R.id.option_edit_list);
        layout = (LinearLayout) findViewById(R.id.layout);
        btnRight = getRightButton();
        btnRight.setOnClickListener(this);
        btnRight.setText(R.string.finish);
        layout.setOnClickListener(this);

        adapter = new DragFundListAdapter(this, mdateList);
        optionEditList.setAdapter(adapter);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_right:
                if (null == optionEditList || optionEditList.getList().isEmpty()) {
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
            List<CombinationBean> list = optionEditList.getList();
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
                PromptManager.showToast("修改成功");
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
            PromptManager.showToast("修改成功");
            finish();
            setResult(RESULT_OK);
        }
    };

}
