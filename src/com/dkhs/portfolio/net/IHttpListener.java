package com.dkhs.portfolio.net;

public interface IHttpListener {


    public void beforeRequest();

    public void requestCallBack();

    public void stopRequest(boolean isStop);
    public boolean isStopRequest();

    /**
     * 
     * 
     * @Title: onHttpSuccess
     * @Description: 网络数据请求成功
     * @param @param jsonObject 设定文件
     * @return void 返回类型
     */
    public void onHttpSuccess(String jsonObject);

    /**
     * 
     * 
     * @Title: onHttpFailure
     * @Description: 网络数据请求错误
     * @param @param errCode
     * @param @param errMsg 设定文件
     * @return void 返回类型
     */
    public void onHttpFailure(int errCode, String errMsg);

    /**
     * 
     * 
     * @Title: onHttpFailure
     * @Description: 网络数据请求错误
     * @param @param errCode
     * @param @param err 设定文件
     * @return void 返回类型
     */
    public void onHttpFailure(int errCode, Throwable err);
}
