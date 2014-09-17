package com.dkhs.portfolio.ui.fragment;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Parcelable;
import android.support.v4.app.Fragment;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;

import com.dkhs.portfolio.R;
import com.dkhs.portfolio.engine.SearchStockEngineImpl;
import com.dkhs.portfolio.ui.MyCombinationActivity;
import com.dkhs.portfolio.ui.PositionAdjustActivity;
import com.dkhs.portfolio.ui.widget.ITitleButtonListener;

public class MainFragment extends Fragment implements OnClickListener {

    private ITitleButtonListener mTitleClickListener;

    private ViewPager viewPager;
    private LinearLayout dotLayout;
    private List<ImageView> imageViews;
    private int[] imageResId;
    private int currentItem = 0;

    private View viewOptionalStock;
    private View viewMyCombination;
    private View viewStockRanking;
    private View viewPlateRanking;
    private View viewFundRanking;
    private View viewPortfolioRanking;

    private static final int MSG_CHANGE_PAGER = 172;
    private Timer mScollTimer;

    Handler mHandler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            if (currentItem >= viewPager.getChildCount()) {
                currentItem = 0;
            } else {
                currentItem++;
            }
            viewPager.setCurrentItem(currentItem, true);
        };
    };

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main, null);
        initView(view);

        return view;
    }

    private void initView(View view) {
        view.findViewById(R.id.btn_back).setOnClickListener(this);

        view.findViewById(R.id.btn_right).setOnClickListener(this);

        ((TextView) view.findViewById(R.id.tv_title)).setText(R.string.portfolio_text);

        // 初始化界面控件实例
        dotLayout = (LinearLayout) view.findViewById(R.id.login_register_linearlayout_dot);
        initDotAndPicture();

        viewPager = (ViewPager) view.findViewById(R.id.vp_billboard);
        viewPager.setAdapter(new MyAdapter());
        viewPager.setOnPageChangeListener(new MyPageChangeListener());

        viewPager.setCurrentItem(1);
        viewPager.setCurrentItem(0);

        viewOptionalStock = view.findViewById(R.id.btn_optional_stoack);
        viewMyCombination = view.findViewById(R.id.btn_mycombina);
        viewStockRanking = view.findViewById(R.id.btn_stock_ranking);
        viewPlateRanking = view.findViewById(R.id.btn_plate_ranking);
        viewFundRanking = view.findViewById(R.id.btn_fund_ranking);
        viewPortfolioRanking = view.findViewById(R.id.btn_portfolio_ranking);

        viewOptionalStock.setOnClickListener(this);
        viewMyCombination.setOnClickListener(this);
        viewStockRanking.setOnClickListener(this);
        viewPlateRanking.setOnClickListener(this);
        viewFundRanking.setOnClickListener(this);
        viewPortfolioRanking.setOnClickListener(this);

        setViewLayoutParams();

    }

    private void setViewLayoutParams() {
        int screenWidth = getResources().getDisplayMetrics().widthPixels;
        int viewPading = getResources().getDimensionPixelSize(R.dimen.main_page_padding);
        int viewWidth = (screenWidth - 3 * viewPading) / 2;
        int smallViewHeight = (viewWidth * 2 - viewPading) / 3;
        viewOptionalStock.getLayoutParams().width = viewWidth;
        viewOptionalStock.getLayoutParams().height = viewWidth;
        viewMyCombination.getLayoutParams().width = viewWidth;
        viewMyCombination.getLayoutParams().height = viewWidth;

        viewStockRanking.getLayoutParams().width = viewWidth;
        viewStockRanking.getLayoutParams().height = smallViewHeight;
        viewPlateRanking.getLayoutParams().width = viewWidth;
        viewPlateRanking.getLayoutParams().height = smallViewHeight;
        viewFundRanking.getLayoutParams().width = viewWidth;
        viewFundRanking.getLayoutParams().height = smallViewHeight;
        viewPortfolioRanking.getLayoutParams().height = (int) (smallViewHeight * 0.7);
    }

    @Override
    public void onResume() {

        super.onResume();
        if (mScollTimer == null) { // 保证只有一个 定时任务
            mScollTimer = new Timer(true);
            mScollTimer.schedule(new ScrollPageTask(), 5000, 5000);
        }
    }

    @Override
    public void onStop() {
        super.onStop();

        if (mScollTimer != null) {
            mScollTimer.cancel();
            mScollTimer = null;
        }
    }

    public class ScrollPageTask extends TimerTask {

        @Override
        public void run() {
            mHandler.sendEmptyMessage(MSG_CHANGE_PAGER);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_back: {
                if (null != mTitleClickListener) {
                    mTitleClickListener.leftButtonClick();
                }
            }
                break;
            case R.id.btn_right: {
                if (null != mTitleClickListener) {
                    mTitleClickListener.rightButtonClick();
                }
            }
                break;
            case R.id.btn_mycombina: {
                Intent intent = new Intent(getActivity(), MyCombinationActivity.class);
                startActivity(intent);
            }
                break;
            case R.id.btn_optional_stoack: {
               new SearchStockEngineImpl().searchStock(89+"");
            }
            break;
            default:
                break;
        }
    }

    public void setTitleClickListener(ITitleButtonListener listener) {
        this.mTitleClickListener = listener;
    }

    /**
     * 初始化要切换的图片和点
     */
    private void initDotAndPicture() {
        imageResId = new int[] { R.drawable.pic_one, R.drawable.pic_two, R.drawable.pic_three };

        imageViews = new ArrayList<ImageView>();
        int viewWidth = getActivity().getResources().getDisplayMetrics().widthPixels;

        int dotWidth = viewWidth / imageResId.length;
        System.out.println("dotwidth:" + dotWidth);
        // 初始化图片资源
        for (int i = 0; i < imageResId.length; i++) {
            ImageView imageView = new ImageView(getActivity());
            imageView.setImageResource(imageResId[i]);
            imageView.setScaleType(ScaleType.FIT_XY);
            imageViews.add(imageView);

            // 根据图片动态设置小圆点
            View dotView = View.inflate(getActivity(), R.layout.dot, null);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(dotWidth, 8);
            // params.weight = 1.0f;
            // dotView.setLayoutParams(params);
            dotLayout.addView(dotView, params);
        }
    }

    /**
     * 当ViewPager中页面的状态发生改变时调用
     * 
     * 
     */
    private class MyPageChangeListener implements OnPageChangeListener {
        private int oldPosition = 0;

        /**
         * 当页面被选中的时候调用这个方法 position: 页面tag标识
         */
        public void onPageSelected(int position) {
            dotLayout.getChildAt(oldPosition).setBackgroundResource(R.drawable.dot_normal);
            dotLayout.getChildAt(position).setBackgroundResource(R.drawable.dot_focused);
            oldPosition = position;
        }

        public void onPageScrollStateChanged(int arg0) {
        }

        public void onPageScrolled(int arg0, float arg1, int arg2) {
        }
    }

    private class MyAdapter extends PagerAdapter {

        @Override
        public int getCount() {
            return imageResId.length;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            ((ViewPager) container).addView(imageViews.get(position));
            return imageViews.get(position);
        }

        @Override
        public void destroyItem(View arg0, int arg1, Object arg2) {
            ((ViewPager) arg0).removeView((View) arg2);
        }

        @Override
        public boolean isViewFromObject(View arg0, Object arg1) {
            return arg0 == arg1;
        }

    }

}
