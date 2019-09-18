package cc.smartcash.wallet.Models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.ArrayList;

@JsonIgnoreProperties(ignoreUnknown = true)
public class FullTransaction {
    ArrayList<Object> vin = new ArrayList<>();
    ArrayList<Object> vout = new ArrayList<>();
    private String txid;
    private float version;
    private float locktime;
    private String blockhash;
    private float blockheight;
    private float confirmations;
    private long time;
    private long blocktime;
    private float valueOut;
    private float size;
    private float valueIn;
    private float fees;


    // Getter Methods

    public String getTxid() {
        return txid;
    }

    public void setTxid(String txid) {
        this.txid = txid;
    }

    public float getVersion() {
        return version;
    }

    public void setVersion(float version) {
        this.version = version;
    }

    public float getLocktime() {
        return locktime;
    }

    public void setLocktime(float locktime) {
        this.locktime = locktime;
    }

    public String getBlockhash() {
        return blockhash;
    }

    public void setBlockhash(String blockhash) {
        this.blockhash = blockhash;
    }

    public float getBlockheight() {
        return blockheight;
    }

    public void setBlockheight(float blockheight) {
        this.blockheight = blockheight;
    }

    public float getConfirmations() {
        return confirmations;
    }

    public void setConfirmations(float confirmations) {
        this.confirmations = confirmations;
    }

    // Setter Methods

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public long getBlocktime() {
        return blocktime;
    }

    public void setBlocktime(long blocktime) {
        this.blocktime = blocktime;
    }

    public float getValueOut() {
        return valueOut;
    }

    public void setValueOut(float valueOut) {
        this.valueOut = valueOut;
    }

    public float getSize() {
        return size;
    }

    public void setSize(float size) {
        this.size = size;
    }

    public float getValueIn() {
        return valueIn;
    }

    public void setValueIn(float valueIn) {
        this.valueIn = valueIn;
    }

    public float getFees() {
        return fees;
    }

    public void setFees(float fees) {
        this.fees = fees;
    }
}