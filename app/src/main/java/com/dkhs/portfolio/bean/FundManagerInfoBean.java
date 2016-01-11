package com.dkhs.portfolio.bean;

import java.util.List;

/**
 * @author zwm
 * @version 2.0
 * @ClassName FundManagerDetailBean
 * @Description TODO(这里用一句话描述这个类的作用)
 * @date 2015/6/5.
 */
public class FundManagerInfoBean  extends  FundManagerBean {


    /**
     * id : 302000757
     * resume :     许杰先生,硕士,特许金融分析师(CFA)。曾任青岛市纺织品进出口公司部门经理、美国迪斯尼公司金融分析员,2002年3月加入泰达宏利基金管理有限公司,历任研究员、研究部副总经理、基金经理等职,2006年12月23日至2011年9月30日任泰达宏利风险预算混合基金基金经理;2010年2月8日至2011年9月30日任泰达宏利首选企业股票基金基金经理;2008年9月26日至2011年9月30日任泰达宏利集利债券基金基金经理。2011年10月加入我公司,2012年3月21日起任建信恒稳价值混合型证券投资基金基金经理;2012年8月14日起任建信社会责任股票型证券投资基金的基金经理。
     * name : 许杰
     * achivements : [{"end_date":null,"cp_rate":0,"fund":{"id":102005770,"symbol":"FP001276","list_status":1,"symbol_stype":301,"abbr_name":"建信新经济灵活配置混合"},"start_date":"2015-05-26","sh300_rate":1.61},{"end_date":null,"cp_rate":47.5,"fund":{"id":102005120,"symbol":"FP000995","list_status":1,"symbol_stype":301,"abbr_name":"建信睿盈灵活配置混合C"},"start_date":"2015-02-03","sh300_rate":54.49},{"end_date":null,"cp_rate":48.7,"fund":{"id":102005119,"symbol":"FP000994","list_status":1,"symbol_stype":301,"abbr_name":"建信睿盈灵活配置混合A"},"start_date":"2015-02-03","sh300_rate":54.49},{"end_date":null,"cp_rate":80.9,"fund":{"id":102004670,"symbol":"FP000756","list_status":1,"symbol_stype":300,"abbr_name":"建信潜力新蓝筹股票"},"start_date":"2014-09-10","sh300_rate":111.9},{"end_date":null,"cp_rate":177.5,"fund":{"id":102002650,"symbol":"FP530019","list_status":1,"symbol_stype":300,"abbr_name":"建信社会责任股票"},"start_date":"2012-08-14","sh300_rate":120.3},{"end_date":null,"cp_rate":138.84,"fund":{"id":102002197,"symbol":"FP530016","list_status":1,"symbol_stype":301,"abbr_name":"建信恒稳价值混合"},"start_date":"2012-03-21","sh300_rate":100.48},{"end_date":"2011-09-30","cp_rate":-16.62,"fund":{"id":102000320,"symbol":"FP162208","list_status":1,"symbol_stype":300,"abbr_name":"泰达宏利首选企业股票"},"start_date":"2010-02-08","sh300_rate":-18.13},{"end_date":"2011-09-30","cp_rate":-1.94,"fund":{"id":102000526,"symbol":"FP162299","list_status":1,"symbol_stype":302,"abbr_name":"泰达宏利集利债券C"},"start_date":"2008-09-26","sh300_rate":16.09},{"end_date":"2011-09-30","cp_rate":-0.09,"fund":{"id":102000524,"symbol":"FP162210","list_status":1,"symbol_stype":302,"abbr_name":"泰达宏利集利债券A"},"start_date":"2008-09-26","sh300_rate":16.09},{"end_date":"2011-09-30","cp_rate":91.08,"fund":{"id":102000175,"symbol":"FP162205","list_status":1,"symbol_stype":301,"abbr_name":"泰达宏利风险预算混合"},"start_date":"2006-12-23","sh300_rate":36.17}]
     */
    private String resume;
    /**
     * 基金经理类型，判断是否需要展示跑赢大盘
     */
    private int manager_type;
    private List<AchivementsEntity> achivements;
    private List<FundIndexEntity> index;

    public List<FundIndexEntity> getIndex() {
        return index;
    }

