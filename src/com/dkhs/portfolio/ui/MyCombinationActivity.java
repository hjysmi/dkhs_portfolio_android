/**
 * @Title MyCombinationActivity.java
 * @Package com.dkhs.portfolio.ui
 * @Description TODO(用一句话描述该文件做什么)
 * @author zjz
 * @date 2014-8-26 下午3:10:51
 * @version V1.0
 */
package com.dkhs.portfolio.ui;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.view.ContextThemeWrapper;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.inputmethod.InputMethodManager;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.Button;
import android.widget.GridView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.dkhs.portfolio.R;
import com.dkhs.portfolio.app.PortfolioApplication;
import com.dkhs.portfolio.bean.CombinationBean;
import com.dkhs.portfolio.bean.MoreDataBean;
import com.dkhs.portfolio.engine.MyCombinationEngineImpl;
import com.dkhs.portfolio.net.BasicHttpListener;
import com.dkhs.portfolio.net.DataParse;
import com.dkhs.portfolio.net.ParseHttpListener;
import com.dkhs.portfolio.ui.adapter.CombinationAdapter;
import com.dkhs.portfolio.ui.adapter.CombinationAdapter.IDelButtonListener;
import com.dkhs.portfolio.ui.fragment.MyCombinationListFragment;
import com.dkhs.portfolio.ui.fragment.UserCombinationListFragment;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.lidroid.xutils.util.LogUtils;

/**
 * @ClassName MyCombinationActivity
 * @Description 我的组合
 * @author zjz
 * @date 2014-8-26 下午3:10:51
 * @version 1.0
 */
public class MyCombinationActivity extends ModelAcitivity implements OnClickListener {
    // private GridView gvCombination;
    // private CombinationAdapter mCombinationAdapter;
    private Button btnMore;
    private Button btnRefresh;

    // private PopupWindow mPopMoreWindow;
    // private List<CombinationBean> mDataList = new ArrayList<CombinationBean>();
    private MyCombinationListFragment listFragment;

    @Override
    protected void onCreate(Bundle arg0) {
        super.onCreate(arg0);
        setContentView(R.layout.activity_mycombination);
        setTitle(R.string.my_combination);

        // initGridView();
        // this.getWindow().getDecorView().setOnTouchListener(this);

        initTitleView();

        // loadCombinationData();
        replaceCombinationListView();
    }

    /**
     * @Title
     * @Description TODO: (用一句话描述这个方法的功能)
     * @return
     */
    @Override
    protected void onStart() {
        super.onStart();
        // loadCombinationData();
    }

    private void initTitleView() {
        btnMore = getRightButton();
        btnMore.setBackgroundResource(R.drawable.ic_title_add);
        btnMore.setOnClickListener(this);

        btnRefresh = getSecondRightButton();
        btnRefresh.setOnClickListener(this);
        btnRefresh.setVisibility(View.VISIBLE);
        btnRefresh.setBackgroundResource(R.drawable.nav_refresh_selector);
        btnRefresh.setTag("refresh");

    }

    private void replaceCombinationListView() {
        listFragment = MyCombinationListFragment.getFragment();
        getSupportFragmentManager().beginTransaction().replace(R.id.content_layout, listFragment).commit();

    }

    //
    // private void loadCombinationData() {
    // // CombinationBean conBean1 = new CombinationBean("我的组合1", 1.152f, 11.22f);
    // // CombinationBean conBean2 = new CombinationBean("我的组合2", 1.153f, 15.22f);
    // // CombinationBean conBean3 = new CombinationBean("我的组合3", -1.152f, -11.22f);
    // // CombinationBean conBean4 = new CombinationBean("我的组合4", 1.152f, 13.22f);
    // // CombinationBean conBean5 = new CombinationBean("我的组合5", -1.154f, -10.22f);
    // // mDataList.add(conBean1);
    // // mDataList.add(conBean2);
    // // mDataList.add(conBean3);
    // // mDataList.add(conBean4);
    // // mDataList.add(conBean5);
    // new MyCombinationEngineImpl().getCombinationList(new ParseHttpListener<MoreDataBean<CombinationBean>>() {
    //
    // @Override
    // protected MoreDataBean<CombinationBean> parseDateTask(String jsonData) {
    //
    // MoreDataBean<CombinationBean> moreBean = null;
    // try {
    //
    // Gson gson = new GsonBuilder().enableComplexMapKeySerialization().create();
    //
    // moreBean = (MoreDataBean) gson.fromJson(jsonData, new TypeToken<MoreDataBean<CombinationBean>>() {
    // }.getType());
    //
    // } catch (Exception e) {
    // e.printStackTrace();
    // }
    //
    // return moreBean;
    //
    // }
    //
    // @Override
    // protected void afterParseData(MoreDataBean<CombinationBean> moreBean) {
    // // LogUtils.d("List<CombinationBean> size:" + dataList.size());
    // if (null != moreBean) {
    // List<CombinationBean> dataList = moreBean.getResults();
    // LogUtils.d("List<CombinationBean> size:" + dataList.size());
    // mDataList.clear();
    // mDataList.addAll(dataList);
    // mCombinationAdapter.notifyDataSetChanged();
    // }
    // }
    //
    // }.setLoadingDialog(MyCombinationActivity.this, R.string.loading));
    // mCombinationAdapter.notifyDataSetChanged();
    // }

