package com.dkhs.portfolio.ui;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.dkhs.portfolio.R;
import com.dkhs.portfolio.app.PortfolioApplication;
import com.dkhs.portfolio.base.widget.ImageView;
import com.dkhs.portfolio.base.widget.ListView;
import com.dkhs.portfolio.bean.CombinationBean;
import com.dkhs.portfolio.bean.FundManagerBean;
import com.dkhs.portfolio.bean.QuotesBean;
import com.dkhs.portfolio.bean.SearchGeneralBean;
import com.dkhs.portfolio.bean.SelectStockBean;
import com.dkhs.portfolio.bean.UserEntity;
import com.dkhs.portfolio.engine.LocalDataEngine.DBLoader.IResultCallback;
import com.dkhs.portfolio.engine.LocalDataEngine.VisitorDataSource;
import com.dkhs.portfolio.engine.SelectGeneralEngine;
import com.dkhs.portfolio.engine.VisitorDataEngine;
import com.dkhs.portfolio.net.DataParse;
import com.dkhs.portfolio.net.ParseHttpListener;
import com.dkhs.portfolio.ui.adapter.SelectGeneralAdapter;
import com.dkhs.portfolio.ui.eventbus.BusProvider;
import com.dkhs.portfolio.ui.eventbus.UpdateCombinationEvent;
import com.dkhs.portfolio.ui.widget.ClearableEditText;
import com.dkhs.portfolio.ui.widget.MAlertDialog;
import com.dkhs.portfolio.ui.widget.ViewBean.SelectCombinationViewBean;
import com.dkhs.portfolio.ui.widget.ViewBean.SelectDividerViewBean;
import com.dkhs.portfolio.ui.widget.ViewBean.SelectFundManagerViewBean;
import com.dkhs.portfolio.ui.widget.ViewBean.SelectMoreViewBean;
import com.dkhs.portfolio.ui.widget.ViewBean.SelectRelatedViewBean;
import com.dkhs.portfolio.ui.widget.ViewBean.SelectStockFundViewBean;
import com.dkhs.portfolio.ui.widget.ViewBean.SelectTitleViewBean;
import com.dkhs.portfolio.ui.widget.ViewBean.SelectUserViewBean;
import com.dkhs.portfolio.ui.widget.ViewBean.ViewBean;
import com.dkhs.portfolio.utils.PromptManager;
import com.dkhs.portfolio.utils.StockUitls;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.squareup.otto.Subscribe;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * @author xuetong
 *         综合搜索（股票/基金/用户/悬赏/话题）
 */
public class SelectGeneralActivity extends ModelAcitivity implements View.OnClickListener {
    @ViewInject(R.id.recyclerView)
    private RecyclerView mRecyclerView;
    @ViewInject(R.id.et_search_key)
    private ClearableEditText et_search_key;
    @ViewInject(R.id.lv_history)
    private ListView mHistoryView;

    private SelectGeneralAdapter adapter;
    private SelectGeneralEngine engine = new SelectGeneralEngine();
    private List<ViewBean> mViewBeanList = new ArrayList<>();
    private String searchString;


    private List<SelectStockBean> mDataList = new ArrayList<>();
    private List<SelectStockBean> history = new ArrayList<>();
    private MyHistoryAdapter hisAdapter;


    @Override
    protected void onCreate(Bundle arg0) {
        super.onCreate(arg0);
        setContentView(R.layout.activity_selectgeneral);
        ViewUtils.inject(this);
        hideHead();
        initViews();
        initData();
    }

    private void initData() {
        if(!PortfolioApplication.hasUserLogin())
            loadVisitorData();
        history = VisitorDataEngine.getHistory();
        mDataList.addAll(history);
        hisAdapter = new MyHistoryAdapter();
        mHistoryView.setAdapter(hisAdapter);
        mHistoryView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (hisAdapter.getItemViewType(position) != hisAdapter.TYPE_BOTTOM) {
                    SelectStockBean itemStock = mDataList.get(position);
                    if (StockUitls.isFundType(itemStock.symbol_type)) {
                        startActivity(FundDetailActivity.newIntent(SelectGeneralActivity.this, itemStock));
                    } else {
                        startActivity(StockQuotesActivity.newIntent(SelectGeneralActivity.this, itemStock));
                    }
                }
            }
        });
