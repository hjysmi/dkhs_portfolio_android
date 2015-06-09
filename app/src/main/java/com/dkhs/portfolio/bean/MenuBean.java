package com.dkhs.portfolio.bean;

import android.content.Context;

import com.dkhs.portfolio.R;

import java.util.ArrayList;
import java.util.List;

/**
 * @author zwm
 * @version 2.0
 * @ClassName FundTypeBean
 * @Description TODO(基金过滤条件基础类)
 * @date 2015/5/29.
 */
public class MenuBean {

    private boolean enable;
    private String key ;

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


    public static List<MenuBean>  fundTypeFromXml(Context ctx){
        
        List<MenuBean>  list=new ArrayList<>();
        
        String[] key=ctx.getResources().getStringArray(R.array.fund_type_keys);
        String[] value=ctx.getResources().getStringArray(R.array.fund_type_values);
        String[] chi=ctx.getResources().getStringArray(R.array.fund_type_chis);


        for (int i = 0; i < chi.length; i++) {
            FundTypeMenuBean item=new FundTypeMenuBean();
            item.setEnable(true);
            item.setKey(key[i]);
            item.setCode(value[i]);
            item.setValue(chi[i]);
            list.add(item);
        }
        return list;
    }
    public static List<MenuBean>  fundSortFromXml(Context ctx){

        List<MenuBean>  list=new ArrayList<>();

        String[] key=ctx.getResources().getStringArray(R.array.fund_sort_keys);
        String[] value=ctx.getResources().getStringArray(R.array.fund_sort_values);


        for (int i = 0; i < key.length; i++) {
            SortTypeMenuBean item=new SortTypeMenuBean();
            //fixme 任职起，目前API上还没有实现，所以暂时这块先禁用。  Strings  要替换成服务器的定义的字段
           if(key[i].equals("任职起")){
               item.setEnable(false);
           }else{
               item.setEnable(true);
           }
            item.setKey(key[i]);
            item.setValue(value[i]);
            list.add(item);
        }
        return list;
    }

}