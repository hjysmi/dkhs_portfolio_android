package com.dkhs.portfolio.bean;

/**
 * Created by xuetong on 2015/12/11.
 */
public class OrgBean {
   /* "id": 324,
            "name": "爱建证券",
            "org_profile_type": 0,
            "chi_spell_all": "aijianzhengquanyouxianzerengongsi"*/

    private Integer id;
    private String name;
    private Integer org_profile_type;
    private String chi_spell_all;

    public OrgBean() {
    }

    public String getChi_spell_all() {
        return chi_spell_all;
    }

    public void setChi_spell_all(String chi_spell_all) {
        this.chi_spell_all = chi_spell_all;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getOrg_profile_type() {
        return org_profile_type;
    }

    public void setOrg_profile_type(Integer org_profile_type) {
        this.org_profile_type = org_profile_type;
    }
}
