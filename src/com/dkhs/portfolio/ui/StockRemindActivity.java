/**
 * @Title StockRemindActivity.java
 * @Package com.dkhs.portfolio.ui
 * @Description TODO(用一句话描述该文件做什么)
 * @author zjz
 * @date 2015-4-13 下午2:07:36
 * @version V1.0
 */
package com.dkhs.portfolio.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;

import com.dkhs.portfolio.R;
import com.dkhs.portfolio.bean.SelectStockBean;
import com.dkhs.portfolio.utils.StringFromatUtils;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;

/**
 * @ClassName StockRemindActivity
 * @Description TODO(这里用一句话描述这个类的作用)
 * @author zjz
 * @date 2015-4-13 下午2:07:36
 * @version 1.0
 */
public class StockRemindActivity extends ModelAcitivity implements OnClickListener {

    public static final String ARGUMENT_STOCK = "agrument_stock";
    private Button btnRight;
    private SelectStockBean mStockBean;

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

    public static Intent newStockIntent(Context context, SelectStockBean stock) {
        Intent intent = new Intent(context, StockRemindActivity.class);
        intent.putExtra(ARGUMENT_STOCK, stock);
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
    }

    private void initView() {
        btnRight = getRightButton();
        btnRight.setOnClickListener(this);
        btnRight.setText(R.string.finish);

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

    }

    private void setStockStyle() {
        if (null != mStockBean) {
            tvStockName.setText(mStockBean.getName());
            tvStockCode.setText(mStockBean.getCode());
            String perText = getString(R.string.format_percent,
                    StringFromatUtils.get2PointPercent(mStockBean.getPercentage()));
            tvPercent.setText(perText);
            perText = getString(R.string.format_stock_price, mStockBean.getCurrentValue());
            tvPrice.setText(perText);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_right: {

            }
                break;
            default:
                break;
        }

    }
}
