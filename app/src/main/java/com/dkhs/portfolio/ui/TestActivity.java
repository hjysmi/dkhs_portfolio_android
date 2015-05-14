package com.dkhs.portfolio.ui;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;

import com.dkhs.portfolio.R;
import com.dkhs.portfolio.ui.fragment.TabF10Fragment;

/**
 * Created by zjz on 2015/5/13.
 */
public class TestActivity extends FragmentActivity {

    @Override
    protected void onCreate(Bundle arg0) {
        super.onCreate(arg0);
        setContentView(R.layout.activity_test);
//        getData();

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.stock_layout, TabF10Fragment.newIntent("sz300363", TabF10Fragment.TabType.INTRODUCTION)).commit();

//        replaceContentFragment(TabF10Fragment.newIntent("sz300363", TabF10Fragment.TabType.INTRODUCTION));
    }

    private void getData() {

//        F10DataBean dataBean = DataParse.parseObjectJson(F10DataBean.class, F10DataBean.testValue2);
//        F10ViewParse viewParse = new F10ViewParse(this);
//        viewParse.setF10Data(dataBean);
//
//        LinearLayout ll = (LinearLayout) findViewById(R.id.ll_content);
//        List<F10DataBean> list = DataParse.parseArrayJson(F10DataBean.class, F10DataBean.testValueIntroduct);
//        for (F10DataBean dataBean1 : list) {
//            ll.addView(new F10ViewParse(this, dataBean1).getContentView());
//        }



    }
}