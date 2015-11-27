package com.dkhs.portfolio.bean;

import java.util.List;

/**
 * Created by xuetong on 2015/11/26.
 */
public class RecommendRewardBean {
     List<TopicsBean> topicsBeans;
    public RecommendRewardBean(List<TopicsBean> topicsBeans) {
        this.topicsBeans = topicsBeans;
    }

    public List<TopicsBean> getTopicsBeans() {
        return topicsBeans;
    }
}
