package com.dkhs.portfolio.ui;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.dkhs.portfolio.R;
import com.dkhs.portfolio.base.widget.Button;
import com.dkhs.portfolio.base.widget.ListView;
import com.dkhs.portfolio.bean.Bank;
import com.dkhs.portfolio.bean.Fund;
import com.dkhs.portfolio.bean.MyBankCard;
import com.dkhs.portfolio.bean.MyFundInfo;
import com.dkhs.portfolio.engine.TradeEngineImpl;
import com.dkhs.portfolio.net.ParseHttpListener;
import com.dkhs.portfolio.net.StringDecodeUtil;
import com.dkhs.portfolio.utils.PromptManager;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.jungly.gridpasswordview.GridPasswordView;
import com.lidroid.xutils.BitmapUtils;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.bitmap.BitmapDisplayConfig;
import com.lidroid.xutils.bitmap.PauseOnScrollListener;
import com.lidroid.xutils.bitmap.callback.BitmapLoadFrom;
import com.lidroid.xutils.bitmap.callback.DefaultBitmapLoadCallBack;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;

import org.json.JSONObject;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by zhangcm on 2015/9/23.15:56
 */
public class BuyFundActivity extends ModelAcitivity {

    @ViewInject(R.id.et_value)
    private EditText et_value;

    @ViewInject(R.id.btn_buy)
    private Button btn_buy;

    @ViewInject(R.id.tv_fund_name)
    private TextView tv_fund_name;

    @ViewInject(R.id.tv_net_value)
    private TextView tv_net_value;

    @ViewInject(R.id.tv_buy_value)
    private TextView tv_buy_value;

    @ViewInject(R.id.tv_bank_card_no_tail)
    private TextView tv_bank_card_no_tail;

    @ViewInject(R.id.iv_bank_logo)
    private ImageView iv_bank_logo;

    @ViewInject(R.id.tv_limit_value)
    private TextView tv_limit_value;

    @ViewInject(R.id.tv_buy_poundage)
    private TextView tv_buy_poundage;

    private boolean isBankcardChoosed;

    private static String FUND_INFO = "fund_info";
    private MyFundInfo mFundInfo;
    private Fund mFund;
    private MyBankCard card;
    private double limitValue;
    public static Intent buyIntent(Context context, MyFundInfo fundInfo){
        Intent intent = new Intent(context, BuyFundActivity.class);
        intent.putExtra(FUND_INFO, fundInfo);
        return intent;
    }

    @Override
    protected void onCreate(Bundle arg0) {
        super.onCreate(arg0);
        setContentView(R.layout.activity_buy_fund);
        ViewUtils.inject(this);
        Bundle extras = getIntent().getExtras();
        if(extras != null)
            handleExtras(extras);
        setTitle(R.string.buy_in);
        initViews();
        initData();
    }

    private void handleExtras(Bundle extras) {
        mFundInfo = (MyFundInfo) extras.getSerializable(FUND_INFO);
        mFund = mFundInfo.getFund();
    }

    @OnClick(R.id.rl_select_bank)
    private void onClick(View view){
        showSelectBankCardDialog();
    }

