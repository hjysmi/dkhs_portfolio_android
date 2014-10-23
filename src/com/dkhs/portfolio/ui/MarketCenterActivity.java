package com.dkhs.portfolio.ui;

import android.graphics.Matrix;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.dkhs.portfolio.R;
import com.dkhs.portfolio.ui.fragment.FragmentSelectStockFund;
import com.dkhs.portfolio.ui.fragment.FragmentSelectStockFund.ViewType;

public class MarketCenterActivity extends ModelAcitivity implements
		OnClickListener {
	private FragmentSelectStockFund loadDataListFragment;
	private ImageView marketIconCursor;
	private int bmpW;
	private int offset;
	private int currIndex = 1;
	private TextView textInland;
	private TextView textShangAndShen;
	private LinearLayout marketLayoutUpanddown;
	private boolean ace = false;
	private ImageView marketIconUpDown;
	@Override
	protected void onCreate(Bundle arg0) {
		// TODO Auto-generated method stub
		super.onCreate(arg0);
		setContentView(R.layout.activity_marketcenter);
		setTitle(R.string.marketcenter_title);
		replaceDataList();
		initView();
		InitImageView();
	}

	private void initView() {
		marketIconCursor = (ImageView) findViewById(R.id.market_icon_cursor);
		textInland = (TextView) findViewById(R.id.text1);
		textShangAndShen = (TextView) findViewById(R.id.text2);
		marketLayoutUpanddown = (LinearLayout) findViewById(R.id.market_layout_upanddown);
		marketIconUpDown = (ImageView) findViewById(R.id.market_icon_up_down);
		textInland.setOnClickListener(this);
		textShangAndShen.setOnClickListener(this);
		marketLayoutUpanddown.setOnClickListener(this);
	}

	private void replaceDataList() {
		// view_datalist
		if (null == loadDataListFragment) {
			loadDataListFragment = loadDataListFragment
					.getStockFragment(ViewType.STOC_INDEX_MARKET);
		}
		getSupportFragmentManager().beginTransaction()
				.replace(R.id.view_datalist, loadDataListFragment).commit();
	}

	/**
	 * 初始化动画
	 */
	private void InitImageView() {
		
		DisplayMetrics dm = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(dm);
		bmpW = (int) (80* dm.densityDpi/160);// 获取图片宽度
		int screenW = dm.widthPixels;// 获取分辨率宽度
		
		offset = (screenW / 2 - bmpW) / 2;// 计算偏移量
		Matrix matrix = new Matrix();
		matrix.postTranslate(offset, 0);
		marketIconCursor.setImageMatrix(matrix);// 设置动画初始位置\
		Animation animation = null;
		animation = new TranslateAnimation(offset,offset , 0, 0);
		animation.setFillAfter(true);// True:图片停在动画结束位置
		animation.setDuration(300);
		marketIconCursor.startAnimation(animation);
		textInland.setTextColor(getResources().getColor(R.color.red));
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		Animation animation = null;
		switch (v.getId()) {
		case R.id.text1:
			if (currIndex == 2) {
				animation = new TranslateAnimation(offset * 3 + bmpW,offset , 0, 0);
				currIndex = 1;
				textInland.setTextColor(getResources().getColor(R.color.red));
				textShangAndShen.setTextColor(getResources().getColor(R.color.black));
				animation.setFillAfter(true);// True:图片停在动画结束位置
				animation.setDuration(300);
				marketIconCursor.startAnimation(animation);
				loadDataListFragment = loadDataListFragment
						.getStockFragment(ViewType.STOC_INDEX_MARKET);
				marketIconUpDown.setImageResource(R.drawable.market_icon_down);
				getSupportFragmentManager().beginTransaction()
				.replace(R.id.view_datalist, loadDataListFragment).commit();
				 ace = false;
			}
			break;
		case R.id.text2:
			if (currIndex == 1) {
				animation = new TranslateAnimation(offset,offset* 3 + bmpW,0, 0);
				currIndex = 2;
				textInland.setTextColor(getResources().getColor(R.color.black));
				textShangAndShen.setTextColor(getResources().getColor(R.color.red));
				animation.setFillAfter(true);// True:图片停在动画结束位置
				animation.setDuration(300);
				marketIconCursor.startAnimation(animation);
				loadDataListFragment = loadDataListFragment
						.getStockFragment(ViewType.STOC_INDEX_POSITION);
				marketIconUpDown.setImageResource(R.drawable.market_icon_down);
				getSupportFragmentManager().beginTransaction()
				.replace(R.id.view_datalist, loadDataListFragment).commit();
				 ace = false;
			}
			break;
		case R.id.market_layout_upanddown:
			if(ace){
				if(currIndex == 1){
				loadDataListFragment = loadDataListFragment
						.getStockFragment(ViewType.STOC_INDEX_MARKET);
				marketIconUpDown.setImageResource(R.drawable.market_icon_down);
				}else{
					loadDataListFragment = loadDataListFragment
							.getStockFragment(ViewType.STOC_INDEX_POSITION);
					marketIconUpDown.setImageResource(R.drawable.market_icon_down);
				}
				ace = false;
			}else{
				if(currIndex == 1){
				loadDataListFragment = loadDataListFragment
						.getStockFragment(ViewType.STOC_INDEX_MARKET_ACE);
				marketIconUpDown.setImageResource(R.drawable.market_icon_up);
				}else{
					loadDataListFragment = loadDataListFragment
							.getStockFragment(ViewType.STOC_INDEX_POSITION_ACE);
					marketIconUpDown.setImageResource(R.drawable.market_icon_up);
				}
				ace = true;
			}
			getSupportFragmentManager().beginTransaction()
					.replace(R.id.view_datalist, loadDataListFragment).commit();
			break;
		}
		
	}

}
