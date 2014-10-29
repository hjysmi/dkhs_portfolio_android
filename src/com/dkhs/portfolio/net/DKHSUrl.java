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

    // 测试用地址
    // public static final String BASE_URL = "http://121.41.25.170:8030";
    // 开发用地址
    public static final String BASE_URL = "http://121.41.25.170:8010";

    // public static final String BASE_URL = "http://192.168.107.251:8010";
    // public static final String BASE_URL = "http://58.23.5.117:8030";
    // 测试用地址
    // public static final String BASE_URL = "http://121.41.25.170:8010";
    public interface Portfolio {
        // 查询我的组合
        String portfolio = "/api/v1/portfolio/mine/";
        // 创建我的组合
        String create = "/api/v1/portfolio/";
        // 删除组合
        String delete = "/api/v1/portfolio/{0}/";
        // 更新组合信息
        String update = "/api/v1/portfolio/";
        // 持仓调整
        String adjust = "/api/v1/portfolio/{0}/adjust_positions/";

        String champion = "/api/v1/portfolio/champion/";

        // 组合排行榜
        String rankingList = "/api/v1/portfolio/ranking_list/";

    }

    public interface StockSymbol {
        // 自选股列表
        // String optional = "/api/v1/symbolfollow/";
        // String optional = "/api/v1/symbolfollow/mysymbols/";
        String optional = "/api/v1/symbols/following/";
        // 股票列表查询
        // /api/v1/quotes/symbols/{exchange}/{sort}/

        // http://192.168.107.251:8002/api/v1/symbols/?exchange=1&sort=change

        String stocklist = "/api/v1/symbols/?exchange={0}&sort={1}&symbol_type={2}";

        // 查股票实时行情信息，包含5档信息
        // http://192.168.107.251:8002/api/v1/symbols/SZ000002/quote/
        String quotes = "/api/v1/symbols/{0}/quote";
        String symbolfollow = "/api/v1/symbols/{0}/follow/";
        // http://192.168.107.251:8002/api/v1/symbols/101000100/unfollow/
        String unfollow = "/api/v1/symbols/{0}/unfollow/";

        // GET /api/v1/quotes/symbols_profile/
        String profile = "/api/v1/symbols/profile/";

        // k线图数据
        String kline = "/api/v1/symbols/{0}/k_line/";

        // GET /api/v1/quotes/fs_thumbnail/SZ000002/
        // 返回当日缩略图分时数据
        // http://192.168.107.251:8002/api/v1/symbols/SZ000002/time_line/?period=1

        String sfthumbnail = "/api/v1/symbols/{0}/time_line/?period=1";
        // 行情中心 指数排行
        String marketcenter = "/api/v1/symbols/?sort={0}percentage&page_size=10&is_midx=1";
        String opitionmarket = "/api/v1/symbols/?sort={0}percentage&page_size=10&symbol_type=1";
    }

    public interface NetValue {
        // /api/v1/portfolio/query_daily_netvalue/
        // GET /api/v1/portfolio/{pk}/netvalue_daily_chart/
        String queryDaily = "/api/v1/portfolio/{0}/netvalue_daily_chart/";

        // http://192.168.107.251:8000/api/v1/portfolio/query_netvalue/?portfolio_id=525
        // String queryToday = "/api/v1/portfolio/query_netvalue/";
        String queryToday = "/api/v1/portfolio/{0}/netvalue_realtime_chart/";

        // http://192.168.107.251:8000/api/v1/portfolio/query_combination_netvalue/?portfolio_id=508&page_size=7
        // String report = "/api/v1/portfolio/query_combination_netvalue/";
        // /api/v1/portfolio/{pk}/netvalue_history/
        String report = "/api/v1/portfolio/{0}/netvalue_history/";
    }

    public interface Fund {
        // http://192.168.107.251:8002/api/v1/symbols/funds/?type=1&sort=percent_month
        String fundsList = "/api/v1/symbols/funds/?type={0}&sort={1}";
        String mainIndexList = "/api/v1/symbols/?symbol_type=5&is_midx=1&sort={0}";
        // http://192.168.107.251:8002/api/v1/symbols/106000082%2C106000232/quote_history/?from_date=2014-09-01&to_date=2014-10-08
        String compare = "/api/v1/symbols/{0}/quote_history/?from_date={1}&to_date={2}";

    }

    public interface News {
        // 新闻公告 从主界面与左滑动栏进入时调用
        String optionnews = "/api/v1/statuses/symbol_timeline/?user_id={0}&page_size=10";
        // 关于某只股票的新闻与公告 10,20,30(0表示话题,10表示新闻,20表示公告,30表示研报
        // http://58.23.5.117:8010/api/v1/statuses/symbol_timeline/?symbol=sz300102&content_type=10
        String peroptionnews = "/api/v1/statuses/symbol_timeline/?page_size=10&symbol={0}&content_type={1}";
        String newstext = "/api/v1/statuses/";
    }

    public interface User {
        // 登录
        String login = "/api/v1/accounts/login/";
        // 注册
        String register = "/api/v1/accounts/signup/";
        // 获取验证码
        String get_vericode = "/api/v1/accounts/new_mobile_captcha/";
        // 是否设置过密码
        String is_setpassword = "/api/v1/accounts/is_password_set/";
        // 设置密码
        String setpassword = "/api/v1/accounts/set_password/";
        // 修改密码
        String changepassword = "/api/v1/accounts/change_password/";
        String checkMobile = "/api/v1/accounts/check_mobile/?mobile={0}";

    }

    public interface MainPage {
        String zhishu = "/api/v1/symbols/{0}/quote/";
    }

}
