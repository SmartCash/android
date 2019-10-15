package cc.smartcash.wallet.Utils;

import android.content.Context;
import android.content.res.AssetManager;
import android.text.TextUtils;
import android.util.Log;
import android.widget.TextView;

import java.io.InputStream;
import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Properties;
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;

import cc.smartcash.wallet.R;

public class Util {

    public static final String TAG = Util.class.getSimpleName();
    public static final String UTF_8 = "UTF-8";
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
        } else if (qrCodeString.contains(prefixQueryStringQrCode) && !qrCodeString.contains(amountQueryStringQrCode)) {
            return parseQrCodeWithoutValue(qrCodeString);
        } else {
            return qrCodeString;
        }
    }

    public static String getProperty(String key, Context context) {
        try {
            Properties properties = new Properties();
            AssetManager assetManager = context.getAssets();
            InputStream inputStream = assetManager.open("config.properties");
            properties.load(inputStream);
            return properties.getProperty(key);
        } catch (Exception ex) {
            Log.e(TAG, ex.getMessage());
        }
        return null;
    }

    public static String getString(TextView view) {
        return view.getText().toString();
    }

    public static BigDecimal getBigDecimal(TextView view) {
        return BigDecimal.valueOf(Util.getDouble(view));
    }

    public static Double getDouble(TextView view) {
        return Double.parseDouble(Util.getString(view));
    }

    public static boolean compareString(TextView textView, TextView textView2) {
        return getString(textView).equalsIgnoreCase(getString(textView2));
    }

    public static boolean isNullOrEmpty(TextView view) {
        return view.getText() == null || view.getText().toString() == null || view.getText().toString().isEmpty();
    }

    public static boolean isNullOrEmpty(String text) {
        return text == null || text.isEmpty();
    }

    public static boolean isTaskComplete(int progress) {
        return ((progress * 2) == 100); //percentage of the progress and the process
    }

    public final static boolean isValidEmail(CharSequence target) {
        return !TextUtils.isEmpty(target) && android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches();
    }

    public static String amountInCoinConcatenation(Context context, String coinTick) {
        return String.format(Locale.getDefault(), context.getString(R.string.send_amount_in_coin_label).replace("%s", coinTick));
    }

    public static String amountInDefaultCryptoConcatenation(Context context) {
        return amountInCoinConcatenation(context, context.getString(R.string.default_crypto));
    }

    public static String getDateFromEpoch(long epoch) {
        Date date = new Date(epoch * 1000L);
        DateFormat format = new SimpleDateFormat(KEYS.KEY_DATE_FORMAT);
        format.setTimeZone(TimeZone.getTimeZone(KEYS.KEY_DATE_TIMEZONE));
        return format.format(date);
    }

    public static String getDate() {
        Date date = new Date();
        DateFormat format = new SimpleDateFormat(KEYS.KEY_DATE_FORMAT);
        format.setTimeZone(TimeZone.getTimeZone(KEYS.KEY_DATE_TIMEZONE));
        return format.format(date);
    }

    public static Date getDate(String date) {

        if (Util.isNullOrEmpty(date)) return null;

        DateFormat format = new SimpleDateFormat(KEYS.KEY_DATE_FORMAT);
        format.setTimeZone(TimeZone.getTimeZone(KEYS.KEY_DATE_TIMEZONE));

        try {
            return format.parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }


    public static long dateDiff(Date d1, Date d2) {
        long diff = d2.getTime() - d1.getTime();//as given
        //long seconds = TimeUnit.MILLISECONDS.toSeconds(diff);
        long minutes = TimeUnit.MILLISECONDS.toMinutes(diff);

        return minutes;
    }

    public static long dateDiffFromNow(Date d1) {

        if (d1 == null) return Long.MAX_VALUE;

        long diff = new Date().getTime() - d1.getTime();//as given
        long minutes = TimeUnit.MILLISECONDS.toMinutes(diff);
        return minutes;
    }

}
