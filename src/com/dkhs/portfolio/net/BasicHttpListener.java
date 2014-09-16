package com.dkhs.portfolio.net;

import org.json.JSONException;
import org.json.JSONObject;

import android.widget.Toast;

import com.dkhs.portfolio.app.PortfolioApplication;
import com.lidroid.xutils.util.LogUtils;

public abstract class BasicHttpListener implements IHttpListener {

    @Override
    public void onHttpSuccess(String result) {
        if (result.startsWith("{")) {
            try {
                JSONObject jsonObject = new JSONObject(result);

                if (jsonObject.has("errors")) {
                    onFailure(777, result);
                    return;
                }
            } catch (JSONException e) {

                e.printStackTrace();
            }
        }

        onSuccess(result);

    }

    @Override
    public void onHttpFailure(int errCode, String errMsg) {
        onFailure(errCode, errMsg);
    }

    @Override
    public void onHttpFailure(int errCode, Throwable err) {
        onFailure(errCode, err.getMessage());
    }

    /**
     * 
     * 
     * @Title: onSuccess
     * @Description:
     * errorCode已验证成功，不需要在验证，可从{@code result} 中直接获取数据<br>
     * errorCode验证不成功处理逻辑在 {@link #onFailure(int, String)}中处理
     * @param @param jsonObject 设定文件
     * @return void 返回类型
     */
    public abstract void onSuccess(String result);

    /**
     * 
     * 
     * @Title: onFailure
     * @Description: 网络错误处理，
     * @param errCode 错误编码，具体查看 {@link Network.HttpCode}
     * @param errMsg 错误信息
     * @return void 返回类型
     */
    public void onFailure(int errCode, String errMsg) {
        // if(showDialogIfIsTokenCode(errCode)){
        // GTGUtils.showTip(HttpCode.getCodeResId(errCode));
        // }
        Toast.makeText(PortfolioApplication.getInstance(), errMsg, Toast.LENGTH_SHORT).show();
        LogUtils.e("Error code :" + errCode + ",message : " + errMsg);

    }

    /**
     * 判断是否是 Token失效的错误码，如果是则显示Token失效对话框
     * 
     * @param errCode 服务端提供的错误码
     * @return true 显示Token失效对话框，false 不做任何操作
     */
    protected boolean showDialogIfIsTokenCode(int errCode) {

        return false;
    }

}
