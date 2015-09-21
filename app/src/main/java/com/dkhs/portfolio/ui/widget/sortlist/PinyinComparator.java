package com.dkhs.portfolio.ui.widget.sortlist;

import com.dkhs.portfolio.bean.SortUserEntity;

import java.util.Comparator;

/**
 * @author
 */
public class PinyinComparator implements Comparator<SortUserEntity> {

    public int compare(SortUserEntity o1, SortUserEntity o2) {
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
