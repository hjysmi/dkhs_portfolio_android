package com.dkhs.portfolio.ui;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;

import com.dkhs.portfolio.R;
import com.dkhs.portfolio.security.SecurityUtils;

/**
 * Created by zjz on 2015/5/13.
 */
public class TestActivity extends FragmentActivity {

    @Override
    protected void onCreate(Bundle arg0) {
        super.onCreate(arg0);
        setContentView(R.layout.activity_test);


        findViewById(R.id.btn_test).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getData();
            }
        });

//        getSupportFragmentManager().beginTransaction()
//                .replace(R.id.stock_layout, TabF10Fragment.newIntent("sz300363", TabF10Fragment.TabType.INTRODUCTION)).commit();

//        replaceContentFragment(TabF10Fragment.newIntent("sz300363", TabF10Fragment.TabType.INTRODUCTION));
    }

    private void getData() {

        new Thread() {
            @Override
            public void run() {
                try {

                    new SecurityUtils().testCreditCardPay();
                } catch (Exception e
                        ) {
                    e.printStackTrace();
                }

            }


        }.start();


    }
}
