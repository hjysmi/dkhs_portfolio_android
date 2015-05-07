/**
 * @Title ErrorBundle.java
 * @Package com.dkhs.portfolio.bean.errorbundle
 * @Description TODO(用一句话描述该文件做什么)
 * @author zjz
 * @date 2014-12-2 上午11:19:41
 * @version V1.0
 */
package com.dkhs.portfolio.bean.errorbundle;

/**
 * @ClassName ErrorBundle
 * @Description TODO(这里用一句话描述这个类的作用)
 * @author zjz
 * @date 2014-12-2 上午11:19:41
 * @version 1.0
 */
public class BaseError<T> {
    private T errors;

    public T getErrors() {
        return errors;
    }

    public void setErrors(T errors) {
        this.errors = errors;
    }
}
