/**
 * @Title FragmentNetValueTrend.java
 * @Package com.dkhs.portfolio.ui.fragment
 * @Description TODO(用一句话描述该文件做什么)
 * @author zjz
 * @date 2014-9-1 下午1:52:54
 * @version V1.0
 */
package com.dkhs.portfolio.ui.fragment;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;

import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.CompoundButton.OnCheckedChangeListener;

import com.dkhs.portfolio.R;
import com.dkhs.portfolio.bean.CombinationBean;
import com.dkhs.portfolio.engine.MyCombinationEngineImpl;
import com.dkhs.portfolio.net.DataParse;
import com.dkhs.portfolio.net.ParseHttpListener;
import com.dkhs.portfolio.ui.CombinationDetailActivity;
import com.dkhs.portfolio.ui.ITouchListener;
import com.dkhs.portfolio.ui.widget.HScrollTitleView;
import com.dkhs.portfolio.ui.widget.HScrollTitleView.ISelectPostionListener;
import com.dkhs.portfolio.ui.widget.ScrollViewPager;
import com.dkhs.portfolio.utils.ColorTemplate;
import com.dkhs.portfolio.utils.StringFromatUtils;

/**
 * @ClassName FragmentNetValueTrend
 * @Description TODO(这里用一句话描述这个类的作用)
 * @author zjz
 * @date 2014-9-1 下午1:52:54
 * @version 1.0
 */
public class FragmentNetValueTrend extends Fragment implements OnClickListener, FragmentLifecycle {

