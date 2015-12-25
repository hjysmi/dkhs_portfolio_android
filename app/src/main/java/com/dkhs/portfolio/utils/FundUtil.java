package com.dkhs.portfolio.utils;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;

import com.dkhs.portfolio.bean.MyBankCard;
import com.dkhs.portfolio.engine.TradeEngineImpl;
import com.dkhs.portfolio.net.ParseHttpListener;
import com.dkhs.portfolio.net.StringDecodeUtil;
import com.dkhs.portfolio.ui.ResetTradePasswordActivity;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.util.List;

/**
 * Created by zhangcm on 2015/12/25.
 */
public class FundUtil {
    public static void goBuyFund(Context context){
        new TradeEngineImpl().getMyBankCards(new ParseHttpListener<List<MyBankCard>>() {
            @Override
            protected List<MyBankCard> parseDateTask(String jsonData) {
                List<MyBankCard> myCards = null;
                if (!TextUtils.isEmpty(jsonData)) {
                    try {
                        jsonData = StringDecodeUtil.decodeUnicode(jsonData);
                        Gson gson = new GsonBuilder().enableComplexMapKeySerialization().create();
                        myCards = gson.fromJson(jsonData, new TypeToken<List<MyBankCard>>(){}.getType());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                return myCards;
            }

            @Override
            protected void afterParseData(List<MyBankCard> cards) {
                if(cards != null && cards.size() > 0){
                    // TODO: 2015/12/25 绑卡了
                }else{
                    // TODO: 2015/12/25 去绑卡
                }
            }
        });
    }
}
