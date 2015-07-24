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

    @Column(column = COLUM_UTCTIME)
    private String utcTime;

    //保存图片的绝对路径
    @Column(column = COLUM_IMAGEURI)
    private String imageUri;

    @Column(column = COLUM_TITLE)
    private String title;
    @Column(column = COLUM_CONTENTE)
    private String content;
    @Column(column = COLUM_AUTHORID)
    private long authorId;


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }


}
