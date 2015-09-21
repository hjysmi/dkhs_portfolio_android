package com.dkhs.portfolio.bean;

import com.lidroid.xutils.db.annotation.Column;
import com.lidroid.xutils.db.annotation.Table;

/**
 * Created by zjz on 2015/9/1.
 */
@Table(name = "SortUserEntity")
public class SortUserEntity extends UserEntity {

    @Column(column = "sortLetters")
    String sortLetters;//显示数据拼音的首字母

    public String getSortLetters() {
        return sortLetters;
    }

    public void setSortLetters(String sortLetters) {
        this.sortLetters = sortLetters;
    }
}
