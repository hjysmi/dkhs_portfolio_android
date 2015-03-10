/**
 * @Title TabFundsFragment.java
 * @Package com.dkhs.portfolio.ui.fragment
 * @Description TODO(用一句话描述该文件做什么)
 * @author zjz
 * @date 2015-2-7 上午11:03:26
 * @version V1.0
 */
package com.dkhs.portfolio.ui.fragment;

import java.util.ArrayList;
import java.util.List;

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

import com.dkhs.portfolio.R;
import com.dkhs.portfolio.app.PortfolioApplication;
import com.dkhs.portfolio.bean.CombinationBean;
import com.dkhs.portfolio.bean.MoreDataBean;
import com.dkhs.portfolio.engine.FollowComListEngineImpl;
import com.dkhs.portfolio.engine.LoadMoreDataEngine.ILoadDataBackListener;
import com.dkhs.portfolio.engine.UserCombinationEngineImpl;
import com.dkhs.portfolio.ui.EditTabFundActivity;
import com.dkhs.portfolio.ui.OrderFundDetailActivity;
import com.dkhs.portfolio.ui.PositionAdjustActivity;
import com.dkhs.portfolio.ui.adapter.TabFundsAdapter;
import com.dkhs.portfolio.ui.eventbus.IDataUpdateListener;
import com.dkhs.portfolio.ui.widget.PullToRefreshListView;
import com.dkhs.portfolio.utils.PromptManager;
import com.dkhs.portfolio.utils.UIUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;

/**
 * @ClassName TabFundsFragment
 * @Description TODO(这里用一句话描述这个类的作用)
 * @author zjz
 * @date 2015-2-7 上午11:03:26
 * @version 1.0
 */
public class TabFundsFragment extends BaseFragment implements IDataUpdateListener {

    @ViewInject(R.id.tv_current)
    private TextView tvCurrent;
    // @ViewInject(R.id.tv_increase)
    // private TextView tvChange;
    @ViewInject(R.id.tv_percentage)
    private TextView tvPercentgae;

    @ViewInject(R.id.view_stock_title)
    private View titleView;

    private TabFundsAdapter mFundsAdapter;
    private List<CombinationBean> mDataList = new ArrayList<CombinationBean>();
    private FollowComListEngineImpl dataEngine;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        // if (null == dataEngine) {
        dataEngine = new FollowComListEngineImpl(new ILoadDataBackListener<CombinationBean>() {

            @Override
            public void loadFinish(MoreDataBean<CombinationBean> object) {
                mSwipeLayout.setRefreshing(false);
                if (null != object.getResults()) {
                    if (!UIUtils.roundAble(object.getStatu())) {
                        // if (mCombinationTimer != null) {
                        // mCombinationTimer.cancel();
                        // mCombinationTimer = null;
                        // }
                    }
                    // if (isRefresh) {
                    mDataList.clear();
                    // isRefresh = false;
                    // }

                    // mDataList = object.getResults();
                    mDataList.addAll(object.getResults());
                    // System.out.println("datalist size :" + mDataList.size());
                    mFundsAdapter.notifyDataSetChanged();
                }
                refreshEditView();
            }
        }, "");

        // }
    }

    @Override
    public int setContentLayoutId() {
        return R.layout.fragment_tab_myfunds;
    }

    /**
     * @Title
     * @Description TODO: (用一句话描述这个方法的功能)
     * @return
     */
    @Override
    public void onResume() {
        // TODO Auto-generated method stub
        super.onResume();
        refreshEditView();

    }

    public void refreshEditView() {
        // if (null != dataUpdateListener) {
        if (!mDataList.isEmpty()) {
            dataUpdate(false);
        } else {
            dataUpdate(true);
        }

        // }
    }

    public void setDataUpdateListener(IDataUpdateListener listen) {
        this.dataUpdateListener = listen;
    }

    private IDataUpdateListener dataUpdateListener;

    @OnClick({ R.id.tv_current, R.id.tv_percentage, R.id.tv_increase })
    public void onClick(View v) {
        int id = v.getId();
        if (!PortfolioApplication.hasUserLogin()) {
            return;
        }
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
        if (PortfolioApplication.hasUserLogin()) {
            refresh();
        }
    }

    protected PullToRefreshListView mListView;
    private RelativeLayout pb;
    public SwipeRefreshLayout mSwipeLayout;

    private void initView(View view) {

        mListView = (PullToRefreshListView) view.findViewById(android.R.id.list);
        mFundsAdapter = new TabFundsAdapter(getActivity(), mDataList);
        mListView.setAdapter(mFundsAdapter);
        mListView.setDividerHeight(0);
        mListView.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // TODO Auto-generated method stub
                getActivity().startActivity(
                        OrderFundDetailActivity.getIntent(getActivity(), mDataList.get(position), true,
                                FundsOrderFragment.ORDER_TYPE_DAY));

            }
        });
        TextView emptyview = (TextView) view.findViewById(R.id.add_data);
        emptyview.setText(R.string.click_creat_fund);
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
        mSwipeLayout.setColorSchemeResources(android.R.color.holo_red_light);
        mSwipeLayout.setOnRefreshListener(new android.support.v4.widget.SwipeRefreshLayout.OnRefreshListener() {

            @Override
            public void onRefresh() {
                refresh();

            }
        });

    }

    public void addItem() {
        if (mDataList.size() >= 20) {
            PromptManager.showShortToast(R.string.more_combination_tip);
        } else {
            getActivity().startActivity(PositionAdjustActivity.newIntent(getActivity(), null));
        }

    }

    public void editFund() {
        if (!mDataList.isEmpty()) {
            // startActivity(EditTabFundActivity.getIntent(getActivity(), mDataList));
            startActivityForResult(EditTabFundActivity.getIntent(getActivity(), mDataList), 1722);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 1722) {
            setDefType(viewLastClick);
            dataEngine.setOrderType(orderType);
            refresh();
        }

    }

    public void refresh() {
        // isRefresh = true;

        dataEngine.loadAllData();
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
    private String orderType;

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
        if (!TextUtils.isEmpty(orderType)
                && (orderType.equals(UserCombinationEngineImpl.ORDER_CUMULATIVE_UP) || orderType
                        .equals(UserCombinationEngineImpl.ORDER_NET_VALUE_UP))) {
            return true;
        }
        return false;
    }

    private boolean isDownOrder(String orderType) {
        if (!TextUtils.isEmpty(orderType)
                && (orderType.equals(UserCombinationEngineImpl.ORDER_CUMULATIVE_DOWN) || orderType
                        .equals(UserCombinationEngineImpl.ORDER_NET_VALUE_DOWN))) {
            return true;
        }
        return false;
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

    private void setDownType(TextView currentSelectView) {
        if (currentSelectView == tvCurrent) {
            orderType = UserCombinationEngineImpl.ORDER_NET_VALUE_DOWN;
        } else if (currentSelectView == tvPercentgae) {
            orderType = UserCombinationEngineImpl.ORDER_CUMULATIVE_DOWN;
        }
        setDrawableDown(currentSelectView);
    }

    private void setUpType(TextView currentSelectView) {
        if (currentSelectView == tvCurrent) {
            orderType = UserCombinationEngineImpl.ORDER_NET_VALUE_UP;
        } else if (currentSelectView == tvPercentgae) {
            orderType = UserCombinationEngineImpl.ORDER_CUMULATIVE_UP;
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
}
