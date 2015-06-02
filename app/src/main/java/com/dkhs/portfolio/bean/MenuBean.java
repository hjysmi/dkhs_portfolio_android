package com.dkhs.portfolio.bean;

import android.content.Context;

import com.dkhs.portfolio.R;

import java.util.ArrayList;
import java.util.List;

/**
 * @author zwm
 * @version 2.0
 * @ClassName FundTypeBean
 * @Description TODO(这里用一句话描述这个类的作用)
 * @date 2015/5/29.
 */
public class MenuBean {

    private boolean enable;
    private String key ;

    private String value;
    private String extra;

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public void setExtra(String extra) {
        this.extra = extra;
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

    public String getExtra() {
        return extra;
    }

    public static List<MenuBean>  fundTypeFromXml(Context ctx){
        
        List<MenuBean>  list=new ArrayList<>();
        
        String[] key=ctx.getResources().getStringArray(R.array.fund_type_keys);
        String[] value=ctx.getResources().getStringArray(R.array.fund_type_values);
        String[] chi=ctx.getResources().getStringArray(R.array.fund_type_chis);


        for (int i = 0; i < chi.length; i++) {
          MenuBean item=new MenuBean();
            item.setEnable(true);
            item.setKey(key[i]);
            item.setValue(value[i]);
            item.setExtra(chi[i]);
            list.add(item);
        }
        return list;
    }
    public static List<MenuBean>  fundSortFromXml(Context ctx){

        List<MenuBean>  list=new ArrayList<>();

        String[] key=ctx.getResources().getStringArray(R.array.fund_sort_keys);
        String[] value=ctx.getResources().getStringArray(R.array.fund_sort_values);


        for (int i = 0; i < key.length; i++) {
          MenuBean item=new MenuBean();
            item.setEnable(true);
            item.setKey(key[i]);
            item.setValue(value[i]);
            list.add(item);
        }
        return list;
    }
}
