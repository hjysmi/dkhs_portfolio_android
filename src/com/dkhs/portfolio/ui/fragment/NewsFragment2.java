package com.dkhs.portfolio.ui.fragment;

import java.security.acl.LastOwnerException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.database.DataSetObserver;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.MeasureSpec;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnGroupExpandListener;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.dkhs.portfolio.R;
import com.dkhs.portfolio.net.DKHSClient;
import com.dkhs.portfolio.net.IHttpListener;
import com.dkhs.portfolio.utils.ScrollViewListViewUtil;

/**
 * 资讯列表
 * @author linbing
 *
 */
public class NewsFragment2 extends Fragment{

	public static final String KEY_TITLE = "title";
	public static final String KEY_URL = "url";
	public static final String KEY_TEXT = "text";
	private ListView mListView; //资讯列表
	private MyListAdapter mAdapter;
	private LayoutInflater mLayoutInflater;
	
	private Handler mHandler = new Handler();
	
	private static List<Map<String, String>> titles = new ArrayList<Map<String,String>>();
	

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		mLayoutInflater = inflater;
		View view = inflater.inflate(R.layout.fragment_news2, null);
		
		//初始化数据
		initailDatas();
		
		initailViews(view);
		
		return view;
	}

	private void initailDatas() {
		if(titles == null || titles.size() == 0) {
			//初始化标题
			intialTitles();
		}
	}

	private void intialTitles() {
		Map<String, String> title = new HashMap<String, String>();
		title.put(KEY_TITLE, "财报摘要");
		title.put(KEY_URL, "http://58.23.5.117:8002/finance/company/f10/financeindex/101000001");
		titles.add(title);
		title = new HashMap<String, String>();
		title.put(KEY_TITLE, "操盘必读");
		title.put(KEY_URL, "http://www.sina.com");
		titles.add(title);
		title = new HashMap<String, String>();
		title.put(KEY_TITLE, "股东情况");
		title.put(KEY_URL, "http://www.sohu.com");
		titles.add(title);
		title = new HashMap<String, String>();
		title.put(KEY_TITLE, "主营业务");
		title.put(KEY_URL, "http://58.23.5.117:8002/finance/company/f10/financeindex/101000001");
		titles.add(title);
		title = new HashMap<String, String>();
		title.put(KEY_TITLE, "核心题材");
		title.put(KEY_URL, "http://58.23.5.117:8002/finance/company/f10/financeindex/101000001");
		titles.add(title);
		title = new HashMap<String, String>();
		title.put(KEY_TITLE, "机构评级");
		title.put(KEY_URL, "http://58.23.5.117:8002/finance/company/f10/financeindex/101000001");
		titles.add(title);
		title = new HashMap<String, String>();
		title.put(KEY_TITLE, "股本情况");
		title.put(KEY_URL, "http://58.23.5.117:8002/finance/company/f10/financeindex/101000001");
		titles.add(title);
		title = new HashMap<String, String>();
		title.put(KEY_TITLE, "成交回报");
		title.put(KEY_URL, "http://58.23.5.117:8002/finance/company/f10/financeindex/101000001");
		titles.add(title);
		title = new HashMap<String, String>();
		title.put(KEY_TITLE, "大宗交易");
		title.put(KEY_URL, "http://58.23.5.117:8002/finance/company/f10/financeindex/101000001");
		titles.add(title);
		title = new HashMap<String, String>();
		title.put(KEY_TITLE, "融资融券");
		title.put(KEY_URL, "http://58.23.5.117:8002/finance/company/f10/financeindex/101000001");
		titles.add(title);
		title = new HashMap<String, String>();
		title.put(KEY_TITLE, "管理层");
		title.put(KEY_URL, "http://58.23.5.117:8002/finance/company/f10/financeindex/101000001");
		titles.add(title);
		title = new HashMap<String, String>();
		title.put(KEY_TITLE, "分红融资");
		title.put(KEY_URL, "http://58.23.5.117:8002/finance/company/f10/financeindex/101000001");
		titles.add(title);
		title = new HashMap<String, String>();
		title.put(KEY_TITLE, "公司概况");
		title.put(KEY_URL, "http://58.23.5.117:8002/finance/company/f10/financeindex/101000001");
		titles.add(title);
		title = new HashMap<String, String>();
		title.put(KEY_TITLE, "回顾展望");
		title.put(KEY_URL, "http://58.23.5.117:8002/finance/company/f10/financeindex/101000001");
		titles.add(title);
		title = new HashMap<String, String>();
		title.put(KEY_TITLE, "资本运作");
		title.put(KEY_URL, "http://58.23.5.117:8002/finance/company/f10/financeindex/101000001");
		titles.add(title);
		title = new HashMap<String, String>();
		title.put(KEY_TITLE, "板块分析");
		title.put(KEY_URL, "http://58.23.5.117:8002/finance/company/f10/financeindex/101000001");
		titles.add(title);
	}

	private void initailViews(View view) {
		mListView = (ListView) view.findViewById(R.id.list_news);
		mListView.setOnItemClickListener(mOnItemClickListener);
		mAdapter = new MyListAdapter();
		mListView.setAdapter(mAdapter);
		mAdapter.registerDataSetObserver(new DataSetObserver() {
			@Override
			public void onChanged() {
				super.onChanged();
//				ScrollViewListViewUtil.setListViewHeightBasedOnChildren(mListView);
			}
		});
	}
	
	private AdapterView.OnItemClickListener mOnItemClickListener = new AdapterView.OnItemClickListener() {

		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			int pos = position;
			if(mAdapter.isLoaded(position)) {
				pos = -1;
			}
			mAdapter.loadPosition(pos);
		}
	};
	

	
	public class MyListAdapter extends BaseAdapter {
		
		private int pos = -1; //点击的索引
		
		
		
	    public void loadPosition(int pos) {
        	this.pos = pos;
        	notifyDataSetChanged();
        	
	    }
	    
	    public boolean isLoaded(int pos) {
	    	return this.pos == pos;
	    }
		 
		@Override
		public int getCount() {
			return titles.size();
		}
	

		@Override
		public Object getItem(int position) {
			return titles.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			View view = null;
        	if(convertView != null) {
        		view = convertView;
        	}else {
        		view = mLayoutInflater.inflate(R.layout.item_news_child2, null);
        	}
        	
        	TextView mTitleView = (TextView) view.findViewById(R.id.text_title);
        	WebView mWebView = (WebView) view.findViewById(R.id.view_news);
        	Map<String,String> item = (Map<String, String>) getItem(position);
        	if(item != null) {
        		mTitleView.setText(item.get(KEY_TITLE));
        		if(position == pos) {
        			mWebView.setVisibility(View.VISIBLE);
        			loadWebview(item.get(KEY_URL),mWebView);
            	}else {
            		mWebView.setVisibility(View.GONE);
            	}
        	}
        	
        	return view;
		}
		
	}
	
	/**
     * 加载webview
     * @param groupPosition
     * @param mTextView
     */
	private void loadWebview(String url, final WebView mTextView) {
//		mTextView.setVisibility(View.GONE);
//    		mTextView.setLayoutParams(new LinearLayout.LayoutParams(0, 0));
    		mTextView.loadUrl(url);
    		mTextView.setOverScrollMode(View.OVER_SCROLL_IF_CONTENT_SCROLLS);
    		mTextView.setVerticalScrollBarEnabled(false);
    		mTextView.setHorizontalScrollBarEnabled(false);
    		mTextView.getSettings().setSupportZoom(true);
    		mTextView.getSettings().setBuiltInZoomControls(true);
    		mTextView.getSettings().setJavaScriptEnabled(true);
    		mTextView.getSettings().setDomStorageEnabled(true);
    		mTextView.getSettings().setUseWideViewPort(true);
    		mTextView.getSettings().setLoadWithOverviewMode(true);
    		
    		mTextView.setWebViewClient(new WebViewClient(){
    			
    			@Override
    			public void onScaleChanged(WebView view, float oldScale,
    					float newScale) {
    				super.onScaleChanged(view, oldScale, newScale);
//    				view.requestLayout();
//    				mAdapter.resetWebView(view);
    			}
    			
    			
				@Override
				public void onPageFinished(WebView view, String url) {
					super.onPageFinished(view, url);
//					mAdapter.resetWebView(view);
					ScrollViewListViewUtil.setListViewHeightBasedOnChildren(mListView);
				}
			});
    		
	}
	
    // class TextLoadListener implements IHttpListener {
    //
    // private WebView mTextView;
    //
    // private Map<String,String> data;
    //
    // public TextLoadListener(Map<String,String> data,WebView textview) {
    // this.data = data;
    // this.mTextView = textview;
    // }
    //
    // @Override
    // public void onHttpSuccess(String text) {
    // data.put(KEY_TEXT, text);
    // mTextView.getSettings().setDefaultTextEncodingName("utf-8");
    // mTextView.loadDataWithBaseURL("about:blank",text, "text/html", "utf-8",null);
    // // mTextView.setText(Html.fromHtml(jsonObject));
    // }
    //
    // @Override
    // public void onHttpFailure(int errCode, String errMsg) {
    // Toast.makeText(getActivity(), "加载失败！", Toast.LENGTH_LONG).show();
    // }
    //
    // @Override
    // public void onHttpFailure(int errCode, Throwable err) {
    // Toast.makeText(getActivity(), "加载失败！", Toast.LENGTH_LONG).show();
    // }
    //
    // }
    //
}
