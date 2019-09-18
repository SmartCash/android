package cc.smartcash.wallet.Models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "email",
        "fromAddress",
        "toAddress",
        "amount",
        "password",
        "code",
        "destinationEmail",
        "phoneNumber",
        "startDate",
        "endDate",
        "transactionDate",
        "recurrenceType",
        "recurringLabel"
})
@JsonIgnoreProperties(ignoreUnknown = true)
public class WalletPaymentFeeRequest {

    @JsonProperty("email")
    private Object email;
    @JsonProperty("fromAddress")
    private String fromAddress;
    @JsonProperty("toAddress")
    private String toAddress;
    @JsonProperty("amount")
    private Double amount;
    @JsonProperty("password")
    private String password;
    @JsonProperty("code")
    private Object code;
    @JsonProperty("destinationEmail")
    private String destinationEmail;
    @JsonProperty("phoneNumber")
    private String phoneNumber;
    @JsonProperty("startDate")
    private String startDate;
    @JsonProperty("endDate")
    private String endDate;
    @JsonProperty("transactionDate")
    private String transactionDate;
    @JsonProperty("recurrenceType")
    private Integer recurrenceType;
    @JsonProperty("recurringLabel")
    private String recurringLabel;

    @JsonProperty("email")
    public Object getEmail() {
        return email;
    }

    @JsonProperty("email")
    public void setEmail(Object email) {
        this.email = email;
    }

    @JsonProperty("fromAddress")
    public String getFromAddress() {
        return fromAddress;
    }

    @JsonProperty("fromAddress")
    public void setFromAddress(String fromAddress) {
        this.fromAddress = fromAddress;
    }

    @JsonProperty("toAddress")
    public String getToAddress() {
        return toAddress;
    }

    @JsonProperty("toAddress")
    public void setToAddress(String toAddress) {
        this.toAddress = toAddress;
    }

    @JsonProperty("amount")
    public Double getAmount() {
        return amount;
    }

    @JsonProperty("amount")
    public void setAmount(Double amount) {
        this.amount = amount;
    }

    @JsonProperty("password")
    public String getPassword() {
        return password;
    }

    @JsonProperty("password")
    public void setPassword(String password) {
        this.password = password;
    }

    @JsonProperty("code")
    public Object getCode() {
        return code;
    }

    @JsonProperty("code")
    public void setCode(Object code) {
        this.code = code;
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
    public String getPhoneNumber() {
        return phoneNumber;
    }

    @JsonProperty("phoneNumber")
    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    @JsonProperty("startDate")
    public String getStartDate() {
        return startDate;
    }

    @JsonProperty("startDate")
    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    @JsonProperty("endDate")
    public String getEndDate() {
        return endDate;
    }

    @JsonProperty("endDate")
    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    @JsonProperty("transactionDate")
    public String getTransactionDate() {
        return transactionDate;
    }

    @JsonProperty("transactionDate")
    public void setTransactionDate(String transactionDate) {
        this.transactionDate = transactionDate;
    }

    @JsonProperty("recurrenceType")
    public Integer getRecurrenceType() {
        return recurrenceType;
    }

    @JsonProperty("recurrenceType")
    public void setRecurrenceType(Integer recurrenceType) {
        this.recurrenceType = recurrenceType;
    }

    @JsonProperty("recurringLabel")
    public String getRecurringLabel() {
        return recurringLabel;
    }

    @JsonProperty("recurringLabel")
    public void setRecurringLabel(String recurringLabel) {
        this.recurringLabel = recurringLabel;
    }

}