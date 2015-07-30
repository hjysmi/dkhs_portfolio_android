package com.dkhs.portfolio.engine;

import android.text.TextUtils;

import com.dkhs.portfolio.net.DKHSClient;
import com.dkhs.portfolio.net.DKHSUrl;
import com.dkhs.portfolio.net.ParseHttpListener;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.client.HttpRequest;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import java.io.File;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;

public class StatusEngineImpl {

    /**
     * 发表话题
     * 如果当前话题是新的主贴， 那么不需要设置以上任何字段
     * 如果当前话题是直接回复某个话题或者评论， 那么需要设置replied_status
     * 如果当前话题是转发某个主贴， 那么需要设置retweeted_status
     * retweeted_status (field, optional, 当前话题要转发的话题),
     * title (string, optional, 帖子标题),
     * text (string, 正文),
     * lon (float, optional, 发帖的纬度坐标),
     * media_ids (string, optional, 图片文件id列表，以逗号分割, 如123,456,789),
     * lat (float, optional, 发帖的经度坐标),
     * replied_status (field, optional, 当前话题要回复的话题)
     */
    public static void postStatus(String title, String text, String replied_status, String retweeted_status, double lat, double lon, String media_ids, ParseHttpListener listener) {
        RequestParams params = new RequestParams();
        if (!TextUtils.isEmpty(title)) {
            params.addBodyParameter("title", title);
        }
        if (!TextUtils.isEmpty(text)) {
            params.addBodyParameter("text", text);
        }
        if (!TextUtils.isEmpty(replied_status)) {
            params.addBodyParameter("replied_status", replied_status);
        }
        if (!TextUtils.isEmpty(retweeted_status)) {
            params.addBodyParameter("retweeted_status", retweeted_status);
        }
        if (lat != 0) {
            params.addBodyParameter("lat", String.valueOf(lat));
        }
        if (lon != 0) {
            params.addBodyParameter("lon", String.valueOf(lon));
        }
        if (!TextUtils.isEmpty(media_ids)) {
            params.addBodyParameter("media_ids", media_ids);
        }
        DKHSClient.request(HttpRequest.HttpMethod.POST, DKHSUrl.Status.statuses, params, listener);
    }


    public static void uploadImage(File image, ParseHttpListener listener) {
        RequestParams params = new RequestParams();
        params.addBodyParameter("image", image);
        DKHSClient.requestLong(HttpRequest.HttpMethod.POST, DKHSUrl.Status.uploadImage, params, listener);
    }

    /**
     * @param statusId  话题id
     * @param sort      排序
     * @param page
     * @param page_size
     * @param listener
     */
    public static void getComments(String statusId, String sort, int page, int page_size, ParseHttpListener listener) {
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        if (page != 0) {
            NameValuePair valuePair = new BasicNameValuePair("page_size", "" + page_size);
            params.add(valuePair);
        }
        if (page_size != 0) {
            NameValuePair valuePair2 = new BasicNameValuePair("page", "" + page);
            params.add(valuePair2);
        }
        NameValuePair valuePair3 = new BasicNameValuePair("sort", sort);
        DKHSClient.requestByGet(MessageFormat.format(DKHSUrl.Status.get_comments, statusId), null, params,
                listener);
    }

    public static void starTopic(String status, ParseHttpListener listener) {

        RequestParams params = new RequestParams();
        params.addBodyParameter("status", status);
        DKHSClient.request(HttpRequest.HttpMethod.POST, DKHSUrl.BBS.star, params, listener);
    }

    public static void unstarTopic(String status, ParseHttpListener listener) {

        RequestParams params = new RequestParams();
        params.addBodyParameter("status", status);
        DKHSClient.request(HttpRequest.HttpMethod.POST, DKHSUrl.BBS.unstar, params, listener);
    }
}
