package cc.smartcash.wallet.Models

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty
import java.io.Serializable

@JsonIgnoreProperties(ignoreUnknown = true)
data class UserRecoveryKey(
        @JsonProperty("recoveryKey")
        @get:JsonProperty("recoveryKey")
        @set:JsonProperty("recoveryKey")
        var recoveryKey: String? = null,

        @JsonProperty("termsVersion")
        @get:JsonProperty("termsVersion")
        @set:JsonProperty("termsVersion")
        var termsVersion: String? = null,

        @JsonProperty("termsText")
        @get:JsonProperty("termsText")
        @set:JsonProperty("termsText")
        var termsText: String? = null

) : Serializable
