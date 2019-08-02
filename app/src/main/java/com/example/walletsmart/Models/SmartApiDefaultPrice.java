package com.example.walletsmart.Models;
import java.io.Serializable;
import java.util.List;

public class SmartApiDefaultPrice  implements Serializable {


    private int count;

    private List<SmartApiItem> items;

    private SmartApiLast last;

    private boolean isError;

    private String requestId;

    private String resource;

    private int status;

    private String version;

    private double execution;

    public void setCount(int count){
        this.count = count;
    }
    public int getCount(){
        return this.count;
    }
    public void setItems(List<SmartApiItem> items){
        this.items = items;
    }
    public List<SmartApiItem> getItems(){
        return this.items;
    }
    public void setLast(SmartApiLast last){
        this.last = last;
    }
    public SmartApiLast getLast(){
        return this.last;
    }
    public void setIsError(boolean isError){
        this.isError = isError;
    }
    public boolean getIsError(){
        return this.isError;
    }
    public void setRequestId(String requestId){
        this.requestId = requestId;
    }
    public String getRequestId(){
        return this.requestId;
    }
    public void setResource(String resource){
        this.resource = resource;
    }
    public String getResource(){
        return this.resource;
    }
    public void setStatus(int status){
        this.status = status;
    }
    public int getStatus(){
        return this.status;
    }
    public void setVersion(String version){
        this.version = version;
    }
    public String getVersion(){
        return this.version;
    }
    public void setExecution(double execution){
        this.execution = execution;
    }
    public double getExecution(){
        return this.execution;
    }
}