    // private EditText etCombinName;
    private TextView tvCombinName;
    private TextView tvCombinDesc;
    private TextView tvCombinCreateTime;
    private TextView tvIncreaseValue;
    private TextView tvIncreaseRatio;
    // private View viewNetvalueHead;
    private ImageView ivUpDownIcon;
    // private Button btnEditName;
    private Switch combinationCheck;
    private CombinationBean mCombinationBean;
    private MyCombinationEngineImpl mMyCombinationEngineImpl;
    MyPagerFragmentAdapter mPagerAdapter;

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

    }

    private void handleExtras(Bundle extras) {
        mCombinationBean = (CombinationBean) extras.getSerializable(CombinationDetailActivity.EXTRA_COMBINATION);

    }

    private void handleArguments(Bundle arguments) {
        // TODO Auto-generated method stub

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_netvalue_trend, null);
        // etCombinName = (EditText) view.findViewById(R.id.et_combination_name);
        mMyCombinationEngineImpl = new MyCombinationEngineImpl();
        ivUpDownIcon = (ImageView) view.findViewById(R.id.tv_combination_image_uporlow);
        tvCombinDesc = (TextView) view.findViewById(R.id.tv_combination_desc);
        tvCombinCreateTime = (TextView) view.findViewById(R.id.tv_combination_time);
        tvCombinName = (TextView) view.findViewById(R.id.tv_combination_name);
        tvIncreaseRatio = (TextView) view.findViewById(R.id.tv_income_netvalue);
        tvIncreaseValue = (TextView) view.findViewById(R.id.tv_history_netvalue);
        // viewNetvalueHead = view.findViewById(R.id.tv_combination_layout);
        // btnEditName = (Button) view.findViewById(R.id.btn_edit_combinname);
        // btnEditName.setOnClickListener(this);
        combinationCheck = (Switch) view.findViewById(R.id.combination_check);
        combinationCheck.setOnCheckedChangeListener(new OnComCheckListener());
        if (mCombinationBean.getIspublic().equals("0")) {
            combinationCheck.setChecked(true);
        } else {
            combinationCheck.setChecked(false);
        }
        if (mCombinationBean.getCreateUser() == null) {
            view.findViewById(R.id.rl_combination_check).setVisibility(View.VISIBLE);
            view.findViewById(R.id.combination_layout_check).setVisibility(View.VISIBLE);
        } else {
            view.findViewById(R.id.rl_combination_check).setVisibility(View.GONE);
            view.findViewById(R.id.combination_layout_check).setVisibility(View.INVISIBLE);

        }
        initTabPage(view);

        // if(null!=mCombinationBean&&
        // mCombinationBean.getCreateUser().getId().equalsIgnoreCase(PortfolioPreferenceManager.getStringValue(PortfolioPreferenceManager.KEY_USERID))){
        //
        //
        // }

        setupViewData();
        return view;
    }

    class OnComCheckListener implements OnCheckedChangeListener {

        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            // TODO Auto-generated method stub
            if (isChecked) {
                QueryCombinationDetailListener listener = new QueryCombinationDetailListener();
                mMyCombinationEngineImpl.changeCombinationIsPublic(mCombinationBean.getId(), "0", listener);
                listener.setLoadingDialog(getActivity()).beforeRequest();
            } else {
                QueryCombinationDetailListener listener = new QueryCombinationDetailListener();
                mMyCombinationEngineImpl.changeCombinationIsPublic(mCombinationBean.getId(), "1", listener);
                listener.setLoadingDialog(getActivity()).beforeRequest();
            }
        }

    }

    class QueryCombinationDetailListener extends ParseHttpListener<List<CombinationBean>> {

        @Override
        protected List<CombinationBean> parseDateTask(String jsonData) {
            JSONArray jsonObject = null;
            try {
                jsonObject = new JSONArray(jsonData);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            List<CombinationBean> object = DataParse.parseArrayJson(CombinationBean.class, jsonObject);
            return object;
        }

        @Override
        protected void afterParseData(List<CombinationBean> object) {
            if (null != object) {

            }

        }
    }

    private void setupViewData() {
        if (null != mCombinationBean) {
            updateIncreaseRatio(mCombinationBean.getNetvalue());

        }
    }

    Handler updateHandler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            System.out.println("hand update message");
            float netValue = (Float) msg.obj;
            // if (netValue > 1) {
            // ivUpDownIcon.setImageResource(R.drawable.ic_combination_up);
            // viewNetvalueHead.setBackgroundResource(R.color.red);
            // } else if (netValue < 1) {
            // ivUpDownIcon.setImageResource(R.drawable.ic_combination_down);
            // viewNetvalueHead.setBackgroundResource(R.color.green);
            //
            // } else {
            // ivUpDownIcon.setImageDrawable(null);
            // viewNetvalueHead.setBackgroundResource(R.color.red);
            // }
            updateIncreaseRatio(netValue);
        };
    };

    private void updateIncreaseRatio(float netValue) {
        tvIncreaseValue.setTextColor(ColorTemplate.getUpOrDrownCSL(netValue - 1));
        tvIncreaseValue.setText(StringFromatUtils.get4Point(netValue));
        tvIncreaseRatio.setTextColor(ColorTemplate.getUpOrDrownCSL(netValue - 1));
        tvIncreaseRatio.setText(StringFromatUtils.get2PointPercent((netValue - 1) * 100));
    }

    private HScrollTitleView hsTitle;
    // privaet view
    private ScrollViewPager mViewPager;

    private ArrayList<Fragment> fragmentList;

    // private void replaceDataList(Fragment fragment) {
    // // view_datalist
    // // if (null == loadDataListFragment) {
    // // loadDataListFragment = loadDataListFragment.getStockFragment(ViewType.STOCK_OPTIONAL_PRICE);
    // // }
    // getChildFragmentManager().beginTransaction().replace(R.id.rl_trend_layout, fragment).commit();
    // }

    TrendChartFragment mtrendFragment = new TrendChartFragment();

    private void replaceFragment(Fragment newFragment) {

        FragmentTransaction trasection = getChildFragmentManager().beginTransaction();
        if (!newFragment.isAdded()) {
            try {
                // FragmentTransaction trasection =
                getChildFragmentManager().beginTransaction();
                trasection.replace(R.id.rl_trend_layout, newFragment);
                trasection.addToBackStack(null);
                trasection.commit();

            } catch (Exception e) {
                // TODO: handle exception
                // AppConstants.printLog(e.getMessage());

            }
        } else {

            trasection.show(newFragment);
        }

    }

    private void initTabPage(View view) {

        String[] titleArray = getResources().getStringArray(R.array.trend_title);
        fragmentList = new ArrayList<Fragment>();// ViewPager中显示的数据
        // FragmentSwitchChart todayFragment = FragmentSwitchChart.newInstance(TrendChartFragment.TREND_TYPE_TODAY);
        // todayFragment.setUpdateHandler(updateHandler);
        // fragmentList.add(todayFragment);
        // fragmentList.add(FragmentSwitchChart.newInstance(TrendChartFragment.TREND_TYPE_SEVENDAY));
        // fragmentList.add(FragmentSwitchChart.newInstance(TrendChartFragment.TREND_TYPE_MONTH));
        // fragmentList.add(FragmentSwitchChart.newInstance(TrendChartFragment.TREND_TYPE_HISTORY));

        // fragmentList.add(TestFragment.getInstance());
        // fragmentList.add(TestFragment.getInstance());
        // fragmentList.add(TestFragment.getInstance());
        // fragmentList.add(TestFragment.getInstance());

        //
        // mViewPager = (ScrollViewPager) view.findViewById(R.id.pager);
        // mViewPager.setCanScroll(false);
        // mPagerAdapter = new MyPagerFragmentAdapter(getChildFragmentManager(), fragmentList, titleArray);
        // mViewPager.setAdapter(mPagerAdapter);
        // mViewPager.setOnPageChangeListener(pageChangeListener);

        hsTitle = (HScrollTitleView) view.findViewById(R.id.hs_title);
        // String[] titleArray = getResources().getStringArray(R.array.quotes_title);
        hsTitle.setTitleList(titleArray, getResources().getDimensionPixelSize(R.dimen.title_2text_length));
        hsTitle.setSelectPositionListener(titleSelectPostion);

        // TabPageIndicator indicator = (TabPageIndicator) view.findViewById(R.id.indicator);
        // indicator.setViewPager(mViewPager);
        // replaceFragment(fragmentList.get(0));
        mtrendFragment.setSelectType(TrendChartFragment.TREND_TYPE_TODAY);
        replaceFragment(mtrendFragment);
    }

    ISelectPostionListener titleSelectPostion = new ISelectPostionListener() {

        @Override
        public void onSelectPosition(int position) {

            // replaceFragment(fragmentList.get(position));
            String type = TrendChartFragment.TREND_TYPE_TODAY;
            switch (position) {
                case 0: {
                    type = TrendChartFragment.TREND_TYPE_TODAY;
                }
                    break;
                case 1: {
                    type = TrendChartFragment.TREND_TYPE_SEVENDAY;

                }
                    break;
                case 2: {
                    type = TrendChartFragment.TREND_TYPE_MONTH;

                }
                    break;
                case 3: {

                    type = TrendChartFragment.TREND_TYPE_HISTORY;
                }
                    break;

                default:
                    break;
            }
            mtrendFragment.setSelectType(type);
        }
    };

    private class MyPagerFragmentAdapter extends FragmentPagerAdapter {

        private List<Fragment> fragmentList;
        private String[] titleList;

        public MyPagerFragmentAdapter(FragmentManager fm, ArrayList<Fragment> fragmentList2, String[] titleList) {
            super(fm);
            this.fragmentList = fragmentList2;
            this.titleList = titleList;
        }

        @Override
        public Fragment getItem(int arg0) {

            return (fragmentList == null || fragmentList.size() == 0) ? null : fragmentList.get(arg0);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return (titleList.length > position) ? titleList[position] : "";
        }

        @Override
        public int getCount() {
            return fragmentList == null ? 0 : fragmentList.size();
        }

    }

    private OnPageChangeListener pageChangeListener = new OnPageChangeListener() {

        int currentPosition = 0;

        @Override
        public void onPageSelected(int newPosition) {

            Fragment fragmentToShow = (Fragment) mPagerAdapter.getItem(newPosition);
            fragmentToShow.setUserVisibleHint(true);

            Fragment fragmentToHide = (Fragment) mPagerAdapter.getItem(currentPosition);
            fragmentToHide.setUserVisibleHint(false);
            currentPosition = newPosition;
        }

        @Override
        public void onPageScrolled(int arg0, float arg1, int arg2) {
        }

        public void onPageScrollStateChanged(int arg0) {
        }
    };

    @Override
    public void onClick(View v) {
        int id = v.getId();
        // switch (id) {
        // case R.id.btn_edit_combinname: {

        // if (tvCombinName.getVisibility() == View.VISIBLE) {
        // btnEditName.setBackgroundResource(R.drawable.action_alias_ok);
        // // etCombinName.setHint(tvCombinName.getText());
        // etCombinName.setText(tvCombinName.getText());
        // tvCombinName.setVisibility(View.GONE);
        // etCombinName.setVisibility(View.VISIBLE);
        // } else {
        // String nameText = etCombinName.getText().toString();
        // if (TextUtils.isEmpty(nameText)) {
        // // tvCombinName.setText(etCombinName.getText());
        // // Toast.makeText(getActivity(), R.string.tips_combination_name_null, Toast.LENGTH_LONG).show();
        // } else {
        // tvCombinName.setText(etCombinName.getText());
        // new MyCombinationEngineImpl().updateCombination(mCombinationBean.getId() + "", nameText, null);
        // }
        // tvCombinName.setVisibility(View.VISIBLE);
        // etCombinName.setVisibility(View.GONE);
        // btnEditName.setBackgroundResource(R.drawable.action_alias);
        // }
        // }
        //
        // break;
        //
        // default:
        // break;
        // }

    }

    /**
     * @Title
     * @Description TODO: (用一句话描述这个方法的功能)
     * @return
     */
    @Override
    public void onPauseFragment() {
        System.out.println("Fragment net value trend onPauseFragment（）");

    }

    /**
     * @Title
     * @Description TODO: (用一句话描述这个方法的功能)
     * @return
     */
    @Override
    public void onResumeFragment() {
        // TODO Auto-generated method stub

    }

    private ITouchListener mTouchListener;

    public void setITouchListener(ITouchListener touchListener) {
        this.mTouchListener = touchListener;
    }
}
