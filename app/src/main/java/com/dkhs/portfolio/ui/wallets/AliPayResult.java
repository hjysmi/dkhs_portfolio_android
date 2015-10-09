package com.dkhs.portfolio.ui.wallets;

import android.text.TextUtils;

/**
 * Created by zjz on 2015/10/9.
 */
public class AliPayResult {

    private String resultStatus;
    private String result;
    private String memo;

    public AliPayResult(String rawResult) {

        if (TextUtils.isEmpty(rawResult))
            return;

        String[] resultParams = rawResult.split(";");
        for (String resultParam : resultParams) {
            if (resultParam.startsWith("resultStatus")) {
                resultStatus = gatValue(resultParam, "resultStatus");
            }
            if (resultParam.startsWith("result")) {
                result = gatValue(resultParam, "result");
            }
            if (resultParam.startsWith("memo")) {
                memo = gatValue(resultParam, "memo");
            }
        }
    }

    @Override
    public String toString() {
        return "resultStatus={" + resultStatus + "};memo={" + memo
                + "};result={" + result + "}";
    }

    private String gatValue(String content, String key) {
        String prefix = key + "={";
        return content.substring(content.indexOf(prefix) + prefix.length(),
                content.lastIndexOf("}"));
    }

    /**
     * @return the resultStatus
     */
    public String getResultStatus() {
        return resultStatus;
    }

    /**
     * @return the memo
     */
    public String getMemo() {
        return memo;
    }

    /**
     * @return the result
     */
    public String getResult() {
        return result;
    }
}

