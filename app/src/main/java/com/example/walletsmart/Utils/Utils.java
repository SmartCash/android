package com.example.walletsmart.Utils;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Base64;
import android.widget.Toast;

import com.example.walletsmart.Activities.MainActivity;
import com.example.walletsmart.Models.Coin;
import com.example.walletsmart.Models.User;
import com.example.walletsmart.Models.Wallet;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class Utils {


    private static SharedPreferences mPrefs;
    private Gson gson = new Gson();
    private static final String FILE_NAME = "Smartcash";
    private static ClipboardManager clipboardManager;

    static SharedPreferences getPreferences (Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context);
    }

    public void saveActualSelectedCoin(Context context, Coin coin) {
        mPrefs = context.getSharedPreferences(FILE_NAME, context.MODE_PRIVATE);
        SharedPreferences.Editor prefsEditor = getPreferences(context).edit();
        String json = gson.toJson(coin);
        prefsEditor.putString("ActualSelectedCoin", json);
        prefsEditor.apply();
    }

    public Coin getActualSelectedCoin(Context context) {
        mPrefs = context.getSharedPreferences(FILE_NAME, context.MODE_PRIVATE);
        String json = getPreferences(context).getString("ActualSelectedCoin", "");
        Coin coin = gson.fromJson(json, Coin.class);
        return coin;
    }

    public void saveCurrentPrice(Context context, ArrayList<Coin> coins) {
        mPrefs = context.getSharedPreferences(FILE_NAME, context.MODE_PRIVATE);
        SharedPreferences.Editor prefsEditor = getPreferences(context).edit();
        String json = gson.toJson(coins);
        prefsEditor.putString("CurrentPrices", json);
        prefsEditor.apply();
    }

    public ArrayList<Coin> getCurrentPrice(Context context) {
        mPrefs = context.getSharedPreferences(FILE_NAME, context.MODE_PRIVATE);
        String json = getPreferences(context).getString("CurrentPrices", "");
        Type type = new TypeToken<ArrayList<Coin>>() {}.getType();
        return gson.fromJson(json, type);
    }

    public void saveUser(Context context, User user) {
        mPrefs = context.getSharedPreferences(FILE_NAME, context.MODE_PRIVATE);
        SharedPreferences.Editor prefsEditor = getPreferences(context).edit();
        String json = gson.toJson(user);
        prefsEditor.putString("User", json);
        prefsEditor.apply();
    }

    public User getUser(Context context) {
        mPrefs = context.getSharedPreferences(FILE_NAME, context.MODE_PRIVATE);
        String json = getPreferences(context).getString("User", "");
        User user = gson.fromJson(json, User.class);
        return user;
    }

    public void saveBoolean(Context context, Boolean bool, String key) {
        mPrefs = context.getSharedPreferences(FILE_NAME, context.MODE_PRIVATE);
        SharedPreferences.Editor prefsEditor = getPreferences(context).edit();
        prefsEditor.putBoolean(key, bool);
        prefsEditor.apply();
    }

    public Boolean getBoolean(Context context, String key) {
        mPrefs = context.getSharedPreferences(FILE_NAME, context.MODE_PRIVATE);
        Boolean bool = getPreferences(context).getBoolean(key, false);
        return bool;
    }

    public void saveWallet(Context context, Wallet wallet) {
        mPrefs = context.getSharedPreferences(FILE_NAME, context.MODE_PRIVATE);
        SharedPreferences.Editor prefsEditor = getPreferences(context).edit();
        String json = gson.toJson(wallet);
        prefsEditor.putString("Wallet", json);
        prefsEditor.apply();
    }

    public Wallet getWallet(Context context) {
        mPrefs = context.getSharedPreferences(FILE_NAME, context.MODE_PRIVATE);
        String json = getPreferences(context).getString("Wallet", "");
        Wallet wallet = gson.fromJson(json, Wallet.class);
        return wallet;
    }

    public static void copyToClipboard(Context context, String text) {
        clipboardManager = (ClipboardManager) context.getSystemService(context.CLIPBOARD_SERVICE);
        ClipData clipData = ClipData.newPlainText(text, text);
        clipboardManager.setPrimaryClip(clipData);

        Toast.makeText(context, "Copied!", Toast.LENGTH_SHORT).show();
    }

    public static String getIPAddress(boolean useIPv4) {
        try {
            List<NetworkInterface> interfaces = Collections.list(NetworkInterface.getNetworkInterfaces());
            for (NetworkInterface intf : interfaces) {
                List<InetAddress> addrs = Collections.list(intf.getInetAddresses());
                for (InetAddress addr : addrs) {
                    if (!addr.isLoopbackAddress()) {
                        String sAddr = addr.getHostAddress();
                        //boolean isIPv4 = InetAddressUtils.isIPv4Address(sAddr);
                        boolean isIPv4 = sAddr.indexOf(':')<0;

                        if (useIPv4) {
                            if (isIPv4)
                                return sAddr;
                        } else {
                            if (!isIPv4) {
                                int delim = sAddr.indexOf('%'); // drop ip6 zone suffix
                                return delim<0 ? sAddr.toUpperCase() : sAddr.substring(0, delim).toUpperCase();
                            }
                        }
                    }
                }
            }
        } catch (Exception ignored) { } // for now eat exceptions
        return "";
    }

    public static ArrayList<Coin> convertToArrayList(String string) {
        String newPrices;
        ArrayList<String> arrayListStrings;
        ArrayList<Coin> coins = new ArrayList<Coin>();

        newPrices = string.replace("{", "").replace("}", "");

        arrayListStrings = new ArrayList<String>(Arrays.asList(newPrices.split(",")));

        for (String element: arrayListStrings) {
            String singleCoin = element.replace("\"", "");
            ArrayList<String> arrayListKeyAndValue = new ArrayList<String>(Arrays.asList(singleCoin.split(":")));
            Coin coin = new Coin(arrayListKeyAndValue.get(0), Double.parseDouble(arrayListKeyAndValue.get(1)));
            coins.add(coin);
        }

        Collections.sort(coins, (p1, p2) -> p1.getName().compareTo(p2.getName()));

        Coin smartcash = new Coin("SMART", 1.0);

        coins.add(0, smartcash);

        return coins;
    }

    public void saveToken(Context context, String token) {
        mPrefs = context.getSharedPreferences(FILE_NAME, context.MODE_PRIVATE);
        SharedPreferences.Editor prefsEditor = getPreferences(context).edit();
        prefsEditor.putString("Token", token);
        prefsEditor.apply();
    }

    public String getToken(Context context) {
        mPrefs = context.getSharedPreferences(FILE_NAME, context.MODE_PRIVATE);
        String token = getPreferences(context).getString("Token", "");
        return token;
    }

    public void deleteSharedPreferences(Context context) {
        mPrefs = context.getSharedPreferences(FILE_NAME, context.MODE_PRIVATE);
        getPreferences(context).edit().clear().apply();
    }
    public String converterValue(double amount, double value){
        Double valueAmount = amount * value;

        return valueAmount.toString();
    }

    public BigDecimal converterBigDecimal(BigDecimal amount, BigDecimal value){
        BigDecimal valueAmount = amount.multiply(value);

        return valueAmount;
    }

    public void savePin(byte[] pin, Context context) {
        String stringPin = new String(pin, Charset.forName("ISO-8859-1"));

        mPrefs = context.getSharedPreferences(FILE_NAME, context.MODE_PRIVATE);
        SharedPreferences.Editor prefsEditor = getPreferences(context).edit();
        prefsEditor.putString("PIN", stringPin);
        prefsEditor.apply();
    }

    public byte[] getPin(Context context) {
        mPrefs = context.getSharedPreferences(FILE_NAME, context.MODE_PRIVATE);
        String stringPin = getPreferences(context).getString("PIN", "");

        byte[] pin;

        if (stringPin == null || stringPin == "")
            return null;
        else {
            pin = stringPin.getBytes(Charset.forName("ISO-8859-1"));
            return pin;
        }
    }

    public void deleteUser(Context context) {
        mPrefs = context.getSharedPreferences(FILE_NAME, context.MODE_PRIVATE);
        SharedPreferences.Editor editor = mPrefs.edit();
        editor.remove("Token");
        editor.commit();
    }

    public void saveByte(byte[] bytes, Context context, String key) {
        String string = new String(bytes, Charset.forName("ISO-8859-1"));

        mPrefs = context.getSharedPreferences(FILE_NAME, context.MODE_PRIVATE);
        SharedPreferences.Editor prefsEditor = getPreferences(context).edit();
        prefsEditor.putString(key, string);
        prefsEditor.apply();
    }

    public byte[] getByte(Context context, String key) {
        mPrefs = context.getSharedPreferences(FILE_NAME, context.MODE_PRIVATE);
        String string = getPreferences(context).getString(key, "");

        byte[] bytes;

        if (string == null || string == "")
            return null;
        else {
            bytes = string.getBytes(Charset.forName("ISO-8859-1"));
            return bytes;
        }
    }

    public void saveIv(byte[] iv, Context context) {
        String stringIv = new String(iv, Charset.forName("ISO-8859-1"));
        mPrefs = context.getSharedPreferences(FILE_NAME, context.MODE_PRIVATE);
        SharedPreferences.Editor prefsEditor = getPreferences(context).edit();
        prefsEditor.putString("iv", stringIv);
        prefsEditor.apply();
    }

    public byte[] getIv(Context context) {
        mPrefs = context.getSharedPreferences(FILE_NAME, context.MODE_PRIVATE);
        String stringIv = getPreferences(context).getString("iv", "");
        byte[] iv = stringIv.getBytes(Charset.forName("ISO-8859-1"));
        return iv;
    }


}