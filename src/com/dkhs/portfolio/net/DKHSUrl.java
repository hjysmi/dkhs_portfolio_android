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
        // 创建我的组合
        String create = "/api/v1/portfolio/";
        // 删除组合
        String delete = "/api/v1/portfolio/delete_portfolios/";
        // 更新组合信息
        String update = "/api/v1/portfolio/update_portfolio/";
        // 持仓调整
        String adjust = "/api/v1/portfolio/adjust_positions/";

    }

    public interface StockSymbol {
        // 自选股列表
        String optional = "/api/v1/symbolfollow/";
        // 股票列表查询
        // /api/v1/quotes/symbols/{exchange}/{sort}/
        String stocklist = "/api/v1/quotes/symbols/";
    }
}
