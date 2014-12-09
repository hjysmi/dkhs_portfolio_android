package com.dkhs.portfolio.ui.fragment;

import java.lang.reflect.Field;
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
import android.view.ViewTreeObserver;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewStub;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.dkhs.portfolio.R;
import com.dkhs.portfolio.app.PortfolioApplication;
import com.dkhs.portfolio.bean.ChampionCollectionBean;
import com.dkhs.portfolio.bean.CombinationBean;
import com.dkhs.portfolio.bean.MoreDataBean;
import com.dkhs.portfolio.bean.StockQuotesBean;
import com.dkhs.portfolio.engine.MainpageEngineImpl;
import com.dkhs.portfolio.engine.MyCombinationEngineImpl;
import com.dkhs.portfolio.net.DataParse;
import com.dkhs.portfolio.net.ParseHttpListener;
import com.dkhs.portfolio.ui.CombinationDetailActivity;
import com.dkhs.portfolio.ui.FundsOrderActivity;
import com.dkhs.portfolio.ui.MarketCenterActivity;
import com.dkhs.portfolio.ui.MyCombinationActivity;
import com.dkhs.portfolio.ui.OptionMarketNewsActivity;
import com.dkhs.portfolio.ui.OptionalStockListActivity;
import com.dkhs.portfolio.ui.PositionAdjustActivity;
import com.dkhs.portfolio.ui.YanBaoActivity;
import com.dkhs.portfolio.ui.adapter.MainCombinationoAdapter;
import com.dkhs.portfolio.ui.adapter.MainFunctionAdapter;
import com.dkhs.portfolio.ui.widget.FixedSpeedScroller;
import com.dkhs.portfolio.ui.widget.ITitleButtonListener;
import com.dkhs.portfolio.utils.ColorTemplate;
import com.dkhs.portfolio.utils.PromptManager;
import com.dkhs.portfolio.utils.StringFromatUtils;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.umeng.analytics.MobclickAgent;

public class MainFragment extends Fragment implements OnClickListener {

    private ITitleButtonListener mTitleClickListener;

    // private MarqueeText tvBottomText;
    private TextView tvBottomText;
    //
    private ViewPager viewPager;
    private LinearLayout dotLayout;
    // private List<ImageView> imageViews;
    private int[] imageResId;
    private int currentItem = 0;

    private GridView gvFunction;
    private GridView gvCombination;

    private View comtentView;
    // private View viewOnecombination;
    // private View viewTwocombination;
    private View viewAddcombination;
    private View mConbinlayout;

    private MainpageEngineImpl dataEngine;

    // private View viewOptionalStock;
    // private View viewMyCombination;
    // private View viewStockRanking;
    // private View viewPlateRanking;
    // private View viewFundRanking;
    // private View viewPortfolioRanking;

    private static final int MSG_CHANGE_PAGER = 172;

