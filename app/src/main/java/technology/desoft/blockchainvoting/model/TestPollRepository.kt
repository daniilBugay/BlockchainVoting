package technology.desoft.blockchainvoting.model

import kotlinx.coroutines.*
import technology.desoft.blockchainvoting.model.network.polls.*
import technology.desoft.blockchainvoting.model.network.user.Token
import java.util.*

class TestPollRepository: PollRepository {

    override fun setToken(token: Token) {

    }

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
                    Calendar.getInstance().time,
                    Date(Calendar.getInstance().timeInMillis + random.nextLong() % 1000 * 60 * 60 * 24 * 10 - 1000 * 60 * 60 * 24 * 5),
                    0
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
                    pollId
                )
            }
        }
    }

    override fun createPoll(createPollView: CreatePollView, pollOptions: List<CreatePollOptionView>): Job {
        return GlobalScope.launch {  }
    }
}