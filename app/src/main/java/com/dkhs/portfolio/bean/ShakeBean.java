package com.dkhs.portfolio.bean;

import com.google.gson.annotations.SerializedName;

import org.parceler.Parcel;

/**
 * @author zwm
 * @version 2.0
 * @ClassName ShakeBean
 * @Description TODO(这里用一句话描述这个类的作用)
 * @date 2015/7/1.
 */

public class ShakeBean {


    /**
     * content : 中国联通降低宽带资费中国联通降低宽带资费中国联通降低宽带资费
     * id : 2
     * title : 中国联通
     * symbol : {"symbol":"SZ300459","abbr_name":"浙江金科"}
     * display_time : 120
     * up_rate : 0.0
     * capital_flow : 4452.12万元
     * coins_bonus : 4
     * times_left : 0
     * modified_at : 2015-07-01T02:51:10Z
     * times_used : 3
     */
    @SerializedName("content")
    public String content;
    @SerializedName("id")
    public int id;
    @SerializedName("title")
    public String title;
    @SerializedName("symbol")
    public SymbolEntity symbol;
    @SerializedName("display_time")
    public int display_time;
    @SerializedName("up_rate")
    public double up_rate;
    @SerializedName("capital_flow")
    public String capital_flow;
    @SerializedName("coins_bonus")
    public int coins_bonus;
    @SerializedName("times_left")
    public int times_left;
    @SerializedName("modified_at")
    public String modified_at;
    @SerializedName("times_used")
    public int times_used;

    public static class SymbolEntity {
        /**
         * symbol : SZ300459
         * abbr_name : 浙江金科
         */
        @SerializedName("symbol")
        public String symbol;
        @SerializedName("abbr_name")
        public String abbr_name;
        public   SymbolEntity(){

        }
    }
}