    public void setIndex(List<FundIndexEntity> index) {
        this.index = index;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setResume(String resume) {
        this.resume = resume;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setAchivements(List<AchivementsEntity> achivements) {
        this.achivements = achivements;
    }

    public int getId() {
        return id;
    }

    public String getResume() {
        return resume;
    }

    public String getName() {
        return name;
    }

    public List<AchivementsEntity> getAchivements() {
        return achivements;
    }

    public int getManager_type() {
        return manager_type;
    }

    public void setManager_type(int manager_type) {
        this.manager_type = manager_type;
    }


    public String getFund_company() {
        return fund_company;
    }

    public void setFund_company(String fund_company) {
        this.fund_company = fund_company;
    }



    public class AchivementsEntity {
        /**
         * end_date : null
         * cp_rate : 0
         * fund : {"id":102005770,"symbol":"FP001276","list_status":1,"symbol_stype":301,"abbr_name":"建信新经济灵活配置混合"}
         * start_date : 2015-05-26
         * sh300_rate : 1.61
         */
        private String end_date;
        private double cp_rate;
        private FundEntity fund;
        private String start_date;
        private double sh300_rate;

        private boolean expend;

        public boolean isExpend() {
            return expend;
        }

        public void setExpend(boolean expend) {
            this.expend = expend;
        }

        public void setEnd_date(String end_date) {
            this.end_date = end_date;
        }

        public void setCp_rate(double cp_rate) {
            this.cp_rate = cp_rate;
        }

        public void setFund(FundEntity fund) {
            this.fund = fund;
        }

        public void setStart_date(String start_date) {
            this.start_date = start_date;
        }

        public void setSh300_rate(double sh300_rate) {
            this.sh300_rate = sh300_rate;
        }

        public String getEnd_date() {
            return end_date;
        }

        public double getCp_rate() {
            return cp_rate;
        }

        public FundEntity getFund() {
            return fund;
        }

        public String getStart_date() {
            return start_date;
        }

        public double getSh300_rate() {
            return sh300_rate;
        }

        public class FundEntity {
            /**
             * id : 102005770
             * symbol : FP001276
             * list_status : 1
             * symbol_stype : 301
             * abbr_name : 建信新经济灵活配置混合
             */
            private int id;
            private String symbol;
            private int list_status;
            private int symbol_stype;
            private String abbr_name;
            private double percent_day;
            private String net_value;
            private String net_cumulative;
            private String year_yld;
            private String tenthou_unit_incm;

            public void setId(int id) {
                this.id = id;
            }

            public void setSymbol(String symbol) {
                this.symbol = symbol;
            }

            public void setList_status(int list_status) {
                this.list_status = list_status;
            }

            public void setSymbol_stype(int symbol_stype) {
                this.symbol_stype = symbol_stype;
            }

            public void setAbbr_name(String abbr_name) {
                this.abbr_name = abbr_name;
            }

            public int getId() {
                return id;
            }

            public String getSymbol() {
                return symbol;
            }

            public int getList_status() {
                return list_status;
            }

            public int getSymbol_stype() {
                return symbol_stype;
            }

            public String getAbbr_name() {
                return abbr_name;
            }

            public double getPercent_day() {
                return percent_day;
            }

            public void setPercent_day(double percent_day) {
                this.percent_day = percent_day;
            }

            public String getNet_value() {
                return net_value;
            }

            public void setNet_value(String net_value) {
                this.net_value = net_value;
            }

            public String getNet_cumulative() {
                return net_cumulative;
            }

            public void setNet_cumulative(String net_cumulative) {
                this.net_cumulative = net_cumulative;
            }

            public String getYear_yld() {
                return year_yld;
            }

            public void setYear_yld(String year_yld) {
                this.year_yld = year_yld;
            }

            public String getTenthou_unit_incm() {
                return tenthou_unit_incm;
            }

            public void setTenthou_unit_incm(String tenthou_unit_incm) {
                this.tenthou_unit_incm = tenthou_unit_incm;
            }
        }
    }

    public class FundIndexEntity{
        private String tradedate;
        private float day_index;
        private float percentage;

        public String getTradedate() {
            return tradedate;
        }

        public void setTradedate(String tradedate) {
            this.tradedate = tradedate;
        }

        public float getDay_index() {
            return day_index;
        }

        public void setDay_index(float day_index) {
            this.day_index = day_index;
        }

        public float getPercentage() {
            return percentage;
        }

        public void setPercentage(float percentage) {
            this.percentage = percentage;
        }
    }
}
