/**
 * @Title StockEngineImpl.java
 * @Package com.dkhs.portfolio.engine
 * @Description TODO(用一句话描述该文件做什么)
 * @author zjz
 * @date 2014-9-18 上午10:27:07
 * @version V1.0
 */
package com.dkhs.portfolio.engine;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.os.Looper;
import android.text.TextUtils;
import android.util.Log;

import com.dkhs.portfolio.app.PortfolioApplication;
import com.dkhs.portfolio.bean.MoreDataBean;
import com.dkhs.portfolio.bean.SectorBean;
import com.dkhs.portfolio.bean.SelectStockBean;
import com.dkhs.portfolio.bean.StockPriceBean;
import com.dkhs.portfolio.bean.UserEntity;
import com.dkhs.portfolio.net.DKHSClient;
import com.dkhs.portfolio.net.DKHSUrl;
import com.dkhs.portfolio.net.DataParse;
import com.dkhs.portfolio.net.IHttpListener;
import com.dkhs.portfolio.net.ParseHttpListener;
import com.dkhs.portfolio.utils.StockUitls;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.lidroid.xutils.http.HttpHandler;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;
import com.lidroid.xutils.util.LogUtils;

/**
 * @ClassName StockEngineImpl
 * @Description TODO(这里用一句话描述这个类的作用)
 * @author zjz
 * @date 2014-9-18 上午10:27:07
 * @version 1.0
 */
public class OptionalStockEngineImpl extends LoadMoreDataEngine {
    // ILoadDataBackListener loadListener;

    public OptionalStockEngineImpl(ILoadDataBackListener loadListener, boolean isShowIndex) {
        super(loadListener);
        this.isShowIndex = isShowIndex;
        // this.loadListener = loadListener;
    }

    private boolean isShowIndex;

    // private List<StockPriceBean> results = new ArrayList<StockPriceBean>();

    // /**
    // * 查询自选股
    // */
    // public void getOptionalList(IHttpListener listener) {
    //
    // }
    private static final String DEF_ORDER_TYPE = "";
    private String orderType = DEF_ORDER_TYPE;

    public void setLoadType(String orderType) {
        this.orderType = orderType;
    }

    private boolean isLoading;

    @Override
    public HttpHandler loadData() {

        Log.e("OPtionalStockEngineImple", "OPtionalStockEngineImple loadData()");

        if (isLoading) {
            return null;
        }
        isLoading = true;

        if (PortfolioApplication.hasUserLogin()) {

            RequestParams params = new RequestParams();
            params.addQueryStringParameter("page", "1");
            params.addQueryStringParameter("sort", orderType);
            params.addQueryStringParameter("page_size", Integer.MAX_VALUE + "");
            return DKHSClient.request(HttpMethod.GET, DKHSUrl.StockSymbol.optional, params, this);
        } else {

            if (null != getLoadListener()) {
                List<SelectStockBean> dataList = new VisitorDataEngine().getOptionalStockListBySort();
                StringBuilder sbIds = new StringBuilder();
                if (null != dataList) {
                    for (SelectStockBean stock : dataList) {
                        sbIds.append(stock.code);
                        sbIds.append(",");
                    }
                    // sbIds = sbIds.substring(0, sbIds.length()-1);
                    // System.out.println("datalist size:" + dataList.size());
                    // getiLoadListener().loadFinish(dataList);
                    if (null != sbIds && sbIds.length() > 1) {

                        Log.i("OptionalStockEngineImpl", "ids:" + sbIds.substring(0, sbIds.length() - 1));

                        RequestParams params = new RequestParams();
                        if (!orderType.equals(DEF_ORDER_TYPE)) {
                            params.addQueryStringParameter("sort", orderType);
                        }
                        params.addQueryStringParameter("page_size", dataList.size() + "");
                        params.addQueryStringParameter("symbols", sbIds.substring(0, sbIds.length() - 1));
                        return DKHSClient.request(HttpMethod.GET, DKHSUrl.StockSymbol.optional, params, this);
                    } else {
                        // MoreDataBean empty = new MoreDataBean<T>()
                        getLoadListener().loadFinish(new MoreDataBean.EmptyMoreBean());
                        isLoading = false;
                    }
                } else {
                    getLoadListener().loadFinish(new MoreDataBean.EmptyMoreBean());

                    isLoading = false;
                    // getiLoadListener().loadFail(null);

                }
            }

            return null;
        }
    }

