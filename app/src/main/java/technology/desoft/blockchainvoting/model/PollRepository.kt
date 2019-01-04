package technology.desoft.blockchainvoting.model

import kotlinx.coroutines.Deferred
import kotlinx.coroutines.Job

interface PollRepository {
    fun getPolls(): Deferred<List<Poll>?>
    fun getOptions(pollId: Long): Deferred<List<PollOption>>
    fun removePoll(id: Long): Job
}