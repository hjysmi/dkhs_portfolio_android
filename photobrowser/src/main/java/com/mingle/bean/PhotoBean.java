package com.mingle.bean;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * @author zwm
 * @version 2.0
 * @ClassName PhotoBean
 * @Description TODO(这里用一句话描述这个类的作用)
 * @date 2015/7/30.
 */
public class PhotoBean implements Parcelable {

    public String loadingURl;
    public int failure;
    public String title;
    public String imgUrl;


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.loadingURl);
        dest.writeInt(this.failure);
        dest.writeString(this.title);
        dest.writeString(this.imgUrl);
    }

    public PhotoBean() {
    }

    private PhotoBean(Parcel in) {
        this.loadingURl = in.readString();
        this.failure = in.readInt();
        this.title = in.readString();
        this.imgUrl = in.readString();
    }

    public static final Creator<PhotoBean> CREATOR = new Creator<PhotoBean>() {
        public PhotoBean createFromParcel(Parcel source) {
            return new PhotoBean(source);
        }

        public PhotoBean[] newArray(int size) {
            return new PhotoBean[size];
        }
    };
}
