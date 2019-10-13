
package cc.smartcash.wallet.Models;

import android.os.Parcel;
import android.os.Parcelable;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "data"
})
public class WebWalletUserAvailableRequest implements Serializable, Parcelable {

    public final static Parcelable.Creator<WebWalletUserAvailableRequest> CREATOR = new Creator<WebWalletUserAvailableRequest>() {


        @SuppressWarnings({
                "unchecked"
        })
        public WebWalletUserAvailableRequest createFromParcel(Parcel in) {
            return new WebWalletUserAvailableRequest(in);
        }

        public WebWalletUserAvailableRequest[] newArray(int size) {
            return (new WebWalletUserAvailableRequest[size]);
        }

    };
    private final static long serialVersionUID = -1418443063754402134L;
    @JsonProperty("data")
    @SerializedName("data")
    @Expose
    private String data;

    public WebWalletUserAvailableRequest(String data) {
        setData(data);
    }

    protected WebWalletUserAvailableRequest(Parcel in) {
        this.data = ((String) in.readValue((String.class.getClassLoader())));
    }

    public WebWalletUserAvailableRequest() {
    }

    @JsonProperty("data")
    public String getData() {
        return data;
    }

    @JsonProperty("data")
    public void setData(String data) {
        this.data = data;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(data);
    }

    public int describeContents() {
        return 0;
    }

}