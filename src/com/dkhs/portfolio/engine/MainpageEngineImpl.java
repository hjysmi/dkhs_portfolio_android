/**   
 * @Title MainpageEngineImpl.java 
 * @Package com.dkhs.portfolio.engine 
 * @Description TODO(用一句话描述该文件做什么) 
 * @author zjz  
 * @date 2014-10-21 下午8:36:35 
 * @version V1.0   
 */
package com.dkhs.portfolio.engine;

import com.dkhs.portfolio.net.DKHSClient;
import com.dkhs.portfolio.net.DKHSUrl;
import com.dkhs.portfolio.net.IHttpListener;

/** 
 * @ClassName MainpageEngineImpl 
 * @Description TODO(这里用一句话描述这个类的作用) 
 * @author zjz 
 * @date 2014-10-21 下午8:36:35 
 * @version 1.0 
 */
public class MainpageEngineImpl {

    
    public void getScrollValue(IHttpListener listener){
        ///api/v1/symbols/SH000001%2CSZ399001/quote/
        DKHSClient.requestByGet(listener, DKHSUrl.MainPage.zhishu, "SH000001,SZ399001");
    }
    
    public void getChampionList(IHttpListener listener){
        DKHSClient.requestByGet(listener, DKHSUrl.Portfolio.champion);
    }
}
