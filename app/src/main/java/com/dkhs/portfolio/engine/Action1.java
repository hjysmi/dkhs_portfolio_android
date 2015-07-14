package com.dkhs.portfolio.engine;

/**
 * @author zwm
 * @version 2.0
 * @ClassName Action1
 * @Description TODO(借鉴RxJava 的Action1类)
 * @date 2015/7/14.
 */
public interface Action1<T extends  Object> {

     void call(T o);
}
