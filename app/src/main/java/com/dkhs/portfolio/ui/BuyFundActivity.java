package com.dkhs.portfolio.ui;

import android.app.Dialog;
import android.content.res.Resources;
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
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.TextView;

import com.dkhs.portfolio.R;
import com.dkhs.portfolio.base.widget.Button;
import com.dkhs.portfolio.base.widget.ListView;
import com.jungly.gridpasswordview.GridPasswordView;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;

/**
 * Created by zhangcm on 2015/9/23.15:56
 */
public class BuyFundActivity extends ModelAcitivity {

    @ViewInject(R.id.et_value)
    private EditText etValue;

    @ViewInject(R.id.btn_buy)
    private Button btnBuy;

    @Override
    protected void onCreate(Bundle arg0) {
        super.onCreate(arg0);
        setContentView(R.layout.activity_buy_fund);
        ViewUtils.inject(this);
        setTitle(R.string.buy_in);
        initViews();
    }

    @OnClick(R.id.rl_select_bank)
    private void onClick(View view){
        showSelectBankCardDialog();
    }

    private void initViews() {
        etValue.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }
            @Override
            public void afterTextChanged(Editable s) {
                btnBuy.setEnabled(!TextUtils.isEmpty(s));
            }
        });
        btnBuy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showTradePwdDialog();
            }
        });
    }

    private Dialog gpvDialog;
    private TextView tvTradePwdWrong;
    private GridPasswordView gpv;
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
                if(count != 0){
                    gpv.clearPassword();
                    tvTradePwdWrong.setVisibility(View.VISIBLE);
                    tvTradePwdWrong.setText(String.format(getResources().getString(R.string.blank_trade_pwd_wrong), count));
                    count--;
                }else{
                    showPwdLockedDialog();
                    gpvDialog.dismiss();
                }
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
        private int TYPE_BANK_CARD = 0;
        private int TYPE_ADD_BANK_CARD = 1;

        @Override
        public int getViewTypeCount() {
            return 2;
        }

        @Override
        public int getItemViewType(int position) {
            if(position == 3){
                return TYPE_ADD_BANK_CARD;
            }else{
                return  TYPE_BANK_CARD;
            }

        }

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
                if(type == TYPE_BANK_CARD){
                    convertView = View.inflate(mContext, R.layout.item_layout_select_bank_card1, null);
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
            return convertView;
        }
    }

}
