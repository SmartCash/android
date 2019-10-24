package cc.smartcash.wallet.Models

import android.os.Parcel
import android.os.Parcelable

import com.fasterxml.jackson.annotation.JsonIgnoreProperties

import java.io.Serializable

@JsonIgnoreProperties(ignoreUnknown = true)
class Transaction : Parcelable, Serializable {
    var hash: String? = null
    var timestamp: String? = null
    var amount: Double? = null
        private set
    var direction: String? = null
    private var toAddress: String? = null
    private var isPending: Boolean? = null
    private var blockindex: Int? = null
    private var isNew: Boolean? = null
    private var isConfirmed: Boolean? = null
    private var orderData: String? = null

    private constructor(`in`: Parcel) {
        this.hash = `in`.readString()
        this.timestamp = `in`.readString()
        this.amount = `in`.readValue(Double::class.java.classLoader) as Double
        this.direction = `in`.readString()
        this.toAddress = `in`.readString()
        this.isPending = `in`.readValue(Boolean::class.java.classLoader) as Boolean
        this.blockindex = `in`.readValue(Int::class.java.classLoader) as Int
        this.isNew = `in`.readValue(Boolean::class.java.classLoader) as Boolean
        this.isConfirmed = `in`.readValue(Boolean::class.java.classLoader) as Boolean
        this.orderData = `in`.readString()
    }

    constructor()

    fun setAmout(amount: Double?) {
        this.amount = amount
    }

    override fun describeContents(): Int {
        return 0
    }

    override fun writeToParcel(dest: Parcel, flags: Int) {
        dest.writeString(this.hash)
        dest.writeString(this.timestamp)
        dest.writeValue(this.amount)
        dest.writeString(this.direction)
        dest.writeString(this.toAddress)
        dest.writeValue(this.isPending)
        dest.writeValue(this.blockindex)
        dest.writeValue(this.isNew)
        dest.writeValue(this.isConfirmed)
        dest.writeString(this.orderData)
    }

    companion object CREATOR : Parcelable.Creator<Transaction> {
        override fun createFromParcel(parcel: Parcel): Transaction {
            return Transaction(parcel)
        }

        override fun newArray(size: Int): Array<Transaction?> {
            return arrayOfNulls(size)
        }
    }
}
