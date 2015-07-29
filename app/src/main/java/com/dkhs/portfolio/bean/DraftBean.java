package com.dkhs.portfolio.bean;

import com.lidroid.xutils.db.annotation.Column;

/**
 * Created by zjz on 2015/7/23.
 */
public class DraftBean {

    private int id;

    public static final String COLUM_UTCTIME = "utctime";
    public static final String COLUM_IMAGEURI = "imageUri";
    public static final String COLUM_TITLE = "title";
    public static final String COLUM_CONTENTE = "content";
    public static final String COLUM_AUTHORID = "authorId";
    public static final String COLUM_LABEL = "label";  //1是正文，2 是回复


    @Column(column = COLUM_UTCTIME)
    private String utcTime;

    public String getUtcTime() {
        return utcTime;
    }

    public void setUtcTime(String utcTime) {
        this.utcTime = utcTime;
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

    //保存图片的绝对路径
    @Column(column = COLUM_IMAGEURI)
    private String imageUri;

    @Column(column = COLUM_TITLE)
    private String title;
    @Column(column = COLUM_CONTENTE)
    private String content;
    @Column(column = COLUM_AUTHORID)
    private String authorId;

    public int getLabel() {
        return label;
    }

    public void setLabel(int label) {
        this.label = label;
    }

    @Column(column = COLUM_LABEL)
    private int label;


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }


}
