package com.dkhs.portfolio.ui.widget;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AbsListView;
import android.widget.ListAdapter;
import android.widget.ListView;

import com.dkhs.portfolio.R;

/**
 * @author zwm
 * @version 2.0
 * @ClassName MListView
 * @Description TODO(这里用一句话描述这个类的作用)
 * @date 2015/9/7.
 */
public class TopicsDetailListView extends ListView implements AbsListView.OnScrollListener {

    private int mLastMotionY;
    private int mStartMotionY;

    public boolean isBlock = false;

    private View mFootView;
    private int mLastItem;


    public TopicsDetailScrollView mTopicsDetailScrollView;

    public TopicsDetailListView(Context context) {
        super(context);
    }

    public TopicsDetailListView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }


    public TopicsDetailListView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public TopicsDetailListView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }


    private void init() {
        mFootView = LayoutInflater.from(getContext()).inflate(R.layout.pull_to_refresh_load_more, null);
        mFootView.setVisibility(INVISIBLE);
        addFooterView(mFootView);
        setOnScrollListener(this);
        setDivider(null);

    }

    @Override
    public void setOnScrollListener(OnScrollListener l) {
        super.setOnScrollListener(new  OnScrollListenerIMp(l));
    }

    class OnScrollListenerIMp implements OnScrollListener{
        OnScrollListener l;

        public OnScrollListenerIMp(OnScrollListener l) {
            this.l = l;
        }

        @Override
        public void onScrollStateChanged(AbsListView view, int scrollState) {

            if(l!= null){
                l.onScrollStateChanged(view,scrollState);
            }
        }

        @Override
        public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

            if(l!= null){
                l.onScroll(view, firstVisibleItem, visibleItemCount,totalItemCount);
            }
        }
    }




    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {

        switch (ev.getAction()) {
            case MotionEvent.ACTION_MOVE: {


                if (getAdapter() != null && this.getChildAt(0) != null) {
                    if (this.getChildAt(0).getTop() == 0 && mTopicsDetailScrollView.listViewIsBelow()) {
                        return false;
                    }
                }
                break;
            }
            case MotionEvent.ACTION_UP:
                MotionEvent event = MotionEvent.obtain(ev);
                event.setAction(MotionEvent.ACTION_CANCEL);
                return super.onInterceptTouchEvent(event);

        }

        return super.onInterceptTouchEvent(ev);
    }



    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {

    }


    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {


        if (getAdapter() != null && mOnLoadMoreListener != null) {
            mLastItem = firstVisibleItem + visibleItemCount - 1;
            if (getAdapter().getCount() == mLastItem) {
                mOnLoadMoreListener.loadMore();
            }
        }
    }

    @Override
    public void setAdapter(ListAdapter adapter) {
        super.setAdapter(adapter);
        init();
    }

    public interface OnLoadMoreListener {
        void loadMore();
    }

    private OnLoadMoreListener mOnLoadMoreListener;

    public void setOnLoadMoreListener(OnLoadMoreListener onLoadMoreListener) {
        mOnLoadMoreListener = onLoadMoreListener;
    }




}
