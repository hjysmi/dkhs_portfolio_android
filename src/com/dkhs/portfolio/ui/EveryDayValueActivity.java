package com.dkhs.portfolio.ui;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.dkhs.portfolio.R;
import com.dkhs.portfolio.bean.CombinationBean;
import com.dkhs.portfolio.bean.HistoryProfitNetValue;
import com.dkhs.portfolio.bean.HistoryProfitNetValue.HistoryNetBean;
import com.dkhs.portfolio.engine.NetValueEngine;
import com.dkhs.portfolio.net.DataParse;
import com.dkhs.portfolio.net.ParseHttpListener;
import com.dkhs.portfolio.ui.widget.PullToRefreshListView;
import com.dkhs.portfolio.ui.widget.PullToRefreshListView.OnLoadMoreListener;
import com.dkhs.portfolio.utils.PromptManager;

public class EveryDayValueActivity extends ModelAcitivity implements OnLoadMoreListener{
	public static final String EXTRA_COMBINATION = "extra_combination";
	private CombinationBean mCombinationBean;
	private PullToRefreshListView mListView;
	private NetValueEngine netValueEngine;
	@Override
	protected void onCreate(Bundle bundle) {
		// TODO Auto-generated method stub
		super.onCreate(bundle);
		setContentView(R.layout.activity_everyday_record);
		setTitle(R.string.privacy_everyday_record);
		Bundle extras = getIntent().getExtras();
        if (extras != null) {
            handleExtras(extras);
        }
        initView();
        initData();
	}
	private int count = 30;
	private int page = 1;
	private int total_count = 0;
	private int total_page = 0;
	
	private List<HistoryNetBean> lists = new ArrayList<HistoryNetBean>();
	private MyAdapter adapter;
	private MyIhttpListener listener;
	
	private void initData() {
		adapter = new MyAdapter();
		mListView.setAdapter(adapter);
		netValueEngine = new NetValueEngine(mCombinationBean.getId());
		listener = new MyIhttpListener();
		PromptManager.showProgressDialog(this, "");
		netValueEngine.requeryHistory(count, page, listener);
	}
	private class MyIhttpListener extends ParseHttpListener<HistoryProfitNetValue>{

		@Override
		protected HistoryProfitNetValue parseDateTask(String jsonData) {
			HistoryProfitNetValue historyValue = DataParse.parseObjectJson(HistoryProfitNetValue.class, jsonData);
			total_count = historyValue.getTotal_count();
			total_page = historyValue.getTotal_page();
            return historyValue;
		}

		@Override
		protected void afterParseData(HistoryProfitNetValue object) {
			// TODO Auto-generated method stub
			if(object != null){
				if(page <= total_page){
					lists.addAll(object.getResults());
					adapter.notifyDataSetChanged();
					page ++;
					PromptManager.closeProgressDialog();
					mListView.onLoadMoreComplete();
				}else{
					mListView.setCanLoadMore(false);
				}
			}
		}
		
	}

	private void initView() {
		mListView = (PullToRefreshListView) findViewById(R.id.mListView);
		mListView.setCanLoadMore(true);
		mListView.setAutoLoadMore(true);
		mListView.setOnLoadListener(this);
	}

	private void handleExtras(Bundle extras) {
        mCombinationBean = (CombinationBean) extras.getSerializable(EXTRA_COMBINATION);

    }
	public static Intent newIntent(Context context, CombinationBean combinationBean) {
        Intent intent = new Intent(context, EveryDayValueActivity.class);

        intent.putExtra(EXTRA_COMBINATION, combinationBean);

        return intent;
    }
	
	private class MyAdapter extends BaseAdapter{

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return lists.size();
		}

		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return lists.get(position);
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			// TODO Auto-generated method stub
			ViewHolder holder = null;
			if (convertView == null) {
				synchronized (EveryDayValueActivity.this) {
					convertView = View.inflate(getApplicationContext(), R.layout.item_history_profit, null);
					holder = new ViewHolder();
					holder.tv_date = (TextView) convertView.findViewById(R.id.tv_date);
					holder.tv_value = (TextView) convertView.findViewById(R.id.tv_value);
					holder.tv_percent = (TextView) convertView.findViewById(R.id.tv_percent);
					convertView.setTag(holder);
				}
			}else{
				holder = (ViewHolder) convertView.getTag();
			}
			HistoryNetBean historyNetBean = lists.get(position);
			holder.tv_date.setText(historyNetBean.getDate());
			holder.tv_value.setText(historyNetBean.getNetvalue()+"");
			float percent = historyNetBean.getPercentage();
			if(percent >= 0){
				holder.tv_percent.setTextColor(getResources().getColor(R.color.red));
			}else{
				holder.tv_percent.setTextColor(getResources().getColor(R.color.green));
			}
			holder.tv_percent.setText(percent+"%");
			return convertView;
		}
		class ViewHolder {
			TextView tv_date;
			TextView tv_value;
			TextView tv_percent;
		}
		
	}

	@Override
	public void onLoadMore() {
		if(page > total_page){
			PromptManager.showToast("当前没有更多了...");
			mListView.setLodaMoreText("当前没有更多了...");
		}else{
			netValueEngine.requeryHistory(count, page, listener);
		}
	}
}
