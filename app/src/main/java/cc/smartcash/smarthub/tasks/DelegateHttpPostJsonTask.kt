package cc.smartcash.smarthub.tasks

import org.json.JSONArray
import org.json.JSONException

import java.io.IOException

/**
 * Created by enrique.soares on 25/08/2016.
 */
interface DelegateHttpPostJsonTask {

    fun onPreLoadTask()

    @Throws(IOException::class, JSONException::class)
    fun onResponseReceived(result: JSONArray)

}
