package com.dkhs.portfolio.ui;

import java.io.Serializable;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.dkhs.portfolio.R;
import com.dkhs.portfolio.bean.FiveRangeItem;
import com.dkhs.portfolio.bean.SelectStockBean;
import com.dkhs.portfolio.bean.StockQuotesBean;
import com.dkhs.portfolio.engine.QuotesEngineImpl;
import com.dkhs.portfolio.net.BasicHttpListener;
import com.dkhs.portfolio.net.DataParse;
import com.dkhs.portfolio.net.IHttpListener;
import com.dkhs.portfolio.net.ParseHttpListener;
import com.dkhs.portfolio.ui.fragment.FragmentSelectStockFund;
import com.dkhs.portfolio.ui.fragment.KChartsLandFragment;
import com.dkhs.portfolio.ui.fragment.StockQuotesChartLandFragment;
import com.dkhs.portfolio.ui.widget.HScrollTitleView;
import com.dkhs.portfolio.ui.widget.HScrollTitleView.ISelectPostionListener;
import com.dkhs.portfolio.ui.widget.InterceptScrollView;
import com.dkhs.portfolio.ui.widget.ScrollViewPager;
import com.dkhs.portfolio.utils.ColorTemplate;
import com.dkhs.portfolio.utils.StockUitls;
import com.dkhs.portfolio.utils.TimeUtils;
import com.dkhs.portfolio.utils.UIUtils;
import com.umeng.analytics.MobclickAgent;

public class KChartLandScapeActivity extends FragmentActivity implements OnClickListener, ITouchListener, Serializable {

	@Override
	protected void onCreate(Bundle arg0) {
		// TODO Auto-generated method stub
		requestWindowFeature(Window.FEATURE_NO_TITLE);//隐藏标题
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
		WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.activity_landscape_kchart);
		 context = this;
	        DisplayMetrics dm = new DisplayMetrics();
	        WindowManager m = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
	        m.getDefaultDisplay().getMetrics(dm);
	        mQuotesEngine = new QuotesEngineImpl();

