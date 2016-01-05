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
import android.text.InputFilter;
import android.text.Spanned;
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
import com.dkhs.portfolio.bean.FundQuoteBean;
import com.dkhs.portfolio.bean.FundTradeInfo;
import com.dkhs.portfolio.bean.MyBankCard;
import com.dkhs.portfolio.engine.TradeEngineImpl;
import com.dkhs.portfolio.net.DataParse;
import com.dkhs.portfolio.net.ErrorBundle;
import com.dkhs.portfolio.net.ParseHttpListener;
import com.dkhs.portfolio.net.StringDecodeUtil;
import com.dkhs.portfolio.ui.widget.MyAlertDialog;
import com.dkhs.portfolio.utils.PromptManager;
import com.dkhs.portfolio.utils.StringFromatUtils;
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
import org.parceler.Parcels;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
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
    @ViewInject(R.id.tv_add_bank_card)
    private TextView tv_add_bank_card;

    private boolean isBankcardChoosed;

    private static String FUND_INFO = "fund_info";
    private FundQuoteBean mQuoteBean;
    private MyBankCard card;
    private String curSelectCardId;
    private double limitValue;
    private double maxValue;

    public static Intent buyIntent(Context context, FundQuoteBean fundInfo) {
        Intent intent = new Intent(context, BuyFundActivity.class);
        intent.putExtra(FUND_INFO, Parcels.wrap(fundInfo));
        return intent;
    }

    @Override
    protected void onCreate(Bundle arg0) {
        super.onCreate(arg0);
        setContentView(R.layout.activity_buy_fund);
        ViewUtils.inject(this);
        Bundle extras = getIntent().getExtras();
        if (extras != null)
            handleExtras(extras);
        setTitle(R.string.buy_in);
        initViews();
        initData();
    }

    private void handleExtras(Bundle extras) {
        mQuoteBean = Parcels.unwrap(extras.getParcelable(FUND_INFO));
    }

    @OnClick(R.id.rl_select_bank)
    private void onClick(View view) {
        if (myCards == null || myCards.size() == 0) {
            startActivity(new Intent(mContext, BankCardNoActivity.class));
        } else {
            showSelectBankCardDialog();
        }
    }

    private void initViews() {
        et_value.addTextChangedListener(new TextWatcher() {
            String beforsStr;
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                beforsStr = s.toString();
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.toString().contains(".")) {
                    if(s.toString().substring(0,s.toString().indexOf(".")).length() > 8){
                        et_value.setText(beforsStr);
                        et_value.setSelection(start);
                        return;
                    }
                    if (s.length() - 1 - s.toString().indexOf(".") > 2) {
                        s = s.toString().subSequence(0,
                                s.toString().indexOf(".") + 3);
                        et_value.setText(s);
                        et_value.setSelection(s.length());
                    }
                }else{
                    if(s.toString().length() > 8){
                        et_value.setText(beforsStr);
                        et_value.setSelection(start);
                        return;
                    }
                }
                if (s.toString().trim().substring(0).equals(".")) {
                    s = "0" + s;
                    et_value.setText(s);
                    et_value.setSelection(2);
                    return;

                }

                if (s.toString().startsWith("0")
                        && s.toString().trim().length() > 1) {
                    if (!s.toString().substring(1, 2).equals(".")) {
                        et_value.setText(s.subSequence(0, 1));
                        et_value.setSelection(1);
                        return;
                    }
                }
                if (tv_add_bank_card.getVisibility() == View.VISIBLE) {
                    btn_buy.setEnabled(false);
                } else {
                    if (!TextUtils.isEmpty(s) && !s.toString().startsWith(".")) {
                        double value = Double.parseDouble(s.toString());
                        if (value < limitValue) {
                            btn_buy.setEnabled(false);
                            tv_buy_poundage.setText(String.format(getResources().getString(R.string.blank_buy_fund_tip1), StringFromatUtils.get2PointPercent(mQuoteBean.getFare_ratio_buy()*mQuoteBean.getDiscount_rate_buy())));
                        } else {
                            value = value * mQuoteBean.getFare_ratio_buy() * 0.01 * mQuoteBean.getDiscount_rate_buy();
                            BigDecimal decimal = new BigDecimal(value);
                            value = decimal.setScale(2, RoundingMode.HALF_UP).doubleValue();
                            tv_buy_poundage.setText(String.format(getResources().getString(R.string.blank_buy_fund_tip2), new DecimalFormat("0.00").format(value)));
                            btn_buy.setEnabled(isBankcardChoosed);
                        }
                    }else{
                        tv_buy_poundage.setText(String.format(getResources().getString(R.string.blank_buy_fund_tip1), StringFromatUtils.get2PointPercent(mQuoteBean.getFare_ratio_buy() * mQuoteBean.getDiscount_rate_buy())));
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
        et_value.setFilters(new InputFilter[]{lengthfilter});
        btn_buy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showTradePwdDialog();
            }
        });
    }
    private static final int DECIMAL_DIGITS = 2;
    InputFilter lengthfilter = new InputFilter() {
        public CharSequence filter(CharSequence source, int start, int end,
                                   Spanned dest, int dstart, int dend) {
            // 删除等特殊字符，直接返回
            if ("".equals(source.toString())) {
                return null;
            }
            String dValue = dest.toString();
            String[] splitArray = dValue.split("\\.");
            if (splitArray.length > 1) {
                String dotValue = splitArray[1];
                int diff = dotValue.length() + 1 - DECIMAL_DIGITS;
                if (diff > 0) {
                    return source.subSequence(start, end - diff);
                }
            }
            return null;
        }
    };

    private void initData() {
        limitValue = mQuoteBean.getAmount_min_buy();
        maxValue = mQuoteBean.getAmount_max_buy();
        tv_fund_name.setText(String.format(getResources().getString(R.string.blank_fund_name), mQuoteBean.getAbbrName(), mQuoteBean.getSymbol()));
        tv_net_value.setText(String.format(getResources().getString(R.string.blank_net_value), mQuoteBean.getNet_value()));
        tv_buy_value.setText(String.format(getResources().getString(R.string.blank_buy_value), mQuoteBean.getAmount_min_buy()));
        tv_buy_poundage.setText(String.format(getResources().getString(R.string.blank_buy_fund_tip1), StringFromatUtils.get2PointPercent(mQuoteBean.getFare_ratio_buy()*mQuoteBean.getDiscount_rate_buy())));
        et_value.setHint(String.format(getResources().getString(R.string.blank_hint_value), String.valueOf(mQuoteBean.getAmount_min_buy())));
        mBitmapUtils = new BitmapUtils(this);
    }

    private BitmapUtils mBitmapUtils;
    private List<MyBankCard> myCards = new ArrayList<MyBankCard>();
    private TradeEngineImpl tradeEngine = new TradeEngineImpl();
    ParseHttpListener<List<MyBankCard>> myCardsListener = new ParseHttpListener<List<MyBankCard>>() {
        @Override
        protected List<MyBankCard> parseDateTask(String jsonData) {
            List<MyBankCard> myCards = null;
            if (!TextUtils.isEmpty(jsonData)) {
                try {
                    jsonData = StringDecodeUtil.decodeUnicode(jsonData);
                    Gson gson = new GsonBuilder().enableComplexMapKeySerialization().create();
                    myCards = gson.fromJson(jsonData, new TypeToken<List<MyBankCard>>() {
                    }.getType());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            return myCards;
        }

        @Override
        protected void afterParseData(List<MyBankCard> cards) {
            if (cards != null && cards.size() > 0) {
                tv_add_bank_card.setVisibility(View.GONE);
                myCards = cards;
                card = myCards.get(0);
                curSelectCardId = card.getId();
                Bank bank = myCards.get(0).getBank();
                mBitmapUtils.display(iv_bank_logo, bank.getLogo(), null, callBack);
                tv_limit_value.setText(String.format(getResources().getString(R.string.blank_limit_value), bank.getSingle_limit(), bank.getSingle_day_limit()));
                tv_bank_card_no_tail.setText(String.format(getResources().getString(R.string.blank_bank), bank.getName(), myCards.get(0).getBank_card_no_tail()));
                isBankcardChoosed = true;
                if (!TextUtils.isEmpty(et_value.getText()) && Double.parseDouble(et_value.getText().toString()) >= limitValue)
                    btn_buy.setEnabled(true);
                tradeEngine.isTradePasswordSet(isTradePwdSetListener);

            } else {
                PromptManager.closeProgressDialog();
                tv_add_bank_card.setVisibility(View.VISIBLE);
            }
        }
    };
    ParseHttpListener<Boolean> isTradePwdSetListener = new ParseHttpListener<Boolean>() {
        @Override
        protected Boolean parseDateTask(String jsonData) {
            try {
                JSONObject json = new JSONObject(jsonData);
                if (json.has("status")) {
                    return json.getBoolean("status");
                }

            } catch (Exception e) {
            }
            return null;
        }

        @Override
        protected void afterParseData(Boolean object) {
            PromptManager.closeProgressDialog();
            if (null != object) {
                if (!object) {
                    showSetTradePwdDialog();
                }
            }
        }
    };

    @Override
    protected void onResume() {
        super.onResume();
        PromptManager.showProgressDialog(mContext, "");
        tradeEngine.getMyBankCards(myCardsListener);
    }

    private Dialog gpvDialog;
    private TextView tvTradePwdWrong;
    private View progressBar;
    private GridPasswordView gpv;
    private String password;
    private int count = 2;

    private void showSetTradePwdDialog() {
        new MyAlertDialog(this).builder()
                .setMsg(getResources().getString(R.string.first_trade_password_msg))
                .setPositiveButton(getResources().getString(R.string.set_right_now), new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        startActivity(ForgetTradePasswordActivity.newIntent(mContext,true));
                    }
                })
                .setNegativeButton(getResources().getString(R.string.cancel), new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        manualFinish();
                    }
                }).show();
    }

    private void showTradePwdDialog() {
        LayoutInflater inflater = LayoutInflater.from(this);
        View view = inflater.inflate(R.layout.layout_trade_password_dialog, null);
        view.findViewById(R.id.iv_close).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gpvDialog.dismiss();
            }
        });
        view.findViewById(R.id.tv_forget_pwd).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(mContext, ForgetTradePasswordActivity.class));
                gpvDialog.dismiss();
            }
        });
        tvTradePwdWrong = (TextView) view.findViewById(R.id.tv_trade_pwd_wrong);
        progressBar =  view.findViewById(R.id.progressBar);
        progressBar.setVisibility(View.GONE);
        gpv = (GridPasswordView) view.findViewById(R.id.gpv_trade_password);
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
                //TODO 请求购买基金
                ParseHttpListener<FundTradeInfo> listener = new ParseHttpListener<FundTradeInfo>() {
                    @Override
                    public void onFailure(ErrorBundle errorBundle) {
                        gpv.clearPassword();
                        if(errorBundle.getErrorKey().equals("password_lock_invalid")){
                            //TODO 密码已被锁定
                            gpvDialog.dismiss();
                            showPwdLockedDialog(errorBundle.getErrorMessage());
                        }else{
                            tvTradePwdWrong.setText(errorBundle.getErrorMessage());
                            progressBar.setVisibility(View.GONE);
                            tvTradePwdWrong.setVisibility(View.VISIBLE);
                        }
                    }


                    @Override
                    protected FundTradeInfo parseDateTask(String jsonData) {
                        FundTradeInfo info = null;
                        try {
                            jsonData = StringDecodeUtil.decodeUnicode(jsonData);
                            info = DataParse.parseObjectJson(FundTradeInfo.class, jsonData);
                        } catch (Exception e) {

                        }
                        return info;
                    }

                    @Override
                    protected void afterParseData(FundTradeInfo object) {
                        //TODO 请求买入基金
                        if (object != null && !"0".equals(object.getId())) {
                            gpvDialog.dismiss();
                            PromptManager.showToast(R.string.buy_fund_suc);
                            startActivity(BuyFundInfoActivity.getFundInfoIntent(mContext, object.getId()));
                            finish();
                        } else {
                            PromptManager.showToast(R.string.buy_fund_fail);
                        }
                    }
                };
                progressBar.setVisibility(View.VISIBLE);
                new TradeEngineImpl().buyFund(mQuoteBean.getId(), card.getId(), et_value.getText().toString(), password, listener);
            }
        });
        gpvDialog = new Dialog(this, R.style.dialog);
        gpvDialog.show();
        gpvDialog.getWindow().setContentView(view);
        gpvDialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
    }

    private void showPwdLockedDialog(String msg) {
        new MyAlertDialog(this).builder()
                .setCancelable(false)
                .setMsg(msg)
                .setPositiveButton(getResources().getString(R.string.forget_password), new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        startActivity(ForgetTradePasswordActivity.newIntent(mContext, false));
                    }
                })
                .setNegativeButton(getResources().getString(R.string.cancel), new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                    }
                }).show();
    }

    private Dialog bankCardDialog;

    private void showSelectBankCardDialog() {
        LayoutInflater inflater = LayoutInflater.from(this);
        View view = inflater.inflate(R.layout.layout_select_bank_card_dialog, null);
        final ListView lvSelectBankCard = (ListView) view.findViewById(R.id.lv_select_bank_card);
        lvSelectBankCard.setOnScrollListener(new PauseOnScrollListener(mBitmapUtils, false, true));
        lvSelectBankCard.setAdapter(new BankCardAdapter());
        lvSelectBankCard.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                int type = lvSelectBankCard.getAdapter().getItemViewType(position);
                if (((BankCardAdapter) lvSelectBankCard.getAdapter()).TYPE_ADD_BANK_CARD == type) {
                    startActivity(new Intent(mContext, BankCardNoActivity.class));
                } else {
                    Bank bank = myCards.get(position).getBank();
                    card = myCards.get(position);
                    curSelectCardId = card.getId();
                    mBitmapUtils.display(iv_bank_logo, bank.getLogo(), null, callBack);
                    tv_limit_value.setText(String.format(getResources().getString(R.string.blank_limit_value), bank.getSingle_limit(), bank.getSingle_day_limit()));
                    tv_bank_card_no_tail.setText(String.format(getResources().getString(R.string.blank_bank), bank.getName(), myCards.get(position).getBank_card_no_tail()));
                    isBankcardChoosed = true;
                    if (!TextUtils.isEmpty(et_value.getText()) && Double.parseDouble(et_value.getText().toString()) >= limitValue)
                        btn_buy.setEnabled(true);
                }
                bankCardDialog.dismiss();
            }
        });
        bankCardDialog = new Dialog(this, R.style.ActionSheetDialogStyle);
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
        lp.height = (metric.heightPixels) / 2;
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

    private class BankCardAdapter extends BaseAdapter {
        public int TYPE_BANK_CARD = 0;
        public int TYPE_ADD_BANK_CARD = 1;

        @Override
        public int getViewTypeCount() {
            return 2;
        }

        @Override
        public int getItemViewType(int position) {
            if (myCards != null && myCards.size() > 0) {
                if (position == myCards.size()) {
                    return TYPE_ADD_BANK_CARD;
                } else {
                    return TYPE_BANK_CARD;
                }
            } else {
                return TYPE_BANK_CARD;
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
            if (convertView == null) {
                if (type == TYPE_BANK_CARD) {
                    convertView = View.inflate(mContext, R.layout.item_layout_select_buy_fund_bank_card, null);
                    holder = new ViewHolder();
                    holder.tv_bank_card_no_tail = (TextView) convertView.findViewById(R.id.tv_bank_card_no_tail);
                    holder.tv_limit_value = (TextView) convertView.findViewById(R.id.tv_limit_value);
                    holder.iv_bank_logo = (ImageView) convertView.findViewById(R.id.iv_bank_logo);
                    holder.iv_selected = (ImageView) convertView.findViewById(R.id.iv_selected);
                    convertView.setTag(holder);
                } else {
                    TextView tv = new TextView(mContext);
                    Resources res = getResources();
                    tv.setBackgroundResource(R.drawable.btn_white_selector);
                    tv.setTextColor(res.getColor(R.color.black));
                    tv.setTextSize(res.getDimension(R.dimen.widget_text_10sp)/2);
                    tv.setText(R.string.add_bank_card);
                    tv.setGravity(Gravity.CENTER);
                    AbsListView.LayoutParams params = new AbsListView.LayoutParams(AbsListView.LayoutParams.MATCH_PARENT, AbsListView.LayoutParams.WRAP_CONTENT);
                    try {
                        tv.setPadding(0, res.getDimensionPixelOffset(R.dimen.widget_padding_medium), 0, res.getDimensionPixelOffset(R.dimen.widget_padding_medium));
                    } catch (Exception e) {

                    }
                    tv.setLayoutParams(params);
                    convertView = tv;
                }
            }
            if (type == TYPE_BANK_CARD) {
                holder = (ViewHolder) convertView.getTag();
                Bank bank = myCards.get(position).getBank();
                mBitmapUtils.display(holder.iv_bank_logo, bank.getLogo(), null, callBack);
                holder.tv_limit_value.setText(String.format(getResources().getString(R.string.blank_limit_value), bank.getSingle_limit(), bank.getSingle_day_limit()));
                holder.tv_bank_card_no_tail.setText(String.format(getResources().getString(R.string.blank_bank), bank.getName(), myCards.get(position).getBank_card_no_tail()));
                if (myCards.get(position).getId().equals(curSelectCardId)) {
                    holder.iv_selected.setVisibility(View.VISIBLE);
                } else {
                    holder.iv_selected.setVisibility(View.GONE);
                }
            }
            return convertView;
        }

        private class ViewHolder {
            TextView tv_bank_card_no_tail;
            TextView tv_limit_value;
            ImageView iv_bank_logo;
            ImageView iv_selected;
        }
    }

    private MyBitmapLoadCallBack callBack = new MyBitmapLoadCallBack();

    private class MyBitmapLoadCallBack<T extends View> extends DefaultBitmapLoadCallBack<T> {

        @Override
        public void onLoadCompleted(T container, String uri, Bitmap bitmap, BitmapDisplayConfig config, BitmapLoadFrom from) {
            super.onLoadCompleted(container, uri, bitmap, config, from);
            setBackGroundWhite(container, bitmap);
        }

        public void setBackGroundWhite(T container, Bitmap bitmap) {
            int roundPixels;
            int width = bitmap.getWidth();
            int height = bitmap.getHeight();
            if (width <= height) {
                roundPixels = width / 2;
            } else {
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
            canvas.drawCircle(width / 2, height / 2, roundPixels, paint);
            container.setBackgroundDrawable(new BitmapDrawable(roundConcerImage));
        }

    }

}
