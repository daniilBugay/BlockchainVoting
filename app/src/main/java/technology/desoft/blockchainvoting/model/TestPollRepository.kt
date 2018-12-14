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
            val random = Random()
            List(10){
                Poll(
                    it.toLong(),
                    "Theme $it",
                    "Desc $it",
                    Calendar.getInstance().timeInMillis,
                    Calendar.getInstance().timeInMillis + random.nextLong() % 1000*60*60*24*10 - 1000*60*60*24*5,
                    0,
                    it.toLong()
                )
            }
        }
    }

    override fun getOptions(pollId: Long): Deferred<List<PollOption>> {
        return GlobalScope.async {
            List(5){
                PollOption(
                    it.toLong(),
                    "Poll#$pollId: Option $it",
                    Calendar.getInstance().timeInMillis,
                    pollId,
                    0
                )
            }
        }
    }
}