package com.dkhs.portfolio.bean;

import com.google.gson.annotations.SerializedName;

public class OptionNewsBean {
	
	String id;
    String title;
    String description;
    String text;
	@SerializedName("comment_root")
	String commentRoot;
    @SerializedName("retweet_status")
    String retweetStatus;
    @SerializedName("parent_comment")
    String parentComment;
    String source;
    @SerializedName("first_pic")
    String firstPic;
    @SerializedName("retweet_count")
    String retweetCount;
    @SerializedName("reply_count")
    String replyCount;
    @SerializedName("fav_count")
    String favCount;
    @SerializedName("created_at")
    String createdTime;
    @SerializedName("modified_at")
    String modifiedTime;
    @SerializedName("status_type")
    String statuType;
    @SerializedName("content_type")
    String contentType; 
    @SerializedName("at_users")
    String[] atUser;
    Symbols symbols;
    User user;
	class User{
		String id;
        String username;
        @SerializedName("is_active")
        boolean active;
        @SerializedName("avatar_xs")
        String headPitureSX;
        @SerializedName("avatar_sm")
        String headPitureSM;
        @SerializedName("avatar_md")
        String headPitureMD;
        @SerializedName("avatar_lg")
        String headPitureLG;
        String description;
        String city;
        String province;
        String gender;
        int[] category;
        @SerializedName("followed_by_count")
        String followedCount;
        @SerializedName("friends_count")
        String friendCount;
        @SerializedName("status_count")
        String statuCount;
        @SerializedName("symbols_count")
        String symbolCount;
		public String getId() {
			return id;
		}
		public void setId(String id) {
			this.id = id;
		}
		public String getUsername() {
			return username;
		}
		public void setUsername(String username) {
			this.username = username;
		}
		public boolean isActive() {
			return active;
		}
		public void setActive(boolean active) {
			this.active = active;
		}
		public String getHeadPitureSX() {
			return headPitureSX;
		}
		public void setHeadPitureSX(String headPitureSX) {
			this.headPitureSX = headPitureSX;
		}
		public String getHeadPitureSM() {
			return headPitureSM;
		}
		public void setHeadPitureSM(String headPitureSM) {
			this.headPitureSM = headPitureSM;
		}
		public String getHeadPitureMD() {
			return headPitureMD;
		}
		public void setHeadPitureMD(String headPitureMD) {
			this.headPitureMD = headPitureMD;
		}
		public String getHeadPitureLG() {
			return headPitureLG;
		}
		public void setHeadPitureLG(String headPitureLG) {
			this.headPitureLG = headPitureLG;
		}
		public String getDescription() {
			return description;
		}
		public void setDescription(String description) {
			this.description = description;
		}
		public String getCity() {
			return city;
		}
		public void setCity(String city) {
			this.city = city;
		}
		public String getProvince() {
			return province;
		}
		public void setProvince(String province) {
			this.province = province;
		}
		public String getGender() {
			return gender;
		}
		public void setGender(String gender) {
			this.gender = gender;
		}
		public int[] getCategory() {
			return category;
		}
		public void setCategory(int[] category) {
			this.category = category;
		}
		public String getFollowedCount() {
			return followedCount;
		}
		public void setFollowedCount(String followedCount) {
			this.followedCount = followedCount;
		}
		public String getFriendCount() {
			return friendCount;
		}
		public void setFriendCount(String friendCount) {
			this.friendCount = friendCount;
		}
		public String getStatuCount() {
			return statuCount;
		}
		public void setStatuCount(String statuCount) {
			this.statuCount = statuCount;
		}
		public String getSymbolCount() {
			return symbolCount;
		}
		public void setSymbolCount(String symbolCount) {
			this.symbolCount = symbolCount;
		}
        
	}
	class Symbols{
		String id;
		String symbol;
        @SerializedName("abbr_name")
        String abbrName;
		public String getId() {
			return id;
		}
		public void setId(String id) {
			this.id = id;
		}
		public String getSymbol() {
			return symbol;
		}
		public void setSymbol(String symbol) {
			this.symbol = symbol;
		}
		public String getAbbrName() {
			return abbrName;
		}
		public void setAbbrName(String abbrName) {
			this.abbrName = abbrName;
		}
        
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getText() {
		return text;
	}
	public void setText(String text) {
		this.text = text;
	}
	public String getCommentRoot() {
		return commentRoot;
	}
	public void setCommentRoot(String commentRoot) {
		this.commentRoot = commentRoot;
	}
	public String getRetweetStatus() {
		return retweetStatus;
	}
	public void setRetweetStatus(String retweetStatus) {
		this.retweetStatus = retweetStatus;
	}
	public String getParentComment() {
		return parentComment;
	}
	public void setParentComment(String parentComment) {
		this.parentComment = parentComment;
	}
	public String getSource() {
		return source;
	}
	public void setSource(String source) {
		this.source = source;
	}
	public String getFirstPic() {
		return firstPic;
	}
	public void setFirstPic(String firstPic) {
		this.firstPic = firstPic;
	}
	public String getRetweetCount() {
		return retweetCount;
	}
	public void setRetweetCount(String retweetCount) {
		this.retweetCount = retweetCount;
	}
	public String getReplyCount() {
		return replyCount;
	}
	public void setReplyCount(String replyCount) {
		this.replyCount = replyCount;
	}
	public String getFavCount() {
		return favCount;
	}
	public void setFavCount(String favCount) {
		this.favCount = favCount;
	}
	public String getCreatedTime() {
		return createdTime;
	}
	public void setCreatedTime(String createdTime) {
		this.createdTime = createdTime;
	}
	public String getModifiedTime() {
		return modifiedTime;
	}
	public void setModifiedTime(String modifiedTime) {
		this.modifiedTime = modifiedTime;
	}
	public String getStatuType() {
		return statuType;
	}
	public void setStatuType(String statuType) {
		this.statuType = statuType;
	}
	public String getContentType() {
		return contentType;
	}
	public void setContentType(String contentType) {
		this.contentType = contentType;
	}
	public String[] getAtUser() {
		return atUser;
	}
	public void setAtUser(String[] atUser) {
		this.atUser = atUser;
	}
	public Symbols getSymbols() {
		return symbols;
	}
	public void setSymbols(Symbols symbols) {
		this.symbols = symbols;
	}
	public User getUser() {
		return user;
	}
	public void setUser(User user) {
		this.user = user;
	}
	
}
