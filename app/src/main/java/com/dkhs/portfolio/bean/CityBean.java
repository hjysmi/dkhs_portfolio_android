package com.dkhs.portfolio.bean;

import android.text.TextUtils;

import com.lidroid.xutils.db.annotation.Table;

/**
 * Created by wuyongsen on 2015/12/8.
 */
@Table(name = "city")
public class CityBean {
    public static final String TYPE = "type";
    public static final String PARENT_CODE = "parent_code";
    public static final String PINYIN = "pinyin";
    private static final String MUNICIPALITY = "北京，上海，重庆，天津,香港，澳门";
    public String id;
    //判断省或者市
    public String type;
    public String parent_code;
    public String name;
    public String pinyin;

    /**
     * 判断是否直辖市
     * @return
     */
    public boolean isMunicipality(){
        return !TextUtils.isEmpty(name)&&MUNICIPALITY.contains(name);
    }
}
