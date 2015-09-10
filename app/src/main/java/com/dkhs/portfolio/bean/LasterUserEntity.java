package com.dkhs.portfolio.bean;

import com.lidroid.xutils.db.annotation.Column;
import com.lidroid.xutils.db.annotation.Table;

/**
 * Created by zjz on 2015/7/15.
 */
@Table(name = "LasterUserEntity")
public class LasterUserEntity extends SortUserEntity {

    public LasterUserEntity() {
        super();
    }

    public LasterUserEntity(SortUserEntity sortUserEntity) {
        this.access_token = sortUserEntity.getAccess_token();
        this.sortLetters = sortUserEntity.getSortLetters();
        this.avatar_xs = sortUserEntity.getAvatar_xs();
        this.avatar_lg = sortUserEntity.getAvatar_lg();
        this.avatar_md = sortUserEntity.getAvatar_md();
        this.avatar_sm = sortUserEntity.getAvatar_sm();
        this.chi_spell_all = sortUserEntity.getChi_spell_all();
        this.chi_spell = sortUserEntity.getChi_spell();
        this.id = sortUserEntity.getId();
        this.username = sortUserEntity.getUsername();
    }

    public long getSaveTime() {
        return saveTime;
    }

    public void setSaveTime(long saveTime) {
        this.saveTime = saveTime;
    }

    @Column(column = "saveTime")
    private long saveTime;

}
