package com.dkhs.portfolio.ui;

import java.util.ArrayList;
import java.util.List;

import com.dkhs.portfolio.R;
import com.dkhs.portfolio.app.PortfolioApplication;
import com.dkhs.portfolio.bean.OptionNewsBean;
import com.dkhs.portfolio.bean.UserEntity;
import com.dkhs.portfolio.common.ConstantValue;
import com.dkhs.portfolio.engine.LoadNewsDataEngine;
import com.dkhs.portfolio.engine.NewsforImpleEngine;
import com.dkhs.portfolio.engine.OpitionNewsEngineImple;
import com.dkhs.portfolio.engine.LoadNewsDataEngine.ILoadDataBackListener;
import com.dkhs.portfolio.ui.adapter.OptionMarketAdapter;
import com.dkhs.portfolio.ui.adapter.OptionlistAdapter;
import com.dkhs.portfolio.utils.UserEntityDesUtil;
import com.lidroid.xutils.DbUtils;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView.OnItemClickListener;

public class OptionListAcitivity extends ModelAcitivity{

	private ListView mListView;

    private boolean isLoadingMore;
    private View mFootView;
    private Context context;
    private OptionlistAdapter mOptionMarketAdapter;
    private List<OptionNewsBean> mDataList;
    private LoadNewsDataEngine mLoadDataEngine;
    boolean first = true;
    private TextView iv;
    private static final String SYMBOL = "symbol";
    private static final String TYPE = "type";
    private static final String NAME = "name";
    private String symbol;
    private String type;
    private String name;
	@Override
	protected void onCreate(Bundle arg0) {
		// TODO Auto-generated method stub
		super.onCreate(arg0);
		setContentView(R.layout.activity_option_market_news);
		context = this;
		mDataList = new ArrayList<OptionNewsBean>();
		
		iv = (TextView) findViewById(android.R.id.empty);
        // iv.setText("暂无公告");
		Bundle extras = getIntent().getExtras();
		if(null != extras){
			symbol = extras.getString(SYMBOL);
			type = extras.getString(TYPE);
			name = extras.getString(NAME);
		}
		((TextView) findViewById(R.id.tv_title)).setText("公告-" + name);
		initDate();
	}
	public static Intent newIntent(Context context, String symbolName,String type,String name) {
        Intent intent = new Intent(context, OptionListAcitivity.class);
        Bundle b = new Bundle();
        b.putString(SYMBOL, symbolName);
        b.putString(TYPE, type);
        b.putString(NAME, name);
         intent.putExtras(b);
        return intent;
    }
	private void initDate(){
		UserEntity user;
			try {
				user = DbUtils.create(PortfolioApplication.getInstance())
						.findFirst(UserEntity.class);
				if (user != null) {
					if (!TextUtils.isEmpty(user.getAccess_token())) {
						user = UserEntityDesUtil.decode(user, "ENCODE",
								ConstantValue.DES_PASSWORD);
					}
					String userId = user.getId()+"";
					NewsforImpleEngine vo = new NewsforImpleEngine();
					vo.setUserid(userId);
					vo.setSymbol(symbol);
					vo.setContentType(type);
					mLoadDataEngine = new OpitionNewsEngineImple(mSelectStockBackListener,OpitionNewsEngineImple.NEWSFOREACH,vo);
					mLoadDataEngine.loadData();
					mLoadDataEngine.setLoadingDialog(context).beforeRequest();;
					mLoadDataEngine.setFromYanbao(false);
				}
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
	}
	private void initView() {
        mFootView = View.inflate(context, R.layout.layout_loading_more_footer, null);
        mListView = (ListView) findViewById(android.R.id.list);
        
        mListView.setEmptyView(iv);
        mListView.addFooterView(mFootView);
        mOptionMarketAdapter = new OptionlistAdapter(context, mDataList);
        mListView.setAdapter(mOptionMarketAdapter);

        mListView.removeFooterView(mFootView);
        mListView.setOnScrollListener(new OnScrollListener() {

            @Override
            public void onScrollStateChanged(AbsListView absListView, int scrollState) {

                switch (scrollState) {
                    case OnScrollListener.SCROLL_STATE_IDLE:

                    {
                        // 判断是否滚动到底部
                        if (absListView.getLastVisiblePosition() == absListView.getCount() - 1 && !isLoadingMore) {
                            loadMore();

                        }
                    }

                }

            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

            }
        });
            mListView.setOnItemClickListener(itemBackClick);

    }
    OnItemClickListener itemBackClick = new OnItemClickListener() {
		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			// TODO Auto-generated method stub
			try {
				if(null != mDataList.get(position).getSymbols() && mDataList.get(position).getSymbols().size() > 0){
				Intent intent = NewsActivity.newIntent(context, mDataList.get(position).getId(), "公告正文",mDataList.get(position).getSymbols().get(0).getAbbrName(),mDataList.get(position).getSymbols().get(0).getId());
				startActivity(intent);
				}else{
					Intent intent = NewsActivity.newIntent(context, mDataList.get(position).getId(), "公告正文",null,null);
					startActivity(intent);
				}
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
    };

    private void loadMore() {
        if (null != mLoadDataEngine) {
            if (mLoadDataEngine.getCurrentpage() >= mLoadDataEngine.getTotalpage()) {
                Toast.makeText(context, "没有更多的数据了", Toast.LENGTH_SHORT).show();
                return;
            }
            mListView.addFooterView(mFootView);

            isLoadingMore = true;
            mLoadDataEngine.loadMore();
            mLoadDataEngine.setLoadingDialog(context).beforeRequest();;
        }
    }
    ILoadDataBackListener mSelectStockBackListener = new ILoadDataBackListener() {

        @Override
        public void loadFinish(List<OptionNewsBean> dataList) {
            try {
				if (null != dataList&&dataList.size()>0) {
				    mDataList.addAll(dataList);
				    if(first){
				    	initView();
				    	first = false;
				    }
				    mOptionMarketAdapter.notifyDataSetChanged();
				    loadFinishUpdateView();
				    
				}else{
				    iv.setText("暂无公告");
				}
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

        }

    };
    private void loadFinishUpdateView() {
    	mOptionMarketAdapter.notifyDataSetChanged();
        isLoadingMore = false;
        if (mListView != null) {
            mListView.removeFooterView(mFootView);
        }
    }
    
}
