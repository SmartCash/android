package com.example.walletsmart.Models;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.Objects;

public class Wallet {
    @SerializedName("walletId")
    private Integer walletId;
    @SerializedName("displayName")
    private String displayName;
    @SerializedName("address")
    private String address;
    @SerializedName("qrCode")
    private String qrCode;
    @SerializedName("balance")
    private Double balance;
    @SerializedName("totalSent")
    private Double totalSent;
    @SerializedName("totalReceived")
    private Double totalReceived;
    @SerializedName("position")
    private Integer position;
    @SerializedName("isRewards")
    private Boolean isRewards;
    @SerializedName("isVault")
    private Boolean isVault;
    @SerializedName("isScheduled")
    private Boolean isScheduled;
    @SerializedName("cardId")
    private Integer cardId;
    @SerializedName("transactions")
    private ArrayList<Transaction> transactions;

    public Integer getWalletId() {
        return walletId;
    }

    public void setWalletId(Integer walletId) {
        this.walletId = walletId;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getQrCode() {
        return qrCode;
    }

    public void setQrCode(String qrCode) {
        this.qrCode = qrCode;
    }

    public Double getBalance() {
        return balance;
    }

    public void setBalance(Double balance) {
        this.balance = balance;
    }

    public Double getTotalSent() {
        return totalSent;
    }

    public void setTotalSent(Double totalSent) {
        this.totalSent = totalSent;
    }

    public Double getTotalReceived() {
        return totalReceived;
    }

    public void setTotalReceived(Double totalReceived) {
        this.totalReceived = totalReceived;
    }

    public Integer getPosition() {
        return position;
    }

    public void setPosition(Integer position) {
        this.position = position;
    }

    public Boolean getIsRewards() {
        return isRewards;
    }

    public void setIsRewards(Boolean isRewards) {
        isRewards = isRewards;
    }

    public Boolean getIsVault() {
        return isVault;
    }

    public void setIsVault(Boolean isVault) {
        isVault = isVault;
    }

    public Boolean getIsScheduled() {
        return isScheduled;
    }

    public void setIsScheduled(Boolean isScheduled) {
        isScheduled = isScheduled;
    }

    public Integer getCardId() {
        return cardId;
    }

    public void setCardId(Integer cardId) {
        this.cardId = cardId;
    }

    public ArrayList<Transaction> getTransactions() {
        return transactions;
    }

    public void setTransactions(ArrayList<Transaction> transactions) {
        this.transactions = transactions;
    }

    public Wallet() {

    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Wallet wallet = (Wallet) o;
        return Objects.equals(walletId, wallet.walletId) &&
                Objects.equals(displayName, wallet.displayName) &&
                Objects.equals(address, wallet.address) &&
                Objects.equals(qrCode, wallet.qrCode) &&
                Objects.equals(balance, wallet.balance) &&
                Objects.equals(totalSent, wallet.totalSent) &&
                Objects.equals(totalReceived, wallet.totalReceived) &&
                Objects.equals(position, wallet.position) &&
                Objects.equals(isRewards, wallet.isRewards) &&
                Objects.equals(isVault, wallet.isVault) &&
                Objects.equals(isScheduled, wallet.isScheduled) &&
                Objects.equals(cardId, wallet.cardId) &&
                Objects.equals(transactions, wallet.transactions);
    }

    @Override
    public int hashCode() {
        return Objects.hash(walletId, displayName, address, qrCode, balance, totalSent, totalReceived, position, isRewards, isVault, isScheduled, cardId, transactions);
    }
}
