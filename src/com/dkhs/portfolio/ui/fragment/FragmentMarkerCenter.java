package com.dkhs.portfolio.ui.fragment;

import java.lang.ref.WeakReference;
import java.util.Timer;
import java.util.TimerTask;

import com.dkhs.portfolio.R;
import com.dkhs.portfolio.app.PortfolioApplication;
import com.dkhs.portfolio.ui.MarketListActivity;
import com.dkhs.portfolio.ui.MarketListActivity.LoadViewType;
import com.dkhs.portfolio.ui.fragment.FragmentSelectStockFund.ViewType;
import com.umeng.analytics.MobclickAgent;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class FragmentMarkerCenter extends BaseFragment implements OnClickListener {
    private static final String KEY_TYPE = "type";
    public static final int TYPE_INLAND_INDEX_UP = 0;
    public static final int TYPE_INLAND_INDEX_DWON = 10;
    public static final int TYPE_HU_SHEN_UP = 1;
    public static final int TYPE_HU_SHEN_DOWN = 11;

    private int mType;

    private FragmentSelectStockFund loadDataListFragment;

    // 按降序排序
    // private boolean iSortAscend = true;

    private ImageView marketIconUpDown;
    private TextView tvUpDown;
    private TextView marketTextIndex;
    private TextView marketTextEdition;
    private LinearLayout marketLayoutUpanddown;
    private Timer mMarketTimer;
    private static final long mPollRequestTime = 1000 * 5;
    private boolean start = false;

    private WeakReference<MarketListActivity> mWeakActivity;

    public static Fragment initFrag(int type) {
        FragmentMarkerCenter fragment = new FragmentMarkerCenter();
        Bundle args = new Bundle();
        args.putInt(KEY_TYPE, type);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        Bundle b = getArguments();
        if (null != b) {
            mType = b.getInt(KEY_TYPE);
        }
    }

    /**
     * @Title
     * @Description TODO: (用一句话描述这个方法的功能)
     * @param activity
     * @return
     */
    @Override
    public void onAttach(Activity activity) {
        // TODO Auto-generated method stub
        super.onAttach(activity);
        if (mWeakActivity == null) {
            mWeakActivity = new WeakReference<MarketListActivity>((MarketListActivity) activity);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_market_canter, null);
        initView(view);
        // initFragment();
        setLoadTypeView(mType);
        loadFragment(mType);
        return view;
    }

    public void initView(View view) {
        marketIconUpDown = (ImageView) view.findViewById(R.id.market_icon_up_down);
        marketTextIndex = (TextView) view.findViewById(R.id.market_text_index);
        marketTextEdition = (TextView) view.findViewById(R.id.market_text_edition);
        tvUpDown = (TextView) view.findViewById(R.id.market_text_change);
        marketLayoutUpanddown = (LinearLayout) view.findViewById(R.id.market_layout_upanddown);
        marketLayoutUpanddown.setOnClickListener(this);
        marketIconUpDown.setVisibility(View.VISIBLE);
        // marketIconUpDown.setVisibility(View.GONE);
        if (mType == TYPE_HU_SHEN_UP || mType == TYPE_HU_SHEN_DOWN) {
            marketTextIndex.setText(R.string.market_stock_name);
            marketTextEdition.setText(R.string.market_current_value);
        }
    }

    // private void initFragment() {
    // // switch (mType) {
    // // case TYPE_INLAND_INDEX_UP:
    // // if (null == loadDataListFragment) {
    // // loadDataListFragment = loadDataListFragment.getStockFragment(ViewType.STOC_INDEX_MARKET_CURRENT);
    // // }
    // // break;
    // // case TYPE_HU_SHEN_UP:
    // // if (null == loadDataListFragment) {
    // // loadDataListFragment = loadDataListFragment.getStockFragment(ViewType.STOC_INDEX_POSITION);
    // // // iSortAscend = false;
    // // }
    // // break;
    // // default:
    // // break;
    // // }
    // // getChildFragmentManager().beginTransaction().replace(R.id.fragment_market, loadDataListFragment).commit();
    // loadFragment(mType);
    // }

    private void loadFragment(int type) {
        switch (type) {
            case TYPE_INLAND_INDEX_UP: {
                loadDataListFragment = loadDataListFragment.getStockFragment(ViewType.STOC_INDEX_MARKET);
            }
                break;
            case TYPE_INLAND_INDEX_DWON: {
                loadDataListFragment = loadDataListFragment.getStockFragment(ViewType.STOC_INDEX_MARKET_ACE);
            }
                break;
            case TYPE_HU_SHEN_UP: {
                loadDataListFragment = loadDataListFragment.getStockFragment(ViewType.STOC_INDEX_POSITION);
            }
                break;
            case TYPE_HU_SHEN_DOWN: {
                loadDataListFragment = loadDataListFragment.getStockFragment(ViewType.STOC_INDEX_POSITION_ACE);
            }
                break;

            default:
                break;
        }
        if (null != loadDataListFragment) {
            getChildFragmentManager().beginTransaction().replace(R.id.fragment_market, loadDataListFragment).commit();
        }
    }

    private void setLoadTypeView(int type) {
        switch (type) {
            case TYPE_INLAND_INDEX_UP:
            case TYPE_HU_SHEN_UP: {
                tvUpDown.setText(R.string.market_up_ratio);
                marketIconUpDown.setImageResource(R.drawable.market_icon_down);
            }
                break;
            case TYPE_INLAND_INDEX_DWON:
            case TYPE_HU_SHEN_DOWN: {
                tvUpDown.setText(R.string.market_down_ratio);
                marketIconUpDown.setImageResource(R.drawable.market_icon_up);
            }
                break;

            default:
                break;
        }
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.market_layout_upanddown) {
            switch (mType) {
                case TYPE_HU_SHEN_DOWN: {
                    mType = TYPE_HU_SHEN_UP;
                    if (null != mWeakActivity.get()) {
                        mWeakActivity.get().setTitleByType(LoadViewType.StockIncease);
                    }

                }
                    break;
                case TYPE_HU_SHEN_UP: {
                    mType = TYPE_HU_SHEN_DOWN;
                    if (null != mWeakActivity.get()) {
                        mWeakActivity.get().setTitleByType(LoadViewType.StockDown);
                    }

                }
                    break;
                case TYPE_INLAND_INDEX_DWON: {
                    mType = TYPE_INLAND_INDEX_UP;

                }
                    break;
                case TYPE_INLAND_INDEX_UP: {
                    mType = TYPE_INLAND_INDEX_DWON;

                }
                    break;

                default:
                    break;
            }

            setLoadTypeView(mType);
            loadFragment(mType);

        }

    }

    @Override
    public void onResume() {
        if (mMarketTimer == null && start) {
            mMarketTimer = new Timer(true);
            mMarketTimer.schedule(new RequestMarketTask(), mPollRequestTime, mPollRequestTime);
        }
        super.onResume();
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

            loadDataListFragment.refreshForMarker();
        }
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        // TODO Auto-generated method stub
        if (isVisibleToUser) {
            start = true;
            if (mMarketTimer == null && start) {
                mMarketTimer = new Timer(true);
                mMarketTimer.schedule(new RequestMarketTask(), mPollRequestTime, mPollRequestTime);
            }
        } else {
            start = false;
            if (mMarketTimer != null) {
                mMarketTimer.cancel();
                mMarketTimer = null;
            }
        }
        super.setUserVisibleHint(isVisibleToUser);
    }

    private final String mPageName = PortfolioApplication.getInstance().getString(R.string.count_market_center_list);

    @Override
    public void onPause() {
        // TODO Auto-generated method stub
        super.onPause();
        // SDK已经禁用了基于Activity 的页面统计，所以需要再次重新统计页面
        MobclickAgent.onPageEnd(mPageName);
    }

}
