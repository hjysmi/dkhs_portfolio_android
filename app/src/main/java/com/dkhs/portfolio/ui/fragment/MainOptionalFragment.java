/**
 * @Title MainOptionalStock.java
 * @Package com.dkhs.portfolio.ui.fragment
 * @Description TODO(用一句话描述该文件做什么)
 * @author zjz
 * @date 2015-2-5 下午3:02:49
 * @version V1.0
 */
package com.dkhs.portfolio.ui.fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

import com.dkhs.portfolio.R;
import com.dkhs.portfolio.ui.EditTabCombinationActivity;
import com.dkhs.portfolio.ui.EditTabFundActivity;
import com.dkhs.portfolio.ui.EditTabStockActivity;
import com.dkhs.portfolio.ui.MainActivity;
import com.dkhs.portfolio.ui.SelectGeneralActivity;
import com.dkhs.portfolio.ui.adapter.BasePagerFragmentAdapter;
import com.dkhs.portfolio.ui.eventbus.BusProvider;
import com.dkhs.portfolio.ui.eventbus.IDataUpdateListener;
import com.dkhs.portfolio.ui.eventbus.NewIntent;
import com.dkhs.portfolio.ui.widget.TabWidget;
import com.dkhs.portfolio.utils.UIUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.squareup.otto.Subscribe;

import java.util.ArrayList;


/**
 * @author zjz
 * @version 1.0
 * @ClassName MainOptionalStock
 * @Description TODO(这里用一句话描述这个类的作用)
 * @date 2015-2-5 下午3:02:49
 */
public class MainOptionalFragment extends VisiableLoadFragment implements IDataUpdateListener {

    @ViewInject(R.id.vp)
    ViewPager mVp;
    @ViewInject(R.id.btn_header_right)
    private TextView btnRight;
    @ViewInject(R.id.btn_header_back)
    private TextView btnLeft;
    @ViewInject(R.id.title_bottom_line)
    private View bottomLine;


    private String mUserId;
    private TabWidget tabWidget;
    private BasePagerFragmentAdapter adapter;

    public static MainOptionalFragment getMainFragment(String userId) {
        MainOptionalFragment fragment = new MainOptionalFragment();
        Bundle args = new Bundle();
        args.putString(FragmentSelectStockFund.ARGUMENT_USER_ID, userId);
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getArguments();

        if (null != bundle) {
            mUserId = bundle.getString(FragmentSelectStockFund.ARGUMENT_USER_ID);
        }
        tabStockFragment = TabStockFragment.getTabStockFragment(mUserId);
        tabConbinationFragment = TabConbinationFragment.getFragment(mUserId);
        tabFundsFragment = TabFundsFragment.getFragment(mUserId);
        tabConbinationFragment.setDataUpdateListener(this);
        tabStockFragment.setDataUpdateListener(this);
        tabFundsFragment.setDataUpdateListener(this);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
    }

    @Override
    public int setContentLayoutId() {
        return R.layout.fragment_main_optionalstock;
    }

    private ArrayList<Fragment> fragments;

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (TextUtils.isEmpty(mUserId)) {
            btnLeft.setVisibility(View.GONE);
            btnLeft.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View v) {
                    Fragment tFundFragment = fragments.get(mVp.getCurrentItem());

                    if (tFundFragment instanceof TabFundsFragment) {

                        if (null != tFundFragment && !((TabFundsFragment) tFundFragment).getDataList().isEmpty()) {
                            Intent intent = EditTabFundActivity.newIntent(getActivity(), tabFundsFragment.getDataList());
                            startActivityForResult(intent, 888);
                            UIUtils.setOverridePendingAnin(getActivity());
                        }
                    }

                }
            });
            bottomLine.setBackgroundColor(getResources().getColor(R.color.drivi_line));

            btnRight.setVisibility(View.VISIBLE);
            btnRight.setCompoundDrawablesWithIntrinsicBounds(R.drawable.btn_search_select, 0, 0, 0);
            btnRight.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getActivity(), SelectGeneralActivity.class);
                    UIUtils.startAnimationActivity(getActivity(), intent);
                }
            });
        } else {
            setBackTitleBar();
        }
