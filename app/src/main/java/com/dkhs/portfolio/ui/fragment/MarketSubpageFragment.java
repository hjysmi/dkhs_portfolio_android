/**
 * @Title MainMarketFragment.java
 * @Package com.dkhs.portfolio.ui.fragment
 * @Description TODO(用一句话描述该文件做什么)
 * @author zjz
 * @date 2015-2-6 上午9:46:52
 * @version V1.0
 */
package com.dkhs.portfolio.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.dkhs.portfolio.R;
import com.dkhs.portfolio.ui.eventbus.BusProvider;
import com.dkhs.portfolio.ui.eventbus.NewIntent;
import com.dkhs.portfolio.ui.eventbus.RotateRefreshEvent;
import com.dkhs.portfolio.ui.eventbus.StopRefreshEvent;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.squareup.otto.Subscribe;

/**
 * @author zcm
 * @version 1.0
 * @Description TODO(这里用一句话描述这个类的作用)
 * @date 2015-12-9 下午13:56:52
 */
public class MarketSubpageFragment extends VisiableLoadFragment implements View.OnClickListener {

    @ViewInject(R.id.rl_header_title)
    RelativeLayout mRlheadertitle;
    //基金
    @ViewInject(R.id.btn_titletab_left)
    Button mBtntitletableft;
    //股票
    @ViewInject(R.id.btn_titletab_center)
    Button mBtntitletabcenter;
    //组合
    @ViewInject(R.id.btn_titletab_right)
    Button mBtntitletabright;

    @ViewInject(R.id.btn_refresh)
    TextView mBtnrefresh;
    @ViewInject(R.id.btn_search)
    TextView mBtnsearch;
    @ViewInject(R.id.left_btn)
    TextView mLeftBtn;


    @Override
    public int setContentLayoutId() {
        return R.layout.fragment_market_subpage;
    }

    private static final String MARKET_TYPE = "market_type";
    public static final int TYPE_FUND = 1;
    public static final int TYPE_COMBINATION = 2;
    private MarketFundsFragment fundsFragment;
    private MarketCombinationFragment combinationFragment;

    public static MarketSubpageFragment getFundFragment() {
        MarketSubpageFragment fragment = new MarketSubpageFragment();
        Bundle args = new Bundle();
        args.putInt(MARKET_TYPE, TYPE_FUND);
        fragment.setArguments(args);
        return fragment;
    }

    public static MarketSubpageFragment getFragment(int type) {
        MarketSubpageFragment fragment = new MarketSubpageFragment();
        Bundle args = new Bundle();
        args.putInt(MARKET_TYPE, type);
        fragment.setArguments(args);
        return fragment;
    }

    public static MarketSubpageFragment getCombinationFragment() {
        MarketSubpageFragment fragment = new MarketSubpageFragment();
        Bundle args = new Bundle();
        args.putInt(MARKET_TYPE, TYPE_COMBINATION);
        fragment.setArguments(args);
        return fragment;
    }