    // @Override
    // public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
    //
    // // Intent intent = new Intent(this, CombinationDetailActivity.class);
    // // intent.putExtra(PositionAdjustActivity.KEY_VIEW_TYPE, PositionAdjustActivity.VALUE_ADJUST_CONBINA);
    // // startActivity(CombinationDetailActivity.newIntent(this, mDataList.get(position)));
    //
    // // new MyCombinationEngineImpl().queryCombinationDetail(mDataList.get(position).getId(),
    // // new ParseHttpListener<String>() {
    // //
    // // @Override
    // // protected String parseDateTask(String jsonData) {
    // // // TODO Auto-generated method stub
    // // return null;
    // // }
    // //
    // // @Override
    // // protected void afterParseData(String object) {
    // // // TODO Auto-generated method stub
    // //
    // // }
    // // });
    //
    // }

    // @Override
    // public boolean onTouch(View v, MotionEvent event) {
    // gvCombination.clearFocus();
    // InputMethodManager imm = (InputMethodManager) v.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
    // if (imm != null) {
    // imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
    // }
    // return super.onTouchEvent(event);
    // }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == RIGHTBUTTON_ID) {

            clickRightButton();
        } else if (id == SECONDRIGHTBUTTON_ID) {

            clickSecondButton();
        } else if (id == R.id.tv_add_combina) {
            // mCombinationAdapter.addItem();
            // mPopMoreWindow.dismiss();

            addNewCombination();
        } else if (id == R.id.tv_delete_combina) {
            btnMore.setText("取消");
            btnMore.setTag("del");
            btnMore.setBackgroundDrawable(null);

            btnRefresh.setTag("del");
            btnRefresh.setBackgroundResource(R.drawable.btn_delete_selector);
            // mCombinationAdapter.setDelStatus(true);
            // mPopMoreWindow.dismiss();
            listFragment.setListDelStatus(true);
            // gvCombination.setOnItemClickListener(null);

        }

    }

    private void clickRightButton() {
        if (btnMore.getTag() != null && btnMore.getTag().equals("cancel")) {
            // mCombinationAdapter.setDelStatus(false);
            listFragment.setListDelStatus(false);
            setButtonAdd();
        } else {
            addNewCombination();
        }

    }

    private void addNewCombination() {
        // startActivity(PositionAdjustActivity.newIntent(this, null));
        if (null != listFragment)
            listFragment.createNewCombination();

    }

    private void setButtonAdd() {
        btnMore.setTag("add");
        btnMore.setText("");
        btnMore.setBackgroundResource(R.drawable.ic_title_add);
        btnRefresh.setVisibility(View.VISIBLE);
    }

    public void setButtonCancel() {
        btnMore.setText(R.string.cancel);
        btnMore.setTag("cancel");
        btnMore.setBackgroundDrawable(null);

        btnRefresh.setVisibility(View.GONE);
    }

    private void setButtonRefresh() {
        btnRefresh.setTag("refresh");
        btnRefresh.setBackgroundResource(R.drawable.nav_refresh_selector);
    }

    private void clickSecondButton() {
        if (btnRefresh.getTag().equals("refresh")) {
            // refreshData();
            listFragment.refresh();
        } else {
            // setButtonRefresh();
            // setButtonMore();
            // gvCombination.setOnItemClickListener(this);
            // removeSelectDatas();
            listFragment.removeSelectCombinations();
        }

    }

    // private void refreshData() {
    // // System.out.println("Second refresh button click");
    // loadCombinationData();
    // }

    // private void removeSelectDatas() {
    // List<CombinationBean> selectList = mCombinationAdapter.getDelPosition();
    // final List<CombinationBean> delList = new ArrayList<CombinationBean>();
    // StringBuilder sbIds = new StringBuilder();
    // for (CombinationBean delStock : selectList) {
    // // int i = index;
    // // CombinationBean delStock = mDataList.get(i);
    // delList.add(delStock);
    // sbIds.append(delStock.getId());
    // sbIds.append(",");
    // }
    // if (delList.size() > 0) {
    // // new MyCombinationEngineImpl().deleteCombination(delList.get(0).getId(), new BasicHttpListener() {
    // new MyCombinationEngineImpl().deleteCombination(sbIds.toString(), new BasicHttpListener() {
    //
    // @Override
    // public void onSuccess(String result) {
    // mCombinationAdapter.getDelPosition().clear();
    // mDataList.removeAll(delList);
    // upateDelViewStatus();
    // }
    //
    // @Override
    // public void onFailure(int errCode, String errMsg) {
    // super.onFailure(errCode, errMsg);
    // Toast.makeText(PortfolioApplication.getInstance(), "删除组合失败", Toast.LENGTH_SHORT).show();
    // }
    // });
    // }
    //
    // }

    public void upateDelViewStatus() {
        setButtonRefresh();
        setButtonAdd();

    }

}
