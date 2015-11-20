package com.dkhs.portfolio.ui;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ListView;

import com.dkhs.portfolio.R;
import com.dkhs.portfolio.base.widget.TextView;
import com.dkhs.portfolio.bean.QuotesBean;
import com.dkhs.portfolio.bean.SearchGeneralBean;
import com.dkhs.portfolio.engine.SelectGeneralEngine;
import com.dkhs.portfolio.net.DataParse;
import com.dkhs.portfolio.net.ParseHttpListener;
import com.dkhs.portfolio.ui.adapter.SelectGeneralAdapter;
import com.dkhs.portfolio.ui.widget.ClearableEditText;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * @author xuetong
 * 综合搜索（股票/基金/用户/悬赏/话题）
 */
public class SelectGeneralActivity extends ModelAcitivity implements View.OnClickListener{
    private ListView lv_search;
    private ClearableEditText et_search_key;
    private TextView tv_cancel;
    private SelectGeneralAdapter adapter;
    private SelectGeneralEngine engine;
    private List<Object> list;
    private List<QuotesBean> list_stock;
    private List<QuotesBean> list_fund;

    @Override
    protected void onCreate(Bundle arg0) {
        super.onCreate(arg0);
        hideHead();
        setContentView(R.layout.activity_selectgeneral);
        initViews();
        initValues();
        initEvents();
    }

    private void initViews() {
        lv_search = (ListView) findViewById(R.id.lv_search);
        et_search_key = (ClearableEditText) findViewById(R.id.et_search_key);
        tv_cancel = (TextView) findViewById(R.id.tv_cancel);
        View footer = LayoutInflater.from(this).inflate(R.layout.layout_search_footer,null);
        lv_search.addFooterView(footer);
    }

    private void initValues() {
        list = new ArrayList<>();
        adapter = new SelectGeneralAdapter(this);
        engine = new SelectGeneralEngine();
        adapter.bindDatas(list);
        lv_search.setAdapter(adapter);

    }

    private void initEvents() {
        tv_cancel.setOnClickListener(this);
        et_search_key.addTextChangedListener(mTextWatcher);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.tv_cancel:

                super.onBackPressed();
                break;
        }
    }
    ParseHttpListener parseHttpListener = new ParseHttpListener() {
        @Override
        protected SearchGeneralBean parseDateTask(String jsonData) {
            SearchGeneralBean searchGeneralBean=null;
            try {
                JSONObject object = new JSONObject(jsonData);
                 searchGeneralBean = DataParse.parseObjectJson(SearchGeneralBean.class, object);
               return searchGeneralBean;
            } catch (JSONException e) {
                e.printStackTrace();
                return null;
            }

        }

        @Override
        protected void afterParseData(Object object) {
            SearchGeneralBean searchGeneralBean = (SearchGeneralBean) object;
            list = new ArrayList<>();
            list_stock = new ArrayList<>();
            list_fund = new ArrayList<>();

           List<QuotesBean> quotesBeans = searchGeneralBean.getSymbols();
            for(QuotesBean qb : quotesBeans){
                switch (qb.getSymbol_type()){
                    case "1":
                        list_stock.add(qb);
                        break;
                    case "3":
                        list_fund.add(qb);
                        break;
                }
            }
            list.add("股票");
            list.addAll(list_stock);
            list.add("基金");
            list.addAll(list_fund);
            list.add("基金经理");
        //    list.addAll(searchGeneralBean.getFund_managers());
            list.add("用户");
        //    list.addAll(searchGeneralBean.getUsers());
            list.add("组合");
          //  list.addAll(searchGeneralBean.getPortfolios());
            adapter.bindDatas(list);
            adapter.notifyDataSetChanged();
        }
    };
    TextWatcher mTextWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {
            String str = s.toString().trim();
            if(!TextUtils.isEmpty(str)){
                engine.searchBygeneral(parseHttpListener,str);
            }
        }
    };
}
