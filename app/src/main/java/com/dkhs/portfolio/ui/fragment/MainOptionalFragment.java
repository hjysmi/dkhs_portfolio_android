/**
 * @Title MainOptionalStock.java
 * @Package com.dkhs.portfolio.ui.fragment
 * @Description TODO(用一句话描述该文件做什么)
 * @author zjz
 * @date 2015-2-5 下午3:02:49
 * @version V1.0
 */
package com.dkhs.portfolio.ui.fragment;

import java.io.Serializable;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.dkhs.portfolio.R;
import com.dkhs.portfolio.ui.BaseSelectActivity;
import com.dkhs.portfolio.ui.EditTabFundActivity;
import com.dkhs.portfolio.ui.FundsOrderActivity;
import com.dkhs.portfolio.ui.EditTabStockActivity;
import com.dkhs.portfolio.ui.SelectAddOptionalActivity;
import com.dkhs.portfolio.ui.SelectStockActivity;
import com.dkhs.portfolio.ui.eventbus.IDataUpdateListener;
import com.dkhs.portfolio.utils.PromptManager;
import com.dkhs.portfolio.utils.UIUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;

/**
 * @ClassName MainOptionalStock
 * @Description TODO(这里用一句话描述这个类的作用)
 * @author zjz
 * @date 2015-2-5 下午3:02:49
 * @version 1.0
 */
public class MainOptionalFragment extends BaseFragment implements OnClickListener, IDataUpdateListener {

    @ViewInject(R.id.btn_titletab_right)
    private Button btnTabRight;
    @ViewInject(R.id.btn_titletab_left)
    private Button btnTabLeft;

    @ViewInject(R.id.btn_right)
    private Button btnRight;
    @ViewInject(R.id.btn_right_second)
    private Button btnSecRight;
    @ViewInject(R.id.btn_back)
    private Button btnLeft;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        tabStockFragment = new TabStockFragment();
        tabFundsFragment = new TabFundsFragment();
        tabFundsFragment.setDataUpdateListener(this);
        tabStockFragment.setDataUpdateListener(this);
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
        btnLeft.setCompoundDrawablesWithIntrinsicBounds(R.drawable.btn_paihang_selecter, 0, 0, 0);
        displayFragmentA();

    }

    private void setOptionTitleBar() {
        btnLeft.setVisibility(View.GONE);
        btnRight.setCompoundDrawablesWithIntrinsicBounds(R.drawable.btn_search_select, 0, 0, 0);
        btnRight.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), SelectAddOptionalActivity.class);
                // startActivity(intent);
                UIUtils.startAminationActivity(getActivity(), intent);
            }
        });
        btnSecRight.setOnClickListener(new OnClickListener() {

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
        btnLeft.setVisibility(View.VISIBLE);
        btnLeft.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), FundsOrderActivity.class);
                UIUtils.startAminationActivity(getActivity(), intent);
                // startActivity(intent);
            }
        });
        btnRight.setCompoundDrawablesWithIntrinsicBounds(R.drawable.btn_add_select, 0, 0, 0);
        btnRight.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                if (!UIUtils.iStartLoginActivity(getActivity())) {
                    tabFundsFragment.addItem();
                }
            }
        });
        btnSecRight.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // tabFundsFragment.editFund();
                startActivityForResult(EditTabFundActivity.getIntent(getActivity()), 1722);
                UIUtils.setOverridePendingAmin(getActivity());
            }
        });
    }

    @OnClick({ R.id.btn_titletab_right, R.id.btn_titletab_left })
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.btn_titletab_right: {
                btnTabRight.setEnabled(false);
                btnTabLeft.setEnabled(true);
                displayFragmentA();
                // PromptManager.showCustomToast(R.drawable.ic_toast_gantan, R.string.message_timeout);
            }
                break;
            case R.id.btn_titletab_left: {
                btnTabRight.setEnabled(true);
                btnTabLeft.setEnabled(false);
                displayFragmentB();

            }
                break;
            case R.id.btn_right: {
                // Intent intent = new Intent(this, OrderHistoryActivity.class);
                // startActivity(intent);
            }
            default:
                break;
        }
    }

    private TabStockFragment tabStockFragment;
    private TabFundsFragment tabFundsFragment;

    protected void displayFragmentA() {
        setOptionTitleBar();
        if (null != tabStockFragment) {

            FragmentTransaction ft = getChildFragmentManager().beginTransaction();
            if (null != tabStockFragment && tabStockFragment.isAdded()) { // if the fragment is already in container
                ft.show(tabStockFragment);
            } else { // fragment needs to be added to frame container
                ft.add(R.id.view_datalist, tabStockFragment, "A");
            }
            if (tabFundsFragment.isAdded()) {
                ft.hide(tabFundsFragment);
                tabFundsFragment.setDataUpdateListener(null);
            }
            tabStockFragment.setDataUpdateListener(this);
            tabStockFragment.refreshEditView();
            ft.commit();
        }

    }

    protected void displayFragmentB() {
        setCombinationBar();
        if (null == tabFundsFragment) {
            return;
        }
        FragmentTransaction ft = getChildFragmentManager().beginTransaction();
        if (tabFundsFragment.isAdded()) { // if the fragment is already in container
            ft.show(tabFundsFragment);
        } else { // fragment needs to be added to frame container
            ft.add(R.id.view_datalist, tabFundsFragment, "B");
        }
        if (tabStockFragment.isAdded()) {
            ft.hide(tabStockFragment);
            tabStockFragment.setDataUpdateListener(null);
        }
        tabFundsFragment.setDataUpdateListener(this);
        tabFundsFragment.refreshEditView();
        ft.commit();
    }

    @Override
    public void dataUpdate(boolean isEmptyData) {
        if (null != btnSecRight) {
            if (isEmptyData) {

                btnSecRight.setVisibility(View.GONE);
            } else {
                btnSecRight.setVisibility(View.VISIBLE);

            }
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 777) {
            tabStockFragment.onActivityResult(requestCode, resultCode, data);
        } else if (requestCode == 1722) {
            tabFundsFragment.onActivityResult(requestCode, resultCode, data);
        }
    }

}
