package com.dkhs.portfolio.bean;

import org.parceler.Parcel;

import java.util.List;

/**
 * Created by wuyongsen on 2015/12/11.
 * 牛人认证
 */
@Parcel
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
    public String real_name;
    public String id_card_no;
    public String province;
    public String city;
    public String description;
    public String id_card_photo_full;

    public Organize org_profile;
    public List<String> photos;

    @Parcel
    public static class Organize {
        public int id;
        public String name;
        public int org_profile_type;
        public String chi_spell_all;


    }


}
