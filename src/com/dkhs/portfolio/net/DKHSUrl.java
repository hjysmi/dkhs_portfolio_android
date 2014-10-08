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

    public static final String BASE_URL = "http://192.168.107.251:8002";

    public interface Portfolio {
        // 查询我的组合
        String portfolio = "/api/v1/portfolio/mine/";
        // 创建我的组合
        String create = "/api/v1/portfolio/";
        // 删除组合
        String delete = "/api/v1/portfolio/delete_portfolios/";
        // 更新组合信息
        String update = "/api/v1/portfolio/";
        // 持仓调整
        String adjust = "/api/v1/portfolio/";

    }

    public interface StockSymbol {
        // 自选股列表
        // String optional = "/api/v1/symbolfollow/";
//        String optional = "/api/v1/symbolfollow/mysymbols/";
        String optional = "/api/v1/symbols/following/";
        // 股票列表查询
        // /api/v1/quotes/symbols/{exchange}/{sort}/

//        http://192.168.107.251:8002/api/v1/symbols/?exchange=1&sort=change

        String stocklist = "/api/v1/symbols/";

        // 查股票实时行情信息，包含5档信息
        //http://192.168.107.251:8002/api/v1/symbols/SZ000002/quote/
        String quotes = "/api/v1/symbols/";
        String symbolfollow = "/api/v1/symbols/";
        //http://192.168.107.251:8002/api/v1/symbols/101000100/unfollow/
        String unfollow = "/api/v1/symbols/";

        // GET /api/v1/quotes/symbols_profile/
        String profile = "/api/v1/symbols/profile/";

        //k线图数据
        String kline_pre = "/api/v1/symbols/";
        String kline_after = "/k_line/";

        // GET /api/v1/quotes/fs_thumbnail/SZ000002/
        // 返回当日缩略图分时数据
        //http://192.168.107.251:8002/api/v1/symbols/SZ000002/time_line/?period=1
        String sfthumbnail = "/api/v1/symbols/";

    }

    public interface NetValue {
        // /api/v1/portfolio/query_daily_netvalue/
        //GET /api/v1/portfolio/{pk}/netvalue_daily_chart/
        String queryDaily = "/api/v1/portfolio/";

        // http://192.168.107.251:8000/api/v1/portfolio/query_netvalue/?portfolio_id=525
//        String queryToday = "/api/v1/portfolio/query_netvalue/";
        String queryToday = "/api/v1/portfolio/";

        // http://192.168.107.251:8000/api/v1/portfolio/query_combination_netvalue/?portfolio_id=508&page_size=7
//        String report = "/api/v1/portfolio/query_combination_netvalue/";
        ///api/v1/portfolio/{pk}/netvalue_history/
        String report = "/api/v1/portfolio/";
    }
}
