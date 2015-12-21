/**
 * @Title TabFundsFragment.java
 * @Package com.dkhs.portfolio.ui.fragment
 * @Description TODO(用一句话描述该文件做什么)
 * @author zjz
 * @date 2015-2-7 上午11:03:26
 * @version V1.0
 */
package com.dkhs.portfolio.ui.fragment;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.TextView;

import com.baidu.mobstat.StatService;
import com.dkhs.portfolio.R;
import com.dkhs.portfolio.bean.FundManagerSortMenuBean;
import com.dkhs.portfolio.bean.FundTypeMenuBean;
import com.dkhs.portfolio.bean.MenuBean;
import com.dkhs.portfolio.ui.SelectGeneralActivity;
import com.dkhs.portfolio.ui.eventbus.BusProvider;
import com.dkhs.portfolio.ui.eventbus.IDataUpdateListener;
import com.dkhs.portfolio.ui.eventbus.NewIntent;
import com.dkhs.portfolio.ui.widget.MenuChooserRelativeLayout;
import com.dkhs.portfolio.ui.widget.MultiChooserRelativeLayout;
import com.dkhs.portfolio.ui.widget.TabPageIndicator;
import com.dkhs.portfolio.utils.StockUitls;
import com.dkhs.portfolio.utils.UIUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.squareup.otto.Subscribe;
import com.umeng.analytics.MobclickAgent;

import java.util.ArrayList;
import java.util.LinkedList;

/**
 * @author zjz
 * @version 1.0
 * @ClassName TabFundFragment
 * @Description TODO(基金tab Fragment)
 * @date 2015-2-7 上午11:03:26
 */
public class MarketFundsFragment extends VisiableLoadFragment implements IDataUpdateListener, OnClickListener{

    public static final Integer[] PERCENT_TITLE_IDS = new Integer[]{R.string.percent_day,
            R.string.percent_year,
            R.string.percent_month,
            R.string.percent_season,
            R.string.percent_six_month,
            R.string.percent_tyear,
            R.string.office_tenure};

    public static final String TAG = "MarketFundsFragment";
    @ViewInject(R.id.rl_menu)
    ViewGroup menuRL;
    private MultiChooserRelativeLayout fundTypeMenuChooserL;
    private MenuChooserRelativeLayout sortTypeMenuChooserL;
    @ViewInject(R.id.tv_fund_type)
    private TextView fundTypeTV;

    @ViewInject(R.id.view_stock_title)
    private View titleView;
    @ViewInject(R.id.rootView)
    private ViewGroup mRootView;
    @ViewInject(R.id.pager)
    private ViewPager mPager;
    @ViewInject(R.id.indicator)
    private TabPageIndicator mPageIndicator;
    @ViewInject(R.id.rl_fund_type)
    private View mFundTypeView;
    private LinkedList<MenuBean> sorts;
    private String mFundType;
    private boolean mShowCanBuy;//仅显示可购买

    private static int defaultIndex = 1;//默认显示周战斗指数

    @Override
    public int setContentLayoutId() {
        return R.layout.fragment_market_funds;
    }