//        displayFragmentA();
        fragments = new ArrayList<>();
        fragments.add(tabFundsFragment);

        fragments.add(tabStockFragment);
        fragments.add(tabConbinationFragment);
        adapter = new BasePagerFragmentAdapter(getChildFragmentManager(), fragments);
        mVp.setAdapter(new BasePagerFragmentAdapter(getChildFragmentManager(), fragments));
        mVp.setOnPageChangeListener(new OnPagerListener());
        tabWidget = new TabWidget(view);
        tabWidget.setOnSelectListener(new TabWidget.OnSelectListener() {
            @Override
            public void onSelect(int position) {
                mVp.setCurrentItem(position);
            }
        });
        mVp.setCurrentItem(0);
        tabWidget.setSelection(0);
        BusProvider.getInstance().register(this);
        if (getActivity() instanceof MainActivity) {
            Bundle bundle = ((MainActivity) getActivity()).mBundle;
            if (bundle != null)
                handIntent(bundle);
        }

    }

    @Override
    public void onDestroyView() {
        BusProvider.getInstance().unregister(this);
        super.onDestroyView();
    }

    private void handIntent(Bundle bundle) {
        if (bundle.containsKey("option_index")) {
            int index = bundle.getInt("option_index", 0);
            mVp.setCurrentItem(index);
        }

    }

    @Subscribe
    public void newIntent(NewIntent newIntent) {
        handIntent(newIntent.bundle);
    }

    @Override
    public void requestData() {

    }


    private void setBackTitleBar() {
        btnRight.setVisibility(View.GONE);
        btnLeft.setVisibility(View.VISIBLE);
        btnLeft.setText("");
        btnLeft.setCompoundDrawablesWithIntrinsicBounds(R.drawable.btn_back_selector, 0, 0, 0);
        btnLeft.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().finish();

            }
        });
    }


    private static final String TAG = MainOptionalFragment.class.getSimpleName();

    @Override
    public void onViewHide() {
        try {

            if (null != mVp && null != adapter) {
                Fragment fragment = adapter.getItem(mVp.getCurrentItem());
                if (fragment instanceof VisiableLoadFragment) {
                    ((VisiableLoadFragment) fragment).onViewHide();
                }
            }


        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onViewShow() {
        try {

            if (null != adapter && null != mVp) {

                Fragment fragment = adapter.getItem(mVp.getCurrentItem());
                if (fragment instanceof VisiableLoadFragment) {
                    ((VisiableLoadFragment) fragment).onViewShow();
                } else {
                    fragment.onResume();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void setCombinationBar() {

        btnRight.setVisibility(View.VISIBLE);

        btnRight.setCompoundDrawablesWithIntrinsicBounds(R.drawable.btn_add_select, 0, 0, 0);
        btnRight.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                if (!UIUtils.iStartLoginActivity(getActivity())) {
                    tabConbinationFragment.addItem();
                }
            }
        });
        btnLeft.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // tabFundsFragment.editFund();
                startActivityForResult(EditTabCombinationActivity.getIntent(getActivity()), 1722);
                UIUtils.setOverridePendingAnin(getActivity());
            }
        });
    }


    private void setOptionTitleBar() {
        btnRight.setVisibility(View.VISIBLE);
        btnRight.setCompoundDrawablesWithIntrinsicBounds(R.drawable.btn_search_select, 0, 0, 0);
        btnRight.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), SelectGeneralActivity.class);
                UIUtils.startAnimationActivity(getActivity(), intent);
            }
        });
        btnLeft.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {

                if (null != tabStockFragment && !tabStockFragment.getDataList().isEmpty()) {
                    Intent intent = EditTabStockActivity.newIntent(getActivity(), tabStockFragment.getDataList());
                    startActivityForResult(intent, 777);
                    UIUtils.setOverridePendingAnin(getActivity());
                }

            }
        });
    }

    private void setFundBar() {
        btnRight.setVisibility(View.VISIBLE);
        btnRight.setCompoundDrawablesWithIntrinsicBounds(R.drawable.btn_search_select, 0, 0, 0);
        btnRight.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), SelectGeneralActivity.class);
                UIUtils.startAnimationActivity(getActivity(), intent);
            }
        });

        Fragment fragment = fragments.get(mVp.getCurrentItem());

        if (fragment instanceof TabFundsFragment) {
            final TabFundsFragment tFundFragment = (TabFundsFragment) fragment;
            boolean isEditAble = (null != tFundFragment && !tFundFragment.getDataList().isEmpty());
            btnLeft.setVisibility(isEditAble ? View.VISIBLE : View.GONE);
            btnLeft.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View v) {
                    if (null != tFundFragment && !tFundFragment.getDataList().isEmpty()) {
                        Intent intent = EditTabFundActivity.newIntent(getActivity(), tabFundsFragment.getDataList());
                        startActivityForResult(intent, 888);
                        UIUtils.setOverridePendingAnin(getActivity());
                    }

                }
            });
        }

    }


    private TabStockFragment tabStockFragment;
    private TabFundsFragment tabFundsFragment;
    private TabConbinationFragment tabConbinationFragment;
    private Fragment selectFragemnt;

    protected void displayFragmentA() {
        if (TextUtils.isEmpty(mUserId)) {

            setFundBar();
        }
        tabStockFragment.setDataUpdateListener(null);
        tabConbinationFragment.setDataUpdateListener(null);
        tabFundsFragment.setDataUpdateListener(this);
        tabFundsFragment.refreshEditView();

    }

    private void displayFragmentC() {
        if (TextUtils.isEmpty(mUserId)) {
            setCombinationBar();
        }
        if (null == tabConbinationFragment) {
            return;
        }

        tabStockFragment.setDataUpdateListener(null);
        tabFundsFragment.setDataUpdateListener(null);
        tabConbinationFragment.setDataUpdateListener(this);
        tabConbinationFragment.refreshEditView();
    }

    protected void displayFragmentB() {
        if (TextUtils.isEmpty(mUserId)) {
            setOptionTitleBar();
        }
        tabFundsFragment.setDataUpdateListener(null);
        tabConbinationFragment.setDataUpdateListener(null);
        tabStockFragment.setDataUpdateListener(this);
        tabStockFragment.refreshEditView();

    }


    @Override
    public void dataUpdate(boolean isEmptyData) {
        if (null != btnLeft && TextUtils.isEmpty(mUserId)) {
            if (isEmptyData) {

                btnLeft.setVisibility(View.GONE);
            } else {
                btnLeft.setVisibility(View.VISIBLE);

            }
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 777) {
            tabStockFragment.onActivityResult(requestCode, resultCode, data);
        } else if (requestCode == 1722) {
            tabConbinationFragment.onActivityResult(requestCode, resultCode, data);

        } else if (requestCode == 888) {
            tabFundsFragment.onActivityResult(requestCode, resultCode, data);
        }
    }


    class OnPagerListener implements ViewPager.OnPageChangeListener {

        @Override
        public void onPageScrolled(int i, float v, int i2) {

        }

        @Override
        public void onPageSelected(int i) {
            tabWidget.setSelection(i);
            switch (i) {
                case 0:
                    displayFragmentA();

                    break;
                case 1:
                    displayFragmentB();

                    break;
                case 2:
                    displayFragmentC();
                    break;
            }

        }

        @Override
        public void onPageScrollStateChanged(int i) {

        }
    }


}