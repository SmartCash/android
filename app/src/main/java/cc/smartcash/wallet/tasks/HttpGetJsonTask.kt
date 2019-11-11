package cc.smartcash.wallet.tasks

import android.content.Context
import android.os.AsyncTask
import android.util.Log
import cc.smartcash.wallet.utils.SmartCashApplication
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import okhttp3.OkHttpClient
import okhttp3.Request
import org.json.JSONException
import java.io.IOException
import java.util.concurrent.TimeUnit


abstract class HttpGetJsonTask<T>(private val context: Context) : AsyncTask<String, Void, T>(), DelegateHttpGetJsonTask<T> {

    private var db: SmartCashApplication? = null

    init {

        this.db = SmartCashApplication(context)
    }

    override fun doInBackground(vararg urls: String): T? {
        try {
            val fromDb = getFromDb(urls[0])
            return fromDb ?: getJSON(urls[0])
        } catch (e: IOException) {
            e.printStackTrace()
        } finally {

        }
        return null
    }

    private fun getFromDb(url: String): T? {
        try {
            return SmartCashApplication.SharedEditor<T>().get(db?.mPrefs!!, url)
        } catch (e: JSONException) {
            e.printStackTrace()
        }
        return null
    }

    private fun setToDb(json: String, url: String) {
        try {
            db?.saveString(json, url)
        } catch (ex: Exception) {
            ex.printStackTrace()
            Log.d("SET_TO_DB_ERROR", ex.message)
        }
    }

    @Throws(IOException::class)
    private fun getJSON(url: String): T? {
        val client: OkHttpClient
        try {
            client = OkHttpClient.Builder()
                    .connectTimeout(60, TimeUnit.SECONDS)
                    .writeTimeout(60, TimeUnit.SECONDS)
                    .readTimeout(60, TimeUnit.SECONDS)
                    .build()

            val request = Request.Builder()
                    .url(url)
                    .build()

            val response = client.newCall(request).execute()
            val r = response.body()!!.string()
            this.setToDb(r, url)
            return Gson().fromJson(r, object : TypeToken<T>() {}.type)
        } catch (e: JSONException) {
            e.printStackTrace()
        }

        return null
    }

    override fun onPreExecute() {
        onPreLoadTask()
    }

    override fun onPostExecute(`object`: T) {
        onResponseReceived(`object`)
    }

    abstract override fun onResponseReceived(result: T)

    abstract override fun onPreLoadTask()
}
