/**
 * @Title AddConbinationStockActivity.java
 * @Package com.dkhs.portfolio.ui
 * @Description TODO(用一句话描述该文件做什么)
 * @author zjz
 * @date 2014-8-28 下午12:11:20
 * @version V1.0
 */
package com.dkhs.portfolio.ui;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.ContextThemeWrapper;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.Toast;

import com.dkhs.portfolio.R;
import com.dkhs.portfolio.bean.SelectStockBean;
import com.dkhs.portfolio.ui.adapter.SelectFundAdapter;
import com.dkhs.portfolio.ui.fragment.FragmentSearchStockFund;
import com.dkhs.portfolio.ui.fragment.FragmentSelectStockFund;
import com.dkhs.portfolio.ui.widget.HScrollTitleView;
import com.dkhs.portfolio.ui.widget.HScrollTitleView.ISelectPostionListener;
import com.dkhs.portfolio.ui.widget.TextImageButton;

import org.parceler.Parcels;

import java.util.ArrayList;
import java.util.List;

/**
 * @author zjz
 * @version 1.0
 * @ClassName AddConbinationStockActivity
 * @Description TODO(这里用一句话描述这个类的作用)
 * @date 2014-8-28 下午12:11:20
 */
public abstract class BaseSelectActivity extends ModelAcitivity implements OnClickListener {
    public static final String KEY_SELECT_STOCK = "key_select_stock";
    public static final String KEY_ISADJUST_COMBINATION = "key_isadjust_combination";
    // protected static final boolean is_load_fund = ;
    // protected static final boolean is_load_stock =true;
    private GridView mSelctStockView;
    private SelectFundAdapter mSelectStockAdapter;
    private Button btnAdd;
    ArrayList<FragmentSelectStockFund> fragmentList = new ArrayList<FragmentSelectStockFund>();// ViewPager中显示的数据
    private EditText etSearchKey;
    private FragmentSearchStockFund mSearchFragment;

    private View mStockPageView;
    private View mSearchListView;

    private Button btnOrder;
    private boolean isAdjustCombination;
    private TextImageButton btnBack;
    public static List<SelectStockBean> mSelectList = new ArrayList<SelectStockBean>();
    public String fromCreate;
    private boolean isFrist;

    private HScrollTitleView hsTitle;

