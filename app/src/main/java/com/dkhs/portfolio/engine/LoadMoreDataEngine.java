/**
 * @Title LoadMoreDataEngine.java
 * @Package com.dkhs.portfolio.engine
 * @Description TODO(用一句话描述该文件做什么)
 * @author zjz
 * @date 2014-9-22 上午9:46:30
 * @version V1.0
 */
package com.dkhs.portfolio.engine;

import com.dkhs.portfolio.bean.MoreDataBean;
import com.dkhs.portfolio.net.ParseHttpListener;
import com.lidroid.xutils.http.HttpHandler;

/**
 * @param <T>
 * @author zjz
 * @version 1.0
 * @ClassName LoadMoreDataEngine
 * @Description TODO(这里用一句话描述这个类的作用)
 * @date 2014-9-22 上午9:46:30
 */
public abstract class LoadMoreDataEngine extends ParseHttpListener<MoreDataBean> {

    private int totalcount;
    private int totalpage;
    private int currentpage;
    private int statu = -1;


    private MoreDataBean moreDataBean;

    public LoadMoreDataEngine() {
    }

    public LoadMoreDataEngine(ILoadDataBackListener loadListener) {
        this.iLoadListener = loadListener;
    }

    private ILoadDataBackListener iLoadListener;

    public void setLoadDataBackListener(ILoadDataBackListener backListener) {
        this.iLoadListener = backListener;
    }

    public interface ILoadDataBackListener<T> {
        void loadFinish(MoreDataBean<T> object);

        void loadFail();
    }

    /**
     * 加载更多
     */
    public abstract HttpHandler loadMore();

    /**
     * 加载数据
     */
    public abstract HttpHandler loadData();

    /**
     * 刷新数据
     */
    public abstract HttpHandler refreshDatabySize(int dataSize);

    /**
     * @param object
     * @return
     * @Title
     * @Description TODO: (用一句话描述这个方法的功能)
     */
    @Override
    protected void afterParseData(MoreDataBean object) {

        if (null != object) {
            moreDataBean = object;
            setTotalcount(object.getTotalCount());
            setTotalpage(object.getTotalPage());
            setCurrentpage(object.getCurrentPage());
            setStatu(object.getStatu());
            if (null != iLoadListener) {
                iLoadListener.loadFinish(object);
            }

        }
    }

    /**
     * @param errCode
     * @param errMsg
     * @return
     * @Title
     * @Description TODO: (用一句话描述这个方法的功能)
     */
    @Override
    public void onFailure(int errCode, String errMsg) {
        // TODO Auto-generated method stub
        super.onFailure(errCode, errMsg);
        if (null != iLoadListener) {
            // iLoadListener.loadFinish(null);
            iLoadListener.loadFail();
        }
    }

    public int getTotalcount() {
        return totalcount;
    }

    public void setTotalcount(int totalcount) {
        this.totalcount = totalcount;
    }

    public int getTotalpage() {
        return totalpage;
    }

    public void setTotalpage(int totalpage) {
        this.totalpage = totalpage;
    }

    public int getCurrentpage() {
        return currentpage;
    }

    public void setCurrentpage(int currentpage) {
        this.currentpage = currentpage;
    }

    public ILoadDataBackListener getLoadListener() {
        return iLoadListener;
    }

    public int getStatu() {
        return statu;
    }

    public void setStatu(int statu) {
        this.statu = statu;
    }

    public MoreDataBean getMoreDataBean() {
        return moreDataBean;
    }

    public void setMoreDataBean(MoreDataBean moreDataBean) {
        this.moreDataBean = moreDataBean;
    }
}
