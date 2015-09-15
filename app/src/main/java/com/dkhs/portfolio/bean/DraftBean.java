package com.dkhs.portfolio.bean;

import android.text.Html;
import android.text.TextUtils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.lidroid.xutils.db.annotation.Column;

import org.parceler.Parcel;

import java.lang.reflect.Type;
import java.util.ArrayList;

/**
 * Created by zjz on 2015/7/23.
 */
@Parcel
public class DraftBean {

    int id;

    public static final String COLUM_EDITTIME = "edittime";
    //    public static final String COLUM_IMAGEURI = "imageUri";
//    public static final String COLUM_IMAGE_FILEPATH = "image_filepath";
    public static final String COLUM_TITLE = "title";
    public static final String COLUM_CONTENTE = "content";
    public static final String COLUM_AUTHORID = "authorId";
    public static final String COLUM_LABEL = "label";  //1是正文，2 是回复
    public static final String COLUM_REPLY_USERNAME = "reply_username";  //评论用户名
    public static final String COLUM_REPLY_STATUS_ID = "reply_status_id";  //评论用户名
    public static final String COLUM_FAIL_REASON = "fail_reason";  //失败原因
    //    public static final String COLUM_PHOTO_LIST = "photo_list";
    public static final String COLUM_PHOTO_PATHS = "photo_paths";

//    //保存图片的绝对路径
//    @Column(column = COLUM_PHOTO_PATHS)
//    String imageUri;

    @Column(column = COLUM_TITLE)
    String title;
    @Column(column = COLUM_CONTENTE)
    String content;

    @Column(column = COLUM_AUTHORID)
    String authorId;

    @Column(column = COLUM_FAIL_REASON)
    String failReason;

    @Column(column = COLUM_LABEL)
    int label;
    @Column(column = COLUM_EDITTIME)
    long edittime;

    @Column(column = COLUM_REPLY_USERNAME)
    String statusId;

    @Column(column = COLUM_REPLY_STATUS_ID)
    String replyUserName;
    @Column(column = COLUM_PHOTO_PATHS)
    String photoPaths;

    ArrayList<String> photoList;

    public String getPhotoPaths() {
        return photoPaths;
    }

    public void setPhotoPaths(String photoPaths) {
        this.photoPaths = photoPaths;
    }


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


    public long getEdittime() {
        return edittime;
    }

    public void setEdittime(long edittime) {
        this.edittime = edittime;
    }
//
//    public String getImageUri() {
//        return imageUri;
//    }
//
//    public void setImageUri(String imageUri) {
//        this.imageUri = imageUri;
//    }

    public String getSimpleTitle() {
        return Html.fromHtml(title).toString();
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSimpleContent() {
        return Html.fromHtml(content).toString();
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


//    public String getImageFilepath() {
//        return imageFilepath;
//    }
//
//    public void setImageFilepath(String imageFilepath) {
//        this.imageFilepath = imageFilepath;
//    }
//

    public String getFailReason() {
        return failReason;
    }

    public void setFailReason(String failReason) {
        this.failReason = failReason;
    }




    public ArrayList<String> getPhotoList() {

        try {
            if (!TextUtils.isEmpty(photoPaths)) {
                Gson gson = new GsonBuilder().enableComplexMapKeySerialization().create();
                Type type = new TypeToken<ArrayList<String>>() {
                }.getType();
                return gson.fromJson(photoPaths, type);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new ArrayList<>();
    }

    public void setPhotoList(ArrayList<String> photoList) {
        this.photoList = photoList;
        this.photoPaths = new Gson().toJson(photoList);

    }
//    public String getHtmlContent() {
//        return htmlContent;
//    }
//
//    public void setHtmlContent(String htmlContent) {
//        this.htmlContent = htmlContent;
//    }


}
