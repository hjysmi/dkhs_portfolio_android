package com.dkhs.portfolio.bean;

/**
 * @author zwm
 * @version 2.0
 * @ClassName ShareBean
 * @Description TODO(这里用一句话描述这个类的作用)
 * @date 2015/6/18.
 */
public class ShareBean {


    /**
     *
     * content : 领取免费流量，下载谁牛app，输入邀请码17350926,即可领取100M免费流量
     * title : 谁牛－免费流量跟踪牛股
     * img : https://www.dkhs.com/static/portfolio/img/shuiniuwap/favicon.png
     * code : 17350926
     * url : https://www.dkhs.com/portfolio/wap/?invite_code=17350926
     */
    private String content;
    private String title;
    private String img;
    private String code;
    private String url;

    private int resId;

    public int getResId() {
        return resId;
    }

    public void setResId(int resId) {
        this.resId = resId;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getContent() {
        return content;
    }

    public String getTitle() {
        return title;
    }

    public String getImg() {
        return img;
    }

    public String getCode() {
        return code;
    }

    public String getUrl() {
        return url;
    }
}
