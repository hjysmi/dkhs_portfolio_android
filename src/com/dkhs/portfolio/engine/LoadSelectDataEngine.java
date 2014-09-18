/**
 * @Title LoadSelectDataEngine.java
 * @Package com.dkhs.portfolio.engine
 * @Description TODO(用一句话描述该文件做什么)
 * @author zjz
 * @date 2014-9-18 下午2:16:43
 * @version V1.0
 */
package com.dkhs.portfolio.engine;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.dkhs.portfolio.bean.SelectStockBean;
import com.dkhs.portfolio.bean.StockPriceBean;
import com.dkhs.portfolio.net.DataParse;
import com.dkhs.portfolio.net.ParseHttpListener;

/**
 * @ClassName LoadSelectDataEngine
 * @Description TODO(这里用一句话描述这个类的作用)
 * @author zjz
 * @date 2014-9-18 下午2:16:43
 * @version 1.0
 */
public abstract class LoadSelectDataEngine extends ParseHttpListener<List<SelectStockBean>> {

    public LoadSelectDataEngine(ILoadDataBackListener loadListener) {
        this.iLoadListener = loadListener;
    }

    private ILoadDataBackListener iLoadListener;

    public void setLoadDataBackListener(ILoadDataBackListener backListener) {
        this.iLoadListener = backListener;
    }

    public interface ILoadDataBackListener {
        void loadFinish(List<SelectStockBean> object);
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
     * @Title
     * @Description TODO: (用一句话描述这个方法的功能)
     * @param object
     * @return
     */
    @Override
    protected void afterParseData(List<SelectStockBean> object) {
        if (null != iLoadListener) {
            iLoadListener.loadFinish(object);
        }

    }
}
