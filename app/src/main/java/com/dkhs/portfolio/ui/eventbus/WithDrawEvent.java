package com.dkhs.portfolio.ui.eventbus;

/**
 * Created by wuyongsen on 2015/10/15.
 */
public class WithDrawEvent {
    /**
     * 0代表成功
     */
    public int errCode;
    public WithDrawEvent(int errCode){
        this.errCode = errCode;
    }
}
