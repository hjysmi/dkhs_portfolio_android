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

    // 测试
    public static final String BASE_TEST_URL = "http://121.41.25.170:8030";
//    public static final String BASE_TEST_URL = "http://192.168.107.75:8000";

    // 开发
    public static final String BASE_DEV_URL = "http://121.41.25.170:8010";
    // 正式
    public static final String BASE_DEV_MAIN = "https://www.dkhs.com";
    // 服务器
    public static final String BASE_DEV_TAG = "http://121.41.25.170:8050";

    // public static final String BASE_URL = "http://192.168.107.251:8010";
    // public static final String BASE_URL = "http://58.23.5.117:8030";
    // 测试用地址
    // public static final String BASE_URL = "http://121.41.25.170:8010";
    public interface Portfolio {
        // 查询我的组合
        String portfolio = "/api/v1/portfolio/mine/";

        // 查询关注的组合
        String following = "/api/v1/portfolio/following/";

        // 取消关注组合
        String delFollow = "/api/v1/portfolio/{0}/unfollow/";
        // 添加关注组合
        String addFollow = "/api/v1/portfolio/{0}/follow/";

        // 设置提醒
        String remind = "/api/v1/portfolio/{0}/follow/";

        // 创建我的组合
        String create = "/api/v1/portfolio/";
        // 删除组合
        String delete = "/api/v1/portfolio/{0}/";
        // 更新组合信息
        String update = "/api/v1/portfolio/";
        // 持仓调整
        String adjust = "/api/v1/portfolio/{0}/adjust_positions/";
        // 设置组合是否公开
        String ispublic = "/api/v1/portfolio/{0}/set_public/";
        // 设置组合是否参与排行
        String setrank = "/api/v1/portfolio/{0}/set_rank/";

        // 首页冠军榜
        String champion = "/api/v1/portfolio/champion/";

        // 组合排行榜
        String rankingList = "/api/v1/portfolio/ranking_list/";

        // 组合置顶
        String set_top = "/api/v1/portfolio/{0}/set_top/";

        // 组合编辑排序
        String sort = "/api/v1/portfolio/following/sort/";

    }

    public interface StockSymbol {
        // 自选股列表
        String optional = "/api/v1/symbols/following/";

        // 股票列表查询
        String stocklist = "/api/v1/symbols/";

        // 查股票实时行情信息，包含5档信息
        String quotes = "/api/v1/symbols/{0}/quote/";
        String symbolfollow = "/api/v1/symbols/{0}/follow/";

        // 股票提醒设置
        String remimd = "/api/v1/symbols/{0}/follow/";
        String unfollow = "/api/v1/symbols/{0}/unfollow/";

        // GET /api/v1/quotes/symbols_profile/
        String profile = "/api/v1/symbols/profile/";

        // k线图数据
        String kline = "/api/v1/symbols/{0}/k_line/";

        String sfthumbnail = "/api/v1/symbols/{0}/time_line/?period=1";
        // 行情中心 指数排行
        String marketcenter = "/api/v1/symbols/";
        String opitionmarket = "/api/v1/symbols/";
        String index = "/api/v1/symbols/following/sort/";
    }

    public interface NetValue {
        String queryDaily = "/api/v1/portfolio/{0}/netvalue_daily_chart/";

        String queryToday = "/api/v1/portfolio/{0}/netvalue_realtime_chart/";

        String report = "/api/v1/portfolio/{0}/netvalue_history/";
        String queryDetailPosition = "/api/v1/portfolio/{0}/get_detailed_positions/";
    }

    public interface Fund {
        String fundsList = "/api/v1/symbols/funds/";
        String mainIndexList = "/api/v1/symbols/";
        String compare = "/api/v1/symbols/{0}/quote_history/?from_date={1}&to_date={2}";

    }

    public interface News {
        // 新闻公告 从主界面与左滑动栏进入时调用
        String optionnews = "/api/v1/statuses/symbol_timeline/?user_id={0}&page_size=50&content_type=20,30";
        // 关于某只股票的新闻与公告 10,20,30(0表示话题,10表示新闻,20表示公告,30表示研报
        // http://58.23.5.117:8010/api/v1/statuses/symbol_timeline/?symbol=sz300102&content_type=10
        String peroptionnews = "/api/v1/statuses/symbol_timeline/?page_size=50&symbol={0}&content_type={1}";
        // String reportnews =
        // "/api/v1/statuses/symbol_timeline/?page_size=50&user_id={0}&content_type=20,30&content_subtype={1}";

        // 游客模式的自选资讯
        String reportnewsByAnony = "/api/v1/statuses/symbol_timeline/?symbol={0}&content_type=20,30";

        // 已登陆用户的自选资讯
        String reportnews = "/api/v1/statuses/symbol_timeline/?page_size=50&user_id={0}&content_type=20,30";
        String reportnewsforone = "/api/v1/statuses/symbol_timeline/?page_size=50&symbol={0}&content_type=30&content_subtype={1}";
        String reportnewstwo = "/api/v1/statuses/symbol_timeline/?page_size=50&content_type=30&content_subtype={0}";
        String reportnewsgroupeach = "/api/v1/statuses/symbol_timeline/?page_size=50&portfolio_id={0}&content_type={1}";
        String reportnewsoptioneach = "/api/v1/statuses/symbol_timeline/?page_size=50&symbol={0}&content_type=30";
        String reportnewsoptioneachs = "/api/v1/statuses/symbol_timeline/?page_size=200&symbol={0}&content_type=30";
        String reportnews_second_notice = "/api/v1/statuses/symbol_timeline/?page_size=50&symbol={0}&content_type=20";
        String newstext = "/api/v1/statuses/";
    }

    public interface User {
        // 登录
        String login = "/api/v1/accounts/login/";
        // 分享
        String share = "/portfolio/share/";
        // 注册
        String register = "/api/v1/accounts/signup/";
        // 绑定第三方
        String bingdings = "/api/v1/accounts/social_bindings/";

        // 获取验证码
        String get_vericode = "/api/v1/accounts/new_mobile_captcha/";
        String check_vericode = "/api/v1/accounts/verify_mobile_captcha/";
        // 是否设置过密码
        String is_setpassword = "/api/v1/accounts/is_password_set/";
        // 设置密码
        String setpassword = "/api/v1/accounts/set_password/";
        // 修改密码
        String changepassword = "/api/v1/accounts/change_password/";
        String checkMobile = "/api/v1/accounts/check_mobile/?mobile={0}";
        // 绑定邮箱
        String boundemail = "/api/v1/accounts/bind_email/?email={0}";
        // 设置昵称
        String setUserName = "/api/v1/accounts/set_username/";
        // 修改头像
        String setUserHead = "/api/v1/accounts/avatar/";
        // 获取设置界面信息
        String settingMessage = "/api/v1/accounts/settings/";
        String base_userinfo = "/api/v1/users/{0}/";
        // 获取服务器版本信息
        String get_version = "/api/v1/apps/";
        // 添加用户反馈
        String add_feed = "/api/v1/apps/";
        String bind_mobile = "/api/v1/accounts/bind_mobile/";
        String get_token="/api/v1/im/token/";
    }

    public interface MainPage {
        String zhishu = "/api/v1/symbols/{0}/quote/";
    }

    public interface Plate {
        String hotPlate = "/api/v1/symbols/sectors/?sector_root_id=26";
    }

    // DKHSUrl.Portfolio.rankingList,
    public static String[] storeURLList = { DKHSUrl.Plate.hotPlate, DKHSUrl.Portfolio.portfolio,
            DKHSUrl.StockSymbol.optional, DKHSUrl.Portfolio.champion, DKHSUrl.StockSymbol.opitionmarket,
            DKHSUrl.StockSymbol.stocklist,

    };

}
