package cc.smartcash.wallet.tasks

import cc.smartcash.wallet.Models.User


interface DelegateLoginTaskTask {

    fun beforeExecution()

    fun afterExecution(result: User?)

}
