package com.dkhs.portfolio.ui;

import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.dkhs.portfolio.R;
import com.dkhs.portfolio.bean.OptionNewsBean;
import com.dkhs.portfolio.engine.LoadNewsTextEngine;
import com.dkhs.portfolio.engine.LoadNewsTextEngine.ILoadDataBackListener;
import com.dkhs.portfolio.engine.NewsTextEngineImple;

public class NewsActivity extends ModelAcitivity{
	private ImageView newsTitleIcon;
	private TextView newsTitleName;
	private TextView newsTitleDate;
	private TextView newsTitleNum;
	private TextView newsTextTitle;
	private TextView newsTextText;
	private static final String EXTRA = "newsId";
	private static final String EXTRA_NUM = "num";
	private static final String EXTRA_NAME = "name";
	private String textId;
	 private LoadNewsTextEngine mLoadDataEngine;
	 private OptionNewsBean mOptionNewsBean;
	 private String optionNum;
	 private String optionName;
	@Override
	protected void onCreate(Bundle arg0) {
		// TODO Auto-generated method stub
		super.onCreate(arg0);
		setContentView(R.layout.activity_news);
		initView();
		Bundle extras = getIntent().getExtras();
	    if (extras != null) {
	        getId(extras);
	    }
	    mLoadDataEngine = new NewsTextEngineImple(mSelectStockBackListener, textId);
		mLoadDataEngine.loadData();
	}
	public static Intent newIntent(Context context, String id,String num,String name) {
        Intent intent = new Intent(context, NewsActivity.class);
         intent.putExtra(EXTRA, id);
         intent.putExtra(EXTRA_NUM, num);
         intent.putExtra(EXTRA_NAME, name);
        return intent;
    }
	
    public void getId(Bundle b){
    	textId = b.getString(EXTRA);
    	optionNum = b.getString(EXTRA_NUM);
    	optionName = b.getString(EXTRA_NAME);
    }
	public void initView(){
		newsTitleIcon = (ImageView) findViewById(R.id.news_title_icon);
		newsTitleName = (TextView) findViewById(R.id.news_title_name);
		newsTitleDate = (TextView) findViewById(R.id.news_title_date);
		newsTitleNum = (TextView) findViewById(R.id.news_title_num);
		newsTextTitle = (TextView) findViewById(R.id.news_text_title);
		newsTextText = (TextView) findViewById(R.id.news_text_text);
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
	    	newsTitleName.setText(optionName);
	    	newsTitleDate.setText(mOptionNewsBean.getCreatedTime().replace("T", " ").substring(0, mOptionNewsBean.getCreatedTime().length()-4));
	    	newsTitleNum.setText(optionNum);
	    	newsTextTitle.setText(mOptionNewsBean.getTitle());
	    	newsTextText.setText(mOptionNewsBean.getText());
	    }
}
