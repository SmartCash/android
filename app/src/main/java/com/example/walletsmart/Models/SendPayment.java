package com.example.walletsmart.Models;

import com.google.gson.annotations.SerializedName;

public class SendPayment {
    @SerializedName("FromAddress")
    private String fromAddress;
    @SerializedName("ToAddress")
    private String toAddress;
    @SerializedName("Amount")
    private Float amount;
    @SerializedName("UserKey")
    private String userKey;
    @SerializedName("code")
    private String code;
    @SerializedName("Email")
    private String email;

    public String getFromAddress() {
        return fromAddress;
    }

    public void setFromAddress(String fromAddress) {
        this.fromAddress = fromAddress;
    }

    public String getToAddress() {
        return toAddress;
    }

    public void setToAddress(String toAddress) {
        this.toAddress = toAddress;
    }

    public Float getAmount() {
        return amount;
    }

    public void setAmount(Float amount) {
        this.amount = amount;
    }

    public String getUserKey() {
        return userKey;
    }

    public void setUserKey(String userKey) {
        this.userKey = userKey;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}