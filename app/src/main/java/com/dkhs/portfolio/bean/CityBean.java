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
    private String municipalityStr = "北京，上海，重庆，天津";
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
        if(!TextUtils.isEmpty(name)&&municipalityStr.contains(name)){
            return true;
        }else{
            return false;
        }
    }
}