    private SubpageType curType;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getArguments();
        if (null != bundle) {
            curType = SubpageType.valueOf(bundle.getInt(MARKET_TYPE));
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mRlheadertitle.setClickable(true);
        mBtnrefresh.setCompoundDrawablesWithIntrinsicBounds(getResources().getDrawable(R.drawable.nav_refresh),
                null, null, null);
        mLeftBtn.setCompoundDrawablesWithIntrinsicBounds(getResources().getDrawable(R.drawable.btn_back_selector),
                null, null, null);
        mLeftBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().finish();
            }
        });
        BusProvider.getInstance().register(this);
        mBtntitletabcenter.setVisibility(View.GONE);
        FragmentTransaction ft = getChildFragmentManager().beginTransaction();
        Fragment fragment = null;

        switch (curType) {
            case TYPE_COMBINATION:
                mBtnrefresh.setVisibility(View.VISIBLE);
                mBtnsearch.setCompoundDrawablesWithIntrinsicBounds(getResources().getDrawable(R.drawable.ic_title_add),
                        null, null, null);
                combinationFragment = new MarketCombinationFragment();
                fragment = combinationFragment;
                mBtntitletabright.setTextColor(getResources().getColor(R.color.black));
                mBtntitletableft.setVisibility(View.GONE);
                break;
            case TYPE_FUND_MANAGER_RANKING_WEEK:
                fragment = gotoFundManagerPage();
                break;
            case TYPE_FUND_MANAGER_RANKING_SIX_MONTH:
                fragment = gotoFundManagerPage();
                break;
            default:
                mBtnrefresh.setVisibility(View.GONE);
                mBtnsearch.setCompoundDrawablesWithIntrinsicBounds(getResources().getDrawable(R.drawable.btn_search_select),
                        null, null, null);
                fundsFragment = MarketFundsFragment.getFragment(curType.ordinal());
                fragment = fundsFragment;
                mBtntitletableft.setText(R.string.fund_market);
                setTitle(R.string.fund_rank);
        }
        ft.replace(R.id.view_datalist, fragment);
        ft.commit();
        mBtnrefresh.setOnClickListener((View.OnClickListener) fragment);
        mBtnsearch.setOnClickListener((View.OnClickListener) fragment);
    }

    private Fragment gotoFundManagerPage() {
        mBtnrefresh.setVisibility(View.GONE);
        mBtnsearch.setCompoundDrawablesWithIntrinsicBounds(getResources().getDrawable(R.drawable.btn_search_select),
                null, null, null);
        fundsFragment = MarketFundsFragment.getFragment(curType.ordinal());
        setTitle(R.string.fund_manager_rank);
        return fundsFragment;
    }

    private void setTitle(int fund_manager_rank) {
        mBtntitletableft.setTextColor(getResources().getColor(R.color.black));
        LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) mBtntitletableft.getLayoutParams();
        lp.width = LinearLayout.LayoutParams.MATCH_PARENT;
        mBtntitletableft.setGravity(Gravity.CENTER);
        mBtntitletableft.setText(fund_manager_rank);
        mBtntitletabright.setVisibility(View.GONE);
    }

    @Override
    public void onDestroyView() {
        BusProvider.getInstance().unregister(this);
        super.onDestroyView();
    }

    @Subscribe
    public void newIntent(NewIntent newIntent) {
        handIntent(newIntent.bundle);
    }


    private void handIntent(Bundle bundle) {

        if (bundle.containsKey("fund_index")) {
            int index = bundle.getInt("fund_index", 0);
        }
    }

    @Override
    public void requestData() {

    }


    private static final String TAG = MarketSubpageFragment.class.getSimpleName();

    @Subscribe
    public void rotateRefreshButton(RotateRefreshEvent rotateRefreshEvent) {
        if (isAdded() && !isHidden()) {
            mBtnrefresh.setCompoundDrawablesWithIntrinsicBounds(getResources().getDrawable(R.drawable.nav_refreshing),
                    null, null, null);
            Animation animation = AnimationUtils.loadAnimation(mActivity, R.anim.rotate_around_center_point);
            mBtnrefresh.startAnimation(animation);
        }
    }

    @Subscribe
    public void stopRefreshAnimation(StopRefreshEvent stopRefreshEvent) {
        if (isAdded() && !isHidden()) {
            mBtnrefresh.clearAnimation();
            mBtnrefresh.setCompoundDrawablesWithIntrinsicBounds(getResources().getDrawable(R.drawable.nav_refresh),
                    null, null, null);
        }
    }

    @OnClick({R.id.btn_titletab_left, R.id.btn_titletab_right})
    public void onClick(View v) {

    }

    public enum SubpageType {
        TYPE_FUND_MANAGER_RANKING_WEEK(0),
        TYPE_FUND_ALL_RANKING_MONTH(1),
        TYPE_FUND_ALL_RANKING_YEAR(2),
        TYPE_FUND_MIXED_MONTH(3),
        TYPE_COMBINATION(4),
        TYPE_FUND_MANAGER_RANKING_SIX_MONTH(5);
        private int value;

        private SubpageType(int var) {
            value = var;
        }

        public static SubpageType valueOf(int ordinal) {
            if (ordinal < 0 || ordinal >= values().length) {
                throw new IndexOutOfBoundsException("Invalid ordinal");
            }
            return values()[ordinal];
        }
    }
}
