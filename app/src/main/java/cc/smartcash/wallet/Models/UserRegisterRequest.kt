package cc.smartcash.wallet.Models

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty
import java.io.Serializable

@JsonIgnoreProperties(ignoreUnknown = true)
class UserRegisterRequest : Serializable {

    @JsonProperty("email")
    var email: String? = null

    @JsonProperty("password")
    var password: String? = null

    @JsonProperty("recoveryKey")
    var recoveryKey: String? = null

    @JsonProperty("firstName")
    var firstName: String? = null

    @JsonProperty("lastName")
    var lastName: String? = null

    @JsonProperty("username")
    var username: String? = null

    @JsonProperty("photo")
    var photo: String? = null

    @JsonProperty("facebookId")
    var facebookId: String? = null

    @JsonProperty("phone")
    var phone: String? = null

    @JsonProperty("userDescription")
    var userDescription: String? = null

    @JsonProperty("countryCode")
    var countryCode: String? = null

    @JsonProperty("timeZone")
    var timeZone: String? = null

    @JsonProperty("termsVersion")
    var termsVersion: String? = null

}



