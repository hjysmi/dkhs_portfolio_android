package com.dkhs.portfolio.bean;

/**
 * @author zwm
 * @version 2.0
 * @ClassName FundTypeBean
 * @Description TODO(这里用一句话描述这个类的作用)
 * @date 2015/5/29.
 */
public class FundTypeBean {

    private boolean enable;
    private String name ;
    private String code ;

    public boolean isEnable() {
        return enable;
    }




    public void setEnable(boolean enable) {
        this.enable = enable;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
}
