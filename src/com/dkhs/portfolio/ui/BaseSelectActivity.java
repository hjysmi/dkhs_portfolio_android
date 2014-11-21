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
import java.util.ArrayList;
import java.util.List;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
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
import com.dkhs.portfolio.bean.ConStockBean;
import com.dkhs.portfolio.bean.SelectStockBean;
import com.dkhs.portfolio.ui.adapter.SelectFundAdapter;
import com.dkhs.portfolio.ui.fragment.FragmentSearchStockFund;
import com.dkhs.portfolio.ui.fragment.FragmentSelectStockFund;
import com.dkhs.portfolio.ui.widget.TabPageIndicator;

/**
 * @ClassName AddConbinationStockActivity
 * @Description TODO(这里用一句话描述这个类的作用)
 * @author zjz
 * @date 2014-8-28 下午12:11:20
 * @version 1.0
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
    private Button btnBack;
    public static List<SelectStockBean> mSelectList = new ArrayList<SelectStockBean>();
    public String fromCreate;

    @Override
    protected void onCreate(Bundle arg0) {
        setTheme(R.style.Theme_PageIndicatorDefaults);
        super.onCreate(arg0);

        setContentView(R.layout.activity_add_conbina_stock);

        // handle intent extras
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            handleExtras(extras);
        }
        setupUI(findViewById(R.id.rl_base_add_view));
        initView();
        setupViewData();
        btnBack.setOnClickListener(new OnBackListener());
    }

    private void handleExtras(Bundle extras) {

        ArrayList<SelectStockBean> listStock = (ArrayList<SelectStockBean>) extras
                .getSerializable(BaseSelectActivity.ARGUMENT_SELECT_LIST);
        isAdjustCombination = extras.getBoolean(KEY_ISADJUST_COMBINATION, false);
        fromCreate = extras.getString(FROM_CREATE_TITLE);
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
        } else if (getLoadByType() == ListViewType.STOCK) {
            btnOrder.setVisibility(View.GONE);
            setTitle(R.string.select_stock);
            mSelctStockView.setNumColumns(3);

        } else if (getLoadByType() == ListViewType.ADD_OPTIONAL) {
            btnOrder.setVisibility(View.GONE);
            setTitle(R.string.add_optional_stock);
            mSelctStockView.setNumColumns(3);

        }

        if (mSelectList.size() > 0) {
            btnAdd.setEnabled(true);
            btnAdd.setTextColor(Color.WHITE);
            btnAdd.setText(getString(R.string.add_postional_format, mSelectList.size()));
        } else {
            btnAdd.setEnabled(false);
            btnAdd.setText(R.string.add_text);
            btnAdd.setTextColor(getResources().getColor(android.R.color.darker_gray));

        }

        initTabPage();
    }

    private void initData() {

    }

    private void initView() {

        mSelctStockView = (GridView) findViewById(R.id.rl_add_stocklist);
        btnAdd = getRightButton();
        btnBack = getBtnBack();
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
    class OnBackListener implements OnClickListener{

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			if(null != fromCreate){
				Intent intent = new Intent();
		        setResult(999, intent);

		        finish();
			}else{
				finish();
			}
		}
    	
    }
    public void notifySelectDataChange(boolean isUpdataFragment) {
        if (mSelectList.size() > 0) {
            btnAdd.setEnabled(true);
            btnAdd.setTextColor(Color.WHITE);
            btnAdd.setText(getString(R.string.add_postional_format, mSelectList.size()));
        } else {
            btnAdd.setEnabled(false);
            btnAdd.setText(R.string.add_text);
            btnAdd.setTextColor(getResources().getColor(android.R.color.darker_gray));

        }

        // if (isUpdataFragment) {
        for (FragmentSelectStockFund fragment : fragmentList) {
            fragment.refreshSelect();
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
        intent.putExtra(ARGUMENT_SELECT_LIST, (Serializable) mSelectList);
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

    protected abstract FragmentSearchStockFund getSearchFragment();

    protected abstract void setTabViewPage(ArrayList<String> titleList, List<FragmentSelectStockFund> fragmenList);

}
