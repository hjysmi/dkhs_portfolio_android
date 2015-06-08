package com.dkhs.portfolio.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.dkhs.portfolio.R;
import com.dkhs.portfolio.bean.FundManagerInfoBean;
import com.dkhs.portfolio.engine.SymbolsEngine;
import com.dkhs.portfolio.net.DataParse;
import com.dkhs.portfolio.net.ParseHttpListener;
import com.dkhs.portfolio.ui.adapter.AchivementAdapter;

import java.util.ArrayList;
import java.util.List;

public class FundManagerActivity extends ModelAcitivity {


    String id = "302000757";
    private  TextView mName;
    private  TextView mDesc;

    private List<FundManagerInfoBean.AchivementsEntity> dataL = new ArrayList<>();

    private AchivementAdapter achivementsAdapter;


    public static Intent getIntent(Context ctx,String pk){
        Intent intent=new Intent(ctx,FundManagerActivity.class);
        intent.putExtra("pk",pk);
        return intent;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

//        id=getIntent().getStringExtra("pk");
        setContentView(R.layout.activity_fund_manager);

        ListView lv = (ListView) findViewById(R.id.listView);

        View view = LayoutInflater.from(FundManagerActivity.this).inflate(R.layout.layout_head_fund_manager, null);
        mName= (TextView) view.findViewById(R.id.name);
        mDesc= (TextView) view.findViewById(R.id.desc);
        lv.addHeaderView(view);
        achivementsAdapter = new AchivementAdapter(this, dataL);
        lv.setAdapter(achivementsAdapter);

        new SymbolsEngine().getFundManagerInfo(id, new ParseHttpListener<FundManagerInfoBean>() {
            @Override
            protected FundManagerInfoBean parseDateTask(String jsonData) {
                return DataParse.parseObjectJson(FundManagerInfoBean.class, jsonData);
            }

            @Override
            protected void afterParseData(FundManagerInfoBean object) {
                if(object !=null)
                updateUI(object);
            }
        });

    }

    /**
     *  iniView initData
     */
    public void initData() {
    }

    /**
     *  getData from net
     */
    public void getDataForNet() {
    }

    private void updateUI(FundManagerInfoBean object) {

        dataL.addAll(object.getAchivements());
        achivementsAdapter.notifyDataSetChanged();
        mName.setText(object.getName());
        mDesc.setText(object.getResume());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_fund_manager, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
