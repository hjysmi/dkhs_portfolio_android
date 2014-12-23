package com.dkhs.portfolio.ui.fragment;

import java.lang.ref.WeakReference;
import java.util.Timer;
import java.util.TimerTask;

import com.dkhs.portfolio.R;
import com.dkhs.portfolio.app.PortfolioApplication;
import com.dkhs.portfolio.ui.MarketListActivity;
import com.dkhs.portfolio.ui.MarketListActivity.LoadViewType;
import com.dkhs.portfolio.ui.fragment.FragmentSelectStockFund.StockViewType;
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
    public static final int TYPE_INLAND_INDEX_UP = 0x00;
    public static final int TYPE_INLAND_INDEX_DWON = 0x10;
    public static final int TYPE_STOCK_UP = 0x01;
    public static final int TYPE_STOCK_DOWN = 0x11;
    public static final int TYPE_STOCK_TURN_UP = 0x20;
    public static final int TYPE_STOCK_TURN_DOWN = 0x21;
    public static final int TYPE_STOCK_AMPLI_DOWN = 0x31;
    public static final int TYPE_STOCK_AMPLI_UP = 0x30;
    public static final int TYPE_PLATE_UP = 0x12;
    public static final int TYPE_PLATE_DOWN = 0x22;

    private int mType;

    private FragmentSelectStockFund loadDataListFragment;

    // 按降序排序
    // private boolean iSortAscend = true;

    private ImageView marketIconUpDown;
    private ImageView ivCenter;
    private TextView tvUpDown;
    private TextView marketTextIndex;
    private TextView marketTextEdition;
    private LinearLayout marketLayoutUpanddown;
    private Timer mMarketTimer;
    private static final long mPollRequestTime = 1000 * 5;
    private boolean start = false;

    private WeakReference<MarketListActivity> mWeakActivity;

    public static FragmentMarkerCenter initFrag(int type) {
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
        super.onAttach(activity);
        if (mWeakActivity == null) {
            mWeakActivity = new WeakReference<MarketListActivity>((MarketListActivity) activity);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_market_canter, null);
        initView(view);
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
        ivCenter = (ImageView) view.findViewById(R.id.market_icon_center);
        marketLayoutUpanddown.setOnClickListener(this);
        marketIconUpDown.setVisibility(View.VISIBLE);
        if (mType == TYPE_STOCK_UP || mType == TYPE_STOCK_DOWN || mType == TYPE_STOCK_AMPLI_UP
                || mType == TYPE_STOCK_TURN_UP) {
            marketTextIndex.setText(R.string.market_stock_name);
            marketTextEdition.setText(R.string.market_current_value);
        } else if (mType == TYPE_PLATE_UP || mType == TYPE_PLATE_DOWN) {
            marketTextIndex.setText(R.string.plate_name);
            marketTextEdition.setText(R.string.market_updown_ratio);
            tvUpDown.setText(R.string.plate_leader_stock);
            marketIconUpDown.setVisibility(View.GONE);
            ivCenter.setVisibility(View.VISIBLE);

        }
    }

    private void loadFragment(int type) {
        switch (type) {
            case TYPE_INLAND_INDEX_UP: {
                loadDataListFragment = FragmentSelectStockFund.getStockFragment(StockViewType.MARKET_INLAND_INDEX);
            }
                break;
            case TYPE_INLAND_INDEX_DWON: {
                loadDataListFragment = FragmentSelectStockFund.getStockFragment(StockViewType.MARKET_INLAND_INDEX_ACE);
            }
                break;
            case TYPE_STOCK_UP: {
                loadDataListFragment = FragmentSelectStockFund.getStockFragment(StockViewType.MARKET_STOCK_UPRATIO);
            }
                break;
            case TYPE_STOCK_DOWN: {
                loadDataListFragment = FragmentSelectStockFund.getStockFragment(StockViewType.MARKET_STOCK_DOWNRATIO);
            }
                break;
            case TYPE_STOCK_AMPLI_DOWN: {
                loadDataListFragment = FragmentSelectStockFund.getStockFragment(StockViewType.MARKET_STOCK_AMPLIT_ACE);
            }
                break;
            case TYPE_STOCK_AMPLI_UP: {
                loadDataListFragment = FragmentSelectStockFund.getStockFragment(StockViewType.MARKET_STOCK_AMPLIT);
            }
                break;
            case TYPE_STOCK_TURN_UP: {
                loadDataListFragment = FragmentSelectStockFund.getStockFragment(StockViewType.MARKET_STOCK_TURNOVER);
            }
                break;
            case TYPE_STOCK_TURN_DOWN: {
                loadDataListFragment = FragmentSelectStockFund.getStockFragment(StockViewType.MARKET_STOCK_TURNOVER_ACE);
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
        int tvTextResId = 0;
        int iconResId = 0;
        switch (type) {
            case TYPE_INLAND_INDEX_UP:
            case TYPE_STOCK_UP: {
                tvTextResId = R.string.market_up_ratio;
                iconResId = R.drawable.market_icon_down;
            }
                break;
            case TYPE_INLAND_INDEX_DWON:
            case TYPE_STOCK_DOWN: {
                tvTextResId = R.string.market_down_ratio;
                iconResId = R.drawable.market_icon_up;
            }
                break;

            case TYPE_STOCK_AMPLI_DOWN: {
                tvTextResId = R.string.market_amplit;
                iconResId = R.drawable.market_icon_up;
            }
                break;
            case TYPE_STOCK_AMPLI_UP: {
                tvTextResId = R.string.market_amplit;
                iconResId = R.drawable.market_icon_down;
            }
                break;
            case TYPE_STOCK_TURN_UP: {
                tvTextResId = R.string.market_turnover;
                iconResId = R.drawable.market_icon_down;
            }
                break;
            case TYPE_STOCK_TURN_DOWN: {
                tvTextResId = R.string.market_turnover;
                iconResId = R.drawable.market_icon_up;
            }
                break;
            default:
                break;
        }
        if (tvTextResId > 0) {

            tvUpDown.setText(tvTextResId);
        }
        if (iconResId > 0) {

            marketIconUpDown.setImageResource(iconResId);
        }
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.market_layout_upanddown) {
            switch (mType) {
                case TYPE_STOCK_DOWN: {
                    mType = TYPE_STOCK_UP;
                    if (null != mWeakActivity.get()) {
                        mWeakActivity.get().setTitleByType(LoadViewType.StockIncease);
                    }

                }
                    break;
                case TYPE_STOCK_UP: {
                    mType = TYPE_STOCK_DOWN;
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
                case TYPE_STOCK_AMPLI_DOWN: {
                    mType = TYPE_STOCK_AMPLI_UP;

                }
                    break;
                case TYPE_STOCK_AMPLI_UP: {
                    mType = TYPE_STOCK_AMPLI_DOWN;

                }
                    break;
                case TYPE_STOCK_TURN_UP: {
                    mType = TYPE_STOCK_TURN_DOWN;

                }
                    break;
                case TYPE_STOCK_TURN_DOWN: {
                    mType = TYPE_STOCK_TURN_UP;

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
        super.onResume();
        if (mMarketTimer == null && start) {
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
