package com.example.walletsmart.Models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.io.Serializable;

@JsonIgnoreProperties(ignoreUnknown = true)
public class SapiAddressBalance implements Serializable {

    private String address;

    private double received;

    private int sent;

    private double balance;

    private SapiUnconfirmed unconfirmed;

    public String getAddress() {
        return this.address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public double getReceived() {
        return this.received;
    }

    public void setReceived(double received) {
        this.received = received;
    }

    public int getSent() {
        return this.sent;
    }

    public void setSent(int sent) {
        this.sent = sent;
    }

    public double getBalance() {
        return this.balance;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }

    public SapiUnconfirmed getUnconfirmed() {
        return this.unconfirmed;
    }

    public void setUnconfirmed(SapiUnconfirmed unconfirmed) {
        this.unconfirmed = unconfirmed;
    }

}