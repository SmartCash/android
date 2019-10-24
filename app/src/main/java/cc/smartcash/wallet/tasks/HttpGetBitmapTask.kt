package cc.smartcash.wallet.tasks

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.AsyncTask
import okhttp3.OkHttpClient
import okhttp3.Request
import java.io.IOException
import java.util.*

abstract class HttpGetBitmapTask<T> : AsyncTask<String, Void, List<Bitmap>>(), DelegateHttpGetBitmapTask {

    private val client = OkHttpClient()

    override fun doInBackground(vararg urls: String): List<Bitmap> {
        val images = ArrayList<Bitmap>()
        for (url in urls) {
            try {
                images.add(downloadImage(url))
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
        return images
    }

    @Throws(IOException::class)
    private fun downloadImage(url: String): Bitmap {
        val request = Request.Builder()
                .url(url)
                .build()

        client.newCall(request).execute().use { response ->

            val inputStream = response.body()!!.byteStream()
            return BitmapFactory.decodeStream(inputStream)
        }
    }

    override fun onPreExecute() {
        onPreExecuteTask()
    }

    override fun onPostExecute(bitmaps: List<Bitmap>) {
        onResponseReceived(bitmaps)
    }

    abstract override fun onResponseReceived(result: List<Bitmap>)

    abstract override fun onPreExecuteTask()
}
