package com.dkhs.portfolio.bean;

/**
 * Created by zjz on 2015/7/3.
 */
public class AllMarketBean {
    //    "midx_data": {}, /国内指数
//    "sect_data": {}, /热门行业
//    "rise_data": {}, /涨幅榜
//    "drop_data": {}, /跌幅榜
//    "turnover_data": {}, /换手榜
//    "amplitude_data": {}, /振幅榜
    private MarkSectorBean sect_data;
    private MarkStockBean rise_data;
    private MarkStockBean amplitude_data;
    private MarkStockBean turnover_data;
    private MarkStockBean drop_data;

    public MarkStockBean getMidx_data() {
        return midx_data;
    }

    public void setMidx_data(MarkStockBean midx_data) {
        this.midx_data = midx_data;
    }

    private MarkStockBean midx_data;

    public MarkSectorBean getSect_data() {
        return sect_data;
    }

    public void setSect_data(MarkSectorBean sect_data) {
        this.sect_data = sect_data;
    }

    public MarkStockBean getRise_data() {
        return rise_data;
    }

    public void setRise_data(MarkStockBean rise_data) {
        this.rise_data = rise_data;
    }

    public MarkStockBean getAmplitude_data() {
        return amplitude_data;
    }

    public void setAmplitude_data(MarkStockBean amplitude_data) {
        this.amplitude_data = amplitude_data;
    }

    public MarkStockBean getTurnover_data() {
        return turnover_data;
    }

    public void setTurnover_data(MarkStockBean turnover_data) {
        this.turnover_data = turnover_data;
    }


    public void setDrop_data(MarkStockBean drop_data) {
        this.drop_data = drop_data;
    }

    public MarkStockBean getDrop_data() {
        return drop_data;
    }


}