    private static final String MARKET_TYPE = "market_type";
    private MarketSubpageFragment.SubpageType curType;
    private String mSort;
    private ArrayList<Fragment> fragments;
    private String[] fundSorts;
    private ViewPager.OnPageChangeListener listener = new ViewPager.OnPageChangeListener() {
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageSelected(int position) {
            switchFundType(position, mFundType,mShowCanBuy);
        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    };


    public static MarketFundsFragment getFragment(int type) {
        MarketFundsFragment fragment = new MarketFundsFragment();
        Bundle args = new Bundle();
        args.putInt(MARKET_TYPE, type);
        fragment.setArguments(args);
        return fragment;
    }

    private FundOrderFragment loadDataListFragment;
    private FundManagerRankingsFragment fundManagerRankingsFragment;


    public interface OnRefreshI {
        public void refresh(String type, String sort);
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getArguments();
        if (null != bundle) {
            curType = MarketSubpageFragment.SubpageType.valueOf(bundle.getInt(MARKET_TYPE));
        }
    }

    private void initViewPager() {
        fragments = new ArrayList<>();
        fundSorts = getResources().getStringArray(R.array.fund_sort_values);
        String[] managerSorts;
        managerSorts = getResources().getStringArray(R.array.fund_manager_sort_values);
        for (int i = 0; i < PERCENT_TITLE_IDS.length; i++) {
            if (curType.compareTo(MarketSubpageFragment.SubpageType.TYPE_FUND_MANAGER_RANKING_WEEK) == 0) {
                FundManagerRankingsFragment fg = FundManagerRankingsFragment.newInstant(fundTypeMenuChooserL.getSelectItem().getValue(), managerSorts[i].substring(0));
                fragments.add(fg);
            } else {
                FundOrderFragment fg = FundOrderFragment.newInstant(fundTypeMenuChooserL.getSelectItem().getValue(), fundSorts[i].substring(0));
                fragments.add(fg);
            }
        }
        FragmentPagerAdapter adapter = new MyPagerAdapter(getActivity().getSupportFragmentManager(), fragments);
        mPager.setOffscreenPageLimit(3);
        if (curType.compareTo(MarketSubpageFragment.SubpageType.TYPE_FUND_MANAGER_RANKING_WEEK) != 0) {
            mPageIndicator.setOnPageChangeListener(listener);
        }
        mPager.setAdapter(adapter);
        mPageIndicator.setViewPager(mPager);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        BusProvider.getInstance().register(this);
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void onDestroyView() {
        BusProvider.getInstance().unregister(this);
        super.onDestroyView();
    }

    private void handIntent(Bundle bundle) {
        if (bundle.containsKey("fund_manager_ranking")) {
            boolean fundManagerRanking = bundle.getBoolean("fund_manager_ranking", true);
            if (fundManagerRanking) {
                fundTypeMenuChooserL.setFundManagerRanking();
            } else {
                fundTypeMenuChooserL.setFundsAllRanking();
            }
        }
    }


    @Subscribe
    public void newIntent(NewIntent newIntent) {
        handIntent(newIntent.bundle);
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        initView(getView());

        super.onViewCreated(view, savedInstanceState);
//        if (getActivity() instanceof MainActivity) {
//            final Bundle bundle = ((MainActivity) getActivity()).mBundle;
//            if (bundle != null) {
//                mRootView.postDelayed(new Runnable() {
//                    @Override
//                    public void run() {
//                        handIntent(bundle);
//
//                    }
//                }, 1200);
//            }
//        }
        //
        loadData();
        initViewPager();
    }

    @Override
    public void requestData() {

    }

    private void loadData() {
        switch (curType) {
            case TYPE_FUND_MANAGER_RANKING_WEEK:
                mFundTypeView.setVisibility(View.GONE);
                break;
            case TYPE_FUND_ALL_RANKING_MONTH:
                defaultIndex = sortTypeMenuChooserL.FUNDS_INDEX_MONTH;
                fundTypeMenuChooserL.setFundsAllRanking();
                break;
            case TYPE_FUND_ALL_RANKING_YEAR:
                defaultIndex = sortTypeMenuChooserL.FUNDS_INDEX_YEAR;
                fundTypeMenuChooserL.setFundsAllRanking();
                break;
            case TYPE_FUND_MIXED_MONTH:
                fundTypeMenuChooserL.setFundsMixedRanking();
                break;
        }
    }


    @Override
    public void onViewShow() {

        super.onViewShow();
        StatService.onPageStart(getActivity(), TAG);
        MobclickAgent.onPageStart(this.getClass().getSimpleName());
    }

    public void setUserVisibleHint(boolean isVisibleToUser) {

        if (isVisibleToUser && isVisible()) {
            if (getView() != null) {
                onViewShow();
            }
        }
        super.setUserVisibleHint(isVisibleToUser);
    }


    public void initView(View view) {
        fundTypeMenuChooserL = new MultiChooserRelativeLayout(getActivity());
//        sortTypeMenuChooserL = new MenuChooserRelativeLayout(getActivity());
//        sortTypeMenuChooserL.setParentView(menuRL);
        fundTypeMenuChooserL.setParentView(menuRL);

        LinkedList<MenuBean> types = MenuBean.fundTypeFromXml(getActivity());
        sorts = MenuBean.fundManagerSortFromXml(getActivity());

        fundTypeMenuChooserL.setData(types, MenuBean.fundManagerFromXml(getActivity()));
//        String type = types.getFirst().getValue();
//        String sort = sorts.get(defaultIndex).getValue();

//        sortTypeMenuChooserL.setData(sorts);
        setDrawableDown(fundTypeTV);
//        setDrawableDown(tvPercentgae);
//        tvPercentgae.setText(R.string.win_rate_week);
//        tvCurrent.setText(R.string.join_time);
//        fundTypeTV.setText(R.string.fund_manager_order);
//        sortKeyFormatStr = mActivity.getString(R.string.win_rate_format);
//        sortTypeMenuChooserL.setSelectItem(defaultIndex);

//        replaceFundManagerRankingsDataList(type, sort);

    }


    @OnClick({R.id.tv_current, R.id.tv_increase, R.id.rl_fund_type})
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.tv_current:
                break;
            case R.id.rl_fund_type: {
                mPageIndicator.setVisibility(View.INVISIBLE);
                boolean select = fundTypeMenuChooserL.getVisibility() == View.GONE;
                fundTypeMenuChooserL.toggle();
                setViewOrderIndicator(fundTypeTV, select);
            }
            break;
            case R.id.btn_refresh: {
                refreshUI();
            }
            break;
            case R.id.btn_search:
//                UIUtils.startAnimationActivity(getActivity(),  new Intent(getActivity(), SelectGeneralActivity.class));
                Intent intent = new Intent(getActivity(), SelectGeneralActivity.class);
                UIUtils.startAnimationActivity(getActivity(), intent);
                break;
            default:
                break;
        }

    }

