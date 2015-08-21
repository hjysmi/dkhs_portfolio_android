package com.dkhs.portfolio.ui.eventbus;

import com.dkhs.portfolio.bean.TopicsBean;

/**
 * @author zwm
 * @version 2.0
 * @ClassName UpdateTopicsListEvent
 * @Description TODO(这里用一句话描述这个类的作用)
 * @date 2015/7/30.
 */
public class AddTopicsEvent {

    public TopicsBean topicsBean;

    public AddTopicsEvent(TopicsBean topicsBean) {
        this.topicsBean = topicsBean;
    }
}
