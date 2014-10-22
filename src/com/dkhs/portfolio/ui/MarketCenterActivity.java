package com.dkhs.portfolio.ui;

import java.util.ArrayList;
import java.util.List;

import com.dkhs.portfolio.R;

import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.PagerTabStrip;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class MarketCenterActivity extends ModelAcitivity{
	private ViewPager marketViewpager;
	//private PagerTabStrip pagerTabStrip;
	private LayoutInflater inflater;
	private List<View> viewList;
	private List<String> titleList;
	//大盘指数,行业板块,地区板块,个股行情,概念板块
	private View grail,industry,area,individual,concept;
	@Override
	protected void onCreate(Bundle arg0) {
		// TODO Auto-generated method stub
		super.onCreate(arg0);
		setContentView(R.layout.activity_marketcenter);
		setTitle(R.string.marketcenter_title);
		//initView();
	}
	/*private void initView(){
		marketViewpager = (ViewPager) findViewById(R.id.market_viewpager);
		inflater = LayoutInflater.from(this);
		grail = inflater.inflate(R.layout.market_layout_market, null);
		area = inflater.inflate(R.layout.market_layout_market, null);
		individual = inflater.inflate(R.layout.market_layout_market, null);
		concept = inflater.inflate(R.layout.market_layout_market, null);
		industry = inflater.inflate(R.layout.market_layout_market, null);
		viewList = new ArrayList<View>();
		viewList.add(grail);
		viewList.add(individual);
		viewList.add(industry);
		viewList.add(area);
		viewList.add(concept);
		titleList = new ArrayList<String>();
		titleList.add("大盘指数");
		titleList.add("个股行情");
		titleList.add("行业板块");
		titleList.add("地区板块");
		titleList.add("概念板块");
		marketViewpager = (ViewPager) findViewById(R.id.market_viewpager); 
        pagerTabStrip=(PagerTabStrip) findViewById(R.id.market_pagertab); 
        pagerTabStrip.setTabIndicatorColor(getResources().getColor(R.color.red));  
        pagerTabStrip.setDrawFullUnderline(false); 
        pagerTabStrip.setBackgroundColor(getResources().getColor(R.color.activity_bg_color)); 
        //pagerTabStrip.setTextSpacing(50);
        marketViewpager.setAdapter(pagerAdapter);  
        marketViewpager.setCurrentItem(0);
	}
	PagerAdapter pagerAdapter = new PagerAdapter() {  
		  
        @Override  
        public boolean isViewFromObject(View arg0, Object arg1) {  

            return arg0 == arg1;  
        }  

        @Override  
        public int getCount() {  

            return viewList.size();  
        }  

        @Override  
        public void destroyItem(ViewGroup container, int position,  
                Object object) {  
            container.removeView(viewList.get(position));  

        }  

        @Override  
        public int getItemPosition(Object object) {  

            return super.getItemPosition(object);  
        }  

        @Override  
        public CharSequence getPageTitle(int position) {  

            return titleList.get(position);//直接用适配器来完成标题的显示，所以从上面可以看到，我们没有使用PagerTitleStrip。当然你可以使用。  

        }  

        @Override  
        public Object instantiateItem(ViewGroup container, int position) {  
            container.addView(viewList.get(position));  
            
            return viewList.get(position);  
        }  

    }; */
}
