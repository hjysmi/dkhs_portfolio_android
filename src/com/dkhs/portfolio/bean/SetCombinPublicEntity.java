package com.dkhs.portfolio.bean;

import com.google.gson.annotations.SerializedName;

public class SetCombinPublicEntity {
	@SerializedName("created_at")
	String createDate;
	String description;
	String id;
	@SerializedName("net_value")
	String value;
	String name;
	@SerializedName("is_public")
	String ispublic;
	public String getCreateDate() {
		return createDate;
	}
	public void setCreateDate(String createDate) {
		this.createDate = createDate;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getIspublic() {
		return ispublic;
	}
	public void setIspublic(String ispublic) {
		this.ispublic = ispublic;
	}
	
}
