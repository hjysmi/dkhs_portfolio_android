package com.dkhs.portfolio.ui.eventbus;

/**
 * @author zwm
 * @version 2.0
 * @ClassName TopicsDetailRefreshEvent
 * @Description TODO(这里用一句话描述这个类的作用)
 * @date 2015/7/29.
 */
public class RewardDetailRefreshEvent {
    private int errCode;
    public RewardDetailRefreshEvent(int errCode){
        this.errCode = errCode;
    }
}
