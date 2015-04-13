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

    private String itemId;
    private String itemName;
    private String itemDesc;
    private boolean itemTixing;
    private int itemSortId;

    public String getItemId() {
        return itemId;
    }

    public void setItemId(String itemId) {
        this.itemId = itemId;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public String getItemDesc() {
        return itemDesc;
    }

    public void setItemDesc(String itemDesc) {
        this.itemDesc = itemDesc;
    }

    public boolean isItemTixing() {
        return itemTixing;
    }

    public void setItemTixing(boolean itemTixing) {
        this.itemTixing = itemTixing;
    }

    public int getItemSortId() {
        return itemSortId;
    }

    public void setItemSortId(int itemSortId) {
        this.itemSortId = itemSortId;
    }

}
