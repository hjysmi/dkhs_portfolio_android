package com.dkhs.portfolio.ui.widget.sortlist;

import com.dkhs.portfolio.bean.UserEntity;

import java.util.Comparator;

/**
 * @author
 */
public class PinyinComparator implements Comparator<UserEntity> {

    public int compare(UserEntity o1, UserEntity o2) {
        if (o1.getSortLetters().equals("@")
                || o2.getSortLetters().equals("#")) {
            return -1;
        } else if (o1.getSortLetters().equals("#")
                || o2.getSortLetters().equals("@")) {
            return 1;
        } else {
            return o1.getSortLetters().compareTo(o2.getSortLetters());
        }
    }

}
