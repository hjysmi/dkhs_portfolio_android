package com.dkhs.portfolio.bean;

/**
 * Created by zjz on 2015/6/18.
 */
public class FlowOverViewBean {

    /**
     * balance : 5000
     * traffic_amount : 500
     * tasks : {"first_portfolio":true,"bind_mobile":false,"invite_code":false,"invite_friends":false}
     */
    private int balance;
    private int traffic_amount;
    private TasksEntity tasks;

    public void setBalance(int balance) {
        this.balance = balance;
    }

    public void setTraffic_amount(int traffic_amount) {
        this.traffic_amount = traffic_amount;
    }

    public void setTasks(TasksEntity tasks) {
        this.tasks = tasks;
    }

    public int getBalance() {
        return balance;
    }

    public int getTraffic_amount() {
        return traffic_amount;
    }

    public TasksEntity getTasks() {
        return tasks;
    }

    public class TasksEntity {
        /**
         * first_portfolio : true
         * bind_mobile : false
         * invite_code : false
         * invite_friends : false
         */
        private boolean first_portfolio;
        private boolean bind_mobile;
        private boolean invite_code;
        private boolean invite_friends;

        public void setFirst_portfolio(boolean first_portfolio) {
            this.first_portfolio = first_portfolio;
        }

        public void setBind_mobile(boolean bind_mobile) {
            this.bind_mobile = bind_mobile;
        }

        public void setInvite_code(boolean invite_code) {
            this.invite_code = invite_code;
        }

        public void setInvite_friends(boolean invite_friends) {
            this.invite_friends = invite_friends;
        }

        public boolean isFirst_portfolio() {
            return first_portfolio;
        }

        public boolean isBind_mobile() {
            return bind_mobile;
        }

        public boolean isInvite_code() {
            return invite_code;
        }

        public boolean isInvite_friends() {
            return invite_friends;
        }
    }
}
