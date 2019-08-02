package com.example.walletsmart.Models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;

@JsonIgnoreProperties(ignoreUnknown = true)
public class ExplorerApiBlock  implements Serializable
{
    public String getNumberOfTransactions() {
        return numberOfTransactions;
    }

    public void setNumberOfTransactions(String numberOfTransactions) {
        this.numberOfTransactions = numberOfTransactions;
    }

    public String getHash() {
        return hash;
    }

    public void setHash(String hash) {
        this.hash = hash;
    }

    public String getBlockReward() {
        return blockReward;
    }

    public void setBlockReward(String blockReward) {
        this.blockReward = blockReward;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public String getMerkleRoot() {
        return merkleRoot;
    }

    public void setMerkleRoot(String merkleRoot) {
        this.merkleRoot = merkleRoot;
    }

    public String getHeight() {
        return height;
    }

    public void setHeight(String height) {
        this.height = height;
    }

    public String getDifficulty() {
        return difficulty;
    }

    public void setDifficulty(String difficulty) {
        this.difficulty = difficulty;
    }

    public String getBits() {
        return bits;
    }

    public void setBits(String bits) {
        this.bits = bits;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getNonce() {
        return nonce;
    }

    public void setNonce(String nonce) {
        this.nonce = nonce;
    }

    public String getPreviousBlockNumber() {
        return previousBlockNumber;
    }

    public void setPreviousBlockNumber(String previousBlockNumber) {
        this.previousBlockNumber = previousBlockNumber;
    }

    public String getPreviousBlockHash() {
        return previousBlockHash;
    }

    public void setPreviousBlockHash(String previousBlockHash) {
        this.previousBlockHash = previousBlockHash;
    }

    public String getJson() {
        return json;
    }

    public void setJson(String json) {
        this.json = json;
    }

    public String getConfirmations() {
        return confirmations;
    }

    public void setConfirmations(String confirmations) {
        this.confirmations = confirmations;
    }

    @JsonProperty("NumberOfTransactions")
    private String numberOfTransactions;

    @JsonProperty("Hash")
    private String hash;

    @JsonProperty("BlockReward")
    private String blockReward;

    @JsonProperty("Timestamp")
    private String timestamp;

    @JsonProperty("MerkleRoot")
    private String merkleRoot;

    @JsonProperty("Height")
    private String height;

    @JsonProperty("Difficulty")
    private String difficulty;

    @JsonProperty("Bits")
    private String bits;

    @JsonProperty("Version")
    private String version;

    @JsonProperty("Nonce")
    private String nonce;

    @JsonProperty("PreviousBlockNumber")
    private String previousBlockNumber;

    @JsonProperty("PreviousBlockHash")
    private String previousBlockHash;

    @JsonProperty("Json")
    private String json;

    @JsonProperty("Confirmations")
    private String confirmations;

}
