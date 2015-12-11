package com.dkhs.portfolio.bean;

/**
 * Created by wuyongsen on 2015/12/11.
 */
public class ProInfoBean {
    public int verified_type;
    public String cert_no;
    public String cert_description;
    public int status;
    public String status_note;
    public String image1;
    public String image2;
    public String image3;
    public String image4;
    public String image5;
    public String image6;
    public Organize org_profile;
    public static  class Organize{
        public int id;
        public String name;
        public int org_profile_type;
        public String chi_spell_all;
    }
}
