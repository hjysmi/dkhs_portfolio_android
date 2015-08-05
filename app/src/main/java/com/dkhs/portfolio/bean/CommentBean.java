package com.dkhs.portfolio.bean;

import java.util.List;

/**
 * Created by zhangcm on 2015/7/28.17:18
 */
public class CommentBean extends LikeBean {
    private String title;
    private String text;
    private boolean truncated;
    private UserEntity user;
    private String replied_status;
    private String replied_comment;
    private String retweeted_status;
    private int status_type;
    private String source;
    private int retweets_count;
    private int comments_count;
    private int favorites_count;
    private int attitudes_count;
    private int content_count;
    private String publish_at;
    private String created_at;
    private String modified_at;
    private double lat;
    private double lon;

    public boolean compact=true;
    private List<UploadImageBean> medias;
    private List<OptionNewsBean.Symbols> symbol;
    private String recomment_level;




    public int getAttitudes_count() {
        return attitudes_count;
    }

    public void setAttitudes_count(int attitudes_count) {
        this.attitudes_count = attitudes_count;
    }

    public int getComments_count() {
        return comments_count;
    }

    public void setComments_count(int comments_count) {
        this.comments_count = comments_count;
    }

    public int getContent_count() {
        return content_count;
    }

    public void setContent_count(int content_count) {
        this.content_count = content_count;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public int getFavorites_count() {
        return favorites_count;
    }

    public void setFavorites_count(int favorites_count) {
        this.favorites_count = favorites_count;
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

    public List<UploadImageBean> getMedias() {
        return medias;
    }

    public void setMedias(List<UploadImageBean> medias) {
        this.medias = medias;
    }

    public String getModified_at() {
        return modified_at;
    }

    public void setModified_at(String modified_at) {
        this.modified_at = modified_at;
    }

    public String getPublish_at() {
        return publish_at;
    }

    public void setPublish_at(String publish_at) {
        this.publish_at = publish_at;
    }

    public String getRecomment_level() {
        return recomment_level;
    }

    public void setRecomment_level(String recomment_level) {
        this.recomment_level = recomment_level;
    }

    public String getReplied_comment() {
        return replied_comment;
    }

    public void setReplied_comment(String replied_comment) {
        this.replied_comment = replied_comment;
    }

    public String getReplied_status() {
        return replied_status;
    }

    public void setReplied_status(String replied_status) {
        this.replied_status = replied_status;
    }

    public String getRetweeted_status() {
        return retweeted_status;
    }

    public void setRetweeted_status(String retweeted_status) {
        this.retweeted_status = retweeted_status;
    }

    public int getRetweets_count() {
        return retweets_count;
    }

    public void setRetweets_count(int retweets_count) {
        this.retweets_count = retweets_count;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public int getStatus_type() {
        return status_type;
    }

    public void setStatus_type(int status_type) {
        this.status_type = status_type;
    }

    public List<OptionNewsBean.Symbols> getSymbol() {
        return symbol;
    }

    public void setSymbol(List<OptionNewsBean.Symbols> symbol) {
        this.symbol = symbol;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public boolean isTruncated() {
        return truncated;
    }

    public void setTruncated(boolean truncated) {
        this.truncated = truncated;
    }

    public UserEntity getUser() {
        return user;
    }

    public void setUser(UserEntity user) {
        this.user = user;
    }
}
