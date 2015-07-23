package com.dkhs.portfolio.engine;

import com.dkhs.portfolio.app.PortfolioApplication;
import com.dkhs.portfolio.bean.DraftBean;
import com.lidroid.xutils.DbUtils;
import com.lidroid.xutils.db.sqlite.Selector;
import com.lidroid.xutils.exception.DbException;
import com.lidroid.xutils.util.LogUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zjz on 2015/7/23.
 */
public class DraftEngine {

    private static final String TAG = "DraftEngine";

    public void getDraftByUserId(long userId) {
        DbUtils dbUtils = DbUtils.create(PortfolioApplication.getInstance());
        List<DraftBean> draftBeanList = new ArrayList<DraftBean>();
        try {

            draftBeanList = dbUtils
                    .findAll(Selector.from(DraftBean.class).where(DraftBean.COLUM_AUTHORID, "=", userId)
                    );

            LogUtils.d(TAG, " Select DraftBeanList size:" + draftBeanList.size());

        } catch (DbException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }


    public void delDraft(DraftBean draftBean) {
        DbUtils dbUtils = DbUtils.create(PortfolioApplication.getInstance());

        try {
            dbUtils.delete(draftBean);
            LogUtils.d(TAG, "delDraft by id:" + draftBean.getId());

        } catch (DbException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public void saveDraft(DraftBean draftBean) {
        DbUtils dbUtils = DbUtils.create(PortfolioApplication.getInstance());

        try {
            dbUtils.saveOrUpdate(draftBean);
            LogUtils.d(TAG, "saveDraft by id:" + draftBean.getId());

        } catch (DbException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

}
