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
        "amoutSmart",
        "createDate",
        "withdrawalDate",
        "depositDate",
        "destinationEmail",
        "phoneNumber",
        "orderID",
        "generatedAddress",
        "txIdDeposit",
        "txIdWithdrawal",
        "addressForWithdrawal",
        "addressRefunded",
        "refundedDate",
        "txIdRefunded",
        "refundedReason",
        "amountSmartSent",
        "amountSmartWithFee",
        "typeSend",
        "urlWithdrawal"
})
@JsonIgnoreProperties(ignoreUnknown = true)
public class SmartTextData implements Serializable, Parcelable {

    public final static Parcelable.Creator<SmartTextData> CREATOR = new Creator<SmartTextData>() {


        @SuppressWarnings({
                "unchecked"
        })
        public SmartTextData createFromParcel(Parcel in) {
            return new SmartTextData(in);
        }

        public SmartTextData[] newArray(int size) {
            return (new SmartTextData[size]);
        }

    };
    private final static long serialVersionUID = -7679495967378891376L;
    @JsonProperty("status")
    private String status;
    @JsonProperty("amoutSmart")
    private Integer amoutSmart;
    @JsonProperty("createDate")
    private String createDate;
    @JsonProperty("withdrawalDate")
    private String withdrawalDate;
    @JsonProperty("depositDate")
    private String depositDate;
    @JsonProperty("destinationEmail")
    private String destinationEmail;
    @JsonProperty("phoneNumber")
    private Object phoneNumber;
    @JsonProperty("orderID")
    private String orderID;
    @JsonProperty("generatedAddress")
    private String generatedAddress;
    @JsonProperty("txIdDeposit")
    private Object txIdDeposit;
    @JsonProperty("txIdWithdrawal")
    private Object txIdWithdrawal;
    @JsonProperty("addressForWithdrawal")
    private String addressForWithdrawal;
    @JsonProperty("addressRefunded")
    private String addressRefunded;
    @JsonProperty("refundedDate")
    private String refundedDate;
    @JsonProperty("txIdRefunded")
    private Object txIdRefunded;
    @JsonProperty("refundedReason")
    private Object refundedReason;
    @JsonProperty("amountSmartSent")
    private Object amountSmartSent;
    @JsonProperty("amountSmartWithFee")
    private Double amountSmartWithFee;
    @JsonProperty("typeSend")
    private String typeSend;
    @JsonProperty("urlWithdrawal")
    private String urlWithdrawal;

    protected SmartTextData(Parcel in) {
        this.status = ((String) in.readValue((String.class.getClassLoader())));
        this.amoutSmart = ((Integer) in.readValue((Integer.class.getClassLoader())));
        this.createDate = ((String) in.readValue((String.class.getClassLoader())));
        this.withdrawalDate = ((String) in.readValue((String.class.getClassLoader())));
        this.depositDate = ((String) in.readValue((String.class.getClassLoader())));
        this.destinationEmail = ((String) in.readValue((String.class.getClassLoader())));
        this.phoneNumber = in.readValue((Object.class.getClassLoader()));
        this.orderID = ((String) in.readValue((String.class.getClassLoader())));
        this.generatedAddress = ((String) in.readValue((String.class.getClassLoader())));
        this.txIdDeposit = in.readValue((Object.class.getClassLoader()));
        this.txIdWithdrawal = in.readValue((Object.class.getClassLoader()));
        this.addressForWithdrawal = ((String) in.readValue((String.class.getClassLoader())));
        this.addressRefunded = ((String) in.readValue((String.class.getClassLoader())));
        this.refundedDate = ((String) in.readValue((String.class.getClassLoader())));
        this.txIdRefunded = in.readValue((Object.class.getClassLoader()));
        this.refundedReason = in.readValue((Object.class.getClassLoader()));
        this.amountSmartSent = in.readValue((Object.class.getClassLoader()));
        this.amountSmartWithFee = ((Double) in.readValue((Double.class.getClassLoader())));
        this.typeSend = ((String) in.readValue((String.class.getClassLoader())));
        this.urlWithdrawal = ((String) in.readValue((String.class.getClassLoader())));
    }

    public SmartTextData() {
    }

    @JsonProperty("status")
    public String getStatus() {
        return status;
    }

    @JsonProperty("status")
    public void setStatus(String status) {
        this.status = status;
    }

    @JsonProperty("amoutSmart")
    public Integer getAmoutSmart() {
        return amoutSmart;
    }

    @JsonProperty("amoutSmart")
    public void setAmoutSmart(Integer amoutSmart) {
        this.amoutSmart = amoutSmart;
    }

    @JsonProperty("createDate")
    public String getCreateDate() {
        return createDate;
    }

    @JsonProperty("createDate")
    public void setCreateDate(String createDate) {
        this.createDate = createDate;
    }

    @JsonProperty("withdrawalDate")
    public String getWithdrawalDate() {
        return withdrawalDate;
    }

    @JsonProperty("withdrawalDate")
    public void setWithdrawalDate(String withdrawalDate) {
        this.withdrawalDate = withdrawalDate;
    }

