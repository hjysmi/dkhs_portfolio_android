/**
 * @Title StockRemindActivity.java
 * @Package com.dkhs.portfolio.ui
 * @Description TODO(用一句话描述该文件做什么)
 * @author zjz
 * @date 2015-4-13 下午2:07:36
 * @version V1.0
 */
package com.dkhs.portfolio.ui;

import java.math.BigDecimal;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.Html;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.ContextThemeWrapper;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.dkhs.portfolio.R;
import com.dkhs.portfolio.bean.AlertSetBean;
import com.dkhs.portfolio.bean.CombinationBean;
import com.dkhs.portfolio.bean.MoreDataBean;
import com.dkhs.portfolio.bean.PortfolioAlertBean;
import com.dkhs.portfolio.bean.SelectStockBean;
import com.dkhs.portfolio.engine.MyCombinationEngineImpl;
import com.dkhs.portfolio.engine.OptionalStockEngineImpl;
import com.dkhs.portfolio.engine.QuetosStockEngineImple;
import com.dkhs.portfolio.engine.QuotesEngineImpl;
import com.dkhs.portfolio.engine.LoadMoreDataEngine.ILoadDataBackListener;
import com.dkhs.portfolio.net.ParseHttpListener;
import com.dkhs.portfolio.ui.fragment.FragmentSelectStockFund;
import com.dkhs.portfolio.ui.fragment.FragmentSelectStockFund.StockViewType;
import com.dkhs.portfolio.utils.PromptManager;
import com.dkhs.portfolio.utils.StockUitls;
import com.dkhs.portfolio.utils.StringFromatUtils;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.lidroid.xutils.view.annotation.event.OnCompoundButtonCheckedChange;
import com.lidroid.xutils.view.annotation.event.OnFocusChange;

/**
 * @ClassName StockRemindActivity
 * @Description TODO(这里用一句话描述这个类的作用)
 * @author zjz
 * @date 2015-4-13 下午2:07:36
 * @version 1.0
 */
