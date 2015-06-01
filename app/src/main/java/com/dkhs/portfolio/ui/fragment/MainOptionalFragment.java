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
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.dkhs.portfolio.R;
import com.dkhs.portfolio.ui.EditTabCombinationActivity;
import com.dkhs.portfolio.ui.EditTabFundActivity;
import com.dkhs.portfolio.ui.EditTabStockActivity;
import com.dkhs.portfolio.ui.SelectAddOptionalActivity;
import com.dkhs.portfolio.ui.adapter.BasePagerFragmentAdapter;
import com.dkhs.portfolio.ui.eventbus.IDataUpdateListener;
import com.dkhs.portfolio.ui.widget.TabWidget;
import com.dkhs.portfolio.utils.UIUtils;
import com.lidroid.xutils.view.annotation.ViewInject;

import java.util.ArrayList;


/**
 * @author zjz
 * @version 1.0
 * @ClassName MainOptionalStock
 * @Description TODO(这里用一句话描述这个类的作用)
 * @date 2015-2-5 下午3:02:49
 */
public class MainOptionalFragment extends BaseFragment implements IDataUpdateListener {

    @ViewInject(R.id.vp)
    ViewPager mVp;
    @ViewInject(R.id.btn_header_right)
    private Button btnRight;
    @ViewInject(R.id.btn_header_back)
    private Button btnLeft;


    private String mUserId;
    private TabWidget tabWidget;

    public static MainOptionalFragment getMainFragment(String userId) {
        MainOptionalFragment fragment = new MainOptionalFragment();
        Bundle args = new Bundle();
        args.putString(FragmentSelectStockFund.ARGUMENT_USER_ID, userId);
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
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
        // TODO Auto-generated method stub
        return R.layout.fragment_main_optionalstock;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (TextUtils.isEmpty(mUserId)) {

        } else {
            setBackTitleBar();
        }
        displayFragmentA();
        ArrayList<Fragment> fragments = new ArrayList<>();
        fragments.add(tabStockFragment);
        fragments.add(tabFundsFragment);
        fragments.add(tabConbinationFragment);
        mVp.setAdapter(new BasePagerFragmentAdapter(getChildFragmentManager(), fragments));
        mVp.setOnPageChangeListener(new OnPagerListener());
        tabWidget = new TabWidget(view);
        tabWidget.setOnSelectListener(new TabWidget.OnSelectListener() {
            @Override
            public void onSelect(int position) {
                mVp.setCurrentItem(position);
            }
        });

    }


    private void setBackTitleBar() {
        btnRight.setVisibility(View.VISIBLE);
        btnRight.setVisibility(View.GONE);
        btnLeft.setVisibility(View.GONE);
        btnRight.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().finish();

            }
        });
    }

    private void setOptionTitleBar() {
        btnRight.setVisibility(View.VISIBLE);
        btnRight.setCompoundDrawablesWithIntrinsicBounds(R.drawable.btn_search_select, 0, 0, 0);
        btnRight.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), SelectAddOptionalActivity.class);
                // startActivity(intent);
                UIUtils.startAminationActivity(getActivity(), intent);
            }
        });
        btnLeft.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // Intent intent = new Intent(getActivity(), OptionEditActivity.class);
                // if (null != tabStockFragment) {
                // intent.putExtra(BaseSelectActivity.ARGUMENT_SELECT_LIST,
                // (Serializable) tabStockFragment.getDataList());
                // }
                if (null != tabStockFragment && !tabStockFragment.getDataList().isEmpty()) {
                    Intent intent = EditTabStockActivity.newIntent(getActivity(), tabStockFragment.getDataList());
                    startActivityForResult(intent, 777);
                    UIUtils.setOverridePendingAmin(getActivity());
                }

            }
        });
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
                UIUtils.setOverridePendingAmin(getActivity());
            }
        });
    }

    private void setFundBar() {
        btnRight.setVisibility(View.VISIBLE);
        btnRight.setCompoundDrawablesWithIntrinsicBounds(R.drawable.btn_search_select, 0, 0, 0);
        btnRight.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), SelectAddOptionalActivity.class);
                UIUtils.startAminationActivity(getActivity(), intent);
            }
        });

        btnLeft.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                if (null != tabFundsFragment && !tabFundsFragment.getDataList().isEmpty()) {
                    Intent intent = EditTabFundActivity.newIntent(getActivity(), tabFundsFragment.getDataList());
                    startActivityForResult(intent, 777);
                    UIUtils.setOverridePendingAmin(getActivity());
                }

            }
        });
    }


    private TabStockFragment tabStockFragment;
    private TabFundsFragment tabFundsFragment;
    private TabConbinationFragment tabConbinationFragment;

    protected void displayFragmentA() {
        if (TextUtils.isEmpty(mUserId)) {
            setOptionTitleBar();
        }
        tabStockFragment.setDataUpdateListener(this);
        tabStockFragment.refreshEditView();

    }

    private void displayFragmentC() {
        if (TextUtils.isEmpty(mUserId)) {
            setCombinationBar();
        }
        if (null == tabConbinationFragment) {
            return;
        }
        FragmentTransaction ft = getChildFragmentManager().beginTransaction();
        if (tabConbinationFragment.isAdded()) { // if the fragment is already in container
            ft.show(tabConbinationFragment);
        } else { // fragment needs to be added to frame container
            ft.add(R.id.view_datalist, tabConbinationFragment, "B");
        }
        if (tabStockFragment.isAdded()) {
            ft.hide(tabStockFragment);
            tabStockFragment.setDataUpdateListener(null);
        }
        tabConbinationFragment.setDataUpdateListener(this);
        tabConbinationFragment.refreshEditView();
        ft.commit();
    }

    protected void displayFragmentB() {
        setFundBar();
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
