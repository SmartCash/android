package cc.smartcash.smarthub.tasks

import android.os.AsyncTask
import android.util.Log
import okhttp3.MediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody
import org.json.JSONArray
import org.json.JSONException
import java.io.IOException
import java.util.concurrent.TimeUnit

abstract class HttpPostJsonTask : AsyncTask<String, Void, JSONArray>(), DelegateHttpPostJsonTask {
    private var json: String? = null

    override fun doInBackground(vararg urls: String): JSONArray? {
        try {
            return postJSON(urls[0])
        } catch (e: IOException) {
            e.printStackTrace()
        }

        return null
    }

    @Throws(IOException::class)
    private fun postJSON(url: String): JSONArray? {

        val client: OkHttpClient

        try {
            //client = new OkHttpClient();
            client = OkHttpClient.Builder()
                    .connectTimeout(60, TimeUnit.SECONDS)
                    .writeTimeout(60, TimeUnit.SECONDS)
                    .readTimeout(60, TimeUnit.SECONDS)
                    .build()

            val body = RequestBody.create(JSON, json!!)

            val request = Request.Builder()
                    .url(url)
                    .post(body)
                    .build()

            val response = client.newCall(request).execute()
            val r = response.body()!!.string()


            Log.d("POST", r)
            return JSONArray(r)

        } catch (e: JSONException) {
            e.printStackTrace()
        }

        return null
    }

    override fun onPreExecute() {
        onPreLoadTask()
    }

    override fun onPostExecute(`object`: JSONArray) {
        onResponseReceived(`object`)
    }

    abstract override fun onResponseReceived(result: JSONArray)

    abstract override fun onPreLoadTask()

    companion object {


        val JSON = MediaType.parse("application/json; charset=utf-8")
    }
}