//        hisAdapter.notifyDataSetChanged();
    }

    private void initViews() {
        mRecyclerView.setLayoutManager(new StaggeredGridLayoutManager(4, StaggeredGridLayoutManager.VERTICAL));
        mRecyclerView.setHasFixedSize(true);
        adapter = new SelectGeneralAdapter(mViewBeanList, this);
        mRecyclerView.setAdapter(adapter);
        et_search_key.addTextChangedListener(mTextWatcher);
        showSearchHistory();
    }

    @OnClick(R.id.tv_cancel)
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_cancel:

                super.onBackPressed();
                break;
        }
    }

    ParseHttpListener parseHttpListener = new ParseHttpListener<List<ViewBean>>() {
        @Override
        protected List<ViewBean> parseDateTask(String jsonData) {
            List<ViewBean> dataList = null;
            try {
                JSONObject object = new JSONObject(jsonData);
                SearchGeneralBean result = DataParse.parseObjectJson(SearchGeneralBean.class, object);
                dataList = new ArrayList<>();
                if (result != null) {
                    //1.股票，基金搜索结果
                    List<QuotesBean> symbols = result.getSymbols();
                    if (symbols != null && symbols.size() > 0) {
                        List<QuotesBean> list_stock = new ArrayList<>();
                        List<QuotesBean> list_fund = new ArrayList<>();
                        for (QuotesBean quotes : symbols) {
                            if (StockUitls.isStockType(quotes.getSymbol_type())) {
                                //股票类
                                list_stock.add(quotes);
                            } else if (StockUitls.isFundType(quotes.getSymbol_type())) {
                                //基金类
                                list_fund.add(quotes);
                            }
                        }
                        if (list_stock.size() > 0) {
                            dataList.add(new SelectTitleViewBean(R.string.select_title_stock));
                            for (QuotesBean stockQuotes : list_stock) {
                                if(!PortfolioApplication.hasUserLogin() && !stockQuotes.isFollowed()){
                                    int index = mFollowList.indexOf(stockQuotes);
                                    if(index != -1){
                                        stockQuotes.setFollowed(mFollowList.get(index).isFollowed);
                                    }
                                }
                                dataList.add(new SelectStockFundViewBean(stockQuotes));
                            }
                            if (list_stock.size() == 3) {
                                //需要显示搜索更多股票
                                dataList.add(new SelectMoreViewBean(searchString, R.string.select_more_stock, SelectMoreViewBean.MoreType.MORE_STOCK));
                            }
                            dataList.add(new SelectDividerViewBean());
                        }
                        if (list_fund.size() > 0) {
                            dataList.add(new SelectTitleViewBean(R.string.select_title_fund));
                            for (QuotesBean fundQuotes : list_fund) {
                                if(!PortfolioApplication.hasUserLogin() && !fundQuotes.isFollowed()){
                                    int index = mFollowList.indexOf(fundQuotes);
                                    if(index != -1){
                                        fundQuotes.setFollowed(mFollowList.get(index).isFollowed);
                                    }
                                }
                                dataList.add(new SelectStockFundViewBean(fundQuotes));
                            }
                            if (list_fund.size() == 3) {
                                //需要显示搜索更多基金
                                dataList.add(new SelectMoreViewBean(searchString, R.string.select_more_fund, SelectMoreViewBean.MoreType.MORE_FUND));
                            }
                            dataList.add(new SelectDividerViewBean());
                        }
                    }
                    //2.基金经理搜索结果
                    List<FundManagerBean> managers = result.getFund_managers();
                    if (managers != null && managers.size() > 0) {
                        dataList.add(new SelectTitleViewBean(R.string.select_title_fund_manager));
                        for (FundManagerBean manager : managers) {
                            dataList.add(new SelectFundManagerViewBean(manager));
                        }
                        if (managers.size() == 4) {
                            //需要显示搜索更多基金经理
                            dataList.add(new SelectMoreViewBean(searchString, R.string.select_more_fund_manager, SelectMoreViewBean.MoreType.MORE_FUND_MANAGER));
                        }
                        dataList.add(new SelectDividerViewBean());
                    }
                    //3.用户搜索结果
                    List<UserEntity> users = result.getUsers();
                    if (users != null && users.size() > 0) {
                        dataList.add(new SelectTitleViewBean(R.string.select_title_user));
                        for (UserEntity user : users) {
                            dataList.add(new SelectUserViewBean(user));
                        }
                        if (users.size() == 4) {
                            //需要显示搜索更多用户
                            dataList.add(new SelectMoreViewBean(searchString, R.string.select_more_user, SelectMoreViewBean.MoreType.MORE_USER));
                        }
                        dataList.add(new SelectDividerViewBean());
                    }
                    //4.组合搜索结果
                    List<CombinationBean> combinationBeans = result.getPortfolios();
                    if (combinationBeans != null && combinationBeans.size() > 0) {
                        dataList.add(new SelectTitleViewBean(R.string.select_title_combination));
                        for (CombinationBean combinationBean : combinationBeans) {
                            if(!PortfolioApplication.hasUserLogin() && !combinationBean.isFollowed()){
                                int index = mFollowCombinationList.indexOf(combinationBean);
                                if(index != -1){
                                    combinationBean.setFollowed(mFollowCombinationList.get(index).isFollowed());
                                }
                            }
                            dataList.add(new SelectCombinationViewBean(combinationBean));
                        }
                        if (combinationBeans.size() == 3) {
                            //需要显示搜索更多组合
                            dataList.add(new SelectMoreViewBean(searchString, R.string.select_more_combination, SelectMoreViewBean.MoreType.MORE_COMBINATION));
                        }
                        dataList.add(new SelectDividerViewBean());
                    }
                    //5.搜索相关悬赏
                    dataList.add(new SelectRelatedViewBean(searchString, R.string.select_related_reward, SelectRelatedViewBean.RelatedType.RELATED_REWARD));
                    dataList.add(new SelectDividerViewBean());
                    //6.搜索相关话题
                    dataList.add(new SelectRelatedViewBean(searchString, R.string.select_related_topic, SelectRelatedViewBean.RelatedType.RELATED_TOPIC));
                    dataList.add(new SelectDividerViewBean());
                }
            } catch (JSONException e) {
                e.printStackTrace();
                return null;
            }
            return dataList;
        }

        @Override
        protected void afterParseData(List<ViewBean> viewBeanList) {
            if (viewBeanList != null && viewBeanList.size() > 0) {
                mViewBeanList.clear();
                mViewBeanList.addAll(viewBeanList);
                adapter.notifyDataSetChanged();
            }
        }

        @Override
        public void onFailure(int errCode, String errMsg) {
            mViewBeanList.clear();
            super.onFailure(errCode, errMsg);
        }
    };
    TextWatcher mTextWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {
            searchString = s.toString().trim();
            if (!TextUtils.isEmpty(searchString)) {
                showSearchResult(searchString);
            } else {
                notifyHistoryData();
                showSearchHistory();
            }
        }
    };

    private class MyHistoryAdapter extends BaseAdapter {
        private final int TYPE_NORMAL = 0;
        private final int TYPE_BOTTOM = 1;
        private int viewTypeCount = 2;
        private boolean isDeleteallShowing = false;

        public MyHistoryAdapter() {
            super();
            if (mDataList != null) {
                if (mDataList.size() > 3) {
                    isDeleteallShowing = false;
                } else {
                    isDeleteallShowing = true;
                }
            }
        }

        @Override
        public int getViewTypeCount() {
            return viewTypeCount;
        }

        @Override
        public int getItemViewType(int position) {
            if (mDataList != null) {
                if (position != getCount() - 1) {
                    return TYPE_NORMAL;
                } else {
                    return TYPE_BOTTOM;
                }
            }
            return super.getItemViewType(position);
        }

        @Override
        public int getCount() {
            if (mDataList != null) {
                if (isDeleteallShowing) {
                    return mDataList.size() + 1;
                } else {
                    int i = 0;
                    //TODO 教训：不能在getCount方法里做while循环，否则半天你都可能找不到bug
//                    while(mDataList.size() - i > 3){
//                        i--;
//                    }
                    if (mDataList.size() > 3) {
                        return 4;
                    } else {
                        return mDataList.size() + 1;
                    }
                }

            }
            return 1;
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            int itemViewType = getItemViewType(position);
            switch (itemViewType) {
                case TYPE_NORMAL:
                    ViewHolder holder;
                    if (convertView == null) {
                        convertView = View.inflate(mContext, R.layout.item_select_history, null);
                        holder = new ViewHolder();
                        holder.iv_item_del = (ImageView) convertView.findViewById(R.id.iv_item_del);
                        holder.tv_stock_name = (TextView) convertView.findViewById(R.id.tv_stock_name);
                        holder.tv_stock_num = (TextView) convertView.findViewById(R.id.tv_stock_num);
                        convertView.setTag(holder);
                    } else {
                        holder = (ViewHolder) convertView.getTag();
                    }
                    holder.iv_item_del.setTag(position);
                    holder.iv_item_del.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            int position = (int) v.getTag();
                            SelectStockBean removeStockBean = mDataList.remove(position);
                            VisitorDataEngine.deleteHistory(removeStockBean.parseHistoryBean());
                            notifyDataSetChanged();
                        }
                    });
                    holder.tv_stock_name.setText(mDataList.get(position).name);
                    holder.tv_stock_num.setText(mDataList.get(position).getSymbol());
                    break;
                case TYPE_BOTTOM:
                    convertView = View.inflate(mContext, R.layout.item_select_history_bottom, null);
                    View rl_history = convertView.findViewById(R.id.rl_history);
                    if (mDataList.size() == 0) {
                        rl_history.setVisibility(View.GONE);
                    } else {
                        rl_history.setVisibility(View.VISIBLE);
                        TextView tv_history = (TextView) convertView.findViewById(R.id.tv_history);
                        if (isDeleteallShowing) {
                            tv_history.setText(R.string.select_clear_history);
                        } else {
                            tv_history.setText(R.string.select_show_history);
                        }
                        rl_history.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                if (isDeleteallShowing) {
                                    showDelDialog();
                                } else {
                                    isDeleteallShowing = true;
                                    notifyDataSetChanged();
                                }
                            }
                        });
                    }
                    break;
            }
            return convertView;
        }

        private class ViewHolder {
            ImageView iv_item_del;
            TextView tv_stock_name;
            TextView tv_stock_num;
        }
    }

    private void showSearchResult(String searchString) {
        if (mHistoryView.getVisibility() != View.GONE) {
            mHistoryView.setVisibility(View.GONE);
        }
        if (mRecyclerView.getVisibility() != View.VISIBLE) {
            mRecyclerView.setVisibility(View.VISIBLE);
        }
        engine.searchBygeneral(searchString, parseHttpListener);
    }

    private void showSearchHistory() {
        if (mRecyclerView.getVisibility() != View.GONE) {
            mRecyclerView.setVisibility(View.GONE);
        }
        if (mHistoryView.getVisibility() != View.VISIBLE) {
            mHistoryView.setVisibility(View.VISIBLE);

        }
    }

    private void showDelDialog() {

        MAlertDialog builder = PromptManager.getAlertDialog(this);

        builder.setMessage(R.string.dialog_msg_del_history)
                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();

                    }
                }).setPositiveButton(R.string.confirm, new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {

                dialog.dismiss();
                clickDelHistory();

            }
        });

        builder.show();
    }

    private void clickDelHistory() {
        mDataList.clear();
        hisAdapter.notifyDataSetChanged();
        VisitorDataEngine.clearHistoryStock();
    }

    private void notifyHistoryData() {
        mDataList.clear();
        history = VisitorDataEngine.getHistory();
        mDataList.addAll(history);
        hisAdapter.notifyDataSetChanged();
    }

    @Override
    protected void onResume() {
        super.onResume();
        BusProvider.getInstance().register(this);

    }

    @Override
    public int getPageStatisticsStringId() {
        return R.string.statistics_selectgeneral;
    }

    @Override
    protected void onDestroy() {
        BusProvider.getInstance().unregister(this);
        super.onDestroy();
    }
    private List<SelectStockBean> mFollowList = new ArrayList<>();
    private List<CombinationBean> mFollowCombinationList = new ArrayList<>();

    private void loadVisitorData() {
        mFollowList.clear();
        mFollowCombinationList.clear();
        VisitorDataSource.getOptionalStockList(this, new IResultCallback<SelectStockBean>() {
            @Override
            public void onResultCallback(List<SelectStockBean> resultList) {
                mFollowList.addAll(resultList);
            }
        });
        VisitorDataSource.getOptionalCombinationList(this, new IResultCallback<CombinationBean>() {
            @Override
            public void onResultCallback(List<CombinationBean> resultList) {
                mFollowCombinationList.addAll(resultList);
            }
        });
//        mFollowCombinationList.addAll(new VisitorDataEngine().getCombinationBySort());

    }

    @Subscribe
    public void receiveStockData(SelectStockBean itemStock){
        int dataIndex = mDataList.indexOf(itemStock);
        if(dataIndex == -1){
            mDataList.add(0,itemStock);
            hisAdapter.notifyDataSetChanged();
        }
        int viewBeanIndex = mViewBeanList.indexOf(itemStock);
        if(viewBeanIndex != -1 ){
            ViewBean viewBean = mViewBeanList.get(viewBeanIndex);
            if(viewBean instanceof SelectStockFundViewBean){
                SelectStockFundViewBean sViewBean = (SelectStockFundViewBean) viewBean;
                QuotesBean quotesBean = sViewBean.getmQuotesBean();
                quotesBean.setFollowed(itemStock.isFollowed);
                sViewBean.setmQuotesBean(quotesBean);
                adapter.notifyDataSetChanged();
            }
        }
        if(!PortfolioApplication.hasUserLogin()){
            int index = mFollowList.indexOf(itemStock);
            if(index != -1){
                mFollowList.remove(index);
                if(itemStock.isFollowed()){
                    mFollowList.add(itemStock);
                }
            }else {
                if(itemStock.isFollowed())
                    mFollowList.add(itemStock);
            }
        }
    }
    @Subscribe
    public void receiveCombinationData(UpdateCombinationEvent updateCombination) {
        CombinationBean combinationBean = updateCombination.mCombinationBean;
        int index = mViewBeanList.indexOf(combinationBean);
        if (index != -1) {
            Object o = mViewBeanList.get(index);
            if (o instanceof SelectCombinationViewBean) {
                SelectCombinationViewBean combinationViewBean = (SelectCombinationViewBean) o;
                CombinationBean mCombinationBean = combinationViewBean.getmCombinationBean();
                mCombinationBean.setFollowed(combinationBean.isFollowed());
                combinationViewBean.setmCombinationBean(mCombinationBean);
                adapter.notifyDataSetChanged();
            }
        }
        if (!PortfolioApplication.hasUserLogin()) {
            index = mFollowCombinationList.indexOf(combinationBean);
            if (index != -1) {
                mFollowCombinationList.remove(index);
                if (combinationBean.isFollowed()) {
                    mFollowCombinationList.add(combinationBean);
                }
            }else{
                if (combinationBean.isFollowed()) {
                    mFollowCombinationList.add(combinationBean);
                }
            }
        }
    }

}
