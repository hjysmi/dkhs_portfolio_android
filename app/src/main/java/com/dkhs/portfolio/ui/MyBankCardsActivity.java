package com.dkhs.portfolio.ui;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.StateListDrawable;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.dkhs.portfolio.R;
import com.dkhs.portfolio.bean.MyBankCard;
import com.dkhs.portfolio.engine.TradeEngineImpl;
import com.dkhs.portfolio.net.ParseHttpListener;
import com.dkhs.portfolio.net.StringDecodeUtil;
import com.dkhs.portfolio.ui.widget.PullToRefreshListView;
import com.dkhs.portfolio.utils.ActivityCode;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.lidroid.xutils.BitmapUtils;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.bitmap.BitmapDisplayConfig;
import com.lidroid.xutils.bitmap.PauseOnScrollListener;
import com.lidroid.xutils.bitmap.callback.BitmapLoadFrom;
import com.lidroid.xutils.bitmap.callback.DefaultBitmapLoadCallBack;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zhangcm on 2015/9/14.15:02
 */
public class MyBankCardsActivity extends ModelAcitivity {


    private SwipeRefreshLayout mSwipeLayout;

    private PullToRefreshListView mListView;

    private TextView tvEmptyText;

    private View mProgressView;

    @Override
    protected void onCreate(Bundle arg0) {
        super.onCreate(arg0);
        setContentView(R.layout.activity_my_bank_cards);
        ViewUtils.inject(this);
        setTitle(R.string.bank_card_package);
        TextView right = getRightButton();
        right.setText(R.string.add_text);
        right.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO 添加银行卡
                startActivityForResult(new Intent(mContext, BankCardNoActivity.class), ActivityCode.BANK_CARD_NO_REQUEST.ordinal());
            }
        });
        initLoadMoreList();
    }

    private BitmapUtils mBitmapUtils;
    private MyBankCardsAdapter mAdapter;
    private void initLoadMoreList() {
        mSwipeLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_container);
//        mSwipeLayout.setOnRefreshListener(setOnRefreshListener());
        mSwipeLayout.setColorSchemeResources(R.color.theme_blue);

        mListView = (PullToRefreshListView) findViewById(android.R.id.list);
        mListView.setDivider(null);
        mAdapter = new MyBankCardsAdapter();
        mListView.setAdapter(mAdapter);
        tvEmptyText = (TextView) findViewById(android.R.id.empty);
        mProgressView = (RelativeLayout) findViewById(android.R.id.progress);

