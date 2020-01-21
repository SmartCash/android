package cc.smartcash.smarthub

import org.junit.Assert
import org.junit.Test
import org.smartcashj.core.Address
import org.smartcashj.core.AddressFormatException
import org.smartcashj.params.MainNetParams

class SmartCashJTest {

    @Test
    fun isValidAddress() {
        try {
            val fromString = Address.fromString(MainNetParams.get(), "SQcQL4ZmXZsgcFQoLs6qRQ2psB27BwKVdA")
            Assert.assertTrue(fromString != null)
        } catch (e: AddressFormatException) {

        }
    }

}
