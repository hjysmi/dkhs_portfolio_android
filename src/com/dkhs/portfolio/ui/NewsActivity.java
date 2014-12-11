package com.dkhs.portfolio.ui;

import java.io.Serializable;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.TextView;

import com.dkhs.portfolio.R;
import com.dkhs.portfolio.app.PortfolioApplication;
import com.dkhs.portfolio.bean.OptionNewsBean;
import com.dkhs.portfolio.engine.LoadNewsTextEngine;
import com.dkhs.portfolio.engine.LoadNewsTextEngine.ILoadDataBackListener;
import com.dkhs.portfolio.engine.NewsTextEngineImple;
import com.dkhs.portfolio.utils.TimeUtils;
import com.umeng.analytics.MobclickAgent;

public class NewsActivity extends ModelAcitivity implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 24654131315456464L;
	private ImageView newsTitleIcon;
	private TextView newsTitleName;
	private TextView newsTitleDate;
	private TextView newsTitleNum;
	private TextView newsTextTitle;
	private TextView newsTextText;
	private static final String EXTRA = "newsId";
	private static final String EXTRA_NAME = "name";
	private static final String EXTRA_SYMBOL = "symbolname";
	private static final String EXTRA_SYMBOL_ID = "symbolid";
	private String textId;
	 private LoadNewsTextEngine mLoadDataEngine;
	 private OptionNewsBean mOptionNewsBean;
	 private String optionName;
	 private String symbolName;
	 private String symbolId;
	 private TextView newsSymbol;
	@Override
	protected void onCreate(Bundle arg0) {
		// TODO Auto-generated method stub
		super.onCreate(arg0);
		setContentView(R.layout.activity_news);
		initView();
		
		Bundle extras = getIntent().getExtras();
	    if (extras != null) {
	        getId(extras);
	        setTitle(optionName);
	    }
	    mLoadDataEngine = new NewsTextEngineImple(mSelectStockBackListener, textId);
	    mLoadDataEngine.setLoadingDialog(this);
		mLoadDataEngine.loadData();
	}
	public static Intent newIntent(Context context, String id,String name,String symbolName,String symbolId) {
        Intent intent = new Intent(context, NewsActivity.class);
        Bundle b = new Bundle();
        b.putString(EXTRA, id);
        b.putString(EXTRA_NAME, name);
        b.putString(EXTRA_SYMBOL, symbolName);
        b.putString(EXTRA_SYMBOL_ID, symbolId);
         intent.putExtras(b);
        return intent;
    }
	
    public void getId(Bundle b){
    	textId = b.getString(EXTRA);
    	optionName = b.getString(EXTRA_NAME);
    	symbolName = b.getString(EXTRA_SYMBOL);
    	symbolId = b.getString(EXTRA_SYMBOL_ID);
    }
	public void initView(){
		newsTitleIcon = (ImageView) findViewById(R.id.news_title_icon);
		newsTitleName = (TextView) findViewById(R.id.news_title_name);
		newsTitleDate = (TextView) findViewById(R.id.news_title_date);
		newsTitleNum = (TextView) findViewById(R.id.news_title_num);
		newsTextTitle = (TextView) findViewById(R.id.news_text_title);
		newsTextText = (TextView) findViewById(R.id.news_text_text);
		newsSymbol = (TextView) findViewById(R.id.news_symbol);
		newsTextText.setTextIsSelectable(true);
	}
	 ILoadDataBackListener mSelectStockBackListener = new ILoadDataBackListener() {

	        @Override
	        public void loadFinish(OptionNewsBean dataList) {
	            try {
					if (null != dataList) {
						mOptionNewsBean = dataList;
						setValue();
					}
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

	        }

	    };
	    public void setValue(){
	    	newsTitleName.setText(mOptionNewsBean.getTitle());
	    	String time = mOptionNewsBean.getPublish().replace("T", " ").substring(0, mOptionNewsBean.getCreatedTime().length()-6) + "00";
	    	time = TimeUtils.addHour(time);
	    	newsTitleDate.setText(time);
	    	if(null != symbolName){
	    		newsSymbol.setText(symbolName);
	    	}
	    	if(null != mOptionNewsBean.getSource()){
	    		newsTitleNum.setText(mOptionNewsBean.getSource().getTitle());
	    	}
	    	newsTextTitle.setText(mOptionNewsBean.getTitle());
	    	newsTextText.setText(mOptionNewsBean.getText());
	    }
	    private final String mPageName = PortfolioApplication.getInstance().getString(R.string.count_option_news);
	    @Override
		public void onPause() {
			// TODO Auto-generated method stub
			super.onPause();
			//SDK已经禁用了基于Activity 的页面统计，所以需要再次重新统计页面
			MobclickAgent.onPageEnd(mPageName);
			MobclickAgent.onPause(this);
		}

		@Override
		public void onResume() {
			// TODO Auto-generated method stub
			super.onResume();
			//SDK已经禁用了基于Activity 的页面统计，所以需要再次重新统计页面
			MobclickAgent.onPageStart(mPageName);
			MobclickAgent.onResume(this);
		}
}
