package com.dkhs.portfolio.ui.fragment;

import java.lang.reflect.Type;
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
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.text.style.RelativeSizeSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewStub;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;

import com.dkhs.portfolio.R;
import com.dkhs.portfolio.bean.CombinationBean;
import com.dkhs.portfolio.bean.StockQuotesBean;
import com.dkhs.portfolio.engine.MainpageEngineImpl;
import com.dkhs.portfolio.engine.MyCombinationEngineImpl;
import com.dkhs.portfolio.net.DataParse;
import com.dkhs.portfolio.net.ParseHttpListener;
import com.dkhs.portfolio.ui.CombinationDetailActivity;
import com.dkhs.portfolio.ui.MarketCenterActivity;
import com.dkhs.portfolio.ui.MyCombinationActivity;
import com.dkhs.portfolio.ui.OptionalStockListActivity;
import com.dkhs.portfolio.ui.PositionAdjustActivity;
import com.dkhs.portfolio.ui.adapter.MainCombinationoAdapter;
import com.dkhs.portfolio.ui.adapter.MainFunctionAdapter;
import com.dkhs.portfolio.ui.widget.ITitleButtonListener;
import com.dkhs.portfolio.ui.widget.MarqueeText;
import com.dkhs.portfolio.utils.PromptManager;
import com.dkhs.portfolio.utils.StringFromatUtils;
import com.google.gson.reflect.TypeToken;

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
    private GridView gvCombination;

    private View comtentView;
    // private View viewOnecombination;
    // private View viewTwocombination;
    private View viewAddcombination;

    private MainpageEngineImpl dataEngine;

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
        dataEngine = new MainpageEngineImpl();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main, null);
        comtentView = view;
        initView(view);
        return view;
    }

    private void initView(View view) {

        view.findViewById(R.id.btn_back).setOnClickListener(this);

        view.findViewById(R.id.btn_right).setOnClickListener(this);
        // view.findViewById(R.id.iv_plus).setOnClickListener(this);
        // view.findViewById(R.id.btn_combination_more).setOnClickListener(this);

        // ((TextView) view.findViewById(R.id.tv_title)).setText(R.string.portfolio_text);

        tvBottomText = (MarqueeText) view.findViewById(R.id.tv_bottom_text);
        // setMarqueeText();

        gvFunction = (GridView) view.findViewById(R.id.gv_function);
        gvCombination = (GridView) view.findViewById(R.id.gv_mycombination);
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

        setViewLayoutParams();
        inflateAddLayout();
        // inflateOneLayout(view);
        // inflateTwoLayout(view);

    }

    /**
     * @Title
     * @Description TODO: (用一句话描述这个方法的功能)
     * @return
     */
    @Override
    public void onStart() {
        // TODO Auto-generated method stub
        super.onStart();
        dataEngine.getScrollValue(scrollDataListener);
        loadCombination();
    }

    private void inflateAddLayout() {
        ViewStub viewstub = (ViewStub) comtentView.findViewById(R.id.layout_add);
        if (viewstub != null) {
            viewAddcombination = viewstub.inflate();
            viewAddcombination.findViewById(R.id.layout_add_combination).setOnClickListener(this);
            viewAddcombination.findViewById(R.id.iv_plus).setOnClickListener(this);

        }
    }

    private void inflateCombinationLayout(final List<CombinationBean> dataList) {
        gvCombination.setVisibility(View.VISIBLE);
        comtentView.findViewById(R.id.title_main_combination).setVisibility(View.VISIBLE);
        comtentView.findViewById(R.id.divier_line).setVisibility(View.VISIBLE);
        comtentView.findViewById(R.id.title_main_combination).setOnClickListener(this);
        final MainCombinationoAdapter cAdapter = new MainCombinationoAdapter(getActivity(), dataList);
        // gvCombination.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
        // @Override
        // public void onGlobalLayout() {
        // final int columnHeight = gvCombination.getHeight();
        //
        // cAdapter.setItemHeight((int) (columnHeight));
        // }
        // });
        gvCombination.setAdapter(cAdapter);
        // gvCombination.setFocusable(true);
        if (null != viewAddcombination) {
            viewAddcombination.setVisibility(View.GONE);
        }

        gvCombination.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                startActivity(CombinationDetailActivity.newIntent(getActivity(), dataList.get(position)));

            }
        });
    }

    OnItemClickListener functionClick = new OnItemClickListener() {

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            // PromptManager.showToastTest("click Postion:" + position);

            Intent intent = null;
            switch (position) {
                case 0: {
                    intent = new Intent(getActivity(), MyCombinationActivity.class);
                }
                    break;
                case 1: {
                }
                    break;
                case 2: {
                    intent = new Intent(getActivity(), OptionalStockListActivity.class);
                }
                    break;
                case 3: {
                    intent = new Intent(getActivity(), MarketCenterActivity.class);

                }
                    break;
                case 4: {

                }
                    break;
                case 5: {

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

    private void loadCombination() {
        new MyCombinationEngineImpl().getCombinationList(new ParseHttpListener<List<CombinationBean>>() {

            @Override
            protected List<CombinationBean> parseDateTask(String jsonData) {
                Type listType = new TypeToken<List<CombinationBean>>() {
                }.getType();
                List<CombinationBean> combinationList = DataParse.parseJsonList(jsonData, listType);

                return combinationList;
            }

            @Override
            protected void afterParseData(List<CombinationBean> dataList) {
                // LogUtils.d("List<CombinationBean> size:" + dataList.size());
                if (null != dataList && isAdded()) {
                    // if (0 == dataList.size()) {
                    //
                    // } else if (1 == dataList.size()) {
                    // inflateOneLayout(dataList);
                    // } else if (dataList.size() > 1) {
                    // inflateTwoLayout(dataList);
                    // }
                    if (dataList.size() > 0) {
                        inflateCombinationLayout(dataList);
                    } else {
                        comtentView.findViewById(R.id.title_main_combination).setVisibility(View.GONE);
                        comtentView.findViewById(R.id.divier_line).setVisibility(View.GONE);
                        viewAddcombination.setVisibility(View.VISIBLE);
                        gvCombination.setVisibility(View.GONE);
                    }
                }
            }

        });
    }

    ParseHttpListener scrollDataListener = new ParseHttpListener<List<StockQuotesBean>>() {

        @Override
        protected List<StockQuotesBean> parseDateTask(String jsonData) {

            return DataParse.parseArrayJson(StockQuotesBean.class, jsonData);
        }

        @Override
        protected void afterParseData(List<StockQuotesBean> object) {
            if (null != object && object.size() > 1) {
                setMarqueeText(object);
            }

        }
    };

    private void setMarqueeText(List<StockQuotesBean> stockList) {
        // 上证指数：SH000001
        // 深证成指：SZ399001
        StockQuotesBean bean1 = stockList.get(0);
        StockQuotesBean bean2 = stockList.get(1);
        String szTilte = bean1.getName();
        String szCurrentValue = StringFromatUtils.get2Point(bean1.getCurrent());
        String szIncrease = StringFromatUtils.get2Point(bean1.getChange());
        String szPercentage = StringFromatUtils.get2PointPercent(bean1.getPercentage());

        String scTilte = bean2.getName();
        String scCurrentValue = StringFromatUtils.get2Point(bean2.getCurrent());
        String scIncrease = StringFromatUtils.get2Point(bean2.getChange());
        String scPercentage = StringFromatUtils.get2PointPercent(bean2.getPercentage());

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
        ForegroundColorSpan bean1CSpan;
        if (bean1.getPercentage() > 0) {
            bean1CSpan = new ForegroundColorSpan(Color.RED);
        } else {
            bean1CSpan = new ForegroundColorSpan(Color.GREEN);
        }
        sp.setSpan(bean1CSpan, szTilte.length(), startTextIndex + szPercentage.length(),
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        startTextIndex += szPercentage.length() + 1;

        sp.setSpan(new ForegroundColorSpan(Color.WHITE), startTextIndex, startTextIndex + scTilte.length(),
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        sp.setSpan(new RelativeSizeSpan(0.6f), startTextIndex, startTextIndex + scTilte.length(),
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

        startTextIndex += scTilte.length() + 1;

        ForegroundColorSpan bean2CSpan;
        if (bean2.getPercentage() > 0) {
            bean2CSpan = new ForegroundColorSpan(Color.RED);
        } else {
            bean2CSpan = new ForegroundColorSpan(Color.GREEN);
        }
        sp.setSpan(bean2CSpan, startTextIndex, startTextIndex + scCurrentValue.length() + 1 + scIncrease.length() + 1
                + scPercentage.length() + 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

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
            case R.id.layout_add_combination:
            case R.id.iv_plus: {

                intent = PositionAdjustActivity.newIntent(getActivity(), null);
            }
                break;
            case R.id.layout_first_combination: {

                PromptManager.showToastTest("进入第一个组合");
            }
                break;
            case R.id.layout_two_combination: {
                PromptManager.showToastTest("进入第二个组合");

            }
                break;
            case R.id.btn_combination_more:
            case R.id.title_main_combination: {
                intent = new Intent(getActivity(), MyCombinationActivity.class);

            }
                break;
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
