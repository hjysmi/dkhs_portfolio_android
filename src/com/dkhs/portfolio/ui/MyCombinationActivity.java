/**
 * @Title MyCombinationActivity.java
 * @Package com.dkhs.portfolio.ui
 * @Description TODO(用一句话描述该文件做什么)
 * @author zjz
 * @date 2014-8-26 下午3:10:51
 * @version V1.0
 */
package com.dkhs.portfolio.ui;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.view.ContextThemeWrapper;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.inputmethod.InputMethodManager;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.Button;
import android.widget.GridView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.dkhs.portfolio.R;
import com.dkhs.portfolio.app.PortfolioApplication;
import com.dkhs.portfolio.bean.CombinationBean;
import com.dkhs.portfolio.bean.MoreDataBean;
import com.dkhs.portfolio.engine.MyCombinationEngineImpl;
import com.dkhs.portfolio.net.BasicHttpListener;
import com.dkhs.portfolio.net.DataParse;
import com.dkhs.portfolio.net.ParseHttpListener;
import com.dkhs.portfolio.ui.adapter.CombinationAdapter;
import com.dkhs.portfolio.ui.adapter.CombinationAdapter.IDelButtonListener;
import com.dkhs.portfolio.ui.fragment.MyCombinationSlideListFragment;
import com.dkhs.portfolio.ui.fragment.UserCombinationListFragment;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.lidroid.xutils.util.LogUtils;
import com.umeng.analytics.MobclickAgent;

/**
 * @ClassName MyCombinationActivity
 * @Description 我的组合
 * @author zjz
 * @date 2014-8-26 下午3:10:51
 * @version 1.0
 */
public class MyCombinationActivity extends ModelAcitivity implements OnClickListener {
    private Button btnMore;
    private Button btnRefresh;
    private MyCombinationSlideListFragment listFragment;

    @Override
    protected void onCreate(Bundle arg0) {
        super.onCreate(arg0);
        setContentView(R.layout.activity_mycombination);
        setTitle(R.string.my_combination);

        initTitleView();
        replaceCombinationListView();
    }

    @Override
    protected void onStart() {
        super.onStart();
        // loadCombinationData();
    }

    private void initTitleView() {
        btnMore = getRightButton();
        btnMore.setCompoundDrawablesWithIntrinsicBounds(getResources().getDrawable(R.drawable.ic_title_add), null,
                null, null);
        btnMore.setOnClickListener(this);

        btnRefresh = getSecondRightButton();
        btnRefresh.setOnClickListener(this);
        btnRefresh.setVisibility(View.VISIBLE);
        btnRefresh.setCompoundDrawablesWithIntrinsicBounds(getResources().getDrawable(R.drawable.nav_refresh_selector),
                null, null, null);

    }

    private void replaceCombinationListView() {
        listFragment = MyCombinationSlideListFragment.getFragment();
        getSupportFragmentManager().beginTransaction().replace(R.id.content_layout, listFragment).commit();

    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == RIGHTBUTTON_ID) {
            // listFragment.setListDelStatus(false);
            // btnMore.setVisibility(View.GONE);
            // btnTwo.setVisibility(View.VISIBLE);
            addNewCombination();
            // clickRightButton();
        } else if (id == SECONDRIGHTBUTTON_ID) {
            clickSecondButton();
            // clickSecondButton();
        } else if (id == R.id.tv_add_combina) {
            // mCombinationAdapter.addItem();
            // mPopMoreWindow.dismiss();

            addNewCombination();
        }

    }

    // private void clickRightButton() {
    // if (btnMore.getTag() != null && btnMore.getTag().equals("cancel")) {
    // // mCombinationAdapter.setDelStatus(false);
    // listFragment.setListDelStatus(false);
    // setButtonAdd();
    // } else {
    // addNewCombination();
    // }
    //
    // }

    private void addNewCombination() {
        // startActivity(PositionAdjustActivity.newIntent(this, null));
        if (null != listFragment)
            listFragment.createNewCombination();

    }

    // private void setButtonAdd() {
    // btnMore.setTag("add");
    // btnMore.setText("");
    // btnMore.setBackgroundResource(R.drawable.ic_title_add);
    // // btnRefresh.setVisibility(View.VISIBLE);
    // }

    // public void setButtonCancel() {
    // // btnMore.setText(R.string.cancel);
    // // btnMore.setTag("cancel");
    // // btnMore.setBackgroundDrawable(null);
    // // btnMore.setVisibility(View.VISIBLE);
    // // btnTwo.setVisibility(View.GONE);
    // // btnMore.setText("取消");
    // // btnRefresh.setVisibility(View.GONE);
    // }

    // public void setButtonFinish() {
    // btnMore.setText("完成");
    // }

    // private void setButtonRefresh() {
    // // btnRefresh.setTag("refresh");
    // // btnRefresh.setBackgroundResource(R.drawable.nav_refresh_selector);
    // }

    private void clickSecondButton() {
        listFragment.refresh();

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
