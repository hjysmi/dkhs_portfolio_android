/**
 * @Title StockRemindActivity.java
 * @Package com.dkhs.portfolio.ui
 * @Description TODO(用一句话描述该文件做什么)
 * @author zjz
 * @date 2015-4-13 下午2:07:36
 * @version V1.0
 */
package com.dkhs.portfolio.ui;

import java.io.Serializable;
import java.util.List;

import com.dkhs.portfolio.R;
import com.dkhs.portfolio.bean.SelectStockBean;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

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
