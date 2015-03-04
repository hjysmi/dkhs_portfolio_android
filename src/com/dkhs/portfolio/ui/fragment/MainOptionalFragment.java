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
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.dkhs.portfolio.R;
import com.dkhs.portfolio.ui.EditTabFundActivity;
import com.dkhs.portfolio.ui.FundsOrderActivity;
import com.dkhs.portfolio.ui.OptionEditActivity;
import com.dkhs.portfolio.ui.OptionalStockListActivity;
import com.dkhs.portfolio.ui.SelectAddOptionalActivity;
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
public class MainOptionalFragment extends BaseFragment implements OnClickListener {

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
                startActivity(intent);
            }
        });
        btnSecRight.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), OptionEditActivity.class);
                startActivity(intent);
            }
        });
    }

    private void setCombinationBar() {
        btnLeft.setVisibility(View.VISIBLE);
        btnLeft.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), FundsOrderActivity.class);
                startActivity(intent);
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
                tabFundsFragment.editFund();
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

    private Fragment tabStockFragment;
    private TabFundsFragment tabFundsFragment;

    protected void displayFragmentA() {
        setOptionTitleBar();
        FragmentTransaction ft = getChildFragmentManager().beginTransaction();
        if (null != tabStockFragment && tabStockFragment.isAdded()) { // if the fragment is already in container
            ft.show(tabStockFragment);
        } else { // fragment needs to be added to frame container
            ft.add(R.id.view_datalist, tabStockFragment, "A");
        }
        if (tabFundsFragment.isAdded()) {
            ft.hide(tabFundsFragment);
        }
        ft.commit();
    }

    protected void displayFragmentB() {
        setCombinationBar();
        FragmentTransaction ft = getChildFragmentManager().beginTransaction();
        if (tabFundsFragment.isAdded()) { // if the fragment is already in container
            ft.show(tabFundsFragment);
        } else { // fragment needs to be added to frame container
            ft.add(R.id.view_datalist, tabFundsFragment, "B");
        }
        if (tabStockFragment.isAdded()) {
            ft.hide(tabStockFragment);
        }

        ft.commit();
    }

}
