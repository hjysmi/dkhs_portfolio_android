/**
 * @Title AddConbinationStockActivity.java
 * @Package com.dkhs.portfolio.ui
 * @Description TODO(用一句话描述该文件做什么)
 * @author zjz
 * @date 2014-8-28 下午12:11:20
 * @version V1.0
 */
package com.dkhs.portfolio.ui;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.Toast;

import com.dkhs.portfolio.R;
import com.dkhs.portfolio.bean.ConStockBean;
import com.dkhs.portfolio.ui.adapter.SelectFundAdapter;
import com.dkhs.portfolio.ui.fragment.FragmentSelectStockFund;
import com.dkhs.portfolio.ui.widget.TabPageIndicator;
import com.dkhs.portfolio.utils.ColorTemplate;

/**
 * @ClassName AddConbinationStockActivity
 * @Description TODO(这里用一句话描述这个类的作用)
 * @author zjz
 * @date 2014-8-28 下午12:11:20
 * @version 1.0
 */
public abstract class BaseSelectActivity extends ModelAcitivity implements OnClickListener {
    public static final String KEY_SELECT_STOCK = "key_select_stock";
    // protected static final boolean is_load_fund = ;
    // protected static final boolean is_load_stock =true;
    private GridView mSelctStockView;
    private SelectFundAdapter mSelectStockAdapter;
    private Button btnAdd;
    private ArrayList<FragmentSelectStockFund> fragmentList = new ArrayList<FragmentSelectStockFund>();// ViewPager中显示的数据
    private EditText etSearchKey;
    private FragmentSelectStockFund mSearchFragment;

    private View mStockPageView;
    private View mSearchListView;

    private Button btnOrder;

    public static List<ConStockBean> mSelectList = new ArrayList<ConStockBean>();

    @Override
    protected void onCreate(Bundle arg0) {
        setTheme(R.style.Theme_PageIndicatorDefaults);
        super.onCreate(arg0);
        setContentView(R.layout.activity_add_conbina_stock);

        initView();
        setupViewData();

    }

    private void setupViewData() {
        mSelectStockAdapter = new SelectFundAdapter(this, mSelectList, isLoadBySelectFund());
        mSelctStockView.setAdapter(mSelectStockAdapter);
        btnAdd.setText(getString(R.string.add_postional_format, mSelectList.size()));
        btnAdd.setOnClickListener(this);
        etSearchKey.addTextChangedListener(mTextWatcher);

        if (isLoadBySelectFund()) {
            setTitle(R.string.select_fund);
            mSelctStockView.setNumColumns(2);
            btnOrder.setVisibility(View.VISIBLE);
        } else {
            btnOrder.setVisibility(View.GONE);
            setTitle(R.string.select_stock);
            mSelctStockView.setNumColumns(3);

        }

        initTabPage();
    }

    private void initData() {

    }

    private void initView() {

        mSelctStockView = (GridView) findViewById(R.id.rl_add_stocklist);
        btnAdd = getRightButton();

        etSearchKey = (EditText) findViewById(R.id.et_search_key);

        mStockPageView = findViewById(R.id.rl_stock_rowview);
        replaceSearchView();
        mSearchListView = findViewById(R.id.rl_stock_searchview);

        btnOrder = (Button) findViewById(R.id.btn_order);
    }

    public Button getOrderButton() {
        return btnOrder;
    }

    private void replaceSearchView() {
        mSearchFragment = setSearchFragment();
        if (null != mSearchFragment) {
            getSupportFragmentManager().beginTransaction().replace(R.id.rl_stock_searchview, mSearchFragment).commit();
        }
    }

    private void initTabPage() {

        ArrayList<String> tileList = new ArrayList<String>();

        ViewPager pager = (ViewPager) findViewById(R.id.pager);
        TabPageIndicator indicator = (TabPageIndicator) findViewById(R.id.indicator);

        setTabViewPage(tileList, fragmentList);

        pager.setAdapter(new SelectPagerFragmentAdapter(getSupportFragmentManager(), fragmentList, tileList));
        indicator.setViewPager(pager);

    }

