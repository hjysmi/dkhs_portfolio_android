package com.dkhs.portfolio.net;

import android.text.TextUtils;

import com.lidroid.xutils.util.LogUtils;

/**
 * @author zwm
 * @version 2.0
 * @ClassName SimpeleParseHttpListener
 * @Description TODO(这里用一句话描述这个类的作用)
 * @date 2015/6/18.
 */
public abstract class SimpleParseHttpListener  extends  ParseHttpListener {


    public abstract Class getClassType();

    @Override
    protected Object parseDateTask(String jsonData) {


        LogUtils.e(jsonData);
        if(TextUtils.isEmpty(jsonData)){
            return  null;
        }
        if(jsonData.trim().startsWith("[")){
            return  DataParse.parseArrayJson(getClassType(),jsonData);
        }else {
            return  DataParse.parseObjectJson(getClassType(),jsonData);
        }
    }




}
