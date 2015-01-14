/**
 * @Title LoadMoreDataEngine.java
 * @Package com.dkhs.portfolio.engine
 * @Description TODO(用一句话描述该文件做什么)
 * @author zjz
 * @date 2014-9-22 上午9:46:30
 * @version V1.0
 */
package com.dkhs.portfolio.engine;

import java.util.List;

import com.dkhs.portfolio.bean.MoreDataBean;
import com.dkhs.portfolio.net.ParseHttpListener;
import com.lidroid.xutils.http.HttpHandler;

/**
 * @ClassName LoadMoreDataEngine
 * @Description TODO(这里用一句话描述这个类的作用)
 * @author zjz
 * @date 2014-9-22 上午9:46:30
 * @version 1.0
 * @param <T>
 */
public abstract class LoadMoreDataEngine extends ParseHttpListener<MoreDataBean> {

    private int totalcount;
    private int totalpage;
    private int currentpage;

    public LoadMoreDataEngine(ILoadDataBackListener loadListener) {
        this.iLoadListener = loadListener;
    }

    private ILoadDataBackListener iLoadListener;

    public void setLoadDataBackListener(ILoadDataBackListener backListener) {
        this.iLoadListener = backListener;
    }

    public interface ILoadDataBackListener<T> {
        void loadFinish(MoreDataBean<T> object);
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
     * @Title
     * @Description TODO: (用一句话描述这个方法的功能)
     * @param object
     * @return
     */
    @Override
    protected void afterParseData(MoreDataBean object) {

        if (null != object) {
            setTotalcount(object.getTotalCount());
            setTotalpage(object.getTotalPage());
            setCurrentpage(object.getCurrentPage());
            if (null != iLoadListener) {
                iLoadListener.loadFinish(object);
            }

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

}