    // 15s
    private static final long mPollRequestTime = 1000 * 5;
    // 30s
    private static final long mCombinationRequestTime = 1000 * 30;
    private Timer mScollTimer;
    private Timer mMarketTimer;
    private Timer mCombinationTimer;

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
        System.out.println("=========onCreateView=========");
        View view = inflater.inflate(R.layout.fragment_main, null);
        comtentView = view;
        initView(view);
        return view;
    }

    private void initView(View view) {

        view.findViewById(R.id.btn_back).setOnClickListener(this);

        ImageButton btnRight = (ImageButton) view.findViewById(R.id.btn_right);
        btnRight.setBackgroundResource(R.drawable.ic_title_add);
        btnRight.setOnClickListener(this);
        btnRight.setVisibility(View.VISIBLE);
        // view.findViewById(R.id.iv_plus).setOnClickListener(this);
        // view.findViewById(R.id.btn_combination_more).setOnClickListener(this);

        // ((TextView)
        // view.findViewById(R.id.tv_title)).setText(R.string.portfolio_text);

        tvBottomText = (TextView) view.findViewById(R.id.tv_bottom_text);
        // setMarqueeText();

        gvCombination = (GridView) view.findViewById(R.id.gv_mycombination);

        gvFunction = (GridView) view.findViewById(R.id.gv_function);
        // gvFunction.getLayoutParams().height =
        // getResources().getDisplayMetrics().widthPixels / 3 * 2;
        gvFunction.setAdapter(new MainFunctionAdapter(getActivity()));
        gvFunction.setOnItemClickListener(functionClick);

        // 初始化界面控件实例
        dotLayout = (LinearLayout) view.findViewById(R.id.linearlayout_dot);
        initDotAndPicture();
        //
        viewPager = (ViewPager) view.findViewById(R.id.vp_billboard);
        setViewPageScroll();

        dataEngine.getChampionList(championDataListener);

        // view.findViewById(R.id.order_layout).setOnClickListener(new OnClickListener() {
        //
        // @Override
        // public void onClick(View v) {
        // Intent intent = new Intent(getActivity(), FundsOrderActivity.class);
        // startActivity(intent);
        // }
        // });

        tvBottomText.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), MarketCenterActivity.class);
                startActivity(intent);

            }
        });
        mConbinlayout = view.findViewById(R.id.ll_myconbinlayout);

        setViewLayoutParams();
        inflateAddLayout();

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

    }

    private void setViewPageScroll() {
        // Interpolator sInterpolator = new AccelerateInterpolator();
        try {
            Field mScroller;
            mScroller = ViewPager.class.getDeclaredField("mScroller");
            mScroller.setAccessible(true);
            FixedSpeedScroller scroller = new FixedSpeedScroller(viewPager.getContext());
            scroller.setDuration(1000);
            mScroller.set(viewPager, scroller);
        } catch (NoSuchFieldException e) {
        } catch (IllegalArgumentException e) {
        } catch (IllegalAccessException e) {
        }
    }

    private void inflateAddLayout() {
        ViewStub viewstub = (ViewStub) comtentView.findViewById(R.id.layout_add);
        if (viewstub != null) {
            viewAddcombination = viewstub.inflate();
            viewAddcombination.findViewById(R.id.layout_add_combination).setOnClickListener(this);
            viewAddcombination.findViewById(R.id.iv_plus).setOnClickListener(this);

        }
    }

    private View viewFirst;
    private View viewTwo;
    View viewAdd = null;

    private void inflateCombinationLayout(final List<CombinationBean> dataList) {
        if (null != viewAddcombination) {
            viewAddcombination.setVisibility(View.GONE);
        }
        comtentView.findViewById(R.id.title_main_combination).setVisibility(View.VISIBLE);
        comtentView.findViewById(R.id.divier_line).setVisibility(View.VISIBLE);
        comtentView.findViewById(R.id.title_main_combination).setOnClickListener(this);
        ViewStub viewstubFirst = (ViewStub) comtentView.findViewById(R.id.vs_fristcombination);
        ViewStub viewstubAdd = (ViewStub) comtentView.findViewById(R.id.vs_addcombination);
        ViewStub viewstubTwo = (ViewStub) comtentView.findViewById(R.id.vs_twocombination);
        if (null != dataList && dataList.size() > 0) {

            // if (viewstubFirst != null) {
            if (null == viewFirst) {
                viewFirst = viewstubFirst.inflate();
            }
            final CombinationBean bean1 = dataList.get(0);
            initCombinationView(viewFirst, bean1);
            viewFirst.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View v) {
                    startActivity(CombinationDetailActivity.newIntent(getActivity(), bean1));
                }
            });
            // }
            if (dataList.size() < 2) {
                // if (viewstubAdd != null) {
                if (null == viewAdd) {
                    System.out.println("viewAdd.inflate");
                    viewAdd = viewstubAdd.inflate();
                    viewAdd.setOnClickListener(new OnClickListener() {

                        @Override
                        public void onClick(View v) {
                            Intent intent = PositionAdjustActivity.newIntent(getActivity(), null);
                            getActivity().startActivity(intent);
                        }
                    });
                } else {
                    System.out.println("viewAdd.setVisibility(View.VISIBLE)");
                    viewAdd.setVisibility(View.VISIBLE);
                    System.out.println("viewTwo.setVisibility(View.GONE)");
                    if (null != viewTwo) {
                        System.out.println("viewTwo.setVisibility(View.GONE)");
                        viewTwo.setVisibility(View.GONE);
                    }
                }

                // }
            } else {

                // if (viewstubTwo != null) {
                if (null == viewTwo) {
                    viewTwo = viewstubTwo.inflate();
                }
                final CombinationBean bean2 = dataList.get(1);
                if (null != viewAdd) {
                    viewAdd.setVisibility(View.GONE);
                }
                initCombinationView(viewTwo, bean2);
                viewTwo.setOnClickListener(new OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        startActivity(CombinationDetailActivity.newIntent(getActivity(), bean2));
                    }
                });
                // }

            }

        }
        // gvCombination.setVisibility(View.VISIBLE);

        // final MainCombinationoAdapter cAdapter = new MainCombinationoAdapter(getActivity(), dataList);
        // // gvCombination.getViewTreeObserver().addOnGlobalLayoutListener(new
        // // ViewTreeObserver.OnGlobalLayoutListener() {
        // // @Override
        // // public void onGlobalLayout() {
        // // final int columnHeight = gvCombination.getHeight();
        // //
        // // cAdapter.setItemHeight((int) (columnHeight));
        // // }
        // // });
        // gvCombination.setAdapter(cAdapter);
        // gvCombination.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
        //
        // @Override
        // public void onGlobalLayout() {
        // int gvHeight = gvCombination.getLayoutParams().height;
        // System.out.println("gvHeight:" + gvHeight);
        // // cAdapter.setItemHeight(gvHeight);
        //
        // }
        // });
        // // gvCombination.setFocusable(true);

        //
        // gvCombination.setOnItemClickListener(new OnItemClickListener() {
        //
        // @Override
        // public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        // startActivity(CombinationDetailActivity.newIntent(getActivity(), dataList.get(position)));
        //
        // }
        // });
    }

    private void initCombinationView(View view, CombinationBean item) {
        TextView cNameText = (TextView) view.findViewById(R.id.tv_mycombination);
        TextView cCurrentText = (TextView) view.findViewById(R.id.tv_current_value);
        TextView cAddupText = (TextView) view.findViewById(R.id.tv_addup_value);
        cNameText.setText(item.getName());
        float currenValue = item.getCurrentValue();
        cCurrentText.setTextColor(ColorTemplate.getUpOrDrownCSL(currenValue));
        cCurrentText.setText(StringFromatUtils.get2PointPercentPlus(currenValue));

        float addValue = item.getAddUpValue();
        cAddupText.setTextColor(ColorTemplate.getUpOrDrownCSL(addValue));
        cAddupText.setText(StringFromatUtils.get2PointPercentPlus(addValue));
    }

    OnItemClickListener functionClick = new OnItemClickListener() {

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            // PromptManager.showToastTest("click Postion:" + position);

            Intent intent = null;
            switch (position) {
                case 0: {
                    goCombination();
                }
                    break;
                // case 1: {
                // intent = new Intent(getActivity(), FundsOrderActivity.class);
                // }
                // break;
                case 1: {
                    intent = new Intent(getActivity(), OptionalStockListActivity.class);
                }
                    break;
                case 2: {
                    intent = new Intent(getActivity(), MarketCenterActivity.class);
                }
                    break;
                case 3: {
                    intent = new Intent(getActivity(), OptionMarketNewsActivity.class);

                }
                    break;
                case 4: {
                    intent = new Intent(getActivity(), YanBaoActivity.class);
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

        new MyCombinationEngineImpl().getCombinationList(new ParseHttpListener<MoreDataBean<CombinationBean>>() {

            @Override
            protected MoreDataBean<CombinationBean> parseDateTask(String jsonData) {
                MoreDataBean<CombinationBean> moreBean = null;
                try {

                    Gson gson = new GsonBuilder().enableComplexMapKeySerialization().create();

                    moreBean = (MoreDataBean) gson.fromJson(jsonData, new TypeToken<MoreDataBean<CombinationBean>>() {
                    }.getType());

                } catch (Exception e) {
                    e.printStackTrace();
                }

                return moreBean;

            }

            @Override
            protected void afterParseData(MoreDataBean<CombinationBean> moreBean) {
                // LogUtils.d("List<CombinationBean> size:" +
                // dataList.size());
                mMoreCombination = moreBean;
                if (null != moreBean) {
                    List<CombinationBean> dataList = moreBean.getResults();
                    if (null != dataList && isAdded()) {

                        if (dataList.size() > 0) {
                            if (null != mConbinlayout) {

                                mConbinlayout.setVisibility(View.VISIBLE);
                            }
                            inflateCombinationLayout(dataList);
                        } else {
                            comtentView.findViewById(R.id.title_main_combination).setVisibility(View.GONE);
                            comtentView.findViewById(R.id.divier_line).setVisibility(View.GONE);
                            viewAddcombination.setVisibility(View.VISIBLE);
                            gvCombination.setVisibility(View.GONE);
                            mConbinlayout.setVisibility(View.GONE);
                        }
                    }
                }
            }

        });
    }

    private MoreDataBean mMoreCombination;

    ParseHttpListener scrollDataListener = new ParseHttpListener<List<StockQuotesBean>>() {

        @Override
        protected List<StockQuotesBean> parseDateTask(String jsonData) {

            return DataParse.parseArrayJson(StockQuotesBean.class, jsonData);
        }

        @Override
        protected void afterParseData(List<StockQuotesBean> object) {
            if (null != object && object.size() > 1) {
                if (!object.get(0).getTrade_status().equals("0")) {
//                    if (mScollTimer != null) {
//                        mScollTimer.cancel();
//                        mScollTimer = null;
//                    }
                    if (mMarketTimer != null) {
                        mMarketTimer.cancel();
                        mMarketTimer = null;
                    }
                    if (mCombinationTimer != null) {
                        mCombinationTimer.cancel();
                        mCombinationTimer = null;
                    }
                }
                setMarqueeText(object);
            }

        }
    };

    List<Fragment> fList = new ArrayList<Fragment>();
    ParseHttpListener championDataListener = new ParseHttpListener<ChampionCollectionBean>() {

        @Override
        protected ChampionCollectionBean parseDateTask(String jsonData) {
            return DataParse.parseObjectJson(ChampionCollectionBean.class, jsonData);
            // return DataParse.parseArrayJson(StockQuotesBean.class, jsonData);
        }

        @Override
        protected void afterParseData(ChampionCollectionBean object) {
            // = new ArrayList<Fragment>();

            fList.clear();

            fList.add(ScrollTopFragment.getInstance(ScrollTopFragment.TYPE_All, object.getCumulative()
                    .getIncreasePercent()));
            float dayValue = object.getDay() == null ? 0 : object.getDay().getIncreasePercent();
            fList.add(ScrollTopFragment.getInstance(ScrollTopFragment.TYPE_DAY, dayValue));
            fList.add(ScrollTopFragment.getInstance(ScrollTopFragment.TYPE_WEEK, object.getWeek().getIncreasePercent()));
            fList.add(ScrollTopFragment.getInstance(ScrollTopFragment.TYPE_MONTH, object.getMonth()
                    .getIncreasePercent()));
            fList.add(ScrollTopFragment.getInstance(ScrollTopFragment.TYPE_All, object.getCumulative()
                    .getIncreasePercent()));
            fList.add(ScrollTopFragment.getInstance(ScrollTopFragment.TYPE_DAY, object.getWeek().getIncreasePercent()));

            viewPager.setAdapter(new ScrollFragmentAdapter(getChildFragmentManager(), fList));
            viewPager.setOnPageChangeListener(scrollPageChangeListener);
            viewPager.setOffscreenPageLimit(6);

            viewPager.setCurrentItem(1);
            // viewPager.setCurrentItem(0);

        }
    };

    OnPageChangeListener scrollPageChangeListener = new OnPageChangeListener() {
        private int oldPosition = 0;

        @Override
        public void onPageSelected(int i) {
            int pageIndex = i;

            if (i == 0) {
                // 当视图在第一个时，将页面号设置为图片的最后一张。
                pageIndex = fList.size() - 2;
            } else if (i == fList.size() - 2 + 1) {
                // 当视图在最后一个是,将页面号设置为图片的第一张。
                pageIndex = 1;
            }
            dotLayout.getChildAt(oldPosition).setBackgroundResource(R.drawable.dot_normal);
            dotLayout.getChildAt(pageIndex).setBackgroundResource(R.drawable.dot_focused);
            oldPosition = pageIndex;
            currentItem = pageIndex;
            if (i != pageIndex) {
                viewPager.setCurrentItem(pageIndex, false);
                return;
            }

        }

        @Override
        public void onPageScrolled(int arg0, float arg1, int arg2) {
            // TODO Auto-generated method stub

        }

        @Override
        public void onPageScrollStateChanged(int arg0) {
            // TODO Auto-generated method stub

        }
    };

    private void setMarqueeText(List<StockQuotesBean> stockList) {
        // 上证指数：SH000001
        // 深证成指：SZ399001
        StockQuotesBean bean1 = stockList.get(0);
        StockQuotesBean bean2 = stockList.get(1);
        String szTilte = "沪";
        String szCurrentValue = StringFromatUtils.get2Point(bean1.getCurrent());
        // String szIncrease = StringFromatUtils.get2Point(bean1.getChange());
        String szIncrease = "";
        String szPercentage = StringFromatUtils.get2PointPercent(bean1.getPercentage());

        // String scTilte = bean2.getName();
        String scTilte = "深";
        String scCurrentValue = StringFromatUtils.get2Point(bean2.getCurrent());
        // String scIncrease = StringFromatUtils.get2Point(bean2.getChange());
        String scIncrease = "";
        String scPercentage = StringFromatUtils.get2PointPercent(bean2.getPercentage());

        SpannableStringBuilder sp = new SpannableStringBuilder();
        sp.append(szTilte);
        sp.append(" ");
        sp.append(szCurrentValue);
        // sp.append(" ");
        sp.append(szIncrease);
        sp.append(" ");
        sp.append(szPercentage);
        sp.append(" ");
        sp.append(scTilte);
        sp.append(" ");
        sp.append(scCurrentValue);
        // sp.append(" ");
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
        // sp.setSpan(redSpan, startTextIndex, startTextIndex +
        // szCurrentValue.length(),
        // Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        startTextIndex += szCurrentValue.length() + 1;
        sp.setSpan(new RelativeSizeSpan(0.6f), startTextIndex, startTextIndex + szIncrease.length(),
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        // sp.setSpan(redSpan, startTextIndex, startTextIndex +
        // szIncrease.length(),
        // Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

        startTextIndex += szIncrease.length();
        sp.setSpan(new RelativeSizeSpan(0.6f), startTextIndex, startTextIndex + szPercentage.length(),
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        ForegroundColorSpan bean1CSpan;
        if (bean1.getPercentage() == 0) {
            bean1CSpan = new ForegroundColorSpan(getResources().getColor(R.color.def_gray));
        } else if (bean1.getPercentage() > 0) {
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
        if (bean2.getPercentage() == 0) {
            bean2CSpan = new ForegroundColorSpan(getResources().getColor(R.color.def_gray));
        } else if (bean2.getPercentage() > 0) {
            bean2CSpan = new ForegroundColorSpan(Color.RED);
        } else {
            bean2CSpan = new ForegroundColorSpan(Color.GREEN);
        }
        sp.setSpan(bean2CSpan, startTextIndex, startTextIndex + scCurrentValue.length() + 1 + scIncrease.length()
                + scPercentage.length() + 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

        sp.setSpan(new RelativeSizeSpan(0.6f), startTextIndex + 1 + scCurrentValue.length(), startTextIndex
                + scCurrentValue.length() + 1 + scIncrease.length() + scPercentage.length() + 1,
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        tvBottomText.setText(sp);

        // tvBottomText.startScroll();

        // tvBottomText.setText(Html.fromHtml(htmlText));
    }

    private void setViewLayoutParams() {
        int screenWidth = getResources().getDisplayMetrics().widthPixels;
        viewPager.getLayoutParams().width = screenWidth / 2;
    }

    private final String mPageName = PortfolioApplication.getInstance().getString(R.string.count_main);

    @Override
    public void onPause() {
        // TODO Auto-generated method stub
        super.onPause();
        // SDK已经禁用了基于Activity 的页面统计，所以需要再次重新统计页面
        MobclickAgent.onPageEnd(mPageName);
    }

    @Override
    public void onResume() {
        super.onResume();
        System.out.println("=========onResume=========");
        MobclickAgent.onPageStart(mPageName);
        if (mScollTimer == null) { // 保证只有一个 定时任务
            mScollTimer = new Timer(true);
            mScollTimer.schedule(new ScrollPageTask(), 2000, 2000);
        }
        if (mMarketTimer == null) {
            mMarketTimer = new Timer(true);
            mMarketTimer.schedule(new RequestMarketTask(), 100, mPollRequestTime);
        }
        if (mCombinationTimer == null) {
            mCombinationTimer = new Timer(true);
            mCombinationTimer.schedule(new RequestCombinationTask(), 200, mCombinationRequestTime);
        }
    }

    @Override
    public void onStop() {
        super.onStop();

        if (mScollTimer != null) {
            mScollTimer.cancel();
            mScollTimer = null;
        }
        if (mMarketTimer != null) {
            mMarketTimer.cancel();
            mMarketTimer = null;
        }
        if (mCombinationTimer != null) {
            mCombinationTimer.cancel();
            mCombinationTimer = null;
        }
    }

    public class ScrollPageTask extends TimerTask {

        @Override
        public void run() {
            mHandler.sendEmptyMessage(MSG_CHANGE_PAGER);
        }
    }

    public class RequestMarketTask extends TimerTask {

        @Override
        public void run() {
            dataEngine.getScrollValue(scrollDataListener);

        }
    }

    public class RequestCombinationTask extends TimerTask {

        @Override
        public void run() {
            loadCombination();

        }
    }

    private void createCombination() {
        Intent intent = PositionAdjustActivity.newIntent(getActivity(), null);
        Bundle bun = new Bundle();
        // bun.put
        getActivity().startActivity(intent);
    }

    @Override
    public void onClick(View v) {
        Intent intent = null;
        MobclickAgent.onEvent(getActivity(), mPageName);
        switch (v.getId()) {
            case R.id.btn_back: {
                if (null != mTitleClickListener) {
                    mTitleClickListener.leftButtonClick();
                }
            }
                break;
            case R.id.btn_right: {
                // if (null != mTitleClickListener) {
                // mTitleClickListener.rightButtonClick();
                // }
                if (null != mMoreCombination && mMoreCombination.getTotalCount() >= 20) {
                    PromptManager.showShortToast(R.string.more_combination_tip);
                } else {
                    createCombination();
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
                // intent = new Intent(getActivity(), MyCombinationActivity.class);
                goCombination();

            }
                break;
            default:
                break;
        }
        if (null != intent) {
            getActivity().startActivity(intent);
        }

    }

    private void goCombination() {
        Intent intent = new Intent(getActivity(), MyCombinationActivity.class);
        getActivity().startActivity(intent);
    }

    public void setTitleClickListener(ITitleButtonListener listener) {
        this.mTitleClickListener = listener;
    }

    /**
     * 初始化要切换的图片和点
     */
    private void initDotAndPicture() {
        imageResId = new int[] { R.drawable.ic_agree, R.drawable.ic_agree, R.drawable.ic_agree };

        // imageViews = new ArrayList<ImageView>();
        // int viewWidth =
        // getActivity().getResources().getDisplayMetrics().widthPixels;

        // int dotWidth = viewWidth / imageResId.length;
        // 初始化图片资源

        // 根据图片动态设置小圆点
        LayoutInflater inflater = getActivity().getLayoutInflater();
        // 初始化图片资源
        for (int i = 0; i < imageResId.length + 3; i++) {
            // ImageView imageView = new ImageView(getActivity());
            // imageView.setImageResource(imageResId[i]);
            // imageView.setScaleType(ScaleType.FIT_XY);
            // imageViews.add(imageView);

            // 根据图片动态设置小圆点
            inflater.inflate(R.layout.dot, dotLayout);
        }
        dotLayout.getChildAt(0).setVisibility(View.INVISIBLE);
        dotLayout.getChildAt(imageResId.length + 2).setVisibility(View.INVISIBLE);
    }

    /**
     * 当ViewPager中页面的状态发生改变时调用
     * 
     * 
     */
    private class MyPageChangeListener implements OnPageChangeListener {

        /**
         * 当页面被选中的时候调用这个方法 position: 页面tag标识
         */
        public void onPageSelected(int position) {

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
