package com.example;

public class MyClass {
    private boolean s;
    public static  void main(String[] args){
        String.format("%2d:%2d",1,20);
//        double a = (3.3-2.4)/0.1;
//        System.out.println(a);

//        System.out.println(      isNewVersion("1.10.133.2554","2.8.5.1215"));

        int i=0;
        System.out.println(i=i|2 );
        System.out.println(i=i|1 );
        int y=i=i|4;
        System.out.println(y );

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