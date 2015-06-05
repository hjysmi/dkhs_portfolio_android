/**
 * @Title AddConbinationStockActivity.java
 * @Package com.dkhs.portfolio.ui
 * @Description TODO(用一句话描述该文件做什么)
 * @author zjz
 * @date 2014-8-28 下午12:11:20
 * @version V1.0
 */
package com.dkhs.portfolio.ui;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;

import com.dkhs.portfolio.R;
import com.dkhs.portfolio.bean.SelectStockBean;
import com.dkhs.portfolio.bean.StockPriceBean;
import com.dkhs.portfolio.engine.OptionalStockEngineImpl;
import com.dkhs.portfolio.net.DataParse;
import com.dkhs.portfolio.net.ParseHttpListener;
import com.dkhs.portfolio.ui.fragment.FragmentSearchStockFund;
import com.dkhs.portfolio.ui.fragment.FragmentSelectStockFund;
import com.dkhs.portfolio.ui.fragment.FragmentSelectStockFund.StockViewType;
import com.umeng.analytics.MobclickAgent;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * @author zjz
 * @version 1.0
 * @ClassName AddConbinationStockActivity
 * @Description TODO(这里用一句话描述这个类的作用)
 * @date 2014-8-28 下午12:11:20
 */
public class SelectAddOptionalActivity extends BaseSelectActivity implements OnClickListener {

    public static List<SelectStockBean> mFollowList = new ArrayList<SelectStockBean>();

    @Override
    protected void onCreate(Bundle arg0) {
        super.onCreate(arg0);
        // findViewById(R.id.rl_search_stock).setVisibility(View.GONE);
        findViewById(R.id.rl_add_stocklist).setVisibility(View.GONE);
        getRightButton().setVisibility(View.GONE);
        OptionalStockEngineImpl.loadAllData(loadAllListener);
    }

    ParseHttpListener loadAllListener = new ParseHttpListener<List<SelectStockBean>>() {

        @Override
        protected List<SelectStockBean> parseDateTask(String jsonData) {
            List<SelectStockBean> selectList = new ArrayList<SelectStockBean>();
            try {
                JSONObject dataObject = new JSONObject(jsonData);

                JSONArray resultsJsonArray = dataObject.optJSONArray("results");
                if (null != resultsJsonArray && resultsJsonArray.length() > 0) {
                    int length = resultsJsonArray.length();

                    for (int i = 0; i < length; i++) {
                        JSONObject stockObject = resultsJsonArray.optJSONObject(i);
                        StockPriceBean stockBean = DataParse.parseObjectJson(StockPriceBean.class, stockObject);
                        SelectStockBean selectBean = new SelectStockBean();
                        selectBean.id = stockBean.getId();
                        selectBean.name = stockBean.getAbbrname();
                        selectBean.currentValue = stockBean.getCurrent();
                        selectBean.code = stockBean.getSymbol();
                        selectBean.percentage = stockBean.getPercentage();
                        selectBean.percentage = stockBean.getPercentage();
                        selectBean.change = stockBean.getChange();
                        selectBean.isStop = stockBean.isStop();
                        selectBean.symbol_type = stockBean.getSymbol_type();

                        // if (StockUitls.SYMBOLTYPE_STOCK.equalsIgnoreCase(stockBean.getSymbol_type())) {
                        selectList.add(selectBean);
                        // results.add(stockBean);
                        // }

                    }

                }

            } catch (JSONException e) {
                e.printStackTrace();
            }

            return selectList;
        }

        @Override
        protected void afterParseData(List<SelectStockBean> object) {
            if (null != object) {
                mFollowList.clear();
                mFollowList.addAll(object);
            }

        }

    };

    @Override
    protected void setTabViewPage(List<FragmentSelectStockFund> fragmenList) {

        // String[] tArray = getResources().getStringArray(R.array.select_optional_stock);
        // int titleLenght = tArray.length;
        // for (int i = 0; i < titleLenght; i++) {
        // titleList.add(tArray[i]);
        //
        // }
        FragmentSelectStockFund mIncreaseFragment = FragmentSelectStockFund
                .getStockFragment(StockViewType.STOCK_INCREASE_CLICKABLE);
        FragmentSelectStockFund mDownFragment = FragmentSelectStockFund
                .getStockFragment(StockViewType.STOCK_DRAWDOWN_CLICKABLE);
        FragmentSelectStockFund mHandoverFragment = FragmentSelectStockFund
                .getStockFragment(StockViewType.STOCK_HANDOVER_CLICKABLE);
        mIncreaseFragment.setDefLoad(true);
        mDownFragment.setDefLoad(true);
        mHandoverFragment.setDefLoad(true);

        fragmenList.add(mIncreaseFragment);
        fragmenList.add(mDownFragment);
        fragmenList.add(mHandoverFragment);

    }

    @Override
    protected FragmentSearchStockFund getSearchFragment() {
        return FragmentSearchStockFund.getItemClickBackFragment();
    }

    @Override
    protected ListViewType getLoadByType() {
        return ListViewType.ADD_OPTIONAL;
    }

    /**
     * @return
     * @Title
     * @Description TODO: (用一句话描述这个方法的功能)
     */
    @Override
    protected int getTitleRes() {
        // TODO Auto-generated method stub
        return R.array.select_optional_stock;
    }

    @Override
    public void onPause() {
        // TODO Auto-generated method stub
        super.onPause();
        // SDK已经禁用了基于Activity 的页面统计，所以需要再次重新统计页面
        MobclickAgent.onPause(this);
    }

    @Override
    public void onResume() {
        // TODO Auto-generated method stub
        super.onResume();
        // SDK已经禁用了基于Activity 的页面统计，所以需要再次重新统计页面
        MobclickAgent.onResume(this);
    }
}
