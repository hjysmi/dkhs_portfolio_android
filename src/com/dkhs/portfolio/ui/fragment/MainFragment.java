package com.dkhs.portfolio.ui.fragment;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.text.Html;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.text.style.RelativeSizeSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.dkhs.portfolio.R;
import com.dkhs.portfolio.ui.MyCombinationActivity;
import com.dkhs.portfolio.ui.OptionalStockListActivity;
import com.dkhs.portfolio.ui.adapter.MainFunctionAdapter;
import com.dkhs.portfolio.ui.widget.ITitleButtonListener;
import com.dkhs.portfolio.ui.widget.MarqueeText;
import com.dkhs.portfolio.utils.PromptManager;

public class MainFragment extends Fragment implements OnClickListener {

    private ITitleButtonListener mTitleClickListener;

    private MarqueeText tvBottomText;
    //
    private ViewPager viewPager;
    private LinearLayout dotLayout;
    private List<ImageView> imageViews;
    private int[] imageResId;
    private int currentItem = 0;

    private GridView gvFunction;

    // private View viewOptionalStock;
    // private View viewMyCombination;
    // private View viewStockRanking;
    // private View viewPlateRanking;
    // private View viewFundRanking;
    // private View viewPortfolioRanking;

    private static final int MSG_CHANGE_PAGER = 172;
    private Timer mScollTimer;

    Handler mHandler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            System.out.println("handleMessage currentItem:" + currentItem);
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

        // ((TextView) view.findViewById(R.id.tv_title)).setText(R.string.portfolio_text);

        tvBottomText = (MarqueeText) view.findViewById(R.id.tv_bottom_text);
        setMarqueeText();

        gvFunction = (GridView) view.findViewById(R.id.gv_function);
        // gvFunction.getLayoutParams().height = getResources().getDisplayMetrics().widthPixels / 3 * 2;
        gvFunction.setAdapter(new MainFunctionAdapter(getActivity()));
        gvFunction.setOnItemClickListener(functionClick);

        // 初始化界面控件实例
        dotLayout = (LinearLayout) view.findViewById(R.id.linearlayout_dot);
        initDotAndPicture();
        //
        viewPager = (ViewPager) view.findViewById(R.id.vp_billboard);
        List<Fragment> fList = new ArrayList<Fragment>();
        fList.add(ScrollTopFragment.getInstance(ScrollTopFragment.TYPE_WEEK));
        fList.add(ScrollTopFragment.getInstance(ScrollTopFragment.TYPE_MONTH));
        fList.add(ScrollTopFragment.getInstance(ScrollTopFragment.TYPE_SEASON));
        viewPager.setAdapter(new ScrollFragmentAdapter(getChildFragmentManager(), fList));
        viewPager.setOnPageChangeListener(new MyPageChangeListener());
        viewPager.setOffscreenPageLimit(3);

        viewPager.setCurrentItem(1);
        viewPager.setCurrentItem(0);

        // viewOptionalStock = view.findViewById(R.id.btn_optional_stoack);
        // viewMyCombination = view.findViewById(R.id.btn_mycombina);
        // viewStockRanking = view.findViewById(R.id.btn_stock_ranking);
        // viewPlateRanking = view.findViewById(R.id.btn_plate_ranking);
        // viewFundRanking = view.findViewById(R.id.btn_fund_ranking);
        // viewPortfolioRanking = view.findViewById(R.id.btn_portfolio_ranking);
        //
        // viewOptionalStock.setOnClickListener(this);
        // viewMyCombination.setOnClickListener(this);
        // viewStockRanking.setOnClickListener(this);
        // viewPlateRanking.setOnClickListener(this);
        // viewFundRanking.setOnClickListener(this);
        // viewPortfolioRanking.setOnClickListener(this);

