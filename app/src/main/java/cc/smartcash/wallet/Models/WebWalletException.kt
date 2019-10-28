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
@JsonPropertyOrder("error", "error_description")
class WebWalletException private constructor(`in`: Parcel) : Serializable, Parcelable {

    @JsonProperty("error")
    @SerializedName("error")
    @Expose
    @get:JsonProperty("error")
    @get:SerializedName("error")
    @set:JsonProperty("error")
    @set:SerializedName("error")
    var error: String? = null


    @JsonProperty("error_description")
    @SerializedName("error_description")
    @Expose
    @get:JsonProperty("error_description")
    @get:SerializedName("error_description")
    @set:JsonProperty("error_description")
    @set:SerializedName("error_description")
    var errorDescription: String? = null


    init {
        this.error = `in`.readValue(String::class.java.classLoader) as String
        this.errorDescription = `in`.readValue(String::class.java.classLoader) as String
    }

    override fun writeToParcel(dest: Parcel, flags: Int) {
        dest.writeValue(error)
        dest.writeValue(errorDescription)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<WebWalletException> {
        override fun createFromParcel(parcel: Parcel): WebWalletException {
            return WebWalletException(parcel)
        }

        override fun newArray(size: Int): Array<WebWalletException?> {
            return arrayOfNulls(size)
        }
    }
}