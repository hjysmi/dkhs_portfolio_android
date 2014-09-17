/**
 * @Title: DKHSUrl.java
 * @Package com.naerju.network
 * @Description: TODO(用一句话描述该文件做什么)
 * @author think4
 * @date 2014-4-2 下午1:40:52
 * @version V1.0
 */
package com.dkhs.portfolio.net;

/**
 * @ClassName: DKHSUrl
 * @Description: 与服务器连接的url地址
 * @author zhoujunzhou
 * @date 2014-09-12 下午1:40:52
 * @version 1.0
 */
public class DKHSUrl {

    static final String BASE_URL = "http://192.168.107.251:8000";

    public interface Portfolio {
        // 查询我的组合
        String portfolio = "/api/v1/portfolio/mine/";
        // 删除组合
        String delete = "/api/v1/portfolio/delete_portfolios/";
        // 更新组合信息
        String update = "/api/v1/portfolio/update_portfolio/";
        // 持仓调整
        String adjust = "/api/v1/portfolio/adjust_positions/";

    }
}
