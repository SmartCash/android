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
        "userWebwallet",
        "destinationEmail",
        "addressRefunded",
        "amountSmart",
        "typeSend",
        "phoneNumber",
        "messageToSend"
})
@JsonIgnoreProperties(ignoreUnknown = true)
public class SmartTextRequest implements Serializable, Parcelable {

    public final static Parcelable.Creator<SmartTextRequest> CREATOR = new Creator<SmartTextRequest>() {


        @SuppressWarnings({
                "unchecked"
        })
        public SmartTextRequest createFromParcel(Parcel in) {
            return new SmartTextRequest(in);
        }

        public SmartTextRequest[] newArray(int size) {
            return (new SmartTextRequest[size]);
        }

    };
    private final static long serialVersionUID = 7668402473711320029L;
    @JsonProperty("userWebwallet")
    private String userWebwallet;
    @JsonProperty("destinationEmail")
    private String destinationEmail;
    @JsonProperty("addressRefunded")
    private String addressRefunded;
    @JsonProperty("amountSmart")
    private String amountSmart;
    @JsonProperty("typeSend")
    private String typeSend;
    @JsonProperty("phoneNumber")
    private String phoneNumber;
    @JsonProperty("messageToSend")
    private String messageToSend;

    protected SmartTextRequest(Parcel in) {
        this.userWebwallet = ((String) in.readValue((String.class.getClassLoader())));
        this.destinationEmail = ((String) in.readValue((String.class.getClassLoader())));
        this.addressRefunded = ((String) in.readValue((String.class.getClassLoader())));
        this.amountSmart = ((String) in.readValue((String.class.getClassLoader())));
        this.typeSend = ((String) in.readValue((String.class.getClassLoader())));
        this.phoneNumber = ((String) in.readValue((String.class.getClassLoader())));
        this.messageToSend = ((String) in.readValue((String.class.getClassLoader())));
    }

    public SmartTextRequest() {
    }

    @JsonProperty("userWebwallet")
    public String getUserWebwallet() {
        return userWebwallet;
    }

    @JsonProperty("userWebwallet")
    public void setUserWebwallet(String userWebwallet) {
        this.userWebwallet = userWebwallet;
    }

    @JsonProperty("destinationEmail")
    public String getDestinationEmail() {
        return destinationEmail;
    }

    @JsonProperty("destinationEmail")
    public void setDestinationEmail(String destinationEmail) {
        this.destinationEmail = destinationEmail;
    }

    @JsonProperty("addressRefunded")
    public String getAddressRefunded() {
        return addressRefunded;
    }

    @JsonProperty("addressRefunded")
    public void setAddressRefunded(String addressRefunded) {
        this.addressRefunded = addressRefunded;
    }

    @JsonProperty("amountSmart")
    public String getAmountSmart() {
        return amountSmart;
    }

    @JsonProperty("amountSmart")
    public void setAmountSmart(String amountSmart) {
        this.amountSmart = amountSmart;
    }

    @JsonProperty("typeSend")
    public String getTypeSend() {
        return typeSend;
    }

    @JsonProperty("typeSend")
    public void setTypeSend(String typeSend) {
        this.typeSend = typeSend;
    }

    @JsonProperty("phoneNumber")
    public String getPhoneNumber() {
        return phoneNumber;
    }

    @JsonProperty("phoneNumber")
    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    @JsonProperty("messageToSend")
    public String getMessageToSend() {
        return messageToSend;
    }

    @JsonProperty("messageToSend")
    public void setMessageToSend(String messageToSend) {
        this.messageToSend = messageToSend;
    }


    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(userWebwallet);
        dest.writeValue(destinationEmail);
        dest.writeValue(addressRefunded);
        dest.writeValue(amountSmart);
        dest.writeValue(typeSend);
        dest.writeValue(phoneNumber);
        dest.writeValue(messageToSend);
    }

    public int describeContents() {
        return 0;
    }

}