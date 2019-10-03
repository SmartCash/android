package cc.smartcash.wallet.Models;

import android.os.Parcel;
import android.os.Parcelable;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.google.gson.Gson;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "error",
        "error_description"
})
public class WebWalletException implements Serializable, Parcelable {

    public final static Parcelable.Creator<WebWalletException> CREATOR = new Creator<WebWalletException>() {


        @SuppressWarnings({
                "unchecked"
        })
        public WebWalletException createFromParcel(Parcel in) {
            return new WebWalletException(in);
        }

        public WebWalletException[] newArray(int size) {
            return (new WebWalletException[size]);
        }

    };
    private final static long serialVersionUID = -6565245106594853047L;
    @JsonProperty("error")
    @SerializedName("error")
    @Expose
    private String error;
    @JsonProperty("error_description")
    @SerializedName("error_description")
    @Expose
    private String errorDescription;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    protected WebWalletException(Parcel in) {
        this.error = ((String) in.readValue((String.class.getClassLoader())));
        this.errorDescription = ((String) in.readValue((String.class.getClassLoader())));
        this.additionalProperties = ((Map<String, Object>) in.readValue((Map.class.getClassLoader())));
    }

    public WebWalletException() {
    }

    public static WebWalletException parse(String value) {
        return new Gson().fromJson(value, WebWalletException.class);
    }

    @JsonProperty("error")
    @SerializedName("error")
    public String getError() {
        return error;
    }

    @JsonProperty("error")
    @SerializedName("error")
    public void setError(String error) {
        this.error = error;
    }

    @JsonProperty("error_description")
    @SerializedName("error_description")
    public String getErrorDescription() {
        return errorDescription;
    }

    @JsonProperty("error_description")
    @SerializedName("error_description")
    public void setErrorDescription(String errorDescription) {
        this.errorDescription = errorDescription;
    }

    @JsonAnyGetter
    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    @JsonAnySetter
    public void setAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(error);
        dest.writeValue(errorDescription);
        dest.writeValue(additionalProperties);
    }

    public int describeContents() {
        return 0;
    }
}