package cc.smartcash.smarthub.tasks


interface DelegateHttpGetJsonTask<T> {

    fun onPreLoadTask()

    fun onResponseReceived(result: T)

}