    @JsonProperty("depositDate")
    public String getDepositDate() {
        return depositDate;
    }

    @JsonProperty("depositDate")
    public void setDepositDate(String depositDate) {
        this.depositDate = depositDate;
    }

    @JsonProperty("destinationEmail")
    public String getDestinationEmail() {
        return destinationEmail;
    }

    @JsonProperty("destinationEmail")
    public void setDestinationEmail(String destinationEmail) {
        this.destinationEmail = destinationEmail;
    }

    @JsonProperty("phoneNumber")
    public Object getPhoneNumber() {
        return phoneNumber;
    }

    @JsonProperty("phoneNumber")
    public void setPhoneNumber(Object phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    @JsonProperty("orderID")
    public String getOrderID() {
        return orderID;
    }

    @JsonProperty("orderID")
    public void setOrderID(String orderID) {
        this.orderID = orderID;
    }

    @JsonProperty("generatedAddress")
    public String getGeneratedAddress() {
        return generatedAddress;
    }

    @JsonProperty("generatedAddress")
    public void setGeneratedAddress(String generatedAddress) {
        this.generatedAddress = generatedAddress;
    }

    @JsonProperty("txIdDeposit")
    public Object getTxIdDeposit() {
        return txIdDeposit;
    }

    @JsonProperty("txIdDeposit")
    public void setTxIdDeposit(Object txIdDeposit) {
        this.txIdDeposit = txIdDeposit;
    }

    @JsonProperty("txIdWithdrawal")
    public Object getTxIdWithdrawal() {
        return txIdWithdrawal;
    }

    @JsonProperty("txIdWithdrawal")
    public void setTxIdWithdrawal(Object txIdWithdrawal) {
        this.txIdWithdrawal = txIdWithdrawal;
    }

    @JsonProperty("addressForWithdrawal")
    public String getAddressForWithdrawal() {
        return addressForWithdrawal;
    }

    @JsonProperty("addressForWithdrawal")
    public void setAddressForWithdrawal(String addressForWithdrawal) {
        this.addressForWithdrawal = addressForWithdrawal;
    }

    @JsonProperty("addressRefunded")
    public String getAddressRefunded() {
        return addressRefunded;
    }

    @JsonProperty("addressRefunded")
    public void setAddressRefunded(String addressRefunded) {
        this.addressRefunded = addressRefunded;
    }

    @JsonProperty("refundedDate")
    public String getRefundedDate() {
        return refundedDate;
    }

    @JsonProperty("refundedDate")
    public void setRefundedDate(String refundedDate) {
        this.refundedDate = refundedDate;
    }

    @JsonProperty("txIdRefunded")
    public Object getTxIdRefunded() {
        return txIdRefunded;
    }

    @JsonProperty("txIdRefunded")
    public void setTxIdRefunded(Object txIdRefunded) {
        this.txIdRefunded = txIdRefunded;
    }

    @JsonProperty("refundedReason")
    public Object getRefundedReason() {
        return refundedReason;
    }

    @JsonProperty("refundedReason")
    public void setRefundedReason(Object refundedReason) {
        this.refundedReason = refundedReason;
    }

    @JsonProperty("amountSmartSent")
    public Object getAmountSmartSent() {
        return amountSmartSent;
    }

    @JsonProperty("amountSmartSent")
    public void setAmountSmartSent(Object amountSmartSent) {
        this.amountSmartSent = amountSmartSent;
    }

    @JsonProperty("amountSmartWithFee")
    public Double getAmountSmartWithFee() {
        return amountSmartWithFee;
    }

    @JsonProperty("amountSmartWithFee")
    public void setAmountSmartWithFee(Double amountSmartWithFee) {
        this.amountSmartWithFee = amountSmartWithFee;
    }

    @JsonProperty("typeSend")
    public String getTypeSend() {
        return typeSend;
    }

    @JsonProperty("typeSend")
    public void setTypeSend(String typeSend) {
        this.typeSend = typeSend;
    }

    @JsonProperty("urlWithdrawal")
    public String getUrlWithdrawal() {
        return urlWithdrawal;
    }

    @JsonProperty("urlWithdrawal")
    public void setUrlWithdrawal(String urlWithdrawal) {
        this.urlWithdrawal = urlWithdrawal;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(status);
        dest.writeValue(amoutSmart);
        dest.writeValue(createDate);
        dest.writeValue(withdrawalDate);
        dest.writeValue(depositDate);
        dest.writeValue(destinationEmail);
        dest.writeValue(phoneNumber);
        dest.writeValue(orderID);
        dest.writeValue(generatedAddress);
        dest.writeValue(txIdDeposit);
        dest.writeValue(txIdWithdrawal);
        dest.writeValue(addressForWithdrawal);
        dest.writeValue(addressRefunded);
        dest.writeValue(refundedDate);
        dest.writeValue(txIdRefunded);
        dest.writeValue(refundedReason);
        dest.writeValue(amountSmartSent);
        dest.writeValue(amountSmartWithFee);
        dest.writeValue(typeSend);
        dest.writeValue(urlWithdrawal);
    }

    public int describeContents() {
        return 0;
    }

}
