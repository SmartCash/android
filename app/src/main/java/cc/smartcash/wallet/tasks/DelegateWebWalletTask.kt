package cc.smartcash.wallet.tasks

interface DelegateWebWalletTask<T> {

    fun beforeExecution(): Void

    fun afterExecution(result: T): Void

}

