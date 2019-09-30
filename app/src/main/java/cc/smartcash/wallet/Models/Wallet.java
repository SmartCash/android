package cc.smartcash.wallet.Models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.google.gson.annotations.SerializedName;

import java.math.BigInteger;
import java.util.ArrayList;

@JsonIgnoreProperties(ignoreUnknown = true)
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
    private BigInteger cardId;
    @SerializedName("transactions")
    private ArrayList<Transaction> transactions;

    public Wallet() {

    }

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
        this.isRewards = isRewards;
    }

    public Boolean getIsVault() {
        return isVault;
    }

    public void setIsVault(Boolean isVault) {
        this.isVault = isVault;
    }

    public Boolean getIsScheduled() {
        return isScheduled;
    }

    public void setIsScheduled(Boolean isScheduled) {
        this.isScheduled = isScheduled;
    }

    public BigInteger getCardId() {
        return cardId;
    }

    public void setCardId(BigInteger cardId) {
        this.cardId = cardId;
    }

    public ArrayList<Transaction> getTransactions() {
        return transactions;
    }

    public void setTransactions(ArrayList<Transaction> transactions) {
        this.transactions = transactions;
    }


}
