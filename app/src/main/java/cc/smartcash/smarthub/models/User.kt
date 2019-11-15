package cc.smartcash.smarthub.models

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import java.math.BigInteger
import java.util.*

@JsonIgnoreProperties(ignoreUnknown = true)
data class User(

        @Expose
        @SerializedName("password")
        var password: String? = null,

        @Expose
        @SerializedName("confirmPassword")
        var confirmPassword: String? = null,

        @Expose
        @SerializedName("recoveryKey")
        var recoveryKey: String? = null,

        @Expose
        @SerializedName("notifications")
        var notifications: Int? = null,

        @Expose
        @SerializedName("lastLoginDate")
        var lastLoginDate: String? = null,

        @Expose
        @SerializedName("termsVersion")
        var termsVersion: String? = null,

        @Expose
        @SerializedName("is2FAEnabled")
        var is2FAEnabled: Boolean? = null,

        @Expose
        @SerializedName("userId")
        var userId: BigInteger? = null,

        @Expose
        @SerializedName("firstName")
        var firstName: String? = null,

        @Expose
        @SerializedName("lastName")
        var lastName: String? = null,

        @Expose
        @SerializedName("username")
        var username: String? = null,

        @Expose
        @SerializedName("email")
        var email: String? = null,

        @Expose
        @SerializedName("facebookId")
        var facebookId: String? = null,

        @Expose
        @SerializedName("countryCode")
        var countryCode: String? = null,

        @Expose
        @SerializedName("app")
        var wallet: ArrayList<Wallet>? = null,

        @Expose
        @SerializedName("require2faToSend")
        var require2faToSend: Boolean? = null
)