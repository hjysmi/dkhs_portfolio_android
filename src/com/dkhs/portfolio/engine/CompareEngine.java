/**
 * @Title CompareEngine.java
 * @Package com.dkhs.portfolio.engine
 * @Description TODO(用一句话描述该文件做什么)
 * @author zjz
 * @date 2014-10-9 下午6:15:07
 * @version V1.0
 */
package com.dkhs.portfolio.engine;

import com.dkhs.portfolio.net.DKHSClient;
import com.dkhs.portfolio.net.DKHSUrl;
import com.dkhs.portfolio.net.IHttpListener;

/**
 * @ClassName CompareEngine
 * @Description TODO(这里用一句话描述这个类的作用)
 * @author zjz
 * @date 2014-10-9 下午6:15:07
 * @version 1.0
 */
public class CompareEngine {

    public void compare(IHttpListener listener, String ids, String startDay, String endDay) {
        DKHSClient.requestByGet(listener, DKHSUrl.Fund.compare, ids, startDay, endDay);
    }

}
