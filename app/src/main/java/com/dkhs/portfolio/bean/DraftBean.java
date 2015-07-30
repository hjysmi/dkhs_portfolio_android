package com.dkhs.portfolio.bean;

import com.lidroid.xutils.db.annotation.Column;

import org.parceler.Parcel;

/**
 * Created by zjz on 2015/7/23.
 */
@Parcel
public class DraftBean {

    int id;

    public static final String COLUM_EDITTIME = "edittime";
    public static final String COLUM_IMAGEURI = "imageUri";
    public static final String COLUM_TITLE = "title";
    public static final String COLUM_CONTENTE = "content";
    public static final String COLUM_AUTHORID = "authorId";
    public static final String COLUM_LABEL = "label";  //1是正文，2 是回复
    public static final String COLUM_REPLY_USERNAME = "reply_username";  //评论用户名
    public static final String COLUM_REPLY_STATUS_ID = "reply_status_id";  //评论用户名

    //保存图片的绝对路径
    @Column(column = COLUM_IMAGEURI)
    String imageUri;

    @Column(column = COLUM_TITLE)
    String title;
    @Column(column = COLUM_CONTENTE)
    String content;
    @Column(column = COLUM_AUTHORID)
    String authorId;

    @Column(column = COLUM_LABEL)
    int label;
    @Column(column = COLUM_EDITTIME)
    long edittime;

    public String getStatusId() {
        return statusId;
    }

    public void setStatusId(String statusId) {
        this.statusId = statusId;
    }

    public String getReplyUserName() {
        return replyUserName;
    }

    public void setReplyUserName(String replyUserName) {
        this.replyUserName = replyUserName;
    }

    @Column(column = COLUM_REPLY_USERNAME)
    String statusId;

    @Column(column = COLUM_REPLY_STATUS_ID)
    String replyUserName;

    public long getEdittime() {
        return edittime;
    }

    public void setEdittime(long edittime) {
        this.edittime = edittime;
    }

    public String getImageUri() {
        return imageUri;
    }

    public void setImageUri(String imageUri) {
        this.imageUri = imageUri;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getAuthorId() {
        return authorId;
    }

    public void setAuthorId(String authorId) {
        this.authorId = authorId;
    }


    public int getLabel() {
        return label;
    }

    public void setLabel(int label) {
        this.label = label;
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }


}
