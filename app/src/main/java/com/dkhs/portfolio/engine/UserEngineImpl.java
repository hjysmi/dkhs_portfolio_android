package com.dkhs.portfolio.engine;

import android.os.AsyncTask;

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
import com.dkhs.portfolio.security.SecurityUtils;
import com.dkhs.portfolio.utils.PortfolioPreferenceManager;
import com.google.gson.Gson;
import com.lidroid.xutils.DbUtils;
import com.lidroid.xutils.exception.DbException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;

import java.io.File;
import java.util.TreeMap;

/**
 * @author zcm
 * @version 1.0
 * @ClassName UserEngineImpl
 * @Description TODO(这里用一句话描述这个类的作用)
 * @date 2014-10-08 下午2:25:01
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

//        RequestParams params = new RequestParams();
//        if (logintype == ConstantValue.IS_CAPTCHA) {
//            params.addBodyParameter("mobile", username);
//            params.addBodyParameter("captcha", password);
//        } else if (logintype == ConstantValue.IS_MOBILE) {
//            params.addBodyParameter("mobile", username);
//            params.addBodyParameter("password", password);
//        } else if (logintype == ConstantValue.IS_EMAIL) {
//            params.addBodyParameter("email", username);
//            params.addBodyParameter("password", password);
//        }
//        DKHSClient.request(HttpMethod.POST, DKHSUrl.User.login, params, listener);
        TreeMap<String, String> paramsMap = new TreeMap<String, String>();
        if (logintype == ConstantValue.IS_CAPTCHA) {
            paramsMap.put("mobile", username);
            paramsMap.put("captcha", password);
        } else if (logintype == ConstantValue.IS_MOBILE) {
            paramsMap.put("mobile", username);
            paramsMap.put("password", password);
        } else if (logintype == ConstantValue.IS_EMAIL) {

            paramsMap.put("email", username);
            paramsMap.put("password", password);
        }

        DKHSClient.requestPostByEncryp(paramsMap, DKHSUrl.User.login, listener.openEncry());


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
//        RequestParams params = new RequestParams();
//        params.addBodyParameter("mobile", mobile);
//        params.addBodyParameter("captcha", captha);
//        params.addBodyParameter("password", password);
//        params.addBodyParameter("username", username);
//        DKHSClient.request(HttpMethod.POST, DKHSUrl.User.register, params, listener);

        TreeMap<String, String> paramsMap = new TreeMap<String, String>();

        paramsMap.put("mobile", mobile);
        paramsMap.put("captcha", captha);
        paramsMap.put("password", password);
        paramsMap.put("username", username);

        DKHSClient.requestPostByEncryp(paramsMap, DKHSUrl.User.register, listener.openEncry());

    }

    public void registerThreePlatform(String username, String openid, String provider, ThreePlatform extraData,
                                      ParseHttpListener<UserEntity> listener) {
//        RequestParams params = new RequestParams();
//
//        params.addBodyParameter("provider", provider);
//        params.addBodyParameter("openid", openid);
//        params.addBodyParameter("username", username);
//        params.addBodyParameter("extra_data", new Gson().toJson(extraData));
//        DKHSClient.request(HttpMethod.POST, DKHSUrl.User.register, params, listener);
//


        TreeMap<String, String> paramsMap = new TreeMap<String, String>();

        paramsMap.put("provider", provider);
        paramsMap.put("openid", openid);
        paramsMap.put("username", username);
        paramsMap.put("extra_data", new Gson().toJson(extraData));

        DKHSClient.requestPostByEncryp(paramsMap, DKHSUrl.User.register, listener.openEncry());
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
        GlobalParams.LOGIN_USER = entity;

        PortfolioPreferenceManager.saveValue(PortfolioPreferenceManager.KEY_USERNAME, entity.getUsername());
        PortfolioPreferenceManager.saveValue(PortfolioPreferenceManager.KEY_USERID, entity.getId() + "");
        PortfolioPreferenceManager.saveValue(PortfolioPreferenceManager.KEY_USER_HEADER_URL, entity.getAvatar_md());

        saveUser(entity);
    }

    private void saveUser(final UserEntity user) {

        new AsyncTask<Void, Void, Boolean>() {

            @Override
            protected Boolean doInBackground(Void... params) {
                UserEntity entity = SecurityUtils.encrypt(user);
                DbUtils dbutil = DbUtils.create(PortfolioApplication.getInstance());
                UserEntity dbentity;
                try {
                    dbentity = dbutil.findFirst(UserEntity.class);
                    if (dbentity != null) {
                        dbutil.delete(dbentity);
                    }
                    dbutil.save(entity);
                    return true;

                } catch (DbException e) {

                    e.printStackTrace();
                    return false;
                }

            }

            @Override
            protected void onPostExecute(Boolean aBoolean) {
                // if (aBoolean) {
                // // 及时通知app连接融云服务器
                // BusProvider.getInstance().post(new RongConnectEvent());
                // }
                super.onPostExecute(aBoolean);
            }
        }.execute();

    }

    public boolean hasUserLogin() {
        UserEntity user = getUserEntity();
        return user != null;

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
     * 获取 用户的token 值
     *
     * @param user_id
     * @param nickName
     * @param portrait_uri
     * @param listener
     */
    public void getToken(String user_id, String nickName, String portrait_uri, BasicHttpListener listener) {

        RequestParams params = new RequestParams();
        params.addQueryStringParameter("user_id", user_id);
        params.addQueryStringParameter("name", nickName);
        params.addQueryStringParameter("portrait_uri", portrait_uri);
        DKHSClient.request(HttpMethod.GET, DKHSUrl.User.get_token, params, listener);

    }

    public static UserEntity getUserEntity() {
        if (GlobalParams.LOGIN_USER != null) {
            return GlobalParams.LOGIN_USER;
        }
        try {
            DbUtils dbUtils = DbUtils.create(PortfolioApplication.getInstance());
            if (null != dbUtils) {
                UserEntity user = dbUtils.findFirst(UserEntity.class);
                if (null != user) {
                    GlobalParams.LOGIN_USER = SecurityUtils.decrypt(user);
                }

            }
            return GlobalParams.LOGIN_USER;
        } catch (DbException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 关注
     */
    public void follow(String follow_id, BasicHttpListener listener) {

        RequestParams params = new RequestParams();

        DKHSClient.request(HttpMethod.POST, String.format(DKHSUrl.User.follow, follow_id), params, listener);

    }

    /**
     * 取消关注
     */
    public void unfollow(String unfollow_id, BasicHttpListener listener) {

        RequestParams params = new RequestParams();

        DKHSClient.request(HttpMethod.POST, String.format(DKHSUrl.User.unfollow, unfollow_id), params, listener);

    }

    /**
     * g
     */
    public void getUserInfo(String pk, BasicHttpListener listener) {
        RequestParams params = new RequestParams();
        DKHSClient.requestSync(HttpMethod.GET, String.format(DKHSUrl.User.getUserInfo, pk), params, listener);
    }


}
