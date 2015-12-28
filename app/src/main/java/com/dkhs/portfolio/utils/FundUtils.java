package com.dkhs.portfolio.utils;

import android.content.Context;

import com.dkhs.portfolio.R;

/**
 * Created by wuyongsen on 2015/12/25.
 */
public class FundUtils {
    public static final int LEVEL_UNKNOWN = 0;
    public static final int LEVEL_LOW = 1;
    public static final int LEVEL_MEDIUM_LOW = 2;
    public static final int LEVEL_MEDIUM = 3;
    public static final int LEVEL_MEDIUM_HIGH = 4;
    public static final int LEVEL_HIGH = 5;
    public static String getInvestRiskByType(int type ,Context context){
        String[] levels = context.getResources().getStringArray(R.array.levels_investment_risk);
        String inverstRisk = "";
        switch (type){
            case LEVEL_UNKNOWN:
                inverstRisk = levels[0];
                break;
            case LEVEL_LOW:
                inverstRisk = levels[1];
                break;
            case LEVEL_MEDIUM_LOW:
                inverstRisk = levels[2];
                break;
            case LEVEL_MEDIUM:
                inverstRisk = levels[3];
                break;
            case LEVEL_MEDIUM_HIGH:
                inverstRisk = levels[4];
                break;
            case LEVEL_HIGH:
                inverstRisk = levels[5];
                break;
        }
        return inverstRisk;
    }
}
