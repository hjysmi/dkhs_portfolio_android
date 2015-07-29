package com.dkhs.portfolio.engine;

import com.dkhs.portfolio.app.PortfolioApplication;
import com.dkhs.portfolio.bean.DraftBean;
import com.dkhs.portfolio.common.GlobalParams;
import com.dkhs.portfolio.ui.eventbus.LoadDraftEvent;
import com.lidroid.xutils.DbUtils;
import com.lidroid.xutils.db.sqlite.Selector;
import com.lidroid.xutils.exception.DbException;
import com.squareup.otto.Bus;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zjz on 2015/7/23.
 */
public class DraftEngine {

    private static final String TAG = "DraftEngine";

    private Bus mEventBus;
    private String mAuthorID;

    public DraftEngine(String authorID, Bus eventBus) {
        this.mEventBus = eventBus;
        this.mAuthorID = authorID;
    }

    public DraftEngine(Bus eventBus) {
        this.mEventBus = eventBus;
        this.mAuthorID = String.valueOf(GlobalParams.LOGIN_USER.getId());
    }

    public void getDraftByUserId() {
        new Thread() {
            @Override
            public void run() {
                DbUtils dbUtils = DbUtils.create(PortfolioApplication.getInstance());
                List<DraftBean> draftBeanList = new ArrayList<DraftBean>();
                try {

                    draftBeanList = dbUtils
                            .findAll(Selector.from(DraftBean.class).where(DraftBean.COLUM_AUTHORID, "=", mAuthorID).orderBy(DraftBean.COLUM_EDITTIME, true)
                            );
                    if (null != mEventBus) {
                        mEventBus.post(new LoadDraftEvent(draftBeanList));
                    }

                } catch (DbException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        }.start();

    }


    public void delDraft(final DraftBean draftBean) {
        new Thread() {
            @Override
            public void run() {
                DbUtils dbUtils = DbUtils.create(PortfolioApplication.getInstance());

                try {
                    draftBean.setAuthorId(mAuthorID);
                    dbUtils.delete(draftBean);

                } catch (DbException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        }.start();

    }

    public void saveDraft(final DraftBean draftBean) {
        new Thread() {
            @Override
            public void run() {
                DbUtils dbUtils = DbUtils.create(PortfolioApplication.getInstance());

                try {
                    draftBean.setAuthorId(mAuthorID);
                    draftBean.setEdittime(System.currentTimeMillis() / 1000);
                    dbUtils.saveOrUpdate(draftBean);

                } catch (DbException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        }.start();

    }

}