    @Override
    protected MoreDataBean parseDateTask(String jsonData) {
        // /需要优化的地方

        isLoading = false;

        if (Looper.getMainLooper().getThread() == Thread.currentThread()) {
            Log.e("OPtionalStockEngineImple", "OPtionalStockEngineImple ParseDataTask on main thread");
        } else {
            Log.e("OPtionalStockEngineImple", "OPtionalStockEngineImple ParseDataTask on Child   thread");
        }

        MoreDataBean<StockPriceBean> dataMoreBean = new MoreDataBean.EmptyMoreBean();
        // List<SelectStockBean> selectList = new ArrayList<SelectStockBean>();
        MoreDataBean<SelectStockBean> parseMoreBean = new MoreDataBean.EmptyMoreBean();

        try {

            Gson gson = new GsonBuilder().enableComplexMapKeySerialization().create();

            dataMoreBean = (MoreDataBean) gson.fromJson(jsonData, new TypeToken<MoreDataBean<StockPriceBean>>() {
            }.getType());
            //
            parseMoreBean.copyMoreDataBean(dataMoreBean);
            parseMoreBean.setResults(new ArrayList<SelectStockBean>());

            for (StockPriceBean priceBean : dataMoreBean.getResults()) {
                // selectList.add(SelectStockBean.copy(priceBean));
                parseMoreBean.getResults().add(SelectStockBean.copy(priceBean));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return parseMoreBean;

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
        isLoading = false;
    }

    // public List<SelectStockBean> forIndex(List<SelectStockBean> datalist) {
    // List<SelectStockBean> tmp = new ArrayList<SelectStockBean>();
    // SelectStockBean sb;
    // int position;
    // while (datalist.size() > 0) {
    // for (int i = 0; i < datalist.size(); i++) {
    // sb = datalist.get(i);
    // position = i;
    // for (int j = i; j < datalist.size(); j++) {
    // if (sb.index < datalist.get(j).index) {
    // sb = datalist.get(j);
    // position = j;
    // }
    // }
    // datalist.remove(position);
    // tmp.add(sb);
    // break;
    // }
    // }
    // return tmp;
    // }

    public static HttpHandler setIndex(ParseHttpListener<List<SelectStockBean>> listener, String json) {
        RequestParams params = new RequestParams();
        params.addBodyParameter("symbols_position", json);
        return DKHSClient.request(HttpMethod.POST, DKHSUrl.StockSymbol.index, params, listener);
    }

    public static HttpHandler loadAllData(IHttpListener listener) {
        RequestParams params = new RequestParams();
        params.addQueryStringParameter("page", "1");
        params.addQueryStringParameter("page_size", Integer.MAX_VALUE + "");
        return DKHSClient.request(HttpMethod.GET, DKHSUrl.StockSymbol.optional, params, listener);

    }

    // public void loadAllData() {
    // RequestParams params = new RequestParams();
    // params.addQueryStringParameter("page", "1");
    // params.addQueryStringParameter("page_size", Integer.MAX_VALUE + "");
    // DKHSClient.request(HttpMethod.GET, DKHSUrl.StockSymbol.optional, params, this);
    //
    // }

    /**
     * @Title
     * @Description TODO: (用一句话描述这个方法的功能)
     * @return
     */
    @Override
    public HttpHandler loadMore() {
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        NameValuePair valuePair = new BasicNameValuePair("page", (getCurrentpage() + 1) + "");
        params.add(valuePair);

        if (TextUtils.isEmpty(orderType)) {
            NameValuePair valuePair2 = new BasicNameValuePair("sort", "followed_at");
            params.add(valuePair2);
            return DKHSClient.requestByGet(DKHSUrl.StockSymbol.optional, null, params, this);

        } else {
            NameValuePair valuePair2 = new BasicNameValuePair("sort", orderType);
            params.add(valuePair2);
            return DKHSClient.requestByGet(DKHSUrl.StockSymbol.optional, null, params, this);

        }

    }

    /**
     * @Title
     * @Description TODO: (用一句话描述这个方法的功能)
     * @param dataSize
     * @return
     */
    @Override
    public HttpHandler refreshDatabySize(int dataSize) {
        RequestParams params = new RequestParams();
        params.addQueryStringParameter("page", "1");
        params.addQueryStringParameter("page_size", dataSize + "");
        return DKHSClient.request(HttpMethod.GET, DKHSUrl.StockSymbol.optional, params, this);

    }

    // public List<StockPriceBean> getResults() {
    // return results;
    // }
    //
    // public void setResults(List<StockPriceBean> results) {
    // this.results = results;
    // }

}
