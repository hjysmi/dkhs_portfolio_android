package com.dkhs.portfolio.ui;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
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
import com.dkhs.portfolio.bean.FundShare;
import com.dkhs.portfolio.bean.FundTradeInfo;
import com.dkhs.portfolio.bean.MyFundInfo;
import com.dkhs.portfolio.engine.TradeEngineImpl;
import com.dkhs.portfolio.net.DataParse;
import com.dkhs.portfolio.net.ErrorBundle;
import com.dkhs.portfolio.net.ParseHttpListener;
import com.dkhs.portfolio.net.StringDecodeUtil;
import com.dkhs.portfolio.ui.widget.MyAlertDialog;
import com.dkhs.portfolio.utils.PromptManager;
import com.dkhs.portfolio.utils.StringFromatUtils;
import com.jungly.gridpasswordview.GridPasswordView;
import com.lidroid.xutils.BitmapUtils;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.bitmap.BitmapDisplayConfig;
import com.lidroid.xutils.bitmap.callback.BitmapLoadFrom;
import com.lidroid.xutils.bitmap.callback.DefaultBitmapLoadCallBack;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;

import org.json.JSONObject;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.List;

/**
 * Created by zhangcm on 2015/9/23.15:56
 */
public class SellFundActivity extends ModelAcitivity {

    @ViewInject(R.id.et_shares)
    private EditText et_shares;

    @ViewInject(R.id.btn_sell)
    private Button btn_sell;

    @ViewInject(R.id.tv_fund_name)
    private TextView tv_fund_name;

    @ViewInject(R.id.tv_hold_shares)
    private TextView tv_hold_shares;


    @ViewInject(R.id.tv_bank_card_no_tail)
    private TextView tv_bank_card_no_tail;

    @ViewInject(R.id.iv_bank_logo)
    private ImageView iv_bank_logo;

    @ViewInject(R.id.tv_available_shares)
    private TextView tv_available_shares;

    @ViewInject(R.id.tv_sell_poundage)
    private TextView tv_sell_poundage;

    private BitmapUtils mBitmapUtils;

    private static String FUND_INFO = "fund_info";
    private MyFundInfo mFundInfo;
    private FundQuoteBean mQuoteBean;
    private List<FundShare> shareLists;
    private FundShare share;
    private String curSelectedBankId;
    private double limitValue;

    public static Intent sellIntent(Context context, MyFundInfo fundInfo) {
        Intent intent = new Intent(context, SellFundActivity.class);
        intent.putExtra(FUND_INFO, fundInfo);
        return intent;
    }

    @Override
    protected void onCreate(Bundle arg0) {
        super.onCreate(arg0);
        setContentView(R.layout.activity_sell_fund);
        ViewUtils.inject(this);
        Bundle extras = getIntent().getExtras();
        if (extras != null)
            handleExtras(extras);
        setTitle(R.string.sell_out);
        initViews();
        initData();
    }

    private void handleExtras(Bundle extras) {
        mFundInfo = (MyFundInfo) extras.getSerializable(FUND_INFO);
        shareLists = mFundInfo.getShares_list();
        mQuoteBean = mFundInfo.getFund();
    }

