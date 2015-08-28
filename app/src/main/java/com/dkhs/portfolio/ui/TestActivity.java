package com.dkhs.portfolio.ui;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.text.Html;
import android.util.Log;
import android.view.View;

import com.dkhs.portfolio.R;
import com.dkhs.portfolio.ui.widget.DKHSEditText;

/**
 * Created by zjz on 2015/5/13.
 */
public class TestActivity extends FragmentActivity implements View.OnClickListener {

    private DKHSEditText etInput;

    @Override
    protected void onCreate(Bundle arg0) {
        super.onCreate(arg0);
        setContentView(R.layout.activity_test);

//        getSupportFragmentManager().beginTransaction()
//                .replace(R.id.stock_layout, TabF10Fragment.newIntent("sz300363", TabF10Fragment.TabType.INTRODUCTION)).commit();

//        replaceContentFragment(TabF10Fragment.newIntent("sz300363", TabF10Fragment.TabType.INTRODUCTION));
        etInput = (DKHSEditText) findViewById(R.id.et_input);
        findViewById(R.id.btn_test2).setOnClickListener(this);
        findViewById(R.id.btn_test1).setOnClickListener(this);
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

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.btn_test1:
                inserUserText();
                break;
            case R.id.btn_test2:
                break;
        }
    }


    public void inserUserText() {

        String name = "zhozuzhou";

        String htmlName = String.format("<a href=\"portfolio:friend\">%s</a>", name);
        Log.e(this.getClass().getSimpleName(), " htmname:" + htmlName);
        etInput.inserUserText(name);
        String befro = Html.toHtml(etInput.getText());
        Log.e(this.getClass().getSimpleName(), " html Edittext:" + befro);

    }


}
