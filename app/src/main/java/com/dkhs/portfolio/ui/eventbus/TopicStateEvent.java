package com.dkhs.portfolio.ui.eventbus;

/**
 * Created by wuyongsen on 2015/11/4.
 */
public class TopicStateEvent {
    public static final int REWARDING = 0;//悬赏中
    public static final int REWARDED = 2;//已悬赏
    public static final int CLOSED = 1;//关闭悬赏

    public int id;
    public int state;

    public TopicStateEvent(int id,int state){
        this.id = id;
        this.state = state;
    }

}
