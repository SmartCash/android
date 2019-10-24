package cc.smartcash.wallet.tasks

import cc.smartcash.wallet.Models.Coin
import java.util.*

interface DelegatePriceTask {

    fun beforeExecution()

    fun afterExecution(result: ArrayList<Coin>?)

}

