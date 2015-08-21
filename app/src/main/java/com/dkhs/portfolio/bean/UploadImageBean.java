package com.dkhs.portfolio.bean;

/**
 * Created by zhangcm on 2015/7/27.15:44
 */
public class UploadImageBean {
    private String id;
    private String image_xs;
    private String image_sm;
    private String image_md;
    private String image_lg;
    private String full_path;
    private String media_type;
    private int size;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getImage_xs() {
        return image_xs;
    }

    public void setImage_xs(String image_xs) {
        this.image_xs = image_xs;
    }

    public String getImage_lg() {
        return image_lg;
    }

    public void setImage_lg(String image_lg) {
        this.image_lg = image_lg;
    }

    public String getFull_path() {
        return full_path;
    }

    public void setFull_path(String full_path) {
        this.full_path = full_path;
    }

    public String getMedia_type() {
        return media_type;
    }

    public void setMedia_type(String media_type) {
        this.media_type = media_type;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public String getImage_sm() {
        return image_sm;
    }

    public void setImage_sm(String image_sm) {
        this.image_sm = image_sm;
    }
}
