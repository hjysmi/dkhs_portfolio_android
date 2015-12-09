/**
 * @Title FragmentNetValueTrend.java
 * @Package com.dkhs.portfolio.ui.fragment
 * @Description TODO(用一句话描述该文件做什么)
 * @author zjz
 * @date 2014-9-1 下午1:52:54
 * @version V1.0
 */
package com.dkhs.portfolio.ui.fragment;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.RelativeSizeSpan;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ImageView;
import android.widget.TextView;

import com.dkhs.portfolio.R;
import com.dkhs.portfolio.app.PortfolioApplication;
import com.dkhs.portfolio.bean.CombinationBean;
import com.dkhs.portfolio.bean.PositionDetail;
import com.dkhs.portfolio.bean.UserEntity;
import com.dkhs.portfolio.common.WeakHandler;
import com.dkhs.portfolio.engine.FollowComEngineImpl;
import com.dkhs.portfolio.engine.MyCombinationEngineImpl;
import com.dkhs.portfolio.engine.UserEngineImpl;
import com.dkhs.portfolio.engine.VisitorDataEngine;
import com.dkhs.portfolio.net.DKHSClient;
import com.dkhs.portfolio.net.DKHSUrl;
import com.dkhs.portfolio.net.DataParse;
import com.dkhs.portfolio.net.ParseHttpListener;
import com.dkhs.portfolio.ui.CombinationDetailActivity;
import com.dkhs.portfolio.ui.UserHomePageActivity;
import com.dkhs.portfolio.ui.eventbus.BusProvider;
import com.dkhs.portfolio.ui.eventbus.TitleChangeEvent;
import com.dkhs.portfolio.ui.eventbus.UpdateComDescEvent;
import com.dkhs.portfolio.ui.eventbus.UpdateCombinationEvent;
import com.dkhs.portfolio.ui.widget.HScrollTitleView;
import com.dkhs.portfolio.ui.widget.HScrollTitleView.ISelectPostionListener;
import com.dkhs.portfolio.ui.widget.MAlertDialog;
import com.dkhs.portfolio.utils.ImageLoaderUtils;
import com.dkhs.portfolio.utils.PromptManager;
import com.dkhs.portfolio.utils.StringFromatUtils;
import com.dkhs.portfolio.utils.WaterMarkUtil;
import com.lidroid.xutils.http.HttpHandler;
import com.squareup.otto.Subscribe;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.parceler.Parcels;

import java.io.File;
import java.io.FileOutputStream;
import java.util.List;

import cn.sharesdk.onekeyshare.OnekeyShare;

/**
 * @author zjz
 * @version 1.0
 * @ClassName FragmentNetValueTrend
 * @Description TODO(这里用一句话描述这个类的作用)
 * @date 2014-9-1 下午1:52:54
 */
public class FragmentNetValueTrend extends VisiableLoadFragment implements OnClickListener {
    private TextView tvIncreaseValue;
    private TextView tvIncreaseRatio;
    private CombinationBean mCombinationBean;
    private MyCombinationEngineImpl mMyCombinationEngineImpl;
//    MyPagerFragmentAdapter mPagerAdapter;

    public static final String ARGUMENT_ISFROM_ORDER = "argument_isfrom_order";
    private static final String TAG_TODAY = "today";
    private static final String TAG_SEVEN = "seven";
    private static final String TAG_MONTH = "month";
    private static final String TAG_HISTORY = "history";

    private TextView tvNetvalueDay;
    private TextView netvalueWeek;
    private TextView netvalueMonth;

    private TextView tvCreateUser;
    // private TextView tvCreate;
    private TextView tvFollowCount;
    private TextView tvComDesc;

    private View comView;
    private String type;
    //    private Timer mMarketTimer;
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


