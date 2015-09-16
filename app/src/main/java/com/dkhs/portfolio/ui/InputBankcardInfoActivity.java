package com.dkhs.portfolio.ui;

import android.os.Bundle;
import android.view.View;

import com.dkhs.portfolio.R;
import com.dkhs.portfolio.bean.BindThreePlat;
import com.dkhs.portfolio.net.DataParse;
import com.dkhs.portfolio.net.ParseHttpListener;
import com.lidroid.xutils.ViewUtils;

import java.util.List;

/**
 * Created by zhangcm on 2015/9/16.15:02
 */
public class InputBankcardInfoActivity extends ModelAcitivity {


    @Override
    protected void onCreate(Bundle arg0) {
        super.onCreate(arg0);
        setContentView(R.layout.activity_input_bank_card_info);
        ViewUtils.inject(this);
        setTitle(R.string.input_bank_card_info);
    }
    private void onclick(View v){
        //TODO 点击下一步
    }

    private ParseHttpListener<List<BindThreePlat>> bindsListener = new ParseHttpListener<List<BindThreePlat>>() {

        public void onFailure(int errCode, String errMsg) {
            super.onFailure(errCode, errMsg);
        }


        @Override
        protected List<BindThreePlat> parseDateTask(String jsonData) {

            return DataParse.parseArrayJson(BindThreePlat.class, jsonData);
        }

        @Override
        protected void afterParseData(List<BindThreePlat> entity) {

        }
    };

}
