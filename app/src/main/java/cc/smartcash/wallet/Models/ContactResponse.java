package cc.smartcash.wallet.Models;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class ContactResponse {
    @JsonProperty("data")
    private List<Contact> data = null;
    @JsonProperty("error")
    private Object error;
    @JsonProperty("status")
    private String status;
    @JsonProperty("isValid")
    private Boolean isValid;

    public List<Contact> getData() {
        return data;
    }

    public void setData(List<Contact> data) {
        this.data = data;
    }

    public Object getError() {
        return error;
    }

    public void setError(Object error) {
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


}