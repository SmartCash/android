package cc.smartcash.wallet.Models;

import android.os.Parcel;
import android.os.Parcelable;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import java.io.Serializable;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "status",
        "data"
})
@JsonIgnoreProperties(ignoreUnknown = true)
public class SmartTextRoot implements Serializable, Parcelable {

    public final static Parcelable.Creator<SmartTextRoot> CREATOR = new Creator<SmartTextRoot>() {


        @SuppressWarnings({
                "unchecked"
        })
        public SmartTextRoot createFromParcel(Parcel in) {
            return new SmartTextRoot(in);
        }

        public SmartTextRoot[] newArray(int size) {
            return (new SmartTextRoot[size]);
        }

    };
    private final static long serialVersionUID = -3486050116655913572L;
    @JsonProperty("status")
    private String status;
    @JsonProperty("data")
    private SmartTextData data;

    protected SmartTextRoot(Parcel in) {
        this.status = ((String) in.readValue((String.class.getClassLoader())));
        this.data = ((SmartTextData) in.readValue((SmartTextData.class.getClassLoader())));
    }

    public SmartTextRoot() {
    }

    @JsonProperty("status")
    public String getStatus() {
        return status;
    }

    @JsonProperty("status")
    public void setStatus(String status) {
        this.status = status;
    }

    @JsonProperty("data")
    public SmartTextData getData() {
        return data;
    }

    @JsonProperty("data")
    public void setData(SmartTextData data) {
        this.data = data;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(status);
        dest.writeValue(data);
    }

    public int describeContents() {
        return 0;
    }

}