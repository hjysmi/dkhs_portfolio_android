package com.dkhs.portfolio.utils;

import android.content.Context;

import com.dkhs.portfolio.R;
import com.dkhs.portfolio.bean.FundQuoteBean;

/**
 * Created by wuyongsen on 2015/12/25.
 */
public class FundUtils {
    private static final int LEVEL_UNKNOWN = 0;
    private static final int LEVEL_LOW = 1;
    private static final int LEVEL_MEDIUM_LOW = 2;
    private static final int LEVEL_MEDIUM = 3;
    private static final int LEVEL_MEDIUM_HIGH = 4;
    private static final int LEVEL_HIGH = 5;

    public static String getInvestRiskByType(int type, Context context) {
        String[] levels = context.getResources().getStringArray(R.array.levels_investment_risk);
        String inverstRisk = "";
        switch (type) {
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

    private static int[] purchaseTypes = new int[]{R.string.purchase_other, R.string.purchase_front, R.string.purchase_back,};

    public static String setPurchaseType(Context context, FundQuoteBean fundQuoteBean) {
        String purchaseStr;
        try {
            purchaseStr = UIUtils.getResString(context, purchaseTypes[fundQuoteBean.getCharge_mode()]);
        } catch (Exception e) {
            purchaseStr = UIUtils.getResString(context, purchaseTypes[0]);
        }
        return purchaseStr;
    }
}
