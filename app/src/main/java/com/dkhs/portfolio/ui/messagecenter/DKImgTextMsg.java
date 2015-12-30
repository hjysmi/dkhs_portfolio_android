package com.dkhs.portfolio.ui.messagecenter;

import android.os.Parcel;
import android.text.TextUtils;

import com.dkhs.portfolio.net.DataParse;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;

import io.rong.common.ParcelUtils;
import io.rong.imlib.MessageTag;
import io.rong.imlib.model.MessageContent;
import io.rong.imlib.model.UserInfo;

/**
 * @author zwm
 * @version 1.0
 * @ClassName CustomerMessage
 * @date 2015/5/13.11:26
 * @Description TODO(这里用一句话描述这个类的作用)
 */
@MessageTag(value = "DK:ImgTextMsg", flag = MessageTag.ISCOUNTED | MessageTag.ISPERSISTED)
public class DKImgTextMsg extends MessageContent {


    public DKImgTextMsg() {
    }

    /**
     * 构造函数。
     *
     * @param in 初始化传入的 Parcel。
     */
    public DKImgTextMsg(Parcel in) {
        setExtra(ParcelUtils.readFromParcel(in));
        setContent(ParcelUtils.readFromParcel(in));
        setTitle(ParcelUtils.readFromParcel(in));
        setImageUri(ParcelUtils.readFromParcel(in));
        setUrl(ParcelUtils.readFromParcel(in));
//        setUserInfo((UserInfo) in.readParcelable(UserInfo.class.getClassLoader()));
        setUserInfo(ParcelUtils.readFromParcel(in, UserInfo.class));

//        UserInfo userInfo= in.readParcelable(UserInfo.class.getClassLoader());
//        setUserInfo(userInfo);

    }

    /**
     * 构建一个文字消息实例。
     *
     * @return 文字消息实例。
     */
    public static DKImgTextMsg obtain(String text) {
        return (DKImgTextMsg)DataParse.parseObjectJson(DKImgTextMsg.class,text);
    }


    /**
     * 构造函数。
     *
     * @param data 初始化传入的二进制数据。
     */
    public DKImgTextMsg(byte[] data) {
        String jsonStr = null;
        try {
            jsonStr = new String(data, "UTF-8");

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        try {
            JSONObject jsonObj = new JSONObject(jsonStr);

            if (jsonObj.has("extra"))
                setExtra(jsonObj.getString("extra"));

            if (jsonObj.has("content"))
                setContent(jsonObj.getString("content"));
            if (jsonObj.has("title"))
                setTitle(jsonObj.getString("title"));
            if (jsonObj.has("url"))
                setUrl(jsonObj.getString("url"));
            if (jsonObj.has("imageUri"))
                setImageUri(jsonObj.getString("imageUri"));
            if(jsonObj.has("userInfo")&& !TextUtils.isEmpty(jsonObj.getString("userInfo")))
                setUserInfo(DataParse.parseObjectJson( UserInfo.class,jsonObj.getString("userInfo")));

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }
    /**
     * 将本地消息对象序列化为消息数据。
     *
     * @return 消息数据。
     */
    @Override
    public byte[] encode() {

        try {
            return DataParse.objectToJson(this).getBytes("UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return  null;


    }


    /**
     * imageUri : http://www.demo.com/1.jpg
     * extra : helloExtra
     * title : hellotitle
     * content : hello
     */
    private String imageUri;
    private String extra;
    private String title;
    private String content;

    private String url;


    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public void setImageUri(String imageUri) {
        this.imageUri = imageUri;
    }

    public void setExtra(String extra) {
        this.extra = extra;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getImageUri() {
        return imageUri;
    }

    public String getExtra() {
        return extra;
    }

    public String getTitle() {
        return title;
    }
    public String getContent() {
        return content;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        ParcelUtils.writeToParcel(dest, getExtra());
        ParcelUtils.writeToParcel(dest, getContent());
        ParcelUtils.writeToParcel(dest, getTitle());
        ParcelUtils.writeToParcel(dest, getImageUri());
        ParcelUtils.writeToParcel(dest, getUrl());
//        ParcelUtils.writeToParcel(dest, getUserInfo());
        dest.writeParcelable(getUserInfo(), flags);  ;
//        dest.writeParcelable(getUserInfo(), flags);

    }

    /**
     * 读取接口，目的是要从Parcel中构造一个实现了Parcelable的类的实例处理。
     */
    public static final Creator<DKImgTextMsg> CREATOR = new Creator<DKImgTextMsg>() {
        @Override
        public DKImgTextMsg createFromParcel(Parcel source) {
            return new DKImgTextMsg(source);
        }

        @Override
        public DKImgTextMsg[] newArray(int size) {
            return new DKImgTextMsg[size];
        }
    };


}
