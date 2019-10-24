package cc.smartcash.wallet.Models

import android.os.Parcel
import android.os.Parcelable

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.annotation.JsonPropertyOrder

import java.io.Serializable

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder("status", "amoutSmart", "createDate", "withdrawalDate", "depositDate", "destinationEmail", "phoneNumber", "orderID", "generatedAddress", "txIdDeposit", "txIdWithdrawal", "addressForWithdrawal", "addressRefunded", "refundedDate", "txIdRefunded", "refundedReason", "amountSmartSent", "amountSmartWithFee", "typeSend", "urlWithdrawal")
@JsonIgnoreProperties(ignoreUnknown = true)
class SmartTextData : Serializable, Parcelable {

    constructor()

    @JsonProperty("status")
    @get:JsonProperty("status")
    @set:JsonProperty("status")
    var status: String? = null

    @JsonProperty("amoutSmart")
    @get:JsonProperty("amoutSmart")
    @set:JsonProperty("amoutSmart")
    var amoutSmart: Int? = null

    @JsonProperty("createDate")
    @get:JsonProperty("createDate")
    @set:JsonProperty("createDate")
    var createDate: String? = null

    @JsonProperty("withdrawalDate")
    @get:JsonProperty("withdrawalDate")
    @set:JsonProperty("withdrawalDate")
    var withdrawalDate: String? = null

    @JsonProperty("depositDate")
    @get:JsonProperty("depositDate")
    @set:JsonProperty("depositDate")
    var depositDate: String? = null

    @JsonProperty("destinationEmail")
    @get:JsonProperty("destinationEmail")
    @set:JsonProperty("destinationEmail")
    var destinationEmail: String? = null

    @JsonProperty("phoneNumber")
    @get:JsonProperty("phoneNumber")
    @set:JsonProperty("phoneNumber")
    var phoneNumber: Any? = null

    @JsonProperty("orderID")
    @get:JsonProperty("orderID")
    @set:JsonProperty("orderID")
    var orderID: String? = null

    @JsonProperty("generatedAddress")
    @get:JsonProperty("generatedAddress")
    @set:JsonProperty("generatedAddress")
    var generatedAddress: String? = null

    @JsonProperty("txIdDeposit")
    @get:JsonProperty("txIdDeposit")
    @set:JsonProperty("txIdDeposit")
    var txIdDeposit: Any? = null

    @JsonProperty("txIdWithdrawal")
    @get:JsonProperty("txIdWithdrawal")
    @set:JsonProperty("txIdWithdrawal")
    var txIdWithdrawal: Any? = null

    @JsonProperty("addressForWithdrawal")
    @get:JsonProperty("addressForWithdrawal")
    @set:JsonProperty("addressForWithdrawal")
    var addressForWithdrawal: String? = null

    @JsonProperty("addressRefunded")
    @get:JsonProperty("addressRefunded")
    @set:JsonProperty("addressRefunded")
    var addressRefunded: String? = null

    @JsonProperty("refundedDate")
    @get:JsonProperty("refundedDate")
    @set:JsonProperty("refundedDate")
    var refundedDate: String? = null

    @JsonProperty("txIdRefunded")
    @get:JsonProperty("txIdRefunded")
    @set:JsonProperty("txIdRefunded")
    var txIdRefunded: Any? = null

    @JsonProperty("refundedReason")
    @get:JsonProperty("refundedReason")
    @set:JsonProperty("refundedReason")
    var refundedReason: Any? = null

    @JsonProperty("amountSmartSent")
    @get:JsonProperty("amountSmartSent")
    @set:JsonProperty("amountSmartSent")
    var amountSmartSent: Any? = null

    @JsonProperty("amountSmartWithFee")
    @get:JsonProperty("amountSmartWithFee")
    @set:JsonProperty("amountSmartWithFee")
    var amountSmartWithFee: Double? = null

    @JsonProperty("typeSend")
    @get:JsonProperty("typeSend")
    @set:JsonProperty("typeSend")
    var typeSend: String? = null

    @JsonProperty("urlWithdrawal")
    @get:JsonProperty("urlWithdrawal")
    @set:JsonProperty("urlWithdrawal")
    var urlWithdrawal: String? = null

    private constructor(`in`: Parcel) {
        this.status = `in`.readValue(String::class.java.classLoader) as String
        this.amoutSmart = `in`.readValue(Int::class.java.classLoader) as Int
        this.createDate = `in`.readValue(String::class.java.classLoader) as String
        this.withdrawalDate = `in`.readValue(String::class.java.classLoader) as String
        this.depositDate = `in`.readValue(String::class.java.classLoader) as String
        this.destinationEmail = `in`.readValue(String::class.java.classLoader) as String
        this.phoneNumber = `in`.readValue(Any::class.java.classLoader)
        this.orderID = `in`.readValue(String::class.java.classLoader) as String
        this.generatedAddress = `in`.readValue(String::class.java.classLoader) as String
        this.txIdDeposit = `in`.readValue(Any::class.java.classLoader)
        this.txIdWithdrawal = `in`.readValue(Any::class.java.classLoader)
        this.addressForWithdrawal = `in`.readValue(String::class.java.classLoader) as String
        this.addressRefunded = `in`.readValue(String::class.java.classLoader) as String
        this.refundedDate = `in`.readValue(String::class.java.classLoader) as String
        this.txIdRefunded = `in`.readValue(Any::class.java.classLoader)
        this.refundedReason = `in`.readValue(Any::class.java.classLoader)
        this.amountSmartSent = `in`.readValue(Any::class.java.classLoader)
        this.amountSmartWithFee = `in`.readValue(Double::class.java.classLoader) as Double
        this.typeSend = `in`.readValue(String::class.java.classLoader) as String
        this.urlWithdrawal = `in`.readValue(String::class.java.classLoader) as String
    }

    override fun writeToParcel(dest: Parcel, flags: Int) {
        dest.writeValue(status)
        dest.writeValue(amoutSmart)
        dest.writeValue(createDate)
        dest.writeValue(withdrawalDate)
        dest.writeValue(depositDate)
        dest.writeValue(destinationEmail)
        dest.writeValue(phoneNumber)
        dest.writeValue(orderID)
        dest.writeValue(generatedAddress)
        dest.writeValue(txIdDeposit)
        dest.writeValue(txIdWithdrawal)
        dest.writeValue(addressForWithdrawal)
        dest.writeValue(addressRefunded)
        dest.writeValue(refundedDate)
        dest.writeValue(txIdRefunded)
        dest.writeValue(refundedReason)
        dest.writeValue(amountSmartSent)
        dest.writeValue(amountSmartWithFee)
        dest.writeValue(typeSend)
        dest.writeValue(urlWithdrawal)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<SmartTextData> {
        override fun createFromParcel(parcel: Parcel): SmartTextData {
            return SmartTextData(parcel)
        }

        override fun newArray(size: Int): Array<SmartTextData?> {
            return arrayOfNulls(size)
        }
    }


}