    private void initViews() {
        et_shares.addTextChangedListener(new TextWatcher() {
            String beforsStr;
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                beforsStr = s.toString();
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.toString().contains(".")) {
                    if(s.toString().substring(0,s.toString().indexOf(".")).length() > 8){
                        et_shares.setText(beforsStr);
                        et_shares.setSelection(start);
                        return;
                    }
                    if (s.length() - 1 - s.toString().indexOf(".") > 2) {
                        s = s.toString().subSequence(0,
                                s.toString().indexOf(".") + 3);
                        et_shares.setText(s);
                        et_shares.setSelection(s.length());
                    }
                }else{
                    if(s.toString().length() > 8){
                        et_shares.setText(beforsStr);
                        et_shares.setSelection(start);
                        return;
                    }
                }
                if (s.toString().trim().substring(0).equals(".")) {
                    s = "0" + s;
                    et_shares.setText(s);
                    et_shares.setSelection(2);
                    return;

                }

                if (s.toString().startsWith("0")
                        && s.toString().trim().length() > 1) {
                    if (!s.toString().substring(1, 2).equals(".")) {
                        et_shares.setText(s.subSequence(0, 1));
                        et_shares.setSelection(1);
                        return;
                    }
                }
                btn_sell.setEnabled(!TextUtils.isEmpty(s));
                if (!TextUtils.isEmpty(s) && !s.toString().startsWith(".")) {
                    double value = Double.parseDouble(s.toString());
                    if (value < limitValue) {
                        btn_sell.setEnabled(false);
                        tv_sell_poundage.setText(String.format(getResources().getString(R.string.blank_sell_fund_tip1), StringFromatUtils.get2PointPercent((float) (mQuoteBean.getFare_ratio_sell() * mQuoteBean.getDiscount_rate_sell()))));

                    } else {
                        value = value * mQuoteBean.getNet_value() * mQuoteBean.getFare_ratio_sell() * 0.01 * mQuoteBean.getDiscount_rate_sell();
                        BigDecimal decimal = new BigDecimal(value);
                        value = decimal.setScale(2, RoundingMode.HALF_UP).doubleValue();
                        tv_sell_poundage.setText(String.format(getResources().getString(R.string.blank_sell_fund_tip2), new DecimalFormat("0.00").format(value)));
                        btn_sell.setEnabled(true);
                    }
                }else{
                    tv_sell_poundage.setText(String.format(getResources().getString(R.string.blank_sell_fund_tip1), StringFromatUtils.get2PointPercent((float) (mQuoteBean.getFare_ratio_sell() * mQuoteBean.getDiscount_rate_sell()))));
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
        et_shares.setFilters(new InputFilter[]{lengthfilter});
        btn_sell.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(Double.parseDouble(share.getShares_enable()) < Double.parseDouble(et_shares.getText().toString().trim())){
                    PromptManager.showToast(R.string.sell_fund_share_error);
                    return;
                }
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
        mQuoteBean = mFundInfo.getFund();
        limitValue = Double.parseDouble(mQuoteBean.getShares_min_sell());
        tv_fund_name.setText(String.format(getResources().getString(R.string.blank_fund_name), mQuoteBean.getAbbrName(), mQuoteBean.getSymbol()));
        tv_hold_shares.setText(String.format(getResources().getString(R.string.blank_limit_hold_shares), mQuoteBean.getShares_min_sell()));
        tv_sell_poundage.setText(String.format(getResources().getString(R.string.blank_sell_fund_tip1), StringFromatUtils.get2PointPercent((float) (mQuoteBean.getFare_ratio_sell() * mQuoteBean.getDiscount_rate_sell()))));

        share = shareLists.get(0);
        curSelectedBankId = share.getBank().getId();
        mBitmapUtils = new BitmapUtils(this);
        tv_bank_card_no_tail.setText(String.format(getResources().getString(R.string.blank_bank), share.getBank().getName(), share.getBank().getBank_card_no_tail()));
        tv_available_shares.setText(String.format(getResources().getString(R.string.blank_available_sell_shares), share.getShares_enable()));
        mBitmapUtils.display(iv_bank_logo, share.getBank().getLogo(), null, callBack);

    }

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
        new TradeEngineImpl().isTradePasswordSet(isTradePwdSetListener.setLoadingDialog(this));
    }

    @OnClick(R.id.rl_select_bank)
    private void onClick(View view) {
        showSelectBankCardDialog();
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
                ParseHttpListener<FundTradeInfo> listener = new ParseHttpListener<FundTradeInfo>() {
                    @Override
                    public void onFailure(ErrorBundle errorBundle) {
                        if(errorBundle.getErrorKey().equals("password_lock_invalid")){
                            //TODO 密码已被锁定
                            gpvDialog.dismiss();
                            showPwdLockedDialog(errorBundle.getErrorMessage());
                        }else{
                            gpv.clearPassword();
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
                        //TODO 请求卖出基金
                        if (object != null && !"0".equals(object.getId())) {
                            gpvDialog.dismiss();
                            PromptManager.showToast(R.string.sell_fund_suc);
                            startActivity(SellFundInfoActivity.getFundInfoIntent(mContext, object.getId(),true));
                            finish();
                        } else {
                            PromptManager.showToast(R.string.sell_fund_fail);
                        }
                    }
                };
                progressBar.setVisibility(View.VISIBLE);
                new TradeEngineImpl().sellFund(mQuoteBean.getId(), share.getBank().getId(), et_shares.getText().toString(), password, listener);

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
        ListView lvSelectBankCard = (ListView) view.findViewById(R.id.lv_select_bank_card);
        lvSelectBankCard.setAdapter(new BankCardAdapter());
        lvSelectBankCard.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Bank bank = shareLists.get(position).getBank();
                share = shareLists.get(position);
                curSelectedBankId = share.getBank().getId();
                mBitmapUtils.display(iv_bank_logo, bank.getLogo(), null, callBack);
                tv_available_shares.setText(String.format(getResources().getString(R.string.blank_available_sell_shares), share.getShares_enable()));
                tv_bank_card_no_tail.setText(String.format(getResources().getString(R.string.blank_bank), bank.getName(), bank.getBank_card_no_tail()));

                if (!TextUtils.isEmpty(et_shares.getText()) && Double.parseDouble(et_shares.getText().toString()) >= limitValue)
                    btn_sell.setEnabled(true);
                share = shareLists.get(0);
                curSelectedBankId = share.getBank().getId();
                tv_bank_card_no_tail.setText(String.format(getResources().getString(R.string.blank_bank), bank.getName(), bank.getBank_card_no_tail()));

                tv_available_shares.setText(String.format(getResources().getString(R.string.blank_available_sell_shares), share.getShares_enable()));
                mBitmapUtils.display(iv_bank_logo, share.getBank().getLogo(), null, callBack);
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

        @Override
        public int getCount() {
            return shareLists.size();
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
            if (convertView == null) {
                convertView = View.inflate(mContext, R.layout.item_layout_select_sell_fund_bank_card, null);
                holder = new ViewHolder();
                holder.tv_bank_card_no_tail = (TextView) convertView.findViewById(R.id.tv_bank_card_no_tail);
                holder.tv_available_shares = (TextView) convertView.findViewById(R.id.tv_available_shares);
                holder.iv_bank_logo = (ImageView) convertView.findViewById(R.id.iv_bank_logo);
                holder.iv_selected = (ImageView) convertView.findViewById(R.id.iv_selected);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            share = shareLists.get(position);
            holder.tv_bank_card_no_tail.setText(String.format(getResources().getString(R.string.blank_bank), share.getBank().getName(), share.getBank().getBank_card_no_tail()));
            holder.tv_available_shares.setText(String.format(getResources().getString(R.string.blank_available_sell_shares), share.getShares_enable()));
            mBitmapUtils.display(holder.iv_bank_logo, share.getBank().getLogo(), null, callBack);
            if (share.getBank().getId().equals(curSelectedBankId)) {
                //显示选中箭头
                holder.iv_selected.setVisibility(View.VISIBLE);
            } else {
                holder.iv_selected.setVisibility(View.GONE);
            }
            return convertView;
        }

        private class ViewHolder {
            TextView tv_bank_card_no_tail;
            TextView tv_available_shares;
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
    @Override
    public int getPageStatisticsStringId() {
        return R.string.statistics_sell_fund;
    }
}
