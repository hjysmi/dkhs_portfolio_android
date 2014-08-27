/**
 * @Title MyCombinationActivity.java
 * @Package com.dkhs.portfolio.ui
 * @Description TODO(用一句话描述该文件做什么)
 * @author zjz
 * @date 2014-8-26 下午3:10:51
 * @version V1.0
 */
package com.dkhs.portfolio.ui;

import android.content.Context;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewTreeObserver;
import android.view.inputmethod.InputMethodManager;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;

import com.dkhs.portfolio.R;
import com.dkhs.portfolio.ui.adapter.CombinationAdapter;

/**
 * @ClassName MyCombinationActivity
 * @Description 我的组合
 * @author zjz
 * @date 2014-8-26 下午3:10:51
 * @version 1.0
 */
public class MyCombinationActivity extends ModelAcitivity implements OnItemClickListener, OnTouchListener {
    private GridView gvCombination;
    private CombinationAdapter mCombinationAdapter;

    @Override
    protected void onCreate(Bundle arg0) {
        // TODO Auto-generated method stub
        super.onCreate(arg0);
        setContentView(R.layout.activity_mycombination);
        setTitle(R.string.my_combination);
        initGridView();
        this.getWindow().getDecorView().setOnTouchListener(this);

    }

    /**
     * @Title
     * @Description TODO: (用一句话描述这个方法的功能)
     * @return void
     */
    private void initGridView() {
        gvCombination = (GridView) findViewById(R.id.gv_mycombination);

        mCombinationAdapter = new CombinationAdapter(this);
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

                mCombinationAdapter.setItemHeight((int) (columnWidth * 1.2));
            }
        });

        gvCombination.setOnTouchListener(this);

    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

    }

    /**
     * @Title
     * @Description TODO: (用一句话描述这个方法的功能)
     * @param v
     * @param event
     * @return
     * @return
     */
    @Override
    public boolean onTouch(View v, MotionEvent event) {
        gvCombination.clearFocus();
        InputMethodManager imm = (InputMethodManager) v.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm != null) {
            imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
        }
        // System.out.println("Touch xxx");
        return super.onTouchEvent(event);
    }
}
