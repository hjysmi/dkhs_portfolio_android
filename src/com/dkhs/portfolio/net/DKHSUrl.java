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
        // String optional = "/api/v1/symbolfollow/";
        String optional = "/api/v1/symbolfollow/mysymbols/";
        // 股票列表查询
        // /api/v1/quotes/symbols/{exchange}/{sort}/
        String stocklist = "/api/v1/quotes/symbols/";

        // 查股票实时行情信息，包含5档信息
        String quotes = "/api/v1/quotes/";
        String symbolfollow = "/api/v1/symbolfollow/";
        String unfollow = "/api/v1/symbols/";

        // GET /api/v1/quotes/symbols_profile/
        String profile = "/api/v1/quotes/symbols_profile/";

        // GET /api/v1/quotes/fs_thumbnail/SZ000002/
        // 返回当日缩略图分时数据
        String sfthumbnail = "/api/v1/quotes/fs_thumbnail/";
    }

    public interface NetValue {
        // /api/v1/portfolio/query_daily_netvalue/
        String queryDaily = "/api/v1/portfolio/query_daily_netvalue/";

        // http://192.168.107.251:8000/api/v1/portfolio/query_netvalue/?portfolio_id=525
        String queryToday = "/api/v1/portfolio/query_netvalue/";

        // http://192.168.107.251:8000/api/v1/portfolio/query_combination_netvalue/?portfolio_id=508&page_size=7
        String report = "/api/v1/portfolio/query_combination_netvalue/";
    }
}
