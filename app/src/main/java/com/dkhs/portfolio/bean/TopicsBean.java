package com.dkhs.portfolio.bean;

import org.parceler.Parcel;

import java.util.List;

/**
 * @author zwm
 * @version 2.0
 *          className TopcsBean
 *          TODO(这里用一句话描述这个类的作用)
 *          date 2015/7/16.
 */
@Parcel
public class TopicsBean extends LikeBean {


    public int state;
    public int content_type;
    public String reward_amount;
    public int reward_state;
    public int rewarded_by_status;
    public String reward_expired_at;
    /**
     * symbols : [{"id":101000910,"symbol":"SH600651","abbr_name":"飞乐音响"}]
     */
    public List<SymbolsBean> symbols;
    /**
     * source : {"id":68,"title":"齐鲁证券"}
     */

    public  TopicsBean.SourceBean source;


    public TopicsBean(){
        compact=false;
    }



//    @Parcel
//    public static class MediasBean {
//        /**
//         * image_sm : http://com-dkhs-media-test.oss.aliyuncs.com/medias/2015/07/27/16/4949/upload.360x360.jpg
//         * image_xs : http://com-dkhs-media-test.oss.aliyuncs.com/medias/2015/07/27/16/4949/upload.180x180.jpg
//         * id : 689076
//         * media_type : null
//         * image_md : http://com-dkhs-media-test.oss.aliyuncs.com/medias/2015/07/27/16/4949/upload.720x19999.jpg
//         * image_lg : http://com-dkhs-media-test.oss.aliyuncs.com/medias/2015/07/27/16/4949/upload.1080x29999.jpg
//         * full_path : null
//         * size : 0
//         */
//        public String image_sm;
//        public String image_xs;
//        public int id;
//        public String media_type;
//        public String image_md;
//        public String image_lg;
//        public String full_path;
//        public int size;
//    }

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

    @Parcel
    public static class SourceBean {
        /**
         * id : 68
         * title : 齐鲁证券
         */
        public int id;
        public String title;
    }
}
