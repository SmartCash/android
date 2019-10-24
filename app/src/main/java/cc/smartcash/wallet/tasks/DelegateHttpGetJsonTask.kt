package cc.smartcash.wallet.tasks


interface DelegateHttpGetJsonTask<T> {

    fun onPreLoadTask()

    fun onResponseReceived(result: T)

}
