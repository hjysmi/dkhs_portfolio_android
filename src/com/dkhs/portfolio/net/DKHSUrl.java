/**
 * @Title: MoshiUrl.java
 * @Package com.naerju.network
 * @Description: TODO(用一句话描述该文件做什么)
 * @author think4
 * @date 2014-4-2 下午1:40:52
 * @version V1.0
 */
package com.dkhs.portfolio.net;

/**
 * @ClassName: MoshiUrl
 * @Description: 与服务器连接的url地址
 * @author zhoujunzhou
 * @date 2014-4-2 下午1:40:52
 * @version 1.0
 */
public class DKHSUrl {

    static final String BASE_URL = "http://192.168.107.100:8000";

    public interface Portfolio {
        // 查询我的组合
        String portfolio = "/api/v1/portfolio/";

    }
}
