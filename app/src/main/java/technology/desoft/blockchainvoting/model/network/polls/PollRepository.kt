package technology.desoft.blockchainvoting.model.network.polls

import kotlinx.coroutines.Deferred
import kotlinx.coroutines.Job
import technology.desoft.blockchainvoting.model.network.user.Token

interface PollRepository {
    fun setToken(token: Token)
    fun getPolls(): Deferred<List<Poll>?>
    fun getOptions(pollId: Long): Deferred<List<PollOption>?>
    fun removePoll(id: Long): Job
}