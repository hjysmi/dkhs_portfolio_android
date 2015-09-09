package com.dkhs.portfolio.ui.widget;

import android.content.Context;
import android.support.v4.widget.NestedScrollView;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import com.dkhs.portfolio.R;
import com.lidroid.xutils.util.LogUtils;

/**
 * @author zwm
 * @version 2.0
 * @ClassName MScrollView
 * @Description TODO(这里用一句话描述这个类的作用)
 * @date 2015/9/7.
 */
public class MScrollView extends NestedScrollView {


    private MListView mMListView;
    public MScrollView(Context context) {
        super(context);
    }

    public MScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MScrollView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onFinishInflate() {
        View view=   LayoutInflater.from(getContext()).inflate(R.layout.scrollview_contain_listview,this,false);
        mMListView= (MListView) view.findViewById(R.id.lv);

        addView(view);

        super.onFinishInflate();
    }


    private boolean isFirst =true;
    @Override
    public void onWindowFocusChanged(boolean hasWindowFocus) {
        super.onWindowFocusChanged(hasWindowFocus);
        if(isFirst &&hasWindowFocus &&mMListView != null){
            isFirst =false;
            ViewGroup.LayoutParams layoutParams=   mMListView.getLayoutParams();
            layoutParams.height=getMeasuredHeight();

            mMListView.requestLayout();
        }

    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {


        LogUtils.e("onInterceptTouchEvent");
        if(mMListView.getTop()-getScrollY()<=0){
            this.setScrollY(mMListView.getTop());
            return  false;
        }

        return super.onInterceptTouchEvent(ev);
    }

    @Override
    public void onNestedScroll(View target, int dxConsumed, int dyConsumed, int dxUnconsumed, int dyUnconsumed) {

        super.onNestedScroll(target, dxConsumed, dyConsumed, dxUnconsumed, dyUnconsumed);
        this.scrollBy(dxConsumed, dyConsumed);
//        this.scrollBy(dxConsumed,dyConsumed);


//
    }
}
