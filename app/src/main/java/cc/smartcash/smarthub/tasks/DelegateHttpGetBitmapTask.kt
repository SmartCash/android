package cc.smartcash.smarthub.tasks

import android.graphics.Bitmap

interface DelegateHttpGetBitmapTask {

    fun onPreExecuteTask()

    fun onResponseReceived(result: List<Bitmap>)

}
