package cc.smartcash.wallet.Models

import android.os.Parcel
import android.os.Parcelable

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.annotation.JsonPropertyOrder

import java.io.Serializable

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder("userWebwallet", "destinationEmail", "addressRefunded", "amountSmart", "typeSend", "phoneNumber", "messageToSend")
@JsonIgnoreProperties(ignoreUnknown = true)
class SmartTextRequest : Serializable, Parcelable {

    constructor()

    @JsonProperty("userWebwallet")
    @get:JsonProperty("userWebwallet")
    @set:JsonProperty("userWebwallet")
    var userWebwallet: String? = null

    @JsonProperty("destinationEmail")
    @get:JsonProperty("destinationEmail")
    @set:JsonProperty("destinationEmail")
    var destinationEmail: String? = null

    @JsonProperty("addressRefunded")
    @get:JsonProperty("addressRefunded")
    @set:JsonProperty("addressRefunded")
    var addressRefunded: String? = null

    @JsonProperty("amountSmart")
    @get:JsonProperty("amountSmart")
    @set:JsonProperty("amountSmart")
    var amountSmart: String? = null

    @JsonProperty("typeSend")
    @get:JsonProperty("typeSend")
    @set:JsonProperty("typeSend")
    var typeSend: String? = null

    @JsonProperty("phoneNumber")
    @get:JsonProperty("phoneNumber")
    @set:JsonProperty("phoneNumber")
    var phoneNumber: String? = null

    @JsonProperty("messageToSend")
    @get:JsonProperty("messageToSend")
    @set:JsonProperty("messageToSend")
    var messageToSend: String? = null

    private constructor(`in`: Parcel) {
        this.userWebwallet = `in`.readValue(String::class.java.classLoader) as String
        this.destinationEmail = `in`.readValue(String::class.java.classLoader) as String
        this.addressRefunded = `in`.readValue(String::class.java.classLoader) as String
        this.amountSmart = `in`.readValue(String::class.java.classLoader) as String
        this.typeSend = `in`.readValue(String::class.java.classLoader) as String
        this.phoneNumber = `in`.readValue(String::class.java.classLoader) as String
        this.messageToSend = `in`.readValue(String::class.java.classLoader) as String
    }

    override fun writeToParcel(dest: Parcel, flags: Int) {
        dest.writeValue(userWebwallet)
        dest.writeValue(destinationEmail)
        dest.writeValue(addressRefunded)
        dest.writeValue(amountSmart)
        dest.writeValue(typeSend)
        dest.writeValue(phoneNumber)
        dest.writeValue(messageToSend)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<SmartTextRequest> {
        override fun createFromParcel(parcel: Parcel): SmartTextRequest {
            return SmartTextRequest(parcel)
        }

        override fun newArray(size: Int): Array<SmartTextRequest?> {
            return arrayOfNulls(size)
        }
    }
}