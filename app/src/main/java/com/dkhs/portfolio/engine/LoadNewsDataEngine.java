/**
 * @Title LoadSelectDataEngine.java
 * @Package com.dkhs.portfolio.engine
 * @Description TODO(用一句话描述该文件做什么)
 * @author zjz
 * @date 2014-9-18 下午2:16:43
 * @version V1.0
 */
package com.dkhs.portfolio.engine;

import com.dkhs.portfolio.bean.OptionNewsBean;
import com.dkhs.portfolio.net.ParseHttpListener;

import java.util.List;

/**
 * \
 *
 * @author weiting
 */
public abstract class LoadNewsDataEngine extends ParseHttpListener<List<OptionNewsBean>> {

    private int totalcount;
    private int totalpage;
    private int currentpage;

    public LoadNewsDataEngine(ILoadDataBackListener loadListener) {
        this.iLoadListener = loadListener;
    }

    private ILoadDataBackListener iLoadListener;

    public void setLoadDataBackListener(ILoadDataBackListener backListener) {
        this.iLoadListener = backListener;
    }

    public interface ILoadDataBackListener {
        void loadFinish(List<OptionNewsBean> object);

        void loadingFail();
    }

    /**
     * 加载更多
     */
    public abstract void loadMore();

    /**
     * 加载数据
     */
    public abstract void loadData();


    /**
     * @param object
     * @return
     * @Title
     * @Description TODO: (用一句话描述这个方法的功能)
     */
    @Override
    protected void afterParseData(List<OptionNewsBean> object) {
        if (null != iLoadListener) {
            iLoadListener.loadFinish(object);
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
            iLoadListener.loadingFail();
        }
    }

    /**
     * @return
     * @Title
     * @Description TODO: (用一句话描述这个方法的功能)
     */
    @Override
    public void requestCallBack() {
        // TODO Auto-generated method stub
        super.requestCallBack();
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
