/**
 * @Title FragmentNetValueTrend.java
 * @Package com.dkhs.portfolio.ui.fragment
 * @Description TODO(用一句话描述该文件做什么)
 * @author zjz
 * @date 2014-9-1 下午1:52:54
 * @version V1.0
 */
package com.dkhs.portfolio.ui.fragment;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.Resources.NotFoundException;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ToggleButton;
import cn.sharesdk.onekeyshare.OnekeyShare;

import com.dkhs.portfolio.R;
import com.dkhs.portfolio.app.PortfolioApplication;
import com.dkhs.portfolio.bean.CombinationBean;
import com.dkhs.portfolio.bean.PositionDetail;
import com.dkhs.portfolio.engine.FollowComEngineImpl;
import com.dkhs.portfolio.engine.FundsOrderEngineImpl;
import com.dkhs.portfolio.engine.MyCombinationEngineImpl;
import com.dkhs.portfolio.net.DKHSClient;
import com.dkhs.portfolio.net.DKHSUrl;
import com.dkhs.portfolio.net.DataParse;
import com.dkhs.portfolio.net.ParseHttpListener;
import com.dkhs.portfolio.ui.CombinationDetailActivity;
import com.dkhs.portfolio.ui.ITouchListener;
import com.dkhs.portfolio.ui.fragment.FragmentMarkerCenter.RequestMarketTask;
import com.dkhs.portfolio.ui.widget.HScrollTitleView;
import com.dkhs.portfolio.ui.widget.HScrollTitleView.ISelectPostionListener;
import com.dkhs.portfolio.ui.widget.ScrollViewPager;
import com.dkhs.portfolio.utils.ColorTemplate;
import com.dkhs.portfolio.utils.PromptManager;
import com.dkhs.portfolio.utils.StringFromatUtils;
import com.umeng.analytics.MobclickAgent;

/**
 * @ClassName FragmentNetValueTrend
 * @Description TODO(这里用一句话描述这个类的作用)
 * @author zjz
 * @date 2014-9-1 下午1:52:54
 * @version 1.0
 */
public class FragmentNetValueTrend extends Fragment implements OnClickListener, FragmentLifecycle {
    // private EditText etCombinName;
    private TextView tvCombinName;
    private TextView tvCombinDesc;
    private TextView tvCombinCreateTime;
    private TextView tvIncreaseValue;
    private TextView tvIncreaseRatio;
    // private View viewNetvalueHead;
    private ImageView ivUpDownIcon;
    // private Button btnEditName;
    private CombinationBean mCombinationBean;
    private MyCombinationEngineImpl mMyCombinationEngineImpl;
    MyPagerFragmentAdapter mPagerAdapter;

    public static final String ARGUMENT_ISFROM_ORDER = "argument_isfrom_order";
    private TextView netvalueDay;
    private TextView netvalueWeek;
    private TextView netvalueMonth;
    private Button netvalueBtnDay;
    private Button netvalueBtnWeek;
    private Button netvalueBtnMonth;
    private String type;
    private Timer mMarketTimer;
    private static final long mPollRequestTime = 1000 * 60;
    private String myType = TrendTodayChartFragment.TREND_TYPE_TODAY;
    private PositionDetail mPositionDetail;

    public static FragmentNetValueTrend newInstance(boolean isOrder, String type) {
        FragmentNetValueTrend fragment = new FragmentNetValueTrend();

        Bundle arguments = new Bundle();
        arguments.putBoolean(ARGUMENT_ISFROM_ORDER, isOrder);
        arguments.putString("type", type);
        fragment.setArguments(arguments);

        return fragment;
    }

