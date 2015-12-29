package com.dkhs.portfolio.ui.eventbus;

/**
 * Created by wuyongsen on 2015/12/29.
 */
public class PreLoadEvent {
    public final static int TYPE_REWARD = 0;
    public final static int TYPE_MARKET = 1;
    public int type;
    public PreLoadEvent(int type){
        this.type = type;
    }
}
