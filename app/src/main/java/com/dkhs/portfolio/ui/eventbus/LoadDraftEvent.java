package com.dkhs.portfolio.ui.eventbus;

import com.dkhs.portfolio.bean.DraftBean;

import java.util.List;

/**
 * Created by zjz on 2015/7/27.
 */
public class LoadDraftEvent {
    public List<DraftBean> dataList;

    public LoadDraftEvent(List<DraftBean> list) {
        this.dataList = list;
    }
}
