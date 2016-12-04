package com.managesystem.model;

/**
 * Created by Administrator on 2016/12/4.
 */
public class Account {

    /**
     * accountId : 1
     * balance : 0
     * comsume : 10
     * rechange : 10
     * userId : xiaoti
     */

    private String accountId;
    private float balance;
    private float comsume;
    private float rechange;
    private String userId;

    public String getAccountId() {
        return accountId;
    }

    public void setAccountId(String accountId) {
        this.accountId = accountId;
    }

    public float getBalance() {
        return balance;
    }

    public void setBalance(float balance) {
        this.balance = balance;
    }

    public float getComsume() {
        return comsume;
    }

    public void setComsume(float comsume) {
        this.comsume = comsume;
    }

    public float getRechange() {
        return rechange;
    }

    public void setRechange(float rechange) {
        this.rechange = rechange;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}
