package technology.desoft.blockchainvoting.model

import kotlinx.coroutines.Deferred
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.delay
import java.util.*

class TestPollRepository: PollRepository {
    override fun getPolls(): Deferred<List<Poll>?> {
        return GlobalScope.async {
            delay(1500)
            List(10){
                Poll(
                    it.toLong(),
                    "Theme $it",
                    "Desc $it",
                    Calendar.getInstance().timeInMillis,
                    0,
                    0,
                    0
                )
            }
        }
    }
}