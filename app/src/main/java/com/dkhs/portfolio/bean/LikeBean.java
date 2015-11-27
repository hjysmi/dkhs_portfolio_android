package com.dkhs.portfolio.bean;

import com.mingle.autolist.AutoData;

import java.util.List;

/**
 * @author zwm
 * @version 2.0
 * @ClassName LikeBean
 * @Description TODO(这里用一句话描述这个类的作用)
 * @date 2015/8/3.
 */
public class LikeBean extends AutoData {


    public int id;
    public boolean like;
    public String title;
    public String text;
    public boolean truncated;
    public PeopleBean user;
    public String replied_status;
    public String replied_comment;
    public String retweeted_status;
    public int status_type;
    public int retweets_count;
    public int comments_count;
    public int favorites_count;
    public int attitudes_count;
    public int content_count;
    public String publish_at;
    public String created_at;
    public String modified_at;
    public double lat;
    public double lon;

    public boolean compact = false;
    public List<UploadImageBean> medias;

    public int content_type;
    public int reward_state;
    public int rewarded_type;
    public String reward_amount;
    public String recommend_title;
    public String recommend_desc;
    public String recommend_image_sm;
    public String recommend_image_md;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public boolean isLike() {
        return like;
    }

    public void setLike(boolean like) {
        this.like = like;
    }


    public TopicsBean toTopicsBean() {


        if (this instanceof TopicsBean) {
            return (TopicsBean) this;
        } else {


            TopicsBean topicsBean = new TopicsBean();
            topicsBean.id = id;
            topicsBean.like = like;
            topicsBean.title = title;
            topicsBean.text = text;
            topicsBean.truncated = truncated;
            topicsBean.user = user;
            topicsBean.replied_status = replied_status;
            topicsBean.replied_comment = replied_comment;
            topicsBean.retweeted_status = retweeted_status;
            topicsBean.status_type = status_type;
            topicsBean.retweets_count = retweets_count;
            topicsBean.comments_count = comments_count;
            topicsBean.favorites_count = favorites_count;
            topicsBean.attitudes_count = attitudes_count;
            topicsBean.content_count = content_count;
            topicsBean.publish_at = publish_at;
            topicsBean.created_at = created_at;
            topicsBean.modified_at = modified_at;
            topicsBean.lat = lat;
            topicsBean.lon = lon;
            topicsBean.medias = medias;
            topicsBean.compact = compact;

            return topicsBean;

        }


    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public boolean isTruncated() {
        return truncated;
    }

    public void setTruncated(boolean truncated) {
        this.truncated = truncated;
    }

    public PeopleBean getUser() {
        return user;
    }

    public void setUser(PeopleBean user) {
        this.user = user;
    }

    public String getReplied_status() {
        return replied_status;
    }

    public void setReplied_status(String replied_status) {
        this.replied_status = replied_status;
    }

    public String getReplied_comment() {
        return replied_comment;
    }

    public void setReplied_comment(String replied_comment) {
        this.replied_comment = replied_comment;
    }

    public String getRetweeted_status() {
        return retweeted_status;
    }

    public void setRetweeted_status(String retweeted_status) {
        this.retweeted_status = retweeted_status;
    }

    public int getStatus_type() {
        return status_type;
    }

    public void setStatus_type(int status_type) {
        this.status_type = status_type;
    }

    public int getRetweets_count() {
        return retweets_count;
    }

    public void setRetweets_count(int retweets_count) {
        this.retweets_count = retweets_count;
    }

    public int getComments_count() {
        return comments_count;
    }

    public void setComments_count(int comments_count) {
        this.comments_count = comments_count;
    }

    public int getFavorites_count() {
        return favorites_count;
    }

    public void setFavorites_count(int favorites_count) {
        this.favorites_count = favorites_count;
    }

    public int getAttitudes_count() {
        return attitudes_count;
    }

    public void setAttitudes_count(int attitudes_count) {
        this.attitudes_count = attitudes_count;
    }

    public int getContent_count() {
        return content_count;
    }

    public void setContent_count(int content_count) {
        this.content_count = content_count;
    }

    public String getPublish_at() {
        return publish_at;
    }

    public void setPublish_at(String publish_at) {
        this.publish_at = publish_at;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public String getModified_at() {
        return modified_at;
    }

    public void setModified_at(String modified_at) {
        this.modified_at = modified_at;
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLon() {
        return lon;
    }

    public void setLon(double lon) {
        this.lon = lon;
    }

    public boolean isCompact() {
        return compact;
    }

    public void setCompact(boolean compact) {
        this.compact = compact;
    }

    public List<UploadImageBean> getMedias() {
        return medias;
    }

    public void setMedias(List<UploadImageBean> medias) {
        this.medias = medias;
    }

    @Override
    public String getIdentifies() {
        return id + "";
    }
}
