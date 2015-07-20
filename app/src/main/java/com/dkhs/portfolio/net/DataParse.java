/**
 * @Title: DataParse.java
 * @Package com.naerju.network
 * @Description: 接口数据解析类
 * @author zhoujunzhou
 * @date 2014-4-3 上午10:21:42
 * @version V1.0
 */
package com.dkhs.portfolio.net;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author zhoujunzhou
 * @version 1.0
 * @ClassName: DataParse
 * @Description: 接口数据解析类
 * @date 2014-09-12 上午10:21:42
 */
public class DataParse<T> {
//    private JSONObject result;

//    public DataParse(JSONObject jsonObject) {
//        this.result = jsonObject;
//    }

    // public T parse(String keyString, Class<T> classOfT) {
    // T s = null;
    //
    // if (result.optInt(HttpCode.ERROR_CODE, HttpCode.UNKOWN) == HttpCode.OK) {
    // Gson gson = new GsonBuilder().enableComplexMapKeySerialization().create();
    // if (result.has(keyString)) {
    // JSONObject userObject = result.optJSONObject(keyString);
    // s = gson.fromJson(userObject.toString(), classOfT);
    // }
    // } else if (result.optString(HttpCode.ERROR_MSG) != null) {
    // // GTGUtils.showTip(result.getString("errorMsg"));
    // }
    // return s;
    //
    // }
    //
    // /**
    // *
    // * Type listType = new TypeToken<List<YourTypeT>>() { }.getType();
    // * 解析List类型数据
    // *
    // * @param keyString
    // * 要解析的json节点名称
    // * @param listType
    // * 要解析的数据对象类型
    // */
    // public List<T> parseList(String keyString, Type listType) {
    // List<T> dataList = null;
    // if (result.optInt(HttpCode.ERROR_CODE, HttpCode.UNKOWN) == HttpCode.OK) {
    // Gson gson = new GsonBuilder().enableComplexMapKeySerialization().create();
    // if (result.has(keyString)) {
    // String userObject = result.optString(keyString);
    // // Type type = new TypeToken<List<T>>() {
    // // }.getType();
    // dataList = gson.fromJson(userObject, listType);
    // }
    // }
    // return dataList;
    //
    // }
    public static <K> List<K> parseJsonList(String userObject, Type listType) {
        try {

            List<K> dataList = null;

            Gson gson = new GsonBuilder().enableComplexMapKeySerialization().create();

            // Type listType = new TypeToken<List<T>>() {
            // }.getType();

            dataList = gson.fromJson(userObject, listType);

            return dataList;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return Collections.emptyList();
    }

    /**
     * 将对象转换成json格式
     *
     * @param ts
     * @return
     */
    public static String objectToJson(Object ts) {
        return new Gson().toJson(ts);
    }


    /**
     * 从{@code jsonObj}中解析出{@code key}结点下的JSONArray对象，并将JSONArray解析为 List数据
     *
     * @param clazz
     * @param jsonObj
     * @param key
     * @return
     */
    public static <K> List<K> parseArrayJson(Class<K> clazz, JSONObject jsonObj, String key) {
        if (jsonObj == null)
            return Collections.emptyList();

        JSONArray array = jsonObj.optJSONArray(key);
        return parseArrayJson(clazz, array);
    }

    /**
     * 将JSONArray解析为 List数据
     *
     * @param clazz
     * @param array
     * @return
     */
    public static <K> List<K> parseArrayJson(Class<K> clazz, JSONArray array) {
        try {

            if (array == null)
                return Collections.emptyList();

            int length = array.length();
            if (length == 0)
                return Collections.emptyList();

            List<K> list = new ArrayList<K>(length);

            Gson gson = new Gson();
            String t = null;
            for (int i = 0; i < length; i++) {
                t = array.optString(i);
                if (t != null) {
                    list.add(gson.fromJson(t, clazz));
                }
            }
            return list;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return Collections.emptyList();
    }

    /**
     * 将JSONArray解析为 List数据
     *
     * @param clazz
     * @param arrayJson
     * @return
     */
    public static <K> List<K> parseArrayJson(Class<K> clazz, String arrayJson) {

        try {
            JSONArray array = new JSONArray(arrayJson);
            return parseArrayJson(clazz, array);
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        // }
        return Collections.emptyList();
    }

    /**
     * 从{@code jsonObj}中解析出{@code key}结点下的JSONObject对象，并将JSONObject实例化为{@code K}的对象
     *
     * @param clazz
     * @return
     */
    public static <K> K parseObjectJson(Class<K> clazz, JSONObject jsonObj, String key) {
        if (jsonObj == null)
            return null;

        JSONObject obj = jsonObj.optJSONObject(key);
        return parseObjectJson(clazz, obj);
    }

    /**
     * 将JSONObject实例化为{@code K}的对象
     *
     * @param clazz
     * @return
     */
    public static <K> K parseObjectJson(Class<K> clazz, JSONObject jsonObj) {
        if (jsonObj == null)
            return null;

        return parseObjectJson(clazz, jsonObj.toString());
    }

    /**
     * 将字符串实例化为{@code K}的对象
     *
     * @param clazz
     * @return
     */
    public static <K> K parseObjectJson(Class<K> clazz, String jsonData) {
        try {

            Gson gson = new Gson();
            return gson.fromJson(jsonData, clazz);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;

    }

}
