package com.example.walletsmart.Models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

@JsonIgnoreProperties({"password", "confirmPassword", "recoveryKey", "orderData", "cardId"})
public class User {
    @SerializedName("password")
    private String password;
    @SerializedName("confirmPassword")
    private String confirmPassword;
    @SerializedName("recoveryKey")
    private String recoveryKey;
    @SerializedName("notifications")
    private Integer notifications;
    @SerializedName("lastLoginDate")
    private String lastLoginDate;
    @SerializedName("termsVersion")
    private String termsVersion;
    @SerializedName("is2FAEnabled")
    private Boolean is2FAEnabled;
    @SerializedName("userId")
    private Integer userId;
    @SerializedName("firstName")
    private String firstName;
    @SerializedName("lastName")
    private String lastName;
    @SerializedName("username")
    private String username;
    @SerializedName("email")
    private String email;
    @SerializedName("facebookId")
    private String facebookId;
    @SerializedName("countryCode")
    private String countryCode;
    @SerializedName("app")
    private ArrayList<Wallet> wallet;
    @SerializedName("require2faToSend")
    private Boolean require2faToSend;

    @Override
    public String toString() {
        return "User: " + getFirstName();
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getConfirmPassword() {
        return confirmPassword;
    }

    public void setConfirmPassword(String confirmPassword) {
        this.confirmPassword = confirmPassword;
    }

    public String getRecoveryKey() {
        return recoveryKey;
    }

    public void setRecoveryKey(String recoveryKey) {
        this.recoveryKey = recoveryKey;
    }

    public Integer getNotifications() {
        return notifications;
    }

    public void setNotifications(Integer notifications) {
        this.notifications = notifications;
    }

    public String getLastLoginDate() {
        return lastLoginDate;
    }

    public void setLastLoginDate(String lastLoginDate) {
        this.lastLoginDate = lastLoginDate;
    }

    public String getTermsVersion() {
        return termsVersion;
    }

    public void setTermsVersion(String termsVersion) {
        this.termsVersion = termsVersion;
    }

    public Boolean getIs2FAEnabled() {
        return is2FAEnabled;
    }

    public void setIs2FAEnabled(Boolean is2FAEnabled) {
        this.is2FAEnabled = is2FAEnabled;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFacebookId() {
        return facebookId;
    }

    public void setFacebookId(String facebookId) {
        this.facebookId = facebookId;
    }

    public String getCountryCode() {
        return countryCode;
    }

    public void setCountryCode(String countryCode) {
        this.countryCode = countryCode;
    }

    public ArrayList<Wallet> getWallet() {
        return wallet;
    }

    public void setWallet(ArrayList<Wallet> wallet) {
        this.wallet = wallet;
    }

    public Boolean getRequire2faToSend() {
        return require2faToSend;
    }

    public void setRequire2faToSend(Boolean require2faToSend) {
        this.require2faToSend = require2faToSend;
    }
}