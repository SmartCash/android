package com.example.walletsmart.Models;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class ExplorerApiAddressBalance implements Serializable {

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public double getBalance() {
        return balance;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }

    public double getSent() {
        return sent;
    }

    public void setSent(double sent) {
        this.sent = sent;
    }

    public double getReceived() {
        return received;
    }

    public void setReceived(double received) {
        this.received = received;
    }

    @SerializedName("address")
    private String address;

    @SerializedName("balance")
    private double balance;

    @SerializedName("sent")
    private double sent;

    @SerializedName("received")
    private double received;

}
