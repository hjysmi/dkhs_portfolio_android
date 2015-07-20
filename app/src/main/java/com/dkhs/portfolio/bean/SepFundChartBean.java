package com.dkhs.portfolio.bean;

/**
 * @author zwm
 * @version 2.0
 * @ClassName StepFundchartBean
 * @Description TODO(这里用一句话描述这个类的作用)
 * @date 2015/6/9.
 */
public class SepFundChartBean {


    /**
     * year_yld : 5.117
     * tenthou_unit_incm : 1.0565
     * date : 2015-03-11
     */
    private float year_yld;
    private float tenthou_unit_incm;
    private String date;

    public float getYear_yld() {
        return year_yld;
    }

    public void setYear_yld(float year_yld) {
        this.year_yld = year_yld;
    }

    public float getTenthou_unit_incm() {
        return tenthou_unit_incm;
    }

    public void setTenthou_unit_incm(float tenthou_unit_incm) {
        this.tenthou_unit_incm = tenthou_unit_incm;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
