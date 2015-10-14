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
public class RewardDetailListView extends ListView  {

    public boolean isBlock = false;
    public RewardDetailScrollView mRewardDetailScrollView;
    private int mLastMotionY;
    private int mStartMotionY;
    private View mFootView;
    private int mLastItem;
    private OnLoadMoreListener mOnLoadMoreListener;

    public RewardDetailListView(Context context) {
        super(context);
    }


    public RewardDetailListView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public RewardDetailListView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }


    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public RewardDetailListView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    private void init() {
        mFootView = LayoutInflater.from(getContext()).inflate(R.layout.pull_to_refresh_load_more, null);
        mFootView.findViewById(R.id.pull_to_refresh_progress).setVisibility(View.VISIBLE);
        mFootView.setVisibility(GONE);
        addFooterView(mFootView);
        setOnScrollListener(null);
        setDivider(null);

    }

    public void toggleFooter(boolean show){
        if(mFootView == null)
            return;
        if(show && mFootView.getVisibility() == View.GONE){
            mFootView.setVisibility(VISIBLE);
        }else if(!show && mFootView.getVisibility() == View.VISIBLE){
            mFootView.setVisibility(GONE);
        }

    }

    @Override
    public void setOnScrollListener(OnScrollListener l) {
        super.setOnScrollListener(new OnScrollListenerIMp(l));
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {

        switch (ev.getAction()) {
            case MotionEvent.ACTION_MOVE: {


                if (getAdapter() != null && this.getChildAt(0) != null) {
                    if (this.getChildAt(0).getTop() == 0 && mRewardDetailScrollView.listViewIsBelow()) {
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
    public void setAdapter(ListAdapter adapter) {
        init();
        super.setAdapter(adapter);
    }

    public void setOnLoadMoreListener(OnLoadMoreListener onLoadMoreListener) {
        mOnLoadMoreListener = onLoadMoreListener;
    }

    public interface OnLoadMoreListener {
        void loadMore();
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
            if (getAdapter() != null && mOnLoadMoreListener != null) {
                int lastItem = firstVisibleItem + visibleItemCount ;
                if (getAdapter().getCount() == lastItem && mLastItem != lastItem) {
                    mLastItem = lastItem;
                    mOnLoadMoreListener.loadMore();
                }
            }
        }
    }




}
