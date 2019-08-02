package com.example.walletsmart.Models;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;

public class Transaction implements Parcelable, Serializable {
    private String hash;
    private String timestamp;
    private Double amount;
    private String direction;
    private String toAddress;
    private Boolean isPending;
    private Integer blockindex;
    private Boolean isNew;
    private Boolean isConfirmed;
    private String orderData;

    public String getHash() {
        return hash;
    }

    public void setHash(String hash) {
        this.hash = hash;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmout(Double amount) {
        this.amount = amount;
    }

    public String getDirection() {
        return direction;
    }

    public void setDirection(String direction) {
        this.direction = direction;
    }

    public String getToAddress() {
        return toAddress;
    }

    public void setToAddress(String toAddress) {
        this.toAddress = toAddress;
    }

    public Boolean getIsPending() {
        return isPending;
    }

    public void setIsPending(Boolean isPending) {
        isPending = isPending;
    }

    public Integer getBlockindex() {
        return blockindex;
    }

    public void setBlockindex(Integer blockindex) {
        this.blockindex = blockindex;
    }

    public Boolean getIsNew() {
        return isNew;
    }

    public void setIsNew(Boolean isNew) {
        isNew = isNew;
    }

    public Boolean getIsConfirmed() {
        return isConfirmed;
    }

    public void setIsConfirmed(Boolean isConfirmed) {
        isConfirmed = isConfirmed;
    }

    public String getOrderData() {
        return orderData;
    }

    public void setOrderData(String orderData) {
        this.orderData = orderData;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.hash);
        dest.writeString(this.timestamp);
        dest.writeValue(this.amount);
        dest.writeString(this.direction);
        dest.writeString(this.toAddress);
        dest.writeValue(this.isPending);
        dest.writeValue(this.blockindex);
        dest.writeValue(this.isNew);
        dest.writeValue(this.isConfirmed);
        dest.writeString(this.orderData);
    }

    protected Transaction(Parcel in) {
        this.hash = in.readString();
        this.timestamp = in.readString();
        this.amount = (Double) in.readValue(Double.class.getClassLoader());
        this.direction = in.readString();
        this.toAddress = in.readString();
        this.isPending = (Boolean) in.readValue(Boolean.class.getClassLoader());
        this.blockindex = (Integer) in.readValue(Integer.class.getClassLoader());
        this.isNew = (Boolean) in.readValue(Boolean.class.getClassLoader());
        this.isConfirmed = (Boolean) in.readValue(Boolean.class.getClassLoader());
        this.orderData = in.readString();
    }

    public static final Creator<Transaction> CREATOR = new Creator<Transaction>() {
        @Override
        public Transaction createFromParcel(Parcel source) {
            return new Transaction(source);
        }

        @Override
        public Transaction[] newArray(int size) {
            return new Transaction[size];
        }
    };

    public Transaction() {

    }
}