    private class SelectPagerFragmentAdapter extends FragmentPagerAdapter {

        private List<FragmentSelectStockFund> fragmentList;
        private ArrayList<String> titleList;

        public SelectPagerFragmentAdapter(FragmentManager fm, ArrayList<FragmentSelectStockFund> fragmentList2,
                ArrayList<String> tileList) {
            super(fm);
            this.fragmentList = fragmentList2;
            this.titleList = tileList;
        }

        // ViewPage中显示的内容
        @Override
        public Fragment getItem(int arg0) {

            return (fragmentList == null || fragmentList.size() == 0) ? null : fragmentList.get(arg0);
        }

        // Title中显示的内容
        @Override
        public CharSequence getPageTitle(int position) {
            return (titleList.size() > position) ? titleList.get(position) : "";
        }

        @Override
        public int getCount() {
            return fragmentList == null ? 0 : fragmentList.size();
        }

    }

    TextWatcher mTextWatcher = new TextWatcher() {

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            // TODO Auto-generated method stub
            // mTextView.setText(s);//将输入的内容实时显示
        }

        @Override
        public void afterTextChanged(Editable s) {
            if (TextUtils.isEmpty(s)) {
                showPageView();
            } else {
                showSearchListView(s.toString());
            }
        }
    };

    private void showPageView() {
        mStockPageView.setVisibility(View.VISIBLE);
        mSearchListView.setVisibility(View.GONE);
    }

    private void showSearchListView(String editText) {
        if (mSearchListView.getVisibility() != View.VISIBLE) {
            mSearchListView.setVisibility(View.VISIBLE);
        }
        if (mStockPageView.getVisibility() != View.GONE) {
            mStockPageView.setVisibility(View.GONE);
        }
        mSearchFragment.searchByKey(editText);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mSelectList.clear();
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();

        if (RIGHTBUTTON_ID == id) {
            if (isLoadBySelectFund()) {
                setSelectBack();
            } else {
                showTypeDialog();
            }
        }
    }

    public void notifySelectDataChange(boolean isUpdataFragment) {
        btnAdd.setText(getString(R.string.add_postional_format, mSelectList.size()));
        // if (isUpdataFragment) {
        for (FragmentSelectStockFund fragment : fragmentList) {
            fragment.refreshSelect();
        }
        // }
        mSelectStockAdapter.notifyDataSetChanged();
    }

    private void showTypeDialog() {
        new AlertDialog.Builder(BaseSelectActivity.this).setTitle("组合创建模式")
                .setItems(R.array.create_type, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {

                        /* User clicked so do some stuff */
                        // String[] items = getResources().getStringArray(R.array.create_type);
                        // String type = items[which];
                        setCombinationBack(which);
                        setSelectBack();
                    }
                }).show();
    }

    private void setCombinationBack(int which) {
        if (null != mSelectList && mSelectList.size() > 0) {
            int length = mSelectList.size();
            int dutyValue = (100 / length);
            for (int i = 0; i < length; i++) {
                ConStockBean c = mSelectList.get(i);
                if (0 == which) {// 快速
                    c.setDutyValue(dutyValue);
                }
                if (i < ColorTemplate.DEFAULTCOLORS.length) {
                    c.setDutyColor(getResources().getColor(ColorTemplate.DEFAULTCOLORS[i]));

                } else {

                    c.setDutyColor(ColorTemplate.getRaddomColor());
                }

            }
        }

    }

    private void setSelectBack() {
        Intent intent = new Intent();
        intent.putExtra("list_select", (Serializable) mSelectList);
        setResult(RESULT_OK, intent);

        finish();
    }

    protected abstract boolean isLoadBySelectFund();

    protected abstract FragmentSelectStockFund setSearchFragment();

    protected abstract void setTabViewPage(ArrayList<String> titleList, List<FragmentSelectStockFund> fragmenList);

}
