/**
 * @Title UpdateCombinationEvent.java
 * @Package com.dkhs.portfolio.ui.eventbus
 * @Description TODO(用一句话描述该文件做什么)
 * @author zjz
 * @date 2015-5-6 下午2:46:45
 * @version V1.0
 */
package com.dkhs.portfolio.ui.eventbus;

import com.dkhs.portfolio.bean.CombinationBean;

/**
 * @ClassName UpdateCombinationEvent
 * @Description TODO(这里用一句话描述这个类的作用)
 * @author zjz
 * @date 2015-5-6 下午2:46:45
 * @version 1.0
 */
public class UpdateCombinationEvent {
    public CombinationBean mCombinationBean;

    public UpdateCombinationEvent(CombinationBean comBean) {
        this.mCombinationBean = comBean;
    }
}
