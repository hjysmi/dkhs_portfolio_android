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
import java.util.HashMap;

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
    public static final String COLUM_SIMPLE_CONTENT = "simple_content";
    public static final String COLUM_UPLOAD_MAP = "upload_map";


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

    @Column(column = COLUM_UPLOAD_MAP)
    String strUploadmap;

    ArrayList<String> photoList;

    HashMap<String, PostImageBean> uploadMap;

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


    public String getSimpleTitle() {
        if (!TextUtils.isEmpty(simleTitle)) {
            return simleTitle;
        }
        return Html.fromHtml(title).toString();
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSimpleContent() {
        if (!TextUtils.isEmpty(simleContent)) {
            return simleContent;
        }
        return Html.fromHtml(content).toString();
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }


    @Column(column = COLUM_SIMPLE_CONTENT)
    String simleContent;

//    public String getSimleTitle() {
//        return simleTitle;
//    }

    public void setSimpleTitle(String simleTitle) {
        this.simleTitle = simleTitle;
    }

    public String simleTitle;

    public void setSimleContent(String simleContent) {
        this.simleContent = simleContent;
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

    public HashMap<String, PostImageBean> getUploadMap() {

        if (null != uploadMap) {
            return uploadMap;
        }
        try {
            if (!TextUtils.isEmpty(strUploadmap)) {
                Gson gson = new GsonBuilder().enableComplexMapKeySerialization().create();
                Type type = new TypeToken<HashMap<String, PostImageBean>>() {
                }.getType();
                return gson.fromJson(strUploadmap, type);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new HashMap<>();
    }

    public void setUploadMap(HashMap<String, PostImageBean> uploadMap) {
        this.uploadMap = uploadMap;
        this.strUploadmap = new Gson().toJson(uploadMap);

    }

//    public String getHtmlContent() {
//        return htmlContent;
//    }
//
//    public void setHtmlContent(String htmlContent) {
//        this.htmlContent = htmlContent;
//    }


}
