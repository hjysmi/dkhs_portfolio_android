/**
 * @Title ConbinationBean.java
 * @Package com.dkhs.portfolio.bean
 * @Description TODO(用一句话描述该文件做什么)
 * @author zjz
 * @date 2014-8-27 下午3:06:15
 * @version V1.0
 */
package com.dkhs.portfolio.bean;

import android.text.TextUtils;

import com.dkhs.portfolio.R;
import com.dkhs.portfolio.app.PortfolioApplication;
import com.dkhs.portfolio.ui.widget.ViewBean.SelectCombinationViewBean;
import com.google.gson.annotations.SerializedName;
import com.lidroid.xutils.db.annotation.NoAutoIncrement;
import com.lidroid.xutils.db.annotation.Transient;

import org.parceler.Parcel;

/**
 * @author zjz
 * @version 1.0
 * @ClassName ConbinationBean
 * @Description TODO(这里用一句话描述这个类的作用)
 * @date 2014-8-27 下午3:06:15
 */
@Parcel
public class CombinationBean extends DragListItem {


    @NoAutoIncrement
    String id;
    String name;
    @Transient
    UserEntity user;
    String description;
    // private float percent;
    // @SerializedName("cumulative")
    // private float addUpValue;
    @SerializedName("net_value")
    float netvalue;
    @SerializedName("created_at")
    String createTime;
    @SerializedName("is_public")
    String ispublic;
    @SerializedName("is_rank")
    String is_rank;//
    boolean ispublics;// 0参与排行/1不排行
    float chng_pct_day;
    float chng_pct_month;
    float chng_pct_week;
    float chng_pct_three_month;
    float cumulative;

    boolean followed;
    int sortId;
    @SerializedName("followers_count")
    int followerCount;

    @SerializedName("alert_settings")
    PortfolioAlertBean alertBean;

    String avatar_sm;
    String recommend_title;

    public CombinationBean() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    // public float getCurrentValue() {
    // return percent;
    // }
    //
    // public void setCurrentValue(float currentValue) {
    // this.percent = currentValue;
    // }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDefDescription() {
        if (TextUtils.isEmpty(description)) {
            return PortfolioApplication.getInstance().getResources().getString(R.string.desc_def_text);
        }
        return description;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    // public float getPercent() {
    // return percent;
    // }
    //
    // public void setPercent(float percent) {
    // this.percent = percent;
    // }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public float getNetvalue() {
        return netvalue;
    }

    public void setNetvalue(float netvalue) {
        this.netvalue = netvalue;
    }

    public String getIspublic() {
        return ispublic;
    }

    public void setIspublic(String ispublic) {
        if (ispublic.equals("0")) {
            setIspublics(true);
        } else {
            setIspublics(false);
        }
        this.ispublic = ispublic;
    }

    public boolean isIspublics() {
        return ispublics;
    }

    public void setIspublics(boolean ispublics) {
        this.ispublics = ispublics;
    }

    // public static CombinationBean parse(ChampionBean cBean) {
    // CombinationBean bean = new CombinationBean();
    // bean.ispublic = cBean.getIs_public();
    // bean.user = cBean.getUser();
    // bean.createTime = cBean.getCreated_at();
    // bean.id = cBean.getId();
    // bean.description = cBean.getDescription();
    // bean.name = cBean.getName();
    // bean.netvalue = cBean.getNet_value();
    // bean.setIspublic(cBean.getIs_public());
    // bean.chng_pct_day = cBean.getChng_pct_day();
    // bean.chng_pct_week = cBean.getChng_pct_week();
    // bean.chng_pct_month = cBean.getChng_pct_month();
    // bean.chng_pct_three_month = cBean.getChng_pct_three_month();
    // return bean;
    // }

    public float getChng_pct_day() {
        return chng_pct_day;
    }

    public void setChng_pct_day(float chng_pct_day) {
        this.chng_pct_day = chng_pct_day;
    }

    public float getChng_pct_month() {
        return chng_pct_month;
    }

    public void setChng_pct_month(float chng_pct_month) {
        this.chng_pct_month = chng_pct_month;
    }

    public float getChng_pct_week() {
        return chng_pct_week;
    }

    public void setChng_pct_week(float chng_pct_week) {
        this.chng_pct_week = chng_pct_week;
    }

    public float getChng_pct_three_month() {
        return chng_pct_three_month;
    }

    public void setChng_pct_three_month(float chng_pct_three_month) {
        this.chng_pct_three_month = chng_pct_three_month;
    }

