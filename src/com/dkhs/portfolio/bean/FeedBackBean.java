package com.dkhs.portfolio.bean;

import com.google.gson.annotations.SerializedName;


public class FeedBackBean {
	long id;
	long user;
	long app;
	String content;
	String contact;
	String image;
	@SerializedName("created_at")
	String created;
	@SerializedName("modified_at")
	String modified;
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public long getUser() {
		return user;
	}
	public void setUser(long user) {
		this.user = user;
	}
	public long getApp() {
		return app;
	}
	public void setApp(long app) {
		this.app = app;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public String getContact() {
		return contact;
	}
	public void setContact(String contact) {
		this.contact = contact;
	}
	public String getImage() {
		return image;
	}
	public void setImage(String image) {
		this.image = image;
	}
	public String getCreated() {
		return created;
	}
	public void setCreated(String created) {
		this.created = created;
	}
	public String getModified() {
		return modified;
	}
	public void setModified(String modified) {
		this.modified = modified;
	}
	
}
