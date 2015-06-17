package com.dkhs.portfolio.bean;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * 用户
 * @author zhangcm
 *
 */
public class User implements Parcelable{
	private String username;
	private String password;
	private String id;
	private String avatar;
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getAvatar() {
		return avatar;
	}
	public void setAvatar(String avatar) {
		this.avatar = avatar;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getUsername() {
		return username;
	}
	public String getPassword() {
		return password;
	}
	public User(String username, String password) {
		super();
		this.username = username;
		this.password = password;
	}
	
	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(this.username);
		dest.writeString(this.avatar);
		dest.writeString(this.id);
	}
	

    public static final Parcelable.Creator<User> CREATOR
            = new Parcelable.Creator<User>() {
        public User createFromParcel(Parcel in) {
            return new User(in);
        }

        public User[] newArray(int size) {
            return new User[size];
        }
    };
    
    private User(Parcel in) {
        username = in.readString();
        avatar = in.readString();
        id = in.readString();
    }
	@Override
	public int describeContents() {
		// TODO Auto-generated method stub
		return 0;
	}
}
