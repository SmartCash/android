package cc.smartcash.wallet;

import org.junit.Assert;
import org.junit.Test;

import cc.smartcash.wallet.Utils.Util;

public class UtilsTest {

    @Test
    public void testQrCodeParseWithoutValue() {
        String qrCodeString = "smartcash:SQcQL4ZmXZsgcFQoLs6qRQ2psB27BwKVdA";
        String qrCodeResult = Util.parseQrCode(qrCodeString);
        String[] parts = qrCodeResult.split("-");
        System.out.println("qrCodeResult: " + qrCodeResult);
        System.out.println("Address: " + parts[0]);
        System.out.println("Amount: " + parts[1]);
        Assert.assertEquals(parts[0], "SQcQL4ZmXZsgcFQoLs6qRQ2psB27BwKVdA");
        Assert.assertEquals(parts[1], "0");
    }

    @Test
    public void testQrCodeParseWithValue() {
        String qrCodeString = "smartcash:SQcQL4ZmXZsgcFQoLs6qRQ2psB27BwKVdA?amount=2,000000";
        String qrCodeResult = Util.parseQrCode(qrCodeString);
        String[] parts = qrCodeResult.split("-");
        System.out.println("qrCodeResult: " + qrCodeResult);
        System.out.println("Address: " + parts[0]);
        System.out.println("Amount: " + parts[1]);
        Assert.assertEquals(parts[0], "SQcQL4ZmXZsgcFQoLs6qRQ2psB27BwKVdA");
        Assert.assertEquals(parts[1], "2,000000");
    }

    @Test
    public void testQrCodeParseAnyValue() {
        String qrCodeString = "SQcQL4ZmXZsgcFQoLs6qRQ2psB27BwKVdA";
        String qrCodeResult = Util.parseQrCode(qrCodeString);
        String[] parts = qrCodeResult.split("-");
        System.out.println("qrCodeResult: " + qrCodeResult);
        System.out.println("Address: " + parts[0]);
        Assert.assertEquals(parts[0], "SQcQL4ZmXZsgcFQoLs6qRQ2psB27BwKVdA");
    }
}
