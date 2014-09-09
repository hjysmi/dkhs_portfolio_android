/**
 * @Title MyCombinationActivity.java
 * @Package com.dkhs.portfolio.ui
 * @Description TODO(用一句话描述该文件做什么)
 * @author zjz
 * @date 2014-8-26 下午3:10:51
 * @version V1.0
 */
package com.dkhs.portfolio.ui;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
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
import android.widget.Button;
import android.widget.GridView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.dkhs.portfolio.R;
import com.dkhs.portfolio.bean.CombinationBean;
import com.dkhs.portfolio.ui.adapter.CombinationAdapter;

/**
 * @ClassName MyCombinationActivity
 * @Description 我的组合
 * @author zjz
 * @date 2014-8-26 下午3:10:51
 * @version 1.0
 */
public class MyCombinationActivity extends ModelAcitivity implements OnItemClickListener, OnTouchListener,
        OnClickListener {
    private GridView gvCombination;
    private CombinationAdapter mCombinationAdapter;
    private Button btnMore;
    private Button btnRefresh;
    private PopupWindow mPopMoreWindow;
    private List<CombinationBean> mDataList = new ArrayList<CombinationBean>();

    @Override
    protected void onCreate(Bundle arg0) {
        super.onCreate(arg0);
        setContentView(R.layout.activity_mycombination);
        setTitle(R.string.my_combination);

        initGridView();
        this.getWindow().getDecorView().setOnTouchListener(this);

        initTitleView();

        initData();

    }

    private void initTitleView() {
        btnMore = getRightButton();
        btnMore.setBackgroundResource(R.drawable.nav_more_selector);
        btnMore.setOnClickListener(this);

        btnRefresh = getSecondRightButton();
        btnRefresh.setOnClickListener(this);
        btnRefresh.setVisibility(View.VISIBLE);
        btnRefresh.setBackgroundResource(R.drawable.nav_refresh_selector);
        btnRefresh.setTag("refresh");

    }

    /**
     * @Title
     * @Description TODO: (用一句话描述这个方法的功能)
     * @return void
     */
    private void initGridView() {
        gvCombination = (GridView) findViewById(R.id.gv_mycombination);

        mCombinationAdapter = new CombinationAdapter(this, mDataList);
        gvCombination.setAdapter(mCombinationAdapter);
        gvCombination.setOnItemClickListener(this);
        gvCombination.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView absListView, int scrollState) {
                // if (scrollState == AbsListView.OnScrollListener.SCROLL_STATE_FLING) {
                // // mImageFetcher.setPauseWork(true);
                // } else {
                // mImageFetcher.setPauseWork(false);
                // }
            }

            @Override
            public void
                    onScroll(AbsListView absListView, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
            }
        });
        gvCombination.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                final int columnWidth = (gvCombination.getWidth() - (getResources()
                        .getDimensionPixelSize(R.dimen.combin_horSpacing))) / 2;

                mCombinationAdapter.setItemHeight((int) (columnWidth ));
            }
        });

        gvCombination.setOnTouchListener(this);

    }

    private void initData() {
        CombinationBean conBean1 = new CombinationBean("我的组合1", 1.152f, 11.22f);
        CombinationBean conBean2 = new CombinationBean("我的组合2", 1.153f, 15.22f);
        CombinationBean conBean3 = new CombinationBean("我的组合3", -1.152f, -11.22f);
        CombinationBean conBean4 = new CombinationBean("我的组合4", 1.152f, 13.22f);
        CombinationBean conBean5 = new CombinationBean("我的组合5", -1.154f, -10.22f);
        mDataList.add(conBean1);
        mDataList.add(conBean2);
        mDataList.add(conBean3);
        mDataList.add(conBean4);
        mDataList.add(conBean5);
        mCombinationAdapter.notifyDataSetChanged();
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        System.out.println("onItemClick click:" + position);

        Intent intent = new Intent(this, CombinationDetailActivity.class);
        // Intent intent = new Intent(this, PositionAdjustActivity.class);
        // intent.putExtra(PositionAdjustActivity.KEY_VIEW_TYPE, PositionAdjustActivity.VALUE_ADJUST_CONBINA);
        startActivity(intent);

    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        gvCombination.clearFocus();
        InputMethodManager imm = (InputMethodManager) v.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm != null) {
            imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
        }
        return super.onTouchEvent(event);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == RIGHTBUTTON_ID) {

            clickRightButton();
        } else if (id == SECONDRIGHTBUTTON_ID) {

            clickSecondButton();
        } else if (id == R.id.tv_add_combina) {
            mCombinationAdapter.addItem();
            mPopMoreWindow.dismiss();

        } else if (id == R.id.tv_delete_combina) {
            btnMore.setText("取消");
            btnMore.setTag("del");
            btnMore.setBackgroundDrawable(null);

            btnRefresh.setTag("del");
            btnRefresh.setBackgroundResource(R.drawable.btn_delete_selector);
            mCombinationAdapter.setDelStatus(true);
            mPopMoreWindow.dismiss();

        }

    }

    private void clickRightButton() {
        if (btnMore.getTag() != null && btnMore.getTag().equals("del")) {
            setButtonMore();
            mCombinationAdapter.getDelPosition().clear();
            mCombinationAdapter.setDelStatus(false);
            mCombinationAdapter.notifyDataSetChanged();
            setButtonRefresh();
        } else {
            showPopWindow();
        }
    }

    private void setButtonMore() {
        btnMore.setTag("more");
        btnMore.setText("");
        btnMore.setBackgroundResource(R.drawable.nav_more_selector);
    }

    private void setButtonRefresh() {
        btnRefresh.setTag("refresh");
        btnRefresh.setBackgroundResource(R.drawable.nav_refresh_selector);
    }

    private void clickSecondButton() {
        if (btnRefresh.getTag().equals("refresh")) {
            refreshData();
        } else {
            setButtonRefresh();
            setButtonMore();
            removeSelectDatas();
        }

    }

    private void refreshData() {
        System.out.println("Second refresh button click");
    }

    private void removeSelectDatas() {
        List<Integer> selectList = mCombinationAdapter.getDelPosition();
        List<CombinationBean> delList = new ArrayList<CombinationBean>();
        for (Integer index : selectList) {
            int i = index;
            delList.add(mDataList.get(i));
        }
        mDataList.removeAll(delList);
        selectList.clear();
        mCombinationAdapter.setDelStatus(false);
        mCombinationAdapter.notifyDataSetChanged();
    }

    private void showPopWindow() {
        View view;
        view = this.getLayoutInflater().inflate(R.layout.layout_more_myconbination, null);
        mPopMoreWindow = new PopupWindow(view, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        mPopMoreWindow.setOutsideTouchable(true); // 不能在没有焦点的时候使用
        mPopMoreWindow.setBackgroundDrawable(new BitmapDrawable());
        mPopMoreWindow.setFocusable(true);

        TextView tvAdd = (TextView) view.findViewById(R.id.tv_add_combina);
        TextView tvDel = (TextView) view.findViewById(R.id.tv_delete_combina);
        tvAdd.setOnClickListener(this);
        tvDel.setOnClickListener(this);

        int width = getApplication().getResources().getDisplayMetrics().widthPixels;
        view.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
        int viewWidth = view.getMeasuredWidth();

        mPopMoreWindow.showAsDropDown(findViewById(R.id.includeHead), width - viewWidth, 0);

    }

}