    @Override
    protected void onCreate(Bundle arg0) {
        // setTheme(R.style.Theme_PageIndicatorDefaults);
        super.onCreate(arg0);

        setContentView(R.layout.activity_add_conbina_stock);
        hadFragment=true;
        // handle intent extras
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            handleExtras(extras);
        }
        initView();
        setupViewData();
        btnBack.setOnClickListener(new OnBackListener());
    }

    private void handleExtras(Bundle extras) {

//        ArrayList<SelectStockBean> listStock = (ArrayList<SelectStockBean>) extras
//                .getSerializable(BaseSelectActivity.ARGUMENT_SELECT_LIST);

        ArrayList<SelectStockBean> listStock;
        listStock = Parcels.unwrap(extras.getParcelable(BaseSelectActivity.ARGUMENT_SELECT_LIST));
        isAdjustCombination = extras.getBoolean(KEY_ISADJUST_COMBINATION, false);
        fromCreate = extras.getString(FROM_CREATE_TITLE);
        isFrist = extras.getBoolean("isFrist");
        if (null != listStock) {
            // for (ConStockBean stockBean : listStock) {
            //
            mSelectList.addAll(listStock);

            // }
        }
    }

    private void setupViewData() {
        mSelectStockAdapter = new SelectFundAdapter(this, mSelectList);
        mSelctStockView.setAdapter(mSelectStockAdapter);
        // btnAdd.setText(getString(R.string.add_postional_format, mSelectList.size()));
        btnAdd.setOnClickListener(this);
        etSearchKey.addTextChangedListener(mTextWatcher);

        if (getLoadByType() == ListViewType.FUND) {
            setTitle(R.string.select_fund);
            mSelctStockView.setNumColumns(2);
            btnOrder.setVisibility(View.VISIBLE);
            etSearchKey.setHint(R.string.search_fund_hint);
        } else if (getLoadByType() == ListViewType.STOCK) {
            btnOrder.setVisibility(View.GONE);
            if (isFrist) {
                setTitle(R.string.create_funds);

            } else {

                setTitle(R.string.select_stock);
            }
            mSelctStockView.setNumColumns(3);

        } else if (getLoadByType() == ListViewType.ADD_OPTIONAL) {
            btnOrder.setVisibility(View.GONE);
            setTitle(R.string.add_optional_stock);
            mSelctStockView.setNumColumns(3);
            etSearchKey.setHint(R.string.search_stockandfunds);

        }


        if (mSelectList.size() > 0) {
            btnAdd.setEnabled(true);
            // btnAdd.setTextColor(Color.WHITE);
//            btnAdd.setText(getString(R.string.add_postional_format, mSelectList.size()));
        } else {
            btnAdd.setEnabled(false);
//            String name = getResources().getString(R.string.add_text) + "(0)";
//            btnAdd.setText(name);
            // modify by zcm ---2014.12.15
            // btnAdd.setTextColor(Color.WHITE);
            // modify by zcm ---2014.12.15
        }
        btnAdd.setText(R.string.next_step);
        initTabPage();
    }

    private void initData() {

    }

    private void initView() {

        mSelctStockView = (GridView) findViewById(R.id.rl_add_stocklist);
        btnAdd = getRightButton();
        btnBack = getBtnBack();
        btnAdd.setBackgroundDrawable(null);
        View mSearchStock;
        mSearchStock = findViewById(R.id.rl_search_stock);
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
        mSearchFragment = getSearchFragment();
        if (null != mSearchFragment) {
            getSupportFragmentManager().beginTransaction().replace(R.id.rl_stock_searchview, mSearchFragment).commit();
        }
    }

    private ViewPager pager;

    private void initTabPage() {

        hsTitle = (HScrollTitleView) findViewById(R.id.hs_title);
        hsTitle.setTitleList(getResources().getStringArray(getTitleRes()));
        hsTitle.setSelectPositionListener(titleSelectPostion);

        // ArrayList<String> tileList = new ArrayList<String>();
        int titleArrayRes = 0;
        pager = (ViewPager) findViewById(R.id.pager);
        // TabPageIndicator indicator = (TabPageIndicator) findViewById(R.id.indicator);

        setTabViewPage(fragmentList);

        pager.setAdapter(new SelectPagerFragmentAdapter(getSupportFragmentManager(), fragmentList));
        pager.setOnPageChangeListener(pageChangeListener);

        // indicator.setViewPager(pager);

    }

    OnPageChangeListener pageChangeListener = new OnPageChangeListener() {

        @Override
        public void onPageSelected(int arg0) {
            // if (!isFromTitle) {
            hsTitle.setSelectIndex(arg0);
            // }
            // isFromTitle = false;

        }

        @Override
        public void onPageScrolled(int arg0, float arg1, int arg2) {
            // TODO Auto-generated method stub

        }

        @Override
        public void onPageScrollStateChanged(int arg0) {
            // TODO Auto-generated method stub

        }
    };

    ISelectPostionListener titleSelectPostion = new ISelectPostionListener() {

        @Override
        public void onSelectPosition(int position) {
            if (null != pager) {
                pager.setCurrentItem(position);
                // isFromTitle = true;
            }
        }
    };

    private class SelectPagerFragmentAdapter extends FragmentPagerAdapter {

        private List<FragmentSelectStockFund> fragmentList;

        // private ArrayList<String> titleList;

        public SelectPagerFragmentAdapter(FragmentManager fm, ArrayList<FragmentSelectStockFund> fragmentList2) {
            super(fm);
            this.fragmentList = fragmentList2;
            // this.titleList = tileList;
        }

        // ViewPage中显示的内容
        @Override
        public Fragment getItem(int arg0) {

            return (fragmentList == null || fragmentList.size() == 0) ? null : fragmentList.get(arg0);
        }

        // Title中显示的内容
        @Override
        public CharSequence getPageTitle(int position) {
            // return (titleList.size() > position) ? titleList.get(position) : "";
            return "";
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
            if (isAdjustCombination) {
                setSelectBack(-1);
                return;
            }
            if (getLoadByType() == ListViewType.STOCK) {
                // showTypeDialog();
                setSelectBack(-1);
            } else if (getLoadByType() == ListViewType.ADD_OPTIONAL) {
                Toast.makeText(this, "添加到自选股", Toast.LENGTH_SHORT).show();
            } else {
                setSelectBack(-1);

            }
        }
    }

    class OnBackListener implements OnClickListener {

        @Override
        public void onClick(View v) {
            // TODO Auto-generated method stub
            // add by zcm --- 2014.12.16
            if (!TextUtils.isEmpty(etSearchKey.getText().toString())) {
                etSearchKey.setText("");
                etSearchKey.requestFocus();
                etSearchKey.clearFocus();
                return;
            }
            // add by zcm --- 2014.12.16
            if (null != fromCreate) {
                Intent intent = new Intent();
                setResult(999, intent);

                finish();
            } else {
                finish();
            }
        }

    }

    public void notifySelectDataChange(boolean isUpdataFragment) {
        if (mSelectList.size() > 0) {
            btnAdd.setEnabled(true);
            // btnAdd.setTextColor(Color.WHITE);
//            btnAdd.setText(getString(R.string.add_postional_format, mSelectList.size()));
        } else {
//            String name = getResources().getString(R.string.add_text) + "(0)";
            btnAdd.setEnabled(false);
//            btnAdd.setText(name);
            // btnAdd.setTextColor(getResources().getColor(android.R.color.darker_gray));
            // modify by zcm ---2014.12.17
            // btnAdd.setTextColor(Color.WHITE);
            // modify by zcm ---2014.12.17

        }

        // if (isUpdataFragment) {
        for (FragmentSelectStockFund fragment : fragmentList) {
            fragment.refreshSelect();
        }
        if (null != mSearchFragment) {
            mSearchFragment.refreshSelect();
        }

        // }
        mSelectStockAdapter.notifyDataSetChanged();
    }

    private void showTypeDialog() {
        new AlertDialog.Builder(new ContextThemeWrapper(this, android.R.style.Theme_Holo_Light_Dialog_NoActionBar))
                .setTitle("组合创建模式").setItems(R.array.create_type, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {

                        /* User clicked so do some stuff */
                // String[] items = getResources().getStringArray(R.array.create_type);
                // String type = items[which];
                // setCombinationBack(which);
                setSelectBack(which);
            }
        }).show();
    }

    public static final String ARGUMENT_SELECT_LIST = "list_select";
    public static final String ARGUMENT_CRATE_TYPE = "type";
    public static final int CRATE_TYPE_FAST = 0;
    public static final int CRATE_TYPE_CUSTOM = 1;
    public static final int FROM_CREATE = 999;
    public static final String FROM_CREATE_TITLE = "first_create";


    private void setSelectBack(int type) {
        Intent intent = new Intent();
        intent.putExtra(ARGUMENT_SELECT_LIST, Parcels.wrap(mSelectList));
        intent.putExtra(ARGUMENT_CRATE_TYPE, type);
        setResult(RESULT_OK, intent);

        finish();
    }

    public enum ListViewType {
        // 基金模式
        FUND(1),
        // 股票模式
        STOCK(2),
        // 添加自选股模式
        ADD_OPTIONAL(3);

        private int typeId;

        ListViewType(int type) {
            this.typeId = type;
        }

        public int getTypeId() {
            return typeId;
        }
    }

    protected abstract ListViewType getLoadByType();

    protected abstract int getTitleRes();

    protected abstract FragmentSearchStockFund getSearchFragment();

    protected abstract void setTabViewPage(List<FragmentSelectStockFund> fragmenList);

}
