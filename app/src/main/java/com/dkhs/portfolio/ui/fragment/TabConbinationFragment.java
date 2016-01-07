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
import android.support.v4.widget.SwipeRefreshLayout;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.baidu.mobstat.StatService;
import com.dkhs.portfolio.R;
import com.dkhs.portfolio.bean.CombinationBean;
import com.dkhs.portfolio.bean.MoreDataBean;
import com.dkhs.portfolio.engine.FollowComListEngineImpl;
import com.dkhs.portfolio.engine.LoadMoreDataEngine.ILoadDataBackListener;
import com.dkhs.portfolio.engine.UserCombinationEngineImpl;
import com.dkhs.portfolio.ui.CombinationDetailActivity;
import com.dkhs.portfolio.ui.EditTabCombinationActivity;
import com.dkhs.portfolio.ui.PositionAdjustActivity;
import com.dkhs.portfolio.ui.adapter.TabFundsAdapter;
import com.dkhs.portfolio.ui.eventbus.BusProvider;
import com.dkhs.portfolio.ui.eventbus.IDataUpdateListener;
import com.dkhs.portfolio.ui.eventbus.TabComTitleChangeEvent;
import com.dkhs.portfolio.ui.eventbus.TopEvent;
import com.dkhs.portfolio.ui.widget.PullToRefreshListView;
import com.dkhs.portfolio.utils.PromptManager;
import com.dkhs.portfolio.utils.UIUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.squareup.otto.Subscribe;
import com.umeng.analytics.MobclickAgent;

import java.util.ArrayList;
import java.util.List;

/**
 * @author zjz
 * @version 1.0
 * @ClassName TabFundsFragment
 * @Description TODO(这里用一句话描述这个类的作用)
 * @date 2015-2-7 上午11:03:26
 */
public class TabConbinationFragment extends VisiableLoadFragment implements IDataUpdateListener, OnClickListener {
    public static final String TAG = "TabConbinationFragment";
    @ViewInject(R.id.tv_current)
    private TextView tvCurrent;
    // @ViewInject(R.id.tv_increase)
    // private TextView tvChange;
    @ViewInject(R.id.tv_percentage)
    private TextView tvPercentgae;

    @ViewInject(R.id.view_stock_title)
    private View titleView;
    @ViewInject(R.id.rootView)
    private View rootView;

    private TabFundsAdapter mFundsAdapter;
    private List<CombinationBean> mDataList = new ArrayList<CombinationBean>();
    private FollowComListEngineImpl dataEngine;
    private String mUserId;

