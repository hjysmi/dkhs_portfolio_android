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
    /**
     * tasks_desc : {"first_portfolio_desc":"创建成功立刻获取10M流量","invite_code_desc":"填写成功立刻获取10M流量","bind_mobile_desc":"绑定成功即可兑换流量","invite_friends_desc":"每邀请1位好友成功注册送10M流量"}
     */
    private Tasks_descEntity tasks_desc;

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

    public void setTasks_desc(Tasks_descEntity tasks_desc) {
        this.tasks_desc = tasks_desc;
    }

    public Tasks_descEntity getTasks_desc() {
        return tasks_desc;
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


    public class Tasks_descEntity {
        /**
         * first_portfolio_desc : 创建成功立刻获取10M流量
         * invite_code_desc : 填写成功立刻获取10M流量
         * bind_mobile_desc : 绑定成功即可兑换流量
         * invite_friends_desc : 每邀请1位好友成功注册送10M流量
         */
        private String first_portfolio_desc;
        private String invite_code_desc;
        private String bind_mobile_desc;
        private String invite_friends_desc;

        public void setFirst_portfolio_desc(String first_portfolio_desc) {
            this.first_portfolio_desc = first_portfolio_desc;
        }

        public void setInvite_code_desc(String invite_code_desc) {
            this.invite_code_desc = invite_code_desc;
        }

        public void setBind_mobile_desc(String bind_mobile_desc) {
            this.bind_mobile_desc = bind_mobile_desc;
        }

        public void setInvite_friends_desc(String invite_friends_desc) {
            this.invite_friends_desc = invite_friends_desc;
        }

        public String getFirst_portfolio_desc() {
            return first_portfolio_desc;
        }

        public String getInvite_code_desc() {
            return invite_code_desc;
        }

        public String getBind_mobile_desc() {
            return bind_mobile_desc;
        }

        public String getInvite_friends_desc() {
            return invite_friends_desc;
        }
    }
}
