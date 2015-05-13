package com.dkhs.portfolio.ui;

import android.app.Activity;
import android.os.Bundle;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.dkhs.portfolio.bean.F10DataBean;
import com.dkhs.portfolio.net.DataParse;
import com.dkhs.portfolio.ui.widget.F10ViewParse;

import java.util.List;

/**
 * Created by zjz on 2015/5/13.
 */
public class TestActivity extends Activity {

    @Override
    protected void onCreate(Bundle arg0) {
        super.onCreate(arg0);

        getData();
    }

    private void getData(){
        F10DataBean dataBean =DataParse.parseObjectJson(F10DataBean.class,F10DataBean.testValue2);
        F10ViewParse viewParse = new F10ViewParse(this);
        viewParse.setF10Data(dataBean);

        LinearLayout ll = new LinearLayout(this);
        ll.setOrientation(LinearLayout.VERTICAL);
        ll.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        List<F10DataBean> list = DataParse.parseArrayJson(F10DataBean.class,F10DataBean.testValueIntroduct);
        for(F10DataBean dataBean1:list){
            ll.addView(new F10ViewParse(this,dataBean1).getContentView());
        }


        this.addContentView(ll,new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));

    }
}
