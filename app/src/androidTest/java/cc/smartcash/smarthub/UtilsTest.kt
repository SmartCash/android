package cc.smartcash.smarthub

import cc.smartcash.smarthub.utils.Util
import org.junit.Assert
import org.junit.Test

class UtilsTest {

    @Test
    fun testQrCodeParseWithoutValue() {
        val qrCodeString = "smartcash:SQcQL4ZmXZsgcFQoLs6qRQ2psB27BwKVdA"
        val qrCodeResult = Util.parseQrCode(qrCodeString)
        val parts = qrCodeResult.split("-".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
        println("qrCodeResult: $qrCodeResult")
        println("Address: " + parts[0])
        println("Amount: " + parts[1])
        Assert.assertEquals(parts[0], "SQcQL4ZmXZsgcFQoLs6qRQ2psB27BwKVdA")
        Assert.assertEquals(parts[1], "0")
    }

    @Test
    fun testQrCodeParseWithValue() {
        val qrCodeString = "smartcash:SQcQL4ZmXZsgcFQoLs6qRQ2psB27BwKVdA?amount=2,000000"
        val qrCodeResult = Util.parseQrCode(qrCodeString)
        val parts = qrCodeResult.split("-".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
        println("qrCodeResult: $qrCodeResult")
        println("Address: " + parts[0])
        println("Amount: " + parts[1])
        Assert.assertEquals(parts[0], "SQcQL4ZmXZsgcFQoLs6qRQ2psB27BwKVdA")
        Assert.assertEquals(parts[1], "2,000000")
    }

    @Test
    fun testQrCodeParseAnyValue() {
        val qrCodeString = "SQcQL4ZmXZsgcFQoLs6qRQ2psB27BwKVdA"
        val qrCodeResult = Util.parseQrCode(qrCodeString)
        val parts = qrCodeResult.split("-".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
        println("qrCodeResult: $qrCodeResult")
        println("Address: " + parts[0])
        Assert.assertEquals(parts[0], "SQcQL4ZmXZsgcFQoLs6qRQ2psB27BwKVdA")
    }
}