    private WeakHandler uiHandler = new WeakHandler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            PositionDetail detail = (PositionDetail) msg.obj;
            updateComView(detail);
            return false;
        }
    });


    private void updateComView(PositionDetail detail) {
        mPositionDetail = detail;


        if (null != mPositionDetail.getPortfolio()) {
            mCombinationBean = mPositionDetail.getPortfolio();
            if (!PortfolioApplication.hasUserLogin()) {
                CombinationBean comBean = new VisitorDataEngine().queryCombination(mCombinationBean.getId());
                if (null != comBean) {
                    mCombinationBean.setFollowed(comBean.isFollowed());
                }
            }
            BusProvider.getInstance().post(new UpdateCombinationEvent(mCombinationBean));

            tvNetvalueDay.setText(StringFromatUtils.get2PointPercent(mPositionDetail.getPortfolio()
                    .getChng_pct_day()));
            netvalueWeek.setText(StringFromatUtils.get2PointPercent(mPositionDetail.getPortfolio()
                    .getChng_pct_week()));
            netvalueMonth.setText(StringFromatUtils.get2PointPercent(mPositionDetail.getPortfolio()
                    .getChng_pct_month()));
            tvFollowCount.setText(mCombinationBean.getFollowerCount() + "");


//                    BitmapUtils bitmapUtils = new BitmapUtils(getActivity());

            UserEntity user = mCombinationBean.getUser();
            ImageLoaderUtils.setHeanderImage(user.getAvatar_md(), ivUserheader);
            //TODO 根据返回值判断加V图片的显示隐藏
            WaterMarkUtil.calWaterMarkImage(ivWaterMark, true, WaterMarkUtil.TYPE_RED);
//                    if (null != user.getAvatar_md() && user.getAvatar_md().length() > 35) {
////                        bitmapUtils.display(ivUserheader, user.getAvatar_md());
//                    }
//                    tvUName.setText(user.getUsername());
            if (TextUtils.isEmpty(user.getDescription())) {
                tvUserDesc.setText(getResources().getString(R.string.nodata_user_description));
            } else {
                tvUserDesc.setText(user.getDescription());
            }

        }
        setColor(myType);
    }

    /**
     * @param savedInstanceState
     * @Title
     * @Description TODO: (用一句话描述这个方法的功能)
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

        BusProvider.getInstance().register(this);
        listener = new QueryCombinationListener(uiHandler);
    }

    private boolean isMyCombination;

    private void handleExtras(Bundle extras) {
        mCombinationBean = Parcels.unwrap(extras.getParcelable(CombinationDetailActivity.EXTRA_COMBINATION));
        if (null != mCombinationBean && null != mCombinationBean.getUser() && mCombinationBean.getUser().getId() > 0) {

            if (null != UserEngineImpl.getUserEntity() && !TextUtils.isEmpty(UserEngineImpl.getUserEntity().getId() + "")) {
                if (mCombinationBean.getUser().getId() == UserEngineImpl.getUserEntity().getId()) {
                    isMyCombination = true;
                }
            }
        }

    }


    private void handleArguments(Bundle arguments) {
        // TODO Auto-generated method stub
        type = arguments.getString("type");
    }

    private ImageView ivUserheader;
    private ImageView ivWaterMark;
    private TextView tvUserDesc;

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        comView = view.findViewById(R.id.tv_combination_layout);
        mMyCombinationEngineImpl = new MyCombinationEngineImpl();
        tvIncreaseRatio = (TextView) view.findViewById(R.id.tv_income_netvalue);
        tvIncreaseValue = (TextView) view.findViewById(R.id.tv_history_netvalue);
        tvCreateUser = (TextView) view.findViewById(R.id.tv_combination_user);
        // tvCreate = (TextView) view.findViewById(R.id.tv_combination);
        tvFollowCount = (TextView) view.findViewById(R.id.tv_follow_num);
        tvComDesc = (TextView) view.findViewById(R.id.tv_desc_text);
        tvUserDesc = (TextView) view.findViewById(R.id.tv_user_desc);
        ivUserheader = (ImageView) view.findViewById(R.id.iv_avatar);
        ivWaterMark = (ImageView)view.findViewById(R.id.iv_water_mark);

        tvNetvalueDay = (TextView) view.findViewById(R.id.netvalue_day);
        // netvalueBtnDay = (Button) view.findViewById(R.id.netvalue_button_day);
        netvalueWeek = (TextView) view.findViewById(R.id.netvalue_week);
        netvalueMonth = (TextView) view.findViewById(R.id.netvalue_month);
        // btnAddOptional = (Button) view.findViewById(R.id.btn_add_optional);
        // btnAddOptional.setOnClickListener(this);
        // btnAddOptional.setVisibility(View.GONE);
//        QueryCombinationListener listener = new QueryCombinationListener();

        view.findViewById(R.id.rl_create_user).setOnClickListener(this);
        // listener.setLoadingDialog(getActivity());
        initTabPage(view);

        setupViewData();

    }


    @Override
    public void onViewHide() {
        super.onViewHide();


    }

    @Override
    public void onViewShow() {
        super.onViewShow();

        requestServer();

    }

    @Subscribe
    public void updateComName(UpdateComDescEvent event) {
        if (null != event) {
            if (!TextUtils.isEmpty(event.comName)) {
                mCombinationBean.setName(event.comName);
            }
            if (!TextUtils.isEmpty(event.comDesc)) {
                mCombinationBean.setDescription(event.comDesc);
            }
            setupViewData();

        }
    }


    static class OnComCheckListener implements OnCheckedChangeListener {
        private MyCombinationEngineImpl mMyCombinationEngineImpl;
        private String comId;

        public OnComCheckListener(MyCombinationEngineImpl combinationEngine, String id) {
            this.mMyCombinationEngineImpl = combinationEngine;
            this.comId = id;
        }

        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            // TODO Auto-generated method stub
            if (isChecked) {
                QueryCombinationDetailListener listener = new QueryCombinationDetailListener();
                mMyCombinationEngineImpl.changeCombinationIsPublic(comId, "0", listener);
                // listener.setLoadingDialog(getActivity());
            } else {
                QueryCombinationDetailListener listener = new QueryCombinationDetailListener();
                mMyCombinationEngineImpl.changeCombinationIsPublic(comId, "1", listener);
                // listener.setLoadingDialog(getActivity());
            }
        }

    }

    static class QueryCombinationDetailListener extends ParseHttpListener<List<CombinationBean>> {

        @Override
        protected List<CombinationBean> parseDateTask(String jsonData) {
            JSONArray jsonObject = null;
            try {
                jsonObject = new JSONArray(jsonData);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return DataParse.parseArrayJson(CombinationBean.class, jsonObject);
        }

        @Override
        protected void afterParseData(List<CombinationBean> object) {
            if (null != object) {

            }

        }
    }

    private void setupViewData() {
        tvCreateUser.setText(mCombinationBean.getUser().getUsername());
        tvComDesc.setText(mCombinationBean.getDefDescription());
        if (null != mCombinationBean) {
            updateIncreaseRatio(mCombinationBean.getNetvalue());
        }
    }

    WeakHandler updateHandler = new WeakHandler() {
        public void handleMessage(android.os.Message msg) {

//            replaceFragment(todayFragment);
//            replaceFragment();
        }
    };

    private void updateIncreaseRatio(float netValue) {
        tvIncreaseValue.setText(StringFromatUtils.get4Point(netValue));
        // tvIncreaseRatio.setTextColor(ColorTemplate.getUpOrDrownCSL(netValue - 1));
        String strPercent = StringFromatUtils.get2PointPercent((netValue - 1) * 100);
        SpannableString msp = new SpannableString(strPercent);
        msp.setSpan(new RelativeSizeSpan(0.6f), msp.length() - 1, msp.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE); // 2

        tvIncreaseRatio.setText(msp);
        BusProvider.getInstance().post(new TitleChangeEvent(netValue));
        netValue = netValue - 1;
//        if (netValue == 0) {
//            comView.setBackgroundResource(R.color.tag_gray);
//        } else if (netValue > 0) {
//            comView.setBackgroundResource(R.color.tag_red);
//        } else {
//            comView.setBackgroundResource(R.color.tag_green);
//        }
    }

    public void setColor(String type) {
        try {
            if (null != mPositionDetail) {
                updateIncreaseRatio(mPositionDetail.getPortfolio().getNetvalue());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public void showShare(boolean silent, String platform, boolean captureView) {
        // if (null != mtrendFragment)
        // // mtrendFragment.showShare(silent, platform, captureView);
        // mtrendFragment.showShareImage();
    }

    private String SHARE_IMAGE;

    public void showShareImage() {
        new Thread() {
            public void run() {

                // initImagePath();
                saveShareBitmap();
                shareHandler.sendEmptyMessage(999);
            }
        }.start();

    }


    private WeakHandler shareHandler = new WeakHandler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            showShare();
            return true;
        }
    });


    private void showShare() {
        if (null != mPositionDetail) {

            Context context = getActivity();
            final OnekeyShare oks = new OnekeyShare();

//            oks.(R.drawable.ic_launcher, mContext.getString(R.string.app_name));

            String shareUrl = DKHSClient.getAbsoluteUrl(DKHSUrl.User.share) + mCombinationBean.getId();
            oks.setTitleUrl(shareUrl);
            oks.setUrl(shareUrl);
            oks.setTitle(mCombinationBean.getName() + " 今日收益率");


            String customText;

            if (isMyCombination) {
                customText = "这是我的组合「" + mPositionDetail.getPortfolio().getName() + "」的收益率走势曲线。你也来创建属于你的组合吧。"
                ;
            } else {

                customText = "我发现这个谁牛组合「" + mPositionDetail.getPortfolio().getName() + "」的收益率走势不错哦，你也来看看吧!";
            }


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

    private void replaceFragment(Fragment fragment, String tag) {
        FragmentTransaction trasection = getChildFragmentManager().beginTransaction();
        TrendTodayChartFragment todayFragment;
        hideAll();

        if (!fragment.isAdded()) {
            try {
                trasection.add(R.id.rl_trend_layout, fragment, tag);

            } catch (Exception e) {
                // TODO: handle exception
                // AppConstants.printLog(e.getMessage());

            }
        } else {
            trasection.show(fragment);
        }
        trasection.commit();

    }

    private void hideAll() {
        FragmentTransaction trasection = getChildFragmentManager().beginTransaction();
        Fragment fragment = getChildFragmentManager().findFragmentByTag(TAG_TODAY);
        if (null != fragment && fragment.isAdded()) {
            trasection.hide(fragment);
        }
        fragment = getChildFragmentManager().findFragmentByTag(TAG_SEVEN);
        if (null != fragment && fragment.isAdded()) {
            trasection.hide(fragment);
        }
        fragment = getChildFragmentManager().findFragmentByTag(TAG_MONTH);
        if (null != fragment && fragment.isAdded()) {
            trasection.hide(fragment);
        }
        fragment = getChildFragmentManager().findFragmentByTag(TAG_HISTORY);
        if (null != fragment && fragment.isAdded()) {
            trasection.hide(fragment);
        }
        trasection.commit();

    }


    private void initTabPage(View view) {

        String[] titleArray = getResources().getStringArray(R.array.trend_title);
//        List<Fragment> fragmentList = new ArrayList<Fragment>();// ViewPager中显示的数据

        HScrollTitleView hsTitle = (HScrollTitleView) view.findViewById(R.id.hs_title);
        // String[] titleArray =
        // getResources().getStringArray(R.array.quotes_title);
        hsTitle.setTitleList(titleArray, getResources().getDimensionPixelSize(R.dimen.title_2text_length));
        hsTitle.setSelectPositionListener(titleSelectPostion);

//        if (null != type) {
//            switch (type) {
//                case CombinationRankEngineImpl.ORDER_DAY:
//                    // hsTitle.setSelectIndex(0);
//                    break;
//                case CombinationRankEngineImpl.ORDER_WEEK:
//                    hsTitle.setSelectIndex(1);
//                    break;
//                case CombinationRankEngineImpl.ORDER_MONTH:
//                    hsTitle.setSelectIndex(2);
//                    break;
//                case CombinationRankEngineImpl.ORDER_ALL:
//                    hsTitle.setSelectIndex(3);
//                    break;
//            }
//
//        }
    }

    ISelectPostionListener titleSelectPostion = new ISelectPostionListener() {

        @Override
        public void onSelectPosition(int position) {

            String type = TrendTodayChartFragment.TREND_TYPE_TODAY;
            Fragment curFragment = null;
            String tag = "";
            switch (position) {
                case 0: {
                    type = TrendTodayChartFragment.TREND_TYPE_TODAY;
                    tag = TAG_TODAY;
                    curFragment = getChildFragmentManager().findFragmentByTag(tag);
                    if (null == curFragment) {
                        curFragment = TrendTodayChartFragment.newInstance(TrendTodayChartFragment.TREND_TYPE_TODAY);
                    }

                }
                break;
                case 1: {
                    type = TrendSevenDayChartFragment.TREND_TYPE_SEVENDAY;
                    tag = TAG_SEVEN;
                    curFragment = getChildFragmentManager().findFragmentByTag(tag);
                    if (null == curFragment) {
                        curFragment = TrendSevenDayChartFragment
                                .newInstance(TrendSevenDayChartFragment.TREND_TYPE_SEVENDAY);
                    }
                }
                break;
                case 2: {
                    type = TrendMonthChartFragment.TREND_TYPE_MONTH;
                    tag = TAG_MONTH;
                    curFragment = getChildFragmentManager().findFragmentByTag(tag);
                    if (null == curFragment) {
                        curFragment = TrendMonthChartFragment
                                .newInstance(TrendMonthChartFragment.TREND_TYPE_MONTH);
                    }
                }
                break;
                case 3: {

                    type = TrendHistoryChartFragment.TREND_TYPE_HISTORY;
                    tag = TAG_HISTORY;
                    curFragment = getChildFragmentManager().findFragmentByTag(tag);
                    if (null == curFragment) {
                        curFragment = TrendHistoryChartFragment
                                .newInstance(TrendHistoryChartFragment.TREND_TYPE_HISTORY);
                    }
                }
                break;

                default:
                    break;
            }
            myType = type;
            setColor(type);
            if (null != curFragment) {
                replaceFragment(curFragment, tag);
            }

        }
    };


    @Override
    public void onClick(View v) {

        if (v.getId() == R.id.rl_create_user) {
            startActivity(UserHomePageActivity.getIntent(getActivity(), mCombinationBean.getUser().getUsername(),
                    mCombinationBean.getUser().getId() + ""));
        }
    }

    private void delFollowCombinatio() {
        if (PortfolioApplication.hasUserLogin()) {

            if (mCombinationBean.isFollowed()) {
                new FollowComEngineImpl().defFollowCombinations(mCombinationBean.getId(), followComListener);
            } else {

                new FollowComEngineImpl().followCombinations(mCombinationBean.getId(), followComListener);
            }
        } else {
            if (mCombinationBean.isFollowed()) {
                mCombinationBean.setFollowed(false);
                new VisitorDataEngine().delCombinationBean(mCombinationBean);
                delFollowSuccess();
            } else {
                mCombinationBean.setFollowed(true);
                new VisitorDataEngine().saveCombination(mCombinationBean);
                addFollowSuccess();
            }
            // btnAddOptional.setEnabled(true);
            // addOptionalButton(mCombinationBean.isFollowed());
        }
    }

    private void delFollowSuccess() {
        PromptManager.showToast(R.string.msg_def_follow_success);

        mCombinationBean.setFollowerCount(mCombinationBean.getFollowerCount() - 1);
        tvFollowCount.setText(mCombinationBean.getFollowerCount() + "");
    }

    private void addFollowSuccess() {
        PromptManager.showToast(R.string.msg_follow_success);
        mCombinationBean.setFollowerCount(mCombinationBean.getFollowerCount() + 1);
        tvFollowCount.setText(mCombinationBean.getFollowerCount() + "");
    }

    public void showDelDialog() {

        MAlertDialog builder = PromptManager.getAlertDialog(getActivity());
        builder.setMessage(R.string.dialog_message_delfollow_combination);
        // builder.setTitle(R.string.tips);
        builder.setPositiveButton(R.string.confirm, new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                delFollowCombinatio();
            }

        });

        builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                // btnAddOptional.setEnabled(true);
            }
        });
        builder.create().show();

    }

    ParseHttpListener followComListener = new ParseHttpListener<Object>() {

        @Override
        public void requestCallBack() {
            super.requestCallBack();
            // btnAddOptional.setEnabled(true);
        }


        @Override
        protected Object parseDateTask(String jsonData) {
            // TODO Auto-generated method stub
            return null;
        }

        @Override
        protected void afterParseData(Object object) {
            mCombinationBean.setFollowed(!mCombinationBean.isFollowed());
            // addOptionalButton(mCombinationBean.isFollowed());
            // BusProvider.getInstance().post(new UpdateCombinationEvent(mCombinationBean));
            if (mCombinationBean.isFollowed()) {
                addFollowSuccess();
            } else {
                delFollowSuccess();
            }
        }
    };


    @Override
    public void requestData() {

    }

    @Override
    public void onResume() {

        super.onResume();
        requestServer();
    }

    @Override
    public void onStop() {
        super.onStop();


    }

    private HttpHandler mHttpHandler;
    private QueryCombinationListener listener;


    private void requestServer() {
        if (null != mHttpHandler) {
            mHttpHandler.cancel();
        }
        mHttpHandler = mMyCombinationEngineImpl.queryCombinationDetail(mCombinationBean.getId(), listener);
    }

    private static final String TAG = FragmentNetValueTrend.class.getSimpleName();

    private static class QueryCombinationListener extends ParseHttpListener<PositionDetail> {

        private WeakHandler handler;

        public QueryCombinationListener(WeakHandler handler) {
            this.handler = handler;
        }

        @Override
        protected PositionDetail parseDateTask(String jsonData) {
            JSONObject jsonObject = null;
            try {
                jsonObject = new JSONObject(jsonData);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return DataParse.parseObjectJson(PositionDetail.class, jsonObject);
        }

        @Override
        protected void afterParseData(PositionDetail object) {
            if (null != object) {

                Message msg = new Message();
                msg.obj = object;
                handler.sendMessage(msg);


            }

        }
    }


    private final String mPageName = PortfolioApplication.getInstance().getString(R.string.count_fund_order_line);

    @Override
    public void onPause() {
        super.onPause();
        if (null != mHttpHandler) {
            mHttpHandler.cancel();
        }

    }

    @Override
    public int setContentLayoutId() {
        return R.layout.fragment_netvalue_trend;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        BusProvider.getInstance().unregister(this);
        mCombinationBean = null;
        mPositionDetail = null;
    }
}
