package com.dkhs.portfolio.ui.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.dkhs.portfolio.R;
import com.dkhs.portfolio.utils.ColorTemplate;
import com.dkhs.portfolio.utils.StringFromatUtils;

public class ScrollTopFragment extends Fragment {
    public static final String ARGUMENT_SCROLL_TYPE = "trend_type";
    public static final String ARGUMENT_VALUE = "argument_value";
    public static final String TYPE_SEASON = "type_season";
    public static final String TYPE_WEEK = "type_week";
    public static final String TYPE_MONTH = "type_month";

    private String mType = TYPE_WEEK;
    private float value;

    public static ScrollTopFragment getInstance(String type, float value) {
        ScrollTopFragment mScrollTopFragment = new ScrollTopFragment();
        Bundle arguments = new Bundle();
        arguments.putString(ARGUMENT_SCROLL_TYPE, type);
        arguments.putFloat(ARGUMENT_VALUE, value);
        mScrollTopFragment.setArguments(arguments);
        return mScrollTopFragment;
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
        // handle fragment arguments
        Bundle arguments = getArguments();
        if (arguments != null) {
            handleArguments(arguments);
        }

        // restore saved state
        if (savedInstanceState != null) {
            handleSavedInstanceState(savedInstanceState);
        }
    }

    /**
     * @Title
     * @Description TODO: (用一句话描述这个方法的功能)
     * @param savedInstanceState
     * @return
     */
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onActivityCreated(savedInstanceState);
        // setRetainInstance(true);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

    }

    private void handleSavedInstanceState(Bundle savedInstanceState) {
        mType = savedInstanceState.getString(ARGUMENT_SCROLL_TYPE);
    }

    private void handleArguments(Bundle arguments) {
        mType = arguments.getString(ARGUMENT_SCROLL_TYPE);
        value = arguments.getFloat(ARGUMENT_VALUE);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_top_scroll, null);
        initView(view);
        // view.setBackgroundColor(ColorTemplate.getRaddomColor());
        return view;
    }

    private void initView(View view) {
        TextView tvTitle = (TextView) view.findViewById(R.id.tv_top);
        TextView tvIncreaseText = (TextView) view.findViewById(R.id.tv_increase_text);
        TextView tvIncreaseValue = (TextView) view.findViewById(R.id.tv_top_value);

        if (!TextUtils.isEmpty(mType)) {

            if (mType.equalsIgnoreCase(TYPE_WEEK)) {
                tvTitle.setText(R.string.week_top);
                tvIncreaseText.setText(R.string.week_top_increase);
            } else if (mType.equalsIgnoreCase(TYPE_SEASON)) {
                tvTitle.setText(R.string.season_top);
                tvIncreaseText.setText(R.string.season_top_increase);
            } else if (mType.equalsIgnoreCase(TYPE_MONTH)) {
                tvTitle.setText(R.string.month_top);
                tvIncreaseText.setText(R.string.month_top_increase);
            }

        }

        tvIncreaseValue.setText(StringFromatUtils.get2PointPercent(value));
    }

}
