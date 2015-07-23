package com.dkhs.portfolio.ui.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.dkhs.portfolio.R;
import com.dkhs.portfolio.app.PortfolioApplication;
import com.dkhs.portfolio.ui.MarketListActivity;
import com.dkhs.portfolio.ui.MarketListActivity.ILoadingFinishListener;
import com.dkhs.portfolio.ui.MarketListActivity.LoadViewType;
import com.dkhs.portfolio.ui.RefreshModelActivity;
import com.dkhs.portfolio.ui.fragment.FragmentSelectStockFund.StockViewType;
import com.umeng.analytics.MobclickAgent;

import java.lang.ref.WeakReference;
import java.util.Timer;
import java.util.TimerTask;

public class FragmentMarkerCenter extends BaseFragment implements OnClickListener, ILoadingFinishListener {
    private static final String KEY_TYPE = "type";
    public static final int TYPE_INLAND_INDEX_UP = 0x00;
    public static final int TYPE_INLAND_INDEX = 0x00;
    public static final int TYPE_INLAND_INDEX_DWON = 0x10;
    public static final int TYPE_STOCK_UP = 0x01;
    public static final int TYPE_STOCK_DOWN = 0x11;
    public static final int TYPE_STOCK_TURN_UP = 0x20;
    public static final int TYPE_STOCK_TURN_DOWN = 0x21;
    public static final int TYPE_STOCK_AMPLI_DOWN = 0x31;
    public static final int TYPE_STOCK_AMPLI_UP = 0x30;
    public static final int TYPE_PLATE_UP = 0x12;
    public static final int TYPE_PLATE_DOWN = 0x22;
    public static final int TYPE_PLATEDETAIL_UP = 0x13;
    public static final int TYPE_PLATEDETAIL_DOWN = 0x23;

    private int mType;

    private Fragment loadDataListFragment;

    // 按降序排序
    // private boolean iSortAscend = true;

    private ImageView marketIconUpDown;
    private ImageView ivCenter;
    private TextView tvUpDown;
    private Timer mMarketTimer;
    private static final long mPollRequestTime = 1000 * 30;
    private boolean start = true;

    private WeakReference<MarketListActivity> mWeakActivity;

