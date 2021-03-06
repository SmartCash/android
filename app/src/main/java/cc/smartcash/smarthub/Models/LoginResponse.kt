package cc.smartcash.smarthub.Models

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty

import java.io.Serializable

@JsonIgnoreProperties(ignoreUnknown = true)
data class LoginResponse(
        @get:JsonProperty("access_token")
        var accessToken: String? = null,

        @get:JsonProperty("token_type")
        var tokenType: String? = null,

        @get:JsonProperty("expires_in")
        var expiresIn: String? = null,

        @get:JsonProperty("refresh_token")
        var refreshToken: String? = null

) : Serializable