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
import com.dkhs.portfolio.bean.Fund;
import com.dkhs.portfolio.bean.FundShare;
import com.dkhs.portfolio.bean.MyFundInfo;
import com.dkhs.portfolio.engine.TradeEngineImpl;
import com.dkhs.portfolio.net.ParseHttpListener;
import com.dkhs.portfolio.utils.PromptManager;
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
    private Fund mFund;
    private List<FundShare> shareLists;
    private FundShare share;
    private double limitValue;
    public static Intent sellIntent(Context context, MyFundInfo fundInfo){
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
        if(extras != null)
            handleExtras(extras);
        setTitle(R.string.sell_out);
        initViews();
        initData();
    }

    private void handleExtras(Bundle extras) {
        mFundInfo = (MyFundInfo) extras.getSerializable(FUND_INFO);
        shareLists = mFundInfo.getShares_list();
        mFund = mFundInfo.getFund();
    }

    private void initViews() {
        et_shares.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                btn_sell.setEnabled(!TextUtils.isEmpty(s));
                if(!TextUtils.isEmpty(s)){
                    double value = Double.parseDouble(s.toString());
                    if(value < limitValue){
                        btn_sell.setEnabled(false);
                    }else{
                        value = value*Double.parseDouble(mFund.getNet_value())*mFund.getFare_ratio_sell()*0.01*mFund.getDiscount_rate_sell()*0.01;
                        BigDecimal decimal = new BigDecimal(value);
                        value = decimal.setScale(2,   RoundingMode.HALF_UP).doubleValue();
                        tv_sell_poundage.setText(String.format(getResources().getString(R.string.blank_sell_fund_tip2), String.valueOf(value)));
                        btn_sell.setEnabled(true);
                    }
                }
            }
        });
        btn_sell.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showTradePwdDialog();
            }
        });
    }
    private void initData() {
        mFund = mFundInfo.getFund();
        limitValue = Double.parseDouble(mFund.getShares_min());
        tv_fund_name.setText(String.format(getResources().getString(R.string.blank_fund_name), mFund.getName(),mFund.getId()));
        tv_hold_shares.setText(String.format(getResources().getString(R.string.blank_limit_hold_shares), mFund.getShares_min()));
        tv_sell_poundage.setText(String.format(getResources().getString(R.string.blank_sell_fund_tip1), mFund.getFare_ratio_buy()+"%"));

        share = shareLists.get(0);
        mBitmapUtils = new BitmapUtils(this);
        tv_bank_card_no_tail.setText(share.getBank().getName() + "("+share.getBank().getBank_card_no_tail()+")");
        tv_available_shares.setText(String.format(getResources().getString(R.string.blank_available_sell_shares), share.getShares_enable()));
        mBitmapUtils.display(iv_bank_logo, share.getBank().getLogo(), null, callBack);

    }

    @OnClick(R.id.rl_select_bank)
    private void onClick(View view){
        showSelectBankCardDialog();
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
                        PromptManager.showToast(object ? "购买成功" : "购买失败");
                    }
                };
                new TradeEngineImpl().sellFund(mFund.getId(), share.getBank().getId(), et_shares.getText().toString(), password, listener.setLoadingDialog(mContext));

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
        ListView lvSelectBankCard = (ListView)view.findViewById(R.id.lv_select_bank_card);
        lvSelectBankCard.setAdapter(new BankCardAdapter());
        lvSelectBankCard.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Bank bank = shareLists.get(position).getBank();
                share = shareLists.get(position);
                mBitmapUtils.display(iv_bank_logo, bank.getLogo(), null, callBack);
                tv_available_shares.setText(String.format(getResources().getString(R.string.blank_available_sell_shares), share.getShares_enable()));
                tv_bank_card_no_tail.setText(bank.getName()+"(" +bank.getBank_card_no_tail()+")");
                if(!TextUtils.isEmpty(et_shares.getText()) && Double.parseDouble(et_shares.getText().toString()) >= limitValue)
                    btn_sell.setEnabled(true);
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

    private class BankCardAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return 4;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            int type = getItemViewType(position);
            if(convertView == null){
                convertView = View.inflate(mContext, R.layout.item_layout_select_bank_card1, null);
            }
            return convertView;
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