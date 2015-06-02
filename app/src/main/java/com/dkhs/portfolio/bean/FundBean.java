package com.dkhs.portfolio.bean;

/**
 * @author zwm
 * @version 2.0
 * @ClassName FundBean
 * @Description TODO(基金实体类)
 * @date 2015/6/2.
 */
public class FundBean {


    /**
     * chi_spell : JJJD
     * net_cumulative : 2.2626
     * percent_day : -10.7885
     * symbol : SH500021
     * percent_six_month : 38.4023
     * percent_season : 23.4419
     * symbol_type : 3
     * list_status : 3
     * percent_week : -10.7885
     * tenthou_unit_incm : 0
     * code : 500021
     * net_value : 1.4686
     * followed : false
     * percent_month : 10.6075
     * id : 102000088
     * symbol_stype : 308
     * year_yld : 0
     * tradedate : 2007-04-06
     * percent_tyear : 23.2698
     * percent_year : 97.3419
     * abbr_name :
     */
    private String chi_spell;
    private double net_cumulative;
    private double percent_day;
    private String symbol;
    private double percent_six_month;
    private double percent_season;
    private int symbol_type;
    private int list_status;
    private double percent_week;
    private int tenthou_unit_incm;
    private String code;
    private double net_value;
    private boolean followed;
    private double percent_month;
    private int id;
    private int symbol_stype;
    private int year_yld;
    private String tradedate;
    private double percent_tyear;
    private double percent_year;
    private String abbr_name;

    public double getValue(String key) {
        double value=0 ;
        if (key.equals("percent_day")) {
            value = percent_day ;
        } else if (key.equals("percent_day")) {
            value = percent_day ;
        } else if (key.equals("percent_month")) {
            value = percent_month ;
        } else if (key.equals("percent_year")) {
            value = percent_year ;
        } else if (key.equals("percent_tyear")) {
            value = percent_tyear;
        } else if (key.equals("net_cumulative")) {
            value = net_cumulative;
        } else if (key.equals("year_yld")) {
            value = year_yld ;
        }
        return value;
    }

    public void setChi_spell(String chi_spell) {
        this.chi_spell = chi_spell;
    }

    public void setNet_cumulative(double net_cumulative) {
        this.net_cumulative = net_cumulative;
    }

    public void setPercent_day(double percent_day) {
        this.percent_day = percent_day;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public void setPercent_six_month(double percent_six_month) {
        this.percent_six_month = percent_six_month;
    }

    public void setPercent_season(double percent_season) {
        this.percent_season = percent_season;
    }

    public void setSymbol_type(int symbol_type) {
        this.symbol_type = symbol_type;
    }

    public void setList_status(int list_status) {
        this.list_status = list_status;
    }

    public void setPercent_week(double percent_week) {
        this.percent_week = percent_week;
    }

    public void setTenthou_unit_incm(int tenthou_unit_incm) {
        this.tenthou_unit_incm = tenthou_unit_incm;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public void setNet_value(double net_value) {
        this.net_value = net_value;
    }

    public void setFollowed(boolean followed) {
        this.followed = followed;
    }

    public void setPercent_month(double percent_month) {
        this.percent_month = percent_month;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setSymbol_stype(int symbol_stype) {
        this.symbol_stype = symbol_stype;
    }

    public void setYear_yld(int year_yld) {
        this.year_yld = year_yld;
    }

    public void setTradedate(String tradedate) {
        this.tradedate = tradedate;
    }

    public void setPercent_tyear(double percent_tyear) {
        this.percent_tyear = percent_tyear;
    }

    public void setPercent_year(double percent_year) {
        this.percent_year = percent_year;
    }

    public void setAbbr_name(String abbr_name) {
        this.abbr_name = abbr_name;
    }

    public String getChi_spell() {
        return chi_spell;
    }

    public double getNet_cumulative() {
        return net_cumulative;
    }

    public double getPercent_day() {
        return percent_day;
    }

    public String getSymbol() {
        return symbol;
    }

    public double getPercent_six_month() {
        return percent_six_month;
    }

    public double getPercent_season() {
        return percent_season;
    }

    public int getSymbol_type() {
        return symbol_type;
    }

    public int getList_status() {
        return list_status;
    }

    public double getPercent_week() {
        return percent_week;
    }

    public int getTenthou_unit_incm() {
        return tenthou_unit_incm;
    }

    public String getCode() {
        return code;
    }

    public double getNet_value() {
        return net_value;
    }

    public boolean isFollowed() {
        return followed;
    }

    public double getPercent_month() {
        return percent_month;
    }

    public int getId() {
        return id;
    }

    public int getSymbol_stype() {
        return symbol_stype;
    }

    public int getYear_yld() {
        return year_yld;
    }

    public String getTradedate() {
        return tradedate;
    }

    public double getPercent_tyear() {
        return percent_tyear;
    }

    public double getPercent_year() {
        return percent_year;
    }

    public String getAbbr_name() {
        return abbr_name;
    }
}
