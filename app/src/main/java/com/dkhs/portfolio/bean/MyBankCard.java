package com.dkhs.portfolio.bean;

import java.io.Serializable;

@SuppressWarnings("serial")
public class MyBankCard implements Serializable{
    private String id;
    private String real_name;
    private Bank bank;
    /**
     * 银行卡尾号
     */
    private String bank_card_no_tail;
    /**
     * 银行卡类型 0 储蓄卡 1 信用卡
     */
    private int bank_card_type;
    /**
     * 是否是默认银行卡
     */
    private boolean is_default;
    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }
    public Bank getBank() {
        return bank;
    }
    public void setBank(Bank bank) {
        this.bank = bank;
    }
    public String getBank_card_no_tail() {
        return bank_card_no_tail;
    }
    public void setBank_card_no_tail(String bank_card_no_tail) {
        this.bank_card_no_tail = bank_card_no_tail;
    }
    public int getBank_card_type() {
        return bank_card_type;
    }
    public void setBank_card_type(int bank_card_type) {
        this.bank_card_type = bank_card_type;
    }
    public boolean isIs_default() {
        return is_default;
    }
    public void setIs_default(boolean is_default) {
        this.is_default = is_default;
    }

    public String getReal_name() {
        return real_name;
    }

    public void setReal_name(String real_name) {
        this.real_name = real_name;
    }
}
