package com.dkhs.portfolio.bean;

import android.text.style.BackgroundColorSpan;
import android.text.style.ForegroundColorSpan;

import com.dkhs.portfolio.common.Spanny;
import com.dkhs.portfolio.utils.ColorTemplate;
import com.dkhs.portfolio.utils.StringFromatUtils;
import com.google.gson.annotations.SerializedName;

import org.parceler.apache.commons.lang.StringUtils;

/**
 * @author zwm
 * @version 2.0
 * @ClassName FundManagerBean
 * @Description TODO(这里用一句话描述这个类的作用)
 * @date 2015/7/10.
 */
public class FundManagerBean {

    /**
     * win_rate_tyear : 26.30071686627794
     * work_seniority : 0
     * avatar_sm :
     * avatar_md :
     * avatar_xs :
     * win_rate_day : -81.32314734934451
     * win_rate_month : 26.252383300793387
     * id : 302004020
     * win_rate_six_month : 26.301329798917394
     * name : 刘方正
     * avatar_lg :
     * win_rate_week : 5.120493109561712
     * win_rate_season : 26.30129524058558
     * win_rate_year : 26.300363591943466
     */
    public float win_rate_tyear;
    public String work_seniority;
    public String avatar_sm;
    public String avatar_md;
    public String avatar_xs;
    public float win_rate_day;
    public float win_rate_month;
    public int id;
    public float win_rate_six_month;
    public String name;
    public String avatar_lg;
    public float win_rate_week;
    public float win_rate_season;
    public float win_rate_year;


    public Float getValue(String value) {
        switch (value) {
            case "win_rate_day":
                return win_rate_day;
            case "win_rate_month":
                return win_rate_month;
            case "win_rate_season":
                return win_rate_season;
            case "win_rate_six_month":
                return win_rate_six_month;
            case "win_rate_year":
                return win_rate_year;
            case "win_rate_tyear":
                return win_rate_tyear;
            case "win_rate_week":
                return win_rate_week;
        }
        return null;
    }

    public CharSequence getValueString(String key) {


        Float value = getValue(key);

        if(value !=null ) {
            return    new  Spanny().append(StringFromatUtils.get2PointPercent(value.floatValue()),new ForegroundColorSpan(ColorTemplate.getPercentColor(value)));
        }else{
            return    new  Spanny().append("--",new  ForegroundColorSpan(ColorTemplate.DEF_GRAY));
        }

    }



}
