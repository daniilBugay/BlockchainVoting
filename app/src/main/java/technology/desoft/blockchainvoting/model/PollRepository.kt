package technology.desoft.blockchainvoting.model

import kotlinx.coroutines.Deferred

interface PollRepository {
    fun getPolls(): Deferred<List<Poll>?>
    fun getOptions(pollId: Long): Deferred<List<PollOption>>
}