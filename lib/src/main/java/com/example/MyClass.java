package com.example;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MyClass {
    private boolean s;
    public static  void main(String[] args){
        String.format("%2d:%2d",1,20);
//        double a = (3.3-2.4)/0.1;
//        System.out.println(a);

//        System.out.println(      isNewVersion("1.10.133.2554","2.8.5.1215"));




//        String a="@com.google.gson.annotations.SerializedName( dfdfdf)";
//
//        String[] d=a.split("\n");
//        Pattern pattern=Pattern.compile("@com\\s*\\.\\s*google\\s*\\.\\s*gson\\s*\\.\\s*annotations\\s*\\.\\s*SerializedName\\s*\\((\\s*\\w+\\s*)\\)");
////        Pattern pattern=Pattern.compile("@\\s*SerializedName\\s*\\((\\s*\\w+\\s*)\\)");
////        String a="@dd(2121212)";
////        Pattern pattern=Pattern.compile("@dd\\((\\d+)\\)");
//        Matcher matcher=pattern.matcher(a);
//
//        if(matcher.find()){
//            System.out.println(matcher.group(1));
//        }


        String jsonStr="{\n" +
                "     /**\n" +
                "     * height : 140cm\n" +
                "     * age : 15\n" +
                "     * name : 王五\n" +
                "     * gender : man\n" +
                "     */\n" +
                "    \"name\": \"王五\",\n" +
                "    \"gender\": \"man\",\n" +
                "    \"age\": 15,\n" +
                "    \"height\": \"140cm\",\n" +
                "}";
        String temp = jsonStr.replaceAll("/\\*" +
                "[\\s\\S]*" +
                "\\*/", "");
        System.out.println(temp);


    }

    public static boolean isNewVersion(String s, String  versionName){


        boolean isNewVersion=false;
        String[] serviceVersion = s.split("\\.");
        String[] oldVersion = versionName.split("\\.");

        if(serviceVersion.length>=3 && oldVersion.length >=3){
            for (int i = 0; i < 3; i++) {
                //先判断是否是数字
                if(!serviceVersion[i].matches("\\d+") || !oldVersion[i].matches("\\d+")){
                    isNewVersion=false;
                    break;
                }else
                if(serviceVersion[i].hashCode() >oldVersion[i].hashCode()){
                    isNewVersion=true;
                    break;
                }else if(serviceVersion[i].hashCode() <oldVersion[i].hashCode()){
                    isNewVersion=false;
                    break;
                }else if(serviceVersion[i].hashCode() <oldVersion[i].hashCode()){
                    continue;
                }
            }
        }
        return  isNewVersion;

    }
}
