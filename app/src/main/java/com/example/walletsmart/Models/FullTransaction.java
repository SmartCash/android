package com.example.walletsmart.Models;

import java.util.ArrayList;

public class FullTransaction {
    private String txid;
    private float version;
    private float locktime;
    ArrayList < Object > vin = new ArrayList < Object > ();
    ArrayList < Object > vout = new ArrayList < Object > ();
    private String blockhash;
    private float blockheight;
    private float confirmations;
    private float time;
    private float blocktime;
    private float valueOut;
    private float size;
    private float valueIn;
    private float fees;


    // Getter Methods

    public String getTxid() {
        return txid;
    }

    public float getVersion() {
        return version;
    }

    public float getLocktime() {
        return locktime;
    }

    public String getBlockhash() {
        return blockhash;
    }

    public float getBlockheight() {
        return blockheight;
    }

    public float getConfirmations() {
        return confirmations;
    }

    public float getTime() {
        return time;
    }

    public float getBlocktime() {
        return blocktime;
    }

    public float getValueOut() {
        return valueOut;
    }

    public float getSize() {
        return size;
    }

    public float getValueIn() {
        return valueIn;
    }

    public float getFees() {
        return fees;
    }

    // Setter Methods

    public void setTxid(String txid) {
        this.txid = txid;
    }

    public void setVersion(float version) {
        this.version = version;
    }

    public void setLocktime(float locktime) {
        this.locktime = locktime;
    }

    public void setBlockhash(String blockhash) {
        this.blockhash = blockhash;
    }

    public void setBlockheight(float blockheight) {
        this.blockheight = blockheight;
    }

    public void setConfirmations(float confirmations) {
        this.confirmations = confirmations;
    }

    public void setTime(float time) {
        this.time = time;
    }

    public void setBlocktime(float blocktime) {
        this.blocktime = blocktime;
    }

    public void setValueOut(float valueOut) {
        this.valueOut = valueOut;
    }

    public void setSize(float size) {
        this.size = size;
    }

    public void setValueIn(float valueIn) {
        this.valueIn = valueIn;
    }

    public void setFees(float fees) {
        this.fees = fees;
    }
}