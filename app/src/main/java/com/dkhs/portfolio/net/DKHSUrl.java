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
 * @author zhoujunzhou
 * @version 1.0
 * @ClassName: DKHSUrl
 * @Description: 与服务器连接的url地址
 * @date 2014-09-12 下午1:40:52
 */
public class DKHSUrl {

    // 测试
//    public static final String BASE_TEST_URL = "http://192.168.107.90:8010";
//    public static final String BASE_TEST_URL = "http://192.168.107.76:8010";
    public static final String BASE_TEST_URL = "http://test.dev.dkhs.com";

    // 开发
    public static final String BASE_DEV_URL = "http://dev.dev.dkhs.com";
    // 正式
    public static final String BASE_DEV_MAIN = "https://www.dkhs.com";
    // 服务器
    public static final String BASE_DEV_TAG = "http://staging.dev.dkhs.com";

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
        //获取关注的人
        String get_frients = "/api/v1/users/%s/friends/";
        //获取粉丝
        String get_followers = "/api/v1/users/%s/followers/";


        //持仓明细
        String portfolio_detail = "/api/v1/portfolio/%s/";

        /**
         * 推荐组合
         */
        String RECOMMEND_PROTFOLIO = "/api/v1/portfolio/recommend/";

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
        String allMarkets = "/api/v1/symbols/markets/cn/";

        String opitionmarket = "/api/v1/symbols/";
        String index = "/api/v1/symbols/following/sort/";


        //接口描述：股本信息与10大流通股东
        //  注：symbol为股票代码 如SZ000880
        String F10_Holder = "/api/v1/symbols/{0}/holders/";


        //接口描述：简况信息
        //注：symbol为股票代码 如SZ000880
        String F10_INTRODUCTION = "/api/v1/symbols/{0}/introduction/";

        //接口描述：个股财务报表
//        注：symbol为股票代码 如SZ000880
        String F10_FINANCE = "/api/v1/symbols/{0}/finance_index/";

