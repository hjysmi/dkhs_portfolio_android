package com.dkhs.portfolio.bean;

/**
 * Created by zjz on 2015/7/23.
 */
public class EncryptData {

    public String getEncryptkey() {
        return signature;
    }

    public void setEncryptkey(String encryptkey) {
        this.signature = encryptkey;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public String signature;
    public String data;
}
