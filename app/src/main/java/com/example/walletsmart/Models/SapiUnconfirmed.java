package com.example.walletsmart.Models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.io.Serializable;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class SapiUnconfirmed implements Serializable {

    private int delta;

    private List<String> transactions;

    public int getDelta() {
        return this.delta;
    }

    public void setDelta(int delta) {
        this.delta = delta;
    }

    public List<String> getTransactions() {
        return this.transactions;
    }

    public void setTransactions(List<String> transactions) {
        this.transactions = transactions;
    }

}