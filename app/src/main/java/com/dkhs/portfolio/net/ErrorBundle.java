/**
 * @Title ErrorBundle.java
 * @Package com.dkhs.portfolio.net
 * @Description TODO(用一句话描述该文件做什么)
 * @author zjz
 * @date 2014-9-25 下午5:00:05
 * @version V1.0
 */
package com.dkhs.portfolio.net;

/**
 * @ClassName ErrorBundle
 * @Description TODO(这里用一句话描述这个类的作用)
 * @author zjz
 * @date 2014-9-25 下午5:00:05
 * @version 1.0
 */
public class ErrorBundle {
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
}
