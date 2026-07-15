package com.splitwise.dto;

import java.math.BigDecimal;

public class BalanceResponse {

    private String userName;
    private BigDecimal paid;
    private BigDecimal owes;
    private BigDecimal balance;

    public BalanceResponse() {
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public BigDecimal getPaid() {
        return paid;
    }

    public void setPaid(BigDecimal paid) {
        this.paid = paid;
    }

    public BigDecimal getOwes() {
        return owes;
    }

    public void setOwes(BigDecimal owes) {
        this.owes = owes;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }
}