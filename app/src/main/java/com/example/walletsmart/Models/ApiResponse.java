package com.example.walletsmart.Models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

@JsonIgnoreProperties({"isValid"})
public class ApiResponse implements Serializable {

    public User getData() {
        return data;
    }

    public void setData(User data) {
        this.data = data;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Boolean getValid() {
        return isValid;
    }

    public void setValid(Boolean valid) {
        isValid = valid;
    }

    public String getErrorDescription() {
        return errorDescription;
    }

    public void setErrorDescription(String errorDescription) {
        this.errorDescription = errorDescription;
    }

    @SerializedName("data")
    private User data;

    @SerializedName("error")
    private String error;

    @SerializedName("status")
    private String status;

    @SerializedName("isValid")
    private Boolean isValid;

    @SerializedName("error_description")
    private String errorDescription;

}
