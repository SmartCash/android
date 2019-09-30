package cc.smartcash.wallet.Utils;

import android.app.Application;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.Toast;

import com.google.crypto.tink.Aead;
import com.google.crypto.tink.Config;
import com.google.crypto.tink.KeysetHandle;
import com.google.crypto.tink.aead.AeadKeyTemplates;
import com.google.crypto.tink.config.TinkConfig;
import com.google.crypto.tink.integration.android.AndroidKeysetManager;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.security.GeneralSecurityException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import cc.smartcash.wallet.Models.Coin;
import cc.smartcash.wallet.Models.User;
import cc.smartcash.wallet.Models.Wallet;

public class SmartCashApplication extends Application {

    private static SharedPreferences mPrefs;
    private static ClipboardManager clipboardManager;
    private Gson gson = new Gson();
    private static final String TAG = SmartCashApplication.class.toString();
    private static final String PREF_FILE_NAME = "smartcash_wallet";

    private static final String TINK_KEYSET_NAME = "smartcash_wallet_keyset";
    private static final String MASTER_KEY_URI = "android-keystore://smartcash_wallet_master_key";
    public Aead aead;
    private Context context;

    static SharedPreferences getPreferences(Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context);
    }

    public static void copyToClipboard(Context context, String text) {
        clipboardManager = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
        ClipData clipData = ClipData.newPlainText(text, text);
        clipboardManager.setPrimaryClip(clipData);

        Toast.makeText(context, "Copied!", Toast.LENGTH_SHORT).show();
    }

    public static String getIPAddress(boolean useIPv4) {
        try {
            List<NetworkInterface> interfaces = Collections.list(NetworkInterface.getNetworkInterfaces());
            for (NetworkInterface networkInterface : interfaces) {
                List<InetAddress> networkAddresses = Collections.list(networkInterface.getInetAddresses());
                for (InetAddress inetAddress : networkAddresses) {
                    if (!inetAddress.isLoopbackAddress()) {
                        String stringAddress = inetAddress.getHostAddress();

                        boolean isIPv4 = stringAddress.indexOf(':') < 0;

                        if (useIPv4) {
                            if (isIPv4)
                                return stringAddress;
                        } else {
                            if (!isIPv4) {
                                int delimiter = stringAddress.indexOf('%'); // drop ip6 zone suffix
                                return delimiter < 0 ? stringAddress.toUpperCase() : stringAddress.substring(0, delimiter).toUpperCase();
                            }
                        }
                    }
                }
            }
        } catch (Exception ignored) {
        } // for now eat exceptions
        return "";
    }

    public static ArrayList<Coin> convertToArrayList(String string) {
        String newPrices;
        ArrayList<String> arrayListStrings;
        ArrayList<Coin> coins = new ArrayList<>();

        newPrices = string.replace("{", "").replace("}", "");

        arrayListStrings = new ArrayList<String>(Arrays.asList(newPrices.split(",")));

        for (String element : arrayListStrings) {
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

    public SmartCashApplication(Context context) {
        try {
            this.context = context;
            Config.register(TinkConfig.LATEST);
            aead = getOrGenerateNewKeysetHandle(context).getPrimitive(Aead.class);
        } catch (GeneralSecurityException | IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void saveActualSelectedCoin(Context context, Coin coin) {
        mPrefs = context.getSharedPreferences(Keys.SHARED_PREFERENCES_FILE_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor prefsEditor = getPreferences(context).edit();
        String json = gson.toJson(coin);
        prefsEditor.putString(Keys.KEY_CURRENT_SELECTED_COIN, json);
        prefsEditor.apply();
    }

    public Coin getActualSelectedCoin(Context context) {

        Coin coin = new Coin("USD", (double) 0);
        mPrefs = context.getSharedPreferences(Keys.SHARED_PREFERENCES_FILE_NAME, Context.MODE_PRIVATE);
        String json = getPreferences(context).getString(Keys.KEY_CURRENT_SELECTED_COIN, "");

        if (json != null) {
            Coin coinAux = gson.fromJson(json, Coin.class);
            if (coinAux != null)
                coin = coinAux;
            else
                saveActualSelectedCoin(context, coin);
        }
        return coin;
    }

    public void saveCurrentPrice(Context context, ArrayList<Coin> coins) {
        mPrefs = context.getSharedPreferences(Keys.SHARED_PREFERENCES_FILE_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor prefsEditor = getPreferences(context).edit();
        String json = gson.toJson(coins);
        prefsEditor.putString(Keys.KEY_CURRENT_PRICES, json);
        prefsEditor.apply();
    }

    public ArrayList<Coin> getCurrentPrice(Context context) {
        mPrefs = context.getSharedPreferences(Keys.SHARED_PREFERENCES_FILE_NAME, Context.MODE_PRIVATE);
        String json = getPreferences(context).getString(Keys.KEY_CURRENT_PRICES, "");
        Type type = new TypeToken<ArrayList<Coin>>() {
        }.getType();
        return gson.fromJson(json, type);
    }

    public void saveUser(Context context, User user) {
        mPrefs = context.getSharedPreferences(Keys.SHARED_PREFERENCES_FILE_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor prefsEditor = getPreferences(context).edit();
        String json = gson.toJson(user);
        prefsEditor.putString(Keys.KEY_USER, json);
        prefsEditor.apply();
    }

    public User getUser(Context context) {
        mPrefs = context.getSharedPreferences(Keys.SHARED_PREFERENCES_FILE_NAME, Context.MODE_PRIVATE);
        String json = getPreferences(context).getString(Keys.KEY_USER, "");
        User user = gson.fromJson(json, User.class);
        return user;
    }

    public void saveBoolean(Context context, Boolean bool, String key) {
        mPrefs = context.getSharedPreferences(Keys.SHARED_PREFERENCES_FILE_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor prefsEditor = getPreferences(context).edit();
        prefsEditor.putBoolean(key, bool);
        prefsEditor.apply();
    }

    public Boolean getBoolean(Context context, String key) {
        mPrefs = context.getSharedPreferences(Keys.SHARED_PREFERENCES_FILE_NAME, Context.MODE_PRIVATE);
        Boolean bool = getPreferences(context).getBoolean(key, false);
        return bool;
    }

    public String converterValue(double amount, double value) {
        return String.format("%f", (amount * value));
    }

    public BigDecimal converterBigDecimal(BigDecimal amount, BigDecimal value) {
        return amount.multiply(value);
    }

    public void saveWallet(Context context, Wallet wallet) {
        mPrefs = context.getSharedPreferences(Keys.SHARED_PREFERENCES_FILE_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor prefsEditor = getPreferences(context).edit();
        String json = gson.toJson(wallet);
        prefsEditor.putString(Keys.KEY_WALLET, json);
        prefsEditor.apply();
    }

    public Wallet getWallet(Context context) {
        mPrefs = context.getSharedPreferences(Keys.SHARED_PREFERENCES_FILE_NAME, Context.MODE_PRIVATE);
        String json = getPreferences(context).getString(Keys.KEY_WALLET, "");
        Wallet wallet = gson.fromJson(json, Wallet.class);
        return wallet;
    }

    public void saveToken(Context context, String token) {
        mPrefs = context.getSharedPreferences(Keys.SHARED_PREFERENCES_FILE_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor prefsEditor = getPreferences(context).edit();
        prefsEditor.putString(Keys.KEY_TOKEN, token);
        prefsEditor.apply();
    }

    public String getToken(Context context) {
        mPrefs = context.getSharedPreferences(Keys.SHARED_PREFERENCES_FILE_NAME, Context.MODE_PRIVATE);
        String token = getPreferences(context).getString(Keys.KEY_TOKEN, "");
        return token;
    }

    public void deleteSharedPreferences(Context context) {
        mPrefs = context.getSharedPreferences(Keys.SHARED_PREFERENCES_FILE_NAME, Context.MODE_PRIVATE);
        getPreferences(context).edit().clear().apply();
    }

    public void deleteMSK() {
        this.context.getSharedPreferences(Keys.KEY_MSK, Context.MODE_PRIVATE).edit().remove(Keys.KEY_MSK).commit();
    }

    public void saveMSK(byte[] bytes) {
        String string = new String(bytes, Charset.forName("ISO-8859-1"));
        this.context.getSharedPreferences(Keys.KEY_MSK, Context.MODE_PRIVATE).edit().putString(Keys.KEY_MSK, string).apply();
    }

    public byte[] getMSK() {
        String string = context.getSharedPreferences(Keys.KEY_MSK, Context.MODE_PRIVATE).getString(Keys.KEY_MSK, "");
        if (string != null && !string.isEmpty())
            return string.getBytes(Charset.forName("ISO-8859-1"));
        return null;
    }

    public String getDecryptedMSK(String pin) {

        String decryptedText = "";
        byte[] encryptedPassword = this.getMSK();

        if (encryptedPassword != null) {

            try {

                aead = getOrGenerateNewKeysetHandle(context).getPrimitive(Aead.class);

                byte[] decryptedPin = aead.decrypt(encryptedPassword, pin.getBytes(StandardCharsets.UTF_8));

                decryptedText = new String(decryptedPin, StandardCharsets.UTF_8);


            } catch (Exception ex) {
                decryptedText = "";
                Log.e(TAG, ex.getMessage());
            }
        }
        return decryptedText;
    }

    public void saveByte(byte[] bytes, Context context, String key) {
        String string = new String(bytes, Charset.forName("ISO-8859-1"));

        mPrefs = context.getSharedPreferences(Keys.SHARED_PREFERENCES_FILE_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor prefsEditor = getPreferences(context).edit();
        prefsEditor.putString(key, string);
        prefsEditor.apply();
    }

    public byte[] getByte(Context context, String key) {
        mPrefs = context.getSharedPreferences(Keys.SHARED_PREFERENCES_FILE_NAME, Context.MODE_PRIVATE);
        String string = getPreferences(context).getString(key, "");

        byte[] bytes;

        if (string == null || string == "")
            return null;
        else {
            bytes = string.getBytes(Charset.forName("ISO-8859-1"));
            return bytes;
        }
    }

    public KeysetHandle getOrGenerateNewKeysetHandle(Context context) throws IOException, GeneralSecurityException {
        return new AndroidKeysetManager.Builder()
                .withSharedPref(context, TINK_KEYSET_NAME, PREF_FILE_NAME)
                .withKeyTemplate(AeadKeyTemplates.AES256_GCM)
                .withMasterKeyUri(MASTER_KEY_URI)
                .build()
                .getKeysetHandle();
    }

    public String getDecryptedPassword(Context context, String pin) {

        String decryptedText = "";
        byte[] encryptedPassword = this.getByte(context, Keys.KEY_PASSWORD);

        if (encryptedPassword != null) {

            try {

                aead = getOrGenerateNewKeysetHandle(context).getPrimitive(Aead.class);

                byte[] decryptedPin = aead.decrypt(encryptedPassword, pin.getBytes(StandardCharsets.UTF_8));

                decryptedText = new String(decryptedPin, StandardCharsets.UTF_8);


            } catch (Exception ex) {
                decryptedText = "";
                Log.e(TAG, ex.getMessage());
            }
        }
        return decryptedText;
    }
}