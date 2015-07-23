package com.dkhs.portfolio.net;

import com.dkhs.portfolio.R;
import com.dkhs.portfolio.utils.PromptManager;
import com.lidroid.xutils.util.LogUtils;

import org.json.JSONException;

public abstract class BasicHttpListener implements IHttpListener {

    private boolean isStop;


    @Override
    public void beforeRequest() {

    }

    @Override
    public void requestCallBack() {

    }

    /**
     * @return
     * @Title
     * @Description TODO: (用一句话描述这个方法的功能)
     */
    @Override
    public boolean isStopRequest() {

        return isStop;
    }

    /**
     * @param isStop
     * @return
     * @Title
     * @Description:是否中断请求
     */
    @Override
    public void stopRequest(boolean isStop) {
        this.isStop = isStop;

    }

    @Override
    public void onHttpSuccess(String result) {
        try {
            if (ErrorBundle.isContainError(result)) {

                onFailure(777, result);
                return;
            }


        } catch (JSONException e) {

            e.printStackTrace();
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
     * @param @param jsonObject 设定文件
     * @return void 返回类型
     * @Title: onSuccess
     * @Description: errorCode已验证成功，不需要在验证，可从{@code result} 中直接获取数据<br>
     * errorCode验证不成功处理逻辑在 {@link #onFailure(int, String)}中处理
     */
    public abstract void onSuccess(String result);

    /**
     * @param errCode 错误编码，具体查看
     * @param errMsg  错误信息
     * @return void 返回类型
     * @Title: onFailure
     * @Description: 网络错误处理，
     */
    public void onFailure(int errCode, String errMsg) {
        // if(showDialogIfIsTokenCode(errCode)){
        // GTGUtils.showTip(HttpCode.getCodeResId(errCode));
        // }
        // Toast.makeText(PortfolioApplication.getInstance(), errMsg, Toast.LENGTH_SHORT).show();
        LogUtils.e("Error code :" + errCode + ",message : " + errMsg);
        if (errCode == 0) {
            PromptManager.showToast(R.string.message_timeout);
        } else if (errCode == 500 || errCode == 404) { // 服务器内部错误
            PromptManager.showToast(R.string.message_server_error);
        } else if (errCode == 429) {
            PromptManager.showToast(R.string.message_server_error429);

        } else if (errCode == 777) { // 服务器正确响应，错误参数需要提示用户
            onFailure(ErrorBundle.parseToErrorBundle(errMsg));
        }

    }


    public void onFailure(ErrorBundle errorBundle) {
        if (null != errorBundle) {
            PromptManager.showToast(errorBundle.getErrorMessage());
        }
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