    public static TabConbinationFragment getFragment(String userId) {
        TabConbinationFragment fragment = new TabConbinationFragment();
        Bundle args = new Bundle();
        args.putString(FragmentSelectStockFund.ARGUMENT_USER_ID, userId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        // if (null == dataEngine) {
        Bundle bundle = getArguments();

        if (null != bundle) {
            mUserId = bundle.getString(FragmentSelectStockFund.ARGUMENT_USER_ID);

        }

        dataEngine = new FollowComListEngineImpl(new ILoadDataBackListener<CombinationBean>() {

            @Override
            public void loadFinish(MoreDataBean<CombinationBean> object) {
                mSwipeLayout.setRefreshing(false);
                if (null != object.getResults()) {
                    if (!UIUtils.roundAble(object.getStatu())) {
                    }
                    mDataList.clear();

                    mDataList.addAll(object.getResults());
                    mFundsAdapter.notifyDataSetChanged();
                }
                refreshEditView();
            }

            @Override
            public void loadFail() {
                mSwipeLayout.setRefreshing(false);
            }
        }, mUserId);

    }

    @Override
    public int setContentLayoutId() {
        return R.layout.fragment_tab_mycombina;
    }

    /**
     * @return
     * @Title
     * @Description TODO: (用一句话描述这个方法的功能)
     */
    @Override
    public void onResume() {
        // TODO Auto-generated method stub
        super.onResume();
        BusProvider.getInstance().register(this);

        refreshEditView();
        refresh();

    }

    @Override
    public void onPause() {
        super.onPause();
        BusProvider.getInstance().unregister(this);
    }

    public void refreshEditView() {
        if (null != mDataList && !mDataList.isEmpty()) {
            dataUpdate(false);
        } else {
            dataUpdate(true);
        }

    }

    public void setDataUpdateListener(IDataUpdateListener listen) {
        this.dataUpdateListener = listen;
    }

    private IDataUpdateListener dataUpdateListener;

    // @OnClick({ R.id.tv_current, R.id.tv_percentage, R.id.tv_increase })
    @Override
    public void onClick(View v) {
        int id = v.getId();
        // if (!PortfolioApplication.hasUserLogin()) {
        // return;
        // }
        switch (id) {
            case R.id.tv_current: {
                setViewOrderIndicator(tvCurrent);
            }
            break;
            case R.id.tv_percentage: {
                setViewOrderIndicator(tvPercentgae);

            }
            break;
            // case R.id.tv_increase: {
            // setViewOrderIndicator(tvChange);
            // }
            // break;

            default:
                break;
        }

        // if (null != loadDataListFragment && !TextUtils.isEmpty(orderType)) {
        // isLoading = true;
        // loadDataListFragment.setOptionalOrderType(orderType);
        // }
        dataEngine.setOrderType(orderType);
        refresh();

    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onViewCreated(view, savedInstanceState);
        replaceDataList();
        initView(view);
//        if (PortfolioApplication.hasUserLogin()) {
//            refresh();
//        }
    }

    @Override
    public void onViewShow() {
        StatService.onPageStart(getActivity(), TAG);
        MobclickAgent.onPageStart(this.getClass().getSimpleName());


    }

    @Override
    public void onViewHide() {
        StatService.onPageEnd(getActivity(), TAG);
        MobclickAgent.onPageEnd(this.getClass().getSimpleName());
    }

    @Override
    public void requestData() {

    }

    protected PullToRefreshListView mListView;
    private RelativeLayout pb;
    public SwipeRefreshLayout mSwipeLayout;

    private void initView(View view) {
        rootView.setBackgroundColor(getResources().getColor(R.color.white));
        mListView = (PullToRefreshListView) view.findViewById(android.R.id.list);
        mListView.setDividerHeight(0);
        mFundsAdapter = new TabFundsAdapter(getActivity(), mDataList);
        mListView.setAdapter(mFundsAdapter);
//        mListView.setDividerHeight(0);
        mListView.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // TODO Auto-generated method stub
                // getActivity().startActivity(
                // OrderFundDetailActivity.newIntent(getActivity(), mDataList.get(position), true,
                // FundsOrderFragment.ORDER_TYPE_DAY));

                startActivity(CombinationDetailActivity.newIntent(getActivity(), mDataList.get(position)));

            }
        });
        TextView emptyview = (TextView) view.findViewById(R.id.add_data);
        emptyview.setText(R.string.click_creat_combina);
        emptyview.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // Intent intent = new Intent(getActivity(), SelectAddOptionalActivity.class);
                // startActivity(intent);
                if (!UIUtils.iStartLoginActivity(getActivity())) {
                    addItem();
                }

            }
        });
        mListView.setEmptyView(emptyview);
        mSwipeLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipe_container);
        // mSwipeLayout.setOnRefreshListener(this);
        mSwipeLayout.setColorSchemeResources(R.color.theme_blue);
        mSwipeLayout.setOnRefreshListener(new android.support.v4.widget.SwipeRefreshLayout.OnRefreshListener() {

            @Override
            public void onRefresh() {
                refresh();

            }
        });

        tvCurrent.setOnClickListener(this);
        tvPercentgae.setOnClickListener(this);

    }

    public void addItem() {
        if (null != dataEngine) {

            if (dataEngine.getMoreDataBean() != null && dataEngine.getMoreDataBean().getPortfoliosCount() >= 20) {
                PromptManager.showShortToast(R.string.more_combination_tip);
            } else {
                getActivity().startActivity(PositionAdjustActivity.newIntent(getActivity(), null));
            }
        }

    }

    public void editFund() {
        if (!mDataList.isEmpty()) {
            // startActivity(EditTabFundActivity.newIntent(getActivity(), mDataList));
            startActivityForResult(EditTabCombinationActivity.getIntent(getActivity()), 1722);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 1722 && null != viewLastClick) {
            setDefType(viewLastClick);
            dataEngine.setOrderType(orderType);
        }
//        refresh();

    }

    public void refresh() {
        // isRefresh = true;
        if (null != dataEngine) {

            dataEngine.loadAllData();
        }
        // UserCombinationEngineImpl.loadAllData(this);

    }

    private void replaceDataList() {
        // view_datalist
        // if (null == loadDataListFragment) {
        // loadDataListFragment = FragmentSelectStockFund.getStockFragment(StockViewType.STOCK_OPTIONAL_PRICE);
        // }
        // getChildFragmentManager().beginTransaction().replace(R.id.view_datalist, new MyCombinationListFragment())
        // .commit();
    }

    private TextView viewLastClick;
    private String orderType = FollowComListEngineImpl.ORDER_DEFALUT;

    // private final String typeCurrentUp = "current";
    // private final String typePercentageUp = "percentage";
    // // 涨跌
    // private final String typeChangeUP = "change";
    // private final String typeCurrentDown = "-current";
    // private final String typePercentageDown = "-percentage";
    // // 涨跌
    // private final String typeChangeDown = "-change";

    private void setDrawableUp(TextView view) {

        Drawable drawable = getResources().getDrawable(R.drawable.market_icon_up);
        drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
        view.setCompoundDrawables(null, null, drawable, null);
        view.setCompoundDrawablePadding(getResources().getDimensionPixelOffset(R.dimen.text_drawable_margin));
    }

    private void setDrawableDown(TextView view) {
        // orderType = typeCurrentDown;
        Drawable drawable = getResources().getDrawable(R.drawable.market_icon_down);
        drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
        view.setCompoundDrawables(null, null, drawable, null);
        view.setCompoundDrawablePadding(getResources().getDimensionPixelOffset(R.dimen.text_drawable_margin));
    }

    private void setTextDrawableHide(TextView view) {
        // Drawable drawable = getResources().getDrawable(R.drawable.market_icon_down);
        // drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
        view.setCompoundDrawables(null, null, null, null);
        // tvCurrent.setCompoundDrawablePadding(getResources().getDimensionPixelOffset(R.dimen.text_drawable_margin));

    }

    private void setViewOrderIndicator(TextView currentSelectView) {
        if (null == viewLastClick) {

            setDownType(currentSelectView);
        } else if (viewLastClick != currentSelectView) {
            setTextDrawableHide(viewLastClick);
            setDownType(currentSelectView);
        } else if (viewLastClick == currentSelectView) {

            if (isDefOrder(orderType)) {
                setDownType(currentSelectView);
            } else if (isDownOrder(orderType)) {
                setUpType(currentSelectView);
            } else {
                setDefType(currentSelectView);
            }
        }
        viewLastClick = currentSelectView;
    }

    private boolean isUpOrder(String orderType) {
        return !TextUtils.isEmpty(orderType)
                && (orderType.equals(FollowComListEngineImpl.ORDER_DAY_UP)
                || orderType.equals(UserCombinationEngineImpl.ORDER_NET_VALUE_UP)
                || orderType.equals(FollowComListEngineImpl.ORDER_WEEK_UP) || orderType
                .equals(FollowComListEngineImpl.ORDER_MONTH_UP));
    }

    private boolean isDownOrder(String orderType) {
        return !TextUtils.isEmpty(orderType)
                && (orderType.equals(FollowComListEngineImpl.ORDER_DAY_DOWN)
                || orderType.equals(UserCombinationEngineImpl.ORDER_NET_VALUE_DOWN)
                || orderType.equals(FollowComListEngineImpl.ORDER_WEEK_DOWN) || orderType
                .equals(FollowComListEngineImpl.ORDER_MONTH_DOWN));
    }

    private boolean isDefOrder(String orderType) {
        if (TextUtils.isEmpty(orderType)) {
            return true;
        }
        // if (orderType.equals(UserCombinationEngineImpl.ORDER_DEFALUT)) {
        // return true;
        // }
        return false;
    }

    private boolean isPercentType(String type) {
        return !TextUtils.isEmpty(orderType)
                && (orderType.equals(FollowComListEngineImpl.ORDER_DAY_UP)
                || orderType.equals(FollowComListEngineImpl.ORDER_DAY_DOWN)
                || orderType.equals(FollowComListEngineImpl.ORDER_WEEK_UP) || orderType
                .equals(FollowComListEngineImpl.ORDER_WEEK_DOWN))
                || orderType.equals(FollowComListEngineImpl.ORDER_MONTH_UP)
                || orderType.equals(FollowComListEngineImpl.ORDER_MONTH_DOWN);
    }

    private int lastPercentTextIds = 0;

    private void setDownType(TextView currentSelectView) {
        if (currentSelectView == tvCurrent) {
            orderType = UserCombinationEngineImpl.ORDER_NET_VALUE_DOWN;
        } else if (currentSelectView == tvPercentgae) {
            // orderType = UserCombinationEngineImpl.ORDER_CUMULATIVE_DOWN;
            if (tvPercentgae.getText().equals(getString(R.string.day_income))) {
                // 涨跌幅
                orderType = FollowComListEngineImpl.ORDER_DAY_DOWN;
                lastPercentTextIds = R.string.day_income;
            } else if (tvPercentgae.getText().equals(getString(R.string.week_income))) {
                // 涨跌额
                orderType = FollowComListEngineImpl.ORDER_WEEK_DOWN;
                lastPercentTextIds = R.string.week_income;

            } else if (tvPercentgae.getText().equals(getString(R.string.month_income))) {
                // 总市值
                orderType = FollowComListEngineImpl.ORDER_MONTH_DOWN;
                lastPercentTextIds = R.string.month_income;

            }

        }
        setDrawableDown(currentSelectView);
    }

    private void setUpType(TextView currentSelectView) {
        if (currentSelectView == tvCurrent) {
            orderType = UserCombinationEngineImpl.ORDER_NET_VALUE_UP;
        } else if (currentSelectView == tvPercentgae) {
            // orderType = UserCombinationEngineImpl.ORDER_CUMULATIVE_UP;
            if (tvPercentgae.getText().equals(getString(R.string.day_income))) {
                // 涨跌幅
                orderType = FollowComListEngineImpl.ORDER_DAY_UP;
                lastPercentTextIds = R.string.day_income;
            } else if (tvPercentgae.getText().equals(getString(R.string.week_income))) {
                // 涨跌额
                orderType = FollowComListEngineImpl.ORDER_WEEK_UP;
                lastPercentTextIds = R.string.week_income;

            } else if (tvPercentgae.getText().equals(getString(R.string.month_income))) {
                // 总市值
                orderType = FollowComListEngineImpl.ORDER_MONTH_UP;
                lastPercentTextIds = R.string.month_income;

            }
        }
        setDrawableUp(currentSelectView);
    }

    private void setDefType(TextView currentSelectView) {
        // if (currentSelectView == tvCurrent) {
        // orderType = "";
        // } else if (currentSelectView == tvChange) {
        // orderType = TYPE_CHANGE_DEF;
        // } else if (currentSelectView == tvPercentgae) {
        // orderType = TYPE_PERCENTAGE_DEF;
        // }
        orderType = FollowComListEngineImpl.ORDER_DEFALUT;
        setTextDrawableHide(currentSelectView);
    }

    @Override
    public void dataUpdate(boolean isEmptyData) {
        if (null != titleView) {

            if (isEmptyData) {
                titleView.setVisibility(View.GONE);
            } else {
                titleView.setVisibility(View.VISIBLE);
            }
        }
        if (null != dataUpdateListener) {
            dataUpdateListener.dataUpdate(isEmptyData);

        }
    }

    public List<CombinationBean> getmDataList() {
        return mDataList;
    }

    public void setmDataList(List<CombinationBean> mDataList) {
        this.mDataList = mDataList;
    }

    @Subscribe
    public void onTabTitleChange(TabComTitleChangeEvent event) {
        if (null != event && !TextUtils.isEmpty(event.tabType) && null != tvPercentgae) {
            // PromptManager.showToast("Change tab text to:总市值");
            int currentTextId = 0;
            if (event.tabType.equalsIgnoreCase(FollowComListEngineImpl.ORDER_WEEK_UP)) {
                tvPercentgae.setText(R.string.week_income);
                currentTextId = R.string.week_income;
                // PromptManager.showToast("Change tab text to:涨跌幅");
            } else if (event.tabType.equalsIgnoreCase(FollowComListEngineImpl.ORDER_MONTH_UP)) {
                tvPercentgae.setText(R.string.month_income);
                currentTextId = R.string.month_income;
                // PromptManager.showToast("Change tab text to:涨跌额");

            } else {
                // PromptManager.showToast("Change tab text to:总市值");
                tvPercentgae.setText(R.string.day_income);
                currentTextId = R.string.day_income;
            }

            setTextDrawableHide(tvPercentgae);
            if (isPercentType(orderType) && lastPercentTextIds > 0 && lastPercentTextIds == currentTextId) {

                if (isDefOrder(orderType)) {
                    setDefType(tvPercentgae);
                } else if (isDownOrder(orderType)) {
                    setDownType(tvPercentgae);
                } else {
                    setUpType(tvPercentgae);
                }

            }
        }
    }

    @Subscribe
    public void forward2Top(TopEvent event){
        if(event != null && isVisible() && getUserVisibleHint()){
            if(mListView != null){
                mListView.smoothScrollToPosition(0);
            }
        }
    }
}
