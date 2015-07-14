package com.dkhs.portfolio.bean;

import android.content.Context;

import com.dkhs.portfolio.R;

import java.util.LinkedList;

/**
 * @author zwm
 * @version 2.0
 * @ClassName FundTypeBean
 * @Description TODO(基金过滤条件基础类)
 * @date 2015/5/29.
 */
public class MenuBean {

    private boolean enable;
    private String key;

    private String value;

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public boolean isEnable() {
        return enable;
    }


    public void setEnable(boolean enable) {
        this.enable = enable;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }


    public static LinkedList<MenuBean> fundTypeFromXml(Context ctx) {

        LinkedList<MenuBean> list = new LinkedList<>();

        String[] key = ctx.getResources().getStringArray(R.array.fund_type_keys);
        int[] stypeValue = ctx.getResources().getIntArray(R.array.fund_stype_values);
        String[] chi = ctx.getResources().getStringArray(R.array.fund_type_chis);


        for (int i = 0; i < chi.length; i++) {
            FundTypeMenuBean item = new FundTypeMenuBean();
            item.setEnable(true);
            item.setKey(key[i]);
            item.setCode(stypeValue[i]);
            item.setValue(chi[i]);
            list.add(item);
        }
        return list;
    }

    public static LinkedList<MenuBean> fundSortFromXml(Context ctx) {

        LinkedList<MenuBean> list = new LinkedList<>();

        String[] key = ctx.getResources().getStringArray(R.array.fund_sort_keys);
        String[] value = ctx.getResources().getStringArray(R.array.fund_sort_values);


        for (int i = 0; i < key.length; i++) {
            SortTypeMenuBean item = new SortTypeMenuBean();
            item.setEnable(true);
            item.setKey(key[i]);
            item.setValue(value[i]);
            list.add(item);
        }
        return list;
    }
    public static LinkedList<MenuBean> sepFundSortFromXml(Context ctx) {

        LinkedList<MenuBean> list = new LinkedList<>();

        String[] key = ctx.getResources().getStringArray(R.array.sep_fund_sort_keys);
        String[] value = ctx.getResources().getStringArray(R.array.sep_fund_sort_values);
        for (int i = 0; i < key.length; i++) {
            SortTypeMenuBean item = new SortTypeMenuBean();
            item.setEnable(true);
            item.setKey(key[i]);
            item.setValue(value[i]);
            list.add(item);
        }
        return list;
    }
    public static LinkedList<MenuBean> fundManagerSortFromXml(Context ctx) {

        LinkedList<MenuBean> list = new LinkedList<>();

        String[] key = ctx.getResources().getStringArray(R.array.fund_manager_sort_keys);
        String[] value = ctx.getResources().getStringArray(R.array.fund_manager_sort_values);
        for (int i = 0; i < key.length; i++) {
            SortTypeMenuBean item = new SortTypeMenuBean();
            item.setEnable(true);
            item.setKey(key[i]);
            item.setValue(value[i]);
            list.add(item);
        }
        return list;
    }
    public static LinkedList<MenuBean> fundManagerFromXml(Context ctx) {

        LinkedList<MenuBean> list = new LinkedList<>();

        String[] key = ctx.getResources().getStringArray(R.array.manager_key);
        String[] value = ctx.getResources().getStringArray(R.array.manager_value);


        for (int i = 0; i < key.length; i++) {
            FundManagerSortMenuBean item = new FundManagerSortMenuBean();
            item.setEnable(true);
            item.setKey(key[i]);
            item.setValue(value[i]);
            list.add(item);
        }
        return list;
    }

}
