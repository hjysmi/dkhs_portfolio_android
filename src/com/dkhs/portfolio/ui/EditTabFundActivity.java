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

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;

import com.dkhs.portfolio.R;
import com.dkhs.portfolio.bean.CombinationBean;
import com.dkhs.portfolio.engine.LoadSelectDataEngine;
import com.dkhs.portfolio.ui.draglist.DragFundListAdapter;
import com.dkhs.portfolio.ui.draglist.DragFundListView;
import com.dkhs.portfolio.ui.draglist.DragListView;

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
        setTitle("编辑自选基金");
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
        btnRight.setText("完成");
        layout.setOnClickListener(this);

        adapter = new DragFundListAdapter(this, mdateList);
        optionEditList.setAdapter(adapter);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_right:
                break;
            case R.id.layout:
                optionEditList.review(-1);
                break;
            default:
                break;
        }
    }

}
