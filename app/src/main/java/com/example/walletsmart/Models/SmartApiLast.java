package com.example.walletsmart.Models;

import java.io.Serializable;

public class SmartApiLast implements Serializable {
    private String id;

    private String created;

    public String getId() {
        return this.id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCreated() {
        return this.created;
    }

    public void setCreated(String created) {
        this.created = created;
    }
}
