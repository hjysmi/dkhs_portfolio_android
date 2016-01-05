package com.dkhs.portfolio.bean;

import android.text.style.ForegroundColorSpan;

import com.dkhs.portfolio.common.Spanny;
import com.dkhs.portfolio.utils.ColorTemplate;
import com.dkhs.portfolio.utils.StringFromatUtils;

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
    public String work_seniority;
    public String avatar_sm;
    public String avatar_md;
    public String avatar_xs;
    public float win_rate_day;
    public String win_rate_week;
    public String win_rate_month;
    public String win_rate_year;
    public String win_rate_tyear;

    public float index_rate_all;
    public int id;
    public float win_rate_six_month;
    public String name;
    public String avatar_lg;
    public float win_rate_season;
    public float win_rate_twyear;
    public float index_rate_week;
    public String recommend_title;
    public String recommend_desc;
    public String fund_company;
    public float index_rate_day;
    public float index_rate_month;
    public float index_rate_season;
    public float index_rate_six_month;
    public float index_rate_year;
    public float index_rate_tyear;
    public float index_rate_twyear;


    public Float getValue(String value) {
        switch (value) {
            case "-win_rate_day":
                return win_rate_day;
            case "-win_rate_month":
                return Float.parseFloat(win_rate_month);
            case "-win_rate_season":
                return win_rate_season;
            case "-win_rate_six_month":
                return win_rate_six_month;
            case "-win_rate_year":
                return Float.parseFloat(win_rate_year);
            case "-win_rate_tyear":
                return Float.parseFloat(win_rate_tyear);
            case "-win_rate_week":
                return Float.parseFloat(win_rate_week);
            case "-win_rate_twyear":
                return win_rate_twyear;
            case "-index_rate_day":
                return index_rate_day;
            case "-index_rate_month":
                return index_rate_month;
            case "-index_rate_season":
                return index_rate_season;
            case "-index_rate_six_month":
                return index_rate_six_month;
            case "-index_rate_year":
                return index_rate_year;
            case "-index_rate_tyear":
                return index_rate_tyear;
            case "-index_rate_week":
                return index_rate_week;
            case "-index_rate_twyear":
                return index_rate_twyear;
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

    @Override
    public String toString() {
        return "FundManagerBean{" +
                "avatar_lg='" + avatar_lg + '\'' +
                ", win_rate_tyear=" + win_rate_tyear +
                ", work_seniority='" + work_seniority + '\'' +
                ", avatar_sm='" + avatar_sm + '\'' +
                ", avatar_md='" + avatar_md + '\'' +
                ", avatar_xs='" + avatar_xs + '\'' +
                ", win_rate_day=" + win_rate_day +
                ", win_rate_month=" + win_rate_month +
                ", win_rate_year=" + win_rate_year +
                ", id=" + id +
                ", win_rate_six_month=" + win_rate_six_month +
                ", name='" + name + '\'' +
                ", win_rate_week=" + win_rate_week +
                ", win_rate_season=" + win_rate_season +
                ", win_rate_twyear=" + win_rate_twyear +
                ", index_rate_week=" + index_rate_week +
                ", recommend_title=" + recommend_title +
                ", recommend_desc=" + recommend_desc +
                ", fund_company=" + fund_company +
                '}';
    }
}
