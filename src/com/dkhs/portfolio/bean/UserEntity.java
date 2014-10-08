package com.dkhs.portfolio.bean;

public class UserEntity {

	/**
	 * 昵称
	 */
	private String username;
	private String mobile;
	private boolean isFirstRegister;
	/**
	 * 性别：0=保密, 1=男, 2=女.
	 */
	private int gender;
	/**
	 * 简介
	 */
	private String description;
	/**
	 * 省份
	 */
	private String province;
	/**
	 * 城市
	 */
	private String city;
	/**
	 * 关注数
	 */
	private int followed_by_count;
	/**
	 * 粉丝数
	 */
	private int friends_count;
	/**
	 * 评论数
	 */
	private int status_count;
	/**
	 * 30x30头像地址
	 */
	private String avatar_xs;
	private String access_token;
	public String getAccess_token() {
		return access_token;
	}
	public void setAccess_token(String access_token) {
		this.access_token = access_token;
	}
	public String getAvatar_xs() {
		return avatar_xs;
	}
	public void setAvatar_xs(String avatar_xs) {
		this.avatar_xs = avatar_xs;
	}
	public int getGender() {
		return gender;
	}
	public void setGender(int gender) {
		this.gender = gender;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getProvince() {
		return province;
	}
	public void setProvince(String province) {
		this.province = province;
	}
	public String getCity() {
		return city;
	}
	public void setCity(String city) {
		this.city = city;
	}
	public int getFollowed_by_count() {
		return followed_by_count;
	}
	public void setFollowed_by_count(int followed_by_count) {
		this.followed_by_count = followed_by_count;
	}
	public int getFriends_count() {
		return friends_count;
	}
	public void setFriends_count(int friends_count) {
		this.friends_count = friends_count;
	}
	public int getStatus_count() {
		return status_count;
	}
	public void setStatus_count(int status_count) {
		this.status_count = status_count;
	}
	public boolean isFirstRegister() {
		return isFirstRegister;
	}
	public void setFirstRegister(boolean isFirstRegister) {
		this.isFirstRegister = isFirstRegister;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getMobile() {
		return mobile;
	}
	public void setMobile(String mobile) {
		this.mobile = mobile;
	}
}
