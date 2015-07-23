/**
 * @Title ErrorBundle.java
 * @Package com.dkhs.portfolio.net
 * @Description TODO(用一句话描述该文件做什么)
 * @author zjz
 * @date 2014-9-25 下午5:00:05
 * @version V1.0
 */
package com.dkhs.portfolio.net;

import com.lidroid.xutils.util.LogUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Iterator;

/**
 * @author zjz
 * @version 1.0
 * @ClassName ErrorBundle
 * @Description TODO(这里用一句话描述这个类的作用)
 * @date 2014-9-25 下午5:00:05
 */
public class ErrorBundle {

    public static final String KEY_ERROR = "errors";

    private String errorKey;
    private int errorCode;
    private String errorMessage;

    public String getErrorKey() {
        return errorKey;
    }

    public void setErrorKey(String errorKey) {
        this.errorKey = errorKey;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public int getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(int errorCode) {
        this.errorCode = errorCode;
    }


    public static boolean isContainError(String result) throws JSONException {
        if (result.startsWith("{")) {
            JSONObject jsonObject = new JSONObject(result);

            if (jsonObject.has(KEY_ERROR)) {
                return true;
            }

        }

        return false;
    }


    public static ErrorBundle parseToErrorBundle(String errMsg) {
        ErrorBundle errorBundle = new ErrorBundle();
        try {
            JSONObject errorJson = new JSONObject(errMsg);
            if (errorJson.has(ErrorBundle.KEY_ERROR)) {
                JSONObject eJObject = errorJson.optJSONObject(ErrorBundle.KEY_ERROR);
                Iterator keyIter = eJObject.keys();
                String key = "";
                while (keyIter.hasNext()) {
                    key = (String) keyIter.next();
                    break;
                }
                JSONArray eJArray = eJObject.optJSONArray(key);
                if (eJArray.length() > 0) {
                    String errorTExt = eJArray.getString(0);
                    LogUtils.e("setErrorMessage : " + errorTExt);
                    errorBundle.setErrorMessage(eJArray.getString(0));
                    errorBundle.setErrorKey(key);
                }
            }

        } catch (JSONException e) {
            errorBundle.setErrorMessage("请求数据失败");
            e.printStackTrace();
        }
        return errorBundle;
    }

}
