package com.dkhs.portfolio.bean;

import org.parceler.Parcel;

import java.util.List;

/**
 * @author zwm
 * @version 2.0
 * className TopcsBean
 *   TODO(这里用一句话描述这个类的作用)
 * date 2015/7/16.
 */
@Parcel
public class TopicsBean {


    public boolean star;
    /**
     * lon : null
     * text : 测试一下哦
     * retweeted_status : null
     * comments_count : 11
     * recommend_level : 1
     * attitudes_count : 0
     * state : 1
     * retweets_count : 1
     * truncated : false
     * publish_at : null
     * id : 1846624
     * replied_comment : null
     * favorites_count : 0
     * title : 测试一下哦
     * symbols : []
     * source : null
     * status_type : 0
     * medias : []
     * created_at : 2015-07-22T07:01:51Z
     * replied_status : null
     * content_type : 0
     * user : {"is_active":true,"portfolios_count":8,"avatar_sm":"","portfolios_following_count":6,"avatar_md":"","following_count":0,"avatar_xs":"","symbols_count":13,"city":null,"followed_by_count":4,"id":1,"status_count":0,"username":"root","category":[],"date_joined":"2014-12-08T09:00:32Z","friends_count":0,"description":null,"avatar_lg":"","province":null,"gender":null,"followers_count":4}
     * lat : null
     * modified_at : 2015-07-22T07:03:55Z
     */
    public String lon;
    public String text;
    public String retweeted_status;
    public int comments_count;
    public int recommend_level;
    public int attitudes_count;
    public int state;
    public int retweets_count;
    public boolean truncated;
    public String publish_at;
    public int id;
    public String replied_comment;
    public int favorites_count;
    public String title;
    public List<String> symbols;
    public String source;
    public int status_type;
    public List<String> medias;
    public String created_at;
    public String replied_status;
    public int content_type;

    public PeopleBean user;
    public String lat;
    public String modified_at;

}
