package com.dkhs.portfolio.engine;

import java.io.File;

import com.dkhs.portfolio.app.PortfolioApplication;
import com.dkhs.portfolio.bean.FeedBackBean;
import com.dkhs.portfolio.bean.ThreePlatform;
import com.dkhs.portfolio.bean.UserEntity;
import com.dkhs.portfolio.common.ConstantValue;
import com.dkhs.portfolio.common.GlobalParams;
import com.dkhs.portfolio.net.BasicHttpListener;
import com.dkhs.portfolio.net.DKHSClient;
import com.dkhs.portfolio.net.DKHSUrl;
import com.dkhs.portfolio.net.IHttpListener;
import com.dkhs.portfolio.net.ParseHttpListener;
import com.dkhs.portfolio.utils.PortfolioPreferenceManager;
import com.dkhs.portfolio.utils.UserEntityDesUtil;
import com.google.gson.Gson;
import com.lidroid.xutils.DbUtils;
import com.lidroid.xutils.exception.DbException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;

/**
 * @ClassName UserEngineImpl
 * @Description TODO(这里用一句话描述这个类的作用)
 * @author zcm
 * @date 2014-10-08 下午2:25:01
 * @version 1.0
 */
public class UserEngineImpl {

    /**
     * 登录
     * 
     * @param username
     * @param password
     * @param logintype
     * @param listener
     */
    public void login(String username, String password, int logintype, ParseHttpListener<UserEntity> listener) {
        RequestParams params = new RequestParams();
        if (logintype == ConstantValue.IS_CAPTCHA) {
            params.addBodyParameter("mobile", username);
            params.addBodyParameter("captcha", password);
        } else if (logintype == ConstantValue.IS_MOBILE) {
            params.addBodyParameter("mobile", username);
            params.addBodyParameter("password", password);
        } else if (logintype == ConstantValue.IS_EMAIL) {
            params.addBodyParameter("email", username);
            params.addBodyParameter("password", password);
        }
        DKHSClient.request(HttpMethod.POST, DKHSUrl.User.login, params, listener);
    }



    /**
     * 设置密码
     * 
     * @param password
     * @param captcha
     * @param listener
     */
    public void setPassword(String mobile, String password, String captcha, IHttpListener listener) {
        RequestParams params = new RequestParams();
        params.addBodyParameter("password", password);
        params.addBodyParameter("captcha", captcha);
        params.addBodyParameter("mobile", mobile);
        DKHSClient.request(HttpMethod.POST, DKHSUrl.User.setpassword, params, listener);
    }

    /**
     * 修改密码
     * 
     * @param oldpassword
     * @param newpassword
     * @param listener
     */
    public void changePassword(String oldpassword, String newpassword, ParseHttpListener<Object> listener) {
        RequestParams params = new RequestParams();
        params.addBodyParameter("old_password", oldpassword);
        params.addBodyParameter("new_password", newpassword);
        DKHSClient.request(HttpMethod.POST, DKHSUrl.User.changepassword, params, listener);
    }

    /**
     * 是否设置过密码
     * 
     * @param mobile
     * @param listener
     */
    public void isSetPassword(String mobile, IHttpListener listener) {
        RequestParams params = new RequestParams();
        params.addQueryStringParameter("mobile", mobile);
        DKHSClient.request(HttpMethod.GET, DKHSUrl.User.is_setpassword, params, listener);
    }

    /**
     * 获取验证码
     * 
     * @param mobile
     * @param listener
     */
    public void getVericode(String mobile, ParseHttpListener<Object> listener) {
        RequestParams params = new RequestParams();
        params.addQueryStringParameter("mobile", mobile);
        DKHSClient.request(HttpMethod.GET, DKHSUrl.User.get_vericode, params, listener);
    }

    public void register(String mobile, String password, String captha, String username,
            ParseHttpListener<UserEntity> listener) {
        RequestParams params = new RequestParams();
        params.addBodyParameter("mobile", mobile);
        params.addBodyParameter("captcha", captha);
        params.addBodyParameter("password", password);
        params.addBodyParameter("username", username);
        DKHSClient.request(HttpMethod.POST, DKHSUrl.User.register, params, listener);
    }

    public void registerThreePlatform(String username, String openid, String provider, ThreePlatform extraData,
            ParseHttpListener<UserEntity> listener) {
        RequestParams params = new RequestParams();

        params.addBodyParameter("provider", provider);
        params.addBodyParameter("openid", openid);
        params.addBodyParameter("username", username);
        params.addBodyParameter("extra_data", new Gson().toJson(extraData));
        DKHSClient.request(HttpMethod.POST, DKHSUrl.User.register, params, listener);
    }

    public static void bindThreePlatform(String openid, String provider, ThreePlatform extraData,
            BasicHttpListener listener) {
        RequestParams params = new RequestParams();

        params.addBodyParameter("provider", provider);
        params.addBodyParameter("openid", openid);
        params.addBodyParameter("extra_data", new Gson().toJson(extraData));
        DKHSClient.request(HttpMethod.POST, DKHSUrl.User.bingdings, params, listener);
    }

    public static void queryThreePlatBind(BasicHttpListener listener) {
        DKHSClient.request(HttpMethod.GET, DKHSUrl.User.bingdings, null, listener);
    }