    private void initViews() {
        et_value.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                if(!TextUtils.isEmpty(s)){
                    double value = Double.parseDouble(s.toString());
                    if(value < limitValue){
                        btn_buy.setEnabled(false);
                    }else{
                        value = value*mFund.getFare_ratio_buy()*0.01*mFund.getDiscount_rate_buy()*0.01;
                        BigDecimal decimal = new BigDecimal(value);
                        value = decimal.setScale(2,   RoundingMode.HALF_UP).doubleValue();
                        tv_buy_poundage.setText(String.format(getResources().getString(R.string.blank_buy_fund_tip2), String.valueOf(value)));
                        btn_buy.setEnabled(isBankcardChoosed);
                    }
                }
            }
        });
        btn_buy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showTradePwdDialog();
            }
        });
    }

    private void initData() {
        limitValue = Double.parseDouble(mFund.getAmount_min());
        tv_fund_name.setText(String.format(getResources().getString(R.string.blank_fund_name), mFund.getName(),mFund.getId()));
        tv_net_value.setText(String.format(getResources().getString(R.string.blank_net_value), mFund.getNet_value()));
        tv_buy_value.setText(String.format(getResources().getString(R.string.blank_buy_value), mFund.getAmount_min()));
        tv_buy_poundage.setText(String.format(getResources().getString(R.string.blank_buy_fund_tip1), mFund.getFare_ratio_buy()+"%"));
        et_value.setHint(String.format(getResources().getString(R.string.blank_hint_value), String.valueOf(mFund.getAmount_min())));
        mBitmapUtils = new BitmapUtils(this);
    }

    private BitmapUtils mBitmapUtils;
    private List<MyBankCard> myCards = new ArrayList<MyBankCard>();
    @Override
    protected void onResume() {
        super.onResume();
        //TODO 获取我的银行卡列表
        ParseHttpListener<List<MyBankCard>> listener = new ParseHttpListener<List<MyBankCard>>() {
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
                    card = myCards.get(0);
                    Bank bank = myCards.get(0).getBank();
                    mBitmapUtils.display(iv_bank_logo, bank.getLogo(), null, callBack);
                    tv_limit_value.setText(String.format(getResources().getString(R.string.blank_limit_value),bank.getSingle_limit(),bank.getSingle_day_limit()));
                    tv_bank_card_no_tail.setText(bank.getName()+"(" +myCards.get(0).getBank_card_no_tail()+")");
                    isBankcardChoosed = true;
                    if(!TextUtils.isEmpty(et_value.getText()) && Double.parseDouble(et_value.getText().toString()) >= limitValue)
                        btn_buy.setEnabled(true);
                }
            }
        };

        new TradeEngineImpl().getMyBankCards(listener.setLoadingDialog(mContext));
    }

    private Dialog gpvDialog;
    private TextView tvTradePwdWrong;
    private GridPasswordView gpv;
    private String password;
    private int count =2;
    private void showTradePwdDialog(){
        LayoutInflater inflater = LayoutInflater.from(this);
        View view = (View) inflater.inflate(R.layout.layout_trade_password_dialog, null);
        tvTradePwdWrong = (TextView) view.findViewById(R.id.tv_trade_pwd_wrong);
        gpv = (GridPasswordView)view.findViewById(R.id.gpv_trade_password);
        gpv.setOnPasswordChangedListener(new GridPasswordView.OnPasswordChangedListener() {
            @Override
            public void onChanged(String psw) {

            }

            @Override
            public void onMaxLength(String psw) {
//                if(count != 0){
//                    gpv.clearPassword();
//                    tvTradePwdWrong.setVisibility(View.VISIBLE);
//                    tvTradePwdWrong.setText(String.format(getResources().getString(R.string.blank_trade_pwd_wrong), count));
//                    count--;
//                }else{
//                    showPwdLockedDialog();
//                    gpvDialog.dismiss();
//                }
                password = gpv.getPassWord();
                gpvDialog.dismiss();
                //TODO 请求购买基金
                ParseHttpListener<Boolean> listener = new ParseHttpListener<Boolean>() {
                    @Override
                    protected Boolean parseDateTask(String jsonData) {
                        Boolean b = null;
                        try{
                            JSONObject json = new JSONObject(jsonData);
                            b = json.getBoolean("status");
                        }catch (Exception e){

                        }
                        return b;
                    }

                    @Override
                    protected void afterParseData(Boolean object) {
                        PromptManager.showToast(object? "购买成功":"购买失败");
                    }
                };
                new TradeEngineImpl().buyFund(mFund.getId(), card.getId(), et_value.getText().toString(), password, listener.setLoadingDialog(mContext));
            }
        });
        gpvDialog = new Dialog(this,R.style.dialog);
        gpvDialog.show();
        gpvDialog.getWindow().setContentView(view);
        gpvDialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
    }
    private Dialog pwdLockedDialog;
    private void showPwdLockedDialog(){
        LayoutInflater inflater = LayoutInflater.from(this);
        View view = (View) inflater.inflate(R.layout.layout_trade_password_locked_dialog, null);
        view.findViewById(R.id.btn_retry).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO 重试
                pwdLockedDialog.dismiss();
            }
        });
        view.findViewById(R.id.btn_forget_pwd).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO 忘记密码
                pwdLockedDialog.dismiss();
            }
        });
        pwdLockedDialog = new Dialog(this,R.style.dialog);
        pwdLockedDialog.show();
        pwdLockedDialog.getWindow().setContentView(view);
    }
    private Dialog bankCardDialog;
    private void showSelectBankCardDialog(){
        LayoutInflater inflater = LayoutInflater.from(this);
        View view = (View) inflater.inflate(R.layout.layout_select_bank_card_dialog, null);
        final ListView lvSelectBankCard = (ListView)view.findViewById(R.id.lv_select_bank_card);
        lvSelectBankCard.setOnScrollListener(new PauseOnScrollListener(mBitmapUtils, false, true));
        lvSelectBankCard.setAdapter(new BankCardAdapter());
        lvSelectBankCard.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                int type = lvSelectBankCard.getAdapter().getItemViewType(position);
                if(((BankCardAdapter)lvSelectBankCard.getAdapter()).TYPE_ADD_BANK_CARD == type){
                    startActivity(new Intent(mContext, BankCardNoActivity.class));
                }else{
                    Bank bank = myCards.get(position).getBank();
                    card = myCards.get(position);
                    mBitmapUtils.display(iv_bank_logo, bank.getLogo(), null, callBack);
                    tv_limit_value.setText(String.format(getResources().getString(R.string.blank_limit_value),bank.getSingle_limit(),bank.getSingle_day_limit()));
                    tv_bank_card_no_tail.setText(bank.getName()+"(" +myCards.get(position).getBank().getBank_card_no_tail()+")");
                    isBankcardChoosed = true;
                    if(!TextUtils.isEmpty(et_value.getText()) && Double.parseDouble(et_value.getText().toString()) >= limitValue)
                        btn_buy.setEnabled(true);
                }
            }
        });
        bankCardDialog = new Dialog(this,R.style.ActionSheetDialogStyle);
        bankCardDialog.show();
        bankCardDialog.getWindow().setContentView(view);
        DisplayMetrics metric = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metric);
        Window dialogWindow = bankCardDialog.getWindow();
        dialogWindow.setGravity(Gravity.LEFT | Gravity.BOTTOM);

        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
        lp.x = 0;
        lp.y = 0;
        lp.width = metric.widthPixels;
        lp.height = (metric.heightPixels)/2;
        dialogWindow.setAttributes(lp);
    }

    /**
     * 获得状态栏的高度
     *
     * @return
     */
    private int getStatusHeight() {

        int statusHeight = -1;
        try {

            Class clazz = Class.forName("com.android.internal.R$dimen");
            Object object = clazz.newInstance();
            int height = Integer.parseInt(clazz.getField("status_bar_height")
                    .get(object).toString());
            statusHeight = getResources().getDimensionPixelSize(height);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return statusHeight;
    }

    private class BankCardAdapter extends BaseAdapter{
        public int TYPE_BANK_CARD = 0;
        public int TYPE_ADD_BANK_CARD = 1;

        @Override
        public int getViewTypeCount() {
            return 2;
        }

        @Override
        public int getItemViewType(int position) {
            if(myCards != null && myCards.size() > 0){
                if(position == myCards.size()){
                    return TYPE_ADD_BANK_CARD;
                }else{
                    return  TYPE_BANK_CARD;
                }
            }else{
                return  TYPE_BANK_CARD;
            }
        }

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
            int type = getItemViewType(position);
            ViewHolder holder;
            if(convertView == null){
                if(type == TYPE_BANK_CARD){
                    convertView = View.inflate(mContext, R.layout.item_layout_select_bank_card1, null);
                    holder = new ViewHolder();
                    holder.tv_bank_card_no_tail = (TextView) convertView.findViewById(R.id.tv_bank_card_no_tail);
                    holder.tv_limit_value = (TextView) convertView.findViewById(R.id.tv_limit_value);
                    holder.iv_bank_logo = (ImageView) convertView.findViewById(R.id.iv_bank_logo);
                    convertView.setTag(holder);
                }else{
                    TextView tv = new TextView(mContext);
                    Resources res = getResources();
                    tv.setBackgroundResource(R.drawable.btn_white_selector);
                    tv.setTextColor(res.getColor(R.color.black));
                    tv.setTextSize(res.getDimension(R.dimen.widget_text_8sp));
                    tv.setText(R.string.add_bank_card);
                    tv.setGravity(Gravity.CENTER);
                    AbsListView.LayoutParams params = new AbsListView.LayoutParams(AbsListView.LayoutParams.MATCH_PARENT,AbsListView.LayoutParams.WRAP_CONTENT);
                    try{
                        tv.setPadding(0, res.getDimensionPixelOffset(R.dimen.widget_padding_medium), 0 , res.getDimensionPixelOffset(R.dimen.widget_padding_medium));
                    }catch (Exception e){

                    }
                    tv.setLayoutParams(params );
                    convertView = tv;
                }
            }
            if(type == TYPE_BANK_CARD){
                holder = (ViewHolder) convertView.getTag();
                Bank bank = myCards.get(position).getBank();
                mBitmapUtils.display(holder.iv_bank_logo, bank.getLogo(), null, callBack);

                holder.tv_limit_value.setText(String.format(getResources().getString(R.string.blank_limit_value),bank.getSingle_limit(),bank.getSingle_day_limit()));
                holder.tv_bank_card_no_tail.setText(bank.getName()+"(" +bank.getBank_card_no_tail()+")");
            }
            return convertView;
        }
        private class ViewHolder{
            TextView tv_bank_card_no_tail;
            TextView tv_limit_value;
            ImageView iv_bank_logo;
        }
    }

    private MyBitmapLoadCallBack callBack = new MyBitmapLoadCallBack();

    private class MyBitmapLoadCallBack <T extends View>extends DefaultBitmapLoadCallBack<T> {

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

}