        /**
         * 推荐基金经理
         */
        String RECOMMEND_FUND_MANAGER = "/api/v1/symbols/funds/managers/recommend/";
        /**
         * 推荐基金
         */
        String RECOMMEND_FUND = "/api/v1/symbols/recommend/";


    }

    public interface Ads {

        String getNewsBannerAds = "/api/v1/ads/area/news_banner/";
        String getStatusesBanner = "/api/v1/ads/area/statuses_banner/";
        String getSplashAds = "/api/v1/ads/area/splash/";
        String getInvitingInfo = "/api/v1/accounts/invitations/code/";
        String getInvitations = "/api/v1/accounts/invitations/invitations/";
        String getSignUp = "/api/v1/ads/area/signup/";
        String getInvite = "/api/v1/ads/area/Invite/";
        String getRechargeBanner = "/api/v1/ads/area/recharge_banner/";
        /**
         * 获取主页广告
         */
        String GET_HOME_BANNER = "/api/v1/ads/area/home_banner/";
        String GET_SUB_HOME_BANNER = "/api/v1/ads/area/home_sub_banner";
        /**
         * 权威信息展示
         */
        String getSafeInfo = "/safe/";

    }

    public interface Shake {
        String getShakeInfo = "/api/v1/shakes/symbols/next/";
        String getDescriptionOfActivities = "/portfolio/shakes";
    }


    public interface NetValue {
        String queryDaily = "/api/v1/portfolio/{0}/netvalue_daily_chart/";

        String queryToday = "/api/v1/portfolio/{0}/netvalue_realtime_chart/";

        String report = "/api/v1/portfolio/{0}/netvalue_history/";
        String queryDetailPosition = "/api/v1/portfolio/{0}/get_detailed_positions/";
    }

    public interface Fund {
        String fundsList = "/api/v1/symbols/funds/";
        String fundsManagerList = "/api/v1/symbols/funds/managers/";
        String mainIndexList = "/api/v1/symbols/";
        String compare = "/api/v1/symbols/{0}/quote_history/?from_date={1}&to_date={2}";
        String compareByPeriod = "/api/v1/symbols/{0}/quote_history/";
        String managerInfo = "/api/v1/symbols/funds/managers/%s/";
    }

    public interface BBS {
        String getLatestTopic = "/api/v1/statuses/public_timeline/";
        String getUserTopics = "/api/v1/statuses/user_timeline/";
        String getHotTopic = "/api/v1/statuses/public_timeline/";
        String getStickTopic = "/api/v1/statuses/public_timeline/";
        String getCommend = "/api/v1/statuses/{0}/comments/";
        String getLikes = "/api/v1/statuses/{0}/likes/";
        String getHotTopicDetail = "/api/v1/statuses/{0}/";
        String star = "/api/v1/statuses/likes/like/";
        String unstar = "/api/v1/statuses/likes/unlike/";
        String getRewardList = "/api/v1/statuses/public_timeline/";
        String getUserRewards = "/api/v1/statuses/user_timeline/";
        String adoptAnswer = "/api/v1/statuses/{0}/reward/";


    }

    public interface News {
        // 新闻公告 从主界面与左滑动栏进入时调用
        String optionnews = "/api/v1/statuses/symbol_timeline/?user_id={0}&page_size=20&content_type=20,30";
        // 关于某只股票的新闻与公告 10,20,30(0表示话题,10表示新闻,20表示公告,30表示研报
        // http://58.23.5.117:8010/api/v1/statuses/symbol_timeline/?symbol=sz300102&content_type=10
        String stocknews = "/api/v1/statuses/symbol_timeline/?page_size=5&symbol={0}&content_type=10";
        String stock_all_news = "/api/v1/statuses/symbol_timeline/?page_size=20&symbol={0}&content_type={1}";
        // 个股公告
        String peroptionnews = "/api/v1/statuses/symbol_timeline/?page_size=5&symbol={0}&content_type={1}";
        // 个股研报
        String reportnewsoptioneach = "/api/v1/statuses/symbol_timeline/?page_size=5&symbol={0}&content_type=30";

        // 游客模式的自选资讯
        String reportnewsByAnony = "/api/v1/statuses/symbol_timeline/?symbol={0}&content_type=10,20";

        // 已登陆用户的自选资讯
        String reportnews = "/api/v1/statuses/symbol_timeline/?page_size=20&user_id={0}&content_type=10,20";
        String reportnewsforone = "/api/v1/statuses/symbol_timeline/?page_size=20&symbol={0}&content_type=30&content_subtype={1}";
        String reportnewstwo = "/api/v1/statuses/symbol_timeline/?page_size=20&content_type=30&content_subtype={0}";
        //今日要闻
        String reportnews_today = "/api/v1/statuses/recommend/?page_size=20&content_type=10";

        //
        String reportnewsgroupeach = "/api/v1/statuses/symbol_timeline/?page_size=20&portfolio_id={0}&content_type={1}";

        String reportnewsoptioneachs = "/api/v1/statuses/symbol_timeline/?page_size=200&symbol={0}&content_type=30";
        String reportnews_second_notice = "/api/v1/statuses/symbol_timeline/?page_size=20&symbol={0}&content_type=20";
        String newstext = "/api/v1/statuses/";
        //获取单条信息
        String newstext_detial = "/api/v1/statuses/%s";

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
        String get_token = "/api/v1/im/token/";
        String follow = "/api/v1/users/%s/follow/";
        String unfollow = "/api/v1/users/%s/unfollow/";
        String getUserInfo = "/api/v1/users/%s/";
        String friend_list = "/api/v1/users/%s/friends/?page_size=999";
        String get_pro_verification = "/api/v1/accounts/pro_verfications/mine/";


    }

    public interface FlowExchange {
        String overview = "/api/v1/coins/exchange/overview/";
        String packages = "/api/v1/coins/exchange/packages/";
        String recharge = "/api/v1/coins/exchange/recharge/";
        String invitecode = "/api/v1/accounts/invitations/invite/";
        String history = "/api/v1/coins/exchange/history/";
    }


    public interface MainPage {
        String zhishu = "/api/v1/symbols/{0}/quote/";
    }

    public interface Plate {
        String hotPlate = "/api/v1/symbols/sectors/?sector_root_id=26";
    }


    public interface Wallets {
        //GET /api/v1/wallets/account/mine/

        //POST 用户钱包充值
        // amount (decimal, 充值金额),
        //  vendor (string, 第三方支付渠道，weixinpay,微信／alipay,支付宝／yibao,易宝)
        String payment = "/api/v1/wallets/recharge/payment/";
        /**
         * 查询余额信息
         */
        String account_info = "/api/v1/wallets/account/mine/";
        String wallet_exchange = "/api/v1/wallets/account/changes/";
        String rewards_balance = "/api/v1/statuses/rewards/balance/";
        String withdraw = "/api/v1/wallets/withdraw/";
    }

    // DKHSUrl.Portfolio.rankingList,
    public static String[] storeURLList = {DKHSUrl.Plate.hotPlate, DKHSUrl.Portfolio.portfolio,
            DKHSUrl.StockSymbol.optional, DKHSUrl.Portfolio.champion, DKHSUrl.StockSymbol.opitionmarket,
            DKHSUrl.StockSymbol.stocklist,

    };

    public interface Status {
        /**
         * 上传图片
         */
        String uploadImage = "/api/v1/media/upload/";
        /**
         * 获取话题列表
         */
        String statuses = "/api/v1/statuses/";
        /**
         * 获取评论列表
         */
        String get_comments = "/api/v1/statuses/{0}/comments/";
        /**
         * 获取个人回复
         */
        String get_replys = "/api/v1/statuses/user_timeline/";


        String getCommentMeList = "/api/v1/statuses/comments_timeline/";


        String getCallMeList = "/api/v1/statuses/mentions_timeline/";
        /**
         * 举报
         */
        String abuse_reports = "/api/v1/statuses/abuse_reports/report/";

        /**
         * 获取推荐悬赏/话题
         */
        String get_recommend = "/api/v1/statuses/recommend/";
    }

    public interface Search {
        /**
         * 综合搜索
         */
        String search_general = "/api/v1/search/suggestions/";
        /**
         * 搜索股票和基金
         */
        String search_symools = "/api/v1/search/symbols/";
        /**
         * 搜索组合
         */
        String search_portfolios = "/api/v1/search/portfolios/";
        /**
         * 搜索基金经理
         */
        String search_fund_managers = "/api/v1/search/fund_managers/";
        /**
         * 搜索用户
         */
        String search_users = "/api/v1/search/users/";
        /**
         * 搜索悬赏和话题
         */
        String search_statues = "/api/v1/search/statuses/";
    }

    public interface Org_profiles {
        //所属机构
        String Org_profiles_list = "/api/v1/accounts/org_profiles/";
    }

    /**
     * 牛人认证
     */
    public interface Pro_verfications {
        String pro_verfications_url = "/api/v1/accounts/pro_verfications/";
        String query_pro_verfications_url = "/api/v1/accounts/pro_verfications/mine/";
    }
    public interface Funds{
        String get_verifications = "/api/v1/accounts/identity_verfications/mine/";
        String verify_identy = "/api/v1/funds/identity_auth/verify/";
        String checkIdentity = "/api/v1/funds/identity_auth/check/";
        String reset_trade_password = "/api/v1/accounts/reset_trade_password/";
        String bind_bank_card = "/api/v1/funds/bind_bank_card/";
        String get_banks = "/api/v1/accounts/banks/";
        String is_trade_password_set = "/api/v1/accounts/is_trade_password_set/";
        String set_trade_password = "/api/v1/accounts/set_trade_password/";
        String check_trade_password = "/api/v1/accounts/check_trade_password/";
        String change_trade_password = "/api/v1/accounts/change_trade_password/";
        String get_my_bank_cards = "/api/v1/accounts/bank_cards/mine/";
        String get_my_assests = "/api/v1/funds/assets/mine/";
        String get_my_funds = "/api/v1/funds/fund/mine/";
        String get_my_fundinfo = "/api/v1/funds/fund/fund_info/";
        String get_funds_trades = "/api/v1/funds/trades/";
        String get_funds_trades_info = "/api/v1/funds/trades/{0}/";
        String buy_fund = "/api/v1/funds/buy/";
        String sell_fund = "/api/v1/funds/sell/";
        String check_bank_card = "/api/v1/accounts/bank_cards/check/";
        String bank_agreement = "/accounts/bank_transfer_agreement/";

    }

}
