package cc.smartcash.smarthub.models

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

import java.io.Serializable

@JsonIgnoreProperties(ignoreUnknown = true)
data class LoginResponse(


        @Expose
        @JsonProperty("access_token")
        @get:JsonProperty("access_token")
        @set:JsonProperty("access_token")
        @SerializedName("access_token")

        var accessToken: String? = null,


        @Expose
        @JsonProperty("token_type")
        @get:JsonProperty("token_type")
        @set:JsonProperty("token_type")
        @SerializedName("token_type")
        var tokenType: String? = null,


        @Expose
        @JsonProperty("expires_in")
        @get:JsonProperty("expires_in")
        @set:JsonProperty("expires_in")
        @SerializedName("expires_in")
        var expiresIn: Long? = null,


        @Expose
        @JsonProperty("refresh_token")
        @get:JsonProperty("refresh_token")
        @set:JsonProperty("refresh_token")
        @SerializedName("refresh_token")
        var refreshToken: String? = null

) : Serializable