public class StockRemindActivity extends ModelAcitivity implements OnClickListener, OnCheckedChangeListener,
        OnFocusChangeListener {

    public static final String ARGUMENT_STOCK = "agrument_stock";
    public static final String ARGUMENT_COMBINATION = "agrument_combination";
    public static final String ARGUMENT_IS_COMBINATION = "agrument_is_combination";
    private Button btnRight;
    private SelectStockBean mStockBean;
    private CombinationBean mComBean;

    private boolean isCombinationSetting = false;

    @ViewInject(R.id.tv_stock_name)
    private TextView tvStockName;
    @ViewInject(R.id.tv_stock_num)
    private TextView tvStockCode;
    @ViewInject(R.id.tv_percent)
    private TextView tvPercent;
    @ViewInject(R.id.tv_price)
    private TextView tvPrice;

    // 涨跌幅
    @ViewInject(R.id.tv_priceup_text)
    private TextView tvUpText;
    @ViewInject(R.id.tv_pricedown_text)
    private TextView tvDownText;

    // 股价涨跌幅提示
    @ViewInject(R.id.tv_priceup_tip)
    private TextView tvUpTip;
    @ViewInject(R.id.tv_pricedown_tip)
    private TextView tvDownTip;
    @ViewInject(R.id.tv_priceup_units)
    private TextView tvUpUnit;
    @ViewInject(R.id.tv_pricedown_units)
    private TextView tvDownUnit;
    @ViewInject(R.id.tv_daypercent_tip)
    private TextView tvDayPercentTip;

    // 股价涨跌幅，switch
    @ViewInject(R.id.sw_price_up)
    private Switch swPriceUp;
    @ViewInject(R.id.sw_price_down)
    private Switch swPriceDown;
    @ViewInject(R.id.sw_day_percent)
    private Switch swDayPercent;

    // 股价涨跌幅输入
    @ViewInject(R.id.et_priceup)
    private EditText etPriceUp;
    @ViewInject(R.id.et_pricedown)
    private EditText etPriceDown;
    @ViewInject(R.id.et_daypercent)
    private EditText etDayPercent;

    @ViewInject(R.id.rl_adjust_remind)
    private View viewAdjustRemind;
    @ViewInject(R.id.rl_notice_remind)
    private View viewNoticeRemind;
    @ViewInject(R.id.rl_yanbao_remind)
    private View viewYanbaoRemind;

    @ViewInject(R.id.sw_adjust_remind)
    private Switch swAdjustRemind;
    @ViewInject(R.id.sw_notice_remind)
    private Switch swNoticeRemind;
    @ViewInject(R.id.sw_yanbao_remind)
    private Switch swYanbaoRemind;

    private boolean isPriceUpOK;

    public static Intent newStockIntent(Context context, SelectStockBean stock) {
        Intent intent = new Intent(context, StockRemindActivity.class);
        intent.putExtra(ARGUMENT_STOCK, stock);
        intent.putExtra(ARGUMENT_IS_COMBINATION, false);
        return intent;
    }

    public static Intent newCombinatIntent(Context context, CombinationBean conBean) {
        Intent intent = new Intent(context, StockRemindActivity.class);
        intent.putExtra(ARGUMENT_COMBINATION, conBean);
        intent.putExtra(ARGUMENT_IS_COMBINATION, true);
        return intent;
    }

    @Override
    protected void onCreate(Bundle arg0) {
        super.onCreate(arg0);
        setTitle(R.string.title_remind);
        setContentView(R.layout.activity_stock_remind);
        ViewUtils.inject(this);
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            handleExtras(extras);
        }
        initView();
    }

    private void handleExtras(Bundle extras) {
        mStockBean = (SelectStockBean) extras.get(ARGUMENT_STOCK);
        mComBean = (CombinationBean) extras.get(ARGUMENT_COMBINATION);
        isCombinationSetting = extras.getBoolean(ARGUMENT_IS_COMBINATION);
    }

    private void initView() {
        btnRight = getRightButton();
        btnRight.setOnClickListener(this);
        btnRight.setText(R.string.finish);

        etPriceUp.addTextChangedListener(priceUpTextWatch);
        etPriceDown.addTextChangedListener(priceDownTextWatch);
        etDayPercent.addTextChangedListener(percentTextWatch);

        if (isCombinationSetting) {
            setCombinationStyle();
        } else {
            setStockStyle();
        }

    }

    private void setCombinationStyle() {
        tvStockCode.setVisibility(View.INVISIBLE);
        tvUpText.setText(R.string.combination_price_up_text);
        tvDownText.setText(R.string.combination_price_down_text);
        viewAdjustRemind.setVisibility(View.VISIBLE);
        viewNoticeRemind.setVisibility(View.GONE);
        viewYanbaoRemind.setVisibility(View.GONE);
        tvDownUnit.setVisibility(View.GONE);
        tvUpUnit.setVisibility(View.GONE);

        if (null != mComBean) {
            tvStockName.setText(mComBean.getName());
            String perText = getString(R.string.format_com_percent,
                    StringFromatUtils.get2PointPercent(mComBean.getChng_pct_day()));
            tvPercent.setText(perText);
            perText = getString(R.string.format_combination_price, mComBean.getNetvalue());
            tvPrice.setText(perText);

            if (null != mComBean.getAlertBean()) {
                setAlertView(mComBean.getAlertBean());
            }
        }

    }

    private void setStockStyle() {
        if (null != mStockBean) {
            tvStockName.setText(mStockBean.getName());
            tvStockCode.setText(mStockBean.getCode());
            String perText = getString(R.string.format_percent,
                    StringFromatUtils.get2PointPercent(mStockBean.getPercentage()));
            tvPercent.setText(perText);

            if (StockUitls.isShangZhengB(mStockBean.getCode())) {
                perText = getString(R.string.format_stock_price,
                        StringFromatUtils.get3Point(mStockBean.getCurrentValue()));
            } else {
                perText = getString(R.string.format_stock_price,
                        StringFromatUtils.get2Point(mStockBean.getCurrentValue()));

            }
            if (StockUitls.isIndexStock(mStockBean.getSymbol_type())) {
                viewNoticeRemind.setVisibility(View.GONE);
                viewYanbaoRemind.setVisibility(View.GONE);
            }

            tvPrice.setText(perText);
            if (null != mStockBean.alertSetBean) {
                setAlertView(mStockBean.alertSetBean);
            }
        }
    }

    private void setAlertView(AlertSetBean alerBean) {
        // AlertSetBean alerBean = mStockBean.alertSetBean;
        if (alerBean.getStock_price_up() > 0) {
            etPriceUp.setText(stripZeros(alerBean.getStock_price_up() + ""));
            swPriceUp.setChecked(true);
            // isPriceUpOK = true;
        }
        if (alerBean.getStock_price_down() > 0) {
            // isPriceDownOk = true;
            etPriceDown.setText(stripZeros(alerBean.getStock_price_down() + ""));
            swPriceDown.setChecked(true);
        }
        if (alerBean.getStock_percentage() > 0) {
            etDayPercent.setText(stripZeros(alerBean.getStock_percentage() + ""));
            swDayPercent.setChecked(true);
        }
        swNoticeRemind.setChecked(alerBean.isNoticeRemind());
        swYanbaoRemind.setChecked(alerBean.isYanbaoRemind());
    }

    private void setAlertView(PortfolioAlertBean alerBean) {
        // AlertSetBean alerBean = mStockBean.alertSetBean;
        if (alerBean.getPortfolio_price_up() > 0) {

            etPriceUp.setText(stripZeros(alerBean.getPortfolio_price_up() + ""));
            swPriceUp.setChecked(true);
            isPriceUpOK = true;
        }
        if (alerBean.getPortfolio_price_down() > 0) {
            isPriceDownOk = true;
            etPriceDown.setText(stripZeros(alerBean.getPortfolio_price_down() + ""));
            swPriceDown.setChecked(true);
        }
        if (alerBean.getPortfolio_percentage() > 0) {
            etDayPercent.setText(stripZeros(alerBean.getPortfolio_percentage() + ""));
            swDayPercent.setChecked(true);
        }
        swAdjustRemind.setChecked(alerBean.isAdjustAlert());
    }

    private String stripZeros(String value) {
        return new BigDecimal(value).stripTrailingZeros() + "";
    }

    @OnClick({ R.id.btn_right, })
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_right: {
                if (swPriceUp.isChecked() && swPriceDown.isChecked() && !isPriceDownOk && !isPriceUpOK) {// 下跌目标价高于最新价&&下跌目标价高于最新价
                    showAlertDialog(R.string.msg_price_error);
                } else if (swPriceUp.isChecked() && !isPriceUpOK) {// 上涨目标低于最新价
                    showAlertDialog(R.string.msg_priceup_toolow);
                } else if (swPriceDown.isChecked() && !isPriceDownOk) {// 下跌目标价高于最新价
                    showAlertDialog(R.string.msg_pricedown_toohigh);

                } else {

                    postRemindToServer();
                }
            }
                break;

            default:
                break;
        }

    }

    private String strBefore;
    TextWatcher priceUpTextWatch = new TextWatcher() {

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            strBefore = s.toString();
        }

        @Override
        public void afterTextChanged(Editable s) {
            String textString = s.toString();
            // 一定要加上此判断，否则会进入死循环
            if (textString.equals(strBefore)) {
                return;
            }

            int editStart = etPriceUp.getSelectionStart();
            if (!isAllowPriceInputText(textString)) {
                etPriceUp.setText(strBefore);
                etPriceUp.setSelection(strBefore.length());

            } else {
                strBefore = s.toString();
                etPriceUp.setText(s);
                etPriceUp.setSelection(editStart);
            }

            float priceUpFloat = priceUpTip(s.toString(),
                    isCombinationSetting ? mComBean.getNetvalue() : mStockBean.getCurrentValue());
            if (priceUpFloat == 0) {
                tvUpTip.setVisibility(View.INVISIBLE);

            } else {

                tvUpTip.setVisibility(View.VISIBLE);
                setPriceUpTip(priceUpFloat);
            }

        }
    };

    TextWatcher priceDownTextWatch = new TextWatcher() {

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            strBefore = s.toString();
        }

        @Override
        public void afterTextChanged(Editable s) {
            String textString = s.toString();
            // 一定要加上此判断，否则会进入死循环
            if (textString.equals(strBefore)) {
                return;
            }

            int editStart = etPriceDown.getSelectionStart();
            if (!isAllowPriceInputText(textString)) {
                etPriceDown.setText(strBefore);
                etPriceDown.setSelection(strBefore.length());

            } else {
                strBefore = s.toString();
                etPriceDown.setText(s);
                etPriceDown.setSelection(editStart);
            }
            float priceDownFloat = priceUpTip(s.toString(),
                    isCombinationSetting ? mComBean.getNetvalue() : mStockBean.getCurrentValue());
            if (priceDownFloat == 0) {
                tvDownTip.setVisibility(View.INVISIBLE);
            } else {

                tvDownTip.setVisibility(View.VISIBLE);
                setPriceDownTip(priceDownFloat);
            }

        }
    };
    TextWatcher percentTextWatch = new TextWatcher() {

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            strBefore = s.toString();
        }

        @Override
        public void afterTextChanged(Editable s) {
            String textString = s.toString();
            // 一定要加上此判断，否则会进入死循环
            if (textString.equals(strBefore)) {
                return;
            }

            int editStart = etDayPercent.getSelectionStart();
            if (!isAllowPercentInputText(textString)) {
                etDayPercent.setText(strBefore);
                etDayPercent.setSelection(strBefore.length());

            } else {
                strBefore = s.toString();
                etDayPercent.setText(s);
                etDayPercent.setSelection(editStart);
            }

        }
    };

    private void setPriceUpTip(float priceUpFloat) {

        if (priceUpFloat > 0) {
            isPriceUpOK = true;
            if (priceUpFloat > 300) {
                if (isCombinationSetting) {

                    tvUpTip.setText(getString(R.string.format_cpriceup_more_tip,
                            StringFromatUtils.get2PointPercent(300)));
                } else {

                    tvUpTip.setText(getString(R.string.format_priceup_more_tip, StringFromatUtils.get2PointPercent(300)));
                }
            } else {
                if (isCombinationSetting) {

                    tvUpTip.setText(getString(R.string.format_cpriceup_tip,
                            StringFromatUtils.get2PointPercent(priceUpFloat)));
                } else {

                    tvUpTip.setText(getString(R.string.format_priceup_tip,
                            StringFromatUtils.get2PointPercent(priceUpFloat)));
                }
            }
        } else {
            isPriceUpOK = false;
            if (priceUpFloat > -300) {
                if (isCombinationSetting) {
                    tvUpTip.setText(Html.fromHtml(setColorText(R.string.format_cpriceup_low_tip,
                            StringFromatUtils.get2PointPercent(Math.abs(priceUpFloat)))));
                } else {
                    tvUpTip.setText(Html.fromHtml(setColorText(R.string.format_priceup_low_tip,
                            StringFromatUtils.get2PointPercent(Math.abs(priceUpFloat)))));
                }

            } else {

                if (isCombinationSetting) {
                    tvUpTip.setText(Html.fromHtml(setColorText(R.string.format_cpriceup_low_more_tip,
                            StringFromatUtils.get2PointPercent(300))));

                } else {

                    tvUpTip.setText(Html.fromHtml(setColorText(R.string.format_priceup_low_more_tip,
                            StringFromatUtils.get2PointPercent(300))));
                }
            }
        }
    }

    private boolean isPriceDownOk;

    private void setPriceDownTip(float priceDownFloat) {
        if (priceDownFloat < 0) {
            isPriceDownOk = true;
            // if (priceDownFloat > -300) {
            if (isCombinationSetting) {

                tvDownTip.setText(getString(R.string.format_cpricedown_tip,
                        StringFromatUtils.get2PointPercent(Math.abs(priceDownFloat))));
            } else {

                tvDownTip.setText(getString(R.string.format_pricedown_tip,
                        StringFromatUtils.get2PointPercent(Math.abs(priceDownFloat))));
            }
            // }
        } else {
            isPriceDownOk = false;
            if (priceDownFloat < 300) {
                if (isCombinationSetting) {
                    tvDownTip.setText(Html.fromHtml(setColorText(R.string.format_cpricedown_high_tip,
                            StringFromatUtils.get2PointPercent(priceDownFloat))));
                } else {

                    tvDownTip.setText(Html.fromHtml(setColorText(R.string.format_pricedown_high_tip,
                            StringFromatUtils.get2PointPercent(priceDownFloat))));
                }
            } else {
                if (isCombinationSetting) {

                    tvDownTip.setText(Html.fromHtml(setColorText(R.string.format_cpricedown_high_more_tip,
                            StringFromatUtils.get2PointPercent(300))));
                } else {

                    tvDownTip.setText(Html.fromHtml(setColorText(R.string.format_pricedown_high_more_tip,
                            StringFromatUtils.get2PointPercent(300))));
                }
            }
        }
    }

    private String setColorText(int resFormatId, String valueStr) {
        String htmlRed = "<font  color=\"red\">" + valueStr + "</font>";
        return getString(resFormatId, htmlRed);
    }

    private boolean isAllowPriceInputText(String str) {

        if (TextUtils.isEmpty(str)) {
            return true;
        }
        // 匹配XXXXXX.XXX
        String compText = "^(\\d{0,6})|(\\d{0,6}?(\\.\\d{0,3}))$";
        Pattern p = Pattern.compile(compText);
        Matcher m = p.matcher(str);
        return m.matches();
    }

    private boolean isAllowPercentInputText(String str) {

        if (TextUtils.isEmpty(str)) {
            return true;
        }
        // 匹配XXXXXX.XXX
        String compText = "^(\\d{0,6})|(\\d{0,6}?(\\.\\d{0,2}))$";
        Pattern p = Pattern.compile(compText);
        Matcher m = p.matcher(str);
        return m.matches();
    }

    private float priceUpTip(String input, float compareValue) {
        try {
            float inputValue = Float.parseFloat(input);
            float uPercent = ((inputValue - compareValue) / compareValue) * 100;
            return uPercent;
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }

    }

    @OnCompoundButtonCheckedChange({ R.id.sw_price_up, R.id.sw_price_down, R.id.sw_day_percent })
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        EditText editText = null;
        switch (buttonView.getId()) {
            case R.id.sw_price_up: {
                editText = etPriceUp;
            }
                break;
            case R.id.sw_price_down: {
                editText = etPriceDown;
            }
                break;
            case R.id.sw_day_percent: {
                editText = etDayPercent;
            }
                break;
            default:
                break;
        }

        if (null != editText) {
            if (isChecked) {
                editText.requestFocus();
            } else {
                editText.clearFocus();
            }
        }
    }

    @OnFocusChange({ R.id.et_priceup, R.id.et_pricedown, R.id.et_daypercent })
    public void onFocusChange(View v, boolean hasFocus) {
        Switch switchButtom = null;
        EditText editText = null;
        switch (v.getId()) {
            case R.id.et_priceup: {
                switchButtom = swPriceUp;
                editText = etPriceUp;
            }
                break;
            case R.id.et_pricedown: {
                switchButtom = swPriceDown;
                editText = etPriceDown;
            }
                break;
            case R.id.et_daypercent: {
                switchButtom = swDayPercent;
                editText = etDayPercent;
            }
                break;

            default:
                break;
        }
        if (null != switchButtom) {
            if (hasFocus) {
                switchButtom.setChecked(true);
            }
            if (!hasFocus && null != editText && TextUtils.isEmpty(editText.getText())) {
                switchButtom.setChecked(false);
            }
        }
    }

    private void showAlertDialog(final int messageId) {

        AlertDialog.Builder builder = new AlertDialog.Builder(new ContextThemeWrapper(this,
                android.R.style.Theme_Holo_Light_Dialog_NoActionBar));

        builder.setMessage(messageId).setPositiveButton(R.string.btn_reinput, new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {

                if (messageId == R.string.msg_priceup_toolow || messageId == R.string.msg_price_error) {
                    etPriceUp.requestFocus();
                } else if (messageId == R.string.msg_pricedown_toohigh) {
                    etPriceDown.requestFocus();
                }
                dialog.dismiss();

            }
        });
        builder.show();

    }

    private void postRemindToServer() {

        float priceUp = swPriceUp.isChecked() ? parseToFloat(etPriceUp) : 0;
        float priceDown = swPriceDown.isChecked() ? parseToFloat(etPriceDown) : 0;
        float dayPercent = swDayPercent.isChecked() ? parseToFloat(etDayPercent) : 0;
        if (isCombinationSetting) {
            new MyCombinationEngineImpl().portfolioRemind(mComBean.getId(), priceUp, priceDown, dayPercent,
                    swAdjustRemind.isChecked(), remindHttpListener.setLoadingDialog(this));
        } else {

            new QuotesEngineImpl().stockRemind(mStockBean.getId() + "", priceUp, priceDown, dayPercent,
                    swNoticeRemind.isChecked(), swYanbaoRemind.isChecked(), remindHttpListener.setLoadingDialog(this));
        }
    }

    ParseHttpListener remindHttpListener = new ParseHttpListener<Object>() {

        @Override
        protected Object parseDateTask(String jsonData) {
            // TODO Auto-generated method stub
            return null;
        }

        @Override
        protected void afterParseData(Object object) {
            // PromptManager.showToast("提醒设置成功");
            PromptManager.showCustomToast(R.drawable.ic_toast_dagou, R.string.toast_remind_success);
            finish();

        }
    };

    private float parseToFloat(EditText edtext) {
        try {
            return Float.parseFloat(edtext.getText().toString());
        } catch (Exception e) {
            return 0;
        }
    }

}
