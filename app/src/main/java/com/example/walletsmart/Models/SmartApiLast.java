package com.example.walletsmart.Models;

import java.io.Serializable;

public class SmartApiLast  implements Serializable {
    private String id;

    private String created;

    public void setId(String id){
        this.id = id;
    }
    public String getId(){
        return this.id;
    }
    public void setCreated(String created){
        this.created = created;
    }
    public String getCreated(){
        return this.created;
    }
}