//    public static long getSerialversionuid() {
//        return serialVersionUID;
//    }

    public boolean isPubilc() {
        return ispublic.equals("0");
    }

    public boolean isRank() {
        return is_rank.equals("0");
    }

    public UserEntity getUser() {
        return user;
    }

    public void setUser(UserEntity user) {
        this.user = user;
    }

    public boolean isFollowed() {
        return followed;
    }

    public void setFollowed(boolean followed) {
        this.followed = followed;
    }

    public int getSortId() {
        return sortId;
    }

    public void setSortId(int sortId) {
        this.sortId = sortId;
    }

    public int getFollowerCount() {
        return followerCount;
    }

    public void setFollowerCount(int followerCount) {
        this.followerCount = followerCount;
    }

    public float getCumulative() {
        return cumulative;
    }

    public void setCumulative(float cumulative) {
        this.cumulative = cumulative;
    }

//    public static class CombinationUser implements Serializable {
//
//        private static final long serialVersionUID = 12894595218L;
//        private String id;
//        private String username;
//
//        public String getId() {
//            return id;
//        }
//
//        public void setId(String id) {
//            this.id = id;
//        }
//
//        public String getUsername() {
//            return username;
//        }
//
//        public void setUsername(String username) {
//            this.username = username;
//        }
//    }

    /**
     * @return
     * @Title
     * @Description TODO: (用一句话描述这个方法的功能)
     */
    @Override
    public String getItemName() {
        // TODO Auto-generated method stub
        return this.name;
    }

    /**
     * @return
     * @Title
     * @Description TODO: (用一句话描述这个方法的功能)
     */
    @Override
    public String getItemDesc() {
        if (null != this.user) {

            return this.user.getUsername();
        }
        return "";
    }

    /**
     * @return
     * @Title
     * @Description TODO: (用一句话描述这个方法的功能)
     */
    @Override
    public String getItemId() {
        return this.id;
    }

    /**
     * @return
     * @Title
     * @Description TODO: (用一句话描述这个方法的功能)
     */
    @Override
    public long getItemSortId() {
        return this.sortId;
    }

    /**
     * @return
     * @Title
     * @Description TODO: (用一句话描述这个方法的功能)
     */
    @Override
    public boolean isItemTixing() {
        return this.alertBean != null;
    }

    public PortfolioAlertBean getAlertBean() {
        return alertBean;
    }

    public void setAlertBean(PortfolioAlertBean alertBean) {
        this.alertBean = alertBean;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this)
            return true; // 如果是自己equals自己的话，这里就可以直接返回true，避免了后面可能的大量比较
        if (obj instanceof CombinationBean){
            CombinationBean other = (CombinationBean) obj;
            return id.equals(other.id) && name.equals(other.name);
        } else if(obj instanceof SelectCombinationViewBean){
            SelectCombinationViewBean combinationViewBean = (SelectCombinationViewBean) obj;
            CombinationBean other = combinationViewBean.getmCombinationBean();
            return id.equals(other.id) && name.equals(other.name);
        }
        return super.equals(obj);
    }

    public String getRecommend_title() {
        return recommend_title;
    }

    public void setRecommend_title(String recommend_title) {
        this.recommend_title = recommend_title;
    }

    public String getAvatar_sm() {
        return avatar_sm;
    }

    public void setAvatar_sm(String avatar_sm) {
        this.avatar_sm = avatar_sm;
    }

    @Override
    public int hashCode() {
        return id.hashCode() + name.hashCode();
    }

}
