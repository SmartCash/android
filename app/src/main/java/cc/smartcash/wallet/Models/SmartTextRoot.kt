package cc.smartcash.wallet.Models

import android.os.Parcel
import android.os.Parcelable

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.annotation.JsonPropertyOrder

import java.io.Serializable

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder("status", "data")
@JsonIgnoreProperties(ignoreUnknown = true)
class SmartTextRoot : Serializable, Parcelable {
    @JsonProperty("status")
    @get:JsonProperty("status")
    @set:JsonProperty("status")
    var status: String? = null
    @JsonProperty("data")
    @get:JsonProperty("data")
    @set:JsonProperty("data")
    var data: SmartTextData? = null

    private constructor(`in`: Parcel) {
        this.status = `in`.readValue(String::class.java.classLoader) as String
        this.data = `in`.readValue(SmartTextData::class.java.classLoader) as SmartTextData
    }

    constructor()

    override fun writeToParcel(dest: Parcel, flags: Int) {
        dest.writeValue(status)
        dest.writeValue(data)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<SmartTextRoot> {
        override fun createFromParcel(parcel: Parcel): SmartTextRoot {
            return SmartTextRoot(parcel)
        }

        override fun newArray(size: Int): Array<SmartTextRoot?> {
            return arrayOfNulls(size)
        }
    }


}