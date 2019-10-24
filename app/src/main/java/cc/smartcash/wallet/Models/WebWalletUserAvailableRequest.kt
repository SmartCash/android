package cc.smartcash.wallet.Models

import android.os.Parcel
import android.os.Parcelable

import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.annotation.JsonPropertyOrder
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

import java.io.Serializable

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder("data")
class WebWalletUserAvailableRequest : Serializable, Parcelable {
    @JsonProperty("data")
    @SerializedName("data")
    @Expose
    @get:JsonProperty("data")
    @set:JsonProperty("data")
    var data: String? = null

    constructor()

    constructor(data: String) {
        this.data = data
    }

    private constructor(`in`: Parcel) {
        this.data = `in`.readValue(String::class.java.classLoader) as String
    }

    override fun writeToParcel(dest: Parcel, flags: Int) {
        dest.writeValue(data)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<WebWalletUserAvailableRequest> {
        override fun createFromParcel(parcel: Parcel): WebWalletUserAvailableRequest {
            return WebWalletUserAvailableRequest(parcel)
        }

        override fun newArray(size: Int): Array<WebWalletUserAvailableRequest?> {
            return arrayOfNulls(size)
        }
    }


}