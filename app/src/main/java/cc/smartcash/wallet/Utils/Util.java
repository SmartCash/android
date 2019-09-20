package cc.smartcash.wallet.Utils;

public class Util {

    public static final String prefixQueryStringQrCode = "smartcash:";
    public static final String amountQueryStringQrCode = "?amount=";

    public static String parseQrCodeWithValue(String qrCodeString) {


        int startPrefix = qrCodeString.indexOf(prefixQueryStringQrCode);
        int endPrefix = qrCodeString.indexOf(amountQueryStringQrCode);

        int startAmount = qrCodeString.indexOf(amountQueryStringQrCode);
        int endAmount = qrCodeString.length();


        String address = qrCodeString.substring(startPrefix + prefixQueryStringQrCode.length(), endPrefix);
        String amount = qrCodeString.substring(startAmount + amountQueryStringQrCode.length(), endAmount);

        return address + "-" + amount;

    }

    public static String parseQrCodeWithoutValue(String qrCodeString) {

        int startPrefix = qrCodeString.indexOf(prefixQueryStringQrCode);
        int endPrefix = qrCodeString.length();

        String address = qrCodeString.substring(startPrefix + prefixQueryStringQrCode.length(), endPrefix);
        String amount = "0";

        return address + "-" + amount;

    }

    public static String parseQrCode(String qrCodeString) {

        if (qrCodeString.indexOf(prefixQueryStringQrCode) >= 0 && qrCodeString.indexOf(amountQueryStringQrCode) >= 0) {
            return parseQrCodeWithValue(qrCodeString);
        } else if (qrCodeString.contains("smartcash:") && !qrCodeString.contains("&amount=")) {
            return parseQrCodeWithoutValue(qrCodeString);
        } else {
            return qrCodeString;
        }
    }
}
