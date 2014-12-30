/**
 * @Title LoadSelectDataEngine.java
 * @Package com.dkhs.portfolio.engine
 * @Description TODO(用一句话描述该文件做什么)
 * @author zjz
 * @date 2014-9-18 下午2:16:43
 * @version V1.0
 */
package com.dkhs.portfolio.engine;

import java.util.List;

import android.content.Context;

import com.dkhs.portfolio.bean.SelectStockBean;
import com.dkhs.portfolio.net.ErrorBundle;
import com.dkhs.portfolio.net.ParseHttpListener;

/**
 * @ClassName LoadSelectDataEngine
 * @Description TODO(这里用一句话描述这个类的作用)
 * @author zjz
 * @date 2014-9-18 下午2:16:43
 * @version 1.0
 */
public abstract class LoadSelectDataEngine extends ParseHttpListener<List<SelectStockBean>> {

    private int totalcount;
    private int totalpage;
    private int currentpage;
    private int statu = -1;

    public LoadSelectDataEngine(ILoadDataBackListener loadListener) {
        this.iLoadListener = loadListener;
    }

    private ILoadDataBackListener iLoadListener;

    public void setLoadDataBackListener(ILoadDataBackListener backListener) {
        this.iLoadListener = backListener;
    }

    public interface ILoadDataBackListener {
        void loadFinish(List<SelectStockBean> object);

        void loadFail(ErrorBundle error);
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
     * 刷新数据
     */
    public abstract void refreshDatabySize(int dataSize);

    /**
     * @Title
     * @Description TODO: (用一句话描述这个方法的功能)
     * @param object
     * @return
     */
    @Override
    protected void afterParseData(List<SelectStockBean> object) {
        if (null != iLoadListener) {
            iLoadListener.loadFinish(object);
            // iLoadListener.setStatu(getStatu());
        }

    }

    /**
     * @Title
     * @Description TODO: (用一句话描述这个方法的功能)
     * @param errCode
     * @param errMsg
     * @return
     */
    @Override
    public void onFailure(int errCode, String errMsg) {
        // TODO Auto-generated method stub
        super.onFailure(errCode, errMsg);
        ErrorBundle error = new ErrorBundle();
        error.setErrorCode(errCode);
        error.setErrorMessage(errMsg);
        if (null != iLoadListener) {
            iLoadListener.loadFail(error);
        }
    }

    /**
     * @Title
     * @Description TODO: (用一句话描述这个方法的功能)
     * @return
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

    public int getStatu() {
        return statu;
    }

    public void setStatu(int statu) {
        this.statu = statu;
    }

}
