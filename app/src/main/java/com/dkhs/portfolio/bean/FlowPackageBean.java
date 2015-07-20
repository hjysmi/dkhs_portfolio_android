package com.dkhs.portfolio.bean;

import java.util.List;

/**
 * Created by zjz on 2015/6/18.
 */
public class FlowPackageBean {


    /**
     * balance : 250
     * oppackages : [{"amount":10,"name":"10M"},{"amount":50,"name":"50M"}]
     * max_name : 10M
     * max_amount : 10
     * opname : 移动
     * opcode : 0
     * mobile : 15810334233
     */
    private int balance;
    private List<OppackagesEntity> oppackages;
    private String max_name;
    private int max_amount;
    private String opname;
    private int opcode;
    private String mobile;

    public void setBalance(int balance) {
        this.balance = balance;
    }

    public void setOppackages(List<OppackagesEntity> oppackages) {
        this.oppackages = oppackages;
    }

    public void setMax_name(String max_name) {
        this.max_name = max_name;
    }

    public void setMax_amount(int max_amount) {
        this.max_amount = max_amount;
    }

    public void setOpname(String opname) {
        this.opname = opname;
    }

    public void setOpcode(int opcode) {
        this.opcode = opcode;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public int getBalance() {
        return balance;
    }

    public List<OppackagesEntity> getOppackages() {
        return oppackages;
    }

    public String getMax_name() {
        return max_name;
    }

    public int getMax_amount() {
        return max_amount;
    }

    public String getOpname() {
        return opname;
    }

    public int getOpcode() {
        return opcode;
    }

    public String getMobile() {
        return mobile;
    }

    public class OppackagesEntity {
        /**
         * amount : 10
         * name : 10M
         */
        private int amount;
        private String name;

        public void setAmount(int amount) {
            this.amount = amount;
        }

        public void setName(String name) {
            this.name = name;
        }

        public int getAmount() {
            return amount;
        }

        public String getName() {
            return name;
        }
    }
}
