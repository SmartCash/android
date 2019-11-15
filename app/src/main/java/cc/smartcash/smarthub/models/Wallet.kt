package cc.smartcash.smarthub.models

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.google.gson.annotations.SerializedName
import java.io.Serializable
import java.math.BigInteger
import java.util.*

@JsonIgnoreProperties(ignoreUnknown = true)
data class Wallet(
        @SerializedName("walletId")
        var walletId: Int? = null,
        @SerializedName("displayName")
        var displayName: String? = null,
        @SerializedName("address")
        var address: String? = null,
        @SerializedName("qrCode")
        var qrCode: String? = null,
        @SerializedName("balance")
        var balance: Double? = null,
        @SerializedName("totalSent")
        var totalSent: Double? = null,
        @SerializedName("totalReceived")
        var totalReceived: Double? = null,
        @SerializedName("position")
        var position: Int? = null,
        @SerializedName("isRewards")
        var isRewards: Boolean? = null,
        @SerializedName("isVault")
        var isVault: Boolean? = null,
        @SerializedName("isScheduled")
        var isScheduled: Boolean? = null,
        @SerializedName("cardId")
        var cardId: BigInteger? = null,
        @SerializedName("transactions")
        var transactions: ArrayList<Transaction>? = null
) : Serializable