//        mListView.setAdapter(getListAdapter());
//        mListView.setOnItemClickListener(getItemClickListener());
//        setListViewInit(mListView);
        mBitmapUtils = new BitmapUtils(this);
        mListView.setOnScrollListener(new PauseOnScrollListener(mBitmapUtils, false, true));
        mListView.setCanRefresh(false);
        mListView.postDelayed(new Runnable() {
            @Override
            public void run() {
                loadData();
            }
        }, 500);
        callBack = new MyBitmapLoadCallBack();
    }

    SwipeRefreshLayout.OnRefreshListener listener = new SwipeRefreshLayout.OnRefreshListener() {
        @Override
        public void onRefresh() {

        }
    };

    private List<MyBankCard> myCards = new ArrayList<MyBankCard>();

    public void loadData() {
        mSwipeLayout.setRefreshing(true);
        ParseHttpListener listener = new ParseHttpListener<List<MyBankCard>>() {
            @Override
            protected List<MyBankCard> parseDateTask(String jsonData) {
                List<MyBankCard> myCards = null;
                if (!TextUtils.isEmpty(jsonData)) {
                    try {
                        jsonData = StringDecodeUtil.decodeUnicode(jsonData);
                        Gson gson = new GsonBuilder().enableComplexMapKeySerialization().create();
                        myCards = gson.fromJson(jsonData, new TypeToken<List<MyBankCard>>(){}.getType());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                return myCards;
            }

            @Override
            protected void afterParseData(List<MyBankCard> cards) {
                if(cards != null && cards.size() > 0){
                    myCards = cards;
                    mAdapter.notifyDataSetChanged();
                }
                mSwipeLayout.setRefreshing(false);
                mSwipeLayout.setEnabled(false);
            }
        };
        new TradeEngineImpl().getMyBankCards(listener);

    }

    private class MyBankCardsAdapter extends BaseAdapter{


        @Override
        public int getCount() {
            return myCards.size() + 1;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder;
            if(convertView == null){
                convertView = View.inflate(getApplicationContext(), R.layout.item_my_bank_cards, null);
                holder = new ViewHolder();
                holder.ll_bank = (View)convertView.findViewById(R.id.rl_bank);
                holder.ll_fund_supervision_bank = (View)convertView.findViewById(R.id.ll_fund_supervision_bank);
                holder.seperate = (View)convertView.findViewById(R.id.seperate);
                holder.iv_bank_logo = (ImageView)convertView.findViewById(R.id.iv_bank_logo);
                holder.tv_addbank = (TextView)convertView.findViewById(R.id.tv_addbank);
                holder.tv_bank_name = (TextView)convertView.findViewById(R.id.tv_bank_name);
                holder.tv_card_num = (TextView)convertView.findViewById(R.id.tv_card_num);
                holder.tv_username = (TextView)convertView.findViewById(R.id.tv_username);
                convertView.setTag(holder);
            }else{
                holder = (ViewHolder)convertView.getTag();
            }
            if(position == myCards.size()){
                holder.tv_addbank.setVisibility(View.VISIBLE);
                holder.seperate.setVisibility(View.GONE);
                holder.ll_bank.setVisibility(View.GONE);
                holder.tv_addbank.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        startActivityForResult(new Intent(mContext, BankCardNoActivity.class), ActivityCode.BANK_CARD_NO_REQUEST.ordinal());
                    }
                });
            }else{
                holder.tv_addbank.setVisibility(View.GONE);
                holder.seperate.setVisibility(View.VISIBLE);
                holder.ll_bank.setVisibility(View.VISIBLE);
            }

            if(position == 0){
                holder.ll_fund_supervision_bank.setVisibility(View.VISIBLE);
            }else{
                holder.ll_fund_supervision_bank.setVisibility(View.GONE);
            }
            if(myCards.size() > 0 && position != myCards.size()){
                MyBankCard card = myCards.get(position);
//                GradientDrawable myGrad = (GradientDrawable)holder.ll_bank.getBackground();
//                myGrad.setColor(getResources().getColor(R.color.orange));
                StateListDrawable list = (StateListDrawable)holder.ll_bank.getBackground();
                GradientDrawable myGrad = (GradientDrawable) getResources().getDrawable(R.drawable.item_bankcard_package_bg);
//                myGrad.setColor(getResources().getColor(R.color.orange));
                myGrad.setColor(getBankbg(Integer.parseInt(card.getBank().getId())));
                list.addState(new int []{},myGrad);
                holder.ll_bank.setBackgroundDrawable(list);
                mBitmapUtils.display(holder.iv_bank_logo, card.getBank().getLogo(), null, callBack);
                holder.tv_username.setText(card.getReal_name());
                holder.tv_bank_name.setText(card.getBank().getName());
                holder.tv_card_num.setText("**** **** **** "+card.getBank_card_no_tail());
            }
            return convertView;
        }

        private class ViewHolder{
            View ll_fund_supervision_bank;
            View ll_bank;
            View seperate;
            ImageView iv_bank_logo;
            TextView tv_addbank;
            TextView tv_bank_name;
            TextView tv_card_num;
            TextView tv_username;

        }
    }

    private MyBitmapLoadCallBack callBack;

    private class MyBitmapLoadCallBack <T extends View>extends DefaultBitmapLoadCallBack<T>{

        @Override
        public void onLoadCompleted(T container, String uri, Bitmap bitmap, BitmapDisplayConfig config, BitmapLoadFrom from) {
            super.onLoadCompleted(container, uri, bitmap, config, from);
            setBackGroundWhite(container,bitmap);
        }

        public void setBackGroundWhite(T container,Bitmap bitmap){
            int roundPixels;
            int width = bitmap.getWidth();
            int height = bitmap.getHeight();
            if(width <= height){
                roundPixels = width / 2;
            }else{
                roundPixels = height / 2;
            }
            //创建一个和原始图片一样大小位图
            int color = container.getResources().getColor(R.color.white);
            Bitmap roundConcerImage = Bitmap.createBitmap(bitmap.getWidth(),
                    bitmap.getHeight(), Bitmap.Config.ARGB_8888);
            //创建带有位图roundConcerImage的画布
            Canvas canvas = new Canvas(roundConcerImage);
            //创建画笔
            Paint paint = new Paint();
            paint.setColor(color);
            // 去锯齿
            paint.setAntiAlias(true);
            canvas.drawCircle(width/2,height/2,roundPixels,paint);
            container.setBackgroundDrawable(new BitmapDrawable(roundConcerImage));
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == ActivityCode.BANK_CARD_NO_REQUEST.ordinal() && resultCode == ActivityCode.BANK_CARD_NO_RESULT.ordinal()){
            //请求获取数据
            loadData();
        }
    }
    private int RED = Color.parseColor("#ff5555");
    private int ORANGE = Color.parseColor("#fca321");
    private int BLUE = Color.parseColor("#0c70c1");
    private int GREEN = Color.parseColor("#009688");
    private int GOLDEN = Color.parseColor("#fca321");
    private int getBankbg(int bank_id){
        if (bank_id == 1 || bank_id == 4 || bank_id == 6 || bank_id == 8 || bank_id == 9 || bank_id == 14 || bank_id == 15) {
            return RED;
        } else if (bank_id == 3 || bank_id == 5 || bank_id == 11 || bank_id == 16){
            return BLUE;
        } else if (bank_id == 2 || bank_id == 10 || bank_id == 13){
            return GREEN;
        } else if (bank_id == 7){
            return GOLDEN;
        } else if (bank_id == 12 || bank_id == 17){
            return ORANGE;
        } else {
            return GOLDEN;
        }

    }
}