    private String mPlateId;

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
        Bundle extras = getActivity().getIntent().getExtras();
        if (extras != null) {
            handleExtras(extras);
        }
    }

    private void handleExtras(Bundle extras) {
        mPlateId = extras.getString(MarketListActivity.EXTRA_PLATE_ID);

    }

    /**
     * @param activity
     * @return
     * @Title
     * @Description TODO: (用一句话描述这个方法的功能)
     */
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        if (mWeakActivity == null) {
            mWeakActivity = new WeakReference<MarketListActivity>((MarketListActivity) activity);
        }
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView(view);
        setLoadTypeView(mType);
        loadFragment(mType);
    }

    public void initView(View view) {
        marketIconUpDown = (ImageView) view.findViewById(R.id.market_icon_up_down);
        TextView marketTextIndex = (TextView) view.findViewById(R.id.market_text_index);
        TextView marketTextEdition = (TextView) view.findViewById(R.id.market_text_edition);
        tvUpDown = (TextView) view.findViewById(R.id.market_text_change);
        LinearLayout marketLayoutUpanddown = (LinearLayout) view.findViewById(R.id.market_layout_upanddown);
        ivCenter = (ImageView) view.findViewById(R.id.market_icon_center);
        marketIconUpDown.setVisibility(View.VISIBLE);
        if (mType == TYPE_STOCK_UP || mType == TYPE_STOCK_DOWN || mType == TYPE_STOCK_AMPLI_UP
                || mType == TYPE_STOCK_TURN_UP || mType == TYPE_PLATEDETAIL_UP) {
            marketLayoutUpanddown.setOnClickListener(this);
            marketTextIndex.setText(R.string.market_stock_name);
            marketTextEdition.setText(R.string.market_current_value);
        } else if (mType == TYPE_PLATE_UP || mType == TYPE_PLATE_DOWN) {
            marketTextIndex.setText(R.string.plate_name);
            marketTextEdition.setText(R.string.market_updown_ratio);
            tvUpDown.setText(R.string.plate_leader_stock);
            marketIconUpDown.setVisibility(View.GONE);
            ivCenter.setVisibility(View.VISIBLE);
            marketTextEdition.setOnClickListener(this);

        } else if (mType == TYPE_INLAND_INDEX_UP) {

            marketIconUpDown.setVisibility(View.GONE);

        }
    }

    public void refreshData() {
        // System.out.println("刷新Fragment markercenter ");
        // startRefreshView();
        if (loadDataListFragment instanceof FragmentMarketList) {
            ((FragmentMarketList) loadDataListFragment).refreshNoCaseTime();
        } else if (loadDataListFragment instanceof HotPlateFragment) {
            ((HotPlateFragment) loadDataListFragment).refreshData();
        }
        // if (mMarketTimer != null) {
        // mMarketTimer.cancel();
        // }
        // mMarketTimer = new Timer(true);
        // mMarketTimer.schedule(new RequestMarketTask(), 0, mPollRequestTime);

    }

    private void startRefreshView() {
        if (mWeakActivity != null && mWeakActivity.get() != null) {
            mWeakActivity.get().startAnimaRefresh();
        }
    }

    private void loadFragment(int type) {
        switch (type) {
            case TYPE_INLAND_INDEX_UP: {
                loadDataListFragment = FragmentMarketList.getStockFragment(StockViewType.MARKET_INLAND_INDEX_CURRENT);
            }
            break;
            case TYPE_INLAND_INDEX_DWON: {
                loadDataListFragment = FragmentMarketList.getStockFragment(StockViewType.MARKET_INLAND_INDEX_ACE);
            }
            break;
            case TYPE_STOCK_UP: {
                loadDataListFragment = FragmentMarketList.getStockFragment(StockViewType.MARKET_STOCK_UPRATIO);
            }
            break;
            case TYPE_STOCK_DOWN: {
                loadDataListFragment = FragmentMarketList.getStockFragment(StockViewType.MARKET_STOCK_DOWNRATIO);
            }
            break;
            case TYPE_STOCK_AMPLI_DOWN: {
                loadDataListFragment = FragmentMarketList.getStockFragment(StockViewType.MARKET_STOCK_AMPLIT_ACE);
            }
            break;
            case TYPE_STOCK_AMPLI_UP: {
                loadDataListFragment = FragmentMarketList.getStockFragment(StockViewType.MARKET_STOCK_AMPLIT);
            }
            break;
            case TYPE_STOCK_TURN_UP: {
                loadDataListFragment = FragmentMarketList.getStockFragment(StockViewType.MARKET_STOCK_TURNOVER);
            }
            break;
            case TYPE_STOCK_TURN_DOWN: {
                loadDataListFragment = FragmentMarketList.getStockFragment(StockViewType.MARKET_STOCK_TURNOVER_ACE);
            }
            break;
            case TYPE_PLATE_UP: {
                loadDataListFragment = HotPlateFragment.getFragment(HotPlateFragment.ORDER_TYPE_UP);
            }
            break;
            case TYPE_PLATE_DOWN: {
                loadDataListFragment = HotPlateFragment.getFragment(HotPlateFragment.ORDER_TYPE_DOWN);
            }
            break;
            case TYPE_PLATEDETAIL_UP: {
                loadDataListFragment = FragmentMarketList.getStockFragmentByPlate(StockViewType.MARKET_PLATE_LIST,
                        mPlateId);
            }
            break;
            case TYPE_PLATEDETAIL_DOWN: {
                loadDataListFragment = FragmentMarketList.getStockFragmentByPlate(StockViewType.MARKET_PLATE_LIST_ACE,
                        mPlateId);
            }
            break;

            default:
                break;
        }
        if (null != loadDataListFragment) {
            getChildFragmentManager().beginTransaction().replace(R.id.fragment_market, loadDataListFragment).commit();
            if (loadDataListFragment instanceof FragmentMarketList) {
                ((FragmentMarketList) loadDataListFragment).setLoadingFinishListener(this);
            }
            if (loadDataListFragment instanceof HotPlateFragment) {
                ((HotPlateFragment) loadDataListFragment).setLoadingFinishListener(this);
            }
        }
    }

    private void setLoadTypeView(int type) {
        int tvTextResId = 0;
        int iconResId = 0;
        switch (type) {
            case TYPE_INLAND_INDEX_UP:
            case TYPE_PLATEDETAIL_UP:
            case TYPE_STOCK_UP: {
                tvTextResId = R.string.market_up_ratio;
                iconResId = R.drawable.market_icon_down;
            }
            break;
            case TYPE_INLAND_INDEX_DWON:
            case TYPE_PLATEDETAIL_DOWN:
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

            case TYPE_PLATE_DOWN: {
                ivCenter.setImageResource(R.drawable.market_icon_up);
            }
            break;
            case TYPE_PLATE_UP: {
                ivCenter.setImageResource(R.drawable.market_icon_down);

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
        if (v.getId() == R.id.market_layout_upanddown || v.getId() == R.id.market_text_edition) {
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
                case TYPE_PLATE_UP: {
                    mType = TYPE_PLATE_DOWN;

                }
                break;
                case TYPE_PLATE_DOWN: {
                    mType = TYPE_PLATE_UP;

                }
                break;
                case TYPE_PLATEDETAIL_DOWN: {
                    mType = TYPE_PLATEDETAIL_UP;

                }
                break;
                case TYPE_PLATEDETAIL_UP: {
                    mType = TYPE_PLATEDETAIL_DOWN;

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

            if (loadDataListFragment instanceof FragmentMarketList) {
                ((FragmentMarketList) loadDataListFragment).refreshForMarker();
            } else if (loadDataListFragment instanceof HotPlateFragment) {
                ((HotPlateFragment) loadDataListFragment).refreshData();
            }
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
    }

    /**
     * @return
     * @Title
     * @Description TODO: (用一句话描述这个方法的功能)
     */
    @Override
    public void loadingFinish() {
        if (null != getActivity() && getActivity() instanceof RefreshModelActivity) {
            ((RefreshModelActivity) getActivity()).endAnimaRefresh();
        }

    }

    /**
     * @return
     * @Title
     * @Description TODO: (用一句话描述这个方法的功能)
     */
    @Override
    public void startLoadingData() {
        if (null != getActivity() && getActivity() instanceof RefreshModelActivity) {
            ((RefreshModelActivity) getActivity()).startAnimaRefresh();
        }

    }

    /**
     * @return
     * @Title
     * @Description TODO: (用一句话描述这个方法的功能)
     */
    @Override
    public int setContentLayoutId() {
        // TODO Auto-generated method stub
        return R.layout.fragment_market_canter;
    }

}
