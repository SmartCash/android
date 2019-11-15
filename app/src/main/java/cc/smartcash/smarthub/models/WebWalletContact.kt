package cc.smartcash.smarthub.models


import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty
import java.io.Serializable

@JsonIgnoreProperties(ignoreUnknown = true)
data class WebWalletContact(

        @JsonProperty("contactId")
        var contactId: Int? = null,
        @JsonProperty("address")
        var address: String? = null,
        @JsonProperty("name")
        var name: String? = null,
        @JsonProperty("email")
        var email: Any? = null,
        @JsonProperty("phone")
        var phone: Any? = null

) : Serializable 