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
import com.dkhs.portfolio.engine.MyCombinationEngineImpl;
import com.dkhs.portfolio.net.BasicHttpListener;
import com.dkhs.portfolio.net.DataParse;
import com.dkhs.portfolio.net.ParseHttpListener;
import com.dkhs.portfolio.ui.adapter.CombinationAdapter;
import com.dkhs.portfolio.ui.adapter.CombinationAdapter.IDelButtonListener;
import com.google.gson.reflect.TypeToken;
import com.lidroid.xutils.util.LogUtils;

/**
 * @ClassName MyCombinationActivity
 * @Description 我的组合
 * @author zjz
 * @date 2014-8-26 下午3:10:51
 * @version 1.0
 */
public class MyCombinationActivity extends ModelAcitivity implements OnItemClickListener, OnTouchListener,
        IDelButtonListener, OnClickListener {
    private GridView gvCombination;
    private CombinationAdapter mCombinationAdapter;
    private Button btnMore;
    private Button btnRefresh;
    private PopupWindow mPopMoreWindow;
    private List<CombinationBean> mDataList = new ArrayList<CombinationBean>();

    @Override
    protected void onCreate(Bundle arg0) {
        super.onCreate(arg0);
        setContentView(R.layout.activity_mycombination);
        setTitle(R.string.my_combination);

        initGridView();
        this.getWindow().getDecorView().setOnTouchListener(this);

        initTitleView();

        // loadCombinationData();

    }

    /**
     * @Title
     * @Description TODO: (用一句话描述这个方法的功能)
     * @return
     */
    @Override
    protected void onStart() {
        super.onStart();
        loadCombinationData();
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

    /**
     * @Title
     * @Description TODO: (用一句话描述这个方法的功能)
     * @return void
     */
    private void initGridView() {
        gvCombination = (GridView) findViewById(R.id.gv_mycombination);

        mCombinationAdapter = new CombinationAdapter(this, mDataList);
        gvCombination.setAdapter(mCombinationAdapter);
        mCombinationAdapter.setDeleteButtonClickListener(this);
        gvCombination.setOnItemClickListener(this);
        gvCombination.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView absListView, int scrollState) {
                // if (scrollState == AbsListView.OnScrollListener.SCROLL_STATE_FLING) {
                // // mImageFetcher.setPauseWork(true);
                // } else {
                // mImageFetcher.setPauseWork(false);
                // }
            }

            @Override
            public void
                    onScroll(AbsListView absListView, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
            }
        });
        gvCombination.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                final int columnWidth = (gvCombination.getWidth() - (getResources()
                        .getDimensionPixelSize(R.dimen.combin_horSpacing))) / 2;

                mCombinationAdapter.setItemHeight((int) (columnWidth));
            }
        });
        gvCombination.setOnItemLongClickListener(new OnItemLongClickListener() {

            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                mCombinationAdapter.setDelStatus(true);
                return true;
            }
        });
        gvCombination.setOnTouchListener(this);

    }

    private void loadCombinationData() {
        // CombinationBean conBean1 = new CombinationBean("我的组合1", 1.152f, 11.22f);
        // CombinationBean conBean2 = new CombinationBean("我的组合2", 1.153f, 15.22f);
        // CombinationBean conBean3 = new CombinationBean("我的组合3", -1.152f, -11.22f);
        // CombinationBean conBean4 = new CombinationBean("我的组合4", 1.152f, 13.22f);
        // CombinationBean conBean5 = new CombinationBean("我的组合5", -1.154f, -10.22f);
        // mDataList.add(conBean1);
        // mDataList.add(conBean2);
        // mDataList.add(conBean3);
        // mDataList.add(conBean4);
        // mDataList.add(conBean5);
        new MyCombinationEngineImpl().getCombinationList(new ParseHttpListener<List<CombinationBean>>() {

            @Override
            protected List<CombinationBean> parseDateTask(String jsonData) {
                Type listType = new TypeToken<List<CombinationBean>>() {
                }.getType();
                List<CombinationBean> combinationList = DataParse.parseJsonList(jsonData, listType);

                return combinationList;
            }

            @Override
            protected void afterParseData(List<CombinationBean> dataList) {
                LogUtils.d("List<CombinationBean> size:" + dataList.size());
                mDataList.clear();
                mDataList.addAll(dataList);
                mCombinationAdapter.notifyDataSetChanged();
            }

        });
        mCombinationAdapter.notifyDataSetChanged();
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

        // Intent intent = new Intent(this, CombinationDetailActivity.class);
        // intent.putExtra(PositionAdjustActivity.KEY_VIEW_TYPE, PositionAdjustActivity.VALUE_ADJUST_CONBINA);
        startActivity(CombinationDetailActivity.newIntent(this, mDataList.get(position)));

        // new MyCombinationEngineImpl().queryCombinationDetail(mDataList.get(position).getId(),
        // new ParseHttpListener<String>() {
        //
        // @Override
        // protected String parseDateTask(String jsonData) {
        // // TODO Auto-generated method stub
        // return null;
        // }
        //
        // @Override
        // protected void afterParseData(String object) {
        // // TODO Auto-generated method stub
        //
        // }
        // });

    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        gvCombination.clearFocus();
        InputMethodManager imm = (InputMethodManager) v.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm != null) {
            imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
        }
        return super.onTouchEvent(event);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == RIGHTBUTTON_ID) {

            clickRightButton();
        } else if (id == SECONDRIGHTBUTTON_ID) {

            clickSecondButton();
        } else if (id == R.id.tv_add_combina) {
            mCombinationAdapter.addItem();
            mPopMoreWindow.dismiss();

        } else if (id == R.id.tv_delete_combina) {
            btnMore.setText("取消");
            btnMore.setTag("del");
            btnMore.setBackgroundDrawable(null);

            btnRefresh.setTag("del");
            btnRefresh.setBackgroundResource(R.drawable.btn_delete_selector);
            mCombinationAdapter.setDelStatus(true);
            mPopMoreWindow.dismiss();

            gvCombination.setOnItemClickListener(null);

        }

    }

    private void clickRightButton() {
        mCombinationAdapter.addItem();
        // if (btnMore.getTag() != null && btnMore.getTag().equals("del")) {
        // setButtonMore();
        // mCombinationAdapter.getDelPosition().clear();
        // mCombinationAdapter.setDelStatus(false);
        // mCombinationAdapter.notifyDataSetChanged();
        // setButtonRefresh();
        // gvCombination.setOnItemClickListener(this);
        // } else {
        // showPopWindow();
        // }

    }

    // private void setButtonMore() {
    // btnMore.setTag("more");
    // btnMore.setText("");
    // btnMore.setBackgroundResource(R.drawable.nav_more_selector);
    // }

    private void setButtonRefresh() {
        btnRefresh.setTag("refresh");
        btnRefresh.setBackgroundResource(R.drawable.nav_refresh_selector);
    }

    private void clickSecondButton() {
        if (btnRefresh.getTag().equals("refresh")) {
            refreshData();
        } else {
            // setButtonRefresh();
            // setButtonMore();
            gvCombination.setOnItemClickListener(this);
            removeSelectDatas();
        }

    }

    private void refreshData() {
        // System.out.println("Second refresh button click");
        loadCombinationData();
    }

    private void removeSelectDatas() {
        List<CombinationBean> selectList = mCombinationAdapter.getDelPosition();
        final List<CombinationBean> delList = new ArrayList<CombinationBean>();
        StringBuilder sbIds = new StringBuilder();
        for (CombinationBean delStock : selectList) {
            // int i = index;
            // CombinationBean delStock = mDataList.get(i);
            delList.add(delStock);
            sbIds.append(delStock.getId());
            sbIds.append(",");
        }
        if (delList.size() > 0) {
            // new MyCombinationEngineImpl().deleteCombination(delList.get(0).getId(), new BasicHttpListener() {
            new MyCombinationEngineImpl().deleteCombination(sbIds.toString(), new BasicHttpListener() {

                @Override
                public void onSuccess(String result) {
                    mCombinationAdapter.getDelPosition().clear();
                    mDataList.removeAll(delList);
                    upateDelViewStatus();
                }

                @Override
                public void onFailure(int errCode, String errMsg) {
                    super.onFailure(errCode, errMsg);
                    Toast.makeText(PortfolioApplication.getInstance(), "删除组合失败", Toast.LENGTH_SHORT).show();
                }
            });
        }

    }

    private void showDelDialog(final CombinationBean mCombination) {

        AlertDialog.Builder builder = new AlertDialog.Builder(new ContextThemeWrapper(this,
                android.R.style.Theme_Holo_Light_Dialog_NoActionBar));
        builder.setMessage("您将要删除选中的组合!");
        builder.setTitle("提示");

        builder.setPositiveButton("确认", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {

                new MyCombinationEngineImpl().deleteCombination(mCombination.getId() + "", new BasicHttpListener() {

                    @Override
                    public void onSuccess(String result) {
                        // mCombinationAdapter.getDelPosition().clear();
                        mDataList.remove(mCombination);
                        upateDelViewStatus();
                    }

                    @Override
                    public void onFailure(int errCode, String errMsg) {
                        super.onFailure(errCode, errMsg);
                        Toast.makeText(PortfolioApplication.getInstance(), "删除组合失败", Toast.LENGTH_SHORT).show();
                    }
                });
                dialog.dismiss();
            }

        });

        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.create().show();
    }

    private void upateDelViewStatus() {
        setButtonRefresh();
        // setButtonMore();

        mCombinationAdapter.setDelStatus(false);
        mCombinationAdapter.notifyDataSetChanged();
    }

    private void showPopWindow() {
        View view;
        view = this.getLayoutInflater().inflate(R.layout.layout_more_myconbination, null);
        mPopMoreWindow = new PopupWindow(view, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        mPopMoreWindow.setOutsideTouchable(true); // 不能在没有焦点的时候使用
        mPopMoreWindow.setBackgroundDrawable(new BitmapDrawable());
        mPopMoreWindow.setFocusable(true);

        TextView tvAdd = (TextView) view.findViewById(R.id.tv_add_combina);
        TextView tvDel = (TextView) view.findViewById(R.id.tv_delete_combina);
        tvAdd.setOnClickListener(this);
        tvDel.setOnClickListener(this);

        int width = getApplication().getResources().getDisplayMetrics().widthPixels;
        view.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
        int viewWidth = view.getMeasuredWidth();

        mPopMoreWindow.showAsDropDown(findViewById(R.id.includeHead), width - viewWidth, 0);

    }

    @Override
    public void clickDeleteButton(int position) {
        CombinationBean combiantinBean = mDataList.get(position);
        // Toast.makeText(this, "Is del :" + combiantinBean.getName(), Toast.LENGTH_SHORT).show();
        showDelDialog(combiantinBean);

    }

}