    String sortKeyFormatStr;

    @Subscribe
    public void update(MenuBean menuBean) {

        if (menuBean instanceof FundTypeMenuBean) {
            switchFundType(mPager.getCurrentItem(), menuBean.getValue(), false);
            fundTypeTV.setText(menuBean.getKey());
            FundTypeMenuBean type = (FundTypeMenuBean) menuBean;
            sortKeyFormatStr = "%s";
            /**
             * (306, '货币型','hb'),
             (307, '理财型','lc'),
             */
            if (!StockUitls.isSepFund(type.getCode())) {
                if (defaultIndex != 1) {
                    defaultIndex = 1;
                }
            }
        }


    }

    private void switchFundType(int position, String fundType,boolean showCanBuy) {
        mFundType = fundType;
        mShowCanBuy = showCanBuy;
        if (fragments != null && fragments.size() >= position) {
            Fragment fg = fragments.get(position);
            if (fg instanceof FundOrderFragment) {
                ((FundOrderFragment) fg).setType(fundType,showCanBuy);
            }
        }
    }

    private void refresh() {


        if (fundTypeMenuChooserL.getSelectItem() instanceof FundManagerSortMenuBean) {

            replaceFundManagerRankingsDataList(fundTypeMenuChooserL.getSelectItem().getValue(), sortTypeMenuChooserL.getSelectItem().getValue());

        } else {

            replaceFundDataList(fundTypeMenuChooserL.getSelectItem().getValue(), sortTypeMenuChooserL.getSelectItem().getValue());
        }

    }

    private void refreshUI() {
        int position = mPager.getCurrentItem();
        if (fragments != null && fragments.size() >= position) {
            Fragment fg = fragments.get(position);
            if (fg instanceof FundOrderFragment) {
                ((FundOrderFragment) fg).refresh();
            } else if (fg instanceof FundManagerRankingsFragment) {
                ((FundManagerRankingsFragment) fg).refresh();
            }
        }
    }


