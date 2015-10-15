package com.dkhs.portfolio.ui.eventbus;

/**
 * Created by wuyongsen on 2015/10/15.
 */
public class PayResEvent {
    /**
     * 0代表成功
     */
    public int errCode;
    public PayResEvent(int errCode){
        this.errCode = errCode;
    }
}