    /**
     * @Title
     * @Description TODO: (用一句话描述这个方法的功能)
     * @param savedInstanceState
     * @return
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
        Bundle arguments = getArguments();
        if (arguments != null) {
            handleArguments(arguments);
        }

        // handle intent extras
        Bundle extras = getActivity().getIntent().getExtras();
        if (extras != null) {
            handleExtras(extras);
        }

    }

    private void handleExtras(Bundle extras) {
        mCombinationBean = (CombinationBean) extras.getSerializable(CombinationDetailActivity.EXTRA_COMBINATION);

    }

    private boolean isFromOrder;

    private void handleArguments(Bundle arguments) {
        // TODO Auto-generated method stub
        isFromOrder = arguments.getBoolean(ARGUMENT_ISFROM_ORDER, false);
        type = arguments.getString("type");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_netvalue_trend, null);
        // etCombinName = (EditText)
        // view.findViewById(R.id.et_combination_name);
        mMyCombinationEngineImpl = new MyCombinationEngineImpl();
        ivUpDownIcon = (ImageView) view.findViewById(R.id.tv_combination_image_uporlow);
        tvCombinDesc = (TextView) view.findViewById(R.id.tv_combination_desc);
        tvCombinCreateTime = (TextView) view.findViewById(R.id.tv_combination_time);
        tvCombinName = (TextView) view.findViewById(R.id.tv_combination_name);
        tvIncreaseRatio = (TextView) view.findViewById(R.id.tv_income_netvalue);
        tvIncreaseValue = (TextView) view.findViewById(R.id.tv_history_netvalue);
        netvalueDay = (TextView) view.findViewById(R.id.netvalue_day);
        netvalueBtnDay = (Button) view.findViewById(R.id.netvalue_button_day);
        netvalueWeek = (TextView) view.findViewById(R.id.netvalue_week);
        netvalueMonth = (TextView) view.findViewById(R.id.netvalue_month);
        netvalueBtnWeek = (Button) view.findViewById(R.id.netvalue_button_week);
        netvalueBtnMonth = (Button) view.findViewById(R.id.netvalue_button_month);
        btnAddOptional = (Button) view.findViewById(R.id.btn_add_optional);
        btnAddOptional.setOnClickListener(this);
        btnAddOptional.setVisibility(View.GONE);
        // viewNetvalueHead = view.findViewById(R.id.tv_combination_layout);
        // btnEditName = (Button) view.findViewById(R.id.btn_edit_combinname);
        // btnEditName.setOnClickListener(this);
        QueryCombinationListener listener = new QueryCombinationListener();
        mMyCombinationEngineImpl.queryCombinationDetail(mCombinationBean.getId(), listener);
        // listener.setLoadingDialog(getActivity());
        initTabPage(view);

        // if(null!=mCombinationBean&&
        // mCombinationBean.getCreateUser().getId().equalsIgnoreCase(PortfolioPreferenceManager.getStringValue(PortfolioPreferenceManager.KEY_USERID))){
        //
        //
        // }

        setupViewData();

        return view;
    }

    private Button btnAddOptional;

    private void addOptionalButton(boolean isFollow) {
        if (isFollow && null != btnAddOptional) {
            btnAddOptional.setText(R.string.delete_fllow);
            btnAddOptional.setBackgroundResource(R.drawable.bg_unfollowed);
            btnAddOptional.setTextColor(ColorTemplate.getTextColor(R.color.unfollowd));
        } else if (null != btnAddOptional) {
            btnAddOptional.setBackgroundResource(R.drawable.btn_addoptional_selector);
            btnAddOptional.setText(R.string.add_fllow);
            btnAddOptional.setTextColor(ColorTemplate.getTextColor(R.color.white));
        }
    }

    class OnComCheckListener implements OnCheckedChangeListener {

        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            // TODO Auto-generated method stub
            if (isChecked) {
                QueryCombinationDetailListener listener = new QueryCombinationDetailListener();
                mMyCombinationEngineImpl.changeCombinationIsPublic(mCombinationBean.getId(), "0", listener);
                // listener.setLoadingDialog(getActivity());
            } else {
                QueryCombinationDetailListener listener = new QueryCombinationDetailListener();
                mMyCombinationEngineImpl.changeCombinationIsPublic(mCombinationBean.getId(), "1", listener);
                // listener.setLoadingDialog(getActivity());
            }
        }

    }

    class QueryCombinationDetailListener extends ParseHttpListener<List<CombinationBean>> {

        @Override
        protected List<CombinationBean> parseDateTask(String jsonData) {
            JSONArray jsonObject = null;
            try {
                jsonObject = new JSONArray(jsonData);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            List<CombinationBean> object = DataParse.parseArrayJson(CombinationBean.class, jsonObject);
            return object;
        }

        @Override
        protected void afterParseData(List<CombinationBean> object) {
            if (null != object) {

            }

        }
    }

    private void setupViewData() {
        if (null != mCombinationBean) {
            updateIncreaseRatio(mCombinationBean.getNetvalue());

        }
    }

    Handler updateHandler = new Handler() {
        public void handleMessage(android.os.Message msg) {

            replaceFragment(todayFragment);
        };
    };

    private void updateIncreaseRatio(float netValue) {
        tvIncreaseValue.setText(StringFromatUtils.get4Point(netValue));
        tvIncreaseRatio.setTextColor(ColorTemplate.getUpOrDrownCSL(netValue - 1));
        tvIncreaseRatio.setText(StringFromatUtils.get2PointPercent((netValue - 1) * 100));

    }

    public void setColor(String type) {
        try {
            if (null != mPositionDetail) {
                updateIncreaseRatio(mPositionDetail.getPortfolio().getNetvalue());
                setDefViewStyle();

                if (type.equals(TrendTodayChartFragment.TREND_TYPE_TODAY)) {

                    netvalueDay.setTextColor(ColorTemplate.getUpOrDrownCSL(mPositionDetail.getPortfolio()
                            .getChng_pct_day()));
                    setSelectViewStyle(mPositionDetail.getPortfolio().getChng_pct_day(), netvalueBtnDay);
                    netvalueBtnWeek.setBackgroundResource(R.drawable.netvalue_gray);
                    netvalueBtnMonth.setBackgroundResource(R.drawable.netvalue_gray);
                } else if (type.equals(TrendSevenDayChartFragment.TREND_TYPE_SEVENDAY)) {

                    netvalueWeek.setTextColor(ColorTemplate.getUpOrDrownCSL(mPositionDetail.getPortfolio()
                            .getChng_pct_week()));
                    setSelectViewStyle(mPositionDetail.getPortfolio().getChng_pct_week(), netvalueBtnWeek);
                    netvalueBtnDay.setBackgroundResource(R.drawable.netvalue_gray);
                    netvalueBtnMonth.setBackgroundResource(R.drawable.netvalue_gray);
                } else if (type.equals(TrendMonthChartFragment.TREND_TYPE_MONTH)) {
                    netvalueMonth.setTextColor(ColorTemplate.getUpOrDrownCSL(mPositionDetail.getPortfolio()
                            .getChng_pct_month()));
                    setSelectViewStyle(mPositionDetail.getPortfolio().getChng_pct_month(), netvalueBtnMonth);
                    netvalueBtnDay.setBackgroundResource(R.drawable.netvalue_gray);
                    netvalueBtnWeek.setBackgroundResource(R.drawable.netvalue_gray);
                } else {
                    netvalueBtnMonth.setBackgroundResource(R.drawable.netvalue_gray);
                    netvalueBtnDay.setBackgroundResource(R.drawable.netvalue_gray);
                    netvalueBtnWeek.setBackgroundResource(R.drawable.netvalue_gray);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void setSelectViewStyle(float value, Button view) {
        if (null == mWhiteColorStateList) {
            mWhiteColorStateList = PortfolioApplication.getInstance().getResources().getColorStateList(R.color.white);
        }
        if (value > 0) {
            view.setBackgroundResource(R.drawable.netvalue_red);
        } else if (value < 0) {
            view.setBackgroundResource(R.drawable.netvalue_blue);
        } else {
            view.setBackgroundResource(R.drawable.netvalue_black);
        }
        view.setTextColor(mWhiteColorStateList);

    }

    private ColorStateList mWhiteColorStateList;
    private ColorStateList mGrayColorStateList;

    private void setDefViewStyle() {

        if (null == mGrayColorStateList) {
            mGrayColorStateList = PortfolioApplication.getInstance().getResources()
                    .getColorStateList(R.color.gray_textcolor);
        }
        netvalueBtnMonth.setTextColor(mGrayColorStateList);
        netvalueBtnDay.setTextColor(mGrayColorStateList);
        netvalueBtnWeek.setTextColor(mGrayColorStateList);
        netvalueDay.setTextColor(mGrayColorStateList);
        netvalueWeek.setTextColor(mGrayColorStateList);
        netvalueMonth.setTextColor(mGrayColorStateList);
    }

    private HScrollTitleView hsTitle;
    private ScrollViewPager mViewPager;
    private ArrayList<Fragment> fragmentList;

    TrendChartFragment mtrendFragment = new TrendChartFragment();

    TrendTodayChartFragment todayFragment = TrendTodayChartFragment
            .newInstance(TrendTodayChartFragment.TREND_TYPE_TODAY);
    TrendSevenDayChartFragment sevendayFragment = TrendSevenDayChartFragment
            .newInstance(TrendSevenDayChartFragment.TREND_TYPE_SEVENDAY);
    TrendMonthChartFragment monthFragment = TrendMonthChartFragment
            .newInstance(TrendMonthChartFragment.TREND_TYPE_MONTH);
    TrendHistoryChartFragment historyFragment = TrendHistoryChartFragment
            .newInstance(TrendHistoryChartFragment.TREND_TYPE_HISTORY);

    // FragmentSwitchChart mSwitchFragment = null;

    public void showShare(boolean silent, String platform, boolean captureView) {
        // if (null != mtrendFragment)
        // // mtrendFragment.showShare(silent, platform, captureView);
        // mtrendFragment.showShareImage();
    }

    private String SHARE_IMAGE;

    public void showShareImage() {

        // initImagePath();
        new Thread() {
            public void run() {

                // initImagePath();
                saveShareBitmap();
                shareHandler.sendEmptyMessage(999);
            }
        }.start();

    }

    Handler shareHandler = new Handler() {
        public void handleMessage(Message msg) {
            showShare();
        };
    };

    private void showShare() {
        if (null != mPositionDetail) {

            Context context = getActivity();
            final OnekeyShare oks = new OnekeyShare();

            oks.setNotification(R.drawable.ic_launcher, context.getString(R.string.app_name));

            String shareUrl = DKHSClient.getAbsoluteUrl(DKHSUrl.User.share) + mCombinationBean.getId();
            oks.setTitleUrl(shareUrl);
            oks.setUrl(shareUrl);
            oks.setTitle(mCombinationBean.getName() + " 今日收益率");

            String customText = "这是我的基金「" + mPositionDetail.getPortfolio().getName() + "」的收益率走势曲线。你也来创建属于你的基金吧。"
                    + shareUrl;

            oks.setText(customText);

            oks.setImagePath(SHARE_IMAGE);

            oks.setFilePath(SHARE_IMAGE);
            oks.setSilent(false);

            oks.setShareFromQQAuthSupport(false);

            // 令编辑页面显示为Dialog模式
            oks.setDialogMode();

            oks.show(context);
        }
    }

    private void saveShareBitmap() {
        try {

            View content = this.getView();
            content.setDrawingCacheEnabled(true);
            content.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);
            Bitmap bitmap = content.getDrawingCache();

            String extr = Environment.getExternalStorageDirectory() + "/CrashLog/";

            String s = "tmp.png";

            File f = new File(extr, s);
            SHARE_IMAGE = f.getAbsolutePath();

            FileOutputStream fos = null;
            fos = new FileOutputStream(f);
            bitmap.compress(CompressFormat.JPEG, 100, fos);
            fos.flush();
            fos.close();
            content.destroyDrawingCache();
            // bitmap.recycle();
            System.out.println("image saved:" + SHARE_IMAGE);
            // Toast.makeText(getActivity(), "image saved", 5000).show();
        } catch (Exception e) {
            System.out.println("Failed To Save");
            e.printStackTrace();
            // Toast.makeText(getActivity(), "Failed To Save", 5000).show();
        }
    }

    private Fragment lastFragment = null;

    private void replaceFragment(Fragment newFragment) {
        FragmentTransaction trasection = getChildFragmentManager().beginTransaction();
        if (lastFragment != null) {
            if (lastFragment instanceof TrendTodayChartFragment) {
                todayFragment.stopRequry();
            } else if (lastFragment instanceof TrendSevenDayChartFragment) {
                sevendayFragment.stopRequry();
            } else if (lastFragment instanceof TrendMonthChartFragment) {
                monthFragment.stopRequry();
            } else if (lastFragment instanceof TrendHistoryChartFragment) {
                historyFragment.stopRequry();
            }
            trasection.hide(lastFragment);
            trasection.commit();
        }
        if (!newFragment.isAdded()) {
            try {
                // FragmentTransaction trasection =
                getChildFragmentManager().beginTransaction();
                // trasection.replace(R.id.rl_trend_layout, newFragment);
                // trasection.addToBackStack(null);
                // trasection.add(R.id.rl_trend_layout, newFragment);
                trasection.add(R.id.rl_trend_layout, newFragment, String.valueOf(newFragment.getId()));
                trasection.commit();
            } catch (Exception e) {
                // TODO: handle exception
                // AppConstants.printLog(e.getMessage());

            }
        } else {
            trasection.show(newFragment);
        }
        lastFragment = newFragment;

    }

    private void showReportFragment(Fragment newFragment) {

        FragmentTransaction trasection = getChildFragmentManager().beginTransaction();
        if (!newFragment.isAdded()) {
            try {
                // FragmentTransaction trasection =
                getChildFragmentManager().beginTransaction();
                trasection.replace(R.id.rl_trend_layout, newFragment);
                trasection.addToBackStack(null);
                trasection.commit();

            } catch (Exception e) {
                // TODO: handle exception
                // AppConstants.printLog(e.getMessage());

            }
        } else {

            trasection.show(newFragment);
        }

    }

    private void initTabPage(View view) {

        String[] titleArray = getResources().getStringArray(R.array.trend_title);
        fragmentList = new ArrayList<Fragment>();// ViewPager中显示的数据

        hsTitle = (HScrollTitleView) view.findViewById(R.id.hs_title);
        // String[] titleArray =
        // getResources().getStringArray(R.array.quotes_title);
        hsTitle.setTitleList(titleArray, getResources().getDimensionPixelSize(R.dimen.title_2text_length));
        hsTitle.setSelectPositionListener(titleSelectPostion);

        if (null != type) {
            if (type.equals(FundsOrderEngineImpl.ORDER_DAY)) {
                // hsTitle.setSelectIndex(0);

            } else if (type.equals(FundsOrderEngineImpl.ORDER_WEEK)) {
                hsTitle.setSelectIndex(1);
            } else if (type.equals(FundsOrderEngineImpl.ORDER_MONTH)) {
                hsTitle.setSelectIndex(2);
            } else if (type.equals(FundsOrderEngineImpl.ORDER_ALL)) {
                hsTitle.setSelectIndex(3);
            }

        }
        updateHandler.sendEmptyMessageDelayed(200, 50);
    }

    ISelectPostionListener titleSelectPostion = new ISelectPostionListener() {

        @Override
        public void onSelectPosition(int position) {

            String type = TrendTodayChartFragment.TREND_TYPE_TODAY;
            Fragment curFragment = null;
            switch (position) {
                case 0: {
                    type = TrendTodayChartFragment.TREND_TYPE_TODAY;
                    curFragment = todayFragment;
                    todayFragment.startRequry();
                }
                    break;
                case 1: {
                    type = TrendSevenDayChartFragment.TREND_TYPE_SEVENDAY;
                    curFragment = sevendayFragment;
                    sevendayFragment.startRequry();
                }
                    break;
                case 2: {
                    type = TrendMonthChartFragment.TREND_TYPE_MONTH;
                    curFragment = monthFragment;
                    monthFragment.startRequry();
                }
                    break;
                case 3: {

                    type = TrendHistoryChartFragment.TREND_TYPE_HISTORY;
                    curFragment = historyFragment;
                    historyFragment.startRequry();
                }
                    break;

                default:
                    break;
            }
            // mSwitchFragment.setSelectType(type);
            myType = type;
            replaceFragment(curFragment);
            setColor(type);
        }
    };

    private class MyPagerFragmentAdapter extends FragmentPagerAdapter {

        private List<Fragment> fragmentList;
        private String[] titleList;

        public MyPagerFragmentAdapter(FragmentManager fm, ArrayList<Fragment> fragmentList2, String[] titleList) {
            super(fm);
            this.fragmentList = fragmentList2;
            this.titleList = titleList;
        }

        @Override
        public Fragment getItem(int arg0) {

            return (fragmentList == null || fragmentList.size() == 0) ? null : fragmentList.get(arg0);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return (titleList.length > position) ? titleList[position] : "";
        }

        @Override
        public int getCount() {
            return fragmentList == null ? 0 : fragmentList.size();
        }

    }

    private OnPageChangeListener pageChangeListener = new OnPageChangeListener() {

        int currentPosition = 0;

        @Override
        public void onPageSelected(int newPosition) {

            Fragment fragmentToShow = (Fragment) mPagerAdapter.getItem(newPosition);
            fragmentToShow.setUserVisibleHint(true);

            Fragment fragmentToHide = (Fragment) mPagerAdapter.getItem(currentPosition);
            fragmentToHide.setUserVisibleHint(false);
            currentPosition = newPosition;
        }

        @Override
        public void onPageScrolled(int arg0, float arg1, int arg2) {
        }

        public void onPageScrollStateChanged(int arg0) {
        }
    };

    @Override
    public void onClick(View v) {

        if (v.getId() == R.id.btn_add_optional) {
            btnAddOptional.setEnabled(false);
            if (mCombinationBean.isFollowed()) {
                new FollowComEngineImpl().defFollowCombinations(mCombinationBean.getId(), followComListener);
            } else {

                new FollowComEngineImpl().followCombinations(mCombinationBean.getId(), followComListener);
            }
        }
    }

    ParseHttpListener followComListener = new ParseHttpListener<Object>() {

        @Override
        public void requestCallBack() {
            super.requestCallBack();
            btnAddOptional.setEnabled(true);
        };

        @Override
        protected Object parseDateTask(String jsonData) {
            // TODO Auto-generated method stub
            return null;
        }

        @Override
        protected void afterParseData(Object object) {
            mCombinationBean.setFollowed(!mCombinationBean.isFollowed());
            addOptionalButton(mCombinationBean.isFollowed());
        }
    };

    @Override
    public void onPauseFragment() {

    }

    @Override
    public void onResume() {

        super.onResume();

        if (mMarketTimer == null) {
            mMarketTimer = new Timer(true);
            mMarketTimer.schedule(new RequestMarketTask(), mPollRequestTime, mPollRequestTime);
        }
        MobclickAgent.onPageStart(mPageName);
    }

    @Override
    public void onStop() {
        super.onStop();

        if (mMarketTimer != null) {
            mMarketTimer.cancel();
            mMarketTimer = null;
        }

    }

    public class RequestMarketTask extends TimerTask {

        @Override
        public void run() {
            QueryCombinationListener listener = new QueryCombinationListener();
            mMyCombinationEngineImpl.queryCombinationDetail(mCombinationBean.getId(), listener);
            // listener.setLoadingDialog(getActivity()).beforeRequest();
        }
    }

    class QueryCombinationListener extends ParseHttpListener<PositionDetail> {

        @Override
        protected PositionDetail parseDateTask(String jsonData) {
            JSONObject jsonObject = null;
            try {
                jsonObject = new JSONObject(jsonData);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            PositionDetail bean = DataParse.parseObjectJson(PositionDetail.class, jsonObject);
            return bean;
        }

        @Override
        protected void afterParseData(PositionDetail object) {
            if (null != object) {
                mPositionDetail = object;

                if (null != mPositionDetail.getPortfolio()) {
                    mCombinationBean = mPositionDetail.getPortfolio();
                    btnAddOptional.setVisibility(View.VISIBLE);
                    addOptionalButton(mCombinationBean.isFollowed());
                    netvalueDay.setText(StringFromatUtils.get2PointPercent(mPositionDetail.getPortfolio()
                            .getChng_pct_day()));
                    netvalueWeek.setText(StringFromatUtils.get2PointPercent(mPositionDetail.getPortfolio()
                            .getChng_pct_week()));
                    netvalueMonth.setText(StringFromatUtils.get2PointPercent(mPositionDetail.getPortfolio()
                            .getChng_pct_month()));
                }
                setColor(myType);

            }

        }
    };

    /**
     * @Title
     * @Description TODO: (用一句话描述这个方法的功能)
     * @return
     */
    @Override
    public void onResumeFragment() {
        // TODO Auto-generated method stub

    }

    private ITouchListener mTouchListener;

    public void setITouchListener(ITouchListener touchListener) {
        this.mTouchListener = touchListener;
    }

    private final String mPageName = PortfolioApplication.getInstance().getString(R.string.count_fund_order_line);

    @Override
    public void onPause() {
        // TODO Auto-generated method stub
        super.onPause();
        // SDK已经禁用了基于Activity 的页面统计，所以需要再次重新统计页面
        MobclickAgent.onPageEnd(mPageName);
    }
}
