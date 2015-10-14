package com.dkhs.portfolio.bean;

/**
 * Created by wuyongsen on 2015/10/14.
 */
public class WalletChangeBean {
    private int id;
    private ChangeSoure change_source;
    private float change;
    private float available;
    private float commission;
    private Target target_user;
    private int cotent_type;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public ChangeSoure getChange_source() {
        return change_source;
    }

    public void setChange_source(ChangeSoure change_source) {
        this.change_source = change_source;
    }

    public float getChange() {
        return change;
    }

    public void setChange(float change) {
        this.change = change;
    }

    public float getAvailable() {
        return available;
    }

    public void setAvailable(float available) {
        this.available = available;
    }

    public float getCommission() {
        return commission;
    }

    public void setCommission(float commission) {
        this.commission = commission;
    }

    public Target getTarget_user() {
        return target_user;
    }

    public void setTarget_user(Target target_user) {
        this.target_user = target_user;
    }

    public int getCotent_type() {
        return cotent_type;
    }

    public void setCotent_type(int cotent_type) {
        this.cotent_type = cotent_type;
    }

    public int getObject_id() {
        return object_id;
    }

    public void setObject_id(int object_id) {
        this.object_id = object_id;
    }

    public String getSerial_no() {
        return serial_no;
    }

    public void setSerial_no(String serial_no) {
        this.serial_no = serial_no;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public String getModified_at() {
        return modified_at;
    }

    public void setModified_at(String modified_at) {
        this.modified_at = modified_at;
    }

    private int object_id;
    private String serial_no;
    private int state;
    private String created_at;
    private String modified_at;


    public static class Target{
        int id;
        String username;
        String chi_spell;
        String chi_spell_all;
        boolean is_active;
        String avatar_xs;
        String avatar_sm;
        String avatar_md;
        String avatar_lg;
        String description;
        String city;
        String province;
        String gender;
        String[] category;
        int followed_by_count;
        int friends_count;
        int status_count;
        int symbols_count;
        int portfolios_count;
        int portfolios_following_count;
        String date_joined;
    }
    public static class ChangeSoure{
        public String code;
        public String name;

    }
}
