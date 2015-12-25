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
    public int rewarded_by_status;
    public String reward_expired_at;
    public int rewarded_comment;
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
        private static final int LEVEL_UNKNOWN = 0;
        private static final int LEVEL_LOW = 1;
        private static final int LEVEL_MEDIUM_LOW = 2;
        private static final int LEVEL_MEDIUM = 3;
        private static final int LEVEL_MEDIUM_HIGH = 4;
        private static final int LEVEL_HIGH = 5;
        public static String getInvestRiskByType(int type ,String [] levels){
            String inverstRisk = "";
            switch (type){
                case LEVEL_UNKNOWN:
                    inverstRisk = levels[0];
                    break;
                case LEVEL_LOW:
                    inverstRisk = levels[1];
                    break;
                case LEVEL_MEDIUM_LOW:
                    inverstRisk = levels[2];
                    break;
                case LEVEL_MEDIUM:
                    inverstRisk = levels[3];
                    break;
                case LEVEL_MEDIUM_HIGH:
                    inverstRisk = levels[4];
                    break;
                case LEVEL_HIGH:
                    inverstRisk = levels[5];
                    break;
            }
            return inverstRisk;
        }


        /**
         * id : 101000910
         * symbol : SH600651
         * abbr_name : 飞乐音响
         */
        public int id;
        public String symbol;
        public String symbol_type;
        public int symbol_stype;
        public String recommend_desc;
        public String abbr_name;
        public double percent_six_month;
        public String net_value;
        public String net_cumulative;
        public String year_yld;
        public String tenthou_unit_incm;
        public String list_status;
        public int investment_risk;
        public double amount_min_buy;
        public double discount_rate_buy;
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
