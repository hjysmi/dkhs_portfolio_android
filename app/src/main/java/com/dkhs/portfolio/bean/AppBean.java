package com.dkhs.portfolio.bean;

import com.google.gson.annotations.SerializedName;

public class AppBean {
	String id;
	@SerializedName("app_code")
	String appCode;
	@SerializedName("app_name")
	String appName;
	String type;
	String url;
	String version;
	@SerializedName("force_upgrade")
	boolean upgrade;
	String desc;
	@SerializedName("created_at")
	String createDate;
	@SerializedName("modified_at")
	String modified;
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getAppCode() {
		return appCode;
	}
	public void setAppCode(String appCode) {
		this.appCode = appCode;
	}
	public String getAppName() {
		return appName;
	}
	public void setAppName(String appName) {
		this.appName = appName;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public String getVersion() {
		return version;
	}
	public void setVersion(String version) {
		this.version = version;
	}
	public boolean isUpgrade() {
		return upgrade;
	}
	public void setUpgrade(boolean upgrade) {
		this.upgrade = upgrade;
	}
	public String getDesc() {
		return desc;
	}
	public void setDesc(String desc) {
		this.desc = desc;
	}
	public String getCreateDate() {
		return createDate;
	}
	public void setCreateDate(String createDate) {
		this.createDate = createDate;
	}
	public String getModified() {
		return modified;
	}
	public void setModified(String modified) {
		this.modified = modified;
	}

	public boolean isNewVersion(String  versionName){


		boolean isNewVersion=false;
		String[] serviceVersion = this.getVersion().split("\\.");
		String[] oldVersion = versionName.split("\\.");

		if(serviceVersion.length>=3 && oldVersion.length >=3){
			for (int i = 0; i < 3; i++) {
				//先判断是否是数字
				if(!serviceVersion[i].matches("\\d+") || !oldVersion[i].matches("\\d+")){
					isNewVersion=false;
					break;
				}else
				if(serviceVersion[i].hashCode() >oldVersion[i].hashCode()){
					isNewVersion=true;
					break;
				}else if(serviceVersion[i].hashCode() <oldVersion[i].hashCode()){
					isNewVersion=false;
					break;
				}
			}
		}
		return  isNewVersion;

	}
	
}
