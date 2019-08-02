package com.example.walletsmart.Models;

import com.google.gson.annotations.SerializedName;

public class TransactionDetails {
    @SerializedName("fromAddress")
    private String fromAddress;
    @SerializedName("toAddress")
    private String toAddress;
    @SerializedName("Amount")
    private Double Amount;

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

    public Double getAmount() {
        return Amount;
    }

    public void setAmount(Double amount) {
        Amount = amount;
    }
}
