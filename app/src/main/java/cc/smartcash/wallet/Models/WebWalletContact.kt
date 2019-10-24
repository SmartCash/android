package cc.smartcash.wallet.Models


import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty
import java.io.Serializable

@JsonIgnoreProperties(ignoreUnknown = true)
class WebWalletContact : Serializable {

    @JsonProperty("contactId")
    var contactId: Int? = null
    @JsonProperty("address")
    var address: String? = null
    @JsonProperty("name")
    var name: String? = null
    @JsonProperty("email")
    var email: Any? = null
    @JsonProperty("phone")
    var phone: Any? = null


}
