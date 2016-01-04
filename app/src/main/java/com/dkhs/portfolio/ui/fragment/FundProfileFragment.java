package com.dkhs.portfolio.ui.fragment;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.LinearLayout;

import com.dkhs.portfolio.R;
import com.dkhs.portfolio.bean.FundQuoteBean;
import com.dkhs.portfolio.ui.widget.F10ViewParse;

import org.parceler.Parcels;

/**
 * Created by zjz on 2015/5/13.
 */
public class FundProfileFragment extends BaseFragment {

    public static final String TAG = "FundProfileFragment";
    public static final String EXTRA_TAB_TYPE = "extra_tab_type";
    public static final String EXTRA_FUND_QUOTE = "extra_fund_quote";
    private Context mContext;

    private FundQuoteBean mFundQuoteBean;

    public static FundProfileFragment newIntent(FundQuoteBean fundQuoteBean) {
        FundProfileFragment fragment = new FundProfileFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelable(EXTRA_FUND_QUOTE, Parcels.wrap(fundQuoteBean));
//        bundle.putSerializable(EXTRA_TAB_TYPE, tabType);
//        bundle.putString(EXTRA_SYMBOL, symbol);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public int setContentLayoutId() {
        return R.layout.fragment_fund_profile;
    }


    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mContext = activity;

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        handArguments();
    }

    private void handArguments() {
        Bundle bundle = getArguments();
        if (null != bundle) {
            mFundQuoteBean = Parcels.unwrap(bundle.getParcelable(EXTRA_FUND_QUOTE));
        }
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView(view);
    }


    private void initView(View view) {
        LinearLayout mContentView = (LinearLayout) view.findViewById(R.id.proflie_content);
        mContentView.removeAllViews();
        if (null != mFundQuoteBean && mFundQuoteBean.isAllow_trade()) {
            //代销的基金展示购买须知
            View profileView_purchase = new F10ViewParse(mContext).parseFundProfileViewPurchase(mFundQuoteBean);
            mContentView.addView(profileView_purchase);
            View v = new View(getActivity());
            int width = LinearLayout.LayoutParams.MATCH_PARENT;
            int height = getResources().getDimensionPixelOffset(R.dimen.combin_horSpacing);
            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(width,height);
            v.setBackgroundColor(getResources().getColor(R.color.drivi_line));
            mContentView.addView(v,lp);
        }

        View profileView = new F10ViewParse(mContext).parseFundProfileView(mFundQuoteBean);
        mContentView.addView(profileView);

    }

}