    public void checkMobile(String mobile, IHttpListener listener) {
        DKHSClient.requestByGet(listener, DKHSUrl.User.checkMobile, mobile);
    }

    public void checkVericode(String mobile, String captcha, IHttpListener listener) {
        RequestParams params = new RequestParams();
        params.addQueryStringParameter("mobile", mobile);
        params.addQueryStringParameter("captcha", captcha);
        DKHSClient.request(HttpMethod.GET, DKHSUrl.User.check_vericode, params, listener);
    }

    public void boundEmail(String email, IHttpListener listener) {
        DKHSClient.requestByGet(listener, DKHSUrl.User.boundemail, email);
    }

    public void setUserName(String username, ParseHttpListener<String> listener) {
        RequestParams params = new RequestParams();
        params.addBodyParameter("username", username);
        DKHSClient.request(HttpMethod.POST, DKHSUrl.User.setUserName, params, listener);
    }

    public void saveLoginUserInfo(UserEntity entity) {
        GlobalParams.ACCESS_TOCKEN = entity.getAccess_token();

        PortfolioPreferenceManager.saveValue(PortfolioPreferenceManager.KEY_USERNAME, entity.getUsername());
        PortfolioPreferenceManager.saveValue(PortfolioPreferenceManager.KEY_USERID, entity.getId() + "");
        PortfolioPreferenceManager.saveValue(PortfolioPreferenceManager.KEY_USER_HEADER_URL, entity.getAvatar_md());

        saveUser(entity);
    }

    private void saveUser(final UserEntity user) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                // TODO Auto-generated method stub
                UserEntity entity = UserEntityDesUtil.decode(user, "DECODE", ConstantValue.DES_PASSWORD);
                DbUtils dbutil = DbUtils.create(PortfolioApplication.getInstance());
                UserEntity dbentity;
                try {
                    dbentity = dbutil.findFirst(UserEntity.class);
                    if (dbentity != null) {
                        dbutil.delete(dbentity);
                    }
                    dbutil.save(entity);
                } catch (DbException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        }).start();
    }

    public boolean hasUserLogin() {

        try {
            UserEntity user = DbUtils.create(PortfolioApplication.getInstance()).findFirst(UserEntity.class);
            if (user == null) {
                return false;
            } else {
                return true;
            }
        } catch (DbException e) {
            e.printStackTrace();

            return false;
        }
    }

    public void setUserHead(File file, ParseHttpListener<UserEntity> listener) {
        RequestParams params = new RequestParams();
        params.addBodyParameter("avatar", file);
        DKHSClient.requestLong(HttpMethod.POST, DKHSUrl.User.setUserHead, params, listener);
    }

    public void getSettingMessage(ParseHttpListener<UserEntity> listener) {
        DKHSClient.request(HttpMethod.GET, DKHSUrl.User.settingMessage, null, listener);
    }

    public void setSettingMessage(String description, ParseHttpListener<UserEntity> listener) {
        RequestParams params = new RequestParams();
        params.addBodyParameter("description", description);
        DKHSClient.request(HttpMethod.POST, DKHSUrl.User.settingMessage, params, listener);
    }

    public void getBaseUserInfo(String userId, IHttpListener listener) {
        DKHSClient.requestByGet(listener, DKHSUrl.User.base_userinfo, userId);

    }

    public void getAppVersion(String appcode, ParseHttpListener<Object> listener) {
        RequestParams params = new RequestParams();
        params.addQueryStringParameter("app_code", appcode);
        // DKHSClient.requestByGet(DKHSUrl.News.newstext +id, null, this);
        DKHSClient.request(HttpMethod.GET, DKHSUrl.User.get_version + appcode, null, listener);
    }

    public void setFeedBack(String app, String version, String content, String contact, File file,
            ParseHttpListener<FeedBackBean> listener) {
        RequestParams params = new RequestParams();
        if (null != app)
            params.addBodyParameter("app_code", app);
        if (null != version)
            params.addBodyParameter("version", version);
        if (null != content)
            params.addBodyParameter("content", content);
        if (null != contact)
            params.addBodyParameter("contact", contact);
        if (null != file)
            params.addBodyParameter("image", file);
        DKHSClient.requestLong(HttpMethod.POST, DKHSUrl.User.add_feed, params, listener);
    }

    public void bindMobile(String mobile, String captcha, BasicHttpListener listener) {
        RequestParams params = new RequestParams();
        params.addBodyParameter("mobile", mobile);
        params.addBodyParameter("captcha", captcha);
        DKHSClient.request(HttpMethod.POST, DKHSUrl.User.bind_mobile, params, listener);
    }

    /**
     *   获取  用户的token 值
     *   todo 未测试
     * @param user_id
     * @param nickName
     * @param portrait_uri
     * @param listener
     */
    public  void getToken(String user_id,String nickName,String portrait_uri,BasicHttpListener listener  ){

        RequestParams params = new RequestParams();
        params.addBodyParameter("user_id", user_id);
        params.addBodyParameter("name", nickName);
//        params.addBodyParameter("portrait_uri",portrait_uri);
        params.addBodyParameter("portrait_uri", "http://su.bdimg.com/static/superplus/img/logo_white_ee663702.png");
        DKHSClient.request(HttpMethod.POST, DKHSUrl.User.get_token, params, listener);

    }

}
