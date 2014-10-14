package com.dkhs.portfolio.ui.fragment;

import java.security.acl.LastOwnerException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.Activity;
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
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnGroupExpandListener;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;
import android.widget.Toast;

import com.dkhs.portfolio.R;
import com.dkhs.portfolio.net.DKHSClient;
import com.dkhs.portfolio.net.IHttpListener;

/**
 * 资讯列表
 * @author linbing
 *
 */
public class NewsFragment extends Fragment{

	public static final String KEY_TITLE = "title";
	public static final String KEY_URL = "url";
	public static final String KEY_TEXT = "text";
	private ExpandableListView mListView; //资讯列表
	private MyExpandableListAdapter mAdapter;
	private LayoutInflater mLayoutInflater;
	
	private Handler mHandler = new Handler();
	
	private static List<Map<String, String>> titles = new ArrayList<Map<String,String>>();
	

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		mLayoutInflater = inflater;
		View view = inflater.inflate(R.layout.fragment_news, null);
		
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
		title.put(KEY_URL, "http://58.23.5.117:8002/finance/company/f10/financeindex/101000001");
		titles.add(title);
		title = new HashMap<String, String>();
		title.put(KEY_TITLE, "股东情况");
		title.put(KEY_URL, "http://58.23.5.117:8002/finance/company/f10/financeindex/101000001");
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
		mListView = (ExpandableListView) view.findViewById(R.id.list_news);
		mAdapter = new MyExpandableListAdapter();
		mListView.setAdapter(mAdapter);
		mListView.setOnGroupExpandListener(onGroupClickListener);
	}
	
	private OnGroupExpandListener onGroupClickListener = new OnGroupExpandListener() {

		@Override
		public void onGroupExpand(int groupPosition) {
			mAdapter.loadPosition(groupPosition);
			
			//关闭其他的
			collapseGroups(groupPosition);
		}

		private void collapseGroups(int groupPosition) {
			int count = mAdapter.getGroupCount();
			for(int i=0; i<count; i++) {
				if(i == groupPosition) {
					continue;
				}
				mListView.collapseGroup(i);
			}
		}
		
	};
	
	public class MyExpandableListAdapter extends BaseExpandableListAdapter {
       
        private int pos; //点击的索引
        private int loastPos; //上一次点击位置
        private View mClickItemView;
		
        
        public void loadPosition(int pos) {
        	this.loastPos = this.pos;
        	this.pos = pos;
        	
//        	mAdapter.notifyDataSetChanged();
        }
		
        public Object getChild(int groupPosition, int childPosition) {
        	Map<String,String> group = titles.get(groupPosition);
        	if(group != null) {
        		return group.get(KEY_URL);
        	}
            return null;
        }

        public long getChildId(int groupPosition, int childPosition) {
            return childPosition;
        }

        public int getChildrenCount(int groupPosition) {
            return 1;
        }

        
        public View getChildView(int groupPosition, int childPosition, boolean isLastChild,
                View convertView, ViewGroup parent) {
        	View view = null;
        	if(convertView != null) {
        		view = convertView;
        	}else {
        		view = mLayoutInflater.inflate(R.layout.item_news_child, null);
        	}
        	if(groupPosition == pos) {
        		mClickItemView = view;
//        		if(groupPosition != loastPos) {
        			loastPos = groupPosition;
            		WebView mTextView = (WebView) view.findViewById(R.id.view_news);
                	loadText(groupPosition,mTextView);
//        		}
        	}else {
        	}
        	
        	
        	return view;
        }
        
        /**
         * 添加webview
         * @param webview
         */
        public void resetWebView(final WebView webview) {
        	if(webview == null) {
        		return;
        	}
        	webview.measure(0, 0);
        	int height = webview.getContentHeight();
        	if(height == 0) {
        		height = ViewGroup.LayoutParams.WRAP_CONTENT;
        	}
        	LinearLayout.LayoutParams lp = (LayoutParams) webview.getLayoutParams();
//        	lp.height = height;
//        	webview.invalidate();
//        	if(mClickItemView != null) {
//        		AbsListView.LayoutParams ilp = (android.widget.AbsListView.LayoutParams) mClickItemView.getLayoutParams();
//        		if(ilp != null) {
//        			ilp.height = height;
//        			if(height > 0) {
//        				mListView.invalidate();
//        			}
//        		}
//        	}
        	
//        	mListView.measure(0, 0);
//        	webview.invalidate();
//        	webview.requestLayout();
//        	webview.requestLayout();
        	//        	mAdapter.notifyDataSetChanged();
        }


        /**
         * 加载文本
         * @param groupPosition
         * @param mTextView
         */
		private void loadText(int groupPosition, final WebView mTextView) {
//			mTextView.setVisibility(View.GONE);
			Map<String,String> group = titles.get(groupPosition);
        	if(group != null) {
        		String url = group.get(KEY_URL);
//        		mTextView.setLayoutParams(new LinearLayout.LayoutParams(0, 0));
        		mTextView.loadUrl(url);
        		mTextView.setOverScrollMode(View.OVER_SCROLL_IF_CONTENT_SCROLLS);
        		mTextView.getSettings().setJavaScriptEnabled(true);
        		
        		mTextView.setWebViewClient(new WebViewClient(){
        			
        			@Override
        			public void onScaleChanged(WebView view, float oldScale,
        					float newScale) {
        				super.onScaleChanged(view, oldScale, newScale);
//        				view.requestLayout();
        				mAdapter.resetWebView(view);
        			}
        			
        			
    				@Override
    				public void onPageFinished(WebView view, String url) {
    					super.onPageFinished(view, url);
    					mAdapter.resetWebView(view);
    				}
    			});
        		
//        		String text = group.get(KEY_TEXT);
        		
        		//测试
//        		mTextView.loadUrl("file:///android_asset/test.html"); 
//        		if(text != null && text.trim().length() > 0) {
//        			mTextView.getSettings().setDefaultTextEncodingName("utf-8");
//        			mTextView.loadDataWithBaseURL("about:blank",text, "text/html", "utf-8",null);
//        			mTextView.setWebViewClient(new WebViewClient(){
//        				@Override
//        				public void onPageFinished(WebView view, String url) {
//        					super.onPageFinished(view, url);
//        					view.requestLayout();
//        					mListView.requestLayout();
//        				}
//        			});
////        			mTextView.setText(Html.fromHtml(text));
//        		}else {
//        			//请求网络加载数据
//        			String url = group.get(KEY_URL);
//            		DKHSClient.requestByGet(url, null, null, new TextLoadListener(group,mTextView));
//        		}
        		
        	}
		}

		public Object getGroup(int groupPosition) {
        	Map<String,String> group = titles.get(groupPosition);
        	if(group != null) {
        		return group.get(KEY_TITLE);
        	}
            return null;
        }

        public int getGroupCount() {
            return titles.size();
        }

        public long getGroupId(int groupPosition) {
            return groupPosition;
        }

        public View getGroupView(int groupPosition, boolean isExpanded, View convertView,
                ViewGroup parent) {
        	View view = mLayoutInflater.inflate(R.layout.item_news_group, null);
        	TextView textView = (TextView) view.findViewById(R.id.text_name);
        	textView.setText(getGroup(groupPosition).toString());
            return view;
        }

        public boolean isChildSelectable(int groupPosition, int childPosition) {
            return true;
        }

        public boolean hasStableIds() {
            return true;
        }

    }
	
	class TextLoadListener implements IHttpListener {

		private WebView mTextView;
		
		private Map<String,String> data;
		
		public TextLoadListener(Map<String,String> data,WebView textview) {
			this.data = data;
			this.mTextView = textview;
		}
		
		@Override
		public void onHttpSuccess(String text) {
			data.put(KEY_TEXT, text);
			mTextView.getSettings().setDefaultTextEncodingName("utf-8");
			mTextView.loadDataWithBaseURL("about:blank",text, "text/html", "utf-8",null);
//			mTextView.setText(Html.fromHtml(jsonObject));			
		}

		@Override
		public void onHttpFailure(int errCode, String errMsg) {
			Toast.makeText(getActivity(), "加载失败！", Toast.LENGTH_LONG).show();
		}

		@Override
		public void onHttpFailure(int errCode, Throwable err) {
			Toast.makeText(getActivity(), "加载失败！", Toast.LENGTH_LONG).show();
		}
		
	}
	
}