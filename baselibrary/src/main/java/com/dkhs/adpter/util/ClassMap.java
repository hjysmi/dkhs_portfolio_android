package com.dkhs.adpter.util;

import java.util.HashMap;

import com.dkhs.adpter.listener.ItemHandler;

/**
 * @author zwm
 * @version 2.0
 * @ClassName ClassMapList
 * @Description TODO(这里用一句话描述这个类的作用)
 * @date 2015/7/16.
 */
public class ClassMap {

    private HashMap<String,Integer> classIndex=new HashMap<>();
    private HashMap<Integer, ItemHandler> handler =new HashMap<>();
    private int index=0;


    public void add(ItemHandler itemHandler){
        classIndex.put(itemHandler.getDataClass().toString(),index);
        handler.put(index, itemHandler);
        index++;
    }
    public void add(String classToString,ItemHandler itemHandler){
        classIndex.put(classToString,index);
        handler.put(index, itemHandler);
        index++;
    }
    public void remove(ItemHandler itemHandler){
        int i=classIndex.get(itemHandler.getDataClass().toString());
        handler.remove(i);
        classIndex.remove(itemHandler.getDataClass().toString());

    }

    public ItemHandler get(Class<?> cla){
        return  handler.get(classIndex.get(cla.toString()));
    }
    public ItemHandler get(String cla){
        return  handler.get(classIndex.get(cla));
    }
    public ItemHandler get(int viewType){
        return  handler.get(viewType);
    }

    public void clern(){
        classIndex.clear();
        handler.clear();
        index=0;
    }

    public int  size(){
      return   classIndex.size();
    }

    public int getViewType(Class<?> cls){
        return   classIndex.get(cls.toString());
    }
    public int getViewType(String cls){
        return   classIndex.get(cls);
    }

}
