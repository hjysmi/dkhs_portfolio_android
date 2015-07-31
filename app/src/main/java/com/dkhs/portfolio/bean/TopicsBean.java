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


    public boolean like;
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
    public int status_type;
    public String created_at;
    public String replied_status;
    public int content_type;

    public boolean compact;

    public PeopleBean user;
    public String lat;

    public String modified_at;
    /**
     * medias : [{"image_sm":"http://com-dkhs-media-test.oss.aliyuncs.com/medias/2015/07/27/16/4949/upload.360x360.jpg","image_xs":"http://com-dkhs-media-test.oss.aliyuncs.com/medias/2015/07/27/16/4949/upload.180x180.jpg","id":689076,"media_type":null,"image_md":"http://com-dkhs-media-test.oss.aliyuncs.com/medias/2015/07/27/16/4949/upload.720x19999.jpg","image_lg":"http://com-dkhs-media-test.oss.aliyuncs.com/medias/2015/07/27/16/4949/upload.1080x29999.jpg","full_path":null,"size":0}]
     */
    public List<MediasBean> medias;
    /**
     * symbols : [{"id":101000910,"symbol":"SH600651","abbr_name":"飞乐音响"}]
     */
    public List<SymbolsBean> symbols;

    @Parcel
    public static class MediasBean {
        /**
         * image_sm : http://com-dkhs-media-test.oss.aliyuncs.com/medias/2015/07/27/16/4949/upload.360x360.jpg
         * image_xs : http://com-dkhs-media-test.oss.aliyuncs.com/medias/2015/07/27/16/4949/upload.180x180.jpg
         * id : 689076
         * media_type : null
         * image_md : http://com-dkhs-media-test.oss.aliyuncs.com/medias/2015/07/27/16/4949/upload.720x19999.jpg
         * image_lg : http://com-dkhs-media-test.oss.aliyuncs.com/medias/2015/07/27/16/4949/upload.1080x29999.jpg
         * full_path : null
         * size : 0
         */
        public String image_sm;
        public String image_xs;
        public int id;
        public String media_type;
        public String image_md;
        public String image_lg;
        public String full_path;
        public int size;
    }

    @Parcel
    public static class SymbolsBean {
        /**
         * id : 101000910
         * symbol : SH600651
         * abbr_name : 飞乐音响
         */
        public int id;
        public String symbol;
        public String abbr_name;
    }
}