    private void replaceFundManagerRankingsDataList(String type, String sort) {
        if (loadDataListFragment != null) {
            getChildFragmentManager().beginTransaction().detach(loadDataListFragment).commitAllowingStateLoss();
        }
        if (fundManagerRankingsFragment == null) {
            fundManagerRankingsFragment = FundManagerRankingsFragment.newInstant(type, mSort);
            getChildFragmentManager().beginTransaction().add(R.id.view_datalist, fundManagerRankingsFragment, "fundManagerRankingsFragment").commitAllowingStateLoss();
        } else {
            getChildFragmentManager().beginTransaction().attach(fundManagerRankingsFragment).commitAllowingStateLoss();
            fundManagerRankingsFragment.refresh(type, mSort);
        }
    }

    private void replaceFundDataList(String type, String sort) {

        if (fundManagerRankingsFragment != null) {
            getChildFragmentManager().beginTransaction().detach(fundManagerRankingsFragment).commitAllowingStateLoss();
        }
        if (loadDataListFragment == null) {
            loadDataListFragment = FundOrderFragment.newInstant(type, mSort);
            getChildFragmentManager().beginTransaction().add(R.id.view_datalist, loadDataListFragment, "loadDataListFragment").commitAllowingStateLoss();
        } else {
            getChildFragmentManager().beginTransaction().attach(loadDataListFragment).commitAllowingStateLoss();
            loadDataListFragment.refresh(type, sort);
        }

    }


    @Subscribe
    public void menuRLdismiss(MenuChooserRelativeLayout menuChooserRelativeLayout) {
        if (menuChooserRelativeLayout == sortTypeMenuChooserL) {
//            setDrawableDown(tvPercentgae);
        } else {
            setDrawableDown(fundTypeTV);
        }

    }

    @Subscribe
    public void menuRLdismiss(MultiChooserRelativeLayout menuChooserRelativeLayout) {

        setDrawableDown(fundTypeTV);
        mPageIndicator.setVisibility(View.VISIBLE);
    }


    private void setDrawableUp(TextView view) {
        view.setSelected(true);
        Drawable drawable = getResources().getDrawable(R.drawable.menu_arrow_up);
        drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
        view.setCompoundDrawables(null, null, drawable, null);
        view.setCompoundDrawablePadding(getResources().getDimensionPixelOffset(R.dimen.text_drawable_margin));
    }

    private void setDrawableDown(TextView view) {
        view.setSelected(false);
        Drawable drawable = getResources().getDrawable(R.drawable.menu_arrow_down);
        drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
        view.setCompoundDrawables(null, null, drawable, null);
        view.setCompoundDrawablePadding(getResources().getDimensionPixelOffset(R.dimen.text_drawable_margin));
    }

    private TextView viewLastClick;

    private void setViewOrderIndicator(TextView currentSelectView, boolean select) {
        currentSelectView.setSelected(select);
        if (select) {
            setDrawableUp(currentSelectView);
        }
    }

    @Override
    public void onViewHide() {
        super.onViewHide();
        StatService.onPageEnd(getActivity(), TAG);
        MobclickAgent.onPageEnd(this.getClass().getSimpleName());
    }


    @Override
    public void dataUpdate(boolean isEmptyData) {
        if (isEmptyData) {
            titleView.setVisibility(View.GONE);
        } else {
            titleView.setVisibility(View.VISIBLE);
        }
    }

    class MyPagerAdapter extends FragmentPagerAdapter {
        private ArrayList<Fragment> mFragmentList;

        public MyPagerAdapter(FragmentManager fm, ArrayList<Fragment> fragments) {
            super(fm);
            mFragmentList = fragments;
        }

        @Override
        public Fragment getItem(int position) {
            return ((mFragmentList == null || mFragmentList.size() < 0) ? null : mFragmentList.get(position));
        }


        @Override
        public CharSequence getPageTitle(int position) {
            int index = position % PERCENT_TITLE_IDS.length;
            return getActivity().getString(PERCENT_TITLE_IDS[index]);
        }

        @Override
        public int getCount() {
            return PERCENT_TITLE_IDS.length;
        }

    }


}
