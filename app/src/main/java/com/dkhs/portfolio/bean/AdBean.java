package com.dkhs.portfolio.bean;

import java.util.List;

/**
 * @author zwm
 * @version 2.0
 * @ClassName AdBean
 * @Description TODO(这里用一句话描述这个类的作用)
 * @date 2015/6/18.
 */
public class AdBean {


    /**
     * id : 5
     * title : 程序启动画面
     * description : 程序启动画面
     * code : splash
     * ads : [{"redirect_url":"https://www.dkhs.com","id":12,"title":"APP启动页广告","display_time":3,"description":"APP启动页广告","image":"http://com-dkhs-media-test.oss.aliyuncs.com/a/2015/06/18/09/4219/splash.jpg"}]
     */
    private int id;
    private String title;
    private String description;
    private String code;
    private List<AdsEntity> ads;

    public void setId(int id) {
        this.id = id;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public void setAds(List<AdsEntity> ads) {
        this.ads = ads;
    }

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public String getCode() {
        return code;
    }

    public List<AdsEntity> getAds() {
        return ads;
    }

    public class AdsEntity {
        /**
         * redirect_url : https://www.dkhs.com
         * id : 12
         * title : APP启动页广告
         * display_time : 3
         * description : APP启动页广告
         * image : http://com-dkhs-media-test.oss.aliyuncs.com/a/2015/06/18/09/4219/splash.jpg
         */
        private String redirect_url;
        private int id;
        private String title;
        private int display_time;
        private String description;
        private String image;

        public void setRedirect_url(String redirect_url) {
            this.redirect_url = redirect_url;
        }

        public void setId(int id) {
            this.id = id;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public void setDisplay_time(int display_time) {
            this.display_time = display_time;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public void setImage(String image) {
            this.image = image;
        }

        public String getRedirect_url() {
            return redirect_url;
        }

        public int getId() {
            return id;
        }

        public String getTitle() {
            return title;
        }

        public int getDisplay_time() {
            return display_time;
        }

        public String getDescription() {
            return description;
        }

        public String getImage() {
            return image;
        }
    }
}
