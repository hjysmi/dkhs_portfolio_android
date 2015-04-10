/**
 * @Title DragListItem.java
 * @Package com.dkhs.portfolio.ui.draglist
 * @Description TODO(用一句话描述该文件做什么)
 * @author zjz
 * @date 2015-4-9 下午3:17:15
 * @version V1.0
 */
package com.dkhs.portfolio.bean;

/**
 * @ClassName DragListItem
 * @Description TODO(这里用一句话描述这个类的作用)
 * @author zjz
 * @date 2015-4-9 下午3:17:15
 * @version 1.0
 */
public class DragListItem {

    private String id;
    private String name;
    private String desc;
    private boolean isAlert;
    private int sortId;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public boolean isAlert() {
        return isAlert;
    }

    public void setAlert(boolean isAlert) {
        this.isAlert = isAlert;
    }

    public int getSortId() {
        return sortId;
    }

    public void setSortId(int sortId) {
        this.sortId = sortId;
    }

}
