package com.dkhs.portfolio.ui;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import android.content.Context;
import android.content.Intent;
import android.graphics.Matrix;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.dkhs.portfolio.R;
import com.dkhs.portfolio.ui.OptionalStockListActivity.RequestMarketTask;
import com.dkhs.portfolio.ui.adapter.FragmentSelectAdapter;
import com.dkhs.portfolio.ui.fragment.FragmentMarkerCenter;
import com.dkhs.portfolio.ui.fragment.FragmentSelectStockFund;
import com.dkhs.portfolio.ui.fragment.FragmentSelectStockFund.ViewType;
/**
 * 行情中心
 * @author weiting
 *
 */
public class MarketCenterActivity extends ModelAcitivity{
	private LinearLayout layoutMarkerParent;
	private Context context;
	@Override
	protected void onCreate(Bundle arg0) {
		// TODO Auto-generated method stub
		super.onCreate(arg0);
		setContentView(R.layout.activity_marketcenter);
		setTitle(R.string.marketcenter_title);
		layoutMarkerParent = (LinearLayout) findViewById(R.id.layout_marker_parenst);
		context = this;
		String[] name = new String[]{"国内指数","沪深行情"};
		List<Fragment> frag = new ArrayList<Fragment>();
		frag.add(FragmentMarkerCenter.initFrag(FragmentMarkerCenter.INSIDE_COUNT));
		frag.add(FragmentMarkerCenter.initFrag(FragmentMarkerCenter.SHEN_HU));
		new FragmentSelectAdapter(context, name, frag, layoutMarkerParent, getSupportFragmentManager());
	}
}