        setViewLayoutParams();

    }

    OnItemClickListener functionClick = new OnItemClickListener() {

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            // PromptManager.showToastTest("click Postion:" + position);

            Intent intent = null;
            switch (position) {
                case 0: {
                    PromptManager.showToastTest("click Postion:牛人栏目");
                }
                    break;
                case 1: {
                    intent = new Intent(getActivity(), OptionalStockListActivity.class);
                }
                    break;
                case 2: {
                    PromptManager.showToastTest("click Postion:公告栏目");
                }
                    break;
                case 3: {
                    PromptManager.showToastTest("click Postion:研报栏目");

                }
                    break;
                case 4: {
                    PromptManager.showToastTest("click Postion:云会议栏目");

                }
                    break;
                case 5: {
                    PromptManager.showToastTest("click Postion:海外资讯");

                }
                    break;
                default:
                    break;
            }
            if (null != intent) {
                getActivity().startActivity(intent);
            }

        }
    };

    private void setMarqueeText() {

        String szTilte = "上证指数";
        String szCurrentValue = "2356.90";
        String szIncrease = "+11.52";
        String szPercentage = "+1.72%";
        String scTilte = "深成指数";
        String scCurrentValue = "8112.39";
        String scIncrease = "-88.88";
        String scPercentage = "-1.28%";

        SpannableStringBuilder sp = new SpannableStringBuilder();
        sp.append(szTilte);
        sp.append(" ");
        sp.append(szCurrentValue);
        sp.append(" ");
        sp.append(szIncrease);
        sp.append(" ");
        sp.append(szPercentage);
        sp.append(" ");
        sp.append(scTilte);
        sp.append(" ");
        sp.append(scCurrentValue);
        sp.append(" ");
        sp.append(scIncrease);
        sp.append(" ");
        sp.append(scPercentage);
        sp.append(" ");

        ForegroundColorSpan whiteSpan = new ForegroundColorSpan(Color.WHITE);
        ForegroundColorSpan redSpan = new ForegroundColorSpan(Color.RED);
        int startTextIndex = szTilte.length();
        sp.setSpan(whiteSpan, 0, startTextIndex, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        sp.setSpan(new RelativeSizeSpan(0.6f), 0, startTextIndex, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        startTextIndex += 1;
        // sp.setSpan(redSpan, startTextIndex, startTextIndex + szCurrentValue.length(),
        // Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        startTextIndex += szCurrentValue.length() + 1;
        sp.setSpan(new RelativeSizeSpan(0.6f), startTextIndex, startTextIndex + szIncrease.length(),
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        // sp.setSpan(redSpan, startTextIndex, startTextIndex + szIncrease.length(),
        // Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

        startTextIndex += 1 + szIncrease.length();
        sp.setSpan(new RelativeSizeSpan(0.6f), startTextIndex, startTextIndex + szPercentage.length(),
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        sp.setSpan(redSpan, szTilte.length(), startTextIndex + szPercentage.length(),
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        startTextIndex += szPercentage.length() + 1;

        sp.setSpan(new ForegroundColorSpan(Color.WHITE), startTextIndex, startTextIndex + scTilte.length(),
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        sp.setSpan(new RelativeSizeSpan(0.6f), startTextIndex, startTextIndex + scTilte.length(),
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

        startTextIndex += scTilte.length() + 1;

        sp.setSpan(new ForegroundColorSpan(Color.GREEN), startTextIndex, startTextIndex + scCurrentValue.length() + 1
                + scIncrease.length() + 1 + scPercentage.length() + 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

        sp.setSpan(new RelativeSizeSpan(0.6f), startTextIndex + 1 + scCurrentValue.length(), startTextIndex
                + scCurrentValue.length() + 1 + scIncrease.length() + 1 + scPercentage.length() + 1,
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        tvBottomText.setText(sp);

        // tvBottomText.startScroll();

        // tvBottomText.setText(Html.fromHtml(htmlText));
    }

    private void setViewLayoutParams() {
        int screenWidth = getResources().getDisplayMetrics().widthPixels;
        viewPager.getLayoutParams().width = screenWidth / 2;
        int viewPading = getResources().getDimensionPixelSize(R.dimen.main_page_padding);
        int viewWidth = (screenWidth - 3 * viewPading) / 2;
        int smallViewHeight = (viewWidth * 2 - viewPading) / 3;
        // viewOptionalStock.getLayoutParams().width = viewWidth;
        // viewOptionalStock.getLayoutParams().height = viewWidth;
        // viewMyCombination.getLayoutParams().width = viewWidth;
        // viewMyCombination.getLayoutParams().height = viewWidth;
        //
        // viewStockRanking.getLayoutParams().width = viewWidth;
        // viewStockRanking.getLayoutParams().height = smallViewHeight;
        // viewPlateRanking.getLayoutParams().width = viewWidth;
        // viewPlateRanking.getLayoutParams().height = smallViewHeight;
        // viewFundRanking.getLayoutParams().width = viewWidth;
        // viewFundRanking.getLayoutParams().height = smallViewHeight;
        // viewPortfolioRanking.getLayoutParams().height = (int) (smallViewHeight * 0.7);
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
        Intent intent = null;
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
            // case R.id.btn_mycombina: {
            // intent = new Intent(getActivity(), MyCombinationActivity.class);
            // }
            // break;
            // case R.id.btn_optional_stoack: {
            // intent = new Intent(getActivity(), OptionalStockListActivity.class);
            //
            // }
            // break;
            default:
                break;
        }
        if (null != intent) {
            getActivity().startActivity(intent);
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
        // int viewWidth = getActivity().getResources().getDisplayMetrics().widthPixels;

        // int dotWidth = viewWidth / imageResId.length;
        // 初始化图片资源

        // 根据图片动态设置小圆点
        LayoutInflater inflater = getActivity().getLayoutInflater();
        // 初始化图片资源
        for (int i = 0; i < imageResId.length; i++) {
            ImageView imageView = new ImageView(getActivity());
            imageView.setImageResource(imageResId[i]);
            imageView.setScaleType(ScaleType.FIT_XY);
            imageViews.add(imageView);

            // 根据图片动态设置小圆点
            inflater.inflate(R.layout.dot, dotLayout);
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
            System.out.println("opageSelecte change :" + position);
            dotLayout.getChildAt(oldPosition).setBackgroundResource(R.drawable.dot_normal);
            dotLayout.getChildAt(position).setBackgroundResource(R.drawable.dot_focused);
            oldPosition = position;
        }

        public void onPageScrollStateChanged(int arg0) {
        }

        public void onPageScrolled(int arg0, float arg1, int arg2) {
        }
    }

    private class ScrollFragmentAdapter extends FragmentStatePagerAdapter {

        private List<Fragment> fragmentList;

        public ScrollFragmentAdapter(FragmentManager fm, List<Fragment> fragmentList2) {
            super(fm);
            this.fragmentList = fragmentList2;

        }

        @Override
        public Fragment getItem(int arg0) {

            return (fragmentList == null || fragmentList.size() == 0) ? null : fragmentList.get(arg0);
        }

        @Override
        public int getCount() {
            return fragmentList == null ? 0 : fragmentList.size();
        }

    }

}
