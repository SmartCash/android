package com.example.walletsmart.Models;

import java.io.Serializable;

public class SmartApiItem implements Serializable {

    private String updated;

    private SmartApiCurrency currencies;

    private String ticker;

    private String object;

    private String created;

    private String exchange;

    private String id;

    private String name;

    public void setUpdated(String updated){
        this.updated = updated;
    }
    public String getUpdated(){
        return this.updated;
    }
    public void setCurrencies(SmartApiCurrency currencies){
        this.currencies = currencies;
    }
    public SmartApiCurrency getCurrencies(){
        return this.currencies;
    }
    public void setTicker(String ticker){
        this.ticker = ticker;
    }
    public String getTicker(){
        return this.ticker;
    }
    public void setObject(String object){
        this.object = object;
    }
    public String getObject(){
        return this.object;
    }
    public void setCreated(String created){
        this.created = created;
    }
    public String getCreated(){
        return this.created;
    }
    public void setExchange(String exchange){
        this.exchange = exchange;
    }
    public String getExchange(){
        return this.exchange;
    }
    public void setId(String id){
        this.id = id;
    }
    public String getId(){
        return this.id;
    }
    public void setName(String name){
        this.name = name;
    }
    public String getName(){
        return this.name;
    }
}
