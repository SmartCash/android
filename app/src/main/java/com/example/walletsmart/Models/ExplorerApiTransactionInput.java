package com.example.walletsmart.Models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

@JsonIgnoreProperties(ignoreUnknown = true)
public class ExplorerApiTransactionInput implements Serializable {

    public String getTxidIn() {
        return txidIn;
    }

    public void setTxidIn(String txidIn) {
        this.txidIn = txidIn;
    }

    public int getIndexIn() {
        return indexIn;
    }

    public void setIndexIn(int indexIn) {
        this.indexIn = indexIn;
    }

    public String getTxIdOut() {
        return txIdOut;
    }

    public void setTxIdOut(String txIdOut) {
        this.txIdOut = txIdOut;
    }

    public int getIndexOut() {
        return indexOut;
    }

    public void setIndexOut(int indexOut) {
        this.indexOut = indexOut;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public String getJson() {
        return json;
    }

    public void setJson(String json) {
        this.json = json;
    }

    @JsonProperty("TxidIn")
    private String txidIn;

    @JsonProperty("IndexIn")
    private int indexIn;

    @JsonProperty("TxidOut")
    private String txIdOut;

    @JsonProperty("IndexOut")
    private int indexOut;

    @JsonProperty("Address")
    private String address;

    @JsonProperty("Value")
    private int value;

    @JsonProperty("Json")
    private String json;

}