package technology.desoft.blockchainvoting.model

import kotlinx.coroutines.*
import java.util.*

class TestPollRepository: PollRepository {

    override fun removePoll(id: Long): Job {
        return GlobalScope.launch{}
    }

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
                    pollId * 100 + it.toLong(),
                    "Poll#$pollId: Option $it",
                    Calendar.getInstance().timeInMillis,
                    pollId,
                    0
                )
            }
        }
    }
}