	        // handle intent extras
	        processExtraData();
	        initView();
	        setupViewDatas();
		super.onCreate(arg0);
	}

	 /**
		 * 
		 */
	    //private LinearLayout landKlineLayout;
	    private static final long serialVersionUID = 15121212311111156L;

	    private SelectStockBean mStockBean;

	    public static final String EXTRA_STOCK = "extra_stock";

	    protected static final int MSG_WHAT_BEFORE_REQUEST = 99;

	    protected static final int MSG_WHAT_AFTER_REQUEST = 97;
	    private final int REQUESTCODE_SELECT_STOCK = 901;



	    private InterceptScrollView mScrollview; // 滚动条，用于滚动到头部

	    private QuotesEngineImpl mQuotesEngine;
	    private StockQuotesBean mStockQuotesBean;
	    private long mStockId;
	    private String mStockCode;
	    private Context context;

	    private HScrollTitleView hsTitle;
	    // privaet view
	    private ScrollViewPager pager;
	    private ArrayList<Fragment> fragmentList;
	    private StockQuotesChartLandFragment mStockQuotesChartFragment;
	    private View viewHeader;
	    private String symbolType;
	    public static final String TYPE = "type";
	    private int type;
	    public static Intent newIntent(Context context, SelectStockBean bean,int type) {
	        Intent intent = new Intent(context, KChartLandScapeActivity.class);
	        intent.putExtra(TYPE, type);
	        intent.putExtra(EXTRA_STOCK, bean);

	        return intent;
	    }

	    @Override
	    protected void onNewIntent(Intent intent) {

	        super.onNewIntent(intent);

	        setIntent(intent);// must store the new intent unless getIntent() will return the old one

	        processExtraData();

	    }

	    private void processExtraData() {

	        Bundle extras = getIntent().getExtras();
	        if (extras != null) {
	            handleExtras(extras);
	        }

	    }




	    /**
	     * @Title
	     * @Description TODO: (用一句话描述这个方法的功能)
	     * @return void
	     */
	    private void setupViewDatas() {
	        if (null != mStockBean) {

	            mStockId = mStockBean.id;
	            mStockCode = mStockBean.code;
	            symbolType = mStockBean.symbol_type;
	            updateStockInfo();
	        }
	        // setAddOptionalButton();
	        initTabPage();

	    }

	    private void handleExtras(Bundle extras) {
	        mStockBean = (SelectStockBean) extras.getSerializable(EXTRA_STOCK);
	        type = extras.getInt(TYPE);
	    }
	    private boolean isIndexType() {
	        return null != mStockBean && mStockBean.symbol_type != null
	                && mStockBean.symbol_type.equalsIgnoreCase(StockUitls.SYMBOLTYPE_INDEX);
	    }
	    private TextView landKlinTextTitle;
	    private TextView landKlinTextPrice;
	    private TextView landKlinTextValum;
	    private TextView landKlinTextData;
	    private void initView() {
	        //landKlineLayout = (LinearLayout) findViewById(R.id.land_kline_layout);
	        hsTitle = (HScrollTitleView) findViewById(R.id.hs_title);
	        String[] titleArray = getResources().getStringArray(R.array.quotes_title);
	        hsTitle.setTitleList(titleArray, getResources().getDimensionPixelSize(R.dimen.title_2text_length));
	        hsTitle.setSelectPositionListener(titleSelectPostion);
	        findViewById(R.id.lank_klind_exit).setOnClickListener(this);
	        landKlinTextTitle = (TextView) findViewById(R.id.land_klin_text_title);
	        landKlinTextPrice = (TextView) findViewById(R.id.land_klin_text_price);
	        landKlinTextValum = (TextView) findViewById(R.id.land_klin_text_valum);
	        landKlinTextData = (TextView) findViewById(R.id.land_klin_text_data);
	        hsTitle.setSelectIndex(type);
	        // stockLayout.setOnTouchListener(new OnLayoutlistener());
	       // initTabPage();
	        // setupViewData();
	        
	        // scrollview + listview 会滚动到底部，需要滚动到头部
	        // setAddOptionalButton();
	    }




	    private void setupViewData() {
	        if (null != mQuotesEngine && mStockBean != null) {
	            mQuotesEngine.quotes(mStockBean.code, listener);
	        }
	    }

	    Handler requestUiHandler = new Handler() {
	        public void handleMessage(android.os.Message msg) {
	            switch (msg.what) {
	                case MSG_WHAT_BEFORE_REQUEST: {
	                }

	                    break;
	                case MSG_WHAT_AFTER_REQUEST: {
	                }

	                    break;

	                default:
	                    break;
	            }
	        };
	    };


	    ISelectPostionListener titleSelectPostion = new ISelectPostionListener() {

	        @Override
	        public void onSelectPosition(int position) {
	            if (null != pager) {
	                /*if(position == 0){
                        landKlineLayout.setVisibility(View.GONE);
                    }else{
                        landKlineLayout.setVisibility(View.VISIBLE);
                    }*/
	                pager.setCurrentItem(position);
	                
	                
	            }
	        }
	    };

	    ParseHttpListener listener = new ParseHttpListener<StockQuotesBean>() {

	        @Override
	        protected StockQuotesBean parseDateTask(String jsonData) {
	            StockQuotesBean stockQuotesBean = null;
	            try {
	                JSONArray jsonArray = new JSONArray(jsonData);
	                JSONObject jsonOb = jsonArray.getJSONObject(0);

	                stockQuotesBean = DataParse.parseObjectJson(StockQuotesBean.class, jsonOb);
	                if (null != stockQuotesBean &&UIUtils.roundAble(stockQuotesBean)) {
	                    quoteHandler.removeCallbacks(runnable);
	                }
	                List<FiveRangeItem> buyList = new ArrayList<FiveRangeItem>();
	                List<FiveRangeItem> sellList = new ArrayList<FiveRangeItem>();
	                int i = 0;
	                for (; i < 5; i++) {
	                    // String buyPrice : stockQuotesBean.getBuyPrice().getBuyPrice()
	                    FiveRangeItem buyItem = new FiveRangeItem();
	                    if (i < stockQuotesBean.getBuyPrice().getBuyVol().size()) {
	                        String buyPrice = stockQuotesBean.getBuyPrice().getBuyPrice().get(i);
	                        if (isFloatText(buyPrice)) {

	                            buyItem.price = Float.parseFloat(buyPrice);
	                        } else {
	                            buyItem.price = 0;
	                        }
	                        String volText = stockQuotesBean.getBuyPrice().getBuyVol().get(i);
	                        if (isFloatText(volText)) {
	                            buyItem.vol = Integer.parseInt(volText);
	                        } else {

	                            buyItem.vol = 0;
	                        }
	                    } else {
	                        buyItem.vol = 0;

	                    }
	                    buyItem.tag = "" + (i + 1);
	                    buyList.add(buyItem);
	                }

	                for (int j = 4; j >= 0; j--) {
	                    FiveRangeItem sellItem = new FiveRangeItem();
	                    if (j < stockQuotesBean.getSellPrice().getSellVol().size()) {
	                        String sellPrice = stockQuotesBean.getSellPrice().getSellPrice().get(j);
	                        if (isFloatText(sellPrice)) {

	                            sellItem.price = Float.parseFloat(sellPrice);
	                        } else {
	                            sellItem.price = 0;
	                        }

	                        // sellItem.price = Float.parseFloat(stockQuotesBean.getSellPrice().getSellPrice().get(j));
	                        String sellVol = stockQuotesBean.getSellPrice().getSellVol().get(j);
	                        if (isFloatText(sellVol)) {
	                            sellItem.vol = Integer.parseInt(sellVol);
	                        } else {

	                            sellItem.vol = 0;
	                        }
	                    } else {
	                        sellItem.vol = 0;
	                    }
	                    sellItem.tag = "" + (j + 1);
	                    sellList.add(sellItem);
	                }

	                stockQuotesBean.setBuyList(buyList);
	                stockQuotesBean.setSellList(sellList);
	            } catch (Exception e) {

	                e.printStackTrace();
	            }
	            return stockQuotesBean;
	        }

	        @Override
	        protected void afterParseData(StockQuotesBean object) {
	            // requestUiHandler.sendEmptyMessage(MSG_WHAT_AFTER_REQUEST);
	            if (null != object) {
	                mStockQuotesBean = object;
	                mStockQuotesChartFragment.setStockQuotesBean(mStockQuotesBean);
	                landKlinTextTitle.setText(object.getName());
	                landKlinTextPrice.setText(object.getCurrent()+"");
	                landKlinTextPrice.setTextColor(ColorTemplate.getUpOrDrownCSL(object.getPercentage()));
	                double volume = object.getVolume()/100;
	                String vo = "";
	                if (volume < 10000) {
	                    vo = volume + "手";
	                } else if(volume > 10000 && volume < 100000000){
	                    volume = volume/10000;
	                    vo = new DecimalFormat("0.00").format(volume) + "万手";
	                }else{
	                    volume = volume/100000000;
	                    vo = new DecimalFormat("0.00").format(volume) + "亿手";
	                }
	                landKlinTextValum.setText(vo);
	                landKlinTextData.setText(TimeUtils.getTimeString(object.getMoment()));
	            }

	        }
	    };

	    private boolean isFloatText(String str) {
	        try {
	            Float.parseFloat(str);
	            return true;
	        } catch (NumberFormatException e) {
	            // TODO Auto-generated catch block
	            return false;
	        }
	    }

	    private void initTabPage() {

	        fragmentList = new ArrayList<Fragment>();// ViewPager中显示的数据

	        mStockQuotesChartFragment = StockQuotesChartLandFragment.newInstance(StockQuotesChartLandFragment.TREND_TYPE_TODAY,
	                mStockCode);
	        mStockQuotesChartFragment.setITouchListener(this);
	        fragmentList.add(mStockQuotesChartFragment);
	        KChartsLandFragment fragment = KChartsLandFragment.getKChartFragment(KChartsLandFragment.TYPE_CHART_DAY, mStockCode,
	                symbolType);
	        fragment.setITouchListener(this);
	        fragmentList.add(fragment);
	        KChartsLandFragment fragment2 = KChartsLandFragment.getKChartFragment(KChartsLandFragment.TYPE_CHART_WEEK, mStockCode,
	                symbolType);
	        fragment2.setITouchListener(this);
	        fragmentList.add(fragment2);
	        KChartsLandFragment fragment3 = KChartsLandFragment.getKChartFragment(KChartsLandFragment.TYPE_CHART_MONTH, mStockCode,
	                symbolType);
	        fragment3.setITouchListener(this);
	        fragmentList.add(fragment3);
	        // fragmentList.add(new TestFragment());
	        pager = (ScrollViewPager) this.findViewById(R.id.pager);
	        pager.setCanScroll(false);
	        pager.setOffscreenPageLimit(4);
	        pager.setAdapter(new MyPagerFragmentAdapter(getSupportFragmentManager(), fragmentList));
	        // TabPageIndicator indicator = (TabPageIndicator) this.findViewById(R.id.indicator);
	        // indicator.setViewPager(pager);

	    }





	    private class MyPagerFragmentAdapter extends FragmentPagerAdapter {

	        private List<Fragment> fragmentList;

	        // private String[] titleList;

	        public MyPagerFragmentAdapter(FragmentManager fm, ArrayList<Fragment> fragmentList2) {
	            super(fm);
	            this.fragmentList = fragmentList2;
	            // this.titleList = titleList;
	        }

	        @Override
	        public Fragment getItem(int arg0) {

	            return (fragmentList == null || fragmentList.size() == 0) ? null : fragmentList.get(arg0);
	        }

	        @Override
	        public CharSequence getPageTitle(int position) {
	            // return (titleList.length > position) ? titleList[position] : "";
	            return "";
	        }

	        @Override
	        public int getCount() {
	            return fragmentList == null ? 0 : fragmentList.size();
	        }

	    }



	    @Override
	    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
	        if (resultCode == RESULT_OK) {
	            Bundle b = data.getExtras(); // data为B中回传的Intent
	            switch (requestCode) {
	                case REQUESTCODE_SELECT_STOCK:

	                    SelectStockBean selectBean = (SelectStockBean) data
	                            .getSerializableExtra(FragmentSelectStockFund.ARGUMENT);
	                    if (null != selectBean) {
	                        mStockBean = selectBean;
	                        updateStockInfo();
	                    }
	                    break;
	            }
	        }
	    }

	    private void updateStockInfo() {
	        setTitle(mStockBean.name + "(" + mStockBean.code + ")");
	        // setTitleTipString(mStockBean.code);
	    }

	    // private boolean hasFollow = true;
	    IHttpListener baseListener = new BasicHttpListener() {

	        @Override
	        public void onSuccess(String result) {
	            mStockQuotesBean.setFollowed(!mStockQuotesBean.isFollowed());
	        }
	    };

	    Handler quoteHandler = new Handler();

	    // Handler quoteHandler = new Handler();



	    public void onStop() {
	        super.onStop();
	        quoteHandler.removeCallbacks(runnable);// 关闭定时器处理

	    }

	    Runnable runnable = new Runnable() {
	        @Override
	        public void run() {
	            // dataHandler.sendEmptyMessage(1722);

	            setupViewData();
	            quoteHandler.postDelayed(this, 5 * 1000);// 隔60s再执行一次
	        }
	    };





	    /**
	     * @Title
	     * @Description TODO: (用一句话描述这个方法的功能)
	     * @return
	     */
	    @Override
	    protected void onDestroy() {

	        super.onDestroy();
	        listener.stopRequest(true);
	    }

	    /**
	     * @Title
	     * @Description TODO: (用一句话描述这个方法的功能)
	     * @return
	     */
	    @Override
	    public void chartTounching() {
	        if (mScrollview != null) {
	            mScrollview.setIsfocus(true);
	        }

	    }

	    /**
	     * @Title
	     * @Description TODO: (用一句话描述这个方法的功能)
	     * @return
	     */
	    @Override
	    public void loseTouching() {
	        if (mScrollview != null) {
	            mScrollview.setIsfocus(false);
	        }

	    }

	    class OnLayoutlistener implements OnTouchListener {

	        @Override
	        public boolean onTouch(View v, MotionEvent event) {
	            // TODO Auto-generated method stub
	            switch (event.getAction()) {
	                case MotionEvent.ACTION_DOWN:
	                    chartTounching();
	                    break;
	                case MotionEvent.ACTION_UP:
	                    loseTouching();
	                    break;
	                default:
	                    break;
	            }
	            return true;
	        }

	    }

	    class OnView implements OnTouchListener {

	        @Override
	        public boolean onTouch(View v, MotionEvent event) {
	            // TODO Auto-generated method stub
	            loseTouching();
	            return true;
	        }

	    }

	    public StockQuotesBean getmStockQuotesBean() {
	        return mStockQuotesBean;
	    }

	    public void setmStockQuotesBean(StockQuotesBean mStockQuotesBean) {
	        this.mStockQuotesBean = mStockQuotesBean;
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
	    	if(getRequestedOrientation()!=ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE){
	    		  setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
	    		 }
	        super.onResume();
	        quoteHandler.postDelayed(runnable, 6);
	        // SDK已经禁用了基于Activity 的页面统计，所以需要再次重新统计页面
	        MobclickAgent.onResume(this);
	    }

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			switch (v.getId()) {
                case R.id.lank_klind_exit:
                    finish();
                    break;

                default:
                    break;
            }
		}
}
