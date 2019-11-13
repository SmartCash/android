package cc.smartcash.smarthub.Models

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.google.gson.annotations.SerializedName
import java.io.Serializable

@JsonIgnoreProperties(ignoreUnknown = true)
data class UserLogin(
        @SerializedName("username")
        var username: String? = null,
        @SerializedName("password")
        var password: String? = null,
        @SerializedName("grant_type")
        var grantType: String? = null,
        @SerializedName("client_id")
        var clientId: String? = null,
        @SerializedName("TwoFactorAuthentication")
        var twoFactorAuthentication: String? = null,
        @SerializedName("client_type")
        var clientType: String? = null,
        @SerializedName("client_ip")
        var clientIp: String? = null,
        @SerializedName("client_secret")
        var clientSecret: String? = null
) : Serializable 