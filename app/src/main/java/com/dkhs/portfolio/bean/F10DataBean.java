package com.dkhs.portfolio.bean;

import java.util.List;

/**
 * Created by zjz on 2015/5/13.
 */
public class F10DataBean {


    /**
     * title : 公司简介
     * values : [["公司名称","武汉钢铁有限公司"],["上市日期","2015-09-08"]]
     * header : [{"title":"","width":"80px","align":"left"},{"title":"","width":"","align":"left"}]
     * sub_title :
     */

   

    private String title;
    private List<List<String>> values;
    private List<HeaderEntity> header;
    private String sub_title;

    public void setTitle(String title) {
        this.title = title;
    }

    public void setValues(List<List<String>> values) {
        this.values = values;
    }

    public void setHeader(List<HeaderEntity> header) {
        this.header = header;
    }

    public void setSub_title(String sub_title) {
        this.sub_title = sub_title;
    }

    public String getTitle() {
        return title;
    }

    public List<List<String>> getValues() {
        return values;
    }

    public List<HeaderEntity> getHeader() {
        return header;
    }

    public String getSub_title() {
        return sub_title;
    }

    public class HeaderEntity {
        /**
         * title :
         * width : 80px
         * align : left
         */
        private String title;
        private float width;
        private String align;

        public void setTitle(String title) {
            this.title = title;
        }

        public void setWidth(float width) {
            this.width = width;
        }

        public void setAlign(String align) {
            this.align = align;
        }

        public String getTitle() {
            return title;
        }

        public float getWidth() {
            return width;
        }

        public String getAlign() {
            return align;
        }
    }


    public enum ALIGN {
        CENTER("center"),
        LEFT("left"),
        RIGHT("right");

        private String type;

        ALIGN(String type) {
            this.type = type;
        }

        public String getType() {
            return type;
        }
    }
}
